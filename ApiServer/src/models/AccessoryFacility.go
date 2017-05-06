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
*		ft		- facility type
*	Returns
*		err - error info
*		lst - facility list
**/
func GetFacilityList(uid, ft int64) (err error, lst []commdef.Facility) {
	FN := "[GetFacilityTypeList] "
	beego.Trace(FN, "login user:", uid, ", facility type:", ft)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/*	argument checking */
	// facility type = 0 means get all facilities
	if 0 != ft {
		if err, _ = getFacilityType(ft); nil != err {
			return
		}
	}

	/* Permission Checking */
	// All logined user could get facility list
	if err, _ = GetUser(uid); nil != err {
		return
	}

	/* Fetching */
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

	/* permission */
	if err, _ = GetUser(uid); nil != err {
		return
	}

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
*	Add New house facilities
*	Arguments:
*		hid	- house id
*	Returns
*		err - error info
*		lst	- house facility list
**/
func GetHouseFacilities(hid int64) (err error, lst []commdef.HouseFacility) {
	FN := "[AddHouseFacilities] "
	beego.Trace(FN, "hid:", hid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/*	argument checking */
	if err, _ = getHouse(hid); nil != err {
		return
	}

	/* query */
	sql := fmt.Sprintf(`SELECT hf.id, f.name, t.name AS type, hf.qty, hf.desc 
							FROM tbl_house_facility AS hf, tbl_facilitys AS f, tbl_facility_type AS t 
							WHERE hf.house=%d AND hf.facility=f.id AND f.type=t.id`, hid)
	o := orm.NewOrm()

	var l []commdef.HouseFacility
	numb, errT := o.Raw(sql).QueryRows(&l)
	// beego.Debug(FN, "numb:", numb, ", err:", errT)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}
	if 0 == numb {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_RES_NOTFOUND, ErrInfo: fmt.Sprintf("house:%d", hid)}
		return
	}

	lst = l
	return
}

/**
*	Modify house facility info
*	Arguments:
*		uid - login user id
*		id	- house facility id
*		fid	- facility id
*		qty	- quantity
*		desc- description
*	Returns
*		err - error info
**/
func EditHouseFacility(uid, id, fid int64, qty int, desc string) (err error) {
	FN := "[EditHouseFacility] "
	beego.Trace(FN, "login user:", uid, ", house facility:", id, ", facility:", fid, ", qty:", qty, ", desc:", desc)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/* argument checking */
	if err, _ = getFacility(fid); nil != err {
		return
	}

	err, hf := getHouseFacility(id)
	if nil != err {
		return
	}

	beego.Debug(FN, "house:", hf.House)
	err, h := getHouse(hf.House)
	if nil != err {
		return
	}
	beego.Debug(FN, "landlord:", h.Owner.Id, ", agency:", h.Agency.Id)

	if qty < 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("qty:%d", qty)}
		return
	}

	o := orm.NewOrm()

	// check if the house + facility already exist
	bExist := o.QueryTable("tbl_house_facility").Filter("House", hf.House).Filter("Facility", fid).Exclude("Id", id).Exist()
	if bExist {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_DUPLICATE, ErrInfo: fmt.Sprintf("house:%d, facility:%d", hf.House, fid)}
		return
	}

	/* Permission checking */
	if err, _ = GetUser(uid); nil != err {
		return
	}
	// only the landlord, house agency and administrator could modify the house facility
	if isHouseOwner(h, uid) || isHouseAgency(h, uid) {
	} else if _, bAdmin := isAdministrator(uid); bAdmin {
	} else {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("uid:%d", uid)}
		return
	}

	/* Processing */
	if 0 == qty { // delete this house facility
		numb, errT := o.Delete(&TblHouseFacility{Id: id})
		if nil != errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
			return
		}
		beego.Debug(FN, fmt.Sprintf("Delete %d records", numb))
	} else { // Update the house facility
		_, errT := o.Update(&TblHouseFacility{Id: id, Facility: fid, Qty: qty, Desc: desc}, "Facility", "Qty", "Desc")
		if nil != errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
			return
		}
	}

	return
}

