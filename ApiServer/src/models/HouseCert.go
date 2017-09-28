package models

import (
	"ApiServer/commdef"
	"fmt"
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/orm"
	// "strconv"
	// "strings"
	"time"
)

/**
*	Get house certification history
*	Arguments:
*		hid 	- house id
*		uid		- login user who made the certification
*	Returns
*		err 	- error info
*		hcs		- house certification list
*		ops		- valid operations
 */
func GetHouseCertHist(hid, uid int64) (err error, hcs []commdef.HouseCert, ops int) {
	Fn := "[CertHouse] "
	beego.Trace(Fn, "house:", hid, ", login user:", uid)

	defer func() {
		if nil != err {
			beego.Error(Fn, err)
		}
	}()

	/*	argument checking */
	err, h := getHouse(hid)
	if nil != err {
		return
	}

	if err, _ = GetUser(uid); nil != err {
		return
	}

	/* Permission checking */
	// Only the person who can SEE the house info could get the certificate history of the house
	// include landlord, current agency and administrator
	if err = canAccessHouse(uid, hid); nil != err {
		return
	}

	// Processing
	o := orm.NewOrm()

	sql := `SELECT c.id, c.who AS uid, u.name AS user, u.login_name AS phone, c.when AS time, c.cert_statu AS stat, c.comment AS cert_txt
				FROM tbl_house_cert AS c, tbl_user AS u  
				WHERE c.who=u.id AND house=? ORDER BY c.id DESC`

	cs := []commdef.HouseCert{}
	_, errT := o.Raw(sql, hid).QueryRows(&cs)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	// Only the landlord and agency(including house agency, other agency, admin) could go here
	bLoginLandlord := isHouseOwner(h, uid)
	beego.Debug(Fn, "login user is landlord?", bLoginLandlord)

	for k, v := range cs {
		// beego.Debug(Fn, fmt.Sprintf("v:%+v", v))
		if 0 == k {
			_, ops = getHouseCertOps(uid, &h, &v)
		}
		// there are 3 kind of roles in hist records: landlord, ex-landlord, agency(including administrator)
		// landlord could see the agency's phone number
		// agency(including admin) could see the current landlord's phone number
		bHidePrivateInfo := false
		if v.Uid == uid { // login user himself, do not need to see phone number, no matter who he is, landlord or agency
			bHidePrivateInfo = true
		} else {
			if bLoginLandlord { // login user is current landlord
				_, bAgency := isAgency(v.Uid)
				_, bAdmin := isAdministrator(v.Uid)
				if !bAgency && !bAdmin {
					bHidePrivateInfo = true
				}
			} else { // login user is agency who can see this house
				// if isHouseOwner(h, v.Uid) {
				// 	bHidePrivateInfo = true
				// }
			}
		}
		// bHidePrivateInfo = true
		// beego.Debug(Fn, "bHidePrivateInfo:", bHidePrivateInfo)

		if bHidePrivateInfo { // hide the privecy info
			v.Uid = 0
			v.Phone = ""
		}
		beego.Debug(Fn, fmt.Sprintf("v:%+v", v))
		hcs = append(hcs, v)
	}

	return
}

/**
*	Certify House
*	Arguments:
*		hid 	- house id
*		uid		- login user who made the certification
*		pass	- certificate result, pass or not
*		comment	- certificate comment
*	Returns
*		err - error info
 */
