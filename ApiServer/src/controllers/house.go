package controllers

import (
	// "encoding/json"
	"ApiServer/commdef"
	"ApiServer/models"
	"encoding/base64"
	"fmt"
	"github.com/astaxie/beego"
)

type HouseController struct {
	beego.Controller
}

func (h *HouseController) URLMapping() {
	h.Mapping("GetHouseInfo", h.GetHouseInfo)
	h.Mapping("GetHouseDigestInfo", h.GetHouseDigestInfo)
	h.Mapping("GetHouseList", h.GetHouseList)
	h.Mapping("CommitHouseByOwner", h.CommitHouseByOwner)
	h.Mapping("ModifyHouse", h.ModifyHouse)
	h.Mapping("CertHouse", h.CertHouse)
	h.Mapping("SetHouseCoverImage", h.SetHouseCoverImage)
	h.Mapping("SetHouseAgency", h.SetHouseAgency)
	h.Mapping("RecommendHouse", h.RecommendHouse)
	h.Mapping("GetBehalfList", h.GetBehalfList)
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
	uid, err := getLoginUser(this.Controller)
	if nil != err {
		return
	}

	hid, _ := this.GetInt64(":id")
	pass, _ := this.GetBool("ps")
	comment := this.GetString("cc")
	tmp, _ := base64.URLEncoding.DecodeString(comment)
	comment = string(tmp)

	/*
	 *	Processing
	 */
	err = models.CertHouse(hid, uid, pass, comment)
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
	uid, err := getLoginUser(this.Controller)
	if nil != err {
		return
	}

	hid, _ := this.GetInt64(":id")
	cid, _ := this.GetInt64("img")

	/*
	 *	Processing
	 */
	err = models.SetHouseCoverImage(hid, cid, uid)
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
	uid, err := getLoginUser(this.Controller)
	if nil != err {
		return
	}

	hid, _ := this.GetInt64(":id")
	aid, _ := this.GetInt64("agent")

	/*
	 *	Processing
	 */
	err = models.SetHouseAgency(hid, aid, uid)
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
	uid, err := getLoginUser(this.Controller)
	if nil != err {
		return
	}

	hid, _ := this.GetInt64(":id")
	prop, _ := this.GetInt64("prop")
	building_no := this.GetString("build")
	house_no := this.GetString("house")
	floor_total, _ := this.GetInt("floor_total")
	floor_this, _ := this.GetInt("floor_this")
	bedrooms, _ := this.GetInt("Bedrooms")
	livingrooms, _ := this.GetInt("LivingRooms")
	bathrooms, _ := this.GetInt("Bathrooms")
	acreage, _ := this.GetInt("Acreage")
	_4sale, _ := this.GetBool("4sale")
	_4rent, _ := this.GetBool("4rent")
	decor, _ := this.GetInt("decor")

	hif := commdef.HouseInfo{Id: hid, Property: prop, BuildingNo: building_no, FloorTotal: floor_total, FloorThis: floor_this,
		HouseNo: house_no, Bedrooms: bedrooms, Livingrooms: livingrooms, Bathrooms: bathrooms, Acreage: acreage,
		ForSale: _4sale, ForRent: _4rent, Decoration: decor}

	/*
	 *	Processing
	 */
	err = models.ModifyHouse(&hif, uid)
	if nil == err {
		// result.Id = id
	}
}

// @Title CommitHouseByOwner
// @Description commit new house by owner
// @Success 200 {string}
// @Failure 403 body is empty
// @router /commit [post]
func (this *HouseController) CommitHouseByOwner() {
	FN := "[CommitHouseByOwner] "
	beego.Warn("[--- API: CommitHouseByOwner ---]")

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
	building_no := this.GetString("build")
	house_no := this.GetString("house")
	floor_total, _ := this.GetInt("floor_total")
	floor_this, _ := this.GetInt("floor_this")
	bedrooms, _ := this.GetInt("Bedrooms")
	livingrooms, _ := this.GetInt("LivingRooms")
	bathrooms, _ := this.GetInt("Bathrooms")
	acreage, _ := this.GetInt("Acreage")
	_4sale, _ := this.GetBool("4sale")
	_4rent, _ := this.GetBool("4rent")
	decor, _ := this.GetInt("decor")
	buy_date := this.GetString("buy_date")

	hif := commdef.HouseInfo{Property: prop, BuildingNo: building_no, FloorTotal: floor_total, FloorThis: floor_this,
		HouseNo: house_no, Bedrooms: bedrooms, Livingrooms: livingrooms, Bathrooms: bathrooms, Acreage: acreage,
		ForSale: _4sale, ForRent: _4rent, Decoration: decor, BuyDate: buy_date}

	/*
	 *	Processing
	 */
	err, id := models.CommitHouseByOwner(&hif, uid, agent)
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
	// uid, err := getLoginUser(this.Controller)
	// if nil != err {
	// 	return
	// }

	tp, _ := this.GetInt("type")
	begin, _ := this.GetInt64("bgn")
	count, _ := this.GetInt64("cnt")
	// sid := this.GetString("sid")

	// beego.Debug(FN, "type:", tp, ", begin:", begin, ", count:", count, ", sid:", sid, ", uid:", uid)

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
	// uid, err := getLoginUser(this.Controller)
	// if nil != err {
	// 	return
	// }
	version := this.GetString("ver")
	hid, _ := this.GetInt64(":id")
	// sid := this.GetString("sid")

	beego.Debug(FN, "ver:", version, ", hid:", hid) //, ", sid:", sid, ", uid:", uid)

	/*
	 *	Processing
	 */
	err, hd := models.GetHouseDigestInfo(hid)
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
	version := this.GetString("ver")
	hid, _ := this.GetInt64(":id")
	pri, _ := this.GetInt("p")

	beego.Debug(FN, "ver:", version, ", hid:", hid, ", private:", pri, ", login user:", uid)
	// if 0 == pri {
	// 	uid = -1
	// }

	/*
	 *	Processing
	 */
	err, hif := models.GetHouseInfo(hid, uid)
	if nil == err {
		result.HouseInfo = hif
		beego.Debug(FN, fmt.Sprintf("%+v", result.HouseInfo))
	}
}
