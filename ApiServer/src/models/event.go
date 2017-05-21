package models

import (
	"ApiServer/commdef"
	"fmt"
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/orm"
	"strconv"
	"time"
)

const (
	EVENT_STAT_Begin = 0

	EVENT_STAT_All    = 0
	EVENT_STAT_New    = 1
	EVENT_STAT_Ongoin = 2
	EVENT_STAT_Closed = 3

	EVENT_STAT_End = 3
)

/**
*	Get even proc list by event id
*	Arguments:
*		uid 	- login user id
*		hid		- house id
*		bgn		- fetch begin position
*		cnt		- how many records to fetch. = 0 means just fetch total number
*		stat	- event status. EVENT_STAT_xxx
*		type  	- event type. 0 means all kind of event. refer to HOUSE_EVENT_xxx
*	Returns
*		err 	- error info
*		hel		- house event list
**/
func GetHouseEventList(uid, hid, bgn, cnt int64, stat, et int, ido bool) (err error, total int64, hel []commdef.HouseEventInfo) {
	FN := "[GetHouseEventList] "
	beego.Trace(FN, "login user:", uid, ", house:", hid, ", fetch begin:", bgn, ", fetch count:", cnt, ", stat:", stat, ", type:", et, ", id only:", ido)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/* Argument Checking */
	err, h := getHouse(hid)
	if nil != err {
		return
	}
	beego.Debug(FN, "house:", fmt.Sprintf("%+v", h))

	if err, _ = GetUser(uid); nil != err {
		return
	}

	if bgn < 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("begin position:%d", bgn)}
		return
	}

	if cnt < 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("begin cnt:%d", cnt)}
		return
	}

	if stat < EVENT_STAT_Begin || stat > EVENT_STAT_End {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("stat:%d", stat)}
		return
	}

	if et < 0 /*commdef.HOUSE_EVENT_Begin*/ || et > commdef.HOUSE_EVENT_End {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("event type:%d", et)}
		return
	}

	/* Permission Checking */
	// Onlye the landlord, house agency and administrator could access the house event
	if isHouseOwner(h, uid) || isHouseAgency(h, uid) {
	} else if _, bAdmin := isAdministrator(uid); bAdmin {
	} else {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("login user:%d", uid)}
		return
	}

	/* Processing */
	// calculate the total number
	o := orm.NewOrm()
	qs := o.QueryTable("tbl_house_event").Filter("House", hid)
	if stat > EVENT_STAT_All { // event status
		switch stat {
		case EVENT_STAT_New:
			qs = qs.Filter("ReadTime__isnull", true)
		case EVENT_STAT_Ongoin:
			qs = qs.Filter("ReadTime__isnull", false).Filter("CloseTime__isnull", true)
		case EVENT_STAT_Closed:
			qs = qs.Filter("CloseTime__isnull", false)
		}
	}
	if et > 0 { // event type
		qs = qs.Filter("Type", et)
	}

	numb, errT := qs.Count()
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}
	beego.Debug(FN, fmt.Sprintf("%d events found", numb))
	// if 0 == numb {
	// 	err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("there are no events for house:%d", hid)}
	// 	return
	// }
	total = numb
	if 0 == cnt || 0 == total { // user just want to detect the total number, or there no event could be fetched
		return
	}

	if bgn > total-1 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("begin position:%d >= total:%d", bgn, total)}
		return
	}

	// fetch real records
	sql_event := fmt.Sprintf(`SELECT event.id, event.house AS house_id, prop.name AS property, building_no AS building,
										house_no, sender, receiver, create_time, read_time, type, event.desc
									FROM tbl_house_event AS event, tbl_house AS house, tbl_property AS prop
									WHERE event.house=%d AND event.house=house.id AND house.property_id = prop.id`, hid)
	if ido {
		sql_event = fmt.Sprintf(`SELECT id FROM tbl_house_event WHERE house=%d`, hid)
	}
	if stat > EVENT_STAT_All { // event status
		switch stat {
		case EVENT_STAT_New:
			sql_event = sql_event + " AND read_time IS NULL"
		case EVENT_STAT_Ongoin:
			sql_event = sql_event + " AND read_time IS NOT NULL AND close_time IS NULL"
		case EVENT_STAT_Closed:
			sql_event = sql_event + " AND close_time IS NOT NULL"
		}
	}
	if et > 0 { // event type
		sql_event = sql_event + fmt.Sprintf(" AND type=%d", et)
	}
	sql_event = sql_event + " ORDER BY read_time DESC, close_time DESC"

	sql_proc := fmt.Sprintf(`SELECT el.id, CASE WHEN ISNULL(proc.event_id) then 0 ELSE COUNT(*) END AS count 
								FROM (SELECT id FROM tbl_house_event WHERE house=%d) AS el LEFT JOIN tbl_house_event_process AS proc
								ON el.id = proc.event_id GROUP BY el.id`, hid)
	beego.Debug(FN, "sql_proc:", sql_proc)

	sql := fmt.Sprintf(`SELECT event.*, proc.count AS proc_count FROM (%s) AS event, (%s) AS proc 
							WHERE event.id=proc.id LIMIT %d, %d`, sql_event, sql_proc, bgn, cnt)
	beego.Debug(FN, "sql:", sql)

	var lst []commdef.HouseEventInfo
	numb, errT = o.Raw(sql).QueryRows(&lst)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}
	beego.Debug(FN, fmt.Sprintf("%d records", numb))

	for k, v := range lst {
		beego.Debug(FN, fmt.Sprintf("%d:%+v", k, v))

		sender, _ := strconv.ParseInt(v.Sender, 10, 64)
		if 0 == sender {
			v.Sender = getName4System()
		} else {
			_, u := GetUserInfo(sender, uid)
			v.Sender = u.Name
		}

		receiver, _ := strconv.ParseInt(v.Receiver, 10, 64)
		_, u := GetUserInfo(receiver, uid)
		v.Receiver = u.Name

		hel = append(hel, v)
		beego.Debug(FN, fmt.Sprintf("%d:%+v", k, hel[k]))
	}

	return
}

