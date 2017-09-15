package models

import (
	"ApiServer/commdef"
	"fmt"
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/orm"
	// "strconv"
	"time"
)

var nilTime time.Time

/**
*	Get new system message list
*	Arguments:
*		uid 	- logined user id
*		bgn 	- fetch begin position, zero-based
*		cnt		- how many recrods want to fetch
*	Returns
*		err 	- error info
*		mst		- system message
**/
func GetNewMsgList(uid, bgn, cnt int64) (err error, total int64, ids []int64) {
	Fn := "[GetNewMsgList] "
	beego.Info(Fn, fmt.Sprintf("login user:%d, fetch(%d - %d)", uid, bgn, cnt))

	defer func() {
		if nil != err {
			beego.Error(Fn, err)
		}
	}()

	/* Argument checking */
	// GetNewMsgCount will check the login user inside
	// if err, _ = GetUser(uid); nil != err {
	// 	return
	// }
	if bgn < 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: "invalid begin position"}
		return
	}
	if cnt < 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: "invalid fetch count"}
		return
	}

	/* Processing */
	// calculate total number
	err, tn := GetNewMsgCount(uid)
	if nil != err {
		return
	}

	total = tn
	if 0 == cnt { // user just want to fetch the total number of new message
		return
	}
	// beego.Debug(Fn, fmt.Sprintf("total %d new messages found", total))

	if 0 == total { // there is no new message for login user
		return
	}

	// fetch message ids
	o := orm.NewOrm()
	qs := o.QueryTable("tbl_message").Filter("Receiver", uid).Filter("ReadTime__isnull", true).OrderBy("-Id")
	qs = qs.Limit(cnt, bgn)

	idlst := []TblMessage{}
	_, errT := qs.All(&idlst)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	for _, v := range idlst {
		ids = append(ids, v.Id)
	}
	return
}

/**
*	Get system message by id
*	Arguments:
*		uid 	- logined user id
*		mid 	- message id
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

	err, m := getSysMsg(mid)
	if nil != err {
		return
	}

	/* Permission Checking*/

	/* Processing */
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
*	set system message as readed
*	Arguments:
*		uid 	- logined user id
*		mid 	- message id
*	Returns
*		err 	- error info
**/
func ReadMsg(uid, mid int64) (err error) {
	Fn := "[ReadMsg] "
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

	err, m := getSysMsg(mid)
	if nil != err {
		return
	}
	if m.ReadTime != nilTime {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: "message already readed"}
		return
	}

	/* Permission Checking*/
	if uid != m.Receiver {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("login user(%d) is not receiver(%d)", uid, m.Receiver)}
		return
	}

	/* Processing */
	o := orm.NewOrm()
	m.ReadTime = time.Now()
	numb, errT := o.Update(&m, "ReadTime")
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("Fail to set message readed, err:%s", errT.Error())}
		return
	}
	beego.Debug(Fn, fmt.Sprintf("%d messages set", numb))

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

func getSysMsg(mid int64) (err error, m TblMessage) {
	Fn := "[getSysMsg] "
	beego.Info(Fn, "message:", mid)

	if mid <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: "invalid message id"}
		return
	}

	o := orm.NewOrm()

	msg := TblMessage{Id: mid}
	if errT := o.Read(&msg); nil != errT {
		if orm.ErrNoRows != errT && orm.ErrMissPK != errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("err:%s", errT.Error())}
		} else {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_RES_NOTFOUND}
		}
		return
	}

	m = msg
	return
}

func unreadMsg(mid int64) (err error) {
	Fn := "[UnreadMsg] "
	beego.Info(Fn, "message:", mid)

	err, m := getSysMsg(mid)
	if nil != err {
		return
	}
	if m.ReadTime == nilTime {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: "message not readed"}
		return
	}

	/* Processing */
	o := orm.NewOrm()
	m.ReadTime = nilTime
	numb, errT := o.Update(&m, "ReadTime")
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("Fail to set message readed, err:%s", errT.Error())}
		return
	}
	beego.Debug(Fn, fmt.Sprintf("%d messages set", numb))

	return
}
