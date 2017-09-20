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
	beego.Info(FN, "appointment:", aid, ", login user:", uid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/* Argeuments checking */
	beego.Warn(FN, "TODO: ")

	/* Permission checking */
	// Only the administrator could delet the appointment, for now
	if _, bAdmin := isAdministrator(uid); !bAdmin {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION}
		return
	}

	/* Processing */
	o := orm.NewOrm()

	a := TblAppointment{Id: aid}
	errT := o.Read(&a)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("err:%s", errT.Error())}
		return
	}

	o.Begin()
	defer func() {
		if nil != err {
			o.Rollback()
		} else {
			o.Commit()
		}
	}()

	// Delete in appointment table
	numb, errT := o.Delete(&a)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("err:%s", errT.Error())}
		return
	}
	beego.Debug(FN, fmt.Sprintf(" delete %d records in TblAppointment", numb))

	// Delete in messages
	msgType := int(0) // commdef.MSG_Begin
	switch a.OrderType {
	case commdef.ORDER_TYPE_SEE_HOUSE:
		msgType = commdef.MSG_AppointSeeHouse
	default:
		err = commdef.SwError{ErrCode: commdef.ERR_NOT_IMPLEMENT, ErrInfo: fmt.Sprintf("Unknown appointment type:%d", a.OrderType)}
		return
	}
	err, numb = delMessageByRefId(msgType, aid, o)
	if nil != err {
		return
	}

	return
}

/**
*	make a action for an appointment
*	Arguments:
*		uid 		- login user
*		act			- action type, ref to APPOINT_ACTION_xxxx
*		time_begin	- the period begin time user expected
*		time_end	- the period end time user expected
*		comment		- action comments
*	Returns
*		err 	- error info
*		aid		- new action id
 */
func MakeAppointmentAction(uid, aid int64, act int, time_begin, time_end, comment string) (err error, act_id int64) {
	FN := "[MakeAppointmentAction] "
	beego.Info(FN, "login user:", uid, ", appointment:", aid, ", act:", act,
		fmt.Sprintf(", period(%s - %s)", time_begin, time_end), ", comment:", comment)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/* Argeuments checking */
	// err, apmt := getAppointment(aid)
	// if nil != err {
	// 	return
	// }
	// if commdef.ORDER_TYPE_SEE_HOUSE != apmt.OrderType {
	// 	err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("order type:%d", apmt.OrderType)}
	// 	return
	// }

	/* Permission checking */
	// appointment subscriber, house agency, agency(if house is opened), administrator

	/* Processing */
	o := orm.NewOrm()
	defer func() {
		if nil != err {
			o.Rollback()
		} else {
			o.Commit()
		}
	}()

	err, nact_id := addAppointmentAction(uid, aid, act, time_begin, time_end, comment, o)
	if nil != err {
		return
	}

	act_id = nact_id
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
	beego.Info(FN, "house:", hid, ", login user:", uid, ", type:", apType,
		fmt.Sprintf(", period(%s - %s)", time_begin, time_end), ", desc:", desc)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/* Argeuments checking */
	if 0 != hid { // appointment is related with house
		if err, _ = getHousePublished(hid); nil != err {
			return
		}
	}
	if apType < commdef.ORDER_TYPE_BEGIN || apType > commdef.ORDER_TYPE_END {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("apType:%d", apType)}
		return
	}

	err, tBegin, tEnd := getScheduleTime(time_begin, time_end)
	if nil != err {
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
	// seems everyone could make an appointment

	/* Processing */
	o := orm.NewOrm()

	// check if the appointment already exist
	qs := o.QueryTable("tbl_appointment").Filter("OrderType", apType).Filter("House", hid).Filter("Phone", phone)
	if qs.Exist() {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_DUPLICATE, ErrInfo: fmt.Sprintf("appointment already exist")}
		return
	}

	o.Begin()
	defer func() {
		if nil == err {
			o.Commit()
		} else {
			beego.Error(FN, "failed")
			o.Rollback()
		}
	}()

	// Insert into appointment table
	naid, errT := o.Insert(&TblAppointment{OrderType: apType, House: hid, Phone: phone,
		ApomtTimeBgn: tBegin, ApomtTimeEnd: tEnd, ApomtDesc: desc})
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("err:%s", errT.Error())}
		return
	}
	beego.Debug(FN, "new appointment:", naid)

	// Appointment action. add submit action automatically
	err, _ = addAppointmentAction(uid, aid, commdef.APPOINT_ACTION_Submit, time_begin, time_end, "客人约看", o)
	if nil != err {
		return
	}

	aid = naid
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
	qs := o.QueryTable("tbl_appointment").Filter("OrderType", commdef.ORDER_TYPE_SEE_HOUSE).Filter("House", hid).Filter("CloseTime__isnull", true)
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
	_, errT = o.Raw(sql, commdef.ORDER_TYPE_SEE_HOUSE, tp_see_house, nameUnset, hid, begin, fetchCnt).QueryRows(&ot)
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
//		-- Internal Functions --
//
func getAppointment(aid int64) (err error, apmt TblAppointment) {
	// Fn := "[getAppointment] "

	if aid <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("appointment:%d", aid)}
		return
	}

	o := orm.NewOrm()
	a := TblAppointment{Id: aid}
	errT := o.Read(&a)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("err:%s", errT.Error())}
		return
	}

	apmt = a
	return
}

