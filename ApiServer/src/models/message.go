package models

import (
	"ApiServer/commdef"
	"fmt"
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/orm"
	// "strconv"
	"time"
)

/**
*	Get system message by id
*	Arguments:
*		uid 	- logined user id
*	Returns
*		err 	- error info
*		mst		- system message
**/
func GetSysMsg(uid, mid int64) (err error, msg commdef.SysMessage) {
	Fn := "[GetSysMsg] "
	beego.Info(Fn, "login user:", uid, ", message:", mid)

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

	if mid <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: "invalid message id"}
		return
	}

	/* Permission Checking*/

	/* Processing */
	o := orm.NewOrm()

	m := TblMessage{Id: mid}
	if errT := o.Read(&m); nil != errT {
		if orm.ErrNoRows != errT && orm.ErrMissPK != errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_RES_NOTFOUND, ErrInfo: fmt.Sprintf("err:%s", errT.Error())}
			return
		}
	}
	// permission checking, only message receiver himself and administrator could access this message
	if m.Receiver != uid {
		if _, bAdmin := isAdministrator(uid); !bAdmin {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("login user(%d) != receiver(%d), and is not an administrator", uid, m.Receiver)}
			return
		}
	}

	// message info
	msg.Id = m.Id
	msg.Type = m.Type
	msg.Receiver = m.Receiver
	msg.Priority = m.Priority
	msg.Msg = m.Msg
	msg.CreateTime = m.CreateTime.Local().String()[:19]

	nilTime := time.Time{}
	if nilTime != m.ReadTime {
		msg.ReadTime = m.ReadTime.Local().String()[:19]
	}

	hid := int64(0)
	switch m.Type {
	case commdef.MSG_HouseCertification:
		{
			hc := TblHouseCert{}
			if err, hc = getMsgReference_HouseCert(m.RefId); nil != err {
				return
			}
			beego.Debug(Fn, fmt.Sprintf("hc:%+v", hc))
			hid = hc.House
		}
	case commdef.MSG_ScheduleHouseWatch:
		beego.Warn(Fn, "TODO: MSG_ScheduleHouseWatch")
		err = commdef.SwError{ErrCode: commdef.ERR_NOT_IMPLEMENT, ErrInfo: "MSG_ScheduleHouseWatch"}
		return
	default:
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("message type:%d", m.Type)}
		return
	}

	// house info
	err, h := getHouse(hid)
	if nil != err { // && commdef.ERR_COMMON_PERMISSION != err {
		return
	}
	err, pif := GetPropertyInfo(h.Property.Id)
	if nil != err {
		return
	}
	msg.House.Property = pif.PropName
	if errT := canAccessHouse(uid, hid); nil == errT {
		msg.House.BuildingNo = h.BuildingNo
		msg.House.HouseNo = h.HouseNo
	}
	msg.House.Bedrooms = h.Bedrooms
	msg.House.Livingrooms = h.Livingrooms
	msg.House.Bathrooms = h.Bathrooms

	beego.Debug(Fn, fmt.Sprintf("msg:%+v", msg))
	return
}

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
	qs := o.QueryTable("tbl_message").Filter("Receiver", uid).Filter("ReadTime__isnull", true)
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
func getMsgReference_HouseCert(hcid int64) (err error, hc TblHouseCert) {
	Fn := "[getMsgReference_HouseCert] "
	beego.Info(Fn, "house cert record:", hcid)

	o := orm.NewOrm()
	hc1 := TblHouseCert{Id: hcid}
	errT := o.Read(&hc1)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	hc = hc1
	return
}

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
