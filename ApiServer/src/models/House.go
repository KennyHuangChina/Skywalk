package models

import (
	"ApiServer/commdef"
	"fmt"
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/orm"
	"time"
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
		return getDeductedHouseList(begin, count)
	case commdef.HOUSE_LIST_New:
		return getNewHouseList(begin, count)
	case commdef.HOUSE_LIST_All:
		fallthrough
	default:
		err = commdef.SwError{ErrCode: commdef.ERR_NOT_IMPLEMENT}
	}

	return
}

/**
*	Get house list by id
*	Arguments:
*		typ		- list type. ref to commdef.BEHALF_TYPE_XXXX
*		begin	- from which item to fetch
*		tofetch	- how many items to fetch
*		uid		- login user
*	Returns
*		err 	- error info
*		total 	- total number
*		fetched	- fetched quantity
*		hids	- house id list
**/
func GetBehalfList(typ int, begin, tofetch, uid int64) (err error, total, fetched int64, hids []int64) {
	FN := "[GetBehalfList] "
	beego.Trace(FN, "typ:", typ, ", begin:", begin, ", tofetch:", tofetch, ", uid:", uid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/* Argeuments checking */
	if begin < 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("begin:%d", begin)}
		return
	}
	if tofetch < 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("tofetch:%d", tofetch)}
		return
	}

	/* Permission checking
	1) Agency could just see houses are behalfed by him
	2) Administrator could see all houses
	*/
	_, bAgency := isAgency(uid)
	_, bAdmin := isAdministrator(uid)
	beego.Debug(FN, "bAgency:", bAgency, ", bAdmin:", bAdmin)
	if !bAdmin && !bAgency {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("uid:%d", uid)}
		return
	}

	sql_cnt := "SELECT COUNT(*) AS count FROM tbl_house WHERE"
	if bAgency && !bAdmin {
		sql_cnt = sql_cnt + fmt.Sprintf(" agency_id='%d' ", uid)
	} else {
		sql_cnt = sql_cnt + " 1"
	}
	switch typ {
	case commdef.BEHALF_TYPE_ALL:
		sql_cnt = sql_cnt + " AND for_rent=1"
	case commdef.BEHALF_TYPE_TO_RENT:
		sql_cnt = sql_cnt + fmt.Sprintf(" AND for_rent=1 AND rent_stat=%d", commdef.HOUSE_RENT_WAIT)
	case commdef.BEHALF_TYPE_RENTED:
		sql_cnt = sql_cnt + fmt.Sprintf(" AND for_rent=1 AND rent_stat=%d", commdef.HOUSE_RENT_RENTED)
	case commdef.BEHALF_TYPE_TO_SALE:
		sql_cnt = sql_cnt + " AND for_sale=1"
	default:
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("list type:%d", typ)}
		return
	}
	// beego.Debug(FN, "sql_cnt:", sql_cnt)

	o := orm.NewOrm()

	cnt := int64(0)
	errT := o.Raw(sql_cnt).QueryRow(&cnt)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}
	total = cnt
	beego.Debug(FN, "total:", total)

	if 0 == tofetch { // user just want to know the total number
		return
	}

	if begin >= total {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("begin:%d", begin)}
		return
	}

	// fetch real houses
	sql := "SELECT id FROM tbl_house WHERE "
	if bAgency && !bAdmin {
		sql = sql + fmt.Sprintf(" agency_id='%d' ", uid)
	} else {
		sql = sql + " 1"
	}
	switch typ {
	case commdef.BEHALF_TYPE_ALL:
		sql = sql + " AND for_rent=1"
	case commdef.BEHALF_TYPE_TO_RENT:
		sql = sql + fmt.Sprintf(" AND for_rent=1 AND rent_stat=%d", commdef.HOUSE_RENT_WAIT)
	case commdef.BEHALF_TYPE_RENTED:
		sql = sql + fmt.Sprintf(" AND for_rent=1 AND rent_stat=%d", commdef.HOUSE_RENT_RENTED)
	case commdef.BEHALF_TYPE_TO_SALE:
		sql = sql + " AND for_sale=1"
	}
	sql = sql + fmt.Sprintf(" LIMIT %d, %d", begin, tofetch)
	// beego.Debug(FN, "sql:", sql)

	var ids []int64
	numb, errT := o.Raw(sql).QueryRows(&ids)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}
	fetched = numb
	hids = ids
	// beego.Debug(FN, "fetched:", fetched)
	// beego.Debug(FN, "hids:", hids)

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
func GetHouseDigestInfo(hid int64) (err error, hd commdef.HouseDigest) {
	FN := "[GetHouseDigestInfo] "
	beego.Trace(FN, "hid:", hid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	o := orm.NewOrm()

	// House info
	var dig commdef.HouseDigest
	sql := fmt.Sprintf(`SELECT house.id AS id, prop.name AS property, address AS property_addr, bedrooms, 
								livingrooms, bathrooms, acreage, cover_img 
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
		p0 := rs[0].RentalBid // first bid price
		p1 := p0              // last bid price
		if len(rs) > 1 {
			p1 = rs[len(rs)-1].RentalBid
		}
		dig.Rental = p1
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
*		hid - house id
*		uid	- login user id
*	Returns
*		err - error info
*		hif - house info
**/
func GetHouseInfo(hid, uid int64) (err error, hif commdef.HouseInfo) {
	FN := "[GetHouseInfo] "
	beego.Trace(FN, "house:", hid, ", login user:", uid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	err, house := getHouse(hid)
	if nil != err {
		return
	}

	// permission checking. Only house owner, agency and administrator could see the private info
	bPriv := false
	if uid > 0 {
		if isHouseAgency(house, uid) || isHouseOwner(house, uid) {
			bPriv = true
		} else if _, bAdmin := isAdministrator(uid); bAdmin {
			bPriv = true
		}
	}
	beego.Debug(FN, "bPriv:", bPriv)

	hif.Id = house.Id
	hif.Property = house.Property.Id
	hif.FloorTotal = house.FloorTotal
	hif.Bedrooms = house.Bedrooms
	hif.Livingrooms = house.Livingrooms
	hif.Bathrooms = house.Bathrooms
	hif.Acreage = house.Acreage

	if bPriv { // get privite info
		hif.BuildingNo = house.BuildingNo
		hif.FloorThis = house.FloorThis
		hif.HouseNo = house.HouseNo
	} else { // hide the privite info
		hif.BuildingNo = 0
		hif.HouseNo = ""
		if house.FloorThis*3 < hif.FloorTotal {
			hif.FloorThis = hif.FloorTotal + 1 // low storied
		} else if house.FloorThis*3 >= hif.FloorTotal && house.FloorThis*3 < hif.FloorTotal*2 {
			hif.FloorThis = hif.FloorTotal + 2 // middle storied
		} else {
			hif.FloorThis = hif.FloorTotal + 3 // high storied
		}
	}

	return
}

/**
*	Add New House
*	Arguments:
*		hif - house info
*		uid	- login user id
*	Returns
*		err - error info
*		id 	- new house info
 */
func ModifyHouse(hif *commdef.HouseInfo, uid int64) (err error) {
	FN := "[ModifyHouse] "
	beego.Trace(FN, "input house info:", fmt.Sprintf("%+v", hif), ", login user:", uid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/*	argument checking */
	if err = checkHouseInfo(hif, false); nil != err {
		return
	}

	_, house := getHouse(hif.Id)

	// Persission checking, only landlord, it's agency and administrator coudl modify this house
	if isHouseOwner(house, uid) || isHouseAgency(house, uid) {
	} else if _, bAdmin := isAdministrator(uid); bAdmin {
	} else {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("uid:%d", uid)}
		return
	}

	// if the house id(property + building + house_no) is conflict
	o := orm.NewOrm()
	qs := o.QueryTable("tbl_house").Filter("Property__Id", hif.Property).Filter("BuildingNo", hif.BuildingNo).Filter("HouseNo", hif.HouseNo)
	h := TblHouse{}
	errT := qs.One(&h)
	// beego.Debug(FN, fmt.Sprintf("%+v", h))
	if nil == errT {
		beego.Debug(FN, "h.Id:", h.Id, ", hif.Id:", hif.Id)
		if h.Id != hif.Id {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_DUPLICATE, ErrInfo: fmt.Sprintf("property:%d, building:%d, house:%s", hif.Property, hif.BuildingNo, hif.HouseNo)}
			return
		}
	}

	// Update
	tModi := time.Now() //.UTC()
	modifyTime := fmt.Sprintf("%d-%d-%d %d:%d:%d", tModi.Year(), tModi.Month(), tModi.Day(), tModi.Hour(), tModi.Minute(), tModi.Second())
	sql := fmt.Sprintf(`UPDATE tbl_house SET property_id=%d, building_no=%d, floor_total=%d, floor_this=%d,
								house_no='%s', bedrooms=%d, livingrooms=%d, bathrooms=%d, acreage=%d, 
								modify_time='%s', for_sale=%t, for_rent=%t WHERE id=%d`,
		hif.Property, hif.BuildingNo, hif.FloorTotal, hif.FloorThis, hif.HouseNo, hif.Bedrooms, hif.Livingrooms, hif.Bathrooms,
		hif.Acreage, modifyTime, hif.ForSale, hif.ForRent, hif.Id)
	res, errT := o.Raw(sql).Exec()
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}
	numb, _ := res.RowsAffected()
	beego.Debug(FN, "numb:", numb)

	// t := TblHouse{Id: hif.Id}
	// o.Read(&t)
	// beego.Debug(FN, "t:", t)

	// sql = "SELECT modify_time FROM tbl_house WHERE id=2"
	// var m time.Time
	// o.Raw(sql).QueryRow(&m)
	// beego.Debug(FN, "m:", m)

	return
}

/**
*	Set house agency
*	Arguments:
*		hid 	- house id
*		aid		- agency id
*		luid	- login user id
*	Returns
*		err 	- error info
 */
func SetHouseAgency(hid, aid, luid int64) (err error) {
	FN := "[SetHouseAgency] "
	beego.Trace(FN, "house:", hid, ", agency:", aid, ", login user:", luid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/*	argument checking */
	err, h := getHouse(hid)
	if nil != err {
		return
	}

	if err, _ = GetUser(aid); nil != err {
		return
	}

	// Permission checking. Only the landload and administrator could assign agency for house
	if isHouseOwner(h, luid) {
	} else if _, bAdmin := isAdministrator(luid); bAdmin {
	} else {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("luid:%d", luid)}
		return
	}

	// Update
	o := orm.NewOrm()

	numb, errT := o.QueryTable("tbl_house").Filter("Id", hid).Update(orm.Params{"Agency": aid})
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}
	beego.Debug(FN, "numb:", numb)

	return
}

/**
*	Set house cover image id
*	Arguments:
*		hid - house id
*		cid	- cover image id
*		uid	- login user
*	Returns
*		err - error info
 */
func SetHouseCoverImage(hid, cid, uid int64) (err error) {
	FN := "[SetHouseCoverImage] "
	beego.Trace(FN, "hid:", hid, ", cid:", cid, ", uid:", uid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/*	argument checking */
	err, h := getHouse(hid)
	if nil != err {
		return
	}

	if err = checkImage(cid); nil != err {
		return
	}

	/* Permission checking */
	// Only the house owner, it's agency and administrator could set the cover image
	bPermission := false
	if isHouseOwner(h, uid) || isHouseAgency(h, uid) {
		bPermission = true
	} else {
		if _, bAdmin := isAdministrator(uid); bAdmin {
			bPermission = true
		}
	}
	if !bPermission {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("uid:%d", uid)}
		return
	}

	// Update
	o := orm.NewOrm()

	u := TblHouse{Id: hid, CoverImg: cid}
	numb, errT := o.Update(&u, "CoverImg")
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}
	beego.Debug(FN, "numb:", numb)

	return
}

/**
*	Certify House
*	Arguments:
*		hid 	- house id
*		uid		- who made the certification
*		pass	- certificate result, pass or not
*		comment	- certificate comment
*	Returns
*		err - error info
 */
func CertHouse(hid, uid int64, pass bool, comment string) (err error) {
	FN := "[CertHouse] "
	beego.Trace(FN, "hid:", hid, ", uid:", uid, ", pass:", pass, ", comment:", comment)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/*	argument checking */
	if 0 == len(comment) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: "No comments"}
		return
	}

	err, h := getHouse(hid)
	if nil != err {
		return
	}

	/* Permission checking */
	// Only the house agency and administrator could certificate house
	bPermission := false
	if isHouseAgency(h, uid) {
		bPermission = true
	} else {
		bAdmin := false
		if _, bAdmin = isAdministrator(uid); bAdmin {
			bPermission = true
		}
	}
	if !bPermission {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("uid:%d", uid)}
		return
	}

	// Processing
	o := orm.NewOrm()

	// add new record in TblHouseCert
	hc := TblHouseCert{House: hid, Who: uid, Comment: comment, Pass: pass}
	/*nId*/ _, errT := o.Insert(&hc)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	sql := ""
	if pass {
		// Update & publish
		tPublish := time.Now() //.UTC()
		publishTime := fmt.Sprintf("%d-%d-%d %d:%d:%d", tPublish.Year(), tPublish.Month(), tPublish.Day(), tPublish.Hour(), tPublish.Minute(), tPublish.Second())
		sql = fmt.Sprintf(`UPDATE tbl_house SET publish_time='%s' WHERE id=%d`, publishTime, hid)
	} else {
		// revoke the publish
		sql = fmt.Sprintf(`UPDATE tbl_house SET publish_time=NULL WHERE id=%d`, hid)
	}
	res, errT := o.Raw(sql).Exec()
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}
	numb, _ := res.RowsAffected()
	beego.Debug(FN, "affect", numb, "records")

	return
}

/**
*	Commit house by owner
*	Arguments:
*		hif	- house info input
*		oid - house owner id
*		pid	- property id
*		aid	- agency id
*	Returns
*		err - error info
*		id 	- new house info
 */
func CommitHouseByOwner(hif *commdef.HouseInfo, oid, aid int64) (err error, id int64) {
	FN := "[CommitHouseByOwner] "
	beego.Trace(FN, "hif:", hif)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/*	argument checking */
	err = checkHouseInfo(hif, true)
	if nil != err {
		return
	}

	// owner
	if errT, _ := GetUser(oid); nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("owner:%d", oid)}
		return
	}

	// Agency
	// Kenny: when house owner commit new house, they could not assign the agency,
	// 			so the agency 0 is possible.
	// if errT := GetUser(aid); nil != errT {
	// 	err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("agency:%d", aid)}
	// 	return
	// }

	/* processing */
	o := orm.NewOrm()
	qs := o.QueryTable("tbl_house").Filter("Property__Id", hif.Property).Filter("BuildingNo", hif.BuildingNo).Filter("HouseNo", hif.HouseNo)
	bExist := qs.Exist()
	if bExist {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_DUPLICATE, ErrInfo: fmt.Sprintf("property:%d, building:%d, house:%s", hif.Property, hif.BuildingNo, hif.HouseNo)}
		return
	}

	tSubmit := time.Now() // .UTC()
	submitTime := fmt.Sprintf("%d-%d-%d %d:%d:%d", tSubmit.Year(), tSubmit.Month(), tSubmit.Day(), tSubmit.Hour(), tSubmit.Minute(), tSubmit.Second())
	sql := fmt.Sprintf(`INSERT INTO tbl_house(property_id, building_no, house_no, floor_total, floor_this, bedrooms, livingrooms, 
									bathrooms, acreage, owner_id, agency_id, submit_time, for_sale, for_rent) 
							VALUES(%d, %d, '%s', %d, %d, %d, %d, %d, %d, %d, %d, '%s', %t, %t)`,
		hif.Property, hif.BuildingNo, hif.HouseNo, hif.FloorTotal, hif.FloorThis, hif.Bedrooms, hif.Livingrooms,
		hif.Bathrooms, hif.Acreage, oid, aid, submitTime, hif.ForSale, hif.ForRent)
	res, errT := o.Raw(sql).Exec()
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	newId, errT := res.LastInsertId()
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	id = newId
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
*	Get deducted house list
*	Arguments:
*		begin		- from which item to fetch
*		fetch_numb	- how many items to fetch
*	Returns
*		err 	- error info
*		total 	- total number
*		fetched	- fetched quantity
*		ids		- house id list
 */
type DeductedHouse struct {
	HouseId int64
	P0Bide  int
	P1Bide  int
}

func getDeductedHouseList(begin, fetch_numb int64) (err error, total, fetched int64, ids []int64) {
	FN := "[getDeductedHouseList] "
	beego.Trace(FN, "begin:", begin, ", fetch_numb:", fetch_numb)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	// calculate the total house number
	sql_1st := "SELECT min(id) AS id FROM tbl_rental WHERE active=true GROUP BY house_id"
	sql_now := "SELECT  max(id) AS id FROM tbl_rental WHERE active=true GROUP BY house_id"

	sql_t0 := fmt.Sprintf(`SELECT t0.id, house_id, rental_bid 
								FROM tbl_rental AS rental LEFT JOIN (%s) AS t0 
								ON rental.id=t0.id WHERE t0.id IS NOT NULL`, sql_1st)
	sql_t1 := fmt.Sprintf(`SELECT t1.id, house_id, rental_bid 
								FROM tbl_rental AS rental LEFT JOIN (%s) AS t1 
								ON rental.id=t1.id WHERE t1.id IS NOT NULL`, sql_now)
	sql := fmt.Sprintf(`SELECT p0.id as p0_id, p1.id as p1_id, p0.house_id, p0.rental_bid as p0_bid, p1.rental_bid as p1_bid 
							FROM (%s) AS p0, (%s) AS p1 
							WHERE p0.house_id=p1.house_id 
							HAVING (p1_bid - p0_bid) < 0`, sql_t0, sql_t1)
	// beego.Debug(FN, "sql:", sql)

	o := orm.NewOrm()

	// calculate total number
	sql_cnt := fmt.Sprintf("SELECT COUNT(*) AS count FROM (%s) AS tmp", sql)
	var cnt int64
	errT := o.Raw(sql_cnt).QueryRow(&cnt)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	total = cnt

	if 0 == fetch_numb { // user just want to get the total number of deducted house
		return
	}

	if begin >= total {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: "begin position out of range"}
		return
	}

	// fetch records
	sql = sql + fmt.Sprintf(" LIMIT %d, %d", begin, fetch_numb)
	var hs []DeductedHouse
	numb, errTmp := o.Raw(sql).QueryRows(&hs)
	if nil != errTmp {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errTmp.Error()}
		return
	}
	if 0 == numb { // no deducted house found
		return
	}

	for /*k*/ _, v := range hs {
		ids = append(ids, v.HouseId)
	}
	fetched = int64(len(ids))

	return
}

/**
*	Get new house list
*	Arguments:
*		begin		- from which item to fetch
*		fetch_numb	- how many items to fetch
*	Returns
*		err 	- error info
*		total 	- total number
*		fetched	- fetched quantity
*		ids		- house id list
**/
func getNewHouseList(begin, fetch_numb int64) (err error, total, fetched int64, ids []int64) {
	FN := "[getNewHouseList] "
	beego.Trace(FN, "begin:", begin, ", fetch_numb:", fetch_numb)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	// default time window: 7 days
	tNow := time.Now()
	beego.Debug(FN, "tNow:", tNow, ", UTC:", tNow.UTC())
	tBegin := tNow.UTC().Add(-7 * 24 * time.Hour)
	beego.Debug(FN, "tBegin:", tBegin)

	t0 := time.Date(tBegin.Year(), tBegin.Month(), tBegin.Day(), 0, 0, 0, 0, time.UTC)
	beego.Debug(FN, "t0:", t0)
	beego.Warn(FN, "TODO: need to check the real submit_time, UTC or Local?")

	// calculate the total house number
	sql := fmt.Sprintf("SELECT Id FROM tbl_house WHERE publish_time IS NOT NULL AND publish_time>='%s'", t0)
	beego.Debug(FN, "sql:", sql)

	o := orm.NewOrm()

	// calculate total number
	sql_cnt := fmt.Sprintf("SELECT COUNT(*) AS count FROM (%s) AS tmp", sql)
	var cnt int64
	errT := o.Raw(sql_cnt).QueryRow(&cnt)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	total = cnt

	if 0 == fetch_numb { // user just want to get the total number of deducted house
		return
	}
	if begin >= total {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: "begin position out of range"}
		return
	}

	// fetch records
	sql = sql + fmt.Sprintf(" LIMIT %d, %d", begin, fetch_numb)
	var hs []int64
	numb, errTmp := o.Raw(sql).QueryRows(&hs)
	if nil != errTmp {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errTmp.Error()}
		return
	}
	if 0 == numb { // no deducted house found
		return
	}

	ids = hs
	fetched = int64(len(ids))

	return
}

/**
*	Recommend/Unrecommend house
*	Arguments:
*		hid		- house id
*		uid		- login user id
*		act		- action. 1: recommend; 2: unrecommend
*	Returns
*		err 	- error info
 */
func RecommendHouse(hid, uid int64, act int) (err error) {
	FN := "[RecommendHouse] "
	beego.Trace(FN, "hid:", hid, ", act:", act, ", uid:", uid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/* Arguments checking*/
	err, h := getHouse(hid)
	if nil != err {
		return err
	}
	if err, _ = GetUser(uid); nil != err {
		return err
	}

	// only the agency and administrator could recommend/unrecommend
	// if !isHouseAgency(h, uid) || (_, bAdmin := isAdministrator(uid); !bAdmin) {
	// 	return
	// }

	switch act {
	case 1: // recomment
		beego.Debug(FN, "Recommend house:", hid)
		// checking
		o := orm.NewOrm()
		r := TblHouseRecommend{House: hid}
		errT := o.Read(&r, "House")
		if nil == errT { // found record
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_DUPLICATE, ErrInfo: fmt.Sprintf("hid:%d", hid)}
			return
		} else if errT != orm.ErrNoRows && errT != orm.ErrMissPK { // other errors
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
			return
		}

		// right checking
		nullTime := time.Time{}
		// beego.Debug(FN, "nullTime:", nullTime, ", h.PublishTime:", h.PublishTime)
		if nil == interface{}(h.PublishTime) || nullTime == h.PublishTime { // can not publish the house that not published
			beego.Debug(FN, "not published")
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: "house not published"}
			return
		}

		beego.Debug(FN, "h.Agency.Id:", h.Agency.Id)
		permission := 0
		if h.Agency.Id == uid { // agency could recommend houses represent by himself
			permission = 1
		} else {
			if _, bAdmin := isAdministrator(uid); bAdmin { // administrator could recomment any house
				permission = 2
			}
		}
		beego.Debug(FN, "permission:", permission)
		if 0 == permission {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION}
			return
		}

		// Recommend
		r.Who = uid
		r.When = time.Now()
		if /*id*/ _, errT := o.Insert(&r); nil != errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
			return
		}
	case 2: // unrecommend
		// checking
		beego.Debug(FN, "Unrecommend house:", hid)
		o := orm.NewOrm()
		r := TblHouseRecommend{House: hid}
		errT := o.Read(&r, "House")
		if nil != errT { // recommend house does not exist
			if errT == orm.ErrNoRows || errT == orm.ErrMissPK { // not found
				err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("hid:%d", hid)}
			} else {
				err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
			}
			return
		}
		// right checking
		permission := 0
		if r.Who == uid {
			permission = 1
		} else {
			if _, bAdmin := isAdministrator(uid); bAdmin {
				permission = 2
			}
		}
		if 0 == permission {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION}
			return
		}
		// unrecommend
		if /*numb*/ _, errT := o.Delete(&TblHouseRecommend{Id: r.Id}); nil != errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
			return
		}
	default:
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("act:%d", act)}
		return
	}

	// err = commdef.SwError{ErrCode: commdef.ERR_NOT_IMPLEMENT}
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
	if begin >= total {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: "begin position out of range"}
		return
	}

	// fetching
	beego.Warn(FN, "TODO: all() will be restricted by limit, and return max 1000 records")
	var hids []TblHouseRecommend
	numb, errT := qs.Limit(count, begin).OrderBy("When").All(&hids)
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

/**
*	Get house number
*	Arguments:
*		hid		- house id
*	Returns
*		err 		- error info
*		building	- house building number
*		house_no	- house number
**/
func getHouseNumber(hid int64) (err error, building int, house_no string) {
	FN := "[getHouseProperty] "
	beego.Trace(FN, "hid:", hid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	o := orm.NewOrm()
	h := TblHouse{Id: hid}
	errT := o.Read(&h) //, "BuildingNo", "HouseNo")
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	building = h.BuildingNo
	house_no = h.HouseNo
	return
}

/**
*	Get house cover image
*	Arguments:
*		hid		- house id
*	Returns
*		err 	- error info
*		pic		- house cover image id
**/
func getHouseCoverImg(hid int64) (err error, pic int64) {
	FN := "[getHouseProperty] "
	beego.Trace(FN, "hid:", hid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	o := orm.NewOrm()
	h := TblHouse{Id: hid}
	errT := o.Read(&h) // , "CoverImg")
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	pic = h.CoverImg
	return
}

/**
*	Get house agency
*	Arguments:
*		hid		- house id
*	Returns
*		err 	- error info
*		aid		- agency id
*		agency	- agency name
**/
func getHouseAgency(hid int64) (err error, aid int64, agency string) {
	FN := "[getHouseAgency] "
	beego.Trace(FN, "hid:", hid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	o := orm.NewOrm()
	h := TblHouse{Id: hid}
	errT := o.Read(&h) // , "CoverImg")
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	aid = h.Agency.Id
	err, uif := GetUser(aid)
	if nil != err {
		return
	}
	agency = uif.Name
	return
}

/**
*	Get house cover image
*	Arguments:
*		hif		- house information
*		bAdd	- is adding operation
*	Returns
*		err 	- error info
**/
func checkHouseInfo(hif *commdef.HouseInfo, bAdd bool) (err error) {
	// FN := "[checkHouseInfo] "

	if !bAdd && hif.Id <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("hid:%d", hif.Id)}
		return
	}
	if hif.Property <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("property:%d", hif.Property)}
		return
	}
	if hif.BuildingNo <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("building_no:%d", hif.BuildingNo)}
		return
	}
	if hif.FloorTotal <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("floor_total:%d", hif.FloorTotal)}
		return
	}
	if hif.FloorThis <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("floor_this:%d", hif.FloorThis)}
		return
	}
	if hif.FloorThis > hif.FloorTotal {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("floor_this:%d > FloorTotal:%d", hif.FloorThis, hif.FloorTotal)}
		return
	}
	if hif.Bedrooms < 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("bedrooms:%d", hif.Bedrooms)}
		return
	}
	if hif.Livingrooms < 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("livingrooms:%d", hif.Livingrooms)}
		return
	}
	if hif.Bathrooms < 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("bathrooms:%d", hif.Bathrooms)}
		return
	}
	if 0 == hif.Bedrooms && 0 == hif.Livingrooms && 0 == hif.Bathrooms {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT,
			ErrInfo: fmt.Sprintf("Bedrooms:%d, Livingrooms:%d, bathrooms:%d", hif.Bedrooms, hif.Livingrooms, hif.Bathrooms)}
		return
	}
	if hif.Acreage <= 100 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("acreage:%d", hif.Acreage)}
		return
	}
	// if hif.ForSale < 0 || hif.ForSale > 1 {
	// 	err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("ForSale:%d", hif.ForSale)}
	// 	return
	// }
	// if hif.ForRent < 0 || hif.ForRent > 1 {
	// 	err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("ForRent:%d", hif.ForRent)}
	// 	return
	// }

	// do further checking
	// house id
	if !bAdd {
		if err, _ = getHouse(hif.Id); nil != err {
			return
		}
	}

	// property id
	if err, _ = GetPropertyInfo(hif.Property); nil != err {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("Property:%d", hif.Property)}
		return
	}

	return
}

func getHouse(hid int64) (err error, h TblHouse) {
	if hid <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("hid:%d", hid)}
		return
	}

	o := orm.NewOrm()
	hT := TblHouse{Id: hid}
	errT := o.Read(&hT)
	if errT == orm.ErrNoRows || errT == orm.ErrMissPK {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_RES_NOTFOUND, ErrInfo: fmt.Sprintf("hid:%d", hid)}
		return
	} else if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	h = hT
	return
}

func isHouseOwner(h TblHouse, uid int64) bool {
	// FN := "[isHouseOwner] "

	if h.Owner.Id == uid {
		return true
	}

	return false
}

func isHouseAgency(h TblHouse, uid int64) bool {
	// FN := "[isHouseAgency] "

	if h.Agency.Id == uid {
		if _, bAgency := isAgency(uid); bAgency {
			return true
		}
	}

	return false
}

/**
*	get all houses belong to specified landlord
*	Arguments:
*		oid - landlord id
*	Returns
*		hl 	- house list belong to the landlord specified
 */
func getHouseListByOwner(oid int64) (err error, hl []TblHouse) {
	FN := "[getHouseListByOwner] "
	beego.Trace(FN, "oid:", oid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	o := orm.NewOrm()
	qs := o.QueryTable("tbl_house").Filter("Owner__Id", oid)
	/*num*/ _, err = qs.All(&hl)

	return
}
