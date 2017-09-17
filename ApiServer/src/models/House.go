package models

import (
	"ApiServer/commdef"
	"fmt"
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/orm"
	"strconv"
	"strings"
	"time"
)

/**
*	Get house showing time
*	Arguments:
*		hid 	- house id
*		uid 	- login user
*	Returns
*		err 	- error info
*		hst		- house showing time
 */
func GetHouseShowTime(hid, uid int64) (err error, hst commdef.HouseShowTime) {
	FN := "[GetHouseShowTime] "
	beego.Trace(FN, "house:", hid, ", login user:", uid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/* Argeuments checking */

	/* Permission checking */
	if err = canAccessHouse(uid, hid); nil != err {
		return
	}

	/* Processing */
	o := orm.NewOrm()

	nameUnset := getSpecialString(KEY_USER_NAME_NOT_SET)

	sql := `SELECT hs.id, hs.period1, hs.period2, hs.desc, IF (LENGTH(u.name) > 0,  u.name, ?) AS who, hs.when
				FROM tbl_house_show_time AS hs, tbl_user AS u WHERE hs.id=? AND hs.who=u.id`
	ht := commdef.HouseShowTime{Id: hid}
	beego.Debug(FN, fmt.Sprintf("ht:%+v", ht))
	errT := o.Raw(sql, nameUnset, hid).QueryRow(&ht)
	if nil != errT {
		if orm.ErrNoRows != errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("Fail to get house showing period, err:%s", errT.Error())}
			return
		}
	}
	beego.Debug(FN, fmt.Sprintf("ht:%+v", ht))

	hst = ht
	return
}

/**
*	Set house showing time
*	Arguments:
*		hid 	- house id
*		uid 	- login user
*		prdw	- house showing period for working day. 1 - morning, 2 - afternoon, 3 - night
*		prdv	- house showing period for vacation and weekend. 1 - morning, 2 - afternoon, 3 - night
*		desc	- period desc when period is out of pre-defined
*	Returns
*		err 	- error info
 */
func SetHouseShowTime(hid, uid int64, prdw, prdv int, desc string) (err error) {
	FN := "[SetHouseShowTime] "
	beego.Trace(FN, "house:", hid, ", login user:", uid, ", period:", prdw, "|", prdv, ", desc:", desc)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/* Argeuments checking */
	err, h := getHouse(hid)
	if nil != err {
		return
	}
	if prdw < commdef.HOUSE_SHOW_PERIOD_Min || prdw > commdef.HOUSE_SHOW_PERIOD_Max {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("period for working day:%d", prdw)}
		return
	}
	if prdv < commdef.HOUSE_SHOW_PERIOD_Min || prdv > commdef.HOUSE_SHOW_PERIOD_Max {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("period for vacation:%d", prdv)}
		return
	}
	// if prd == commdef.HOUSE_SHOW_PERIOD_OTHERS && 0 == len(desc) {
	// 	err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("period:%d, desc not set", prd)}
	// 	return
	// }

	/* Permission checking*/
	// Only the landlord and administrator is able to set the house showing period
	if isHouseOwner(h, uid) {
	} else if _, bAdmin := isAdministrator(uid); bAdmin {
	} else {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("login user:%d", uid)}
		return
	}

	/* Processing */
	o := orm.NewOrm()

	hst := TblHouseShowTime{Id: hid}
	errT := o.Read(&hst)
	beego.Debug(FN, fmt.Sprintf("hst: %+v", hst))
	if nil != errT {
		if orm.ErrNoRows != errT && orm.ErrMissPK != errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("Fail to get house showing period, err:%s", errT.Error())}
			return
		}
		// not set, add new record
		hst.Id = hid
		hst.Period1 = prdw
		hst.Period2 = prdv
		hst.Desc = desc
		hst.Who = uid
		id, errT := o.Insert(&hst)
		if nil != errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("Fail to insert house showing period, err:%s", errT.Error())}
			return
		}
		beego.Debug(FN, "new id:", id)

	} else { // already exist, update
		hst.Id = hid
		hst.Period1 = prdw
		hst.Period2 = prdv
		hst.Desc = desc
		hst.Who = uid
		hst.When = time.Now()
		numb, errT := o.Update(&hst, "Period1", "Period2", "Desc", "Who", "When")
		if nil != errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("Fail to update house showing period, err:%s", errT.Error())}
			return
		}
		if 1 != numb {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("numb:%d", numb)}
			return
		}
	}

	return
}

type ValueOp struct {
	Operator int // HOUSE_FILTER_TYPE_xxx
	Values   []int64
}

type HouseFilter struct {
	Rental     ValueOp
	Livingroom ValueOp
	Bedroom    ValueOp
	Bathroom   ValueOp
	Acreage    ValueOp
}

/**
*	Get house list by id
*	Arguments:
*		ht 		- list type. ref to commdef.HOUSE_LIST_xxx
*		begin	- from which item to fetch
*		count	- how many items to fetch
*		filter	- query filters. HOUSE_FILTER_TYPE_xxx
*		sort	- sort type. HOUSE_SORT_xxx
*	Returns
*		err 	- error info
*		total 	- total number
*		fetched	- fetched quantity
*		ids		- house id list
 */
