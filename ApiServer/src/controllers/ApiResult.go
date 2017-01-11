package controllers

import (
	"ApiServer/commdef"
	"ApiServer/models"
	// "errors"
	"github.com/astaxie/beego"
)

type ResCommon struct {
	ErrCode   int64
	ErrString string
}

/************************************************************************************
*
*		Api result structures
*
*************************************************************************************/
// result of API get security picture
type ResAdminGetSecurePic struct {
	ResCommon
	Capt models.Captcha
}

// result of API get user info
type ResAdminGetUserInfo struct {
	ResCommon
	commdef.UserInfo
}

// result of API GetSaltForUser{
type ResAdminGetUserSalt struct {
	ResCommon
	Salt   string
	Random string
}

// result of API get house info
type ResGetUserInfo struct {
	ResCommon
	commdef.HouseInfo
}

// result of API get property info
type ResGetPropInfo struct {
	ResCommon
	commdef.PropInfo
}

/************************************************************************************
*
*		Functions
*
*************************************************************************************/
func api_result(err error, controller beego.Controller, data *ResCommon) {

	data.ErrCode = commdef.ERR_NONE
	data.ErrString = ""

	httpRespCode := int(0)

	defer func() {
		// beego.Debug("[api_result] ", "httpRespCode:", httpRespCode)
		controller.Ctx.ResponseWriter.WriteHeader(httpRespCode)
	}()

	if nil == err {
		httpRespCode = 200
		return
	}

	se, ok := err.(commdef.SwError)
	if !ok {
		data.ErrCode = commdef.ERR_COMMON_UNEXPECTED
		data.ErrString = "Unexpected result"
		return
	}

	se.FillError()

	// http response header
	httpRespCode = int(se.HttpRespcode)
	if commdef.ERR_NONE == se.ErrCode {
		return
	}

	// http response body, common part
	data.ErrCode = se.ErrCode
	data.ErrString = err.Error() // "" //ErrorCodes[resCode]
}
