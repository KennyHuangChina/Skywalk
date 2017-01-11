package controllers

import (
	// "encoding/json"
	// "fmt"
	"ApiServer/commdef"
	"ApiServer/models"
	"crypto/rand"
	"encoding/hex"
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/cache"
	"github.com/astaxie/beego/utils/captcha"
	"io"
)

var cpt *captcha.Captcha

// var store *cache.MemoryCache

type AdminController struct {
	beego.Controller
}

func (a *AdminController) URLMapping() {
	a.Mapping("GetSecurePic", a.GetSecurePic)
	a.Mapping("GetUserInfo", a.GetUserInfo)
	a.Mapping("GetSaltForUser", a.GetSaltForUser)
}

// @Title GetUserInfo
// @Description get user info by id
// @Success 200 {string}
// @Failure 403 body is empty
// @router /user/:id [get]
func (this *AdminController) GetUserInfo() {
	FN := "[GetUserInfo] "
	beego.Warn("[--- API: GetUserInfo ---]")

	var result ResAdminGetUserInfo
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
	uid, _ := this.GetInt64(":id")
	sid := this.GetString("sid")

	beego.Debug(FN, "ver:", version, ", uid:", uid, ", sid:", sid)

	/*
	 *	Processing
	 */
	err, uif := models.GetUserInfo(uid)
	if nil == err {
		result.UserInfo = uif
	}
}

// @Title GetSecurePic
// @Description generate security picture with GUID
// @Success 200 {string}
// @Failure 403 body is empty
// @router /sec_pic [get]
func (this *AdminController) GetSecurePic() {
	FN := "[GetSecurePic] "
	beego.Warn("[API --- GetSecurePic ---]")

	var result ResAdminGetSecurePic
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

	imageID, err1 := cpt.CreateCaptcha()
	if err1 != nil {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_CAPTCHA_SERVER, ErrInfo: err1.Error()}
		return
	}

	url := cpt.URLPrefix + imageID + ".png"
	beego.Trace(FN, "url:", url)
	result.Capt.PicDataBase64 = url
	result.Capt.GUID = imageID
	beego.Warn(FN, "TODO: cross-site access setting is too dangeour, need to set correct")
	this.Ctx.Output.Header("Access-Control-Allow-Origin", "*")
}

// @Title GetSaltForUser
// @Description get user's salt and random info
// @Success 200 {string}
// @Failure 400 body is empty
// @router /salt [get]
func (this *AdminController) GetSaltForUser() {
	FN := "<GetSaltForUser> "
	beego.Warn("[API --- GetSaltForUser ---]")

	var result ResAdminGetUserSalt
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

	/* Extract agreements */
	username := this.GetString("un")
	beego.Debug(FN, "username:", username)

	if 0 == len(username) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: "user name could not be empty"}
		return
	}

	err, salt := models.GetSaltByName(username)
	//get salt
	if nil == err {
		result.Salt = salt
		random := make([]byte, 16)
		_, ioerr := io.ReadFull(rand.Reader, random)
		if ioerr != nil {
			beego.Error(FN, ioerr)
		}
		result.Random = hex.EncodeToString(random)
	}

	beego.Debug(FN, "salt:", result.Salt, ", random:", result.Random)
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
func init() {
	store := cache.NewMemoryCache()
	cpt = captcha.NewWithFilter("/captcha/", store)
}
