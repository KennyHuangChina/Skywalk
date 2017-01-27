package controllers

import (
	// "encoding/json"
	// "fmt"
	"ApiServer/commdef"
	"ApiServer/models"
	"github.com/astaxie/beego"
)

type HouseController struct {
	beego.Controller
}

func (h *HouseController) URLMapping() {
	h.Mapping("GetHouseInfo", h.GetHouseInfo)
	h.Mapping("GetHouseDigestInfo", h.GetHouseDigestInfo)
	h.Mapping("GetHouseList", h.GetHouseList)
	h.Mapping("AddHouse", h.AddHouse)
	h.Mapping("ModifyHouse", h.ModifyHouse)

	h.Mapping("GetPropertyInfo", h.GetPropertyInfo)
	h.Mapping("GetPropertyList", h.GetPropertyList)
	h.Mapping("AddProperty", h.AddProperty)
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

// @Title AddProperty
// @Description get property list
// @Success 200 {string}
// @Failure 403 body is empty
// @router /property [post]
func (this *HouseController) AddProperty() {
	FN := "[AddProperty] "
	beego.Warn("[--- API: AddProperty ---]")

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
	/*uid*/ _, err = getLoginUser(this.Controller)
	if nil != err {
		return
	}

	prop := this.GetString("prop")
	beego.Debug(FN, "prop:", prop)

	/*
	 *	Processing
	 */
	err, id := models.AddProperty(prop)
	if nil == err {
		result.Id = id
	}
}

// @Title GetPropertyList
// @Description get property list
// @Success 200 {string}
// @Failure 403 body is empty
// @router /property/list [get]
func (this *HouseController) GetPropertyList() {
	FN := "[GetPropertyList] "
	beego.Warn("[--- API: GetPropertyList ---]")

	var result ResGetPropertyList
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

	name := this.GetString("name")
	begin, _ := this.GetInt64("bgn")
	count, _ := this.GetInt64("cnt")
	sid := this.GetString("sid")

	beego.Debug(FN, "type:", name, ", begin:", begin, ", count:", count, ", sid:", sid, ", uid:", uid)

	/*
	 *	Processing
	 */
	err, total, fetched, pl := models.GetPropertyList(name, begin, count)
	if nil == err {
		result.Total = total
		result.Count = fetched
		result.Properties = pl
	}
}

// @Title ModifyHouse
// @Description modify house info
// @Success 200 {string}
// @Failure 403 body is empty
// @router /:id [put]
func (this *HouseController) ModifyHouse() {
	FN := "[ModifyHouse] "
	beego.Warn("[--- API: ModifyHouse ---]")

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
	/*uid*/ _, err = getLoginUser(this.Controller)
	if nil != err {
		return
	}

	hid, _ := this.GetInt64(":id")
	prop, _ := this.GetInt64("prop")
	building_no, _ := this.GetInt("build")
	house_no := this.GetString("house")
	floor_total, _ := this.GetInt("floor_total")
	floor_this, _ := this.GetInt("floor_this")
	bedrooms, _ := this.GetInt("Bedrooms")
	livingrooms, _ := this.GetInt("LivingRooms")
	bathrooms, _ := this.GetInt("Bathrooms")
	acreage, _ := this.GetInt("Acreage")
	hif := commdef.HouseInfo{Id: hid, Property: prop, BuddingNo: building_no, FloorTotal: floor_total, FloorThis: floor_this,
		HouseNo: house_no, Bedrooms: bedrooms, Livingrooms: livingrooms, Bathrooms: bathrooms, Acreage: acreage}

	/*
	 *	Processing
	 */
	err = models.ModifyHouse(&hif)
	if nil == err {
		// result.Id = id
	}
}

// @Title AddHouse
// @Description add new house
// @Success 200 {string}
// @Failure 403 body is empty
// @router /commit [post]
func (this *HouseController) AddHouse() {
	FN := "[AddHouse] "
	beego.Warn("[--- API: AddHouse ---]")

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

	agent, _ := this.GetInt64("agen")
	prop, _ := this.GetInt64("prop")
	building_no, _ := this.GetInt("build")
	house_no := this.GetString("house")
	floor_total, _ := this.GetInt("floor_total")
	floor_this, _ := this.GetInt("floor_this")
	bedrooms, _ := this.GetInt("Bedrooms")
	livingrooms, _ := this.GetInt("LivingRooms")
	bathrooms, _ := this.GetInt("Bathrooms")
	acreage, _ := this.GetInt("Acreage")
	hif := commdef.HouseInfo{Property: prop, BuddingNo: building_no, FloorTotal: floor_total, FloorThis: floor_this,
		HouseNo: house_no, Bedrooms: bedrooms, Livingrooms: livingrooms, Bathrooms: bathrooms, Acreage: acreage}

	/*
	 *	Processing
	 */
	err, id := models.AddHouse(&hif, uid, agent)
	if nil == err {
		result.Id = id
	}
}

// @Title GetHouseList
// @Description get house list by type
// @Success 200 {string}
// @Failure 403 body is empty
// @router /list [get]
func (this *HouseController) GetHouseList() {
	FN := "[GetHouseList] "
	beego.Warn("[--- API: GetHouseList ---]")

	var result ResGetHouseList
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

	tp, _ := this.GetInt("type")
	begin, _ := this.GetInt64("bgn")
	count, _ := this.GetInt64("cnt")
	sid := this.GetString("sid")

	beego.Debug(FN, "type:", tp, ", begin:", begin, ", count:", count, ", sid:", sid, ", uid:", uid)

	/*
	 *	Processing
	 */
	err, total, fetched, ids := models.GetHouseListByType(tp, begin, count)
	if nil == err {
		result.Total = total
		result.Count = fetched
		result.IDs = ids
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

	var result ResGetHouseDigest
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
