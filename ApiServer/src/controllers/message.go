package controllers

import (
	// "ApiServer/commdef"
	// "encoding/base64"
	// "fmt"
	"github.com/astaxie/beego"
	// "github.com/astaxie/beego/orm"
	"ApiServer/models"
)

type MsgController struct {
	beego.Controller
}

func (m *MsgController) URLMapping() {
	m.Mapping("GetNewMsgCount", m.GetNewMsgCount)
	m.Mapping("GetSysMsg", m.GetSysMsg)
	m.Mapping("ReadMsg", m.ReadMsg)
}

// @Title ReadMsg
// @Description get system message by id
// @Success 200 {string}
// @Failure 403 body is empty
// @router /:id/read [put]
func (this *MsgController) ReadMsg() {
	FN := "[ReadMsg] "
	beego.Warn("[--- API: ReadMsg ---]")

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
	// uid = 10
	beego.Debug(FN, "uid:", uid)

	mid, _ := this.GetInt64(":id")

	/*
	 *	Processing
	 */
	err = models.ReadMsg(uid, mid)
	if nil == err {
	}
}

// @Title GetSysMsg
// @Description get system message by id
// @Success 200 {string}
// @Failure 403 body is empty
// @router /:id [get]
func (this *MsgController) GetSysMsg() {
	FN := "[GetSysMsg] "
	beego.Warn("[--- API: GetSysMsg ---]")

	var result ResGetSysMsg
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
	beego.Debug(FN, "uid:", uid)

	mid, _ := this.GetInt64(":id")

	/*
	 *	Processing
	 */
	err, msg := models.GetSysMsg(uid, mid)
	if nil == err {
		result.Msg = msg
	}
}

// @Title GetNewMsgCount
// @Description get new system message count
// @Success 200 {string}
// @Failure 403 body is empty
// @router /newmsg [get]
func (this *MsgController) GetNewMsgCount() {
	FN := "[GetNewMsgCount] "
	beego.Warn("[--- API: GetNewMsgCount ---]")

	var result ResGetNewMsgCnt
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
	beego.Debug(FN, "uid:", uid)

	/*
	 *	Processing
	 */
	err, nmsg := models.GetNewMsgCount(uid)
	if nil == err {
		result.NewMsgCnt = nmsg
	}
}
