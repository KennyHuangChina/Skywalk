package controllers

import (
	// "encoding/json"
	"ApiServer/commdef"
	"ApiServer/models"
	"encoding/base64"
	"fmt"
	"github.com/astaxie/beego"
)

type AccessoryController struct {
	beego.Controller
}

func (a *AccessoryController) URLMapping() {
	a.Mapping("AddDeliverable", a.AddDeliverable)
	a.Mapping("GetDeliverableList", a.GetDeliverableList)
	a.Mapping("NewHouseDeliverable", a.NewHouseDeliverable)
	a.Mapping("GetHouseDeliverableList", a.GetHouseDeliverableList)

	a.Mapping("AddFacilityType", a.AddFacilityType)
	a.Mapping("GetFacilityTypeList", a.GetFacilityTypeList)
	a.Mapping("AddFacility", a.AddFacility)
	a.Mapping("GetFacilityList", a.GetFacilityList)
	a.Mapping("AddHouseFacilities", a.AddHouseFacilities)
	a.Mapping("GetHouseFacilities", a.GetHouseFacilities)
}

// @Title AccessoryController
// @Description add new deliverable
// @Success 200 {string}
// @Failure 403 body is empty
// @router /deliverable [post]
func (this *AccessoryController) AddDeliverable() {
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
	tmp, _ := base64.URLEncoding.DecodeString(name)
	name = string(tmp)
	beego.Debug(FN, "name:", name)

	/*
	 *	Processing
	 */
	err, id := models.AddDeliverable(name, uid)
	if nil == err {
		result.Id = id
	}
}

// @Title GetDeliverableList
// @Description add new deliverable
// @Success 200 {string}
// @Failure 403 body is empty
// @router /deliverables [get]
func (this *AccessoryController) GetDeliverableList() {
	FN := "[GetDeliverableList] "
	beego.Warn("[--- API: GetDeliverableList ---]")

	var result ResGetCommonList
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
		result.List = lst
	}
}

// @Title NewHouseDeliverable
// @Description add new house deliverable
// @Success 200 {string}
// @Failure 403 body is empty
// @router /house/:id/deliverable [post]
func (this *AccessoryController) NewHouseDeliverable() {
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
	tmp, _ := base64.URLEncoding.DecodeString(desc)
	desc = string(tmp)

	/*
	 *	Processing
	 */
	err, id := models.AddHouseDeliverable(uid, hid, did, qty, desc)
	if nil == err {
		result.Id = id
	}
}

// @Title GetHouseDeliverableList
// @Description add new house deliverable
// @Success 200 {string}
// @Failure 403 body is empty
// @router /house/:id/deliverables [get]
func (this *AccessoryController) GetHouseDeliverableList() {
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

// @Title AddFacilityType
// @Description add new facility type
// @Success 200 {string}
// @Failure 403 body is empty
// @router /facility/type [post]
func (this *AccessoryController) AddFacilityType() {
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
	// beego.Debug(FN, "name:", name)
	tmp, _ := base64.URLEncoding.DecodeString(name)
	name = string(tmp)
	beego.Debug(FN, "name:", name)

	/*
	 *	Processing
	 */
	err, id := models.AddFacilityType(name, uid)
	if nil == err {
		result.Id = id
	}
}

// @Title GetFacilityTypeList
// @Description get facility type list
// @Success 200 {string}
// @Failure 403 body is empty
// @router /facitypelst [get]
func (this *AccessoryController) GetFacilityTypeList() {
	FN := "[GetFacilityTypeList] "
	beego.Warn("[--- API: GetFacilityTypeList ---]")

	var result ResGetCommonList
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
	err, lst := models.GetFacilityTypeList(uid)
	if nil == err {
		result.List = lst
	}
}

// @Title AddFacility
// @Description add new facility type
// @Success 200 {string}
// @Failure 403 body is empty
// @router /facility [post]
func (this *AccessoryController) AddFacility() {
	FN := "[AddFacility] "
	beego.Warn("[--- API: AddFacility ---]")

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

	ft, _ := this.GetInt64("type")
	name := this.GetString("name")
	tmp, _ := base64.URLEncoding.DecodeString(name)
	name = string(tmp)
	// beego.Debug(FN, "name:", name, ", ft:", ft)

	/*
	 *	Processing
	 */
	err, id := models.AddFacility(name, ft, uid)
	if nil == err {
		result.Id = id
	}
}

// @Title GetFacilityList
// @Description get facility list
// @Success 200 {string}
// @Failure 403 body is empty
// @router /facilitys [get]
func (this *AccessoryController) GetFacilityList() {
	FN := "[GetFacilityList] "
	beego.Warn("[--- API: GetFacilityList ---]")

	var result ResGetFacilities
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

	ft, _ := this.GetInt64("type")

	/*
	 *	Processing
	 */
	err, lst := models.GetFacilityList(uid, ft)
	if nil == err {
		result.Facilities = lst
		result.Total = int64(len(lst))
	}
}

// @Title AddHouseFacilities
// @Description add new house facility
// @Success 200 {string}
// @Failure 403 body is empty
// @router /house/:id/facilities [post]
func (this *AccessoryController) AddHouseFacilities() {
	FN := "[AddHouseFacilities] "
	beego.Warn("[--- API: AddHouseFacilities ---]")

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
	numb, _ := this.GetInt("numb")
	var al []commdef.AddHouseFacility
	for i := 0; i < numb; i++ {
		item := fmt.Sprintf("fid_%d", i)
		fid, _ := this.GetInt64(item)
		item = fmt.Sprintf("fqty_%d", i)
		qty, _ := this.GetInt(item)
		item = fmt.Sprintf("fdesc_%d", i)
		desc := this.GetString(item)
		tmp, _ := base64.URLEncoding.DecodeString(desc)
		desc = string(tmp)

		newItem := commdef.AddHouseFacility{Facility: fid, Qty: qty, Desc: desc}

		al = append(al, newItem)
	}
	beego.Debug(FN, "numb:", numb, ", al:", al)

	/*
	 *	Processing
	 */
	err = models.AddHouseFacilities(uid, hid, al)
	if nil == err {
	}
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
