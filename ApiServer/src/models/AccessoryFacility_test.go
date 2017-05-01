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
