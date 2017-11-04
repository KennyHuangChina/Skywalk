package controllers

import (
	// "encoding/json"
	"ApiServer/commdef"
	"ApiServer/models"
	"encoding/base64"
	"fmt"
	"html/template"
	// "text/template"
	// "crypto/rand"
	// "encoding/hex"
	"github.com/astaxie/beego"
	// "github.com/astaxie/beego/cache"
	"strconv"
	"strings"
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
	h.Mapping("GetHouseCertHist", h.GetHouseCertHist)
	h.Mapping("RecommitHouseCert", h.RecommitHouseCert)

	h.Mapping("SetHouseCoverImage", h.SetHouseCoverImage)
	h.Mapping("SetHouseAgency", h.SetHouseAgency)
	h.Mapping("RecommendHouse", h.RecommendHouse)
	h.Mapping("GetBehalfList", h.GetBehalfList)

	h.Mapping("LandlordSubmitHouseConfirm", h.LandlordSubmitHouseConfirm)
}

type LandlordNewHouse struct {
	Landlord   string
	Estate     string
	BuildingNo string
	HouseNo    string
}

// @Title LandlordSubmitHouseConfirm
// @Description get the house showing time
// @Success 200 {string}
// @Failure 403 body is empty
// @router /LandlordSubmitHouseContract [get]
func (this *HouseController) LandlordSubmitHouseConfirm() {
	FN := "[LandlordSubmitHouseConfirm] "
	beego.Warn("[--- API: LandlordSubmitHouseConfirm ---]")

	/*
	 *	Extract agreements
	 */
	uid := int64(4)
	// uid, err := getLoginUser(this.Controller)
	// if nil != err {
	// 	return
	// }
	eid, _ := this.GetInt64("ett")
	build_no := this.GetString("bn")
	house_no := this.GetString("hn")

	err, est := models.GetPropertyInfo(eid)
	if nil != err {
		return
	}
	err, u := models.GetUser(uid)
	if nil != err {
		return
	}

	tplFile := "views/LandlordSubmitHouseContract.html"
	info := LandlordNewHouse{u.Name, est.PropName, build_no, house_no}
	beego.Debug(FN, "info:", info)

	tmpl, err := template.ParseFiles(tplFile)
	if nil != err {
		beego.Error(FN, "err:", err)
		this.Data["json"] = fmt.Sprintf("Error: %s", err)
		this.ServeJSON()
		return
	}
	tmpl.Execute(this.Controller.Ctx.ResponseWriter, info)

	return
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

// @Title GetHouseCertHist
// @Description get house certification history
// @Success 200 {string}
// @Failure 403 body is empty
// @router /:id/certhist [get]
func (this *HouseController) GetHouseCertHist() {
	FN := "[GetHouseCertHist] "
	beego.Warn("[--- API: GetHouseCertHist ---]")

	var result ResGetHouseCertHist
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
	err, hcs, ops := models.GetHouseCertHist(hid, uid)
	if nil == err {
		result.Total = len(hcs)
		result.Count = len(hcs)
		result.CertHist = hcs
		result.Ops = ops
	}

	return
}

// @Title RecommitHouseCert
// @Description recommit the house certification request
// @Success 200 {string}
// @Failure 403 body is empty
// @router /:id/recert [post]
func (this *HouseController) RecommitHouseCert() {
	FN := "[RecommitHouseCert] "
	beego.Warn("[--- API: RecommitHouseCert ---]")

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
	comment := this.GetString("cc")
	tmp, _ := base64.URLEncoding.DecodeString(comment)
	comment = string(tmp)

	/*
	 *	Processing
	 */
	err, hcid := models.RecommitHouseCert(hid, uid, comment)
	if nil == err {
		result.Id = hcid
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
	if be && uid <= 0 {
		return
	}

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
func getFilterValues(strVal string) (vs []int64) {
	// FN := "[getFilterValues] "

	vals := []int64{}
	strVals := strings.Split(strVal, ",")
	// beego.Debug(FN, fmt.Sprintf("strVals:%+v", strVals))
	for _, v := range strVals {
		if 0 == len(v) {
			continue
		}
		i, errT := strconv.Atoi(v)
		if nil != errT {
			return
		}
		vals = append(vals, int64(i))
		// beego.Debug(FN, "vals:", fmt.Sprintf("%+v", vals))
	}

	vs = vals
	// beego.Debug(FN, "vs:", fmt.Sprintf("%+v", vs))
	return
}

func getHouseFilter(this *HouseController) (err error, filter models.HouseFilter) {
	// FN := "[getHouseFilter] "

	// Rental
	if nOp, errT := this.GetInt("rtop"); nil == errT && nOp > 0 {
		strVals := this.GetString("rt")
		if 0 == len(strVals) {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: "Fail to get value for Rental"}
			return
		}
		filter.Rental.Operator = nOp
		filter.Rental.Values = getFilterValues(strVals)
	}

	// Livingroom
	if nOp, errT := this.GetInt("lvop"); nil == errT && nOp > 0 {
		strVals := this.GetString("lr")
		if 0 == len(strVals) {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: "Fail to get value for Livingroom"}
			return
		}
		filter.Livingroom.Operator = nOp
		filter.Livingroom.Values = getFilterValues(strVals)
	}

	// Bedroom
	if nOp, errT := this.GetInt("berop"); nil == errT && nOp > 0 {
		strVals := this.GetString("ber")
		if 0 == len(strVals) {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: "Fail to get value for Bedroom"}
			return
		}
		filter.Bedroom.Operator = nOp
		filter.Bedroom.Values = getFilterValues(strVals)
	}

	// Bathroom
	if nOp, errT := this.GetInt("barop"); nil == errT && nOp > 0 {
		strVals := this.GetString("bar")
		if 0 == len(strVals) {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: "Fail to get value for Bathroom"}
			return
		}
		filter.Bathroom.Operator = nOp
		filter.Bathroom.Values = getFilterValues(strVals)
	}

	// Acreage
	if nOp, errT := this.GetInt("acop"); nil == errT && nOp > 0 {
		strVals := this.GetString("ac")
		// beego.Debug(FN, "ac:", strVals)
		if 0 == len(strVals) {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: "Fail to get value for Acreage"}
			return
		}
		filter.Acreage.Operator = nOp
		filter.Acreage.Values = getFilterValues(strVals)
		// beego.Debug(FN, "Acreage:", fmt.Sprintf("%+v", filter.Acreage.Values))
	}

	// Property
	if nOp, errT := this.GetInt("pop"); nil == errT && nOp > 0 {
		strVals := this.GetString("prop")
		// beego.Debug(FN, "ac:", strVals)
		if 0 == len(strVals) {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: "Fail to get value for property"}
			return
		}
		filter.Property.Operator = nOp
		filter.Property.Values = getFilterValues(strVals)
		// beego.Debug(FN, "Property:", fmt.Sprintf("%+v", filter.Property.Values))
	}

	return
}