func GetHouseListByType(ht int, begin, count int64, filter HouseFilter, sort string) (err error, total, fetched int64, ids []int64) {
	FN := "[GetHouseListByType] "
	beego.Trace(FN, "type:", ht, ", begin:", begin, ", count:", count, ", filter:", fmt.Sprintf("%+v", filter), ", sort:", sort)

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

	// sort types
	sorts := []int{}
	sortT := strings.Split(sort, ",")
	// beego.Debug(FN, "sortT:", sortT)
	for _, v := range sortT {
		// beego.Debug(FN, "v:", v)
		if 0 == len(v) {
			continue
		}
		i, errT := strconv.Atoi(v)
		if nil != errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
			return
		}
		sorts = append(sorts, i)
	}
	// beego.Debug(FN, "sorts:", sorts)

	if isSortTypeExist(sorts, commdef.HOUSE_SORT_PUBLISH) && isSortTypeExist(sorts, commdef.HOUSE_SORT_PUBLISH_DESC) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: "HOUSE_SORT_PUBLISH vs HOUSE_SORT_PUBLISH_DESC"}
		return
	}
	if isSortTypeExist(sorts, commdef.HOUSE_SORT_RENTAL) && isSortTypeExist(sorts, commdef.HOUSE_SORT_RENTAL_DESC) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: "HOUSE_SORT_RENTAL vs HOUSE_SORT_RENTAL_DESC"}
		return
	}
	if isSortTypeExist(sorts, commdef.HOUSE_SORT_APPOINT) && isSortTypeExist(sorts, commdef.HOUSE_SORT_APPOINT_DESC) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: "HOUSE_SORT_APPOINT vs HOUSE_SORT_APPOINT_DESC"}
		return
	}

	/* Permission Checking */

	switch ht {
	case commdef.HOUSE_LIST_Recommend:
		return getRecommendHouseList(begin, count, filter, sorts)
	case commdef.HOUSE_LIST_Deducted:
		return getDeductedHouseList(begin, count, filter, sorts)
	case commdef.HOUSE_LIST_New:
		return getNewHouseList(begin, count, filter, sorts)
	case commdef.HOUSE_LIST_All:
		return getHouseListAll(begin, count, filter, sorts)
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
	beego.Trace(FN, "type:", typ, ", fetch:(", begin, ",", tofetch, "), login user:", uid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	fetched = -1

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
	2) Administrator could see all houses (TODO: ?)
	*/
	_, bAgency := isAgency(uid)
	_, bAdmin := isAdministrator(uid)
	beego.Debug(FN, "bAgency:", bAgency, ", bAdmin:", bAdmin)
	if !bAdmin && !bAgency {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("login user:%d", uid)}
		return
	}

	sql_query := " FROM tbl_house WHERE"
	if bAgency && !bAdmin {
		sql_query += fmt.Sprintf(" agency_id='%d' ", uid)
	} else {
		sql_query += " 1"
	}
	switch typ {
	case commdef.BEHALF_TYPE_ALL:
		sql_query += " AND for_rent=1"
	case commdef.BEHALF_TYPE_TO_RENT:
		sql_query += fmt.Sprintf(" AND for_rent=1 AND rent_stat=%d", commdef.HOUSE_RENT_WAIT)
	case commdef.BEHALF_TYPE_RENTED:
		sql_query += fmt.Sprintf(" AND for_rent=1 AND rent_stat=%d", commdef.HOUSE_RENT_RENTED)
	case commdef.BEHALF_TYPE_TO_SALE:
		sql_query += " AND for_sale=1"
	case commdef.BEHALF_TYPE_TO_APPROVE:
		sql_query += " AND publish_time IS NULL"
	default:
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("list type:%d", typ)}
		return
	}
	// beego.Debug(FN, "sql_query:", sql_query)

	o := orm.NewOrm()

	sql_cnt := "SELECT COUNT(*) AS count" + sql_query
	cnt := int64(0)
	errT := o.Raw(sql_cnt).QueryRow(&cnt)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}
	total = cnt
	beego.Debug(FN, "total:", total)

	if 0 == tofetch || 0 == total { // user just want to know the total number, or no real records could be fetched
		return
	}

	if begin >= total {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("begin:%d", begin)}
		return
	}

	// fetch real houses
	sql := "SELECT id" + sql_query + fmt.Sprintf(" LIMIT %d, %d", begin, tofetch)
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
	beego.Debug(FN, "hids:", hids)

	return
}

/**
*	Get house information by id
*	Arguments:
*		id 	- house id
*		uid	- login user
*	Returns
*		err - error info
*		hd 	- house digest info
 */
