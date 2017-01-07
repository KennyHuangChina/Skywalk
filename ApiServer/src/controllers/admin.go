package controllers

import (
	// "encoding/json"
	// "fmt"
	"ApiServer/models"
	"github.com/astaxie/beego"
)

type AdminController struct {
	beego.Controller
}

func (a *AdminController) URLMapping() {
	a.Mapping("GetSecurePic", a.GetSecurePic)
}

// @Title GetSecurePic
// @Description generate security picture with GUID
// @Success 200 {string}
// @Failure 403 body is empty
// @router /sec_pic [get]
func (this *AdminController) GetSecurePic() {
	beego.Warn("[--- GetSecurePic ---]")

	this.Data["json"] = "Kenny Test"
	this.ServeJSON()
}

func init() {
	models.TmpUse()
}
