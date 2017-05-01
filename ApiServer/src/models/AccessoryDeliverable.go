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
	if err, _ = getHouse(hid); nil != err {
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
	beego.Trace(FN, "name:", name, ", login user:", uid)

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

/**
*	Modify Deliverable
*	Arguments:
*		uid 	- login user id
*		did		- deliverable id
*		name	- deliverable name
*	Returns
*		err - error info
*		id 	- new deliverable id
**/
func EditDeliverable(name string, did, uid int64) (err error) {
	FN := "[EditDeliverable] "
	beego.Trace(FN, "deliverable:", did, ", name:", name, ", login user:", uid)

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
	if err, _ := getDeliverable(did); nil != err {
		return err
	}

	// permission checking
	if _, bAdmin := isAdministrator(uid); bAdmin {
	} else {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("uid:%d", uid)}
		return
	}

	o := orm.NewOrm()
	// check if the deliver name already exist
	bExist := o.QueryTable("tbl_deliverables").Filter("Name__contains", name).Exclude("Id", did).Exist()
	if bExist {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_DUPLICATE, ErrInfo: fmt.Sprintf("name:%s", name)}
		return
	}

	// Update
	/*numb*/ _, errT := o.Update(&TblDeliverables{Id: did, Name: name}, "Name")
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	return
}

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
	beego.Trace(FN, "login user:", uid, ", house", hid, ", deliverable:", did, ", qty:", qty, ", desc:", desc)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/* Argument Checking */
	err, h := getHouse(hid)
	if nil != err {
		return
	}
	if err, _ = getDeliverable(did); nil != err {
		return
	}
	if qty < 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("qty:%d", qty)}
		return
	}

	/* Permission Checking */
	// only the landlorad, house agency, and administrator are capable to add deliverable for house
	if isHouseOwner(h, uid) || isHouseAgency(h, uid) {
	} else if _, bAdmin := isAdministrator(uid); bAdmin {
	} else {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("login user:%d", uid)}
		return
	}

	o := orm.NewOrm()

	// check if this house deliverable exist
	bExist := o.QueryTable("tbl_house_deliverable").Filter("House", hid).Filter("Deliverable", did).Exist()
	beego.Debug(FN, "bExist:", bExist)
	if 0 == qty && !bExist { // nothing to delete
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("house:%d, deliverable:%d", hid, did)}
		return
	} else if qty > 0 && bExist { // already exist
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_DUPLICATE, ErrInfo: fmt.Sprintf("house:%d, deliverable:%d", hid, did)}
		return
	}

	// processing
	if qty > 0 { // add
		n := TblHouseDeliverable{House: hid, Deliverable: did, Qty: qty, Desc: desc}
		nid, errT := o.Insert(&n)
		if nil != errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
			return
		}
		id = nid
	} else { // delete
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
*	Modify House Deliverable
*	Arguments:
*		uid 	- login user id
*		hdid	- house deliverable id
*		qty		- deliverable quantity. 0 means delete this house deliverable
*		desc	- deliverable description
*	Returns
*		err 	- error info
**/
func EditHouseDeliverable(hdid, uid int64, qty int, desc string) (err error) {
	FN := "[AddHouseDeliverable] "
	beego.Trace(FN, "login user:", uid, ", house deliverable:", hdid, ", qty:", qty, ", desc:", desc)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/* Argument Checking */
	err, hd := getHouseDeliverable(hdid)
	if nil != err {
		return
	}
	beego.Debug(FN, fmt.Sprintf("%+v", hd))
	if qty < 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("qty:%d", qty)}
		return
	}

	err, h := getHouse(hd.House)
	if nil != err {
		return
	}

	/* Permission Checking */
	// only the landlorad, house agency, and administrator are capable to add deliverable for house
	if isHouseOwner(h, uid) || isHouseAgency(h, uid) {
		beego.Debug(FN, "1")
	} else if _, bAdmin := isAdministrator(uid); bAdmin {
		beego.Debug(FN, "2")
	} else {
		beego.Debug(FN, "3")
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("login user:%d", uid)}
		return
	}

	o := orm.NewOrm()

	// processing
	if qty > 0 { // Update
		hd.Qty = qty
		hd.Desc = desc
		/*numb*/ _, errT := o.Update(&hd)
		if nil != errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
			return
		}
	} else { // delete
		numb, errT := o.QueryTable("tbl_house_deliverable").Filter("Id", hdid).Delete()
		if nil != errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
			return
		}
		beego.Debug(FN, "numb:", numb)
	}

	return
}

/*********************************************************************************************************
*
*	Internal Functions
*
**********************************************************************************************************/
func getDeliverable(did int64) (err error, d TblDeliverables) {
	FN := "[getDeliverable] "
	beego.Trace(FN, "did:", did)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	if did <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("deliverable:%d", did)}
		return
	}

	o := orm.NewOrm()

	dT := TblDeliverables{Id: did}
	errT := o.Read(&dT)
	if nil != errT {
		if orm.ErrNoRows == errT || orm.ErrMissPK == errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_RES_NOTFOUND, ErrInfo: fmt.Sprintf("did:%d", did)}
		} else {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		}
		return
	}

	d = dT
	return
}

func delDeliverable(did, lu int64) (err error) {
	FN := "[delDeliverable] "
	beego.Trace(FN, "deliverable:", did, ", login user:", lu)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	if err, _ = getDeliverable(did); nil != err {
		return
	}

	/* Permission Checking */
	// Only the administrator could delete the deliverable
	err, bAdmin := isAdministrator(lu)
	if nil != err {
		return
	}
	if !bAdmin {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("lu:%d", lu)}
		return
	}

	/* Delete deliverable specified */
	beego.Warn(FN, "checking deliverable relation or just delete all of them along with deliverable")

	o := orm.NewOrm()
	if /*num*/ _, err1 := o.Delete(&TblDeliverables{Id: did}); err1 != nil {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: err1.Error()}
		return
	}

	return
}

func getHouseDeliverable(hdid int64) (err error, d TblHouseDeliverable) {
	FN := "[getHouseDeliverable] "
	beego.Trace(FN, "hdid:", hdid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	if hdid <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("house deliverable:%d", hdid)}
		return
	}

	o := orm.NewOrm()

	dT := TblHouseDeliverable{Id: hdid}
	errT := o.Read(&dT)
	if nil != errT {
		if orm.ErrNoRows == errT || orm.ErrMissPK == errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_RES_NOTFOUND, ErrInfo: fmt.Sprintf("house deliverable:%d", hdid)}
		} else {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		}
		return
	}

	d = dT
	return
}