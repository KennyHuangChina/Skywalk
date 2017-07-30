package models

import (
	"ApiServer/commdef"
	"fmt"
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/orm"
	"strconv"
	"time"
)

/**
*	Get house list of appointment of house see
*	Arguments:
*		begin	- from which item to fetch
*		tofetch	- how many items to fetch
*		uid		- login user
*	Returns
*		err 	- error info
*		total 	- total number
*		fetched	- fetched quantity
*		hids	- house id list
**/
func GetHouseList_AppointSee(begin, tofetch, uid int64) (err error, total, fetched int64, hids []int64) {
	FN := "[GetHouseList_AppointSee] "
	beego.Trace(FN, "fetch:(", begin, ",", tofetch, "), login user:", uid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	fetched = -1

	/* Argeuments checking */
	if begin < 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("begin:%d", begin)}
		return
	}
	if tofetch < 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("tofetch:%d", tofetch)}
		return
	}

	/* Permission checking */
	err, u := GetUser(uid)
	if nil != err {
		beego.Error(FN, "err:", err)
		return
	}

	/* Processing */
	o := orm.NewOrm()

	sqlQuery := " FROM tbl_appointment WHERE subscriber=? OR phone=?"
	sql := "SELECT COUNT(*) AS count" + sqlQuery
	count := int64(0)
	if errT := o.Raw(sql, uid, u.LoginName).QueryRow(&count); nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("error:%s", errT.Error())}
		return
	}
	beego.Debug(FN, "count:", count)

	total = count
	if 0 == tofetch || 0 == total {
		return
	}

	if begin >= total {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("begin(%d) over the range(%d)", begin, total)}
		return
	}

	sql = "SELECT house " + sqlQuery + " LIMIT ?, ?"
	ids := []int64{}
	numb, errT := o.Raw(sql, uid, u.LoginName, begin, tofetch).QueryRows(&ids)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("error:%s", errT.Error())}
		return
	}
	fetched = numb
	hids = ids
	beego.Debug(FN, "hids:", hids)

	return
}

func DeleAppointment(aid, uid int64) (err error) {
	FN := "[ DeleAppointment] "
	beego.Trace(FN, "appointment:", aid, ", login user:", uid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/* Argeuments checking */
	beego.Warn(FN, "TODO: ")

	/* Permission checking */

	/* Processing */
	o := orm.NewOrm()
	numb, errT := o.Delete(&TblAppointment{Id: aid})
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("err:%s", errT.Error())}
		return
	}
	beego.Debug(FN, fmt.Sprintf("%d records deleted", numb))

	return
}

/**
*	Get house order table
*	Arguments:
*		hid 		- house id
*		uid 		- login user
*		apType		- appointment type, ref to ORDER_TYPE_XXXX
*		phone		- contace phone number, if user not login, this field must be filled
*		time_begin	- the period begin time user expected
*		time_end	- the period end time user expected
*		desc		- appointment description
*	Returns
*		err 	- error info
*		aid		- new appointment id
 */
func MakeAppointment(hid, uid int64, apType int, phone, time_begin, time_end, desc string) (err error, aid int64) {
	FN := "[MakeAppointment] "
	beego.Trace(FN, "house:", hid, ", login user:", uid, ", type:", apType,
		fmt.Sprintf(", period(%s - %s)", time_begin, time_end), ", desc:", desc)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/* Argeuments checking */
	if 0 != hid {
		if err, _ = getHousePublished(hid); nil != err {
			return
		}
	}
	if apType < commdef.ORDER_TYPE_BEGIN || apType > commdef.ORDER_TYPE_END {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("apType:%d", apType)}
		return
	}
	tBegin, errT := time.Parse("2006-01-02 15:04", time_begin)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("time_begin:%s, %s", time_begin, errT.Error())}
		return
	}
	tEnd, errT := time.Parse("2006-01-02 15:04", time_end)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("time_end:%s, %s", time_end, errT.Error())}
		return
	}
	if tBegin.After(tEnd) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("time_end:%s early than time_begin:%s", time_end, time_begin)}
		return
	}
	if 0 == len(desc) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("desc not set")}
		return
	}
	err1, _ := GetUser(uid)
	if nil != err1 {
		if 11 != len(phone) {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("phone:%s", phone)}
			return
		}

		nPhoneNumb, errT := strconv.ParseInt(phone, 10, 64)
		if nil != errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("phone:%s %s", phone, errT.Error())}
			return
		}
		beego.Debug(FN, "nPhoneNumb:", nPhoneNumb)
	}

	/* Permission checking */

	/* Processing */
	o := orm.NewOrm()

	// check if the appointment already exist
	qs := o.QueryTable("tbl_appointment").Filter("OrderType", apType).Filter("House", hid).Filter("Phone", phone)
	if qs.Exist() {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_DUPLICATE, ErrInfo: fmt.Sprintf("appointment already exist")}
		return
	}

	nid, errT := o.Insert(&TblAppointment{OrderType: apType, House: hid, Phone: phone,
		ApomtTimeBgn: tBegin, ApomtTimeEnd: tEnd, ApomtDesc: desc})
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("err:%s", errT.Error())}
		return
	}
	beego.Debug(FN, "new appointment:", nid)

	aid = nid
	return
}

