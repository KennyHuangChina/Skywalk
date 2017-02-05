package models

import (
	"ApiServer/commdef"
	"fmt"
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/orm"
	// "time"
)

/**
*	Get facility list
*	Arguments:
*		uid 	- login user id
*		ft		- facility list
*	Returns
*		err - error info
*		lst - facility list
**/
func GetFacilityList(uid, ft int64) (err error, lst []commdef.Facility) {
	FN := "[GetFacilityTypeList] "
	beego.Trace(FN, "uid:", uid, ", ft:", ft)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/*	argument checking */
	if 0 != ft {
		if err = checkFacilityType(ft); nil != err {
			return
		}
	}

	sql := "SELECT f.id, f.name, t.name AS type FROM tbl_facilitys AS f, tbl_facility_type AS t WHERE f.type=t.id"
	if ft > 0 {
		sql = sql + fmt.Sprintf(" AND type=%d", ft)
	}
	sql = sql + " ORDER BY t.name, f.name"

	o := orm.NewOrm()

	var l []commdef.Facility
	/*numb*/ _, errT := o.Raw(sql).QueryRows(&l)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	lst = l
	return
}

/**
*	Get facility type list
*	Arguments:
*		uid 	- login user id
*	Returns
*		err - error info
*		lst - facility type list
**/
func GetFacilityTypeList(uid int64) (err error, lst []commdef.CommonListItem) {
	FN := "[GetFacilityTypeList] "
	beego.Trace(FN, "uid:", uid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/*	argument checking */

	// get facility type list
	o := orm.NewOrm()

	var l []TblFacilityType
	/*numb*/ _, errT := o.QueryTable("tbl_facility_type").All(&l)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	for _, v := range l {
		lst = append(lst, commdef.CommonListItem{Id: v.Id, Name: v.Name})
	}

	return
}

/**
*	Add New facility type
*	Arguments:
*		uid 	- login user id
*		name	- deliverable name
*		ft		- facility type
*	Returns
*		err - error info
*		id 	- new facility type id
**/
func AddFacility(name string, ft int, uid int64) (err error, id int64) {
	FN := "[AddFacility] "
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
	if ft <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("ft:%d", ft)}
		return
	}

	// check if the facility type actual exist
	o := orm.NewOrm()

	f := TblFacilityType{Id: int64(ft)}
	errT := o.Read(&f)
	if nil != errT {
		if orm.ErrNoRows == errT || orm.ErrMissPK == errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("ft:%d", ft)}
		} else {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		}
		return
	}

	// check if type + facility name already exist
	bExist := o.QueryTable("tbl_facilitys").Filter("Type", ft).Filter("Name__contains", name).Exist()
	if bExist {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_DUPLICATE, ErrInfo: fmt.Sprintf("facility type:%d, name:%s", ft, name)}
		return
	}

	err, bAdmin := isAdministrator(uid)
	if nil != err {
		return
	}
	if !bAdmin {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("uid:%d", uid)}
		return
	}

	/* Add to table */
	nf := TblFacilitys{Type: int64(ft), Name: name}
	nid, errT := o.Insert(&nf)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	id = nid
	return
}

/**
*	Add New facility type
*	Arguments:
*		uid 	- login user id
*		name	- deliverable name
*	Returns
*		err - error info
*		id 	- new facility type id
**/
func AddFacilityType(name string, uid int64) (err error, id int64) {
	FN := "[AddFacilityType] "
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

	// check if the deliver name already exist
	o := orm.NewOrm()
	bExist := o.QueryTable("tbl_facility_type").Filter("Name__contains", name).Exist()
	if bExist {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_DUPLICATE, ErrInfo: fmt.Sprintf("name:%s", name)}
		return
	}

	err, bAdmin := isAdministrator(uid)
	if nil != err {
		return
	}
	if !bAdmin {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("uid:%d", uid)}
		return
	}

	// Add
	n := TblFacilityType{Name: name}
	nid, errT := o.Insert(&n)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	id = nid
	return
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//		Internal Functions
//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
func checkFacilityType(ft int64) (err error) {
	if ft <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("facility type:%d", ft)}
		return
	}

	o := orm.NewOrm()

	t := TblFacilityType{Id: ft}
	errT := o.Read(&t)
	if nil != errT {
		if orm.ErrNoRows == errT || orm.ErrMissPK == errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_RES_NOTFOUND, ErrInfo: fmt.Sprintf("facility type:%d", ft)}
		} else {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		}
		return
	}

	return
}
