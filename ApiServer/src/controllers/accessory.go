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
	a.Mapping("EditDeliverable", a.EditDeliverable)
	// a.Mapping("DelDeliverable", a.DelDeliverable)

	a.Mapping("NewHouseDeliverable", a.NewHouseDeliverable)
	a.Mapping("GetHouseDeliverableList", a.GetHouseDeliverableList)
	a.Mapping("EditHouseDeliverable", a.EditHouseDeliverable)

	a.Mapping("AddFacilityType", a.AddFacilityType)
	a.Mapping("EditFacilityType", a.EditFacilityType)
	a.Mapping("GetFacilityTypeList", a.GetFacilityTypeList)

	a.Mapping("AddFacility", a.AddFacility)
	a.Mapping("EditFacility", a.EditFacility)
	a.Mapping("GetFacilityList", a.GetFacilityList)

	a.Mapping("AddHouseFacilities", a.AddHouseFacilities)
	a.Mapping("EditHouseFacility", a.EditHouseFacility)
	a.Mapping("GetHouseFacilities", a.GetHouseFacilities)
}

// @Title AddDeliverable
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
		result.List = lst
	}
}

// @Title EditDeliverable
// @Description modify deliverable
// @Success 200 {string}
// @Failure 403 body is empty
// @router /deliverable/:id [put]
func (this *AccessoryController) EditDeliverable() {
	FN := "[EditDeliverable] "
	beego.Warn("[--- API: EditDeliverable ---]")

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

	did, _ := this.GetInt64(":id")
	name := this.GetString("name")
	tmp, _ := base64.URLEncoding.DecodeString(name)
	name = string(tmp)
	// beego.Debug(FN, "name:", name)

	/*
	 *	Processing
	 */
	err = models.EditDeliverable(name, did, uid)
	if nil == err {
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

// @Title EditHouseDeliverable
// @Description modify a house deliverable
// @Success 200 {string}
// @Failure 403 body is empty
// @router /deliverable/:id [put]
func (this *AccessoryController) EditHouseDeliverable() {
	FN := "[EditHouseDeliverable] "
	beego.Warn("[--- API: EditHouseDeliverable ---]")

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

	hdid, _ := this.GetInt64(":id")
	// did, _ := this.GetInt64("did")
	qty, _ := this.GetInt("qty")
	desc := this.GetString("desc")
	tmp, _ := base64.URLEncoding.DecodeString(desc)
	desc = string(tmp)

	/*
	 *	Processing
	 */
	err = models.EditHouseDeliverable(hdid, uid, qty, desc)
	if nil == err {
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

// @Title EditFacilityType
// @Description modify facility type
// @Success 200 {string}
// @Failure 403 body is empty
// @router /facility/type/:id [put]
func (this *AccessoryController) EditFacilityType() {
	FN := "[EditFacilityType] "
	beego.Warn("[--- API: EditFacilityType ---]")

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

	ftid, _ := this.GetInt64(":id")
	name := this.GetString("name")
	// beego.Debug(FN, "name:", name)
	tmp, _ := base64.URLEncoding.DecodeString(name)
	name = string(tmp)
	// beego.Debug(FN, "name:", name)

	/*
	 *	Processing
	 */
	err = models.EditFacilityType(name, ftid, uid)
	if nil == err {
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

	// Icon. ref to AddPic()
	picDir := models.GetPicBaseDir() // beego.AppConfig.String("PicBaseDir") // os.Getwd()
	picFile := ""
	file, fHead, errT := this.GetFile("file") // pic")
	if nil == errT {
		for k, v := range fHead.Header {
			beego.Debug(FN, k, ":", v)
		}
		// file type
		fType := fHead.Header["Content-Type"][0]
		if !checkPicType(fType) {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("invalid Content-Type::%s", fType)}
			return
		}

		// beego.Warn("picDir:", picDir)
		err, picFile = generatePicFileName(picDir, getPicExtName(fType))
		if nil != err {
			return
		}
		if err, _ = savePicture(file, picDir+picFile); nil != err {
			return
		}
	}

	/*
	 *	Processing
	 */
	err, id := models.AddFacility(name, ft, uid, picFile, picDir)
	if nil == err {
		result.Id = id
	} else if len(picFile) > 0 {
		models.DelImageFile(picDir + picFile)
	}
}

// @Title EditFacility
// @Description modify facility
// @Success 200 {string}
// @Failure 403 body is empty
// @router /facility/:id [put]
func (this *AccessoryController) EditFacility() {
	FN := "[EditFacility] "
	beego.Warn("[--- API: EditFacility ---]")

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

	fid, _ := this.GetInt64(":id")
	ft, _ := this.GetInt64("type")
	name := this.GetString("name")
	tmp, _ := base64.URLEncoding.DecodeString(name)
	name = string(tmp)
	// beego.Debug(FN, "name:", name, ", ft:", ft)

	/*
	 *	Processing
	 */
	err = models.EditFacility(fid, ft, uid, name)
	if nil == err {
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
	err, _ = models.AddHouseFacilities(uid, hid, al)
	if nil == err {
	}
}

// @Title EditHouseFacility
// @Description add new house facility
// @Success 200 {string}
// @Failure 403 body is empty
// @router /housefacility/:id [put]
func (this *AccessoryController) EditHouseFacility() {
	FN := "[EditHouseFacility] "
	beego.Warn("[--- API: EditHouseFacility ---]")

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

	id, _ := this.GetInt64(":id")
	fid, _ := this.GetInt64("fid")
	qty, _ := this.GetInt("fqty")
	desc := this.GetString("fdesc")

	/*
	 *	Processing
	 */
	err = models.EditHouseFacility(uid, id, fid, qty, desc)
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
