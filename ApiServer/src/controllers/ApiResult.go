package controllers

import (
	"ApiServer/commdef"
	"ApiServer/models"
	// "fmt"
	"errors"
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

// result of API GetSms{
type ResAdminGetSms struct {
	ResCommon
	SmsCode string
}

// result of API RegCust{
type ResAdminRegCust struct {
	ResCommon
	Uid int64
}

// result of API Logoin
type ResAdminLogin struct {
	ResCommon
	Sid string
}

// result of API get house info
type ResGetHouseInfo struct {
	ResCommon
	HouseInfo commdef.HouseInfo
}

// result of API get house digest info
type ResGetHouseDigest struct {
	ResCommon
	HouseDigest commdef.HouseDigest
}

// result of API get house list
type ResGetHouseList struct {
	ResCommon
	Total int64
	Count int64
	IDs   []int64
}

// result of API add resource
type ResAddResource struct {
	ResCommon
	Id int64
}

// result of API get property list
type ResGetPropertyList struct {
	ResCommon
	Total      int64
	Count      int64
	Properties []commdef.PropInfo
}

// result of API get property info
type ResGetPropInfo struct {
	ResCommon
	PropInfo commdef.PropInfo
}

// result of API get picture url
type ResGetPicUrl struct {
	ResCommon
	Url_s string
	Url_m string
	Url_l string
}

// result of API get new event count
type ResGetNewEventCnt struct {
	ResCommon
	NewEvent int64
}

// result of API get house new event
type ResGetHouseEvents struct {
	ResCommon
	Houses []commdef.HouseEvents
}

// result of API get house event info
type ResGetHouseEventInfo struct {
	ResCommon
	Event commdef.HouseEventInfo
}

// result of API get house event proc list
type ResGetHouseEventProcList struct {
	ResCommon
	Total    int
	Count    int
	ProcList []commdef.HouseEventProc
}

// result of APIs of list, like get deliverable list
type ResGetCommonList struct {
	ResCommon
	Total int64
	List  []commdef.CommonListItem
}

// result of API get house deliverable list
type ResGetHouseDeliverables struct {
	ResCommon
	Total        int64
	Deliverables []commdef.HouseDeliverable
}

// result of API get facility list
type ResGetFacilities struct {
	ResCommon
	Total      int64
	Facilities []commdef.Facility
}

// result of API get house facility list
type ResGetHouseFacilities struct {
	ResCommon
	Total      int64
	Facilities []commdef.HouseFacility
}

// result of API get house picture list
type ResGetHousePicList struct {
	ResCommon
	Total int
	Pics  []commdef.HousePicture
}

/************************************************************************************
*
*		Functions
*
*************************************************************************************/
func api_result(err error, controller beego.Controller, data *ResCommon) (errRet error) {

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
		errRet = errors.New(data.ErrString)
		return
	}

	se.FillError()
	errRet = se

	// http response header
	httpRespCode = int(se.HttpRespcode)
	if commdef.ERR_NONE == se.ErrCode {
		return
	}

	// http response body, common part
	data.ErrCode = se.ErrCode
	data.ErrString = se.GetErrorString() // se.Error() // "" //ErrorCodes[resCode]

	return
}

/**
*	Get loginned user
	Return:
		uid	- logined user id, must > 0 (0 is reserved for system), -1 for error
		err	- error
*/
func getLoginUser(c beego.Controller) (uid int64, err error) {
	FN := "[getLoginUser] "

	uid = -1
	defer func() {
		if nil != err {
			beego.Error(FN, "uid:", uid, ", err:", err)
		}
	}()

	// try to read uid from http request cookie
	// beego.Debug(FN, "c.CruSession:", c.CruSession)
	c.StartSession()
	// beego.Debug(FN, "c.CruSession:", c.CruSession)
	if nil != c.CruSession {
		/*sid :=*/ c.CruSession.SessionID()
		// beego.Debug(FN, "request.Sid:", sid)
		userId := c.GetSession("userid")
		// beego.Debug(FN, "userId:", userId)
		if nil != userId {
			switch userId.(type) {
			case int64:
				uid, _ = userId.(int64)
				if uid > 0 {
					err, _ = models.GetUser(uid)
				} else {
					err = commdef.SwError{ErrCode: commdef.ERR_COMMON_NOT_LOGIN}
				}
			default:
				err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: "Unknow type of userId"}
			}
			return
		}
	}

	// Not get uid from session, try to read it from http request paramers
	sid := c.GetString("sid")
	// beego.Debug(FN, "sid:", sid)
	if 0 == len(sid) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_NOT_LOGIN}
		return
	}

	session, errTmp := beego.GlobalSessions.GetSessionStore(sid)
	// beego.Debug(FN, "session:", session)
	if nil != errTmp {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_NOT_LOGIN, ErrInfo: errTmp.Error()}
		return
	}

	userId := session.Get("userid")
	// beego.Debug(FN, "userId:", userId)
	if nil != userId {
		switch userId.(type) {
		case int64:
			uid, _ = userId.(int64)
			if uid > 0 {
				err, _ = models.GetUser(uid)
			} else {
				err = commdef.SwError{ErrCode: commdef.ERR_COMMON_NOT_LOGIN}
			}
		default:
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: "Unknow type of userId"}
		}
		return
	}

	return
}
