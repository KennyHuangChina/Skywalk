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
}

// @Title OrderSeeHouse
// @Description make a appointment to see the house
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

	err, aid := models.MakeAppointment(hid, uid, commdef.ORDER_TYPE_SEE_APARTMENT, phone, period_bgn, period_end, desc)
	if nil == err {
		result.Id = aid
	}

	return
}
