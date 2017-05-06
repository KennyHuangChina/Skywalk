package models

import (
	"ApiServer/commdef"
	"fmt"
	// "github.com/astaxie/beego"
	"testing"
)

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- GetFacilityTypeList --
//
func Test_GetFacilityTypeList(t *testing.T) {
	t.Log("Test GetFacilityTypeList")

	t.Log("<Case> Invalid parameter: user not login")
	if e, _ := GetFacilityTypeList(-1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Invalid parameter: user does not exist")
	if e, _ := GetFacilityTypeList(100000000); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> get facility type list")
	e, ftl := GetFacilityTypeList(4)
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}

	for k, v := range ftl {
		t.Log("", k, ":", fmt.Sprintf("%+v", v))
	}
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- EditFacilityType --
//
func Test_EditFacilityType(t *testing.T) {
	t.Log("Test EditFacilityType")

	t.Log("<Case> Permission: user not login")
	if e := EditFacilityType("", -1, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Permission: login user does not exist")
	if e := EditFacilityType("", -1, 100000000); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Permission: login user is a regular user")
	if e := EditFacilityType("", -1, 9); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Permission: login user is a agency")
	if e := EditFacilityType("", -1, 11); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Invalid parameter: type name is empty")
	if e := EditFacilityType("", -1, 5); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Invalid parameter: type < 0")
	if e := EditFacilityType("test", -1, 5); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Invalid parameter: type = 0")
	if e := EditFacilityType("test", 0, 5); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Invalid parameter: type does not exist")
	if e := EditFacilityType("test", 100000000, 5); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Invalid parameter: type name already exist")
	if e := EditFacilityType("试类", 2, 5); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Modify")
	// Modify
	if e := EditFacilityType("家电1", 2, 5); e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	// restore
	if e := EditFacilityType("家电", 2, 5); e != nil {
		t.Error("Failed, err: ", e)
		return
	}
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- DeleFacilityType --
//
func Test_DeleFacilityType(t *testing.T) {
	t.Log("Test DeleFacilityType")

	t.Log("<Case> Permission: user not login")
	if e := DeleFacilityType(-1, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Permission: login user does not exist")
	if e := DeleFacilityType(-1, 100000000); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Permission: login user is a regular user")
	if e := DeleFacilityType(-1, 9); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Permission: login user is a agency")
	if e := DeleFacilityType(-1, 11); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Invalid parameter: type < 0")
	if e := DeleFacilityType(-1, 5); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Invalid parameter: type = 0")
	if e := DeleFacilityType(0, 5); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Invalid parameter: type does not exist")
	if e := DeleFacilityType(100000000, 5); e == nil {
		t.Error("Failed, err: ", e)
		return
	}
	t.Log("The actual delete testing, please ref to Test_AddFacilityType")

	return
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- AddFacilityType --
//
func Test_AddFacilityType(t *testing.T) {
	t.Log("Test AddFacilityType")

	t.Log("<Case> Permission: user not login")
	if e, _ := AddFacilityType("", -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Permission: login user does not exist")
	if e, _ := AddFacilityType("", 100000000); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Permission: login user is a regular user")
	if e, _ := AddFacilityType("", 9); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Permission: login user is a agency")
	if e, _ := AddFacilityType("", 11); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Invalid parameter: type name is empty")
	if e, _ := AddFacilityType("", 5); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Invalid parameter: type name already exist")
	if e, _ := AddFacilityType("试类", 5); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Add facility type")
	e, nftid := AddFacilityType("test 测试 test", 5)
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Delete facility type just added a second ago")
	if e := DeleFacilityType(nftid, 5); e != nil {
		t.Error("Failed, err: ", e)
		return
	}

	return
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- GetFacilityList --
//
func Test_GetFacilityList(t *testing.T) {
	t.Log("Test GetFacilityList")

	t.Log("<Case> Invalid argument: type < 0")
	if e, _ := GetFacilityList(-1, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Invalid argument: facility type does not exist")
	if e, _ := GetFacilityList(-1, 100000000); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Permission: user not logined")
	if e, _ := GetFacilityList(-1, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Permission: user does not exist")
	if e, _ := GetFacilityList(100000000, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> fetch all facilities")
	e, fl := GetFacilityList(9, 0)
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	for k, v := range fl {
		t.Log("", k, ":", fmt.Sprintf("%+v", v))
	}

	t.Log("<Case> fetch a certain kind of facilities")
	e, fl = GetFacilityList(9, 2)
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	for k, v := range fl {
		t.Log("", k, ":", fmt.Sprintf("%+v", v))
	}

	return
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- EditFacility --
//
func Test_EditFacility(t *testing.T) {
	t.Log("Test EditFacility")

	t.Log("<Case 1> Permission: user not login")
	if e := EditFacility(-1, -1, -1, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case 2> Permission: user does not exist")
	if e := EditFacility(-1, -1, 100000000, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case 3> Permission: user is a regualr user")
	if e := EditFacility(-1, -1, 9, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case 4> Permission: user is a agency")
	if e := EditFacility(-1, -1, 6, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case 5> Invalid argument: facility < 0")
	if e := EditFacility(-1, -1, 5, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case 6> Invalid argument: facility = 0")
	if e := EditFacility(0, -1, 5, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case 7> Invalid argument: facility does not exist")
	if e := EditFacility(100000000, -1, 5, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case 8> Invalid argument: name is empty")
	if e := EditFacility(3, -1, 5, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case 9> Invalid argument: type < 0")
	if e := EditFacility(3, -1, 5, "test"); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case 10> Invalid argument: type = 0")
	if e := EditFacility(3, 0, 5, "test"); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case 11> Invalid argument: duplicated")
	if e := EditFacility(3, 2, 5, "电视"); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case 12> Modify facility")
	if e := EditFacility(3, 6, 5, "电视机60吋"); e != nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case 13> Restore facility")
	if e := EditFacility(3, 2, 5, "电冰箱"); e != nil {
		t.Error("Failed, err: ", e)
		return
	}

	return
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- DelFacility --
//
func Test_DelFacility(t *testing.T) {
	t.Log("Test DelFacility")

	t.Log("<Case 1> Permission: user not login")
	if e := DelFacility(-1, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case 2> Permission: user does not exist")
	if e := DelFacility(-1, 100000000); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case 3> Permission: user is a regualr user")
	if e := DelFacility(-1, 9); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case 4> Permission: user is a agency")
	if e := DelFacility(-1, 6); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case 5> Invalid argument: facility < 0")
	if e := DelFacility(-1, 5); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case 6> Invalid argument: facility = 0")
	if e := DelFacility(0, 5); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case 7> Invalid argument: facility does not exist")
	if e := DelFacility(100000000, 5); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("The actual delete testing, please ref to Test_AddFacility")
	return
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- AddFacility --
//
func Test_AddFacility(t *testing.T) {
	t.Log("Test AddFacility")

	t.Log("<Case 1> Permission: user not login")
	if e, _ := AddFacility("", -1, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case 2> Permission: user does not exist")
	if e, _ := AddFacility("", -1, 100000000); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case 3> Permission: user is a regualr user")
	if e, _ := AddFacility("", -1, 9); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case 4> Permission: user is a agency")
	if e, _ := AddFacility("", -1, 6); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case 5> Invalid argument: name is empty")
	if e, _ := AddFacility("", -1, 5); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case 6> Invalid argument: type < 0")
	if e, _ := AddFacility("test", -1, 5); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case 7> Invalid argument: type = 0")
	if e, _ := AddFacility("test", 0, 5); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case 8> Invalid argument: type does not exist")
	if e, _ := AddFacility("test", 100000000, 5); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case 9> Invalid argument: duplicated")
	if e, _ := AddFacility("冰", 2, 5); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case 10> Add facility")
	e, nid := AddFacility("宽带网络", 6, 5)
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	t.Log("new facility:", nid)

	t.Log("<Case 11> Delete facility just added a second ago")
	if e := DelFacility(nid, 5); e != nil {
		t.Error("Failed, err: ", e)
		return
	}

	return
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- GetHouseFacilities --
//
func Test_GetHouseFacilities(t *testing.T) {
	t.Log("Test GetHouseFacilities")

	seq := 1
	t.Log("<Case", seq, "> Invalid argument: house < 0")
	if e, _ := GetHouseFacilities(-1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}
	seq += 1

	t.Log("<Case", seq, "> Invalid argument: house = 0")
	if e, _ := GetHouseFacilities(0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}
	seq += 1

	t.Log("<Case", seq, "> Invalid argument: house does not exist")
	if e, _ := GetHouseFacilities(100000000); e == nil {
		t.Error("Failed, err: ", e)
		return
	}
	seq += 1

	t.Log("<Case", seq, "> Invalid argument: house does not have any facility")
	if e, _ := GetHouseFacilities(4); e == nil {
		t.Error("Failed, err: ", e)
		return
	}
	seq += 1

	t.Log("<Case", seq, "> get house facility list")
	e, hfl := GetHouseFacilities(6)
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	seq += 1
	for k, v := range hfl {
		t.Log("", k, ":", fmt.Sprintf("%+v", v))
	}

	return
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- EditHouseFacility --
//
func Test_EditHouseFacility(t *testing.T) {
	t.Log("Test EditHouseFacility")
	seq := 1

	t.Log("<Case", seq, "> Invalid argument: facility < 0")
	if e := EditHouseFacility(-1, -1, -1, 0, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}
	seq += 1

	t.Log("<Case", seq, "> Invalid argument: facility = 0")
	if e := EditHouseFacility(-1, -1, 0, 0, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}
	seq += 1

	t.Log("<Case", seq, "> Invalid argument: facility does not exist")
	if e := EditHouseFacility(-1, -1, 100000000, 0, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}
	seq += 1

	t.Log("<Case", seq, "> Invalid argument: house facility < 0")
	if e := EditHouseFacility(-1, -1, 3, 0, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}
	seq += 1

	t.Log("<Case", seq, "> Invalid argument: house facility = 0")
	if e := EditHouseFacility(-1, 0, 3, 0, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}
	seq += 1

	t.Log("<Case", seq, "> Invalid argument: house facility does not exist")
	if e := EditHouseFacility(-1, 100000000, 3, 0, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}
	seq += 1

	t.Log("<Case", seq, "> Invalid argument: qty < 0")
	if e := EditHouseFacility(-1, 7, 3, -1, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}
	seq += 1

	t.Log("<Case", seq, "> Permission: user not login")
	if e := EditHouseFacility(-1, 7, 3, 1, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}
	seq += 1

	t.Log("<Case", seq, "> Permission: user does not exist")
	if e := EditHouseFacility(100000000, 7, 3, 1, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}
	seq += 1

	t.Log("<Case", seq, "> Permission: user is a regular user")
	if e := EditHouseFacility(9, 7, 3, 1, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}
	seq += 1

	t.Log("<Case", seq, "> Permission: user is a agency, but not for this house")
	if e := EditHouseFacility(11, 5, 4, 1, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}
	seq += 1

	t.Log("<Case", seq, "> Update by administrator")
	if e := EditHouseFacility(4, 5, 7, 10, "测试说明"); e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	seq += 1

	t.Log("<Case", seq, "> Restore by house agency")
	if e := EditHouseFacility(6, 5, 4, 333, "fdesc_1111"); e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	seq += 1

	t.Log("<Case", seq, "> Add new facility by landlord")
	hf := [...]commdef.AddHouseFacility{commdef.AddHouseFacility{Facility: int64(7), Qty: 3, Desc: "add desc"}}
	e, nid := AddHouseFacilities(9, 2, hf[:])
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	seq += 1

	t.Log("<Case", seq, "> remove the new facility by landlord")
	if e := EditHouseFacility(9, nid[0], 7, 0, "fdesc_1111"); e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	seq += 1

	return
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- DeleteHouseFacility --
//
func Test_DeleteHouseFacility(t *testing.T) {
	t.Log("Test DeleteHouseFacility")
	seq := 1

	t.Log("<Case", seq, "> Invalid argument: house facility < 0")
	if e := DeleteHouseFacility(-1, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}
	seq++

	t.Log("<Case", seq, "> Invalid argument: house facility = 0")
	if e := DeleteHouseFacility(-1, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}
	seq++

	t.Log("<Case", seq, "> Invalid argument: house facility does not exist")
	if e := DeleteHouseFacility(-1, 100000000); e == nil {
		t.Error("Failed, err: ", e)
		return
	}
	seq++

	t.Log("<Case", seq, "> Permission: user not login")
	if e := DeleteHouseFacility(-1, 5); e == nil {
		t.Error("Failed, err: ", e)
		return
	}
	seq++

	t.Log("<Case", seq, "> Permission: user is a regular user")
	if e := DeleteHouseFacility(11, 5); e == nil {
		t.Error("Failed, err: ", e)
		return
	}
	seq++

	t.Log("<Case", seq, "> Permission: user is an agency, but not for this house")
	if e := DeleteHouseFacility(2, 5); e == nil {
		t.Error("Failed, err: ", e)
		return
	}
	seq++

	t.Log("Please ref to API Test_AddHouseFacilities for actual processing of DeleteHouseFacility")
	// if e := DeleteHouseFacility(5, 5); e == nil {
	// 	t.Error("Failed, err: ", e)
	// 	return
	// }
	// seq += 1

	return
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- AddHouseFacilities --
//
func Test_AddHouseFacilities(t *testing.T) {
	t.Log("Test AddHouseFacilities")
	seq := 1

	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid argument: house < 0")
	if e, _ := AddHouseFacilities(-1, -1, nil); e == nil {
		t.Error("Failed, err: ", e)
		return
	}
	seq++

	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid argument: house = 0")
	if e, _ := AddHouseFacilities(-1, 0, nil); e == nil {
		t.Error("Failed, err: ", e)
		return
	}
	seq++

	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid argument: house does not exist")
	if e, _ := AddHouseFacilities(-1, 100000000, nil); e == nil {
		t.Error("Failed, err: ", e)
		return
	}
	seq++

	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid argument: user not login")
	if e, _ := AddHouseFacilities(-1, 4, nil); e == nil {
		t.Error("Failed, err: ", e)
		return
	}
	seq++

	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid argument: user does not exist")
	if e, _ := AddHouseFacilities(100000000, 4, nil); e == nil {
		t.Error("Failed, err: ", e)
		return
	}
	seq++

	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission Checking: user is a regular user")
	if e, _ := AddHouseFacilities(9, 4, nil); e == nil {
		t.Error("Failed, err: ", e)
		return
	}
	seq++

	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission Checking: user is an agency, but not for this house")
	if e, _ := AddHouseFacilities(11, 4, nil); e == nil {
		t.Error("Failed, err: ", e)
		return
	}
	seq++

	// t.Log(fmt.Sprintf("<Case %d>", seq), "Permission Checking: user is an agency of this house")
	// if e, _ := AddHouseFacilities(6, 4, nil); e == nil {
	// 	t.Error("Failed, err: ", e)
	// 	return
	// }
	// seq++

	// t.Log(fmt.Sprintf("<Case %d>", seq), "Permission Checking: user is house owner")
	// if e, _ := AddHouseFacilities(4, 4, nil); e == nil {
	// 	t.Error("Failed, err: ", e)
	// 	return
	// }
	// seq++

	// t.Log(fmt.Sprintf("<Case %d>", seq), "Permission Checking: user is an administrator")
	// if e, _ := AddHouseFacilities(5, 4, nil); e == nil {
	// 	t.Error("Failed, err: ", e)
	// 	return
	// }
	// seq++

	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid argument: no facility list assigned, nil")
	if e, _ := AddHouseFacilities(5, 4, nil); e == nil {
		t.Error("Failed, err: ", e)
		return
	}
	seq++

	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid argument: no facility list assigned, empty")
	hf := [...]commdef.AddHouseFacility{}
	if e, _ := AddHouseFacilities(5, 4, hf[:]); e == nil {
		t.Error("Failed, err: ", e)
		return
	}
	seq++

	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid argument: facility not found")
	hf1 := [...]commdef.AddHouseFacility{commdef.AddHouseFacility{Facility: int64(100000000), Qty: 3, Desc: "add desc"}}
	if e, _ := AddHouseFacilities(5, 4, hf1[:]); e == nil {
		t.Error("Failed, err: ", e)
		return
	}
	seq++

	t.Log(fmt.Sprintf("<Case %d>", seq), "add facility to house that don't have any facility")
	seq++
	hf1[0].Facility = 7
	e, ids := AddHouseFacilities(5, 4, hf1[:])
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	t.Log("new house facility ids:", ids)

	t.Log(fmt.Sprintf("<Case %d>", seq), "add facility to house that already have facility")
	seq++
	hf2 := [...]commdef.AddHouseFacility{commdef.AddHouseFacility{Facility: 3, Qty: 33, Desc: "add desc 3"},
		commdef.AddHouseFacility{Facility: 7, Qty: 77, Desc: "add desc 7"},
		commdef.AddHouseFacility{Facility: 8, Qty: 88, Desc: "add desc 8"}}
	e, ids2 := AddHouseFacilities(5, 4, hf2[:])
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	t.Log("new house facility ids:", ids2)

	t.Log(fmt.Sprintf("<Case %d>", seq), "Remove all house facilities added above")
	seq++
	for _, v := range ids {
		t.Log("Delete new house facility:", v)
		if e := DeleteHouseFacility(5, v); e != nil {
			t.Error("Fail to remove house facility:", v, ", err: ", e)
			return
		}
	}
	for _, v := range ids2 {
		t.Log("Delete new house facility:", v)
		if e := DeleteHouseFacility(5, v); e != nil {
			t.Error("Fail to remove house facility:", v, ", err: ", e)
			return
		}
	}

	return
}
