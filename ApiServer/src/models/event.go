package models

import (
	"ApiServer/commdef"
	"fmt"
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/orm"
	"time"
)

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
*	Get new event count
*	Arguments:
*		uid 	- user id
*	Returns
*		err 	- error info
*		houses 	- house list with new event
*	Comments:
*		for house owner, only the houses they owned could be fetched
*		for agency, the house they owned and the house they represent could be fetched
**/
type house_events struct {
	House  int64 // house id
	Events int   // new event count
}

func GetHouseNewEvents(uid int64) (err error, houses []commdef.HouseEvents) {
	FN := "[GetHouseNewEvents] "
	beego.Trace(FN, "uid:", uid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	if uid <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("user id:%d", uid)}
		return
	}
	beego.Warn(FN, "TODO: User permission checking")

	// houses that can access
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