/**
*	Get even proc list by event id
*	Arguments:
*		uid 	- login user id
*		eid		- event id
*	Returns
*		err 	- error info
*		epl		- event proc list
**/
func GetEventProcList(uid, eid int64) (err error, epl []commdef.HouseEventProc) {
	FN := "[GetEventProcList] "
	beego.Trace(FN, "login user:", uid, ", event:", eid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/* Argument Checking */
	err, he := getEvent(eid)
	if nil != err {
		return
	}
	beego.Debug(FN, "he:", fmt.Sprintf("%+v", he))

	if err, _ = GetUser(uid); nil != err {
		return
	}

	/* Permission Checking */
	// The landlord, house agency and administrator could access event
	err, h := getHouse(he.House)
	if nil != err {
		return
	}
	if isHouseOwner(h, uid) {
		beego.Debug(FN, "landlord")
	} else if isHouseAgency(h, uid) {
		beego.Debug(FN, "house agency")
	} else if _, bAdmin := isAdministrator(uid); bAdmin {
		beego.Debug(FN, "administrator")
	} else {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("login user:%d", uid)}
		return
	}

	/* Processing */
	o := orm.NewOrm()
	qs := o.QueryTable("tbl_house_event_process")
	if eid > 0 {
		qs = qs.Filter("Event__Id", eid)
	}

	var l []TblHouseEventProcess
	//	/*numb*/ _, errT := qs.RelatedSel("TblHouseEvent").All(&l)
	/*numb*/ _, errT := qs.OrderBy("Event__Id", "-When").RelatedSel().All(&l)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	for k, v := range l {
		np := commdef.HouseEventProc{Id: v.Id, Desc: v.Desc, Op: getEventProcType(v.Type), Time: v.When.Local().String()[:19]}
		_, u := GetUserInfo(v.Who, uid)
		np.User = u.Name
		epl = append(epl, np)
		beego.Debug(FN, fmt.Sprintf("%d: %+v", k, np))
	}

	return
}

