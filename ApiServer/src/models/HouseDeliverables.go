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
*		did		- deliverable id
*		qty		- deliverable quantity. 0 means delete this house deliverable
*		desc	- deliverable description
*	Returns
*		err 	- error info
*		id 	- new house deliverable id
**/
func AddHouseDeliverable(uid, hid, did int64, qty int, desc string) (err error, id int64) {
	FN := "[AddHouseDeliverable] "
	beego.Trace(FN, "uid:", uid, ", hid", hid, ", did:", did, ", qty:", qty, ", desc:", desc)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/* argument checking */
	err, _ = checkHouse(hid)
	if nil != err {
		return
	}
	if did <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("did:%d", did)}
		return
	}
	// check if the deliverable is real
	o := orm.NewOrm()
	bExist := o.QueryTable("tbl_deliverables").Filter("Id", did).Exist()
	if !bExist {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_RES_NOTFOUND, ErrInfo: fmt.Sprintf("did:%d", did)}
		return
	}

	if qty < 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("qty:%d", qty)}
		return
	} else {
		// check if this house deliverable exist
		bExist = o.QueryTable("tbl_house_deliverable").Filter("House", hid).Filter("Deliverable", did).Exist()
		beego.Debug(FN, "bExist:", bExist)
		if 0 == qty && !bExist {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("house:%d, deliverable:%d", hid, did)}
			return
		} else if qty > 0 && bExist { //
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_DUPLICATE, ErrInfo: fmt.Sprintf("house:%d, deliverable:%d", hid, did)}
			return
		}
	}

	// processing
	if qty > 0 {
		n := TblHouseDeliverable{House: hid, Deliverable: did, Qty: qty, Desc: desc}
		nid, errT := o.Insert(&n)
		if nil != errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
			return
		}
		id = nid
	} else {
		numb, errT := o.QueryTable("tbl_house_deliverable").Filter("House", hid).Filter("Deliverable", did).Delete()
		if nil != errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
			return
		}
		beego.Debug(FN, "numb:", numb)
	}

	return
}

/**
*	Add New Deliverable
*	Arguments:
*		uid 	- login user id
*		name	- deliverable name
*	Returns
*		err - error info
*		id 	- new deliverable id
**/
func AddDeliverable(name string, uid int64) (err error, id int64) {
	FN := "[AddDeliverable] "
	beego.Trace(FN, "name:", name, ", uid:", uid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/*	argument checking */
	if 0 == len(name) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("name:%s", name)}
		return
	}

	// permission checking
	if _, bAdmin := isAdministrator(uid); bAdmin {
	} else {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("uid:%d", uid)}
		return
	}

	// check if the deliver name already exist
	o := orm.NewOrm()
	bExist := o.QueryTable("tbl_deliverables").Filter("Name__contains", name).Exist()
	if bExist {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_DUPLICATE, ErrInfo: fmt.Sprintf("name:%s", name)}
		return
	}

	// Add
	n := TblDeliverables{Name: name}
	nid, errT := o.Insert(&n)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	id = nid
	return
}

/*********************************************************************************************************
*
*	Internal Functions
*
**********************************************************************************************************/
