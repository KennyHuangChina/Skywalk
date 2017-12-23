﻿package models

import (
	"ApiServer/commdef"
	"fmt"
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/orm"
	"strconv"
	"time"
)

/**
*	Assign receptionist to specified appointment
*	Arguments:
*		uid 	- login user
*		aptid	- appointment id
*		rectp	- appoint receptionist
*	Returns
*		err 	- error info
 */
func AssignAppointmentRectptionist(uid, aptid, recpt int64) (err error) {
	FN := "[GetAppointmentInfo] "
	beego.Info(FN, "login user:", uid, ", appointment:", aptid, ", recpt:", recpt)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/* Argeuments checking */
	// GetUser(uid)		// isAdministrator will call GetUser either, so skip it here
	err, apmt := getAppointment(aptid)
	if nil != err {
		return
	}
	if apmt.Receptionist == recpt {
		// receptionist not change
		beego.Debug(FN, fmt.Sprintf("apmt.Receptionist(%d) == recpt(%d)", apmt.Receptionist, recpt))
		return
	}

	// user receptionist
	if _, bAgency := isAgency(recpt); !bAgency { // isAgency will check GetUser inside
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: "receptionist is not a agency"}
		return
	}

	_, ur := GetUser(recpt)

	/* Permission checking */
	// Only administrator could assign the receptionist for appointment
	if _, bAdmin := isAdministrator(uid); !bAdmin {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION}
		return
	}

	/* Processing */
	o := orm.NewOrm()

	o.Begin()
	defer func() {
		if nil == err {
			o.Commit()
		} else {
			o.Rollback()
		}
	}()

	// update recrptionist in appointment table
	apmt.Receptionist = recpt
	_, errT := o.Update(&apmt, "Receptionist")
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("error:%s", errT.Error())}
		return
	}

	// appointment action table and fire system message
	comments := fmt.Sprintf("指派[%s]处理客人预约", ur.Name)
	err, act_id := addAppointmentAction(uid, &apmt, commdef.APPOINT_ACTION_SetRectptionist, "", "", comments, o)
	beego.Debug(FN, "new action:", act_id)

	// Assign the agency for house if it not set
	beego.Debug(FN, "house of appointment:", apmt.House)
	if apmt.House > 0 {
		// check whether the house agency set or not
		err1, h := getHouse(apmt.House)
		beego.Debug(FN, "err1:", err1, fmt.Sprintf("house agency: %+v", h.Agency.Id))
		if nil == err1 && 0 == h.Agency.Id {
			SetHouseAgency(apmt.House, recpt, uid)
		}
	}

	return
}

/**
*	get appointment info and its actions hist by appointment id
*	Arguments:
*		uid 		- login user
*	Returns
*		err 		- error info
*		apt_info	- appointment info
 */
