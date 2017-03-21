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
	p.Mapping("UpdateProperty", p.UpdateProperty)
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
