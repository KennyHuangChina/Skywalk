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

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- AddDeliverable --
//
func Test_AddDeliverable(t *testing.T) {
	t.Log("Test AddDeliverable")
	seq := 0

	seq++
	t.Log(fmt.Sprintf("<Case %d> Invalid argument: name not set", seq))
	if e, _ := AddDeliverable("", -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d> Permission: user not login", seq))
	if e, _ := AddDeliverable("11", -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d> Permission: login user does not exist", seq))
	if e, _ := AddDeliverable("11", 100000000); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d> Permission: login user is a regualr user", seq))
	if e, _ := AddDeliverable("11", 2); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d> Permission: login user is a agency", seq))
	if e, _ := AddDeliverable("11", 6); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d> Invalid argument: deliverable already exist", seq))
	if e, _ := AddDeliverable("钥匙", 5); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d> Add Deliverable", seq))
	// Add
	name_add := fmt.Sprintf("水电存折测试_%d", seq)
	e, id := AddDeliverable(name_add, 5)
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	t.Log("id:", id)

	// remove
	if e := delDeliverable(id, 5); nil != e {
		t.Error("Failed, err: ", e)
		return
	}
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- AddHouseDeliverable --
//
func Test_AddHouseDeliverable(t *testing.T) {
	t.Log("Test AddHouseDeliverable")

	t.Log("<Case> Invalid argument: house < 0")
	if e, _ := AddHouseDeliverable(-1, -1, 0, 0, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Invalid argument: house = 0")
	if e, _ := AddHouseDeliverable(-1, 0, 0, 0, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Invalid argument: house does not exist")
	if e, _ := AddHouseDeliverable(-1, 100000000, 0, 0, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}
	hid := int64(2)

	t.Log("<Case> Invalid argument: deliverable < 0")
	if e, _ := AddHouseDeliverable(-1, hid, -1, 0, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Invalid argument: deliverable = 0")
	if e, _ := AddHouseDeliverable(-1, hid, 0, 0, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Invalid argument: deliverable does not exist")
	if e, _ := AddHouseDeliverable(-1, hid, 100000000, 0, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Invalid argument: qty < 0")
	if e, _ := AddHouseDeliverable(-1, hid, 5, -10, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Permission: user not login")
	if e, _ := AddHouseDeliverable(-1, hid, 5, 0, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Permission: user do not exist")
	if e, _ := AddHouseDeliverable(100000000, hid, 5, 0, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Permission: regular user")
	if e, _ := AddHouseDeliverable(2, hid, 5, 0, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Permission: agency, but not for this house")
	if e, _ := AddHouseDeliverable(11, hid, 5, 0, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Permission: agency of this house, no record to delete")
	if e, _ := AddHouseDeliverable(6, hid, 5, 0, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Permission: landlord of this house, no record to delete")
	if e, _ := AddHouseDeliverable(9, hid, 5, 0, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Permission: administrator, no record to delete")
	if e, _ := AddHouseDeliverable(5, hid, 5, 0, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Invalid Argument: administrator, house deliverable already exist")
	if e, _ := AddHouseDeliverable(5, 4, 3, 10, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Add deliverable for house, success")
	e, nid := AddHouseDeliverable(5, 4, 6, 10, "Test AddHouseDeliverable")
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	t.Log("new id:", nid)
	t.Log("<Case> Add same deliverable for house, rejected")
	if e, _ := AddHouseDeliverable(5, 4, 6, 10, "Test AddHouseDeliverable"); e == nil {
		t.Error("Failed, err: ", e)
		return
	}
	t.Log("<Case> remove the new house deliverable just add")
	if e, _ := AddHouseDeliverable(5, 4, 6, 0, "AddHouseDeliverable"); e != nil {
		t.Error("Failed, err: ", e)
		return
	}
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- EditHouseDeliverable --
//
func Test_EditHouseDeliverable(t *testing.T) {
	t.Log("Test EditHouseDeliverable")

	t.Log("<Case> Invalid argument: house deliverable < 0")
	if e := EditHouseDeliverable(-1, -1, 0, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Invalid argument: house deliverable = 0")
	if e := EditHouseDeliverable(0, -1, 0, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Invalid argument: house deliverable does not exist")
	if e := EditHouseDeliverable(100000000, -1, 0, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Permission: user not login")
	if e := EditHouseDeliverable(7, -1, 0, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Permission: login user is a regular user")
	if e := EditHouseDeliverable(7, 9, 0, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("<Case> Permission: login user is agency, but not for this house")
	if e := EditHouseDeliverable(7, 11, 0, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	// backup
	e, hd := getHouseDeliverable(7)
	if nil != e {
		t.Error("Failed, err: ", e)
		return
	}
	t.Log("before edit:", fmt.Sprintf("%+v", hd))

	// landlord
	t.Log("<Case> edit house deliverable by landlord")
	desc := "test deliverable"
	if e := EditHouseDeliverable(7, 2, hd.Qty+1, desc); e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	_, hd1 := getHouseDeliverable(7)
	t.Log("after edit:", fmt.Sprintf("%+v", hd1))
	if hd1.Qty != hd.Qty+1 || hd1.Desc != desc {
		t.Error("Failed to modify house deliverable")
		return
	}
	// restore
	if e := EditHouseDeliverable(7, 2, hd.Qty, hd.Desc); e != nil {
		t.Error("Fail to restore, err: ", e)
		return
	}

	// agency
	t.Log("<Case> edit house deliverable by agency")
	if e := EditHouseDeliverable(7, 1, hd.Qty+1, desc); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	// administrator
	t.Log("<Case> edit house deliverable by administrator")
	if e := EditHouseDeliverable(7, 5, hd.Qty+1, desc); e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	_, hd1 = getHouseDeliverable(7)
	t.Log("after edit:", fmt.Sprintf("%+v", hd1))
	if hd1.Qty != hd.Qty+1 || hd1.Desc != desc {
		t.Error("Failed to modify house deliverable")
		return
	}
	// restore
	if e := EditHouseDeliverable(7, 5, hd.Qty, hd.Desc); e != nil {
		t.Error("Fail to restore, err: ", e)
		return
	}

	t.Log("<Case> delete the house deliver by editing qty = 0")
	// add new one
	e, nhd := AddHouseDeliverable(5, 6, 1, 2, "new deliverable")
	if nil != e {
		t.Error("Fail to add, err: ", e)
		return
	}
	t.Log("new house deliverable:", nhd)
	// remove
	if e := EditHouseDeliverable(nhd, 5, 0, ""); e != nil {
		t.Error("Failed, err: ", e)
		return
	}

	return
}