func GetAppointmentInfo(uid, aid int64) (err error, apt_info commdef.AppointmentInfo1) {
	FN := "[GetAppointmentInfo] "
	beego.Info(FN, "login user:", uid, ", appointment:", aid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/* Argeuments checking */
	if err, _ = GetUser(uid); nil != err {
		return
	}
	err, apmt := getAppointment(aid)
	if nil != err {
		return
	}

	/* Permission checking */
	// appointment subscriber, appointment receptionest & administrator are able to see
	beego.Debug(FN, fmt.Sprintf("Subscriber: %d, Receptionist: %d", apmt.Subscriber, apmt.Receptionist))
	bPermission := false
	if apmt.Subscriber == uid || apmt.Receptionist == uid {
		bPermission = true
	} else if _, bAdmin := isAdministrator(uid); bAdmin {
		bPermission = true
	}

	if !bPermission {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION}
		return
	}

	/* Processing */
	err, h := getHouse(apmt.House)
	if nil != err {
		return
	}
	// beego.Debug(FN, fmt.Sprintf("house:%+v", h))
	// beego.Debug(FN, fmt.Sprintf("property:%+v", h.Property))

	err, pif := GetPropertyInfo(h.Property.Id)
	if nil != err {
		return
	}
	apt_info.House.HouseId = apmt.House
	apt_info.House.Property = pif.PropName // h.Property.Name
	apt_info.House.Bedrooms = h.Bedrooms
	apt_info.House.Livingrooms = h.Livingrooms
	apt_info.House.Bathrooms = h.Bathrooms
	if errT := canAccessHouse(uid, apmt.House); nil == errT {
		beego.Debug(FN, "Can access house:", apmt.House)
		apt_info.House.BuildingNo = h.BuildingNo
		apt_info.House.HouseNo = h.HouseNo
	}

	apt_info.Date = apmt.ApomtTimeBgn.Local().String()[:10]
	apt_info.TimeBegin = apmt.ApomtTimeBgn.Local().String()[11:16]
	apt_info.TimeEnd = apmt.ApomtTimeEnd.Local().String()[11:16]
	err, u := GetUser(apmt.Subscriber)
	if nil != err {
		return
	}
	apt_info.Subscriber = u.Name
	apt_info.SubscriberPhone = u.LoginName
	if 0 == apmt.Receptionist { // appointment receptionist not assigned yet
		apt_info.Receptionist = "未指派"
		apt_info.ReceptionistPhone = ""
	} else {
		err, u = GetUser(apmt.Receptionist)
		if nil != err {
			return
		}
		apt_info.Receptionist = u.Name
		apt_info.ReceptionistPhone = u.LoginName
	}
	apt_info.ApmtDesc = apmt.ApomtDesc
	apt_info.SubscribeTime = apmt.SubscTime.Local().String()[:19]

	// apt_info.Acts
	o := orm.NewOrm()
	qs := o.QueryTable("tbl_appointment_action").Filter("Appoint", aid)
	acts := []TblAppointmentAction{}
	_, errT := qs.OrderBy("-Id").All(&acts)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("error:%s", errT.Error())}
		return
	}

	for k, v := range acts {
		// beego.Debug(FN, fmt.Sprintf("v:%+v", v))
		if 0 == k {
			// Operations calcualting
			_, apt_info.Ops = getAppointmenOps(uid, &apmt, &v)
		}
		na := commdef.AppointmentAct{Id: v.Id, Act: v.Action, When: v.When.Local().String()[:20], Comment: v.Comment}
		err1, u := GetUser(v.Who)
		if nil != err1 {
			err = err1
			return
		}
		na.Who = u.Name
		// na.Phone = u.LoginName
		if nilTime != v.TimeBgn && nilTime != v.TimeEnd {
			na.TimeBegin = v.TimeBgn.Local().String()[:20]
			na.TimeEnd = v.TimeEnd.Local().String()[:20]
		}
		apt_info.Acts = append(apt_info.Acts, na)
	}

	beego.Debug(FN, fmt.Sprintf("appointment:%+v", apt_info))
	return
}

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

	sqlQuery := " FROM v_appointment_actived WHERE subscriber=? OR phone=?"
	sql := "SELECT COUNT(*) AS count" + sqlQuery
	// beego.Debug(FN, "sql:", sql)
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

