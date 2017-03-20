package models

import (
	"ApiServer/commdef"
	"fmt"
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/orm"
	// "time"
)

/**
*	Add New House Deliverable
*	Arguments:
*		uid 	- login user id
*		hid		- house id
*	Returns
*		err 	- error info
*		lst 	- new house deliverable id
**/
func GetHouseDeliverableList(uid, hid int64) (err error, lst []commdef.HouseDeliverable) {
	FN := "[GetHouseDeliverableList] "
	beego.Trace(FN, "uid:", uid, ", hid", hid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/* argument checking */
	if err, _ = checkHouse(hid); nil != err {
		return
	}

	o := orm.NewOrm()
	sql := fmt.Sprintf(`SELECT h.deliverable AS id, d.name, h.qty, h.desc
							FROM tbl_house_deliverable AS h, tbl_deliverables AS d 
							WHERE h.house=%d AND h.deliverable = d.Id`, hid)

	var l []commdef.HouseDeliverable
	/*numb*/ _, errT := o.Raw(sql).QueryRows(&l)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	lst = l
	return
}

/**
*	Add New Deliverable
*	Arguments:
*		uid 	- login user id
*	Returns
*		err 	- error info
*		lst 	- deliverable list
**/
func GetDeliverables(uid int64) (err error, lst []commdef.CommonListItem) {
	FN := "[GetDeliverables] "
	beego.Trace(FN, "uid:", uid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	// permission checking, all system user could fetch
	o := orm.NewOrm()

	var dl []TblDeliverables
	/*numb*/ _, errT := o.QueryTable("tbl_deliverables").All(&dl)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	for _, v := range dl {
		newItem := commdef.CommonListItem{Id: v.Id, Name: v.Name}
		lst = append(lst, newItem)
	}
	// lst = dl
	return
}
