package models

import (
	// "ApiServer/commdef"
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
