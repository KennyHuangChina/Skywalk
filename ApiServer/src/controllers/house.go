package controllers

import (
	// "encoding/json"
	// "fmt"
	// "ApiServer/commdef"
	"ApiServer/models"
	"github.com/astaxie/beego"
)

type HouseController struct {
	beego.Controller
}

func (h *HouseController) URLMapping() {
	h.Mapping("GetHouseInfo", h.GetHouseInfo)
	h.Mapping("GetPropertyInfo", h.GetPropertyInfo)
	h.Mapping("GetHouseDigestInfo", h.GetHouseDigestInfo)
}

// @Title GetPropertyInfo
// @Description get property info by id
// @Success 200 {string}
// @Failure 403 body is empty
// @router /property/:id [get]
func (this *HouseController) GetPropertyInfo() {
	FN := "[GetPropertyInfo] "
	beego.Warn("[--- API: GetPropertyInfo ---]")

	var result ResGetPropInfo
	var err error

	defer func() {
		if nil != err {
			beego.Error(FN, err.Error())
		}

		// export result
		api_result(err, this.Controller, &result.ResCommon)
		this.Data["json"] = result
		this.ServeJSON()
	}()

	/*	Extract agreements */
	version := this.GetString("ver")
	pid, _ := this.GetInt64(":id")
	sid := this.GetString("sid")

	beego.Debug(FN, "ver:", version, ", pid:", pid, ", sid:", sid)

	/* Processing */
	err, pif := models.GetPropertyInfo(pid)
	if nil == err {
		result.PropInfo = pif
	}
}

// @Title GetHouseDigestInfo
// @Description get user info by id
// @Success 200 {string}
// @Failure 403 body is empty
// @router /digest/:id [get]
func (this *HouseController) GetHouseDigestInfo() {
	FN := "[GetHouseDigestInfo] "
	beego.Warn("[--- API: GetHouseDigestInfo ---]")

	var result ResGetUserDigest
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
	version := this.GetString("ver")
	hid, _ := this.GetInt64(":id")
	sid := this.GetString("sid")

	beego.Debug(FN, "ver:", version, ", hid:", hid, ", sid:", sid, ", uid:", uid)

	/*
	 *	Processing
	 */
	err, hd := models.GetHouseDigestInfo(hid, uid)
	if nil == err {
		result.HouseDigest = hd
	}
}

// @Title GetHouseInfo
// @Description get user info by id
// @Success 200 {string}
// @Failure 403 body is empty
// @router /:id [get]
func (this *HouseController) GetHouseInfo() {
	FN := "[GetHouseInfo] "
	beego.Warn("[--- API: GetHouseInfo ---]")

	var result ResGetUserInfo
	var err error

	defer func() {
		if nil != err {
			beego.Error(FN, err.Error())
		}

		// export result
		api_result(err, this.Controller, &result.ResCommon)
		this.Data["json"] = result
		this.ServeJSON()
	}()

	/*
	 *	Extract agreements
	 */
	version := this.GetString("ver")
	hid, _ := this.GetInt64(":id")
	sid := this.GetString("sid")

	beego.Debug(FN, "ver:", version, ", hid:", hid, ", sid:", sid)

	/*
	 *	Processing
	 */
	err, hif := models.GetHouseInfo(hid)
	if nil == err {
		result.HouseInfo = hif
	}
}