/**
*	Delete a house facility
*	Arguments:
*		uid - login user id
*		id	- house facility id
*	Returns
*		err - error info
**/
func DeleteHouseFacility(uid, id int64) (err error) {
	FN := "[EditHouseFacility] "
	beego.Trace(FN, "login user:", uid, ", house facility:", id)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/* argument checking */
	err, hf := getHouseFacility(id)
	if nil != err {
		return
	}

	beego.Debug(FN, "house:", hf.House)
	err, h := getHouse(hf.House)
	if nil != err {
		return
	}
	beego.Debug(FN, "landlord:", h.Owner.Id, ", agency:", h.Agency.Id)

	/* Permission checking */
	if err, _ = GetUser(uid); nil != err {
		return
	}
	// only the landlord, house agency and administrator could modify the house facility
	if isHouseOwner(h, uid) || isHouseAgency(h, uid) {
	} else if _, bAdmin := isAdministrator(uid); bAdmin {
	} else {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("uid:%d", uid)}
		return
	}

	/* Processing */
	o := orm.NewOrm()
	// delete this house facility
	numb, errT := o.Delete(&TblHouseFacility{Id: id})
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}
	beego.Debug(FN, fmt.Sprintf("Delete %d records", numb))

	return
}

/**
*	Add New house facilities
*	Arguments:
*		uid - login user id
*		hid	- house id
*		fl	- facility list
*	Returns
*		err - error info
*		ids - new house facility id list
**/
func AddHouseFacilities(uid, hid int64, fl []commdef.AddHouseFacility) (err error, ids []int64) {
	FN := "[AddHouseFacilities] "
	beego.Trace(FN, "user:", uid, ", house:", hid, ", fl:", fmt.Sprintf("%+v", fl))

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/*	argument checking */
	// house
	err, h := getHouse(hid)
	if nil != err {
		return
	}

	if err, _ = GetUser(uid); nil != err {
		return
	}

	/* Permission Checking */
	// Only the house owner, its agency and administrator could add facility
	// who could do this operation
	if isHouseOwner(h, uid) || isHouseAgency(h, uid) {
	} else if _, bAdmin := isAdministrator(uid); bAdmin {
	} else {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("user:%d", uid)}
		return
	}

	// facility id
	nLen := 0
	if nil != fl {
		nLen = len(fl)
	}
	if 0 == nLen {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: "no facility found"}
		return
	}

	for _, v := range fl {
		if err, _ = getFacility(v.Facility); nil != err {
			return
		}
	}

	o := orm.NewOrm()

	/* Processing */
	var al []commdef.AddHouseFacility // list for adding
	var ul []commdef.AddHouseFacility // list for updating
	for _, v := range fl {
		// check if the house facility already exist
		hf := TblHouseFacility{}
		errT := o.QueryTable("tbl_house_facility").Filter("House", hid).Filter("Facility", v.Facility).One(&hf)
		beego.Debug(FN, "errT:", errT)
		if nil == errT {
			// facility already exist, update information
			v.Id = hf.Id
			ul = append(ul, v)
		} else if orm.ErrNoRows == errT || orm.ErrMissPK == errT {
			// new facility, add
			al = append(al, v)
		} else {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
			return
		}
	}

	o.Begin()
	defer func() {
		if nil != err {
			beego.Error(FN, "Rollback")
			o.Rollback()
		} else {
			beego.Debug(FN, "Commit")
			o.Commit()
		}
	}()

	// add new house facilities
	// Kenny: change to use prepareInset, instead of InsertMulti, in order to get the new record ids
	if len(al) > 0 {
		qs := o.QueryTable("tbl_house_facility")
		i, _ := qs.PrepareInsert()
		defer i.Close()
		for k, v := range al {
			beego.Debug(FN, k, ":", v)
			nid, e := i.Insert(&TblHouseFacility{House: hid, Facility: v.Facility, Qty: v.Qty, Desc: v.Desc})
			if nil != e {
				err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("add facility:", v.Facility)}
				return
			}
			ids = append(ids, nid)
		}
	}
	// var la []TblHouseFacility
	// for _, v := range al {
	// 	newItem := TblHouseFacility{House: hid, Facility: v.Facility, Qty: v.Qty, Desc: v.Desc}
	// 	la = append(la, newItem)
	// }
	// if len(la) > 0 {
	// 	numb _, errT := o.InsertMulti(len(la), la)
	// 	if nil != errT {
	// 		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED} // , ErrInfo: fmt.Sprintf("add facility:", v.Facility)}
	// 		return
	// 	}
	// }

	// update facilities already exist
	for _, v := range ul {
		u := TblHouseFacility{Id: v.Id, Qty: v.Qty, Desc: v.Desc}
		/*numb*/ _, errT := o.Update(&u, "Qty", "Desc")
		if nil != errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("update facility:", v.Facility)}
			return
		}
	}

	return
}