/**
*	Get event info by id
*	Arguments:
*		uid 	- login user id
*		eid		- event id
*	Returns
*		err 	- error info
*		ei		- event info
**/
func GetEventInfo(uid, eid int64) (err error, ei commdef.HouseEventInfo) {
	FN := "[GetEventInfo] "
	beego.Trace(FN, "login user:", uid, ", event:", eid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/* Argument Checking */
	err, he := getEvent(eid)
	if nil != err {
		return
	}
	beego.Debug(FN, "he:", fmt.Sprintf("%+v", he))

	if err, _ = GetUser(uid); nil != err {
		return
	}

	/* Permission Checking */
	// The landlord, house agency administrator, sender and receiver could get event
	err, h := getHouse(he.House)
	if nil != err {
		return
	}
	// if he.Sender == uid || he.Receiver == uid {
	// 	beego.Debug(FN, "sender or receiver")
	// } else
	if isHouseOwner(h, uid) {
		beego.Debug(FN, "landlord")
	} else if isHouseAgency(h, uid) {
		beego.Debug(FN, "house agency")
	} else if _, bAdmin := isAdministrator(uid); bAdmin {
		beego.Debug(FN, "administrator")
	} else {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("login user:%d", uid)}
		return
	}

	/* Processing */
	o := orm.NewOrm()
	qs := o.QueryTable("tbl_house_event_process").Filter("Event__Id", eid)
	cnt, errT := qs.Count()
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	ei.Id = he.Id
	ei.HouseId = he.House
	_, ei.Property = getHouseProperty(ei.HouseId)
	_, ei.Building, ei.HouseNo = getHouseNumber(ei.HouseId)
	// _, ei.Picture = getHouseCoverImg(ei.HouseId)

	if 0 == he.Sender {
		ei.Sender = getName4System()
	} else {
		_, u := GetUserInfo(he.Sender, uid)
		ei.Sender = u.Name
		beego.Debug(FN, "sender:", fmt.Sprintf("%+v", u))
	}

	if 0 == he.Receiver {
		ei.Receiver = getName4System()
	} else {
		_, u := GetUserInfo(he.Receiver, uid)
		ei.Receiver = u.Name
		beego.Debug(FN, "receiver:", fmt.Sprintf("%+v", u))
	}

	// beego.Debug(FN, "CreateTime:", he.CreateTime.Local())
	nullTime := time.Time{}
	if nullTime != he.CreateTime {
		ei.CreateTime = he.CreateTime.Local().String()[:19]
	}
	if nullTime != he.ReadTime {
		ei.ReadTime = he.ReadTime.Local().String()[:19]
	}
	ei.Type = getEventType(he.Type)
	ei.Desc = he.Desc
	ei.ProcCount = cnt

	return
}

/**
*	Get new event count
*	Arguments:
*		uid 		- login user id
*		eid			- event id
*	Returns
*		err 		- error info
**/
func NewEventRead(uid, eid int64) (err error) {
	FN := "[NewEventRead] "
	beego.Trace(FN, "login user:", uid, ", event:", eid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/* Argument Checking */
	err, he := getEvent(eid)
	if nil != err {
		return
	}
	nullTime := time.Time{}
	if nullTime != he.ReadTime {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("event:%d already read", eid)}
		return
	}

	if err, _ = GetUser(uid); nil != err {
		return
	}

	/* Permission Checking */
	// Only the event receiver could set event read status
	if he.Receiver != uid {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("login user:%d", uid)}
		return
	}

	/* Processing */
	o := orm.NewOrm()
	eu := TblHouseEvent{Id: eid, ReadTime: time.Now()}
	/*numb*/ _, errT := o.Update(&eu, "ReadTime")
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	return
}

/**
*	Get new event count
*	Arguments:
*		uid 		- user id
*	Returns
*		err 		- error info
*		new_event 	- new event count
**/
func GetNewEventCount(uid int64) (err error, new_event int64) {
	FN := "[GetNewEventCount] "
	beego.Trace(FN, "login user:", uid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	if err, _ = GetUser(uid); nil != err {
		return
	}

	sql := fmt.Sprintf("SELECT COUNT(*) AS count FROM tbl_house_event WHERE (sender=%d OR receiver=%d) AND read_time IS NULL", uid, uid)

	o := orm.NewOrm()

	var count []int64
	numb, errT := o.Raw(sql).QueryRows(&count)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}
	beego.Debug(FN, "numb:", numb, ", count:", count)

	if numb > 0 {
		new_event = count[0]
	}
	beego.Warn(FN, "TODO: User permission checking")

	return
}

