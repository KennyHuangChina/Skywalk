package models

import (
	"ApiServer/commdef"
	// "github.com/astaxie/beego"
	"testing"
)

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- GetUserInfo --
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

	// begin position < 0
	if e /*total*/, _ /*fetched*/, _, _ := GetBehalfList(commdef.BEHALF_TYPE_ALL, -1, 0, 0); e == nil {
		t.Error("Failed, err: ", e)
	}
	// fetch count < 0
	if e /*total*/, _ /*fetched*/, _, _ := GetBehalfList(commdef.BEHALF_TYPE_ALL, 0, -1, 0); e == nil {
		t.Error("Failed, err: ", e)
	}

	// login user has nothing to do with this house, neither agency of this house nor administrator
	if e /*total*/, _ /*fetched*/, _, _ := GetBehalfList(commdef.BEHALF_TYPE_ALL, 0, 0, 9); e == nil {
		t.Error("Failed, err: ", e)
	}

	//
	e, total /*fetched*/, _, _ := GetBehalfList(commdef.BEHALF_TYPE_ALL, 0, 0, 4)
	if e != nil {
		t.Error("Failed, err: ", e)
	}
	t.Log("total:", total)

	//
	t.Log("Test Case: begin position beyond total")
	if e, total /*fetched*/, _, _ = GetBehalfList(commdef.BEHALF_TYPE_ALL, total, 1, 4); e == nil {
		t.Error("Failed, err: ", e)
	}
}
