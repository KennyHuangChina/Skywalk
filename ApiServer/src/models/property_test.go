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

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- ModifyProperty --
//
func Test_ModifyProperty(t *testing.T) {
	t.Log("Test ModifyProperty")

	t.Log("<Case> invalid arguments: name is empty")
	if e := ModifyProperty(-1, 5, "", "", ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> invalid arguments: address is empty")
	if e := ModifyProperty(-1, 5, "1", "", ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> invalid arguments: desc is empty")
	if e := ModifyProperty(-1, 5, "1", "1", ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> permission: user not login")
	if e := ModifyProperty(-1, 5, "1", "1", "1"); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> permission: regular user have no right to modify")
	if e := ModifyProperty(2, 5, "1", "1", "1"); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> invalid arguments: property already exist")
	if e := ModifyProperty(6, 5, "花", "1", "1"); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> update by agency")
	// backup
	e, p := GetPropertyInfo(5)
	if nil != e {
		t.Error("Failed, err: ", e)
		return
	}
	t.Log("old property: ", fmt.Sprintf("%+v", p))
	// update
	name := "test property"
	addr := "test address"
	desc := "test desc"
	if e := ModifyProperty(6, 5, name, addr, desc); e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	_, p1 := GetPropertyInfo(5)
	t.Log("property: ", fmt.Sprintf("%+v", p1))
	if p1.PropName != name || p1.PropAddress != addr || p1.PropDesc != desc {
		t.Error("Failed")
		return
	}
	// restore
	if e := ModifyProperty(6, 5, p.PropName, p.PropAddress, p.PropDesc); e != nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> update by administrator")
	// backup
	e, p = GetPropertyInfo(5)
	if nil != e {
		t.Error("Failed, err: ", e)
		return
	}
	t.Log("old property: ", fmt.Sprintf("%+v", p))
	// update
	if e := ModifyProperty(4, 5, name, addr, desc); e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	_, p1 = GetPropertyInfo(5)
	t.Log("property: ", fmt.Sprintf("%+v", p1))
	if p1.PropName != name || p1.PropAddress != addr || p1.PropDesc != desc {
		t.Error("Failed")
		return
	}
	// restore
	if e := ModifyProperty(4, 5, p.PropName, p.PropAddress, p.PropDesc); e != nil {
		t.Error("Failed, err: ", e)
		return
	}
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- AddProperty --
//
func Test_AddProperty(t *testing.T) {
	t.Log("Test AddProperty")

	t.Log("<Case> invalid arguments: name is empty")
	if e, _ := AddProperty("", "", ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> invalid arguments: property already exist")
	if e, _ := AddProperty("花园", "", ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> add property")
	e, nid := AddProperty("AddProperty", "", "")
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	t.Log("new property:", nid)

	delProperty(nid, 4)
}
