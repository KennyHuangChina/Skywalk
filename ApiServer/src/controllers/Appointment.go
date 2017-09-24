package controllers

import (
	// "encoding/json"
	"ApiServer/commdef"
	"ApiServer/models"
	"encoding/base64"
	// "fmt"
	"github.com/astaxie/beego"
)

type AppointmentController struct {
	beego.Controller
}

func (a *AppointmentController) URLMapping() {
	a.Mapping("OrderSeeHouse", a.OrderSeeHouse)
	a.Mapping("MakeAppointmentAction", a.MakeAppointmentAction)
	a.Mapping("GetAppointmentInfo", a.GetAppointmentInfo)

	a.Mapping("GetAppointList_SeeHouse", a.GetAppointList_SeeHouse)
	a.Mapping("GetHouseList_AppointSee", a.GetHouseList_AppointSee)
}

// @Title GetAppointmentInfo
// @Description get appointment info
// @Success 200 {string}
// @Failure 403 body is empty
// @router /:id [get]
func (this *AppointmentController) GetAppointmentInfo() {
	FN := "[GetAppointmentInfo] "
	beego.Warn("[--- API: GetAppointmentInfo ---]")

	var result ResGetAppointmentInfo
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

	aid, _ := this.GetInt64(":id")

	// beego.Debug(FN, "type:", tp, ", begin:", begin, ", count:", count, ", uid:", uid)

	/*
	 *	Processing
	 */
	err, apt_info := models.GetAppointmentInfo(uid, aid)
	if nil == err {
		result.Appointment = apt_info
	}
}

// @Title MakeAppointmentAction
// @Description make a action for an appointment
// @Success 200 {string}
// @Failure 403 body is empty
// @router /:id/act [post]
func (this *AppointmentController) MakeAppointmentAction() {
	FN := "[MakeAppointmentAction] "
	beego.Warn("[--- API: MakeAppointmentAction ---]")

	var result ResAddResource
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

	aid, _ := this.GetInt64(":id")
	act, _ := this.GetInt("act")
	tb := this.GetString("tb")
	te := this.GetString("te")
	ac := this.GetString("ac")
	tmp, _ := base64.URLEncoding.DecodeString(ac)
	ac = string(tmp)

	// beego.Debug(FN, "type:", tp, ", begin:", begin, ", count:", count, ", uid:", uid)

	/*
	 *	Processing
	 */
	err, act_id := models.MakeAppointmentAction(uid, aid, act, tb, te, ac)
	if nil == err {
		result.Id = act_id
	}
}

// @Title GetHouseList_AppointSee
// @Description get house list of appoint house see
// @Success 200 {string}
// @Failure 403 body is empty
// @router /seeHouselist [get]
func (this *AppointmentController) GetHouseList_AppointSee() {
	FN := "[GetHouseList_AppointSee] "
	beego.Warn("[--- API: GetHouseList_AppointSee ---]")

	var result ResGetHouseDigestList
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

	begin, _ := this.GetInt64("bgn")
	count, _ := this.GetInt64("cnt")

	// beego.Debug(FN, "type:", tp, ", begin:", begin, ", count:", count, ", uid:", uid)

	/*
	 *	Processing
	 */
	err, total, fetched, ids := models.GetHouseList_AppointSee(begin, count, uid)
	if nil == err {
		result.Total = total
		if count > 0 {
			// result.Count = fetched
			// result.IDs = ids
			if fetched > 0 {
				for _, v := range ids {
					// beego.Debug(FN, "v:", fmt.Sprintf("%+v", v))
					hdi := commdef.HouseDigest{}
					err, hdi = models.GetHouseDigestInfo(v, uid)
					if nil != err {
						return
					}
					// beego.Debug(FN, "add")
					result.HouseDigests = append(result.HouseDigests, hdi)
				}
			}
			result.Count = int64(len(result.HouseDigests))
		} else {
			result.Count = -1
		}
	}
}

// @Title OrderSeeHouse
// @Description make a appointment to see house
// @Success 200 {string}
// @Failure 403 body is empty
// @router /see/house/:id [post]
func (this *AppointmentController) OrderSeeHouse() {
	FN := "[OrderSeeHouse] "
	beego.Warn("[--- API: OrderSeeHouse ---]")

	var result ResAddResource
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
		// return
	}

	hid, _ := this.GetInt64(":id")
	phone := this.GetString("phone")
	period_bgn := this.GetString("pb")
	period_end := this.GetString("pe")
	desc := this.GetString("desc")
	tmp, _ := base64.URLEncoding.DecodeString(desc)
	desc = string(tmp)

	err, aid := models.MakeAppointment(hid, uid, commdef.ORDER_TYPE_SEE_HOUSE, phone, period_bgn, period_end, desc)
	if nil == err {
		result.Id = aid
	}

	return
}

// @Title GetAppointList_SeeHouse
// @Description get the house see appointment list
// @Success 200 {string}
// @Failure 403 body is empty
// @router /house/:id/seelist [get]
func (this *AppointmentController) GetAppointList_SeeHouse() {
	FN := "[GetAppointList_SeeHouse] "
	beego.Warn("[--- API: GetAppointList_SeeHouse ---]")

	var result ResGetAppointmenLst_HouseSee
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
		// return
	}

	hid, _ := this.GetInt64(":id")
	bgn, _ := this.GetInt("bgn")
	fetchCnt, _ := this.GetInt("fCnt")

	err, total, hot := models.GetAppointList_SeeHouse(hid, uid, bgn, fetchCnt)
	if nil == err {
		result.Total = int64(total)
		if fetchCnt > 0 {
			result.Appointments = hot
		}
		result.Count = int64(len(result.Appointments))
		// beego.Debug(FN, fmt.Sprintf("%+v", result))
	}

	return
}