/**
*	Get house see appointment list
*	Arguments:
*		hid 	- house id
*		uid 	- login user
*	Returns
*		err 	- error info
*		hot		- house order table
 */
func GetAppointList_SeeHouse(hid, uid int64, begin, fetchCnt int) (err error, total int64, hot []commdef.AppointmentInfo) {
	FN := "[GetAppointList_SeeHouse] "
	beego.Trace(FN, "house:", hid, ", login user:", uid, ", begin:", begin, ", fetchCnt:", fetchCnt)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/* Argeuments checking */
	if begin < 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("begin:%d", begin)}
		return
	}
	if fetchCnt < 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("fetchCnt:%d", fetchCnt)}
		return
	}

	/* Get total number */
	o := orm.NewOrm()
	qs := o.QueryTable("tbl_appointment").Filter("OrderType", commdef.ORDER_TYPE_SEE_APARTMENT).Filter("House", hid).Filter("CloseTime__isnull", true)
	cnt, errT := qs.Count()
	if nil != errT {
		if orm.ErrNoRows != errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("Fail to get house order table, err:%s", errT.Error())}
			return
		} else {
			beego.Debug(FN, fmt.Sprintf("No order table for house(%d) found", hid))
			return
		}
	}
	total = cnt
	beego.Debug(FN, fmt.Sprintf("%d records found", total))

	if 0 == fetchCnt { // user just want to get the total number
		return
	}

	if total > 0 && int64(begin) >= total {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("begin(%d) is out of bounds", begin)}
		return
	}
	if err, _ = GetUser(uid); nil != err { // user not login, can only get the total number
		return
	}

	/* Permission checking */
	if err = canAccessHouse(uid, hid); nil != err {
		return
	}

	/* Get records */
	tp_see_house := getSpecialString(KEY_APPOINTMENT_TYPE_SEE_HOUSE)
	nameUnset := getSpecialString(KEY_USER_NAME_NOT_SET)
	sql := `SELECT o.id, order_type AS apomt_type, IF(order_type = ? , ?, "Unknown") AS type_desc, 
					o.house, o.phone, IF(LENGTH(u.name) > 0, u.name, ?) AS subscriber, o.apomt_time_bgn, 
					o.apomt_time_end, o.apomt_desc, subsc_time AS subscrib_time, close_time 
				FROM tbl_appointment AS o LEFT JOIN tbl_user AS u ON o.subscriber=u.id 
				WHERE close_time IS NULL AND house=?
				LIMIT ?, ?`

	ot := []commdef.AppointmentInfo{}
	_, errT = o.Raw(sql, commdef.ORDER_TYPE_SEE_APARTMENT, tp_see_house, nameUnset, hid, begin, fetchCnt).QueryRows(&ot)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("Fail to get house see appointment list, err:%s", errT.Error())}
		return
	}
	// beego.Debug(FN, fmt.Sprintf("ot:%+v", ot))
	beego.Warn(FN, "TODO: add other type support")

	hot = ot
	return
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
const (
	KEY_APPOINTMENT_TYPE_SEE_HOUSE = "KEY_APPOINTMENT_TYPE_SEE_HOUSE"
)
