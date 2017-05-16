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
}

// @Title NewEventRead
// @Description get new event count
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
