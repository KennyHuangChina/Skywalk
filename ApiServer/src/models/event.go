package models

import (
	"ApiServer/commdef"
	"fmt"
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/orm"
)

/**
*	Get new event count
*	Arguments:
*		uid 		- user id
*	Returns
*		err 		- error info
*		new_event 	- new event count
**/
func GetNewEventCount(uid int64) (err error, new_event int64) {
	FN := "[GetNewEventCount] "
	beego.Trace(FN, "uid:", uid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	if uid <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("user id:%d", uid)}
		return
	}

	sql := fmt.Sprintf(`SELECT proc.event_id 
							FROM tbl_house_event AS event LEFT JOIN tbl_house_event_process AS proc 
								ON event.id=proc.event_id 
							WHERE (sender=%d OR receiver=%d) AND event_id IS NULL`, uid, uid)
	sql = fmt.Sprintf("SELECT COUNT(*) AS count FROM (%s) AS tmp GROUP BY event_id", sql)
	o := orm.NewOrm()

	var count []int64
	numb, errT := o.Raw(sql).QueryRows(&count)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}
	beego.Debug(FN, "numb:", numb, ", count:", count)

	if numb > 0 {
		new_event = count[0]
	}

	return
}
