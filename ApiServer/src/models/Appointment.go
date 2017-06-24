package models

import (
	"ApiServer/commdef"
	"fmt"
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/orm"
	// "strconv"
	"time"
)

/**
*	Get house order table
*	Arguments:
*		hid 	- house id
*		uid 	- login user
*	Returns
*		err 	- error info
*		hot		- house order table
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
	if apType < commdef.ORDER_TYPE_BEGIN || apType > commdef.ORDER_TYPE_END {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("apType:%d", apType)}
		return
	}
	tBegin, errT := time.Parse("2006-01-02 15:04", time_begin)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("time_begin:%s", time_begin)}
		return
	}
	tEnd, errT := time.Parse("2006-01-02 15:04", time_end)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("time_end:%s", time_end)}
		return
	}
	if 0 == len(desc) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("desc not set")}
		return
	}

	/* Permission checking */
	err1, _ := GetUser(uid)
	if nil != err1 && 11 != len(phone) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("phone:%s", phone)}
		return
	}

	/* Processing */
	o := orm.NewOrm()

	// check if the appointment already exist
	qs := o.QueryTable("tbl_order_table").Filter("OrderType", apType).Filter("House", hid).Filter("Phone", phone)
	if qs.Exist() {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_DUPLICATE, ErrInfo: fmt.Sprintf("appointment already exist")}
		return
	}

	nid, errT := o.Insert(&TblOrderTable{OrderType: apType, House: hid, Phone: phone,
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
*	Get house order table
*	Arguments:
*		hid 	- house id
*		uid 	- login user
*	Returns
*		err 	- error info
*		hot		- house order table
 */
func GetOrderTable(hid, uid int64, begin, fetchCnt int) (err error, total int64, hot []commdef.HouseOrderTable) {
	FN := "[GetOrderTable] "
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
	qs := o.QueryTable("tbl_order_table").Filter("House", hid).Filter("CloseTime__isnull", true)
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

	if 0 == fetchCnt {
		return
	}
	if int64(begin) >= total {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("begin(%d) out of bounds", begin)}
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
	nameUnset := getSpecialString(KEY_USER_NAME_NOT_SET)
	sql := `SELECT o.id, o.house, IF(LENGTH(u.name) > 0, u.name, ?) AS subscriber, subsc_time AS order_time, close_time 
				FROM tbl_order_table AS o, tbl_user AS u 
				WHERE o.subscriber=u.id AND close_time IS NULL AND house=?
				LIMIT ?, ?`

	ot := []commdef.HouseOrderTable{}
	_, errT = o.Raw(sql, nameUnset, hid, begin, fetchCnt).QueryRows(&ot)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("Fail to get house order table, err:%s", errT.Error())}
		return
	}
	// beego.Debug(FN, fmt.Sprintf("ot:%+v", ot))

	hot = ot
	return
}
