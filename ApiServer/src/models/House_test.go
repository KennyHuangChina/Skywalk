package models

import (
	"ApiServer/commdef"
	"fmt"
	"strconv"
	// "github.com/astaxie/beego"
	"testing"
)

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- GetHouseListByType --
//
func Test_GetHouseListByType_1(t *testing.T) {
	t.Log("Test GetHouseListByType, Invalid Arguments")

	// Incorrect house type: < 0
	if e /*total*/, _ /*fetched*/, _, _ := GetHouseListByType(commdef.HOUSE_LIST_Unknown-1, 0, 0); e == nil {
		t.Error("Failed, err: ", e)
	} else {
		// t.Log("Pass, user:", uif)
	}
	// Incorrect house type: > HOUSE_LIST_Max
	if e /*total*/, _ /*fetched*/, _, _ := GetHouseListByType(commdef.HOUSE_LIST_Max+1, 0, 0); e == nil {
		t.Error("Failed, err: ", e)
	} else {
		// t.Log("Pass, user:", uif)
	}

	// Incorrect begin position
	if e /*total*/, _ /*fetched*/, _, _ := GetHouseListByType(commdef.HOUSE_LIST_Max, -1, 0); e == nil {
		t.Error("Failed, err: ", e)
	} else {
		// t.Log("Pass, user:", uif)
	}

	// Incorrect fetch count
	if e /*total*/, _ /*fetched*/, _, _ := GetHouseListByType(commdef.HOUSE_LIST_Max, 0, -1); e == nil {
		t.Error("Failed, err: ", e)
	} else {
		// t.Log("Pass, user:", uif)
	}

	t.Error("Not Implement")
}

func Test_getRecommendHouseList_1(t *testing.T) {
	t.Log("Test getRecommendHouseList")

	// get total number
	e, total, _, _ := GetHouseListByType(commdef.HOUSE_LIST_Recommend, 0, 0)
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	t.Log("total:", total)

	e, total, _, _ = GetHouseListByType(commdef.HOUSE_LIST_Recommend, total, 1)
	if e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	nCount := total
	if total > 10 {
		nCount = 10
	}
	e, total, fetched, ids := GetHouseListByType(commdef.HOUSE_LIST_Recommend, 0, nCount)
	if e != nil || fetched != nCount {
		t.Error("Failed, err: ", e, ", fetched:", fetched)
		return
	}
	t.Log("IDs:", ids)
}

func Test_getDeductedHouseList_1(t *testing.T) {
	t.Log("Test getDeductedHouseList")

	// get total number
	e, total, _, _ := GetHouseListByType(commdef.HOUSE_LIST_Deducted, 0, 0)
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	t.Log("total:", total)

	e, total, _, _ = GetHouseListByType(commdef.HOUSE_LIST_Deducted, total, 1)
	if e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	nCount := total
	if total > 10 {
		nCount = 10
	}
	e, total, fetched, ids := GetHouseListByType(commdef.HOUSE_LIST_Deducted, 0, nCount)
	if e != nil || fetched != nCount {
		t.Error("Failed, err: ", e, ", fetched:", fetched)
		return
	}
	t.Log("IDs:", ids)
}

