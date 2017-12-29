package controllers

import (
	// "encoding/json"
	// "ApiServer/commdef"
	"ApiServer/models"
	"encoding/base64"
	// "fmt"
	"github.com/astaxie/beego"
)

type PropertyController struct {
	beego.Controller
}

func (p *PropertyController) URLMapping() {
	p.Mapping("GetPropertyInfo", p.GetPropertyInfo)
	p.Mapping("GetPropertyList", p.GetPropertyList)
	p.Mapping("AddProperty", p.AddProperty)
	p.Mapping("UpdateProperty", p.UpdateProperty)
}

// @Title GetPropertyInfo
// @Description get property info by id
// @Success 200 {string}
// @Failure 403 body is empty
// @router /:id [get]
func (this *PropertyController) GetPropertyInfo() {
	FN := "[GetPropertyInfo] "
	beego.Warn("[--- API: GetPropertyInfo ---]")

	var result ResGetPropInfo
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

	/*	Extract agreements */
	pid, _ := this.GetInt64(":id")

	beego.Debug(FN, "pid:", pid)

	/* Processing */
	err, pif := models.GetPropertyInfo(pid)
	if nil == err {
		result.PropInfo = pif
	}
}

// @Title GetPropertyList
// @Description get property list
// @Success 200 {string}
// @Failure 403 body is empty
// @router /list [get]
func (this *PropertyController) GetPropertyList() {
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
	// uid, err := getLoginUser(this.Controller)
	// if nil != err {
	// 	return
	// }

	name := this.GetString("name")
	begin, _ := this.GetInt64("bgn")
	count, _ := this.GetInt64("cnt")

	beego.Debug(FN, "type:", name, ", begin:", begin, ", count:", count) //, ", uid:", uid)

	/*
	 *	Processing
	 */
	err, total, fetched, pl := models.GetPropertyList(name, begin, count)
	if nil == err {
		result.Total = total
		if 0 == count {
			result.Count = -1
		} else {
			result.Count = fetched
		}
		result.Properties = pl
	}
}

// @Title AddProperty
// @Description get property list
// @Success 200 {string}
// @Failure 403 body is empty
// @router /new [post]
func (this *PropertyController) AddProperty() {
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
	addr := this.GetString("addr")
	desc := this.GetString("desc")
	beego.Debug(FN, "prop:", prop, ", addr:", addr, ", desc:", desc)
	// beego.Debug(FN, "prop:", []byte(prop))

	tmp, _ := base64.URLEncoding.DecodeString(prop)
	prop = string(tmp)
	tmp, _ = base64.URLEncoding.DecodeString(addr)
	addr = string(tmp)
	tmp, _ = base64.URLEncoding.DecodeString(desc)
	desc = string(tmp)
	// beego.Debug(FN, "prop:", prop, ", addr:", addr, ", desc:", desc)

	/*
	 *	Processing
	 */
	err, id := models.AddProperty(prop, addr, desc)
	if nil == err {
		result.Id = id
	}
}

// @Title UpdateProperty
// @Description get property list
// @Success 200 {string}
// @Failure 403 body is empty
// @router /:id/info [put]
func (this *PropertyController) UpdateProperty() {
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
	uid, err := getLoginUser(this.Controller)
	if nil != err {
		return
	}

	pid, _ := this.GetInt64(":id")
	prop := this.GetString("prop")
	tmp, _ := base64.URLEncoding.DecodeString(prop)
	prop = string(tmp)
	addr := this.GetString("addr")
	tmp, _ = base64.URLEncoding.DecodeString(addr)
	addr = string(tmp)
	desc := this.GetString("desc")
	tmp, _ = base64.URLEncoding.DecodeString(desc)
	desc = string(tmp)
	// beego.Debug(FN, "pid:", pid)

	/*
	 *	Processing
	 */
	err = models.ModifyProperty(uid, pid, prop, addr, desc)
	if nil == err {
		// result.Id = id
	}
}