/**
*	Get new event count for each house
*	Arguments:
*		uid 	- login user
*	Returns
*		err 	- error info
*		houses 	- house list with new event
**/
type house_events struct {
	House  int64 // house id
	Events int   // new event count
}

func GetHouseNewEvents(uid int64) (err error, houses []commdef.HouseEvents) {
	FN := "[GetHouseNewEvents] "
	beego.Trace(FN, "login user:", uid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/* Argumen Checking */
	if err, _ = GetUser(uid); nil != err {
		return
	}

	/* Permission checking */
	beego.Warn(FN, "TODO: User permission checking")

	// Houses that can access
	//	for house owner, only the houses they owned could be fetched
	//	for agency, the houses they owned and the house they represent could be fetched
	sql_house := fmt.Sprintf("SELECT id FROM tbl_house WHERE owner_id=%d", uid)
	if _, bAgency := isAgency(uid); bAgency {
		sql_house = sql_house + fmt.Sprintf(" OR agency_id=%d", uid)
	}
	// beego.Debug(FN, "sql_house:", sql_house)

	// Kenny: due to sql_mod ONLY_FULL_GROUP_BY, we could get all information which is not in GROUP BY clause
	//			so, we have to split it into two steps: 1st get all houses, and then get rest of informations by house id
	// new events
	sql_event := fmt.Sprintf(`SELECT event.id, house, create_time, event.desc 
								FROM tbl_house_event AS event, (%s) AS house 
								WHERE event.house=house.id AND read_time IS NULL
								ORDER BY house, create_time DESC`, sql_house)
	// beego.Debug(FN, "sql_event:", sql_event)

	sql_event = fmt.Sprintf("SELECT t1.house, COUNT(*) AS events FROM (%s) AS t1 GROUP BY house", sql_event)
	// beego.Debug(FN, "sql_event:", sql_event)

	var hes []house_events
	o := orm.NewOrm()
	numb, errT := o.Raw(sql_event).QueryRows(&hes)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	if 0 == numb { // there are no new events
		return
	}

	hs := make([]commdef.HouseEvents, 0)
	for /*k*/ _, v := range hes {
		newItem := commdef.HouseEvents{HouseId: v.House, EventCnt: v.Events}

		_, newItem.Property = getHouseProperty(newItem.HouseId)
		_, newItem.Building, newItem.HouseNo = getHouseNumber(newItem.HouseId)
		_, newItem.Picture = getHouseCoverImg(newItem.HouseId)

		_, EventDesc, create_time := getHouseNewestEventDesc(newItem.HouseId)
		newItem.EventDesc = EventDesc
		newItem.Time = create_time.String()

		hs = append(hs, newItem)
	}
	beego.Debug(FN, "hs:", hs)
	// fmt.Printf("%s hs:%+v", FN, hs)

	houses = hs
	return
}

/***********************************************************************************************************************
*
*		Internal Functions
*
************************************************************************************************************************/
/*
*	get newest event desc of house specified
*	Returns:
*		ct	- create time
*		ed	- event description
**/
type event_desc struct {
	Desc       string
	CreateTime time.Time
}

func getHouseNewestEventDesc(hid int64) (err error, ed string, ct time.Time) {
	// FN := "[getHouseNewestEventDesc] "

	sql_event := fmt.Sprintf(`SELECT create_time, event.desc, proc.event_id 
								FROM tbl_house_event AS event LEFT JOIN tbl_house_event_process AS proc 
									ON event.id=proc.event_id 
								WHERE event.house=%d AND event_id IS NULL
								ORDER BY house, create_time DESC`, hid)
	// beego.Debug(FN, "sql_event:", sql_event)

	o := orm.NewOrm()

	var es []event_desc
	numb, errT := o.Raw(sql_event).QueryRows(&es)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}
	if 0 == numb { // no new event found
		return
	}

	ct = es[0].CreateTime
	ed = es[0].Desc

	return
}

