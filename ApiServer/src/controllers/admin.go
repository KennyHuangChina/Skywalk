package controllers

import (
	// "encoding/json"
	"ApiServer/commdef"
	"ApiServer/models"
	"fmt"
	// "crypto/rand"
	// "encoding/hex"
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/cache"
	"github.com/astaxie/beego/utils/captcha"
	// "io"
	// "net/http"
	// "net/url"
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

	a.Mapping("Loginpass", a.Loginpass)
	a.Mapping("FetchSms", a.FetchSms)
	a.Mapping("Loginsms", a.Loginsms)
	a.Mapping("Logout", a.Logout)
	a.Mapping("TestLogin", a.TestLogin)
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
		err = commdef.SwError{ErrCode: commdef.ERR_USERLOGIN_CAPTCHA_SERVER, ErrInfo: err1.Error()}
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

	err, salt, r := models.GetSaltByName(username)
	if nil == err {
		result.Salt = salt
		result.Random = r
	}

	// beego.Debug(FN, "salt:", result.Salt, ", random:", result.Random)
}

// @Title TestLogin
// @Description login by login name & password
// @Success 200 {string}
// @Failure 400 body is empty
// @router /test [post]
func (this *AdminController) TestLogin() {
	FN := "<TestLogin> "
	beego.Warn("[--- API: TestLogin ---]")

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

	uid, err := getLoginUser(this.Controller)
	if nil != err {
		return
	}
	beego.Debug(FN, "uid:", uid)

	return
}

// @Title Loginpass
// @Description login by login name & password
// @Success 200 {string}
// @Failure 400 body is empty
// @router /loginpass [post]
func (this *AdminController) Loginpass() {
	FN := "<Loginpass> "
	beego.Warn("[--- API: Loginpass ---]")

	var result ResAdminLogin
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

	/* Extract agreements */
	ver, _ := this.GetInt("ver")
	loginName := this.GetString("ln")
	password := this.GetString("pw")
	client, _ := this.GetInt("typ") // client type. 0 - web; 1 - APP
	psid := this.GetString("psid")  // secure picture id
	pss := this.GetString("pss")    // secure picture result
	// beego.Debug(FN, "LoginName:", loginName, ", password:", password, ", psid:", psid, ", pss:", pss, ", client:", client)

	if 1 != ver {
		err = commdef.SwError{ErrCode: commdef.ERR_LOWER_CLIENT, ErrInfo: fmt.Sprintf("ver:%d", ver)}
		return
	}
	if 0 == len(loginName) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: "login name could not be empty"}
		return
	}

	/* Processing */
	// 1. check captcha MD5(GUID+Password) with client's secret
	if 0 == client {
		if success := cpt.Verify(psid, pss); !success {
			err = commdef.SwError{ErrCode: commdef.ERR_USERLOGIN_CAPTCHA_FAIL}
			return
		}
	}

	// 2. check login
	err, userid := models.LoginByPass(loginName, password)
	if nil != err {
		return
	}
	// userid := int64(4)
	beego.Info(FN, "login success for user:", userid)

	// 3. return session id
	this.StartSession()
	result.Sid = this.CruSession.SessionID()
	// beego.Debug(FN, "result.Sid:", result.Sid)
	// cookie := &http.Cookie{
	// 	Name:     beego.BConfig.WebConfig.Session.SessionName, // beego.SessionName,
	// 	Value:    url.QueryEscape(result.Sid),
	// 	Path:     "/",
	// 	HttpOnly: true,
	// 	Secure:   beego.EnableHttpTLS,
	// 	Domain: beego.BConfig.WebConfig.Session.SessionDomain,
	// }
	// if beego.SessionCookieLifeTime > 0 {
	// 	cookie.MaxAge = beego.SessionCookieLifeTime
	// 	cookie.Expires = time.Now().Add(time.Duration(beego.SessionCookieLifeTime) * time.Second)
	// }
	// // NOTE by Gavin:SetCookie must be called before ResponseWriter.WriteHeader
	// // because ResponseWriter.Header will never accept header modification after Wri
	// http.SetCookie(this.Ctx.ResponseWriter, cookie)
	this.SetSession("userid", userid)
	// commdefin.ApiResult(commdefin.ERR_NONE, this.Controller, &result.ResCommon)
}

// @Title Logout
// @Description login by login name & password
// @Success 200 {string}
// @Failure 400 body is empty
// @router /logout [post]
func (this *AdminController) Logout() {
	FN := "<Logout> "
	beego.Warn("[--- API: Logout ---]")

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

	uid, err := getLoginUser(this.Controller)
	if nil != err {
		return
	}
	beego.Debug(FN, "uid:", uid)

	this.StartSession()
	Sid := this.CruSession.SessionID()
	beego.Debug(FN, "Sid:", Sid)
	this.SetSession("userid", int64(-1)) // remove the user id for this session, GC will remove it automatically
}

// @Title FetchSms
// @Description get sms code for user(phone number)
// @Success 200 {string}
// @Failure 400 body is empty
// @router /fetchsms [get]
func (this *AdminController) FetchSms() {
	FN := "<FetchSms> "
	beego.Warn("[API --- FetchSms ---]")

	var result ResAdminGetSms
	var err error

	defer func() {
		err = api_result(err, this.Controller, &result.ResCommon)
		if nil != err {
			beego.Error(FN, err.Error())
		}

		// beego.Debug(FN, "result:", result)
		// export result
		this.Data["json"] = result
		this.ServeJSON()
	}()

	/* Extract agreements */
	login_name := this.GetString("ln")
	beego.Debug(FN, "login_name:", login_name)

	/* Process */
	err, sms := models.FetchSms(login_name)
	if nil == err {
		result.SmsCode = sms
	}
}

// @Title Loginsms
// @Description login by phone + sms code
// @Success 200 {string}
// @Failure 400 body is empty
// @router /loginsms [post]
func (this *AdminController) Loginsms() {
	FN := "<Loginsms> "
	beego.Warn("[API --- Loginsms ---]")

	var result ResAdminRegCust
	var err error

	defer func() {
		err = api_result(err, this.Controller, &result.ResCommon)
		if nil != err {
			beego.Error(FN, err.Error())
		}

		// beego.Debug(FN, "result:", result)
		// export result
		this.Data["json"] = result
		this.ServeJSON()
	}()

	/* Extract agreements */
	login_name := this.GetString("ln")
	sms_code := this.GetString("sms")
	beego.Debug(FN, "login_name:", login_name, ", sms code:", sms_code)

	/* Process */
	err, uid := models.LoginSms(login_name, sms_code)
	if nil == err {
		result.Uid = uid
	}
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//
func init() {
	store := cache.NewMemoryCache()
	cpt = captcha.NewWithFilter("/captcha/", store)
}
