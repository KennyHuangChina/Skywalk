package models

import (
	"ApiServer/commdef"
	"fmt"
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/orm"
	// "time"
)

/**
*	add property. Anyone loginned could add new property
*	Arguments:
*		prop	- property name
*		addr 	- property address
*		desc	- property description
*	Returns
*		err 	- error info
*		id		- new property id
 */
func AddProperty(prop, addr, desc string) (err error, id int64) {
	FN := "[AddProperty] "
	beego.Debug(FN, "prop:", prop, ", addr:", addr, ", desc:", desc)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	if 0 == len(prop) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("property name:%s", prop)}
		return
	}

	o := orm.NewOrm()

	// p := TblProperty{Name: prop}
	// errt := o.Read(&p, "Name")
	// if nil == errt { // property already exist
	// 	err = commdef.SwError{ErrCode: commdef.ERR_COMMON_DUPLICATE}
	// 	return
	// }

	// maybe the property name is partial identical with one or some properties already exist
	qs := o.QueryTable("tbl_property")
	if qs.Filter("Name__contains", prop).Exist() {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_DUPLICATE}
		return
	}

	// add into table
	n := TblProperty{Name: prop, Address: addr, Desc: desc}
	newId, errT := o.Insert(&n)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	beego.Warn(FN, "Notify administrator or agency to complete the new property information")

	id = newId
	return
}

/**
*	Update property info
*	Arguments:
*		uid		- login user
*		pid		- property id
*		name	- property name
*		addr	- property address
*		desc	- property additional description
*	Returns
*		err 	- error info
 */
func ModifyProperty(uid, pid int64, name, addr, desc string) (err error) {
	FN := "[ModifyProperty] "
	beego.Trace(FN, "pid:", pid, ", name:", name, ", addr:", addr, ", desc:", desc)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/* Argeuments checking */
	err, _ = GetPropertyInfo(pid)
	if nil != err {
		return
	}
	if 0 == len(name) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("property name:%d", pid)}
		return
	}
	if 0 == len(addr) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("address:%d", addr)}
		return
	}
	if 0 == len(desc) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("description:%d", desc)}
		return
	}

	/* permission checking */
	// only the agency and administrator could modify property info
	bPermission := false
	if _, bAgency := isAgency(uid); bAgency {
		bPermission = true
	} else if _, bAdmin := isAdministrator(uid); bAdmin {
		bPermission = true
	}
	if !bPermission {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("uid:%d", uid)}
		return
	}

	o := orm.NewOrm()

	// new name duplicate with other property?
	// maybe the property name is partial identical with one or some properties already exist
	qs := o.QueryTable("tbl_property")
	if qs.Filter("Name__contains", name).Exist() {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_DUPLICATE, ErrInfo: fmt.Sprintf("property id:%d", pid)}
		return
	}

	// Update
	/*numb*/ _, errT := o.Update(&TblProperty{Id: pid, Name: name, Address: addr, Desc: desc}, "Name", "Address", "Desc")
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}
	// beego.Debug(FN, "numb:", numb)
	return
}

/**
*	Get property list
*	Arguments:
*		pn 		- property name. whole or partial
*		begin	- from which item to fetch
*		fetch	- how many items to fetch
*	Returns
*		err 	- error info
*		total 	- total number
*		fetched	- fetched quantity
*		ps		- property list
 */
func GetPropertyList(pn string, begin, fetch int64) (err error, total, fetched int64, ps []commdef.PropInfo) {
	FN := "[GetPropertyList] "
	beego.Trace(FN, "pn:", pn, ", begin:", begin, ", fetch:", fetch)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/* Argeuments checking */
	if begin < 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("begin position:%d", begin)}
		return
	}
	if fetch < 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("fetch:%d", fetch)}
		return
	}

	sql_cnt := "SELECT COUNT(*) FROM tbl_property"
	beego.Debug(FN, "pn:", len(pn), "pn=\"\"", pn == "")
	if len(pn) > 0 || pn != "" {
		sql_cnt = fmt.Sprintf("%s WHERE name LIKE '%%%s%%'", sql_cnt, pn)
	}
	beego.Debug(FN, "sql_cnt:", sql_cnt)

	o := orm.NewOrm()

	numb := int64(0)
	errT := o.Raw(sql_cnt).QueryRow(&numb)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}
	total = numb

	if 0 == fetch { // user just want to know the total number
		return
	}

	sql := "SELECT id, name AS prop_name FROM tbl_property"
	if len(pn) > 0 || pn != "" {
		sql = fmt.Sprintf("%s WHERE name LIKE '%%%s%%'", sql, pn)
	}
	sql = sql + fmt.Sprintf(" LIMIT %d, %d", begin, fetch)
	beego.Debug(FN, "sql:", sql)

	var ps_tmp []commdef.PropInfo
	numb, errT = o.Raw(sql).QueryRows(&ps_tmp)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}
	beego.Debug(FN, "ps_tmp:", ps_tmp)

	ps = ps_tmp
	fetched = numb
	beego.Debug(FN, "numb:", numb)
	return
}

/**
*	Get property information by id
*	Arguments:
*		id - property id
*	Returns
*		err - error info
*		hif - property info
 */
func GetPropertyInfo(pid int64) (err error, pif commdef.PropInfo) {
	FN := "[GetPropertyInfo] "
	beego.Trace(FN, "pid:", pid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	if pid <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("property id:%d", pid)}
		return
	}

	o := orm.NewOrm()

	prop := TblProperty{Id: pid}
	if err1 := o.Read(&prop); nil != err1 {
		// beego.Error(FN, err1)
		if orm.ErrNoRows == err1 || orm.ErrMissPK == err1 {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_RES_NOTFOUND, ErrInfo: err1.Error()}
		} else {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: err1.Error()}
		}
		return
	}

	// beego.Debug(FN, "prop:", prop)

	pif.Id = prop.Id
	pif.PropName = prop.Name
	pif.PropAddress = prop.Address
	pif.PropDesc = prop.Desc

	return
}

/*********************************************************************************************************
*
*	Internal Functions
*
**********************************************************************************************************/
/**
*	Get house property name
*	Arguments:
*		hid		- house id
*	Returns
*		err 	- error info
*		prop	- property name
**/
func getHouseProperty(hid int64) (err error, prop string) {
	FN := "[getHouseProperty] "
	beego.Trace(FN, "hid:", hid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	o := orm.NewOrm()

	sql := fmt.Sprintf("SELECT name FROM tbl_house AS house, tbl_property AS prop WHERE house.id=%d AND house.property_id=prop.id", hid)
	Name := ""
	errT := o.Raw(sql).QueryRow(&Name)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	prop = Name
	return
}