func DeleAppointment(aid, uid int64, o orm.Ormer) (err error) {
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
	bNewOrmer := false
	if nil == o {
		o = orm.NewOrm()
		bNewOrmer = true
	}

	apmt := TblAppointment{Id: aid}
	errT := o.Read(&apmt)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("err:%s", errT.Error())}
		return
	}

	if bNewOrmer {
		o.Begin()
		defer func() {
			if nil != err {
				o.Rollback()
			} else {
				o.Commit()
			}
		}()
	}

	// Delete record in appointment table
	numb, errT := o.Delete(&apmt)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("err:%s", errT.Error())}
		return
	}
	beego.Debug(FN, fmt.Sprintf(" delete %d records in TblAppointment", numb))

	// Delete record in appointment action table
	numb, errT = o.QueryTable("tbl_appointment_action").Filter("appoint", aid).Delete()
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("err:%s", errT.Error())}
		return
	}
	beego.Debug(FN, fmt.Sprintf(" delete %d records in TblAppointmentAction", numb))

	// Delete records in messages
	err, msgType := appointType2MessageType(apmt.OrderType)
	if nil != err {
		return
	}

	if err, numb = delMessageByRefId(msgType, aid, o); nil != err {
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
	err, apmt := getAppointment(aid)
	if nil != err {
		return
	}

	if commdef.APPOINT_ACTION_Submit == act {
		// action submit is only for system using, not for user using
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: "Invalid Action"}
		return
	}
	beego.Warn("TODO: 每个状态对应的可执行的操作是不一样的")

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

	err, nact_id := addAppointmentAction(uid, &apmt, act, time_begin, time_end, comment, o)
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
		fmt.Sprintf(", period: %s -> %s", time_begin, time_end), ", desc:", desc)

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

	err, tBegin, tEnd := getScheduleTime(time_begin, time_end)
	if nil != err {
		return
	}

	if 0 == len(desc) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("desc not set")}
		return
	}

	landlord := int64(0) // house landlord
	recpt := int64(0)    // default appointment receptionist
	if hid > 0 {         // appointment is related with house
		h := TblHouse{}
		if err, h = getHousePublished(hid); nil != err {
			return
		}
		recpt = h.Agency.Id
		landlord = h.Owner.Id
	}

	err1, _ := GetUser(uid) // TODO: Kenny
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
		beego.Warn(FN, "TODO: add new user by phone number if it does not exit")
	}

	/* Permission checking */
	// seems everyone could make an appointment, except house owner
	// beego.Debug(FN, fmt.Sprintf("uid:%d, landlord:%d", uid, landlord))
	if uid == landlord {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: "appointment subscriber is landlord himself"}
		return
	}

	/* Processing */
	o := orm.NewOrm()

	// check if the appointment already exist
	sql := "SELECT COUNT(*) AS count FROM v_appointment_actived WHERE order_type=? AND house=? AND subscriber=? AND phone=?"
	Count := []int{}
	_, errT := o.Raw(sql, apType, hid, uid, phone).QueryRows(&Count)
	// qs := o.QueryTable("VAppointmentActived").Filter("OrderType", apType).Filter("House", hid).Filter("Subscriber", uid).Filter("Phone", phone)
	// if qs.Exist() {
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("err:%s", errT.Error())}
		return
	}
	beego.Debug(FN, "numb:", Count[0])
	if Count[0] > 0 {
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
	na := TblAppointment{OrderType: apType, House: hid, Subscriber: uid, Phone: phone, Receptionist: recpt, ApomtTimeBgn: tBegin, ApomtTimeEnd: tEnd, ApomtDesc: desc}
	naid, errT := o.Insert(&na)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("err:%s", errT.Error())}
		return
	}
	beego.Debug(FN, "new appointment:", naid)

	na.Id = naid

	// Appointment action. add submit action automatically
	err, _ = addAppointmentAction(uid, &na, commdef.APPOINT_ACTION_Submit, time_begin, time_end, "", o)
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
	sql := "SELECT COUNT(*) AS count FROM v_appointment_actived WHERE order_type=? AND house=?"
	Count := []int64{}
	_, errT := o.Raw(sql, commdef.ORDER_TYPE_SEE_HOUSE, hid).QueryRows(&Count)
	if nil != errT {
		if orm.ErrNoRows != errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("Fail to get house order table, err:%s", errT.Error())}
		} else {
			beego.Debug(FN, fmt.Sprintf("No order table for house(%d) found", hid))
		}
		return
	}
	total = Count[0]

	// qs := o.QueryTable("v_appointment_actived").Filter("OrderType", commdef.ORDER_TYPE_SEE_HOUSE).Filter("House", hid)
	// cnt, errT := qs.Count()
	// if nil != errT {
	// 	if orm.ErrNoRows != errT {
	// 		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("Fail to get house order table, err:%s", errT.Error())}
	// 		return
	// 	} else {
	// 		beego.Debug(FN, fmt.Sprintf("No order table for house(%d) found", hid))
	// 		return
	// 	}
	// }
	// total = cnt
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
	sql = `SELECT o.id, order_type AS apomt_type, IF(order_type = ? , ?, "Unknown") AS type_desc, 
					o.house, o.phone, IF(LENGTH(u.name) > 0, u.name, ?) AS subscriber, o.apomt_time_bgn, 
					o.apomt_time_end, o.apomt_desc, subsc_time AS subscrib_time, close_time 
				FROM v_appointment_actived AS o LEFT JOIN tbl_user AS u ON o.subscriber=u.id 
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
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("time_end(%s) early than time_begin(%s)", time_end, time_begin)}
		return
	}

	tb = tBegin
	te = tEnd
	return
}

func addAppointmentAction(uid int64, apmt *TblAppointment, act int, time_begin, time_end string, comment string, o orm.Ormer) (err error, act_id int64) {
	FN := "[addAppointmentAction] "
	beego.Info(FN, "login user:", uid, ", appointment:", apmt.Id, ", act:", act,
		fmt.Sprintf(", period(%s - %s)", time_begin, time_end), ", comment:", comment)

	defer func() {
		if nil != err {
			// beego.Error(FN, err)
		}
	}()

	aid := apmt.Id

	/* Argeuments checking */
	if err, _ = GetUser(uid); nil != err {
		return
	}

	if act <= commdef.APPOINT_ACTION_Begin || act > commdef.APPOINT_ACTION_End {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: "Action out of range"}
		return
	}

	tBgn, tEnd := nilTime, nilTime // time.Time{}, time.Time{}
	if commdef.APPOINT_ACTION_Reschedule == act || commdef.APPOINT_ACTION_Submit == act {
		// check schedule time period
		err, tBgn, tEnd = getScheduleTime(time_begin, time_end)
		if nil != err {
			return
		}
	}

	if 0 == len(comment) {
		beego.Debug(FN, fmt.Sprintf("order type:%d, act:%d", apmt.OrderType, act))
		if commdef.ORDER_TYPE_SEE_HOUSE == apmt.OrderType && commdef.APPOINT_ACTION_Submit == act {
			comment = "预约看房"
		}
	}
	if 0 == len(comment) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("comment not set")}
		return
	}

	/* Permission checking */
	if 0 == apmt.Receptionist && commdef.APPOINT_ACTION_Submit != act {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: "appointment receptionist not assigned"}
		return
	}

	// appointment subscriber, appointment receptionist and administrator could manipulate the appointment
	beego.Debug(FN, fmt.Sprintf("Subscriber: %d, Receptionist: %d", apmt.Subscriber, apmt.Receptionist))
	bPermission := false
	if apmt.Subscriber == uid || apmt.Receptionist == uid {
		bPermission = true
	} else if _, bAdmin := isAdministrator(uid); bAdmin {
		bPermission = true
	}

	if !bPermission {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION}
		return
	}

	// operations
	if !checkAppointmentNewAction(uid, apmt, act) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: "Incorrect action"}
		return
	}

	/* Processing */
	// appointment action table
	nact := TblAppointmentAction{Appoint: aid, Action: act, Who: uid, Comment: comment}
	if tBgn != nilTime && tEnd != nilTime {
		nact.TimeBgn = tBgn
		nact.TimeEnd = tEnd
	}

	nact_id, errT := o.Insert(&nact)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("err:%s", errT.Error())}
		return
	}

	// update schedule time in appointment table
	if tBgn != nilTime && tEnd != nilTime {
		a := TblAppointment{Id: aid, ApomtTimeBgn: tBgn, ApomtTimeEnd: tEnd}
		if _, errT = o.Update(&a, "ApomtTimeBgn", "ApomtTimeEnd"); nil != errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("err:%s", errT.Error())}
			return
		}
	}

	// generate system message
	msgType := commdef.MSG_Unknown
	msgTxt := ""
	if 0 == apmt.Receptionist { // action must be commdef.APPOINT_ACTION_Submit
		// appointment receptionist not assigned, send message to admin to assign an agent to deal with the appointment
		if err, msgType = appointType2MessageType(apmt.OrderType); nil != err {
			return
		}
		err, _ = sendMsg2Admin(msgType, commdef.MSG_PRIORITY_Warning, aid, "指派预约接待人", o)
	} else {
		role := ""
		if uid == apmt.Subscriber {
			role = "客人"
		} else {
			role = "经纪人"
		}
		msgPri := commdef.MSG_PRIORITY_Info
		if err, msgTxt, msgType, msgPri = getMsgParameters(apmt.OrderType, act, role); nil != err {
			return
		}
		if uid == apmt.Subscriber {
			// current user is subscriber, so send message to receptionist
			err, _ = addMessage(msgType, msgPri, aid, apmt.Receptionist, msgTxt, o)
		} else if uid == apmt.Receptionist {
			// current user is receptionist, so send message to subscriber
			err, _ = addMessage(msgType, msgPri, aid, apmt.Subscriber, msgTxt, o)
		} else {
			// current user is an administrator, the only thing he can do is assign receptionist so far
			if act != commdef.APPOINT_ACTION_SetRectptionist {
				err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: "Invalid action"}
				return
			}
			err, _ = addMessage(msgType, msgPri, aid, apmt.Receptionist, msgTxt, o)
		}
	}

	act_id = nact_id
	return
}

func appointType2MessageType(apType int) (err error, msgType int) {
	switch apType {
	case commdef.ORDER_TYPE_SEE_HOUSE:
		msgType = commdef.MSG_AppointSeeHouse
	default:
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNKNOWN, ErrInfo: "appointment order type"}
	}

	return
}

func getMsgParameters(order_type, act int, role string) (err error, msg string, msgType, msgPriority int) {
	Fn := "[getMsgParameters] "
	beego.Info(Fn, fmt.Sprintf("order_type:%d, act:%d", order_type, act))

	msgType = commdef.MSG_Unknown
	msgPriority = commdef.MSG_PRIORITY_Info

	switch order_type {
	case commdef.ORDER_TYPE_SEE_HOUSE:
		msgType = commdef.MSG_AppointSeeHouse
		switch act {
		case commdef.APPOINT_ACTION_Submit:
			msg = "客人约看"
		case commdef.APPOINT_ACTION_Confirm:
			msg = role + "同意时间"
		case commdef.APPOINT_ACTION_Reschedule:
			msg = role + "请求改期"
			msgPriority = commdef.MSG_PRIORITY_Warning
		case commdef.APPOINT_ACTION_Done:
			msg = "约看完成"
		case commdef.APPOINT_ACTION_Cancel:
			msg = role + "取消约看"
			msgPriority = commdef.MSG_PRIORITY_Error
		case commdef.APPOINT_ACTION_SetRectptionist:
			msg = "请处理客人预约"
			msgPriority = commdef.MSG_PRIORITY_Info
		default:
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNKNOWN, ErrInfo: "appointment action type"}
		}
	default:
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNKNOWN, ErrInfo: "appointment order type"}
	}

	return
}

func checkAppointmentNewAction(uid int64, apmt *TblAppointment, act int) bool {
	Fn := "[checkAppointmentNewAction] "
	beego.Info(Fn, fmt.Sprintf("login user: %d, act: %d", uid, act))
	// beego.Info(Fn, fmt.Sprintf("appointment: %+v", apmt))

	o := orm.NewOrm()

	qs := o.QueryTable("tbl_appointment_action").Filter("Appoint", apmt.Id)
	act1 := TblAppointmentAction{}
	errT := qs.OrderBy("-Id").One(&act1)
	if nil != errT {
		if (orm.ErrNoRows == errT || orm.ErrMissPK == errT) && commdef.APPOINT_ACTION_Submit == act {
			beego.Debug(Fn, "1st action record")
			return true
		}
		beego.Error(Fn, errT.Error())
		return false
	}

	beego.Debug(Fn, "act1.Action:", act1.Action)
	switch act1.Action {
	case commdef.APPOINT_ACTION_Submit:
		if uid == apmt.Subscriber { // appointment subscriber
			if commdef.APPOINT_ACTION_Cancel == act || commdef.APPOINT_ACTION_Reschedule == act {
				return true
			}
		} else if uid == apmt.Receptionist { // appointment receptionist
			if commdef.APPOINT_ACTION_Confirm == act || commdef.APPOINT_ACTION_Reschedule == act {
				return true
			}
		} else { // administrator
			if commdef.APPOINT_ACTION_SetRectptionist == act {
				return true
			}
		}
	case commdef.APPOINT_ACTION_Confirm:
		// beego.Debug(Fn, fmt.Sprintf("Subscriber: %d, Receptionist: %d", apmt.Subscriber, apmt.Receptionist))
		if uid == apmt.Subscriber { // appointment subscriber
			if commdef.APPOINT_ACTION_Cancel == act || commdef.APPOINT_ACTION_Reschedule == act {
				return true
			}
		} else if uid == apmt.Receptionist { // appointment receptionist
			if commdef.APPOINT_ACTION_Done == act || commdef.APPOINT_ACTION_Reschedule == act {
				return true
			}
		} else { // administrator
			if commdef.APPOINT_ACTION_SetRectptionist == act {
				return true
			}
		}
	case commdef.APPOINT_ACTION_Reschedule:
		if uid == apmt.Subscriber { // appointment subscriber
			if act1.Who == apmt.Subscriber { // reschedule requested by subscriber
				if commdef.APPOINT_ACTION_Cancel == act || commdef.APPOINT_ACTION_Reschedule == act {
					return true
				}
			} else if act1.Who == apmt.Receptionist { // reschedule requested by receptionist
				if commdef.APPOINT_ACTION_Cancel == act || commdef.APPOINT_ACTION_Reschedule == act || commdef.APPOINT_ACTION_Confirm == act {
					return true
				}
			}
		} else if uid == apmt.Receptionist { // appointment receptionist
			if act1.Who == apmt.Subscriber { // reschedule requested by subscriber
				if commdef.APPOINT_ACTION_Confirm == act || commdef.APPOINT_ACTION_Reschedule == act {
					return true
				}
			} else if act1.Who == apmt.Receptionist { // reschedule requested by receptionist
				if commdef.APPOINT_ACTION_Reschedule == act {
					return true
				}
			}
		} else { // administrator
			if commdef.APPOINT_ACTION_SetRectptionist == act {
				return true
			}
		}
	case commdef.APPOINT_ACTION_SetRectptionist:
		if uid == apmt.Subscriber { // appointment subscriber
			if commdef.APPOINT_ACTION_Cancel == act || commdef.APPOINT_ACTION_Reschedule == act {
				return true
			}
		} else if uid == apmt.Receptionist { // appointment receptionist
			if commdef.APPOINT_ACTION_Confirm == act || commdef.APPOINT_ACTION_Reschedule == act {
				return true
			}
		} else { // administrator
			if commdef.APPOINT_ACTION_SetRectptionist == act {
				return true
			}
		}
	case commdef.APPOINT_ACTION_Done:
	case commdef.APPOINT_ACTION_Cancel:
	default:
	}

	return false
}

func getAppointmenOps(uid int64, apmt *TblAppointment, act *TblAppointmentAction) (err error, Ops int) {
	Fn := "[getAppointmenOps] "
	beego.Info(Fn, fmt.Sprintf("login user:%d, appointment:%+v, act:%+v", uid, apmt, act))

	Ops = 0

	switch act.Action {
	case commdef.APPOINT_ACTION_Submit:
		if uid == apmt.Subscriber { // appointment subscriber
			Ops = commdef.APPOINT_OP_Cancel + commdef.APPOINT_OP_Reschedule
		} else if uid == apmt.Receptionist { // appointment receptionist
			Ops = commdef.APPOINT_OP_Reschedule + commdef.APPOINT_OP_Confirm
		} else { // administrator
			Ops = commdef.APPOINT_OP_AssignReceptionist
		}
	case commdef.APPOINT_ACTION_Confirm:
		// beego.Debug(Fn, fmt.Sprintf("Subscriber: %d, Receptionist: %d", apmt.Subscriber, apmt.Receptionist))
		if uid == apmt.Subscriber { // appointment subscriber
			Ops = commdef.APPOINT_OP_Cancel + commdef.APPOINT_OP_Reschedule
		} else if uid == apmt.Receptionist { // appointment receptionist
			Ops = commdef.APPOINT_OP_Reschedule + commdef.APPOINT_OP_Done
		} else { // administrator
			Ops = commdef.APPOINT_OP_AssignReceptionist
		}
	case commdef.APPOINT_ACTION_Reschedule:
		if uid == apmt.Subscriber { // appointment subscriber
			if act.Who == apmt.Subscriber { // reschedule requested by subscriber
				Ops = commdef.APPOINT_OP_Cancel + commdef.APPOINT_OP_Reschedule
			} else if act.Who == apmt.Receptionist { // reschedule requested by receptionist
				Ops = commdef.APPOINT_OP_Cancel + commdef.APPOINT_OP_Reschedule + commdef.APPOINT_OP_Confirm
			}
		} else if uid == apmt.Receptionist { // appointment receptionist
			if act.Who == apmt.Subscriber { // reschedule requested by subscriber
				Ops = commdef.APPOINT_OP_Reschedule + commdef.APPOINT_OP_Confirm
			} else if act.Who == apmt.Receptionist { // reschedule requested by receptionist
				Ops = commdef.APPOINT_OP_Reschedule
			}
		} else { // administrator
			Ops = commdef.APPOINT_OP_AssignReceptionist
		}
	case commdef.APPOINT_ACTION_SetRectptionist:
		if uid == apmt.Subscriber { // appointment subscriber
			Ops = commdef.APPOINT_OP_Cancel + commdef.APPOINT_OP_Reschedule
		} else if uid == apmt.Receptionist { // appointment receptionist
			Ops = commdef.APPOINT_OP_Reschedule + commdef.APPOINT_OP_Confirm
		} else { // administrator
			Ops = commdef.APPOINT_OP_AssignReceptionist
		}
	case commdef.APPOINT_ACTION_Done:
	case commdef.APPOINT_ACTION_Cancel:
	default:
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNKNOWN, ErrInfo: "Unknow action"}
	}

	beego.Debug(Fn, "Options:", Ops)
	return
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
const (
	KEY_APPOINTMENT_TYPE_SEE_HOUSE = "KEY_APPOINTMENT_TYPE_SEE_HOUSE"
)
