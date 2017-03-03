package models

import (
	"ApiServer/commdef"
	"fmt"
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/orm"
	// "time"
)

/**
*	add property
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

	p := TblProperty{Name: prop}
	errt := o.Read(&p, "Name")
	if nil == errt { // property already exist
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_DUPLICATE}
		return
	}

	// add into table
	n := TblProperty{Name: prop}
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
*		pid		- property id
*		name	- property name
*		addr	- property address
*		desc	- property additional description
*	Returns
*		err 	- error info
 */
func ModifyProperty(pid int64, name, addr, desc string) (err error) {
	FN := "[ModifyProperty] "
	beego.Trace(FN, "pid:", pid, ", name:", name, ", addr:", addr, ", desc:", desc)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/* Argeuments checking */
	if pid <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("property id:%d", pid)}
		return
	}
	if 0 == len(name) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("property name:%d", pid)}
		return
	}

	o := orm.NewOrm()

	// pid exist?
	p := TblProperty{Id: pid}
	errT := o.Read(&p)
	if nil != errT { // property already exist
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("property id:%d", pid)}
		return
	}

	// new name duplicate?
	t := TblProperty{Name: name}
	errT = o.Read(&t, "Name")
	// beego.Debug(FN, "t.Id:", t.Id)
	if nil == errT && t.Id != pid {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("property id:%d", pid)}
		return
	}

	// Update
	p.Id = pid
	p.Name = name
	p.Address = addr
	p.Desc = desc
	/*numb*/ _, errT = o.Update(&p, "Name", "Address", "Desc")
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
