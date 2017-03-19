package controllers

import (
	// "encoding/json"
	//"ApiServer/commdef"
	"ApiServer/models"
	// "encoding/base64"
	// "fmt"
	"github.com/astaxie/beego"
)

type AccessoryController struct {
	beego.Controller
}

func (a *AccessoryController) URLMapping() {
	a.Mapping("GetHouseFacilities", a.GetHouseFacilities)
}

// @Title GetHouseFacilities
// @Description get house facility list
// @Success 200 {string}
// @Failure 403 body is empty
// @router /house/:id/facilities [get]
func (this *AccessoryController) GetHouseFacilities() {
	FN := "[GetHouseFacilities] "
	beego.Warn("[--- API: GetHouseFacilities ---]")

	var result ResGetHouseFacilities
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
	// uid, err := getLoginUser(this.Controller)
	// if nil != err {
	// 	return
	// }

	hid, _ := this.GetInt64(":id")

	/*
	 *	Processing
	 */
	err, lst := models.GetHouseFacilities(hid)
	if nil == err {
		result.Facilities = lst
		result.Total = int64(len(lst))
	}
}
