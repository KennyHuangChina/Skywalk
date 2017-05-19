package controllers

import (
	// "ApiServer/commdef"
	"fmt"
	"github.com/astaxie/beego"
	// "github.com/astaxie/beego/orm"
	"ApiServer/models"
)

type EventController struct {
	beego.Controller
}

func (e *EventController) URLMapping() {
	e.Mapping("GetNewEventCount", e.GetNewEventCount)
	e.Mapping("GetHouseNewEvents", e.GetHouseNewEvents)
	e.Mapping("NewEventRead", e.NewEventRead)
	e.Mapping("GetEvent", e.GetEvent)
	e.Mapping("GetEventProcs", e.GetEventProcs)
	e.Mapping("GetHouseEventList", e.GetHouseEventList)
}

// @Title GetHouseEventList
// @Description get house event list
// @Success 200 {string}
// @Failure 403 body is empty
// @router /list/house/:id [get]
func (this *EventController) GetHouseEventList() {
	FN := "[GetHouseEventList] "
	beego.Warn("[--- API: GetHouseEventList ---]")

	var result ResGetHouseEventList
	var err error

	defer func() {
		err = api_result(err, this.Controller, &result.ResCommon)
		if nil != err {
			beego.Error(FN, err.Error())
		}

		// export result
		this.Data["json"] = result
		this.ServeJSON()
	}()

	/*
	 *	Extract agreements
	 */
	uid, err := getLoginUser(this.Controller)
	if nil != err {
		return
	}
	hid, _ := this.GetInt64(":id")
	stat, _ := this.GetInt("stat")
	et, _ := this.GetInt("type")
	bgn, _ := this.GetInt64("bgn")
	cnt, _ := this.GetInt64("cnt")
	ido, _ := this.GetBool("ido")
	beego.Debug(FN, "uid:", uid, ", house:", hid)

	/*
	 *	Processing
	 */
	err, total, hel := models.GetHouseEventList(uid, hid, bgn, cnt, stat, et, ido)
	if nil == err {
		result.Total = total
		result.Count = int64(len(hel))
		if result.Count > 0 {
			result.EventLst = hel
		}
	}
}

// @Title GetEventProcs
// @Description get event proc list by id
// @Success 200 {string}
// @Failure 403 body is empty
// @router /:id/proc [get]
func (this *EventController) GetEventProcs() {
	FN := "[GetEventProcs] "
	beego.Warn("[--- API: GetEventProcs ---]")

	var result ResGetHouseEventProcList
	var err error

	defer func() {
		err = api_result(err, this.Controller, &result.ResCommon)
		if nil != err {
			beego.Error(FN, err.Error())
		}

		// export result
		this.Data["json"] = result
		this.ServeJSON()
	}()

	/*
	 *	Extract agreements
	 */
	uid, err := getLoginUser(this.Controller)
	if nil != err {
		return
	}
	eid, _ := this.GetInt64(":id")
	beego.Debug(FN, "uid:", uid, ", event:", eid)

	/*
	 *	Processing
	 */
	err, epl := models.GetEventProcList(uid, eid)
	if nil == err {
		result.Total = len(epl)
		result.Count = len(epl)
		result.ProcList = epl
	}
}

// @Title GetEvent
// @Description get event info by id
// @Success 200 {string}
// @Failure 403 body is empty
// @router /:id [get]
func (this *EventController) GetEvent() {
	FN := "[GetEvent] "
	beego.Warn("[--- API: GetEvent ---]")

	var result ResGetHouseEventInfo
	var err error

	defer func() {
		err = api_result(err, this.Controller, &result.ResCommon)
		if nil != err {
			beego.Error(FN, err.Error())
		}

		// export result
		this.Data["json"] = result
		this.ServeJSON()
	}()

	/*
	 *	Extract agreements
	 */
	uid, err := getLoginUser(this.Controller)
	if nil != err {
		return
	}
	eid, _ := this.GetInt64(":id")
	beego.Debug(FN, "uid:", uid, ", event:", eid)

	/*
	 *	Processing
	 */
	err, ei := models.GetEventInfo(uid, eid)
	if nil == err {
		result.Event = ei
	}
}

// @Title NewEventRead
// @Description set new event read status
// @Success 200 {string}
// @Failure 403 body is empty
// @router /:id/read [put]
func (this *EventController) NewEventRead() {
	FN := "[NewEventRead] "
	beego.Warn("[--- API: NewEventRead ---]")

	var result ResCommon
	var err error

	defer func() {
		err = api_result(err, this.Controller, &result)
		if nil != err {
			beego.Error(FN, err.Error())
		}

		// export result
		this.Data["json"] = result
		this.ServeJSON()
	}()

	/*
	 *	Extract agreements
	 */
	uid, err := getLoginUser(this.Controller)
	if nil != err {
		return
	}
	eid, _ := this.GetInt64(":id")
	beego.Debug(FN, "uid:", uid, ", event:", eid)

	/*
	 *	Processing
	 */
	err = models.NewEventRead(uid, eid)
	if nil == err {
	}
}

// @Title GetNewEventCount
// @Description get new event count
// @Success 200 {string}
// @Failure 403 body is empty
// @router /count [get]
func (this *EventController) GetNewEventCount() {
	FN := "[GetNewEventCount] "
	beego.Warn("[--- API: GetNewEventCount ---]")

	var result ResGetNewEventCnt
	var err error

	defer func() {
		err = api_result(err, this.Controller, &result.ResCommon)
		if nil != err {
			beego.Error(FN, err.Error())
		}

		// export result
		this.Data["json"] = result
		this.ServeJSON()
	}()

	/*
	 *	Extract agreements
	 */
	uid, err := getLoginUser(this.Controller)
	if nil != err {
		return
	}
	beego.Debug(FN, "uid:", uid)

	/*
	 *	Processing
	 */
	err, ne := models.GetNewEventCount(uid)
	if nil == err {
		result.NewEvent = ne
	}
}

// @Title GetHouseNewEvents
// @Description get new events by house
// @Success 200 {string}
// @Failure 403 body is empty
// @router /houses [get]
func (this *EventController) GetHouseNewEvents() {
	FN := "[GetHouseNewEvents] "
	beego.Warn("[--- API: GetHouseNewEvents ---]")

	var result ResGetHouseEvents
	var err error

	defer func() {
		err = api_result(err, this.Controller, &result.ResCommon)
		if nil != err {
			beego.Error(FN, err.Error())
		}

		// export result
		this.Data["json"] = result
		this.ServeJSON()
	}()

	/*
	 *	Extract agreements
	 */
	uid, err := getLoginUser(this.Controller)
	if nil != err {
		return
	}

	/*
	 *	Processing
	 */
	err, houses := models.GetHouseNewEvents(uid)
	if nil == err {
		result.Houses = houses
		beego.Debug(FN, fmt.Sprintf("%+v", houses))
	}
}
