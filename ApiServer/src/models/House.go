package models

import (
	"ApiServer/commdef"
	"fmt"
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/orm"
)

/**
*	Get house list by id
*	Arguments:
*		ht 		- list type. ref to commdef.HOUSE_LIST_xxx
*		begin	- from which item to fetch
*		count	- how many items to fetch
*	Returns
*		err 	- error info
*		total 	- total number
*		fetched	- fetched quantity
*		ids		- house id list
 */
func GetHouseListByType(ht int, begin, count int64) (err error, total, fetched int64, ids []int64) {
	FN := "[GetHouseListByType] "
	beego.Trace(FN, "type:", ht, ", begin:", begin, ", count:", count)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/* Argeuments checking */
	if ht < commdef.HOUSE_LIST_Unknown || ht > commdef.HOUSE_LIST_Max {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("Invalid list type:%d", ht)}
		return
	}
	if begin < 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("Invalid begin position:%d", begin)}
		return
	}
	if count < 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("Invalid count:%d", count)}
		return
	}

	switch ht {
	case commdef.HOUSE_LIST_Recommend:
		return getRecommendHouseList(begin, count)
	case commdef.HOUSE_LIST_Deducted:
	case commdef.HOUSE_LIST_New:
	case commdef.HOUSE_LIST_All:
	}

	return
}

/**
*	Get house information by id
*	Arguments:
*		id - house id
*	Returns
*		err - error info
*		hd 	- house digest info
 */
func GetHouseDigestInfo(hid, uid int64) (err error, hd commdef.HouseDigest) {
	FN := "[GetUserInfo] "
	beego.Trace(FN, "hid:", hid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	o := orm.NewOrm()

	// House info
	var dig commdef.HouseDigest
	sql := fmt.Sprintf(`SELECT house.id AS id, prop.name AS property, address AS property_addr, bedrooms, livingrooms, bathrooms, acreage, cover_img 
							FROM tbl_house AS house, tbl_property AS prop 
							WHERE house.property_id=prop.id AND house.id=%d`, hid)
	errTmp := o.Raw(sql).QueryRow(&dig)
	if nil != errTmp {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errTmp.Error()}
		return
	}

	// rental info
	err, rs := getHouseRental(hid)
	if nil != err {
		return
	}

	if len(rs) > 0 {
		beego.Warn(FN, "TODO: bug here")
		p0 := rs[0].RentalBid // first bid price
		p1 := p0              // last bid price
		if len(rs) > 1 {
			p1 = rs[len(rs)-1].RentalBid
		}
		dig.Rental = p0
		dig.Pricing = p1 - p0
	}

	// house tags
	err, tgs := getHouseTags(hid)
	if nil != err {
		return
	}
	hd = dig
	hd.Tags = tgs

	return
}

/**
*	Get house information by id
*	Arguments:
*		id - house id
*	Returns
*		err - error info
*		hif - house info
**/
func GetHouseInfo(hid int64) (err error, hif commdef.HouseInfo) {
	FN := "[GetUserInfo] "
	beego.Trace(FN, "hid:", hid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	o := orm.NewOrm()

	house := TblHouse{Id: hid}
	if err1 := o.Read(&house); nil != err1 {
		// beego.Error(FN, err1)
		if orm.ErrNoRows == err1 || orm.ErrMissPK == err1 {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_RES_NOTFOUND, ErrInfo: err1.Error()}
		} else {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: err1.Error()}
		}
		return
	}

	// beego.Debug(FN, "house:", house)
	// beego.Debug(FN, "property:", house.Property)

	hif.Id = house.Id
	hif.Property = house.Property.Id
	hif.BuddingNo = house.BuildingNo
	hif.FloorTotal = house.FloorTotal
	hif.FloorThis = house.FloorThis
	hif.HouseNo = house.HouseNo
	hif.Bedrooms = house.Bedrooms
	hif.Livingrooms = house.Livingrooms
	hif.Bathrooms = house.Bathrooms
	hif.Acreage = house.Acreage

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
	FN := "[GetUserInfo] "
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

/*********************************************************************************************************
*
*	Internal Functions
*
**********************************************************************************************************/
/**
*	Get house active rental by house id
*	Arguments:
*		hid - house id
*	Returns
*		err - error info
*		rs 	- house rental list
 */
func getHouseRental(hid int64) (err error, rs []TblRental) {
	FN := "[getHouseRental] "
	beego.Trace(FN, "hid:", hid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	o := orm.NewOrm()
	var rlst []TblRental

	qs := o.QueryTable("tbl_rental")
	num, errTmp := qs.Filter("HouseId", hid).Filter("Active", true).OrderBy("Id").All(&rlst)
	if nil != errTmp {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errTmp.Error()}
		return
	}

	if 0 == num {
		// err = commdef.SwError{ErrCode: commdef.ERR_COMMON_RES_NOTFOUND}
		// return
		beego.Debug(FN, "No active rental found")
	}

	rs = rlst
	beego.Debug(FN, "rs:", rs)
	return
}

/**
*	Get house tags by house id
*	Arguments:
*		hid - house id
*	Returns
*		err - error info
*		ts 	- house tag list
 */
func getHouseTags(hid int64) (err error, ts []commdef.HouseTags) {
	FN := "[getHouseTags] "
	beego.Trace(FN, "hid:", hid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	o := orm.NewOrm()
	sql := fmt.Sprintf(`SELECT htags.tag AS tag_id, tags.tag AS tag_desc
							FROM tbl_house_tag AS htags, tbl_tag AS tags 
							WHERE htags.tag=tags.id AND house=%d`, hid)

	var tgs []commdef.HouseTags
	numb, errTmp := o.Raw(sql).QueryRows(&tgs)
	if nil != errTmp {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errTmp.Error()}
		return
	}

	if 0 == numb /*len(tgs)*/ {
		beego.Debug(FN, "No tags found")
	}

	ts = tgs
	beego.Debug(FN, "tags:", ts)
	return
}

/**
*	Get recommend house list
*	Arguments:
*		begin	- from which item to fetch
*		count	- how many items to fetch
*	Returns
*		err 	- error info
*		total 	- total number
*		fetched	- fetched quantity
*		ids		- house id list
 */
func getRecommendHouseList(begin, count int64) (err error, total, fetched int64, ids []int64) {
	FN := "[getRecommendHouseList] "
	beego.Trace(FN, "begin:", begin, ", count:", count)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	o := orm.NewOrm()
	qs := o.QueryTable("tbl_house_recommend")

	// calculate the total house number
	cnt, errT := qs.Count()
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}
	total = cnt

	if 0 == count { // user just want to detect how many houses
		return
	}

	// fetch exact house list
	if begin > total {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: "begin position out of range"}
		return
	}

	// fetching
	beego.Warn(FN, "TODO: all() will be restricted by limit, and return max 1000 records")
	var hids []TblHouseRecommend
	numb, errT := qs.OrderBy("When").All(&hids)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}
	// beego.Debug(FN, "hids:", hids)

	fetched = numb
	for _, v := range hids {
		ids = append(ids, v.House)
	}
	// ids = hids

	return
}
