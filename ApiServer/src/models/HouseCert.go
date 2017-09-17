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
*		err - error info
 */
func GetHouseCertHist(hid, uid int64) (err error, hcs []commdef.HouseCert) {
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
	// 	1. landlord, house agency and administrator could get the house certification hist
	//	2. landload could see the private info of himeself and agency and administrator, like phone number
	//	3. house agency and administrator could see the private info
	//	4.
	if err = canAccessHouse(uid, hid); nil != err {
		return
	}

	// canModifyHouse(uid, hid)
	// if isHouseAgency(h, uid) {
	// } else if _, bAdmin := isAdministrator(uid); bAdmin {
	// } else {
	// 	err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("uid:%d", uid)}
	// 	return
	// }

	// Processing
	o := orm.NewOrm()

	sql := `SELECT c.id, c.who AS uid, u.name AS user, u.phone, c.when AS time, c.cert_statu AS stat, c.comment AS cert_txt
				FROM tbl_house_cert AS c, tbl_user AS u  
				WHERE c.who=u.id AND house=?`

	cs := []commdef.HouseCert{}
	errT := o.Raw(sql, hid).QueryRow(&cs)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	for k, v := range cs {
		v.Uid = 0
	}

	// qs := o.QueryTable("tbl_house_cert").Filter("House", hid).OrderBy("-Id")

	// cs := []TblHouseCert{}
	// numb, errT := qs.All(&cs)
	// if nil != errT {
	// 	err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
	// 	return
	// }
	// beego.Debug(Fn, fmt.Sprintf("%d records found", numb))

	// for k, v := range cs {

	// }

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
	err, _ = addMessage(commdef.MSG_HouseCertification, pri, hc_id, h.Owner.Id, msg)
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
