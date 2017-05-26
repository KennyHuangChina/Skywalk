package models

import (
	"ApiServer/commdef"
	"fmt"
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
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- GetHouseDigestInfo --
//
func Test_GetHouseDigestInfo(t *testing.T) {
	t.Log("Test GetHouseDigestInfo")

	t.Log("<Case> house does not exist")
	if e, _ := GetHouseDigestInfo(10000000000); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> house actual exist")
	e, hd := GetHouseDigestInfo(2)
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

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Arguments: house < 0")
	if e, _ := GetHouseInfo(-1, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Arguments: house == 0")
	if e, _ := GetHouseInfo(0, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Arguments: house does not exist")
	if e, _ := GetHouseInfo(10000000000, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "user not login, only public info could be retrieved")
	e, hd := GetHouseInfo(2, -1)
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	t.Log("house:", fmt.Sprintf("%+v", hd))
	if hd.BuildingNo > 0 || len(hd.HouseNo) > 0 || hd.FloorThis <= hd.FloorTotal || hd.FloorThis > hd.FloorTotal+3 {
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
	if hd.BuildingNo > 0 || len(hd.HouseNo) > 0 || hd.FloorThis <= hd.FloorTotal || hd.FloorThis > hd.FloorTotal+3 {
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
	if 0 == hd.BuildingNo || 0 == len(hd.HouseNo) || hd.FloorThis >= hd.FloorTotal {
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
	if 0 == hd.BuildingNo || 0 == len(hd.HouseNo) || hd.FloorThis >= hd.FloorTotal {
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
	if 0 == hd.BuildingNo || 0 == len(hd.HouseNo) || hd.FloorThis >= hd.FloorTotal {
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
	if 0 == hd.BuildingNo || 0 == len(hd.HouseNo) || hd.FloorThis >= hd.FloorTotal {
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
	if hd.BuildingNo > 0 || len(hd.HouseNo) > 0 || hd.FloorThis <= hd.FloorTotal || hd.FloorThis > hd.FloorTotal+3 {
		t.Error("BuildingNo:", hd.BuildingNo, ", HouseNo:", hd.HouseNo, ", FloorThis:", hd.FloorThis)
		return
	}
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- ModifyHouse --
//
func Test_ModifyHouseInfo(t *testing.T) {
	t.Log("Test ModifyHouse")
	seq := 0

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Arguments: property == 0")
	hif := commdef.HouseInfo{Id: 2, Property: 0}
	if e := ModifyHouse(&hif, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Arguments: property < 0")
	hif = commdef.HouseInfo{Id: 2, Property: -1}
	if e := ModifyHouse(&hif, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Arguments: building_no = 0")
	hif = commdef.HouseInfo{Id: 2, Property: 2, BuildingNo: 0}
	if e := ModifyHouse(&hif, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Arguments: building_no < 0")
	hif = commdef.HouseInfo{Id: 2, Property: 2, BuildingNo: -1}
	if e := ModifyHouse(&hif, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Arguments: FloorTotal = 0")
	hif = commdef.HouseInfo{Id: 2, Property: 2, BuildingNo: 175, FloorTotal: 0}
	if e := ModifyHouse(&hif, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Arguments: FloorTotal < 0")
	hif = commdef.HouseInfo{Id: 2, Property: 2, BuildingNo: 175, FloorTotal: -1}
	if e := ModifyHouse(&hif, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Arguments: FloorThis = 0")
	hif = commdef.HouseInfo{Id: 2, Property: 2, BuildingNo: 175, FloorTotal: 35, FloorThis: 0}
	if e := ModifyHouse(&hif, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Arguments: FloorThis < 0")
	hif = commdef.HouseInfo{Id: 2, Property: 2, BuildingNo: 175, FloorTotal: 35, FloorThis: -1}
	if e := ModifyHouse(&hif, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Arguments: FloorThis > FloorTotal")
	hif = commdef.HouseInfo{Id: 2, Property: 2, BuildingNo: 175, FloorTotal: 35, FloorThis: 36}
	if e := ModifyHouse(&hif, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Arguments: Bedrooms < 0")
	hif = commdef.HouseInfo{Id: 2, Property: 2, BuildingNo: 175, FloorTotal: 35, FloorThis: 15, Bedrooms: -1}
	if e := ModifyHouse(&hif, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Arguments: Livingrooms < 0")
	hif = commdef.HouseInfo{Id: 2, Property: 2, BuildingNo: 175, FloorTotal: 35, FloorThis: 15, Bedrooms: 0, Livingrooms: -1}
	if e := ModifyHouse(&hif, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Arguments: Bathrooms < 0")
	hif = commdef.HouseInfo{Id: 2, Property: 2, BuildingNo: 175, FloorTotal: 35, FloorThis: 15, Bedrooms: 0,
		Livingrooms: 0, Bathrooms: -1}
	if e := ModifyHouse(&hif, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Arguments: Bathrooms, livingrooms, bedrooms are all 0")
	hif = commdef.HouseInfo{Id: 2, Property: 2, BuildingNo: 175, FloorTotal: 35, FloorThis: 15, Bedrooms: 0,
		Livingrooms: 0, Bathrooms: 0}
	if e := ModifyHouse(&hif, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Arguments: Acreage <= 100")
	hif = commdef.HouseInfo{Id: 2, Property: 2, BuildingNo: 175, FloorTotal: 35, FloorThis: 15, Bedrooms: 2,
		Livingrooms: 1, Bathrooms: 1, Acreage: 100}
	if e := ModifyHouse(&hif, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Arguments: house does not exist")
	hif = commdef.HouseInfo{Id: 100000000, Property: 2, BuildingNo: 175, FloorTotal: 35, FloorThis: 15, Bedrooms: 2,
		Livingrooms: 1, Bathrooms: 1, Acreage: 10000}
	if e := ModifyHouse(&hif, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Arguments: property does not exist")
	hif = commdef.HouseInfo{Id: 2, Property: 200000000, BuildingNo: 175, FloorTotal: 35, FloorThis: 15, Bedrooms: 2,
		Livingrooms: 1, Bathrooms: 1, Acreage: 10000}
	if e := ModifyHouse(&hif, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Arguments: house duplicated")
	hif = commdef.HouseInfo{Id: 2, Property: 1, BuildingNo: 177, FloorTotal: 35, FloorThis: 15, HouseNo: "1505",
		Bedrooms: 2, Livingrooms: 1, Bathrooms: 1, Acreage: 10000}
	if e := ModifyHouse(&hif, 9); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user not login")
	hif = commdef.HouseInfo{Id: 2, Property: 2, BuildingNo: 175, FloorTotal: 35, FloorThis: 15, Bedrooms: 2,
		Livingrooms: 1, Bathrooms: 1, Acreage: 10000}
	if e := ModifyHouse(&hif, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: login user is a regular user, have no right to modify house")
	hif = commdef.HouseInfo{Id: 2, Property: 2, BuildingNo: 175, FloorTotal: 35, FloorThis: 15, Bedrooms: 2,
		Livingrooms: 1, Bathrooms: 1, Acreage: 10000}
	if e := ModifyHouse(&hif, 2); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: login user is an agency, but not behalf this house, have no right to modify house")
	// hif = commdef.HouseInfo{Id: 2, Property: 2, BuildingNo: 175, FloorTotal: 35, FloorThis: 15, Bedrooms: 2,
	// 	Livingrooms: 1, Bathrooms: 1, Acreage: 10000}
	if e := ModifyHouse(&hif, 11); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	lgusrs := []int64{9, 6, 4}
	usrnames := []string{"Landlord", "Agency", "Administrator"}
	steps := []int{1, 1, -2}
	for k, v := range lgusrs {
		seq++
		t.Log(fmt.Sprintf("<Case %d> %s (%d)", seq, usrnames[k], v), "modify the house info")
		e, hd := GetHouseInfo(2, v) // get current info
		if e != nil {
			t.Error("Failed, err: ", e)
			return
		}
		t.Log("house:", fmt.Sprintf("%+v", hd))
		if 0 == hd.BuildingNo || 0 == len(hd.HouseNo) || hd.FloorThis >= hd.FloorTotal {
			t.Error("BuildingNo:", hd.BuildingNo, ", HouseNo:", hd.HouseNo, ", FloorThis:", hd.FloorThis)
			return
		}
		bn := hd.BuildingNo
		hd.BuildingNo = hd.BuildingNo + steps[k]
		t.Log("house:", fmt.Sprintf("%+v", hd))
		if e := ModifyHouse(&hd, v); e != nil { // update house info
			t.Error("Failed, err: ", e)
			return
		}
		e, hd = GetHouseInfo(2, v) // get again
		if e != nil {
			t.Error("Failed, err: ", e)
			return
		}
		// t.Log("house:", fmt.Sprintf("%+v", hd))
		t.Log("building no:(before)", bn, ", (after)", hd.BuildingNo)
		if hd.BuildingNo != bn+steps[k] {
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

	t.Log("<Case> invalid arguments: house does not exist")
	if e := SetHouseAgency(100000000, -1, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> invalid arguments: agency < 0")
	if e := SetHouseAgency(2, -1, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> invalid arguments: agency does not exist")
	if e := SetHouseAgency(2, 100000000, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	// no owner, no administrator
	t.Log("<Case> permission: regular people have no right to set agency")
	if e := SetHouseAgency(2, 5, 2); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> permission: agency have no right to set agency")
	if e := SetHouseAgency(2, 5, 6); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> permission: landlord have right to set agency")
	if e := SetHouseAgency(2, 5, 9); e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	SetHouseAgency(2, 6, 9) // restore before agency

	t.Log("<Case> permission: administrator have right to set agency")
	if e := SetHouseAgency(2, 5, 4); e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	SetHouseAgency(2, 6, 4) // restore before agency
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- SetHouseCoverImage --
//
func Test_SetHouseCoverImage(t *testing.T) {
	t.Log("Test SetHouseCoverImage")

	t.Log("<Case> invalid arguments: house does not exist")
	if e := SetHouseCoverImage(100000000, -1, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> invalid arguments: picture <= 0")
	if e := SetHouseCoverImage(2, 0, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> invalid arguments: picture does not exist")
	if e := SetHouseCoverImage(2, 100000000, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	// not landload, not it's agency, not administrator
	t.Log("<Case> permission: regular user can not set cover image")
	if e := SetHouseCoverImage(2, 30, 2); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> permission: landload have right to set cover image")
	if e := SetHouseCoverImage(2, 30, 9); e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	SetHouseCoverImage(2, 31, 9) // restore

	t.Log("<Case> permission: agency have right to set cover image")
	if e := SetHouseCoverImage(2, 30, 6); e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	SetHouseCoverImage(2, 31, 6) // restore

	t.Log("<Case> permission: administrator have right to set cover image")
	if e := SetHouseCoverImage(2, 30, 4); e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	SetHouseCoverImage(2, 31, 4) // restore
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- CertHouse --
//
func Test_CertHouse(t *testing.T) {
	t.Log("Test CertHouse")

	t.Log("<Case> invalid arguments: house does not exist")
	if e := CertHouse(100000000, -1, false, "test"); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> invalid arguments: no comment")
	if e := CertHouse(2, -1, false, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> invalid arguments: already published")
	if e := CertHouse(2, -1, true, "test pass"); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> permission: user not login")
	if e := CertHouse(2, -1, false, "test pass"); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> permission: login user is landlord")
	if e := CertHouse(2, 9, false, "test pass"); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> permission: login user is nither agency nor administrator")
	if e := CertHouse(2, 2, false, "test pass"); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> login user is agency, revoke certification")
	if e := CertHouse(2, 6, false, "test pass"); e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	t.Log("<Case> login user is agency, pass certification")
	if e := CertHouse(2, 6, true, "certification passed"); e != nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> login user is administrator, revoke certification")
	if e := CertHouse(2, 4, false, "test pass"); e != nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> login user is administrator, pass certification")
	if e := CertHouse(2, 4, true, "certification passed"); e != nil {
		t.Error("Failed, err: ", e)
		return
	}
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- RecommendHouse --
//
func Test_RecommendHouse(t *testing.T) {
	t.Log("Test RecommendHouse")

	t.Log("<Case> invalid arguments: house does not exist")
	if e := RecommendHouse(100000000, -1, 1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> permission: no user login")
	if e := RecommendHouse(2, -1, 1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> permission: login user does not exist")
	if e := RecommendHouse(2, 100000000, 1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> permission: login user is landlord")
	if e := RecommendHouse(2, 9, 3); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> permission: login user is nither agency nor administrator")
	if e := RecommendHouse(2, 2, 3); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> invalid arguments: action")
	if e := RecommendHouse(2, 6, 3); e == nil {
		t.Error("Failed, err: ", e)
		return
	}
	if e := RecommendHouse(2, 4, 3); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> permission: house not published")
	if e := RecommendHouse(4, 6, 2); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> invalid arguments: house has been recommended")
	if e := RecommendHouse(2, 6, 1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> unrecommend house")
	if e := RecommendHouse(2, 6, 2); e != nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> recommend house")
	if e := RecommendHouse(2, 6, 1); e != nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> invalid arguments: house not been recommended")
	if e := RecommendHouse(6, 5, 2); e == nil {
		t.Error("Failed, err: ", e)
		return
	}
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- CommitHouseByOwner --
//
func Test_CommitHouseByOwner(t *testing.T) {
	t.Log("Test CommitHouseByOwner")

	t.Log("<Case> invalid arguments: property == 0")
	hif := commdef.HouseInfo{Property: 0}
	if e, _ := CommitHouseByOwner(&hif, 9, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> invalid arguments: property < 0")
	hif = commdef.HouseInfo{Property: -1}
	if e, _ := CommitHouseByOwner(&hif, 9, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> invalid arguments: building_no = 0")
	hif = commdef.HouseInfo{Property: 2, BuildingNo: 0}
	if e, _ := CommitHouseByOwner(&hif, 9, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> invalid arguments: building_no < 0")
	hif = commdef.HouseInfo{Property: 2, BuildingNo: -1}
	if e, _ := CommitHouseByOwner(&hif, 9, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> invalid arguments: FloorTotal = 0")
	hif = commdef.HouseInfo{Property: 2, BuildingNo: 175, FloorTotal: 0}
	if e, _ := CommitHouseByOwner(&hif, 9, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> invalid arguments: FloorTotal < 0")
	hif = commdef.HouseInfo{Property: 2, BuildingNo: 175, FloorTotal: -1}
	if e, _ := CommitHouseByOwner(&hif, 9, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> invalid arguments: FloorThis = 0")
	hif = commdef.HouseInfo{Property: 2, BuildingNo: 175, FloorTotal: 35, FloorThis: 0}
	if e, _ := CommitHouseByOwner(&hif, 9, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> invalid arguments: FloorThis < 0")
	hif = commdef.HouseInfo{Property: 2, BuildingNo: 175, FloorTotal: 35, FloorThis: -2}
	if e, _ := CommitHouseByOwner(&hif, 9, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> invalid arguments: FloorThis > FloorTotal")
	hif = commdef.HouseInfo{Property: 2, BuildingNo: 175, FloorTotal: 35, FloorThis: 36}
	if e, _ := CommitHouseByOwner(&hif, 9, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> invalid arguments: Bedrooms < 0")
	hif = commdef.HouseInfo{Property: 2, BuildingNo: 175, FloorTotal: 35, FloorThis: 15, Bedrooms: -1}
	if e, _ := CommitHouseByOwner(&hif, 9, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> invalid arguments: Livingrooms < 0")
	hif = commdef.HouseInfo{Property: 2, BuildingNo: 175, FloorTotal: 35, FloorThis: 15, Bedrooms: 0, Livingrooms: -1}
	if e, _ := CommitHouseByOwner(&hif, 9, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> invalid arguments: Bathrooms < 0")
	hif = commdef.HouseInfo{Property: 2, BuildingNo: 175, FloorTotal: 35, FloorThis: 15, Bedrooms: 0,
		Livingrooms: 0, Bathrooms: -1}
	if e, _ := CommitHouseByOwner(&hif, 9, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> invalid arguments: Bathrooms, livingrooms, bedrooms are all 0")
	hif = commdef.HouseInfo{Property: 2, BuildingNo: 175, FloorTotal: 35, FloorThis: 15, Bedrooms: 0,
		Livingrooms: 0, Bathrooms: 0}
	if e, _ := CommitHouseByOwner(&hif, 9, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> invalid arguments: Acreage <= 100")
	hif = commdef.HouseInfo{Property: 2, BuildingNo: 175, FloorTotal: 35, FloorThis: 15, Bedrooms: 2,
		Livingrooms: 1, Bathrooms: 1, Acreage: 100}
	if e, _ := CommitHouseByOwner(&hif, 9, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> invalid arguments: property does not exist")
	hif = commdef.HouseInfo{Property: 200000000, BuildingNo: 175, FloorTotal: 35, FloorThis: 15, Bedrooms: 2,
		Livingrooms: 1, Bathrooms: 1, Acreage: 12300}
	if e, _ := CommitHouseByOwner(&hif, 9, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> invalid arguments: house owner")
	hif = commdef.HouseInfo{Property: 2, BuildingNo: 175, FloorTotal: 35, FloorThis: 15, Bedrooms: 2,
		Livingrooms: 1, Bathrooms: 1, Acreage: 12300}
	if e, _ := CommitHouseByOwner(&hif, -1, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}
	if e, _ := CommitHouseByOwner(&hif, 900000000, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> invalid arguments: house agency")
	if e, _ := CommitHouseByOwner(&hif, 9, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}
	if e, _ := CommitHouseByOwner(&hif, 9, 100000000); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> invalid arguments: house duplicated")
	hif = commdef.HouseInfo{Property: 2, BuildingNo: 56, FloorTotal: 45, FloorThis: 16, HouseNo: "1605", Bedrooms: 2,
		Livingrooms: 1, Bathrooms: 1, Acreage: 12300}
	if e, _ := CommitHouseByOwner(&hif, 9, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> commit new house")
	hif = commdef.HouseInfo{Property: 7, BuildingNo: 156, FloorTotal: 45, FloorThis: 26, HouseNo: "2605", Bedrooms: 2,
		Livingrooms: 1, Bathrooms: 1, Acreage: 12400}
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
