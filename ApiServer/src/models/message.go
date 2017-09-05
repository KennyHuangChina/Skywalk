package models

import (
	"ApiServer/commdef"
	"fmt"
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/orm"
	// "strconv"
	// "time"
)

/**
*	Get new system message count
*	Arguments:
*		uid 	- logined user id
*	Returns
*		err 	- error info
*		nmc 	- new message count
**/
func GetNewMsgCount(uid int64) (err error, nmc int64) {
	Fn := "[GetNewMsgCount] "
	beego.Info(Fn, "login user:", uid)

	defer func() {
		if nil != err {
			beego.Error(Fn, err)
		}
	}()

	/* Argument checking */
	err, _ = GetUser(uid)
	if nil != err {
		return
	}

	/* Processing */
	o := orm.NewOrm()
	qs := o.QueryTable("tbl_message").Filter("Receiver", uid).Filter("ReadTime_isnull")
	cnt, errT := qs.Count()
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}
	beego.Debug(Fn, fmt.Sprintf("%d new messages found", cnt))

	nmc = cnt
	return
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//		-- Internal Functions --
//
func addMessage(mt, pri int, refId, receiver int64, msg string) (err error, msg_id int64) {
	Fn := "[addMessage] "
	beego.Info(Fn, fmt.Sprintf("type:%d, priority:%d, refId:%d, receiver:%d, msg:%s", mt, pri, refId, receiver, msg))

	/* argument checking */
	if mt < commdef.MSG_Begin || mt > commdef.MSG_End {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: "invalid message type"}
		return
	}
	if pri < commdef.MSG_PRIORITY_Begin || pri > commdef.MSG_PRIORITY_End {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: "invalid message priority"}
		return
	}
	if refId <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: "invalid reference id"}
		return
	}
	if 0 == len(msg) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: "message not set"}
		return
	}
	if err, _ = GetUser(receiver); nil != err {
		return
	}

	/* processing */
	o := orm.NewOrm()

	m := TblMessage{Type: mt, RefId: refId, Receiver: receiver, Msg: msg}
	nId, errT := o.Insert(&m)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	msg_id = nId
	return
}