func GetHouseDigestInfo(hid, uid int64) (err error, hd commdef.HouseDigest) {
	FN := "[GetHouseDigestInfo] "
	beego.Trace(FN, "house:", hid, ", login user:", uid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/* Permission Checking */
	// if user not login, it can only access the houses published
	table_name := "tbl_house"
	if uid <= 0 {
		table_name = "v_house_published"
	}

	o := orm.NewOrm()

	// House info
	var dig commdef.HouseDigest
	sql := fmt.Sprintf(`SELECT house.id AS id, prop.name AS property, address AS property_addr, bedrooms, 
								livingrooms, bathrooms, acreage, cover_img 
							FROM %s AS house, tbl_property AS prop 
							WHERE house.property_id=prop.id AND house.id=%d`, table_name, hid)
	errTmp := o.Raw(sql).QueryRow(&dig)
	if nil != errTmp {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errTmp.Error()}
		return
	}

	// cover image
	_, url_s, url_m, _ := GetPicUrl(dig.CoverImg, 0, commdef.PIC_SIZE_ALL)
	dig.CovImgUrlS = url_s
	dig.CovImgUrlM = url_m

	// rental price info
	err, rs := getHouseRental(hid)
	if nil != err {
		return
	}

	if len(rs) > 0 {
		p0 := rs[0].RentalBid // first bid price
		dig.PropFee = rs[0].PropFee
		p1 := p0 // last bid price
		if len(rs) > 1 {
			p1 = rs[len(rs)-1].RentalBid
			dig.PropFee = rs[len(rs)-1].PropFee
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
*		be 	- back end
*	Returns
*		err - error info
*		hif - house info
*	Comment: anyone, even not login, could fetch the published house public info for front-end using,
*			 only the right person could feth the complete house info for back-end using
**/
func GetHouseInfo(hid, uid int64, be bool) (err error, hif commdef.HouseInfo) {
	FN := "[GetHouseInfo] "
	beego.Trace(FN, "house:", hid, ", login user:", uid, ", back end:", be)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/* Argument checking */
	house := TblHouse{}
	if be { // back-end using, check house in whole house pool
		err, house = getHouse(hid)
	} else { // front-end using, just check the house published
		err, house = getHousePublished(hid)
	}
	if nil != err {
		return
	}

	/* Permission Checking */
	// House owner, agency and administrator could see the private info
	// front-end using, could just retrieve the private info, no matter what kind user is, and whatever user login or not
	// back-end using, user can see the private info, depend on what kind of user is and if he logined
	bShowPrivInfo := false
	if be {
		if err = canAccessHouse(uid, hid); nil != err {
			return
		}
		bShowPrivInfo = true
	}
	beego.Debug(FN, "bShowPrivInfo:", bShowPrivInfo)

	hif.Id = house.Id
	hif.Property = house.Property.Id
	hif.FloorTotal = house.FloorTotal
	hif.Bedrooms = house.Bedrooms
	hif.Livingrooms = house.Livingrooms
	hif.Bathrooms = house.Bathrooms
	hif.Acreage = house.Acreage
	hif.ForSale = house.ForSale
	hif.ForRent = house.ForRent
	hif.RentStat = house.RentStat
	hif.Decoration = house.Decoration
	hif.ModifyDate = fmt.Sprintf("%s", house.ModifyTime.Local())[:10]
	hif.Agency = house.Agency.Id

	if bShowPrivInfo { // get privite info
		hif.BuildingNo = house.BuildingNo
		hif.FloorThis = house.FloorThis
		hif.HouseNo = house.HouseNo
		hif.BuyDate = fmt.Sprintf("%s", house.PurchaseDate.Local())[:10]

		hif.Landlord = house.Owner.Id
		hif.SubmitTime = house.SubmitTime.Local().String()[:19]

		nullTime := time.Time{}
		if nullTime != house.PublishTime {
			hif.CertStat = commdef.HOUSE_CERT_STAT_PASSED
			hif.CertTime = fmt.Sprintf("%s", house.PublishTime.Local())
		} else { // house not been published
			hc := TblHouseCert{}
			if hc, err = getHouseNewestCert(hid); nil != err {
				return
			}
			if commdef.HOUSE_CERT_STAT_Unknown == hc.CertStatu {
				err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("incorrect status:%d", hc.CertStatu)}
				return
			}
			hif.CertStat = hc.CertStatu // HOUSE_CERT_STAT_FAILED or HOUSE_CERT_STAT_WAIT
			if commdef.HOUSE_CERT_STAT_FAILED == hc.CertStatu {
				hif.CertTime = fmt.Sprintf("%s", hc.When.Local())
				hif.CertDesc = hc.Comment
			}
		}
	} else { // hide the privite info
		hif.BuildingNo = ""
		hif.HouseNo = ""
		if house.FloorThis*3 < hif.FloorTotal {
			hif.FloorThis = hif.FloorTotal + 1 // low storied
		} else if house.FloorThis*3 >= hif.FloorTotal && house.FloorThis*3 < hif.FloorTotal*2 {
			hif.FloorThis = hif.FloorTotal + 2 // middle storied
		} else {
			hif.FloorThis = hif.FloorTotal + 3 // high storied
		}
		hif.BuyDate = fmt.Sprintf("%s", house.PurchaseDate.Local())[:4]
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

	// if the house id(property + building + house_no) is conflict
	o := orm.NewOrm()
	qs := o.QueryTable("tbl_house").Filter("Property__Id", hif.Property).Filter("BuildingNo", hif.BuildingNo).Filter("HouseNo", hif.HouseNo).Exclude("Id", hif.Id)
	bExist := qs.Exist()
	if bExist {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_DUPLICATE,
			ErrInfo: fmt.Sprintf("property:%d, building:%s, house:%s alredy exist", hif.Property, hif.BuildingNo, hif.HouseNo)}
		return
	}

	/* Permission Checking */
	// Only the landlord, house agency and administrator could modify house info
	if err = canModifyHouse(uid, hif.Id); nil != err {
		return
	}

	// Update
	tModi := time.Now() //.UTC()
	modifyTime := fmt.Sprintf("%d-%d-%d %d:%d:%d", tModi.Year(), tModi.Month(), tModi.Day(), tModi.Hour(), tModi.Minute(), tModi.Second())
	sql := fmt.Sprintf(`UPDATE tbl_house SET property_id=%d, building_no='%s', floor_total=%d, floor_this=%d,
								house_no='%s', bedrooms=%d, livingrooms=%d, bathrooms=%d, acreage=%d, 
								modify_time='%s', for_sale=%t, for_rent=%t, decoration=%d, purchase_date='%s' WHERE id=%d`,
		hif.Property, hif.BuildingNo, hif.FloorTotal, hif.FloorThis, hif.HouseNo, hif.Bedrooms, hif.Livingrooms, hif.Bathrooms,
		hif.Acreage, modifyTime, hif.ForSale, hif.ForRent, hif.Decoration, hif.BuyDate, hif.Id)
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
	// new agency id is a real agency
	if err, _ = isAgency(aid); nil != err {
		return
	}

	/* Permission */
	// Only the landload and administrator could assign agency for house
	if err = canModifyHouse(luid, hid); nil != err {
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
	if err = checkImage(cid); nil != err {
		return
	}

	/* Permission */
	// Only the landlord, house agency and administrator could set the cover image
	if err = canModifyHouse(uid, hid); nil != err {
		return
	}

	/* processing */
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
*	Set House Price
*	Arguments:
*		hid 		- house id
*		uid			- login user
*	Returns
*		err - error info
*		pl	- price list
 */
func GetHousePrice(hid, uid int64, begin, fetchCnt int) (err error, total int64, pl []commdef.HousePrice) {
	FN := "[GetHousePrice] "
	beego.Trace(FN, "house:", hid, ", login user:", uid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/* Argument checking */
	// err, h := getHouse(hid)	// canAccessHouse will also check the house
	if begin < 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("begin: %d", begin)}
		return
	}
	if fetchCnt < 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("fetchCnt: %d", fetchCnt)}
		return
	}

	/* Permission checking */
	if err = canAccessHouse(uid, hid); nil != err {
		return
	}

	/* Processing */
	o := orm.NewOrm()

	// counting
	Count := int64(0)
	errT := o.Raw("SELECT COUNT(*) AS count FROM tbl_house_price WHERE house=?", hid).QueryRow(&Count)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}
	total = Count
	if 0 == fetchCnt || 0 == total { // user just want to know the total number in database, or no record for fetching
		return
	}

	if int64(begin) >= total {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("begin: %d", begin)}
		return
	}

	// fetching real price records
	sql := `SELECT p.id, rental_tag, rental_bottom AS rental_min, prop_fee, selling_tag AS sale_price_tag, selling_bottom AS sale_price_min,
					IF (LENGTH(u.name) > 0,  u.name, ?) AS who, p.when
					FROM tbl_house_price AS p, tbl_user AS u WHERE p.who=u.id AND p.house=? ORDER BY id DESC LIMIT ?, ?`

	nameUnset := getSpecialString(KEY_USER_NAME_NOT_SET)
	var lst []commdef.HousePrice
	numb, errT := o.Raw(sql, nameUnset, hid, begin, fetchCnt).QueryRows(&lst)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}
	beego.Debug(FN, "Total:", numb)
	pl = lst

	return
}

