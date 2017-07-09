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
	h.Mapping("GetHouseDigestList", h.GetHouseDigestList)
	h.Mapping("CommitHouseByOwner", h.CommitHouseByOwner)
	h.Mapping("ModifyHouse", h.ModifyHouse)

	h.Mapping("SetHouseShowTime", h.SetHouseShowTime)
	h.Mapping("GetHouseShowTime", h.GetHouseShowTime)

	h.Mapping("SetHousePrice", h.SetHousePrice)
	h.Mapping("GetHousePrice", h.GetHousePrice)

	h.Mapping("CertHouse", h.CertHouse)
	h.Mapping("SetHouseCoverImage", h.SetHouseCoverImage)
	h.Mapping("SetHouseAgency", h.SetHouseAgency)
	h.Mapping("RecommendHouse", h.RecommendHouse)
	h.Mapping("GetBehalfList", h.GetBehalfList)
}

// @Title GetHouseShowTime
// @Description get the house showing time
// @Success 200 {string}
// @Failure 403 body is empty
// @router /:id/showtime [get]
func (this *HouseController) GetHouseShowTime() {
	FN := "[GetHouseShowTime] "
	beego.Warn("[--- API: GetHouseShowTime ---]")

	var result ResGetHouseShowtime
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

	err, hst := models.GetHouseShowTime(hid, uid)
	if nil == err {
		result.HouseShowTime = hst
	}

	return
}

// @Title SetHouseShowTime
// @Description set the house showing time
// @Success 200 {string}
// @Failure 403 body is empty
// @router /:id/showtime [put]
func (this *HouseController) SetHouseShowTime() {
	FN := "[SetHouseShowTime] "
	beego.Warn("[--- API: SetHouseShowTime ---]")

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
	prdw, _ := this.GetInt("prdw")
	prdv, _ := this.GetInt("prdv")
	desc := this.GetString("desc")

	err = models.SetHouseShowTime(hid, uid, prdw, prdv, desc)
	if nil == err {
	}

	return
}

// @Title GetHousePrice
// @Description get house price history, incouding rental and selling price
// @Success 200 {string}
// @Failure 403 body is empty
// @router /:id/price [get]
func (this *HouseController) GetHousePrice() {
	FN := "[GetHousePrice] "
	beego.Warn("[--- API: GetHousePrice ---]")

	var result ResGetHousePriceList
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
	bgn, _ := this.GetInt("bgn")
	cnt, _ := this.GetInt("cnt")

	/*
	 *	Processing
	 */
	err, total, pl := models.GetHousePrice(hid, uid, bgn, cnt)
	if nil == err {
		result.Total = total
		result.Count = int64(len(pl))
		result.Prices = pl
	}

	return
}

// @Title SetHousePrice
// @Description set house price, incouding rental and selling price
// @Success 200 {string}
// @Failure 403 body is empty
// @router /:id/price [post]
func (this *HouseController) SetHousePrice() {
	FN := "[SetHousePrice] "
	beego.Warn("[--- API: SetHousePrice ---]")

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
	rental_tp, _ := this.GetInt64("r_tp")
	rental_bp, _ := this.GetInt64("r_bp")
	prop_fee, _ := this.GetBool("pf")
	price_tp, _ := this.GetInt64("p_tp")
	price_bp, _ := this.GetInt64("p_bp")

	/*
	 *	Processing
	 */
	err, pid := models.SetHousePrice(hid, uid, rental_tp, rental_bp, price_tp, price_bp, prop_fee)
	if nil == err {
		result.Id = pid
	}

	return
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
	buy_date := this.GetString("buy_date")

	hif := commdef.HouseInfo{Id: hid, Property: prop, BuildingNo: building_no, FloorTotal: floor_total, FloorThis: floor_this,
		HouseNo: house_no, Bedrooms: bedrooms, Livingrooms: livingrooms, Bathrooms: bathrooms, Acreage: acreage,
		ForSale: _4sale, ForRent: _4rent, Decoration: decor, BuyDate: buy_date}

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
		// TODO: use house digest list, and fetch count set to -1 if count is 0 which means user just want to fetch the total number
	}
}

// @Title GetHouseDigestList
// @Description get house list by type, in digest info
// @Success 200 {string}
// @Failure 403 body is empty
// @router /list [get]
func (this *HouseController) GetHouseDigestList() {
	FN := "[GetHouseDigestList] "
	beego.Warn("[--- API: GetHouseDigestList ---]")

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
		// return
	}

	tp, _ := this.GetInt("type")
	begin, _ := this.GetInt64("bgn")
	count, _ := this.GetInt64("cnt")
	// sid := this.GetString("sid")
	err, filter := getHouseFilter(this)
	sort := this.GetString("sort")
	if nil != err {
		return
	}

	// beego.Debug(FN, "type:", tp, ", begin:", begin, ", count:", count)

	/*
	 *	Processing
	 */
	err, total, fetched, ids := models.GetHouseListByType(tp, begin, count, filter, sort)
	beego.Debug(FN, "ids:", ids, ", fetched:", fetched)
	if nil == err {
		result.Total = total
		if 0 == count {
			result.Count = -1
		} else {
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
		}
	}
}

