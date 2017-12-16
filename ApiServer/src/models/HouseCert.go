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

	/* Processing */
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
*	Recommit the house certification request
*	Arguments:
*		hid 	- house id
*		uid		- login user
*		comment	- certificate comment
*	Returns
*		err - error info
 */
func RecommitHouseCert(hid, uid int64, comment string) (err error, hcid int64) {
	FN := "[RecommitHouseCert] "
	beego.Trace(FN, "house:", hid, ", login user:", uid, ", comment:", comment)

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

	if nilTime != h.PublishTime {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: "already published"}
		return
	}

	if err, _ = GetUser(uid); nil != err {
		return
	}

	/* Permission checking */
	// Only the landlord could recommit the certification request
	if !isHouseOwner(h, uid) {
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

	// the last record should be HOUSE_CERT_STAT_FAILED
	hc, err := getHouseNewestCert(hid)
	if nil != err {
		return
	}
	if commdef.HOUSE_CERT_STAT_FAILED != hc.CertStatu {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("incorrect status:%d", hc.CertStatu)}
		return
	}

	// add new record in TblHouseCert
	err, hcid = addHouseCertRec(hid, uid, commdef.HOUSE_CERT_STAT_WAIT, comment, o)
	if nil != err {
		return
	}

	// create a system message to notify the landlord
	// generate a system message to notify the agency or administrator if no agency assigned
	if h.Agency.Id > 0 { // agency assinged
		err, _ = addMessage(commdef.MSG_HouseCertification, commdef.MSG_PRIORITY_Info, hcid, h.Agency.Id, comment, o)
	} else { // no agency assigned
		err, _ = sendMsg2Admin(commdef.MSG_HouseCertification, commdef.MSG_PRIORITY_Warning, hcid, comment, o)
	}
	if nil != err {
		return
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
	// beego.Debug(FN, "publish time:", interface{}(h.PublishTime), ", is null:", nullTime == h.PublishTime)
	if pass && nilTime != h.PublishTime {
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

	/* Processing */
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
	beego.Debug(FN, "CertStatu:", hc.CertStatu)
	if commdef.HOUSE_CERT_STAT_WAIT != hc.CertStatu {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("incorrect status:%d", hc.CertStatu)}
		return
	}

	// add new record in TblHouseCert
	cs := commdef.HOUSE_CERT_STAT_FAILED
	if pass {
		cs = commdef.HOUSE_CERT_STAT_PASSED
	}
	err, hc_id := addHouseCertRec(hid, uid, cs, comment, o)
	if nil != err {
		return
	}

	// Update TblHouse
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

	// Send a system message to info the landlord
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
func addHouseCertRec(hid, uid int64, cs int, cc string, o orm.Ormer) (err error, cid int64) {
	Fn := "[addHouseCertRec] "
	beego.Info(Fn, fmt.Sprintf("login user:%d, house:%d, cert status:%d, comment:%s", uid, hid, cs, cc))

	/* Argument checking */

	/* Permission checking */

	/* Processing */
	// o := orm.NewOrm()

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

func deleHouseCertRec(hcid int64, o orm.Ormer) (err error) {
	Fn := "[deleHouseCertRec] "
	beego.Info(Fn, "house certification:", hcid)

	if nil == o {
		beego.Warn(Fn, "no ormer set, create one")
		o = orm.NewOrm()

		o.Begin()
		defer func() {
			if nil != err {
				o.Rollback()
			} else {
				o.Commit()
			}
		}()
	}

	// TblHouseCert
	numb, errT := o.QueryTable("tbl_house_cert").Filter("Id", hcid).Delete()
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}
	beego.Debug(Fn, fmt.Sprintf("delete %d records from tbl_house_cert", numb))

	// TblMessage
	numb, errT = o.QueryTable("tbl_message").Filter("Type", commdef.MSG_HouseCertification).Filter("RefId", hcid).Delete()
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}
	beego.Debug(Fn, fmt.Sprintf("delete %d records from tbl_message", numb))

	return
}

func removeHouseCertRecByHose(hid int64, o orm.Ormer) (err error /*, numb int64, hcs []TblHouseCert*/) {
	Fn := "[removeHouseCertRecByHose] "
	beego.Info(Fn, "house:", hid)

	/* Argument checking */

	/* Permission checking */

	/* Processing */
	// o := orm.NewOrm()

	// tbl_house_cert
	qs := o.QueryTable("tbl_house_cert").Filter("House", hid)
	hcs := []TblHouseCert{}
	numb, errT := qs.All(&hcs)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}
	numb, errT = qs.Delete()
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}
	beego.Debug(Fn, fmt.Sprintf("delete %d records from TblHouseCert", numb))

	// tbl_message
	if numb > 0 {
		hcids := []int64{}
		for _, v := range hcs {
			hcids = append(hcids, v.Id)
		}
		numb1, errT := o.QueryTable("tbl_message").Filter("Type", commdef.MSG_HouseCertification).Filter("RefId__in", hcids).Delete()
		if nil != errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
			return
		}
		beego.Debug(Fn, fmt.Sprintf("delete %d records from tbl_message", numb1))
	}

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
			// err = commdef.SwError{ErrCode: commdef.ERR_COMMON_RES_NOTFOUND, ErrInfo: fmt.Sprintf("house:%d", hid)}
			// backward compatibility
			beego.Debug(Fn, fmt.Sprintf("No certification record for house(%d), insert one", hid))
			h := TblHouse{}
			if err, h = getHouse(hid); nil != err {
				return
			}
			comment := "业主提交新房源"
			if err, _ = addHouseCertRec(hid, h.Owner.Id, commdef.HOUSE_CERT_STAT_WAIT, comment, o); nil != err {
				return
			}
			// fetch again
			if errT := qs.One(&hc); nil != errT {
				if orm.ErrNoRows == errT || orm.ErrMissPK == errT {
					// no certificate record found
					err = commdef.SwError{ErrCode: commdef.ERR_COMMON_RES_NOTFOUND, ErrInfo: fmt.Sprintf("house:%d", hid)}
				}
			}
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
	beego.Debug(Fn, "Stat:", hc.Stat)

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