/**
*	Add New facility
*	Arguments:
*		uid 	- login user id
*		name	- facility name
*		ft		- facility type
*	Returns
*		err 	- error info
*		id 		- new facility id
**/
func AddFacility(name string, ft, uid int64) (err error, id int64) {
	FN := "[AddFacility] "
	beego.Trace(FN, "name:", name, ", facility type:", ft, ", login user:", uid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	// permission checking: only the administrator could add facility
	if _, bAdmin := isAdministrator(uid); bAdmin {
	} else {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("uid:%d", uid)}
		return
	}

	/*	argument checking */
	if 0 == len(name) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("name:%s", name)}
		return
	}
	if err, _ = getFacilityType(ft); nil != err {
		return
	}

	o := orm.NewOrm()

	// check if type + facility name already exist
	bExist := o.QueryTable("tbl_facilitys").Filter("Type", ft).Filter("Name__contains", name).Exist()
	if bExist {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_DUPLICATE, ErrInfo: fmt.Sprintf("facility type:%d, name:%s", ft, name)}
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
*	Modify facility
*	Arguments:
*		uid 	- login user id
*		fid 	- facility id
*		name	- facility name
*		ft		- facility type
*	Returns
*		err 	- error info
**/
func EditFacility(fid, ft, uid int64, name string) (err error) {
	FN := "[AddFacility] "
	beego.Trace(FN, "Facility:", fid, ", type:", ft, ", name:", name, ", login user:", uid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	// permission checking: only the administrator could modify facility
	if _, bAdmin := isAdministrator(uid); bAdmin {
	} else {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("uid:%d", uid)}
		return
	}

	/*	argument checking */
	if err, _ = getFacility(fid); nil != err {
		return
	}
	if 0 == len(name) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("name:%s", name)}
		return
	}
	if err, _ = getFacilityType(ft); nil != err {
		return
	}

	o := orm.NewOrm()

	// check if type + facility name already exist
	bExist := o.QueryTable("tbl_facilitys").Filter("Type", ft).Filter("Name__contains", name).Exclude("Id", fid).Exist()
	if bExist {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_DUPLICATE, ErrInfo: fmt.Sprintf("facility type:%d, name:%s", ft, name)}
		return
	}

	/* Processing: Update */
	_, errT := o.Update(&TblFacilitys{Id: fid, Type: ft, Name: name})
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	return
}

/**
*	Delete facility
*	Arguments:
*		uid 	- login user id
*		fid 	- facility id
*	Returns
*		err 	- error info
**/
func DelFacility(fid, uid int64) (err error) {
	FN := "[DelFacility] "
	beego.Trace(FN, "Facility:", fid, ", login user:", uid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	// permission checking: only the administrator could delete facility
	if _, bAdmin := isAdministrator(uid); bAdmin {
	} else {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("uid:%d", uid)}
		return
	}

	/*	argument checking */
	if err, _ = getFacility(fid); nil != err {
		return
	}

	o := orm.NewOrm()

	/* Processing: Delete */
	beego.Warn(FN, "Prevent deleting if this facility has been used or delete cascaded")
	_, errT := o.Delete(&TblFacilitys{Id: fid})
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	return
}