func Test_getNewHouseList_1(t *testing.T) {
	t.Log("Test getNewHouseList")

	// get total number
	e, total, _, _ := GetHouseListByType(commdef.HOUSE_LIST_New, 0, 0)
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	t.Log("total:", total)

	e, total, _, _ = GetHouseListByType(commdef.HOUSE_LIST_New, total, 1)
	if e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	nCount := total
	if total > 10 {
		nCount = 10
	}
	e, total, fetched, ids := GetHouseListByType(commdef.HOUSE_LIST_New, 0, nCount)
	if e != nil || fetched != nCount {
		t.Error("Failed, err: ", e, ", fetched:", fetched)
		return
	}
	t.Log("IDs:", ids)
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- GetBehalfList --
//
func Test_GetBehalfList_1(t *testing.T) {
	t.Log("Test GetBehalfList, Invalid Arguments")

	t.Log("<Case> begin position < 0")
	if e /*total*/, _ /*fetched*/, _, _ := GetBehalfList(commdef.BEHALF_TYPE_ALL, -1, 0, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> fetch count < 0")
	if e /*total*/, _ /*fetched*/, _, _ := GetBehalfList(commdef.BEHALF_TYPE_ALL, 0, -1, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> login user has no right to access, neither agency nor administrator")
	if e /*total*/, _ /*fetched*/, _, _ := GetBehalfList(commdef.BEHALF_TYPE_ALL, 0, 0, 9); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> login user is house owner himself")
	if e /*total*/, _ /*fetched*/, _, _ := GetBehalfList(commdef.BEHALF_TYPE_ALL, 0, 0, 2); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> login user is a agency")
	e, total /*fetched*/, _, _ := GetBehalfList(commdef.BEHALF_TYPE_ALL, 0, 0, 6)
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	t.Log("total:", total)

	t.Log("<Case> login user is a administrator")
	e, total /*fetched*/, _, _ = GetBehalfList(commdef.BEHALF_TYPE_ALL, 0, 0, 4)
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	t.Log("total:", total)

	t.Log("<Case> begin position beyond total")
	if e, total /*fetched*/, _, _ = GetBehalfList(commdef.BEHALF_TYPE_ALL, total, 1, 4); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> all behalfed houses")
	e, total, fetched, hs := GetBehalfList(commdef.BEHALF_TYPE_ALL, 0, total, 4)
	t.Log("fetched:", fetched, ", houses:", hs)
	if e != nil || total != fetched {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> all behalfed houses to rent")
	e, total, fetched, _ = GetBehalfList(commdef.BEHALF_TYPE_TO_RENT, 0, 0, 4)
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	t.Log("total:", total)
	e, total, fetched, hs = GetBehalfList(commdef.BEHALF_TYPE_TO_RENT, 0, total, 4)
	t.Log("fetched:", fetched, ", houses:", hs)
	if e != nil || total != fetched {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> all behalfed houses rented")
	e, total, fetched, _ = GetBehalfList(commdef.BEHALF_TYPE_RENTED, 0, 0, 4)
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	t.Log("total:", total)
	e, total, fetched, hs = GetBehalfList(commdef.BEHALF_TYPE_RENTED, 0, total, 4)
	t.Log("fetched:", fetched, ", houses:", hs)
	if e != nil || total != fetched {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> all behalfed houses to sale")
	e, total, fetched, _ = GetBehalfList(commdef.BEHALF_TYPE_TO_SALE, 0, 0, 4)
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	t.Log("total:", total)
	e, total, fetched, hs = GetBehalfList(commdef.BEHALF_TYPE_TO_SALE, 0, total, 4)
	t.Log("fetched:", fetched, ", houses:", hs)
	if e != nil || total != fetched {
		t.Error("Failed, err: ", e)
		return
	}

	t.Error("TODO: new type, to approve")
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- GetHouseDigestInfo --
//
func Test_GetHouseDigestInfo(t *testing.T) {
	t.Log("Test GetHouseDigestInfo")

	t.Log("<Case> house does not exist")
	if e, _ := GetHouseDigestInfo(10000000000, 5); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> house actual exist")
	e, hd := GetHouseDigestInfo(2, 5)
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	t.Log("house:", fmt.Sprintf("%+v", hd))
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- GetHouseInfo --
//
func Test_GetHouseInfo(t *testing.T) {
	t.Log("Test GetHouseInfo")
	seq := 0

	xitem := []int64{-1, 0, 100000000}
	xdesc := []string{"< 0", "= 0", "does not exist"}
	back_end := []bool{true, false}
	for _, v1 := range back_end {
		for k, v := range xitem {
			seq++
			t.Log(fmt.Sprintf("<Case %d> Invalid Arguments: house(%d) %s, back_end(%t)", seq, v, xdesc[k], v1))
			if e, _ := GetHouseInfo(v, -1, v1); e == nil {
				t.Error("Failed, err: ", e)
				return
			}
		}
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d> Invalid Arguments: house(4) is not published, can not found by front-end", seq))
	if e, _ := GetHouseInfo(4, 5, false); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d> Testing: house(4) is not published, can be found from back-end", seq))
	if e, _ := GetHouseInfo(4, 5, true); e != nil {
		t.Error("Failed, err: ", e)
		return
	}

	// no matter which user is, only public info could be fetched for front-end
	xitem = []int64{-1, 0, 2, 9, 6, 5}
	xdesc = []string{"not login", "is SYSTEM", "is regular user", "is landlord", "is house agency", "is administrator"}
	for k, v := range xitem {
		seq++
		t.Log(fmt.Sprintf("<Case %d> user(%d) %s, front end", seq, v, xdesc[k]))
		e, hd := GetHouseInfo(2, v, false)
		if e != nil {
			t.Error("Failed, err: ", e)
			return
		}
		t.Log(fmt.Sprintf("house: %+v", hd))
		if len(hd.BuildingNo) > 0 || len(hd.HouseNo) > 0 || hd.FloorThis <= hd.FloorTotal ||
			hd.FloorThis > hd.FloorTotal+3 || len(hd.BuyDate) > 0 {
			t.Error("BuildingNo:", hd.BuildingNo, ", HouseNo:", hd.HouseNo, ", FloorThis:", hd.FloorThis, ", buy date:", hd.BuyDate)
			return
		}
	}

	// fetching house info from back-end, permission denied
	xitem = []int64{-1, 0, 100000000, 2, 11}
	xdesc = []string{"not login", "is SYSTEM", "not exist", "is a regular user", "is an agency, but not for this house"}
	for k, v := range xitem {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Permission: user(%d) %s, -- back end --", seq, v, xdesc[k]))
		e, _ := GetHouseInfo(13, v, true)
		if nil == e {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// fetching house info from back-end, permission granted
	xitem = []int64{9, 6, 11, 5}
	xdesc = []string{"landlord", "is house agency", "an agency, although not for this house, but house is public", "is an administrator"}
	for k, v := range xitem {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Testing: user(%d) %s, house published -- back end --", seq, v, xdesc[k]))
		e, hd := GetHouseInfo(2, v, true)
		if nil != e {
			t.Error("Failed, err: ", e)
			return
		}
		t.Log(fmt.Sprintf("house: %+v", hd))
		if 0 == len(hd.BuildingNo) || 0 == len(hd.HouseNo) || hd.FloorThis > hd.FloorTotal || 0 == len(hd.BuyDate) {
			t.Error("BuildingNo:", hd.BuildingNo, ", HouseNo:", hd.HouseNo, ", FloorThis:", hd.FloorThis, ", buy date:", hd.BuyDate)
			return
		}
	}

	xitem = []int64{4, 6, 5}
	xdesc = []string{"landlord", "is house agency", "is an administrator"}
	for k, v := range xitem {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Testing: user(%d) %s, house not published  -- back end --", seq, v, xdesc[k]))
		e, hd := GetHouseInfo(4, v, true)
		if nil != e {
			t.Error("Failed, err: ", e)
			return
		}
		t.Log(fmt.Sprintf("house: %+v", hd))
		if 0 == len(hd.BuildingNo) || 0 == len(hd.HouseNo) || hd.FloorThis > hd.FloorTotal || 0 == len(hd.BuyDate) {
			t.Error("BuildingNo:", hd.BuildingNo, ", HouseNo:", hd.HouseNo, ", FloorThis:", hd.FloorThis, ", buy date:", hd.BuyDate)
			return
		}
	}

	t.Error("TODO: check new info, certification info")

	return
	/*
		seq++
		t.Log(fmt.Sprintf("<Case %d>", seq), "user not login, only public info could be retrieved")
		e, hd := GetHouseInfo(2, -1)
		if e != nil {
			t.Error("Failed, err: ", e)
			return
		}
		t.Log("house:", fmt.Sprintf("%+v", hd))
		if len(hd.BuildingNo) > 0 || len(hd.HouseNo) > 0 || hd.FloorThis <= hd.FloorTotal || hd.FloorThis > hd.FloorTotal+3 {
			t.Error("BuildingNo:", hd.BuildingNo, ", HouseNo:", hd.HouseNo, ", FloorThis:", hd.FloorThis)
			return
		}

		seq++
		t.Log(fmt.Sprintf("<Case %d>", seq), "login user do not have right to access the private info")
		e, hd = GetHouseInfo(2, 2)
		if e != nil {
			t.Error("Failed, err: ", e)
			return
		}
		t.Log("house:", fmt.Sprintf("%+v", hd))
		if len(hd.BuildingNo) > 0 || len(hd.HouseNo) > 0 || hd.FloorThis <= hd.FloorTotal || hd.FloorThis > hd.FloorTotal+3 {
			t.Error("BuildingNo:", hd.BuildingNo, ", HouseNo:", hd.HouseNo, ", FloorThis:", hd.FloorThis)
			return
		}

		seq++
		t.Log(fmt.Sprintf("<Case %d>", seq), "landlord fetch the private info")
		e, hd = GetHouseInfo(2, 9)
		if e != nil {
			t.Error("Failed, err: ", e)
			return
		}
		t.Log("house:", fmt.Sprintf("%+v", hd))
		if 0 == len(hd.BuildingNo) || 0 == len(hd.HouseNo) || hd.FloorThis >= hd.FloorTotal {
			t.Error("BuildingNo:", hd.BuildingNo, ", HouseNo:", hd.HouseNo, ", FloorThis:", hd.FloorThis)
			return
		}

		seq++
		t.Log(fmt.Sprintf("<Case %d>", seq), "House agency fetch the private info")
		e, hd = GetHouseInfo(4, 6)
		if e != nil {
			t.Error("Failed, err: ", e)
			return
		}
		t.Log("house:", fmt.Sprintf("%+v", hd))
		if 0 == len(hd.BuildingNo) || 0 == len(hd.HouseNo) || hd.FloorThis >= hd.FloorTotal {
			t.Error("BuildingNo:", hd.BuildingNo, ", HouseNo:", hd.HouseNo, ", FloorThis:", hd.FloorThis)
			return
		}

		seq++
		t.Log(fmt.Sprintf("<Case %d>", seq), "administrator fetch the private info")
		e, hd = GetHouseInfo(2, 5)
		if e != nil {
			t.Error("Failed, err: ", e)
			return
		}
		t.Log("house:", fmt.Sprintf("%+v", hd))
		if 0 == len(hd.BuildingNo) || 0 == len(hd.HouseNo) || hd.FloorThis >= hd.FloorTotal {
			t.Error("BuildingNo:", hd.BuildingNo, ", HouseNo:", hd.HouseNo, ", FloorThis:", hd.FloorThis)
			return
		}

		seq++
		t.Log(fmt.Sprintf("<Case %d>", seq), "house is opened for other agency fetching the private info")
		e, hd = GetHouseInfo(2, 11)
		if e != nil {
			t.Error("Failed, err: ", e)
			return
		}
		t.Log("house:", fmt.Sprintf("%+v", hd))
		if 0 == len(hd.BuildingNo) || 0 == len(hd.HouseNo) || hd.FloorThis >= hd.FloorTotal {
			t.Error("BuildingNo:", hd.BuildingNo, ", HouseNo:", hd.HouseNo, ", FloorThis:", hd.FloorThis)
			return
		}

		seq++
		t.Log(fmt.Sprintf("<Case %d>", seq), "house is not open, other agency can not fetch the private info")
		e, hd = GetHouseInfo(4, 11)
		if e != nil {
			t.Error("Failed, err: ", e)
			return
		}
		t.Log("house:", fmt.Sprintf("%+v", hd))
		if len(hd.BuildingNo) > 0 || len(hd.HouseNo) > 0 || hd.FloorThis <= hd.FloorTotal || hd.FloorThis > hd.FloorTotal+3 {
			t.Error("BuildingNo:", hd.BuildingNo, ", HouseNo:", hd.HouseNo, ", FloorThis:", hd.FloorThis)
			return
		}
	*/
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- ModifyHouse --
//
func Test_ModifyHouseInfo(t *testing.T) {
	t.Log("Test ModifyHouse")
	seq := 0

	xids := []int64{-1, 0 /*, 100000000*/}
	xdesc := []string{"< 0", "= 0", "does not exist"}
	for k, v := range xids {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Invalid Arguments: property (%d) %s", seq, v, xdesc[k]))
		hif := commdef.HouseInfo{Id: 2, Property: v}
		if e := ModifyHouse(&hif, -1); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	xitem := []string{"", "-1", "0"}          //, "177A"}
	xdesc = []string{"not set", "< 0", "= 0"} //, "177A"}
	for k, v := range xitem {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Invalid Arguments: building no (%s) %s", seq, v, xdesc[k]))
		hif := commdef.HouseInfo{Id: 2, Property: 2, BuildingNo: v}
		if e := ModifyHouse(&hif, -1); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	xdesc = []string{"< 0", "= 0"}
	for k, v := range xids {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Invalid Arguments: Total floor (%d) %s", seq, v, xdesc[k]))
		hif := commdef.HouseInfo{Id: 2, Property: 2, BuildingNo: "175", FloorTotal: int(v)}
		if e := ModifyHouse(&hif, -1); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	for k, v := range xids {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Invalid Arguments: House floor (%d) %s", seq, v, xdesc[k]))
		hif := commdef.HouseInfo{Id: 2, Property: 2, BuildingNo: "175", FloorTotal: 35, FloorThis: int(v)}
		if e := ModifyHouse(&hif, -1); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Arguments: FloorThis > FloorTotal")
	hif := commdef.HouseInfo{Id: 2, Property: 2, BuildingNo: "175", FloorTotal: 35, FloorThis: 36}
	if e := ModifyHouse(&hif, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Arguments: Bedrooms < 0")
	hif = commdef.HouseInfo{Id: 2, Property: 2, BuildingNo: "175", FloorTotal: 35, FloorThis: 15, Bedrooms: -1}
	if e := ModifyHouse(&hif, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Arguments: Livingrooms < 0")
	hif = commdef.HouseInfo{Id: 2, Property: 2, BuildingNo: "175", FloorTotal: 35, FloorThis: 15, Bedrooms: 0, Livingrooms: -1}
	if e := ModifyHouse(&hif, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Arguments: Bathrooms < 0")
	hif = commdef.HouseInfo{Id: 2, Property: 2, BuildingNo: "175", FloorTotal: 35, FloorThis: 15, Bedrooms: 0,
		Livingrooms: 0, Bathrooms: -1}
	if e := ModifyHouse(&hif, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Arguments: Bathrooms, livingrooms, bedrooms are all 0")
	hif = commdef.HouseInfo{Id: 2, Property: 2, BuildingNo: "175", FloorTotal: 35, FloorThis: 15, Bedrooms: 0,
		Livingrooms: 0, Bathrooms: 0}
	if e := ModifyHouse(&hif, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Arguments: Acreage <= 100")
	hif = commdef.HouseInfo{Id: 2, Property: 2, BuildingNo: "175", FloorTotal: 35, FloorThis: 15, Bedrooms: 2,
		Livingrooms: 1, Bathrooms: 1, Acreage: 100}
	if e := ModifyHouse(&hif, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	xids = []int64{commdef.DECORATION_Workblank - 1, commdef.DECORATION_Luxury + 1}
	xdesc = []string{"< commdef.DECORATION_Workblank", "> commdef.DECORATION_Luxury"}
	for k, v := range xids {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Invalid Arguments: Decoration (%d) %s", seq, v, xdesc[k]))
		hif = commdef.HouseInfo{Id: 2, Property: 2, BuildingNo: "175", FloorTotal: 35, FloorThis: 15, Bedrooms: 2,
			Livingrooms: 1, Bathrooms: 1, Acreage: 10300, Decoration: int(v)}
		if e := ModifyHouse(&hif, -1); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	xitem = []string{"", "abcdefg", "20160305", "2016.03.05", "2016", "201603", "2016/03/06",
		"2016-03-07 19:35:46", "2016-3-7", "2017-13-23", "2017-03-33"}
	xdesc = []string{"not set", "is not date", "no '-'", "'.' -> '-'", "only year", "day not set", "'/' -> '-'",
		"extra text 19:35:46", "month out of range", "month out of range", "day out of range"}
	for k, v := range xitem {
		seq++
		t.Log(fmt.Sprintf("<Case %d> invalid arguments: buying date(%s) %s", seq, v, xdesc[k]))
		hif = commdef.HouseInfo{Property: 2, BuildingNo: "175", FloorTotal: 35, FloorThis: 15, Bedrooms: 2,
			Livingrooms: 1, Bathrooms: 1, Acreage: 12300, BuyDate: v}
		if e, _ := CommitHouseByOwner(&hif, 9, 0); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Arguments: house does not exist")
	hif = commdef.HouseInfo{Id: 100000000, Property: 2, BuildingNo: "175", FloorTotal: 35, FloorThis: 15, Bedrooms: 2,
		Livingrooms: 1, Bathrooms: 1, Acreage: 10000, BuyDate: "2017-06-07"}
	if e := ModifyHouse(&hif, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Arguments: property does not exist")
	hif = commdef.HouseInfo{Id: 2, Property: 200000000, BuildingNo: "175", FloorTotal: 35, FloorThis: 15, Bedrooms: 2,
		Livingrooms: 1, Bathrooms: 1, Acreage: 10000, BuyDate: "2017-06-07"}
	if e := ModifyHouse(&hif, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Arguments: house duplicated")
	hif = commdef.HouseInfo{Id: 2, Property: 1, BuildingNo: "177", FloorTotal: 35, FloorThis: 15, HouseNo: "1505",
		Bedrooms: 2, Livingrooms: 1, Bathrooms: 1, Acreage: 10000, BuyDate: "2017-06-07"}
	if e := ModifyHouse(&hif, 9); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	xids = []int64{-1, 0, 2, 11}
	xdesc = []string{"not login", "is SYSTEM", "is a regular user", "is an agency, but not behalf this house"}
	for k, v := range xids {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Permission: user(%d) %s", seq, v, xdesc[k]))
		hif = commdef.HouseInfo{Id: 2, Property: 2, BuildingNo: "175", FloorTotal: 35, FloorThis: 15, HouseNo: "1505",
			Bedrooms: 2, Livingrooms: 1, Bathrooms: 1, Acreage: 10000, BuyDate: "2017-06-07"}
		if e := ModifyHouse(&hif, v); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	lgusrs := []int64{9, 6, 4}
	usrnames := []string{"Landlord", "Agency", "Administrator"}
	steps := []int{1, 1, -2}
	for k, v := range lgusrs {
		seq++
		t.Log(fmt.Sprintf("<Case %d> %s (%d)", seq, usrnames[k], v), "modify the house info")
		e, hd := GetHouseInfo(2, v, true) // get current info
		if e != nil {
			t.Error("Failed, err: ", e)
			return
		}
		t.Log("house:", fmt.Sprintf("%+v", hd))
		// nBN := int64(0)
		// if len(hd.BuildingNo) > 0 {
		// 	nBN, _ = strconv.ParseInt(hd.BuildingNo, 10, 64)
		// }
		if /*0 == nBN ||*/ 0 == len(hd.HouseNo) || hd.FloorThis >= hd.FloorTotal {
			t.Error("BuildingNo:", hd.BuildingNo, ", HouseNo:", hd.HouseNo, ", FloorThis:", hd.FloorThis)
			return
		}

		if 0 == len(hd.BuyDate) {
			hd.BuyDate = "2017-06-07"
		}

		bn := hd.BuildingNo
		nBN, _ := strconv.ParseInt(hd.BuildingNo, 10, 64)
		hd.BuildingNo = fmt.Sprintf("%d", nBN+int64(steps[k]))
		if e := ModifyHouse(&hd, v); e != nil { // update house info
			t.Error("Failed, err: ", e)
			return
		}
		e, hd = GetHouseInfo(2, v, true) // get again
		if e != nil {
			t.Error("Failed, err: ", e)
			return
		}
		// t.Log("house:", fmt.Sprintf("%+v", hd))
		t.Log("building no:(before)", bn, ", (after)", hd.BuildingNo)
		nBN1, _ := strconv.ParseInt(bn, 10, 64)
		nBN2, _ := strconv.ParseInt(hd.BuildingNo, 10, 64)
		if nBN2 != nBN1+int64(steps[k]) {
			t.Error("error building no")
			return
		}
	}
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- SetHouseAgency --
//
func Test_SetHouseAgency(t *testing.T) {
	t.Log("Test SetHouseAgency")
	seq := 0

	// Invalid agency id to assign
	aids := []int64{-1, 0, 100000000, 2}
	agency_desc := []string{"< 0", "= 0", "does not exist", "is not an agency"}
	for k, v := range aids {
		seq++
		t.Log(fmt.Sprintf("<Case %d> invalid arguments: agency (%d) %s", seq, v, agency_desc[k]))
		if e := SetHouseAgency(-1, v, -1); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// Invalid house id
	hids := []int64{-1, 0, 100000000}
	house_desc := []string{"< 0", "= 0", "does not exist"}
	for k, v := range hids {
		seq++
		t.Log(fmt.Sprintf("<Case %d> invalid arguments: house (%d) %s", seq, v, house_desc[k]))
		if e := SetHouseAgency(v, 11, -1); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// permissions
	uids := []int64{-1, 2}
	user_desc := []string{"not login", "is a regualr user"}
	for k, v := range uids {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Permission denied: user (%d) %s", seq, v, user_desc[k]))
		if e := SetHouseAgency(2, 11, v); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// process
	uids1 := []int64{9, 6, 5}
	user_desc1 := []string{"Landlord", "House agency", "Administrator"}
	for k, v := range uids1 {
		seq++
		t.Log(fmt.Sprintf("<Case %d> %s(%d) set house agency", seq, user_desc1[k], v))
		if e := SetHouseAgency(2, 11, v); e != nil {
			t.Error("Failed, err: ", e)
			return
		}
		SetHouseAgency(2, 6, uids1[0]) // restore before agency
	}
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- SetHouseCoverImage --
//
func Test_SetHouseCoverImage(t *testing.T) {
	t.Log("Test SetHouseCoverImage")
	seq := 0

	// Invalid image id
	iids := []int64{-1, 0, 100000000}
	img_desc := []string{"< 0", "= 0", "does not exist"}
	for k, v := range iids {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Invalid Argument: image (%d) %s", seq, v, img_desc[k]))
		if e := SetHouseCoverImage(2, v, -1); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// Invalid house id
	hids := []int64{-1, 0, 100000000}
	hid_desc := []string{"< 0", "= 0", "does not exist"}
	for k, v := range hids {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Invalid Argument: house (%d) %s", seq, v, hid_desc[k]))
		if e := SetHouseCoverImage(v, 29, -1); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// Permission fail
	uids := []int64{-1, 0, 2, 11}
	user_desc := []string{"not login", "is SYSTEM", "is a regular user", "is an agency, but not for this house"}
	for k, v := range uids {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Permission: user (%d) %s", seq, v, user_desc[k]))
		if e := SetHouseCoverImage(2, 29, v); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// set house cover image
	users := []int64{9, 6, 5}
	user_desc = []string{"landlord", "agency", "administrator"}
	for k, v := range users {
		seq++
		t.Log(fmt.Sprintf("<Case %d> %s (%d) set house cover image", seq, user_desc[k], v))
		if e := SetHouseCoverImage(2, 29, v); e != nil {
			t.Error("Failed, err: ", e)
			return
		}
		SetHouseCoverImage(2, 31, v) // restore
	}
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- CertHouse --
//
func Test_CertHouse(t *testing.T) {
	t.Log("Test CertHouse")
	seq := 0

	// comment
	t.Log(fmt.Sprintf("<Case %d> Invalid argument: no comment", seq))
	if e := CertHouse(2, -1, false, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	// House
	hids := []int64{-1, 0, 100000000}
	hdesc := []string{"< 0", "= 0", "does not exist"}
	for k, v := range hids {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Invalid argument: house (%d) %s", seq, v, hdesc[k]))
		if e := CertHouse(v, -1, false, "test"); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// house has been published
	seq++
	t.Log(fmt.Sprintf("<Case %d> Invalid argument: already published", seq))
	if e := CertHouse(2, -1, true, "test pass"); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	// user permission
	uids := []int64{-1, 0, 100000000, 2, 9}
	udesc := []string{"not login", "is SYSTEM", "does not exist", "is a regular user", "is landlord"}
	for k, v := range uids {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Permission checking: user (%d) %s", seq, v, udesc[k]))
		if e := CertHouse(2, v, false, "test"); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// processing
	uids = []int64{6, 5}
	udesc = []string{"House Agency", "Administrator"}
	for k, v := range uids {
		seq++
		t.Log(fmt.Sprintf("<Case %d> %s (%d) revoke the certification", seq, udesc[k], v))
		if e := CertHouse(2, v, false, "cancel the certification"); e != nil {
			t.Error("Failed, err: ", e)
			return
		}

		seq++
		t.Log(fmt.Sprintf("<Case %d> %s (%d) pass certification", seq, udesc[k], v))
		if e := CertHouse(2, v, true, "certification passed"); e != nil {
			t.Error("Failed, err: ", e)
			return
		}
	}
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- RecommendHouse --
//
func Test_RecommendHouse(t *testing.T) {
	t.Log("Test RecommendHouse")
	seq := 0

	// House
	xids := []int64{-1, 0, 100000000, 4, 2}
	xdesc := []string{"< 0", "= 0", "does not exist", "not been published", "already recommended"}
	for k, v := range xids {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Invalid argument: House (%d) %s", seq, v, xdesc[k]))
		if e := RecommendHouse(v, 6, 1); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// User
	xids = []int64{-1, 0, 100000000}
	xdesc = []string{"not login", "is SYSTEM", "does not exist"}
	for k, v := range xids {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Invalid argument: User (%d) %s", seq, v, xdesc[k]))
		if e := RecommendHouse(2, v, 1); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// User permission
	xids = []int64{2, 9, 11}
	xdesc = []string{"is a regular user", "is landlord", "is an agency, but not for this house"}
	for k, v := range xids {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Permission: User (%d) %s", seq, v, xdesc[k]))
		if e := RecommendHouse(2, v, 1); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	xids = []int64{0, 3}
	for _, v := range xids {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Invalid argument: action (%d)", seq, v))
		if e := RecommendHouse(2, -2, int(v)); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// regular operation
	xids = []int64{2, 1}
	xdesc = []string{"unrecommend house", "recommend house"}
	for k, v := range xids {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Operation: %s", seq, xdesc[k]))
		if e := RecommendHouse(2, 6, int(v)); e != nil {
			t.Error("Failed, err: ", e)
			return
		}
	}
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- CommitHouseByOwner --
//
func Test_CommitHouseByOwner(t *testing.T) {
	t.Log("Test CommitHouseByOwner")
	seq := 0

	xids := []int64{-1, 0}
	xdesc := []string{"< 0", "= 0"}
	for k, v := range xids {
		seq++
		t.Log(fmt.Sprintf("<Case %d> invalid arguments: property(%d) %s", seq, v, xdesc[k]))
		hif := commdef.HouseInfo{Property: v}
		if e, _ := CommitHouseByOwner(&hif, 9, 0); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	xitem := []string{"", "-1", "0"}
	xdesc = []string{"not set", "< 0", "= 0"}
	for k, v := range xitem {
		seq++
		t.Log(fmt.Sprintf("<Case %d> invalid arguments: building_no(%s) %s", seq, v, xdesc[k]))
		hif := commdef.HouseInfo{Property: 2, BuildingNo: v}
		if e, _ := CommitHouseByOwner(&hif, 9, 0); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	xids = []int64{-1, 0}
	xdesc = []string{"< 0", "= 0"}
	for k, v := range xids {
		seq++
		t.Log(fmt.Sprintf("<Case %d> invalid arguments: FloorTotal(%d) %s", seq, v, xdesc[k]))
		hif := commdef.HouseInfo{Property: 2, BuildingNo: "175", FloorTotal: int(v)}
		if e, _ := CommitHouseByOwner(&hif, 9, 0); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d> Invalid arguments: FloorThis < 0", seq))
	hif := commdef.HouseInfo{Property: 2, BuildingNo: "175", FloorTotal: 35, FloorThis: -2}
	if e, _ := CommitHouseByOwner(&hif, 9, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d> Invalid arguments: FloorThis > FloorTotal", seq))
	hif = commdef.HouseInfo{Property: 2, BuildingNo: "175", FloorTotal: 35, FloorThis: 36}
	if e, _ := CommitHouseByOwner(&hif, 9, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d> Invalid arguments: Bedrooms < 0", seq))
	hif = commdef.HouseInfo{Property: 2, BuildingNo: "175", FloorTotal: 35, FloorThis: 15, Bedrooms: -1}
	if e, _ := CommitHouseByOwner(&hif, 9, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d> Invalid arguments: Livingrooms < 0", seq))
	hif = commdef.HouseInfo{Property: 2, BuildingNo: "175", FloorTotal: 35, FloorThis: 15, Bedrooms: 0, Livingrooms: -1}
	if e, _ := CommitHouseByOwner(&hif, 9, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d> Invalid arguments: Bathrooms < 0", seq))
	hif = commdef.HouseInfo{Property: 2, BuildingNo: "175", FloorTotal: 35, FloorThis: 15, Bedrooms: 0,
		Livingrooms: 0, Bathrooms: -1}
	if e, _ := CommitHouseByOwner(&hif, 9, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d> Invalid arguments: Bathrooms, livingrooms, bedrooms are all 0", seq))
	hif = commdef.HouseInfo{Property: 2, BuildingNo: "175", FloorTotal: 35, FloorThis: 15, Bedrooms: 0,
		Livingrooms: 0, Bathrooms: 0}
	if e, _ := CommitHouseByOwner(&hif, 9, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d> Invalid arguments: Acreage <= 100", seq))
	hif = commdef.HouseInfo{Property: 2, BuildingNo: "175", FloorTotal: 35, FloorThis: 15, Bedrooms: 2,
		Livingrooms: 1, Bathrooms: 1, Acreage: 100}
	if e, _ := CommitHouseByOwner(&hif, 9, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	xids = []int64{commdef.DECORATION_Workblank - 1, commdef.DECORATION_Luxury + 1}
	xdesc = []string{"< commdef.DECORATION_Workblank", "> commdef.DECORATION_Luxury"}
	for k, v := range xids {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Invalid Arguments: Decoration (%d) %s", seq, v, xdesc[k]))
		hif = commdef.HouseInfo{Id: 2, Property: 2, BuildingNo: "175", FloorTotal: 35, FloorThis: 15, Bedrooms: 2,
			Livingrooms: 1, Bathrooms: 1, Acreage: 10300, Decoration: int(v)}
		if e := ModifyHouse(&hif, -1); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	xitem = []string{"", "abcdefg", "20160305", "2016.03.05", "2016", "201603", "2016/03/06",
		"2016-03-07 19:35:46", "2016-3-7", "2017-13-23", "2017-03-33"}
	xdesc = []string{"not set", "is not date", "no '-'", "'.' -> '-'", "only year", "day not set", "'/' -> '-'",
		"extra text 19:35:46", "month out of range", "month out of range", "day out of range"}
	for k, v := range xitem {
		seq++
		t.Log(fmt.Sprintf("<Case %d> invalid arguments: buying date(%s) %s", seq, v, xdesc[k]))
		hif = commdef.HouseInfo{Property: 2, BuildingNo: "175", FloorTotal: 35, FloorThis: 15, Bedrooms: 2,
			Livingrooms: 1, Bathrooms: 1, Acreage: 12300, BuyDate: v}
		if e, _ := CommitHouseByOwner(&hif, 9, 0); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d> Invalid arguments: property does not exist", seq))
	hif = commdef.HouseInfo{Property: 200000000, BuildingNo: "175", FloorTotal: 35, FloorThis: 15, Bedrooms: 2,
		Livingrooms: 1, Bathrooms: 1, Acreage: 12300, BuyDate: "2017-06-07"}
	if e, _ := CommitHouseByOwner(&hif, 9, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	xids = []int64{-1, 100000000}
	xdesc = []string{"not login", "does not exist"}
	for k, v := range xids {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Permission: house owner (%d) %s", seq, v, xdesc[k]))
		hif = commdef.HouseInfo{Property: 2, BuildingNo: "175", FloorTotal: 35, FloorThis: 15, Bedrooms: 2,
			Livingrooms: 1, Bathrooms: 1, Acreage: 12300, BuyDate: "2017-06-07"}
		if e, _ := CommitHouseByOwner(&hif, v, -1); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	for k, v := range xids {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Permission: house agency (%d) %s", seq, v, xdesc[k]))
		hif = commdef.HouseInfo{Property: 2, BuildingNo: "175", FloorTotal: 35, FloorThis: 15, Bedrooms: 2,
			Livingrooms: 1, Bathrooms: 1, Acreage: 12300, BuyDate: "2017-06-07"}
		if e, _ := CommitHouseByOwner(&hif, 9, v); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d> Invalid arguments: house duplicated", seq))
	hif = commdef.HouseInfo{Property: 2, BuildingNo: "56", FloorTotal: 45, FloorThis: 16, HouseNo: "1605", Bedrooms: 2,
		Livingrooms: 1, Bathrooms: 1, Acreage: 12300, BuyDate: "2017-06-07"}
	if e, _ := CommitHouseByOwner(&hif, 9, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d> commit new house", seq))
	hif = commdef.HouseInfo{Property: 7, BuildingNo: "156", FloorTotal: 45, FloorThis: 26, HouseNo: "2605", Bedrooms: 2,
		Livingrooms: 1, Bathrooms: 1, Acreage: 12400, BuyDate: "2017-06-07"}
	e, id := CommitHouseByOwner(&hif, 9, 6)
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	t.Log("new house:", id)
	if e = delHouse(id, 4); nil != e {
		t.Error("Failed, err: ", e)
		return
	}
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- SetHousePrice --
//
func Test_SetHousePrice(t *testing.T) {
	seq := 0

	xids := []int64{-1, 0, 100000000}
	xdesc := []string{"< 0", "= 0", "does not exist"}
	for k, v := range xids {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Invalid arguments: house(%d) %s", seq, v, xdesc[k]))
		if e, _ := SetHousePrice(v, -1, 0, 0, 0, 0, false); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	xid_1 := []int64{-1, 0, 0, 100}
	xid_2 := []int64{0, -1, 0, 200}
	xdesc = []string{"rental(%d, %d), tag < 0", "rental(%d, %d), bottom < 0", "rental(%d, %d), not set", "rental(%d, %d), tag price < bottom price"}
	for k, v := range xid_1 {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Invalid arguments: %s", seq, fmt.Sprintf(xdesc[k], v, xid_2[k])))
		if e, _ := SetHousePrice(3, -1, v, xid_2[k], -1, -1, false); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	xid_1 = []int64{-1, 0, 0, 10000}
	xid_2 = []int64{0, -1, 0, 20000}
	xdesc = []string{"selling price(%d, %d), tag < 0", "selling price(%d, %d), bottom < 0", "selling price(%d, %d), not set", "selling price(%d, %d), tag price < bottom price"}
	for k, v := range xid_1 {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Invalid arguments: %s", seq, fmt.Sprintf(xdesc[k], v, xid_2[k])))
		if e, _ := SetHousePrice(12, -1, 1, 1, v, xid_2[k], false); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d> Invalid arguments: house can not accept any price", seq))
	if e, _ := SetHousePrice(2, -1, 1, 1, 1, 1, false); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d> Invalid arguments: duplicate price", seq))
	if e, _ := SetHousePrice(11, -1, 1000, 800, 500000, 470000, true); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	xids = []int64{-1, 0, 100000000, 2, 11}
	xdesc = []string{"not login", "is SYSTEM", "does not exist", "is a regular user", "is an agency, but not for this house"}
	for k, v := range xids {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Permission: User(%d) %s", seq, v, xdesc[k]))
		if e, _ := SetHousePrice(13, v, 1, 1, 1, 1, false); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// set rental
	xids = []int64{10, 6, 5}
	xdesc = []string{"Landlord", "house agency", "administrator"}
	for k, v := range xids {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Testing: %s(%d) set rental price", seq, xdesc[k], v))
		if e, _ := SetHousePrice(13, v, 1000+int64(k), 800+int64(k), 500000+int64(k), 48000+int64(k), false); e != nil {
			t.Error("Failed, err: ", e)
			return
		}
	}
	// remove all records just added
	delHousePrices(13)

	// set selling price
	xids = []int64{9, 5}
	xdesc = []string{"Landlord", "administrator"}
	for k, v := range xids {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Testing: %s(%d) set selling price", seq, xdesc[k], v))
		if e, _ := SetHousePrice(12, v, 1000+int64(k), 800+int64(k), 500000+int64(k), 48000+int64(k), false); e != nil {
			t.Error("Failed, err: ", e)
			return
		}
	}
	// remove all records just added
	delHousePrices(12)

	// set both rental and selling price
	xids = []int64{2 /*1,*/, 5}
	xdesc = []string{"Landlord" /*"house agency",*/, "administrator"}
	for k, v := range xids {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Testing: %s(%d) set both rental and selling price", seq, xdesc[k], v))
		if e, _ := SetHousePrice(6, v, 1000+int64(k), 800+int64(k), 500000+int64(k), 48000+int64(k), false); e != nil {
			t.Error("Failed, err: ", e)
			return
		}
	}
	// remove all records just added
	delHousePrices(6)
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- GetHousePrice --
//
func Test_GetHousePrice(t *testing.T) {
	seq := 0

	seq++
	begin := -1
	t.Log(fmt.Sprintf("<Case %d> Invalid arguments: begin(%d) < 0", seq, begin))
	if e, _, _ := GetHousePrice(4, 5, begin, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	count := -1
	t.Log(fmt.Sprintf("<Case %d> Invalid arguments: count(%d) < 0", seq, count))
	if e, _, _ := GetHousePrice(4, 5, 0, count); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	xids := []int64{-1, 0, 100000000}
	xdesc := []string{"< 0", "= 0", "does not exist"}
	for k, v := range xids {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Invalid arguments: house(%d) %s", seq, v, xdesc[k]))
		if e, _, _ := GetHousePrice(v, -1, 0, 100); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	xids = []int64{-1, 0, 100000000, 2, 11}
	xdesc = []string{"not login", "is SYSTEM", "does not exist", "is a regular user", "is an agency, but not for this privacy house"}
	for k, v := range xids {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Permission: user(%d) %s", seq, v, xdesc[k]))
		if e, _, _ := GetHousePrice(4, v, 0, 100); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	xids = []int64{10, 6, 4, 5, 11}
	xdesc = []string{"landlord", "is house agency", "administator", "administrator", "other agency"}
	for k, v := range xids {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Testing: %s(%d) retrieve the price list", seq, xdesc[k], v))
		if e, _, _ := GetHousePrice(13, v, 0, 100); e != nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d> Testing: Landlord(4) retrieve the price list", seq))
	e, total, _ := GetHousePrice(11, 4, 0, 100)
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	if 2 != total {
		t.Error("Failed, total(%d) != 2", total)
		return
	}
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- SetHouseShowTime --
//
func Test_SetHouseShowTime(t *testing.T) {
	// seq := 0

	t.Error("Not Implement")
	// 	seq++
	// 	begin := -1
	// 	t.Log(fmt.Sprintf("<Case %d> Invalid arguments: begin(%d) < 0", seq, begin))
	// 	if e, _, _ := SetHouseShowTime(4, 5, begin, -1); e == nil {
	// 		t.Error("Failed, err: ", e)
	// 		return
	// 	}
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- GetHouseShowTime --
//
func Test_GetHouseShowTime(t *testing.T) {
	// seq := 0

	t.Error("Not Implement")
	// 	seq++
	// 	begin := -1
	// 	t.Log(fmt.Sprintf("<Case %d> Invalid arguments: begin(%d) < 0", seq, begin))
	// 	if e, _, _ := GetHouseShowTime(4, 5, begin, -1); e == nil {
	// 		t.Error("Failed, err: ", e)
	// 		return
	// 	}
}