/**
*	Set House Price
*	Arguments:
*		hid 		- house id
*		uid			- login user
*		rental_tp	- rental, tag price
*		rental_bp	- rental, bottom price
*		prop_fee	- if the rental involve the property fee
*		price_tp	- selling price, tag price
*		price_bp	- selling price, bottom price
*	Returns
*		err - error info
*		pid	- price id
 */
func SetHousePrice(hid, uid, rental_tp, rental_bp, price_tp, price_bp int64, prop_fee bool) (err error, pid int64) {
	FN := "[SetHousePrice] "
	beego.Trace(FN, "house:", hid, ", login user:", uid, ", rental:(", rental_tp, ",", rental_bp,
		"), include property_fee:", prop_fee, ", selling price:(", price_tp, ",", price_bp, ")")

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/* Argument checking */
	err, h := getHouse(hid)
	if nil != err {
		return
	}

	if h.ForRent {
		if rental_tp < 0 {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("rental tag: %d", rental_tp)}
			return
		}
		if rental_bp < 0 {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("rental bottom: %d", rental_bp)}
			return
		}
		if 0 == rental_tp && 0 == rental_bp {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: "Rental not set"}
			return
		}
		if rental_tp < rental_bp {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("Rental: tag(%d) < bottom(%d)", rental_tp, rental_bp)}
			return
		}
	}

	if h.ForSale {
		if price_tp < 0 {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("selling tag: %d", price_tp)}
			return
		}
		if price_bp < 0 {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("selling bottom: %d", price_bp)}
			return
		}
		if 0 == price_tp && 0 == price_bp {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: "Selling price not set"}
			return
		}
		if price_tp < price_bp {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("Selling price: tag(%d) < bottom(%d)", price_tp, price_bp)}
			return
		}
	}

	if !h.ForRent && !h.ForSale {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: "House can not accept any price"}
		return
	}

	// check if the new price is equal to the "Current Price"
	err, cp := getHouseCurentPrice(hid)
	if nil != err {
		if se, ok := err.(commdef.SwError); ok {
			if commdef.ERR_COMMON_RES_NOTFOUND != se.ErrCode { // no price set for this house
				return
			}
		} else {
			return
		}
	}

	bDuplicate := false
	bRentEqual := false
	bPriceEqual := false
	if h.ForRent {
		if cp.RentalBid == int(rental_tp) && cp.RentalBottom == int(rental_bp) && cp.PropFee == prop_fee {
			bRentEqual = true
			if !h.ForSale {
				bDuplicate = true
			}
		}
	}
	// if h.ForSale {
	// 	if cp.SellingTag == price_tp && cp.SellingBottom == price_bp {
	// 		bPriceEqual = true
	// 		if !h.ForRent {
	// 			bDuplicate = true
	// 		}
	// 	}
	// }
	if !bDuplicate && bRentEqual && bPriceEqual {
		bDuplicate = true
	}

	if bDuplicate {
		beego.Error(FN, fmt.Sprintf("current price:%+v", cp))
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_DUPLICATE}
		return
	}

	/* Premission checking */
	if err = canModifyHouse(uid, hid); nil != err {
		return
	}

	/* Processing */
	o := orm.NewOrm()
	np := TblRental{HouseId: hid, Who: int(uid)}
	if h.ForRent {
		np.RentalBid = int(rental_tp)
		np.RentalBottom = int(rental_bp)
		np.PropFee = prop_fee
		np.Active = true
	}
	// if h.ForSale {
	// 	np.SellingTag = price_tp
	// 	np.SellingBottom = price_bp
	// }

	nId, errT := o.Insert(&np)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	beego.Warn(FN, "TODO: send message if someone change the price")

	pid = nId
	return
}

/**
*	Commit house by owner
*	Arguments:
*		hif	- house info input
*		oid - house owner id
*		aid	- agency id
*	Returns
*		err - error info
*		id 	- new house info
 */