func getEvent(eid int64) (err error, he TblHouseEvent) {
	FN := "[getEvent] "
	beego.Info(FN, "event:", eid)

	if eid <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("event:%d", eid)}
		return
	}

	o := orm.NewOrm()
	e := TblHouseEvent{Id: eid}
	errT := o.Read(&e)
	if errT == orm.ErrNoRows || errT == orm.ErrMissPK {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_RES_NOTFOUND, ErrInfo: fmt.Sprintf("event:%d", eid)}
		return
	} else if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	he = e
	return
}

// reset even to unread status
func newEventUnread(uid, eid int64) (err error) {
	FN := "[newEventUnread] "
	beego.Trace(FN, "login user:", uid, ", event:", eid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/* Argument Checking */
	err, he := getEvent(eid)
	if nil != err {
		return
	}
	nullTime := time.Time{}
	if nullTime == he.ReadTime {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("event:%d not read", eid)}
		return
	}

	if err, _ = GetUser(uid); nil != err {
		return
	}

	/* Permission Checking */
	// Only the administrator could set event unread status
	if _, bAdmin := isAdministrator(uid); !bAdmin {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("login user:%d", uid)}
		return
	}

	/* Processing */
	o := orm.NewOrm()
	eu := TblHouseEvent{Id: eid, ReadTime: nullTime}
	/*numb*/ _, errT := o.Update(&eu, "ReadTime")
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	return
}

func getEventType(et int) string {
	if v, ok := EVENT_TYPE_MAP[et]; ok {
		return getSpecialString(v)
	}
	return getSpecialString(KEY_UNKNOWN)
}

func getEventProcType(ept int) string {
	if v, ok := EVENT_PROC_TYPE_MAP[ept]; ok {
		return getSpecialString(v)
	}
	return getSpecialString(KEY_UNKNOWN)
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//

const (
	KEY_UNKNOWN = "KEY_UNKNOWN"

	KEY_USER_SYSTEM               = "KEY_USER_SYSTEM"
	KEY_USER_NAME_NOT_SET         = "KEY_USER_NAME_NOT_SET"
	KEY_LANDLORD_SUBMIT_NEW_HOUSE = "KEY_LANDLORD_SUBMIT_NEW_HOUSE"
	KEY_HOUSE_CERTIFICATE_BEGIN   = "KEY_HOUSE_CERTIFICATE_BEGIN"
	KEY_HOUSE_CERTIFICATE_FAILED  = "KEY_HOUSE_CERTIFICATE_FAILED"
	KEY_HOUSE_CERTIFICATE_PASS    = "KEY_HOUSE_CERTIFICATE_PASS"

	KEY_HOUSE_EVENT_PROC_FOLLOW = "KEY_HOUSE_EVENT_PROC_FOLLOW"
	KEY_HOUSE_EVENT_PROC_CLOSE  = "KEY_HOUSE_EVENT_PROC_CLOSE"
)

var EVENT_TYPE_MAP map[int]string
var EVENT_PROC_TYPE_MAP map[int]string

func init() {
	// Initialize the event type map
	EVENT_TYPE_MAP = make(map[int]string)
	EVENT_TYPE_MAP[commdef.HOUSE_EVENT_Submit] = KEY_LANDLORD_SUBMIT_NEW_HOUSE
	EVENT_TYPE_MAP[commdef.HOUSE_EVENT_Certification_Begin] = KEY_HOUSE_CERTIFICATE_BEGIN
	EVENT_TYPE_MAP[commdef.HOUSE_EVENT_Certification_Fail] = KEY_HOUSE_CERTIFICATE_FAILED
	EVENT_TYPE_MAP[commdef.HOUSE_EVENT_Certification_OK] = KEY_HOUSE_CERTIFICATE_PASS

	// Initialize the event proc type map
	EVENT_PROC_TYPE_MAP = make(map[int]string)
	EVENT_PROC_TYPE_MAP[commdef.HOUSE_EVENT_PROC_Follow] = KEY_HOUSE_EVENT_PROC_FOLLOW
	EVENT_PROC_TYPE_MAP[commdef.HOUSE_EVENT_PROC_Close] = KEY_HOUSE_EVENT_PROC_CLOSE
}