// @Title GetHouseDigestInfo
// @Description get user info by id
// @Success 200 {string}
// @Failure 403 body is empty
// @router /:id/digest [get]
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
		// return
	}
	version := this.GetString("ver")
	hid, _ := this.GetInt64(":id")
	// sid := this.GetString("sid")

	beego.Debug(FN, "ver:", version, ", hid:", hid) //, ", sid:", sid, ", uid:", uid)

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
	be, _ := this.GetBool("be")

	beego.Debug(FN, "ver:", version, ", hid:", hid, ", back end:", be, ", login user:", uid)
	// if 0 == pri {
	// 	uid = -1
	// }

	/*
	 *	Processing
	 */
	err, hif := models.GetHouseInfo(hid, uid, be)
	if nil == err {
		result.HouseInfo = hif
		beego.Debug(FN, fmt.Sprintf("%+v", result.HouseInfo))
	}
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
func getHouseFilter(this *HouseController) (err error, filter models.HouseFilter) {

	nVal := int64(0)

	// Rental
	if nOp, errT := this.GetInt("rtop"); nil == errT && nOp > 0 {
		if nVal, errT = this.GetInt64("rt1"); nil != errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("Fail to get value for Rental lower, err:%s", errT.Error())}
			return
		}
		filter.Rental.Operator = nOp
		filter.Rental.Value1 = nVal
		if commdef.HOUSE_FILTER_TYPE_BETWEEN == filter.Rental.Operator {
			if nVal, errT = this.GetInt64("rt2"); nil != errT {
				err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("Fail to get value for Rental upper, err:%s", errT.Error())}
				return
			}
			filter.Rental.Value2 = nVal
		}
	}

	// Livingroom
	if nOp, errT := this.GetInt("lvop"); nil == errT && nOp > 0 {
		if nVal, errT = this.GetInt64("lr1"); nil != errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("Fail to get value for Livingroom lower, err:%s", errT.Error())}
			return
		}
		filter.Livingroom.Operator = nOp
		filter.Livingroom.Value1 = nVal
		if commdef.HOUSE_FILTER_TYPE_BETWEEN == filter.Livingroom.Operator {
			if nVal, errT = this.GetInt64("lr2"); nil != errT {
				err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("Fail to get value for Livingroom upper, err:%s", errT.Error())}
				return
			}
			filter.Livingroom.Value2 = nVal
		}
	}

	// Bedroom
	if nOp, errT := this.GetInt("berop"); nil == errT && nOp > 0 {
		if nVal, errT = this.GetInt64("ber1"); nil != errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("Fail to get value for Bedroom lower, err:%s", errT.Error())}
			return
		}
		filter.Bedroom.Operator = nOp
		filter.Bedroom.Value1 = nVal
		if commdef.HOUSE_FILTER_TYPE_BETWEEN == filter.Bedroom.Operator {
			if nVal, errT = this.GetInt64("ber2"); nil != errT {
				err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("Fail to get value for Bedroom upper, err:%s", errT.Error())}
				return
			}
			filter.Bedroom.Value2 = nVal
		}
	}

	// Bathroom
	if nOp, errT := this.GetInt("barop"); nil == errT && nOp > 0 {
		if nVal, errT = this.GetInt64("bar1"); nil != errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("Fail to get value for Bathroom lower, err:%s", errT.Error())}
			return
		}
		filter.Bathroom.Operator = nOp
		filter.Bathroom.Value1 = nVal
		if commdef.HOUSE_FILTER_TYPE_BETWEEN == filter.Bathroom.Operator {
			if nVal, errT = this.GetInt64("bar2"); nil != errT {
				err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("Fail to get value for Bathroom upper, err:%s", errT.Error())}
				return
			}
			filter.Bathroom.Value2 = nVal
		}
	}

	// Acreage
	if nOp, errT := this.GetInt("acop"); nil == errT && nOp > 0 {
		if nVal, errT = this.GetInt64("ac1"); nil != errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("Fail to get value for Acreage lower, err:%s", errT.Error())}
			return
		}
		filter.Acreage.Operator = nOp
		filter.Acreage.Value1 = nVal
		if commdef.HOUSE_FILTER_TYPE_BETWEEN == filter.Acreage.Operator {
			if nVal, errT = this.GetInt64("ac2"); nil != errT {
				err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("Fail to get value for Acreage upper, err:%s", errT.Error())}
				return
			}
			filter.Acreage.Value2 = nVal
		}
	}

	return
}