func CommitHouseByOwner(hif *commdef.HouseInfo, oid, aid int64) (err error, id int64) {
	FN := "[CommitHouseByOwner] "
	beego.Trace(FN, "house:", fmt.Sprintf("%+v", hif), ", landlord:", oid, ", agency:", aid)

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
	// Kenny: when house owner commit new house, they may not assign the agency, so the agency 0 is possible.
	if 0 != aid {
		if errT, _ := GetUser(aid); nil != errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("agency:%d", aid)}
			return
		}
	}

	/* processing */
	o := orm.NewOrm()

	qs := o.QueryTable("tbl_house").Filter("Property__Id", hif.Property).Filter("BuildingNo", hif.BuildingNo).Filter("HouseNo", hif.HouseNo)
	bExist := qs.Exist()
	if bExist {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_DUPLICATE, ErrInfo: fmt.Sprintf("property:%d, building:%s, house:%s", hif.Property, hif.BuildingNo, hif.HouseNo)}
		return
	}

	o.Begin()
	defer func() {
		if nil == err {
			o.Commit()
		} else {
			o.Rollback()
		}
	}()

	tSubmit := time.Now() // .UTC()
	submitTime := fmt.Sprintf("%d-%d-%d %d:%d:%d", tSubmit.Year(), tSubmit.Month(), tSubmit.Day(), tSubmit.Hour(), tSubmit.Minute(), tSubmit.Second())
	sql := fmt.Sprintf(`INSERT INTO tbl_house(property_id, building_no, house_no, floor_total, floor_this, bedrooms, livingrooms, 
									bathrooms, acreage, decoration, owner_id, agency_id, purchase_date, submit_time, for_sale, for_rent) 
							VALUES(%d, '%s', '%s', %d, %d, %d, %d, %d, %d, %d, %d, %d, '%s', '%s', %t, %t)`,
		hif.Property, hif.BuildingNo, hif.HouseNo, hif.FloorTotal, hif.FloorThis, hif.Bedrooms, hif.Livingrooms,
		hif.Bathrooms, hif.Acreage, hif.Decoration, oid, aid, hif.BuyDate, submitTime, hif.ForSale, hif.ForRent)
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

	// add new record in TblHouseCert
	comment := "业主提交新房源"
	err, hcid := addHouseCertRec(newId, oid, commdef.HOUSE_CERT_STAT_WAIT, comment)
	if nil != err {
		return
	}

	// generate a system message to notify the agency or administrator if no agency assigned
	if aid > 0 { // agency assinged
		err, _ = addMessage(commdef.MSG_HouseCertification, commdef.MSG_PRIORITY_Info, hcid, aid, comment)
		if nil != err {
			return
		}
	} else { // no agency assigned
		var aus []TblUserGroupMember // admin users
		// numb, errT = o.QueryTable("tbl_user_group_member").Filter("Group", ags).All(&aus)
		numb, errT := o.QueryTable("tbl_user_group_member").Filter("Group__Admin", true).All(&aus)
		if nil != errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
			return
		}
		beego.Debug(FN, fmt.Sprintf("%d administrators found", numb))
		for _, v := range aus { // send system message to each administrator
			err, _ = addMessage(commdef.MSG_HouseCertification, commdef.MSG_PRIORITY_Info, hcid, v.User.Id, comment)
			if nil != err {
				return
			}
		}
	}

	id = newId
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
	beego.Trace(FN, "house:", hid, ", act:", act, ", login user:", uid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/* Arguments checking*/
	if 1 != act && 2 != act {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("act:%d", act)}
		return
	}
	err, h := getHouse(hid)
	if nil != err {
		return err
	}
	if err, _ = GetUser(uid); nil != err {
		return err
	}

	/* Permission checking */
	// Only the house agency and administrator could recommend house and revoke the recommendation
	if isHouseAgency(h, uid) {
	} else if _, bAdmin := isAdministrator(uid); bAdmin {
	} else {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("uid:%d", uid)}
		return
	}
	// if the house has been published, no one ould recomment or unrecomment the house not been published
	nullTime := time.Time{}
	// beego.Debug(FN, "nullTime:", nullTime, ", h.PublishTime:", h.PublishTime)
	if nil == interface{}(h.PublishTime) || nullTime == h.PublishTime {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: "house not published"}
		return
	}

	switch act {
	case 1: // recomment
		beego.Debug(FN, "Recommend house:", hid)
		// checking
		o := orm.NewOrm()
		r := TblHouseRecommend{House: hid}
		errT := o.Read(&r, "House")
		if nil == errT { // found record
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_DUPLICATE, ErrInfo: fmt.Sprintf("hid:%d already been recommended", hid)}
			return
		} else if errT != orm.ErrNoRows && errT != orm.ErrMissPK { // other errors
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
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

func getDeductedHouseList(begin, fetch_numb int64, filter HouseFilter, sorts []int) (err error, total, fetched int64, ids []int64) {
	FN := "[getDeductedHouseList] "
	beego.Trace(FN, "begin:", begin, ", fetch_numb:", fetch_numb)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	// calculate the total house number
	sql_1st := `SELECT min(r.id) AS id FROM tbl_rental AS r, v_house_published AS h 
					WHERE r.active=true AND r.house_id=h.id GROUP BY house_id`
	sql_now := `SELECT max(r.id) AS id FROM tbl_rental AS r, v_house_published AS h 
					WHERE r.active=true AND r.house_id=h.id GROUP BY house_id`

	sql_t0 := fmt.Sprintf(`SELECT t0.id, house_id, rental_bid FROM (%s) AS t0 LEFT JOIN tbl_rental AS r ON r.id=t0.id`, sql_1st)
	sql_t1 := fmt.Sprintf(`SELECT t1.id, house_id, rental_bid FROM (%s) AS t1 LEFT JOIN tbl_rental AS r ON r.id=t1.id`, sql_now)
	sql := fmt.Sprintf(`SELECT p0.id AS p0_id, p1.id AS p1_id, p0.house_id, p0.rental_bid AS p0_bid, p1.rental_bid AS p1_bid 
							FROM (%s) AS p0, (%s) AS p1 
							WHERE p0.house_id=p1.house_id
							HAVING (p1_bid - p0_bid) < 0`, sql_t0, sql_t1)
	// beego.Debug(FN, "sql:", sql)

	strFilter, strSort := getHouseListFilterAndSort(filter, sorts)
	sql_cnt := fmt.Sprintf("SELECT COUNT(*) AS count FROM (%s) AS r1, (SELECT h.id %s) AS h1 WHERE r1.house_id=h1.id", sql, strFilter)

	o := orm.NewOrm()

	// calculate total number
	// sql_cnt := fmt.Sprintf("SELECT COUNT(*) AS count FROM (%s) AS tmp", sql)
	var cnt int64
	errT := o.Raw(sql_cnt).QueryRow(&cnt)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	total = cnt
	beego.Debug(FN, "total:", total)

	if 0 == fetch_numb { // user just want to get the total number of deducted house
		return
	}

	if begin >= total {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: "begin position out of range"}
		return
	}

	// fetch records
	sql = fmt.Sprintf("SELECT r1.* FROM (SELECT h.id %s %s) AS h1, (%s) AS r1 WHERE r1.house_id=h1.id", strFilter, strSort, sql)
	// sql = sql + fmt.Sprintf(" LIMIT %d, %d", begin, fetch_numb)
	hs := []DeductedHouse{}
	numb, errTmp := o.Raw(sql).QueryRows(&hs)
	if nil != errTmp {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errTmp.Error()}
		return
	}
	if 0 == numb { // no deducted house found
		return
	}

	for k, v := range hs {
		beego.Debug(FN, fmt.Sprintf("[%d] %+v", k, v))
		ids = append(ids, v.HouseId)
	}
	fetched = int64(len(ids))

	return
}

