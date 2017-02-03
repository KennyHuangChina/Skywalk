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
	h.Mapping("CertHouse", h.CertHouse)
	h.Mapping("SetHouseCoverImage", h.SetHouseCoverImage)
	h.Mapping("SetHouseAgency", h.SetHouseAgency)
	h.Mapping("RecommendHouse", h.RecommendHouse)
	h.Mapping("GetBehalfList", h.GetBehalfList)

	h.Mapping("GetPropertyInfo", h.GetPropertyInfo)
	h.Mapping("GetPropertyList", h.GetPropertyList)
	h.Mapping("AddProperty", h.AddProperty)
	h.Mapping("UpdateProperty", h.UpdateProperty)

	h.Mapping("AddDeliverable", h.AddDeliverable)
	h.Mapping("GetDeliverableList", h.GetDeliverableList)
	h.Mapping("NewHouseDeliverable", h.NewHouseDeliverable)
	h.Mapping("GetHouseDeliverableList", h.GetHouseDeliverableList)

	h.Mapping("AddFacilityType", h.AddFacilityType)
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

// @Title UpdateProperty
// @Description get property list
// @Success 200 {string}
// @Failure 403 body is empty
// @router /property/:id [put]
func (this *HouseController) UpdateProperty() {
	FN := "[UpdateProperty] "
	beego.Warn("[--- API: UpdateProperty ---]")

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

	pid, _ := this.GetInt64(":id")
	name := this.GetString("name")
	addr := this.GetString("addr")
	desc := this.GetString("desc")
	// beego.Debug(FN, "pid:", pid)

	/*
	 *	Processing
	 */
	err = models.ModifyProperty(pid, name, addr, desc)
	if nil == err {
		// result.Id = id
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

// @Title GetDeliverableList
// @Description add new deliverable
// @Success 200 {string}
// @Failure 403 body is empty
// @router /delivelst [get]
func (this *HouseController) GetDeliverableList() {
	FN := "[GetDeliverableList] "
	beego.Warn("[--- API: GetDeliverableList ---]")

	var result ResGetDeliverables
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
	err, lst := models.GetDeliverables(uid)
	if nil == err {
		result.Total = int64(len(lst))
		result.Deliverables = lst
	}
}

// @Title AddFacilityType
// @Description add new facility type
// @Success 200 {string}
// @Failure 403 body is empty
// @router /facilitytype [post]
func (this *HouseController) AddFacilityType() {
	FN := "[AddFacilityType] "
	beego.Warn("[--- API: AddFacilityType ---]")

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

	name := this.GetString("name")
	beego.Debug(FN, "name:", name)

	/*
	 *	Processing
	 */
	err, id := models.AddFacilityType(name, uid)
	if nil == err {
		result.Id = id
	}
}

// @Title AddDeliverable
// @Description add new deliverable
// @Success 200 {string}
// @Failure 403 body is empty
// @router /deliverable [post]
func (this *HouseController) AddDeliverable() {
	FN := "[AddDeliverable] "
	beego.Warn("[--- API: AddDeliverable ---]")

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

	name := this.GetString("name")
	beego.Debug(FN, "name:", name)

	/*
	 *	Processing
	 */
	err, id := models.AddDeliverable(name, uid)
	if nil == err {
		result.Id = id
	}
}

// @Title GetHouseDeliverableList
// @Description add new house deliverable
// @Success 200 {string}
// @Failure 403 body is empty
// @router /hdl/:id [get]
func (this *HouseController) GetHouseDeliverableList() {
	FN := "[GetHouseDeliverableList] "
	beego.Warn("[--- API: GetHouseDeliverableList ---]")

	var result ResGetHouseDeliverables
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

	/*
	 *	Processing
	 */
	err, lst := models.GetHouseDeliverableList(uid, hid)
	if nil == err {
		result.Total = int64(len(lst))
		result.Deliverables = lst
	}
}

// @Title NewHouseDeliverable
// @Description add new house deliverable
// @Success 200 {string}
// @Failure 403 body is empty
// @router /housedeliv/:id [post]
func (this *HouseController) NewHouseDeliverable() {
	FN := "[NewHouseDeliverable] "
	beego.Warn("[--- API: NewHouseDeliverable ---]")

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

	hid, _ := this.GetInt64(":id")
	did, _ := this.GetInt64("did")
	qty, _ := this.GetInt("qty")
	desc := this.GetString("desc")

	/*
	 *	Processing
	 */
	err, id := models.AddHouseDeliverable(uid, hid, did, qty, desc)
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

// @Title CertHouse
// @Description modify house info
// @Success 200 {string}
// @Failure 403 body is empty
// @router /cert/:id [post]
func (this *HouseController) CertHouse() {
	FN := "[CertHouse] "
	beego.Warn("[--- API: CertHouse ---]")

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

	/*
	 *	Processing
	 */
	err = models.CertHouse(hid)
	if nil == err {
		// result.Id = id
	}

	return
}

// @Title SetHouseCoverImage
// @Description set house cover image
// @Success 200 {string}
// @Failure 403 body is empty
// @router /covimg/:id [put]
func (this *HouseController) SetHouseCoverImage() {
	FN := "[SetHouseCoverImage] "
	beego.Warn("[--- API: SetHouseCoverImage ---]")

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
	cid, _ := this.GetInt64("img")

	/*
	 *	Processing
	 */
	err = models.SetHouseCoverImage(hid, cid)
	if nil == err {
		// result.Id = id
	}

	return
}

// @Title RecommendHouse
// @Description recommend/unrecomment house
// @Success 200 {string}
// @Failure 403 body is empty
// @router /recommend/:id [put]
func (this *HouseController) RecommendHouse() {
	FN := "[RecommendHouse] "
	beego.Warn("[--- API: RecommendHouse ---]")

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

	hid, _ := this.GetInt64(":id")
	act, _ := this.GetInt("act")

	/*
	 *	Processing
	 */
	err = models.RecommendHouse(hid, uid, act)
	if nil == err {
		// result.Id = id
	}

	return
}

// @Title SetHouseAgency
// @Description set house agency
// @Success 200 {string}
// @Failure 403 body is empty
// @router /agency/:id [put]
func (this *HouseController) SetHouseAgency() {
	FN := "[SetHouseAgency] "
	beego.Warn("[--- API: SetHouseAgency ---]")

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
	aid, _ := this.GetInt64("agent")

	/*
	 *	Processing
	 */
	err = models.SetHouseAgency(hid, aid)
	if nil == err {
		// result.Id = id
	}

	return
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
	_4sale, _ := this.GetBool("4sale")
	_4rent, _ := this.GetBool("4rent")
	hif := commdef.HouseInfo{Id: hid, Property: prop, BuddingNo: building_no, FloorTotal: floor_total, FloorThis: floor_this,
		HouseNo: house_no, Bedrooms: bedrooms, Livingrooms: livingrooms, Bathrooms: bathrooms, Acreage: acreage, ForSale: _4sale, ForRent: _4rent}

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
	_4sale, _ := this.GetBool("4sale")
	_4rent, _ := this.GetBool("4rent")
	hif := commdef.HouseInfo{Property: prop, BuddingNo: building_no, FloorTotal: floor_total, FloorThis: floor_this,
		HouseNo: house_no, Bedrooms: bedrooms, Livingrooms: livingrooms, Bathrooms: bathrooms, Acreage: acreage, ForSale: _4sale, ForRent: _4rent}

	/*
	 *	Processing
	 */
	err, id := models.AddHouse(&hif, uid, agent)
	if nil == err {
		result.Id = id
	}
}

// @Title GetBehalfList
// @Description get house list by type
// @Success 200 {string}
// @Failure 403 body is empty
// @router /behalf [get]
func (this *HouseController) GetBehalfList() {
	FN := "[GetBehalfList] "
	beego.Warn("[--- API: GetBehalfList ---]")

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

	// beego.Debug(FN, "type:", tp, ", begin:", begin, ", count:", count, ", uid:", uid)

	/*
	 *	Processing
	 */
	err, total, fetched, ids := models.GetBehalfList(tp, begin, count, uid)
	if nil == err {
		result.Total = total
		result.Count = fetched
		result.IDs = ids
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

	var result ResGetHouseInfo
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
