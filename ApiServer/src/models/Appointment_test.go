package models

import (
	"ApiServer/commdef"
	"fmt"
	// "strconv"
	// "github.com/astaxie/beego"
	"testing"
)

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- MakeAppointment --
//
func Test_MakeAppointment(t *testing.T) {
	t.Log("Test MakeAppointment, Invalid Arguments")
	seq := 0

	// appointment type
	x1 := []int64{commdef.ORDER_TYPE_BEGIN - 1, commdef.ORDER_TYPE_END + 1}
	xdesc := []string{"< commdef.ORDER_TYPE_BEGIN(%d)", "> commdef.ORDER_TYPE_END(%d)"}
	x2 := []int{commdef.ORDER_TYPE_BEGIN, commdef.ORDER_TYPE_END}
	for k, v := range x1 {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Invalid arguments: appointment type(%d) %s", seq, v, fmt.Sprintf(xdesc[k], x2[k])))
		if e, _ := MakeAppointment(0, 0, int(v), "", "", "", ""); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// appoint period
	x3 := []string{"", "20170623", "201", "abcd", "2016-06-251130", "2016/06/25 1130", "2016-06-25 1130",
		"2016-06-25 11.30", "2016-06-25 11-30", "2016-06-25 11A30", "2016-06-25 11:30"}
	x4 := []string{"", "20170623", "201", "abcd", "2016-06-251130", "2016/06/25 1130", "2016-06-25 1130",
		"2016-06-25 11.30", "2016-06-25 11-30", "2016-06-25 11A30", "2016-06-25 11:29"}
	for k, v := range x3 {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Invalid arguments: schedule time(%s - %s) Incorrect", seq, v, x4[k]))
		if e, _ := MakeAppointment(0, 0, commdef.ORDER_TYPE_SEE_HOUSE, "", v, x4[k], ""); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// description
	t1 := "2016-06-25 11:30"
	t2 := "2016-06-25 12:30"
	seq++
	t.Log(fmt.Sprintf("<Case %d> Invalid arguments: appointment description not set", seq))
	if e, _ := MakeAppointment(0, 0, commdef.ORDER_TYPE_SEE_HOUSE, "", t1, t2, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	// house
	x1 = []int64{-1, 100000000, 11}
	xdesc = []string{"< 0", "does not exist", "not published"}
	for k, v := range x1 {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Invalid arguments: house(%d) %s", seq, v, xdesc[k]))
		if e, _ := MakeAppointment(v, -1, commdef.ORDER_TYPE_SEE_HOUSE, "", t1, t2, "test appointment"); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// user not login, and phone not set
	xitem := []string{"", "1", "1530626", "153062618044", "153a6261804", "153 6261804"}
	xdesc = []string{"not set", "incomplete", "incomplete", "over length", "include non-numeric character", "include space"}
	for k, v := range xitem {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Invalid arguments: appoint phone(%s) %s", seq, v, xdesc[k]))
		if e, _ := MakeAppointment(0, 0, commdef.ORDER_TYPE_SEE_HOUSE, v, t1, t2, "test appointment"); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// Make Appointment
	seq++
	t.Log(fmt.Sprintf("<Case %d> Test: make appointment", seq))
	e, nid := MakeAppointment(2, 10, commdef.ORDER_TYPE_SEE_HOUSE, "13862601240", t2, t2, "test appointment")
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	t.Log("new appointment:", nid)

	// Appointment already exist
	seq++
	t.Log(fmt.Sprintf("<Case %d> Invalid arguments: appointment already exist", seq))
	if e, _ := MakeAppointment(2, 10, commdef.ORDER_TYPE_SEE_HOUSE, "13862601240", t2, t2, "test appointment"); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d> Test: del the appointment(%d) just added above", seq, nid))
	if e := DeleAppointment(nid, 5); nil != e {
		t.Error("Failed, err: ", e)
		return
	}

	// t.Error("NOT Implement")
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- GetAppointList_SeeHouse --
//
func Test_GetAppointList_SeeHouse(t *testing.T) {
	t.Log("Test GetAppointList_SeeHouse, Invalid Arguments")
	seq := 0

	// begin position
	seq++
	t.Log(fmt.Sprintf("<Case %d> Invalid arguments: begin(-1) < 0", seq))
	if e, _, _ := GetAppointList_SeeHouse(-1, -1, -1, -1); nil == e {
		t.Error("Failed, err: ", e)
		return
	}

	// fetch count
	seq++
	t.Log(fmt.Sprintf("<Case %d> Invalid arguments: fetch count(-1) < 0", seq))
	if e, _, _ := GetAppointList_SeeHouse(-1, -1, 0, -1); nil == e {
		t.Error("Failed, err: ", e)
		return
	}

	// record count
	seq++
	t.Log(fmt.Sprintf("<Case %d> Test: get record count", seq))
	e, total, _ := GetAppointList_SeeHouse(2, -1, 0, 0)
	if nil != e {
		t.Error("Failed, err: ", e)
		return
	}

	// begin position
	seq++
	t.Log(fmt.Sprintf("<Case %d> Invalid arguments: begin(%d) over the", seq, total))
	if e, _, _ := GetAppointList_SeeHouse(2, -1, int(total), int(total)); nil == e {
		t.Error("Failed, err: ", e)
		return
	}

	// user
	xid := []int64{-1, 0, 100000000}
	xdesc := []string{"not login", "is SYSTEM", "does not exist"}
	for k, v := range xid {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Invalid arguments: user(%d) %s", seq, v, xdesc[k]))
		if e, _, _ := GetAppointList_SeeHouse(2, v, 0, int(total)); nil == e {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// house
	xid = []int64{-1, 100000000}
	xdesc = []string{"< 0", "does not exist"}

	for k, v := range xid {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Invalid arguments: house(%d) %s", seq, v, xdesc[k]))
		if e, _, _ := GetAppointList_SeeHouse(v, 5, 0, int(total)); nil == e {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// user permission
	xid = []int64{2 /*, 11*/}
	xdesc = []string{"is a regular user", "is agency, but not for this house"}
	for k, v := range xid {
		seq++
		t.Log(fmt.Sprintf("<Case %d> permission: user(%d) %s", seq, v, xdesc[k]))
		if e, _, _ := GetAppointList_SeeHouse(2, v, 0, int(total)); nil == e {
			t.Error("Failed, err: ", e)
			return
		}
	}
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- GetHouseList_AppointSee --
//
func Test_GetHouseList_AppointSee(t *testing.T) {
	// seq := 0

	t.Error("TODO:")
}