func getHouseListAll(begin, fetch_numb int64, filter HouseFilter, sorts []int) (err error, total, fetched int64, ids []int64) {
	FN := "[getHouseListAll] "

	beego.Trace(FN, "begin:", begin, ", fetch_numb:", fetch_numb, ", filter:", fmt.Sprintf("%+v", filter))

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/* Argument checking */

	/* Permission checking */

	/* Processing */
	strFilter, strSort := getHouseListFilterAndSort(filter, sorts)

	// count calculating
	Count := int64(0)
	o := orm.NewOrm()
	errT := o.Raw("SELECT COUNT(*)" + strFilter).QueryRow(&Count)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}
	total = Count
	beego.Debug(FN, "total:", total)

	if 0 == fetch_numb || 0 == total { // user just want to get the total number or no record could be fetched
		return
	}

	if begin >= total {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("begin(%d) over the bound(%d)", begin, total)}
		return
	}

	// fetch real records
	sql := `SELECT h.id ` + strFilter + strSort + " LIMIT ?, ?"
	idlst := []int64{}
	numb, errT := o.Raw(sql, begin, fetch_numb).QueryRows(&idlst)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}
	beego.Debug(FN, fmt.Sprintf("%d records fetched", numb))

	ids = idlst
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
func getNewHouseList(begin, fetch_numb int64, filter HouseFilter, sorts []int) (err error, total, fetched int64, ids []int64) {
	FN := "[getNewHouseList] "
	beego.Trace(FN, "begin:", begin, ", fetch_numb:", fetch_numb)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	// default times window: 7 days
	tNow := time.Now()
	beego.Debug(FN, "tNow:", tNow, ", UTC:", tNow.UTC())
	tBegin := tNow.UTC().Add(-7 * 24 * time.Hour)
	beego.Debug(FN, "tBegin:", tBegin)

	t0 := time.Date(tBegin.Year(), tBegin.Month(), tBegin.Day(), 0, 0, 0, 0, time.UTC)
	beego.Debug(FN, "t0:", t0)
	beego.Warn(FN, "TODO: need to check the real submit_time, UTC or Local?")

	strFilter, strSort := getHouseListFilterAndSort(filter, sorts)

	// calculate the total house number
	o := orm.NewOrm()

	// calculate total number
	sql_cnt := fmt.Sprintf("SELECT COUNT(*) %s AND publish_time>='%s'", strFilter, t0)
	cnt := int64(0)
	errT := o.Raw(sql_cnt).QueryRow(&cnt)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	total = cnt

	if 0 == fetch_numb || 0 == total { // user just want to get the total number of deducted house
		return
	}

	if begin >= total {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: "begin position out of range"}
		return
	}

	// fetch records
	sql := fmt.Sprintf("SELECT h.Id %s AND publish_time>='%s' %s LIMIT %d, %d", strFilter, t0, strSort, begin, fetch_numb)
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
*		begin		- from which item to fetch
*		fetchCnt	- how many items to fetch
*	Returns
*		err 	- error info
*		total 	- total number
*		fetched	- fetched quantity
*		ids		- house id list
 */
func getRecommendHouseList(begin, fetchCnt int64, filter HouseFilter, sorts []int) (err error, total, fetched int64, ids []int64) {
	FN := "[getRecommendHouseList] "
	beego.Trace(FN, "begin:", begin, ", fetchCnt:", fetchCnt)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	o := orm.NewOrm()

	strFilter, strSort := getHouseListFilterAndSort(filter, sorts)

	// calculate the total house number
	sql_cnt := fmt.Sprintf(`SELECT COUNT(*) AS count FROM tbl_house_recommend AS r, 
								(SELECT h.id %s) AS h1 WHERE r.house=h1.id`, strFilter)
	var Count int64 = 0
	errT := o.Raw(sql_cnt).QueryRow(&Count)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	total = Count
	beego.Debug(FN, "total:", total)

	if 0 == fetchCnt || 0 == total { // user just want to detect how many houses, or there no reccor could be fetched
		return
	}

	// fetch exact house list
	if begin >= total {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: "begin position out of range"}
		return
	}

	// fetching
	beego.Warn(FN, "TODO: all() will be restricted by limit, and return max 1000 records")
	sql := fmt.Sprintf(`SELECT h1.id AS id FROM (SELECT h.id %s %s) AS h1, tbl_house_recommend AS r1
							WHERE h1.id=r1.house LIMIT ?, ? `, strFilter, strSort)
	idlst := []int64{}
	numb, errT := o.Raw(sql, begin, fetchCnt).QueryRows(&idlst)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}
	beego.Debug(FN, "idlst:", idlst)

	fetched = numb
	ids = idlst

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
func getHouseNumber(hid int64) (err error, building, house_no string) {
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
	FN := "[checkHouseInfo] "

	if !bAdd && hif.Id <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("hid:%d", hif.Id)}
		return
	}
	if hif.Property <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("property:%d", hif.Property)}
		return
	}
	if 0 == len(hif.BuildingNo) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("building_no:%s", hif.BuildingNo)}
		return
	}
	nBuilding, errT := strconv.ParseInt(hif.BuildingNo, 10, 64)
	if nil == errT {
		if nBuilding <= 0 {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("building_no:%d", nBuilding)}
			return
		}
	} else {
		beego.Warn(FN, "errT:", errT.Error())
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
	if hif.Decoration < commdef.DECORATION_Workblank || hif.Decoration > commdef.DECORATION_Luxury {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("decoration:%d", hif.Decoration)}
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
	if 0 == len(hif.BuyDate) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("buying date not set")}
		return
	}
	if _, errT = time.Parse("2006-01-02", hif.BuyDate); nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("date error: %s, %s", hif.BuyDate, errT.Error())}
		return
	}

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