func CertHouse(hid, uid int64, pass bool, comment string) (err error) {
	FN := "[CertHouse] "
	beego.Trace(FN, "house:", hid, ", login user:", uid, ", pass:", pass, ", comment:", comment)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/*	argument checking */
	if 0 == len(comment) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: "No comments"}
		return
	}

	err, h := getHouse(hid)
	if nil != err {
		return
	}

	// beego.Debug(FN, fmt.Sprintf("%+v", h))
	nullTime := time.Time{}
	// beego.Debug(FN, "publish time:", interface{}(h.PublishTime), ", is null:", nullTime == h.PublishTime)
	if pass && nullTime != h.PublishTime {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: "already published"}
		return
	}

	/* Permission checking */
	// Only the house agency and administrator could certificate house
	if isHouseAgency(h, uid) {
	} else if _, bAdmin := isAdministrator(uid); bAdmin {
	} else {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("uid:%d", uid)}
		return
	}

	// Processing
	o := orm.NewOrm()
	o.Begin()
	defer func() {
		if nil != err {
			o.Rollback()
		} else {
			o.Commit()
		}
	}()

	// the last record should be HOUSE_CERT_STAT_WAIT
	hc, err := getHouseNewestCert(hid)
	if nil != err {
		return
	}
	if commdef.HOUSE_CERT_STAT_WAIT != hc.CertStatu {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("incorrect status:%d", hc.CertStatu)}
		return
	}

	// add new record in TblHouseCert
	hc = TblHouseCert{House: hid, Who: uid, Comment: comment /*, Pass: pass*/}
	if pass {
		hc.CertStatu = commdef.HOUSE_CERT_STAT_PASSED
	} else {
		hc.CertStatu = commdef.HOUSE_CERT_STAT_FAILED
	}
	hc_id, errT := o.Insert(&hc)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	sql := ""
	if pass {
		// Update & publish
		tPublish := time.Now() //.UTC()
		publishTime := fmt.Sprintf("%d-%d-%d %d:%d:%d", tPublish.Year(), tPublish.Month(), tPublish.Day(), tPublish.Hour(), tPublish.Minute(), tPublish.Second())
		sql = fmt.Sprintf(`UPDATE tbl_house SET publish_time='%s' WHERE id=%d`, publishTime, hid)
	} else {
		// revoke the publish
		sql = fmt.Sprintf(`UPDATE tbl_house SET publish_time=NULL WHERE id=%d`, hid)
	}
	res, errT := o.Raw(sql).Exec()
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}
	numb, _ := res.RowsAffected()
	beego.Debug(FN, "affect", numb, "records")

	// create a system message to notify the landlord
	pri := commdef.MSG_PRIORITY_Info
	msg := "审核通过"
	if !pass {
		pri = commdef.MSG_PRIORITY_Error
		msg = "审核未通过"
	}
	err, _ = addMessage(commdef.MSG_HouseCertification, pri, hc_id, h.Owner.Id, msg, o)
	if nil != err {

	}

	return
}

/*********************************************************************************************************
*
*	Internal Functions
*
**********************************************************************************************************/
func addHouseCertRec(hid, uid int64, cs int, cc string) (err error, cid int64) {
	Fn := "[addHouseCertRec] "
	beego.Info(Fn, fmt.Sprintf("login user:%d, house:%d, cert status:%d, comment:%s", uid, hid, cs, cc))

	/* Argument checking */

	/* Permission checking */

	/* Processing */
	o := orm.NewOrm()

	// add new record in TblHouseCert
	// comment := "业主提交新房源"
	// hc := TblHouseCert{House: hid, Who: uid, Comment: cc, CertStatu: cs}
	hc_id, errT := o.Insert(&TblHouseCert{House: hid, Who: uid, Comment: cc, CertStatu: cs})
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	cid = hc_id
	return
}

func removeHouseCertRecByHose(hid int64) (err error, numb int64, hcs []TblHouseCert) {
	Fn := "[removeHouseCertRecByHose] "
	beego.Info(Fn, "house:", hid)

	/* Argument checking */

	/* Permission checking */

	/* Processing */
	o := orm.NewOrm()
	// tbl_house_cert
	qs := o.QueryTable("tbl_house_cert").Filter("House", hid)
	n, errT := qs.All(&hcs)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}
	n, errT = qs.Delete()
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	numb = n
	beego.Debug(Fn, fmt.Sprintf("delete %d records from TblHouseCert", numb))
	return
}

func getHouseNewestCert(hid int64) (hc TblHouseCert, err error) {
	Fn := "[getHouseNewestCert] "
	beego.Info(Fn, "House:", hid)

	if hid <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("house:", hid)}
		return
	}

	o := orm.NewOrm()
	qs := o.QueryTable("tbl_house_cert").Filter("House", hid).OrderBy("-Id").Limit(1, 0)
	if errT := qs.One(&hc); nil != errT {
		if orm.ErrNoRows != errT && orm.ErrMissPK != errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		} else {
			// no certificate record found
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_RES_NOTFOUND, ErrInfo: fmt.Sprintf("house:", hid)}
		}
		return
	}

	return
}

func getHouseCertOps(uid int64, h *TblHouse, hc *commdef.HouseCert) (err error, ops int) {
	Fn := "[getHouseCertOps] "
	// beego.Info(Fn, "uid:")

	bLandlord := isHouseOwner(*h, uid)
	bAgency := isHouseAgency(*h, uid)
	_, bAdmin := isAdministrator(uid)
	beego.Debug(Fn, "bLandlord:", bLandlord, ", bAgency:", bAgency, ", bAdmin:", bAdmin)

	switch hc.Stat {
	case commdef.HOUSE_CERT_STAT_WAIT:
		if bLandlord {
		} else if bAgency || bAdmin {
			ops = commdef.HOUSE_COP_Certify
		}
	case commdef.HOUSE_CERT_STAT_PASSED:
		if bLandlord {
		} else if bAgency || bAdmin {
			ops = commdef.HOUSE_COP_Revoke
		}
	case commdef.HOUSE_CERT_STAT_FAILED:
		if bLandlord {
			ops = commdef.HOUSE_COP_Recommit
		} else if bAgency || bAdmin {
		}
	}
	beego.Debug(Fn, "ops:", ops)

	return
}