/**
*	Add New facility type
*	Arguments:
*		uid 	- login user id
*		name	- facility type name
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

	// permission checking. Only the administrator could add new facility type
	if _, bAdmin := isAdministrator(uid); bAdmin {
	} else {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("uid:%d", uid)}
		return
	}

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

/**
*	Modify New facility type
*	Arguments:
*		ftid	- facility type id
*		uid 	- login user id
*		name	- facility type name
*	Returns
*		err 	- error info
**/
func EditFacilityType(name string, ftid, uid int64) (err error) {
	FN := "[EditFacilityType] "
	beego.Trace(FN, "name:", name, ", facility type:", ftid, ", login user:", uid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	// permission checking
	if _, bAdmin := isAdministrator(uid); bAdmin {
	} else {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("uid:%d", uid)}
		return
	}

	/*	argument checking */
	if 0 == len(name) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("name:%s", name)}
		return
	}
	if err, _ = getFacilityType(ftid); nil != err {
		return
	}

	// check if the target facility type name already exist
	o := orm.NewOrm()
	bExist := o.QueryTable("tbl_facility_type").Filter("Name__contains", name).Exclude("Id", ftid).Exist()
	if bExist {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_DUPLICATE, ErrInfo: fmt.Sprintf("name:%s", name)}
		return
	}

	// Update
	_, errT := o.Update(&TblFacilityType{Id: ftid, Name: name})
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	return
}

/**
*	Delete facility type
*	Arguments:
*		ftid	- facility type id
*		uid 	- login user id
*	Returns
*		err 	- error info
**/
func DeleFacilityType(ftid, uid int64) (err error) {
	FN := "[DeleFacilityType] "
	beego.Trace(FN, "facility type:", ftid, ", login user:", uid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	// permission checking. Only administrator could delete the facility type
	if _, bAdmin := isAdministrator(uid); bAdmin {
	} else {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("uid:%d", uid)}
		return
	}

	/*	argument checking */
	if err, _ = getFacilityType(ftid); nil != err {
		return
	}

	beego.Warn(FN, "Prevent deleting if this type has been used or delete cascaded")

	// Delete
	o := orm.NewOrm()
	_, errT := o.Delete(&TblFacilityType{Id: ftid})
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	return
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//		Internal Functions
//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
func getFacilityType(ftid int64) (err error, ft TblFacilityType) {
	if ftid <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("facility type:%d", ftid)}
		return
	}

	o := orm.NewOrm()

	t := TblFacilityType{Id: ftid}
	errT := o.Read(&t)
	if nil != errT {
		if orm.ErrNoRows == errT || orm.ErrMissPK == errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_RES_NOTFOUND, ErrInfo: fmt.Sprintf("facility type:%d", ftid)}
		} else {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		}
		return
	}

	ft = t
	return
}

func getFacility(fid int64) (err error, f TblFacilitys) {
	if fid <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("facility:%d", fid)}
		return
	}

	o := orm.NewOrm()

	t := TblFacilitys{Id: fid}
	errT := o.Read(&t)
	if nil != errT {
		if orm.ErrNoRows == errT || orm.ErrMissPK == errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_RES_NOTFOUND, ErrInfo: fmt.Sprintf("facility:%d", fid)}
		} else {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		}
		return
	}

	f = t
	return
}

func getHouseFacility(hfid int64) (err error, hf TblHouseFacility) {
	if hfid <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("house facility:%d", hfid)}
		return
	}

	o := orm.NewOrm()

	t := TblHouseFacility{Id: hfid}
	errT := o.Read(&t)
	if nil != errT {
		if orm.ErrNoRows == errT || orm.ErrMissPK == errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_RES_NOTFOUND, ErrInfo: fmt.Sprintf("facility:%d", hfid)}
		} else {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		}
		return
	}

	hf = t
	return
}