func getScheduleTime(time_begin, time_end string) (err error, tb, te time.Time) {
	Fn := "[getScheduleTime] "
	beego.Info(Fn, fmt.Sprintf("period: %s - %s", time_begin, time_end))

	if 0 == len(time_begin) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: "invalid time_begin"}
		return
	}
	if 0 == len(time_end) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: "invalid time_end"}
		return
	}

	tBegin, errT := time.Parse("2006-01-02 15:04", time_begin)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("time_begin: %s", errT.Error())}
		return
	}
	tEnd, errT := time.Parse("2006-01-02 15:04", time_end)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("time_end: %s", errT.Error())}
		return
	}
	if tBegin.After(tEnd) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("time_end:%s early than time_begin:%s", time_end, time_begin)}
		return
	}

	tb = tBegin
	te = tEnd
	return
}

func addAppointmentAction(uid, aid int64, act int, time_begin, time_end, comment string, o orm.Ormer) (err error, act_id int64) {
	FN := "[addAppointmentAction] "
	beego.Info(FN, "login user:", uid, ", appointment:", aid, ", act:", act,
		fmt.Sprintf(", period(%s - %s)", time_begin, time_end), ", comment:", comment)

	/* Argeuments checking */
	if err, _ = GetUser(uid); nil != err {
		return
	}

	err, apmt := getAppointment(aid)
	if nil != err {
		return
	}

	if act <= commdef.APPOINT_ACTION_Begin || act > commdef.APPOINT_ACTION_End {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: "invalid action"}
		return
	}

	tBgn, tEnd := time.Time{}, time.Time{}
	if act == commdef.APPOINT_ACTION_Reschedule {
		err, tBgn, tEnd = getScheduleTime(time_begin, time_end)
		if nil != err {
			return
		}
	}

	if 0 == len(comment) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("comment not set")}
		return
	}

	/* Permission checking */
	// appointment subscriber, house agency, agency(if house is opened), administrator

	/* Processing */
	// appointment action table
	nact := TblAppointmentAction{Appoint: aid, Action: act, Who: uid, TimeBgn: tBgn, TimeEnd: tEnd, Comment: comment}
	nact_id, errT := o.Insert(&nact)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("err:%s", errT.Error())}
		return
	}

	// appointment table
	if tBgn != nilTime {
		a := TblAppointment{Id: aid, ApomtTimeBgn: tBgn, ApomtTimeEnd: tEnd}
		_, errT = o.Update(&a, "ApomtTimeBgn", "ApomtTimeEnd")
		if nil != errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("err:%s", errT.Error())}
			return
		}
	}

	// generate system message
	if apmt.House > 0 { // appointment is related with house
		h := TblHouse{}
		if err, h = getHouse(apmt.House); nil != err {
			return
		}

		role := ""
		if uid == apmt.Subscriber {
			role = "客人"
		} else {
			role = "经纪人"
		}

		msgTxt := ""
		msgType := 0
		err, msgTxt, msgType = getMsgTxt(apmt.OrderType, act, role)
		if nil != err {
			return
		}

		// TODO: commdef.MSG_AppointSeeHouse should be changed
		if h.Agency.Id > 0 { // agency assigned
			err, _ = addMessage(msgType, commdef.MSG_PRIORITY_Info, aid, h.Agency.Id, msgTxt, o)
		} else { // no agency assigned
			err, _ = sendMsg2Admin(msgType, commdef.MSG_PRIORITY_Info, aid, msgTxt, o)
		}
		if nil != err {
			return
		}
	}

	act_id = nact_id
	return
}

func getMsgTxt(order_type, act int, role string) (err error, msg string, apmtType int) {
	Fn := "[getMsgTxt] "
	beego.Info(Fn, fmt.Sprintf("order_type:%d, act:%d", order_type, act))

	switch order_type {
	case commdef.ORDER_TYPE_SEE_HOUSE:
		apmtType = commdef.MSG_AppointSeeHouse
		switch act {
		case commdef.APPOINT_ACTION_Submit:
			msg = role + "约看"
		case commdef.APPOINT_ACTION_Confirm:
			msg = role + "同意时间"
		case commdef.APPOINT_ACTION_Reschedule:
			msg = role + "请求改期"
		case commdef.APPOINT_ACTION_Done:
			msg = "约看完成"
		case commdef.APPOINT_ACTION_Cancel:
			msg = role + "取消约看"
		default:
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNKNOWN, ErrInfo: "appointment action type"}
		}
	default:
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNKNOWN, ErrInfo: "appointment order type"}
	}

	return
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
const (
	KEY_APPOINTMENT_TYPE_SEE_HOUSE = "KEY_APPOINTMENT_TYPE_SEE_HOUSE"
)