func getHousePublished(hid int64) (err error, h TblHouse) {
	if hid <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("hid:%d", hid)}
		return
	}

	o := orm.NewOrm()
	ht := TblHouse{}
	errT := o.Raw(`SELECT * FROM v_house_published WHERE id=?`, hid).QueryRow(&ht)
	if errT == orm.ErrNoRows || errT == orm.ErrMissPK {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_RES_NOTFOUND, ErrInfo: fmt.Sprintf("hid:%d, err: %s", hid, errT.Error())}
		return
	} else if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	h = ht
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
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_RES_NOTFOUND, ErrInfo: fmt.Sprintf("hid:%d, err: %s", hid, errT.Error())}
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

/**
*	Delete the house specified (just for testing now)
*	Arguments:
*		hid 	- house id
*		luid	- login user id
*	Returns
 */
func delHouse(hid, luid int64) (err error) {
	FN := "[delHouse] "
	beego.Trace(FN, "house:", hid, ", login user:", luid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	err, _ = getHouse(hid)
	if nil != err {
		return
	}

	/* Only Administrator is able to delte the house */
	if _, bAdmin := isAdministrator(luid); !bAdmin {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("luid:%d", luid)}
		return
	}

	// TODO
	beego.Warn(FN, "checking house relation or just delete all of them along with house")

	o := orm.NewOrm()
	o.Begin()
	defer func() {
		if nil != err {
			o.Rollback()
		} else {
			o.Commit()
		}
	}()

	// tbl_house_cert  delHouseCertRecByHose
	err, numb, hcs := removeHouseCertRecByHose(hid)
	if nil != err {
		return
	}

	// tbl_message
	if numb > 0 {
		hcids := []int64{}
		for _, v := range hcs {
			hcids = append(hcids, v.Id)
		}
		numb, errT := o.QueryTable("tbl_message").Filter("Type", commdef.MSG_HouseCertification).Filter("RefId__in", hcids).Delete()
		if nil != errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
			return
		}
		beego.Debug(FN, fmt.Sprintf("delete %d records from tbl_message", numb))
	}

	if /*num*/ _, err1 := o.Delete(&TblHouse{Id: hid}); err1 != nil {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: err1.Error()}
		return
	}

	return
}

// check if the login user has right to access the private info of house
func canAccessHouse(uid, hid int64) (err error) {
	FN := "[canAccessHouse] "
	beego.Trace(FN, "house:", hid, ", login user:", uid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	err, h := getHouse(hid)
	if nil != err {
		return
	}

	if isHouseOwner(h, uid) || isHouseAgency(h, uid) { // landlord or house agency
		return
	}

	if _, bAdmin := isAdministrator(uid); bAdmin { // administrator
		return
	}

	if h.Public { // house is public
		if _, bAgency := isAgency(uid); bAgency { // login user is a agency
			return
		}
	}

	err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("login user:%d", uid)}
	return
}

// check if the login user has right to modify the house info
func canModifyHouse(uid, hid int64) (err error) {
	FN := "[canModifyHouse] "
	beego.Trace(FN, "house:", hid, ", login user:", uid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	err, h := getHouse(hid)
	if nil != err {
		return
	}

	if isHouseOwner(h, uid) || isHouseAgency(h, uid) { // landlord or house agency
		return
	}

	if _, bAdmin := isAdministrator(uid); bAdmin { // administrator
		return
	}

	err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("login user:%d", uid)}
	return
}

func getHouseCurentPrice(hid int64) (err error, cp TblRental) {

	o := orm.NewOrm()
	qs := o.QueryTable("tbl_rental").Filter("HouseId", hid).Filter("Active", true)

	var p TblRental
	errT := qs.OrderBy("-Id").One(&p)
	if nil != errT {
		switch errT {
		case orm.ErrMultiRows:
			{
			}
		case orm.ErrNoRows:
			{
				err = commdef.SwError{ErrCode: commdef.ERR_COMMON_RES_NOTFOUND, ErrInfo: fmt.Sprintf("house:%d", hid)}
				return
			}
		default:
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
			return
		}
	}

	cp = p
	return
}

