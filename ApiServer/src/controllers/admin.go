package controllers

import (
	// "encoding/json"
	// "fmt"
	"ApiServer/models"
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/cache"
	"github.com/astaxie/beego/utils/captcha"
)

var cpt *captcha.Captcha

// var store *cache.MemoryCache

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
	FN := "[GetSecurePic] "
	beego.Warn("[--- API: GetSecurePic ---]")

	var result ResAdminGetSecurePic

	imageID, err := cpt.CreateCaptcha()
	if err != nil {
		beego.Error(FN, err)
		// commdefin.ApiResult(commdefin.ERR_COMMON_CAPTCHA_SERVER, a.Controller, &result.ResCommon)
		return
	}

	url := cpt.URLPrefix + imageID + ".png"
	beego.Trace(FN, "url:", url)
	result.Capt.PicDataBase64 = url
	result.Capt.GUID = imageID
	this.Ctx.Output.Header("Access-Control-Allow-Origin", "*")
	// commdefin.ApiResult(commdefin.ERR_NONE, a.Controller, &result.ResCommon)

	this.Data["json"] = result
	this.ServeJSON()
}

func init() {
	models.TmpUse()

	store := cache.NewMemoryCache()
	cpt = captcha.NewWithFilter("/captcha/", store)
}
