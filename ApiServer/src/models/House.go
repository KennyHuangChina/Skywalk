package models

import (
	"ApiServer/commdef"
	"fmt"
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/orm"
	"time"
)

/**
*	add property
*	Arguments:
*		prop	- property name
*	Returns
*		err 	- error info
*		id		- new property id
 */
func AddProperty(prop string) (err error, id int64) {
	FN := "[AddProperty] "
	beego.Trace(FN, "prop:", prop)

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
	if nil == errt { // property already exist, return directly
		id = p.Id
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
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("Invalid begin position:%d", begin)}
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
	return
}

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
*	Get house information by id
*	Arguments:
*		id - house id
*	Returns
*		err - error info
*		hd 	- house digest info
 */
func GetHouseDigestInfo(hid, uid int64) (err error, hd commdef.HouseDigest) {
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
*		id - house id
*	Returns
*		err - error info
*		hif - house info
**/
func GetHouseInfo(hid int64) (err error, hif commdef.HouseInfo) {
	FN := "[GetHouseInfo] "
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
*	Add New House
*	Arguments:
*		hid - house id
*		pid	- property id
*	Returns
*		err - error info
*		id 	- new house info
 */
func ModifyHouse(hif *commdef.HouseInfo) (err error) {
	FN := "[AddHouse] "
	beego.Trace(FN, "hif:", hif)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/*	argument checking */
	err = checkHouseInfo(hif, false)
	if nil != err {
		return
	}

	err = commdef.SwError{ErrCode: commdef.ERR_NOT_IMPLEMENT}
	return
}

/**
*	Add New House
*	Arguments:
*		oid - house owner id
*		pid	- property id
*		aid	- agency id
*	Returns
*		err - error info
*		id 	- new house info
 */
func AddHouse(hif *commdef.HouseInfo, oid, aid int64 /*, building_no int, house_no string, floor_total, floor_this, bedrooms, livingrooms, bathrooms, acreage int*/) (err error, id int64) {
	FN := "[AddHouse] "
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
	beego.Warn(FN, "TODO: argument checking, oid & aid")

	o := orm.NewOrm()
	qs := o.QueryTable("tbl_house").Filter("Property__Id", hif.Property).Filter("BuildingNo", hif.BuddingNo).Filter("HouseNo", hif.HouseNo)
	bExist := qs.Exist()
	if bExist {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_DUPLICATE, ErrInfo: fmt.Sprintf("property:%d, building:%d, house:%s", hif.Property, hif.BuddingNo, hif.HouseNo)}
		return
	}

	tSubmit := time.Now().UTC()
	submitTime := fmt.Sprintf("%d-%d-%d %d:%d:%d", tSubmit.Year(), tSubmit.Month(), tSubmit.Day(), tSubmit.Hour(), tSubmit.Minute(), tSubmit.Second())
	sql := fmt.Sprintf(`INSERT INTO tbl_house(property_id, building_no, house_no, floor_total, floor_this, bedrooms, livingrooms, bathrooms, acreage, owner_id, agency_id, submit_time) 
							VALUES(%d, %d, '%s', %d, %d, %d, %d, %d, %d, %d, %d, '%s')`,
		hif.Property, hif.BuddingNo, hif.HouseNo, hif.FloorTotal, hif.FloorThis, hif.Bedrooms, hif.Livingrooms, hif.Bathrooms, hif.Acreage, oid, aid, submitTime)
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
*	Get house cover image
*	Arguments:
*		hif		- house information
*	Returns
*		err 	- error info
**/
func checkHouseInfo(hif *commdef.HouseInfo, bAdd bool) (err error) {
	FN := "[checkHouseInfo] "

	if !bAdd && hif.Id <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("hid:%d", hif.Id)}
		return
	}
	if hif.Property <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("property:%d", hif.Property)}
		return
	}
	if hif.BuddingNo <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("building_no:%d", hif.BuddingNo)}
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
	if hif.Acreage <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("acreage:%d", hif.Acreage)}
		return
	}

	// do further checking
	beego.Warn(FN, "TODO: hif.Id, hif.Property")

	return
}
