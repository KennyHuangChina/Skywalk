package models

import (
	// "ApiServer/commdef"
	"fmt"
	// "github.com/astaxie/beego"
	"testing"
)

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- GetHouseDeliverableList --
//
func Test_GetHouseDeliverableList(t *testing.T) {
	t.Log("Test GetHouseDeliverableList")

	t.Log("<Case>")
	e, hds := GetHouseDeliverableList(-1, 4)
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	for k, v := range hds {
		t.Log("", k, ":", fmt.Sprintf("%+v", v))
	}
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- GetDeliverables --
//
func Test_GetDeliverables(t *testing.T) {
	t.Log("Test GetDeliverables")

	t.Log("<Case>")
	e, ds := GetDeliverables(-1)
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	for k, v := range ds {
		t.Log("", k, ":", fmt.Sprintf("%+v", v))
	}
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- EditDeliverable --
//
func Test_EditDeliverable(t *testing.T) {
	t.Log("Test EditDeliverable")

	t.Log("<Case> Invalid argument: name not set")
	if e := EditDeliverable("", -1, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Invalid argument: did < 0")
	if e := EditDeliverable("1", -1, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Invalid argument: did = 0")
	if e := EditDeliverable("1", 0, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Invalid argument: deliverable not exist")
	if e := EditDeliverable("1", 100000000, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Invalid argument: deliverable name already exist")
	did := int64(3)
	if e := EditDeliverable("钥匙", did, 4); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> permission: user not login")
	if e := EditDeliverable("钥匙", did, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> permission: user does not exist")
	if e := EditDeliverable("钥匙", did, 100000000); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> permission: regular user")
	if e := EditDeliverable("钥匙", did, 2); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> permission: user is agency")
	if e := EditDeliverable("钥匙", did, 6); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Modify")
	// backup
	e, d := getDeliverable(did)
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	name_back := d.Name
	t.Log("name, before:", name_back)
	// modify
	name_modify := "TestName"
	if e := EditDeliverable(name_modify, did, 4); e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	_, d = getDeliverable(did)
	t.Log("name, to modify:", name_modify, ", modified:", d.Name)
	if name_modify != d.Name {
		t.Error("Failed, name mismatch ")
		return
	}
	// restore
	if e := EditDeliverable(name_back, did, 4); e != nil {
		t.Error("Failed, err: ", e)
		return
	}
}

// AddDeliverable

// AddHouseDeliverable
