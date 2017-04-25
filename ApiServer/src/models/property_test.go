package models

import (
	// "ApiServer/commdef"
	"fmt"
	// "github.com/astaxie/beego"
	"testing"
)

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- GetPropertyList --
//
func Test_GetPropertyList(t *testing.T) {
	t.Log("Test GetPropertyList")

	t.Log("<Case> invalid arguments: begin < 0")
	if e, _, _, _ := GetPropertyList("", -1, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> invalid arguments: toFetch < 0")
	if e, _, _, _ := GetPropertyList("", 0, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> get max count of whole property list")
	e, total, _, _ := GetPropertyList("", 0, 0)
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	t.Log("total:", total)
	e, total, fetchCnt, pl := GetPropertyList("", 0, total)
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	t.Log("fetchCnt:", fetchCnt)
	for k, v := range pl {
		t.Log("", k, ":", fmt.Sprintf("%+v", v))
	}

	t.Log("<Case> invalid arguments: begin > total")
	if e, _, _, _ = GetPropertyList("", total, 1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	keyword := "长江"
	t.Log("<Case> get count of property list with search key word:", keyword)
	e, total, _, _ = GetPropertyList(keyword, 0, 0)
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	t.Log("total:", total)
	e, total, fetchCnt, pl = GetPropertyList(keyword, 0, total)
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	t.Log("fetchCnt:", fetchCnt)
	for k, v := range pl {
		t.Log("", k, ":", fmt.Sprintf("%+v", v))
	}

	keyword = "江花"
	t.Log("<Case> get count of property list with search key word:", keyword)
	e, total, _, _ = GetPropertyList(keyword, 0, 0)
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	t.Log("total:", total)
	e, total, fetchCnt, pl = GetPropertyList(keyword, 0, total)
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	t.Log("fetchCnt:", fetchCnt)
	for k, v := range pl {
		t.Log("", k, ":", fmt.Sprintf("%+v", v))
	}
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- GetPropertyInfo --
//
func Test_GetPropertyInfo(t *testing.T) {
	t.Log("Test GetPropertyInfo")

	t.Log("<Case> invalid arguments: pid <= 0")
	if e, _ := GetPropertyInfo(-1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> invalid arguments: property doe's not exist")
	if e, _ := GetPropertyInfo(100000000); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> get property by id")
	e, p := GetPropertyInfo(2)
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	t.Log("property:", fmt.Sprintf("%+v", p))
}