func delHousePrices(hid int64) (err error) {
	FN := "[delHousePrices] "
	beego.Trace(FN, "house:", hid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	o := orm.NewOrm()
	qs := o.QueryTable("tbl_house_price").Filter("House", hid)
	numb, errT := qs.Delete()
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	beego.Debug(FN, fmt.Sprintf("%d records get deleted", numb))
	return
}

func getHouseQuery(vo ValueOp, key string) (qs string) {
	// FN := "[getHouseQuery] "
	// beego.Debug(FN, fmt.Sprintf("vo:%+v, key:%s", vo, key))

	switch vo.Operator {
	case commdef.HOUSE_FILTER_TYPE_EQ:
		qs += fmt.Sprintf(" %s=%d", key, vo.Values[0])
	case commdef.HOUSE_FILTER_TYPE_LT:
		qs += fmt.Sprintf(" %s<%d", key, vo.Values[0])
	case commdef.HOUSE_FILTER_TYPE_LE:
		qs += fmt.Sprintf(" %s<=%d", key, vo.Values[0])
	case commdef.HOUSE_FILTER_TYPE_GT:
		qs += fmt.Sprintf(" %s>%d", key, vo.Values[0])
	case commdef.HOUSE_FILTER_TYPE_GE:
		qs += fmt.Sprintf(" %s>=%d", key, vo.Values[0])
	case commdef.HOUSE_FILTER_TYPE_BETWEEN:
		qs += fmt.Sprintf(" %s BETWEEN %d AND %d", key, vo.Values[0], vo.Values[1])
	case commdef.HOUSE_FILTER_TYPE_IN:
		strIn := ""
		for k, v := range vo.Values {
			if k > 0 {
				strIn += ","
			}
			strIn += fmt.Sprintf("%d", v)
		}
		qs += fmt.Sprintf(" %s IN (%s)", key, strIn)
	}

	return
}

func isSortTypeExist(sorts []int, sortType int) bool {

	for _, v := range sorts {
		// beego.Debug("v:", v)
		if v == sortType {
			// beego.Debug("found:", sortType)
			return true
		}
	}

	return false
}

func getHouseListFilterAndSort(filter HouseFilter, sorts []int) (strFilter, strSort string) {
	FN := "[getHouseListFilterAndSort] "

	sqlCondition := ""
	if filter.Livingroom.Operator > 0 {
		if len(sqlCondition) > 0 {
			sqlCondition += " AND "
		}
		sqlCondition += getHouseQuery(filter.Livingroom, "livingrooms")
	}
	if filter.Bedroom.Operator > 0 {
		if len(sqlCondition) > 0 {
			sqlCondition += " AND "
		}
		sqlCondition += getHouseQuery(filter.Bedroom, "bedrooms")
	}
	if filter.Bathroom.Operator > 0 {
		if len(sqlCondition) > 0 {
			sqlCondition += " AND "
		}
		sqlCondition += getHouseQuery(filter.Bathroom, "bathrooms")
	}
	if filter.Acreage.Operator > 0 {
		if len(sqlCondition) > 0 {
			sqlCondition += " AND "
		}
		sqlCondition += getHouseQuery(filter.Acreage, "acreage")
	}

	if filter.Rental.Operator > 0 ||
		isSortTypeExist(sorts, commdef.HOUSE_SORT_RENTAL) ||
		isSortTypeExist(sorts, commdef.HOUSE_SORT_RENTAL_DESC) ||
		isSortTypeExist(sorts, commdef.HOUSE_SORT_APPOINT) ||
		isSortTypeExist(sorts, commdef.HOUSE_SORT_APPOINT_DESC) {

		// appointment
		sqlAppoint := fmt.Sprintf(`SELECT house, COUNT(*) AS appoints FROM tbl_appointment 
										WHERE close_time IS NULL AND order_type=%d 
										GROUP BY House`, commdef.ORDER_TYPE_SEE_APARTMENT)
		// rental
		sqlRental := `SELECT id, house_id, rental_bid FROM tbl_rental as r GROUP BY id, house_id, rental_bid
							HAVING id=(SELECT MAX(id) FROM tbl_rental WHERE house_id=r.house_id AND active=1)`

		strFilter = fmt.Sprintf(` FROM v_house_published AS h 
										LEFT JOIN (%s) AS r ON h.id=r.house_id 
										LEFT JOIN (%s) AS a ON h.id=a.house 
									WHERE `, sqlRental, sqlAppoint)

		switch filter.Rental.Operator {
		case commdef.HOUSE_FILTER_TYPE_EQ:
			strFilter += fmt.Sprintf(" r.rental_bid=%d", filter.Rental.Values[0])
		case commdef.HOUSE_FILTER_TYPE_LT:
			strFilter += fmt.Sprintf(" r.rental_bid<%d", filter.Rental.Values[0])
		case commdef.HOUSE_FILTER_TYPE_LE:
			strFilter += fmt.Sprintf(" r.rental_bid<=%d", filter.Rental.Values[0])
		case commdef.HOUSE_FILTER_TYPE_GT:
			strFilter += fmt.Sprintf(" r.rental_bid>%d", filter.Rental.Values[0])
		case commdef.HOUSE_FILTER_TYPE_GE:
			strFilter += fmt.Sprintf(" r.rental_bid>=%d", filter.Rental.Values[0])
		case commdef.HOUSE_FILTER_TYPE_BETWEEN:
			strFilter += fmt.Sprintf(" r.rental_bid BETWEEN %d AND %d", filter.Rental.Values[0], filter.Rental.Values[1])
		case commdef.HOUSE_FILTER_TYPE_IN:
			strIn := ""
			for k, v := range filter.Rental.Values {
				if k > 0 {
					strIn += ","
				}
				strIn += fmt.Sprintf("%d", v)
			}
			strFilter += fmt.Sprintf(" r.rental_bid IN (%s)", strIn)
		default:
			strFilter += " 1 "
		}
		if len(sqlCondition) > 0 {
			strFilter += " AND " + sqlCondition
		}

		if len(sorts) > 0 {
			strSort += " ORDER BY "
			for k, v := range sorts {
				if k > 0 {
					strSort += ","
				}
				switch v {
				case commdef.HOUSE_SORT_PUBLISH:
					strSort += " publish_time"
				case commdef.HOUSE_SORT_PUBLISH_DESC:
					strSort += " publish_time DESC"
				case commdef.HOUSE_SORT_RENTAL:
					strSort += " rental_bid"
				case commdef.HOUSE_SORT_RENTAL_DESC:
					strSort += " rental_bid DESC"
				case commdef.HOUSE_SORT_APPOINT:
					strSort += " appoints"
				case commdef.HOUSE_SORT_APPOINT_DESC:
					strSort += " appoints DESC"
				}
			}
		}
	} else {
		strFilter = ` FROM v_house_published AS h `
		if len(sqlCondition) > 0 {
			strFilter += " WHERE " + sqlCondition
		} else {
			strFilter += " WHERE 1"
		}

		if len(sorts) > 0 {
			strSort += " ORDER BY "
			for k, v := range sorts {
				if k > 0 {
					strFilter += ","
				}
				switch v {
				case commdef.HOUSE_SORT_PUBLISH:
					strSort += " publish_time"
				case commdef.HOUSE_SORT_PUBLISH_DESC:
					strSort += " publish_time DESC"
				}
			}
		}
	}

	beego.Debug(FN, "strFilter:", strFilter)
	beego.Debug(FN, "strSort:", strSort)
	return
}
