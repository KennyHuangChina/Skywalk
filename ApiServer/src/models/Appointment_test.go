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
func Test_MakeAppointment_(t *testing.T) {
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
	x3 := []string{"", "20170623", "201", "abcd", "2016-06-251130", "2016/06/25 1130", "2016-06-25 1130", "2016-06-25 11.30",
		"2016-06-25 11-30", "2016-06-25 11A30", "2016-06-25 11:30", "2016-06-25 11:30", "2016-06-25 11:30", "2016-06-25 11:30",
		"2016-06-25 11:30", "2016-06-25 11:30", "2016-06-25 11:30", "2016-06-25 11:30", "2016-06-25 11:30", "2016-06-25 11:30"}
	x4 := []string{"", "", "", "", "", "", "", "", "", "", "20170623", "201", "abcd", "2016-06-251130", "2016/06/25 1130",
		"2016-06-25 1130", "2016-06-25 11.30", "2016-06-25 11-30", "2016-06-25 11A30", "2016-06-25 11:29"}
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

	//
	u := int64(9)
	seq++
	t.Log(fmt.Sprintf("<Case %d> Invalid arguments: appoint subscriber(%d) is landlord", seq, u))
	if e, _ := MakeAppointment(2, u, commdef.ORDER_TYPE_SEE_HOUSE, "", t1, t2, "test appointment"); e == nil {
		t.Error("Failed, err: ", e)
		return
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
//	-- MakeAppointmentAction --
//
func Test_MakeAppointmentAction(t *testing.T) {
	t.Log("Test MakeAppointmentAction")
	seq := 0

	// appointment
	x1 := []int64{-1, 0, 100000000}
	s1 := []string{"< 0", " == 0", "does not exist"}
	for k, v := range x1 {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Invalid arguments: appointment (%d) %s", seq, v, s1[k]))
		if e, _ := MakeAppointmentAction(0, v, commdef.APPOINT_ACTION_Unknow, "", "", ""); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// User
	x1 = []int64{-1, 0, 100000000}
	s1 = []string{"< 0", " == 0", "does not exist"}
	for k, v := range x1 {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Invalid arguments: User (%d) %s", seq, v, s1[k]))
		if e, _ := MakeAppointmentAction(v, 1, commdef.APPOINT_ACTION_Unknow, "", "", ""); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// Action
	x1 = []int64{commdef.APPOINT_ACTION_Begin, commdef.APPOINT_ACTION_End + 1, commdef.APPOINT_ACTION_Submit}
	s1 = []string{"<= commdef.APPOINT_ACTION_Begin", "> commdef.APPOINT_ACTION_End", "commdef.APPOINT_ACTION_Submit"}
	for k, v := range x1 {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Invalid arguments: Action (%d) %s", seq, v, s1[k]))
		if e, _ := MakeAppointmentAction(4, 1, int(v), "", "", ""); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// time period
	s3 := []string{"", "20170623", "201", "abcd", "2016-06-251130", "2016/06/25 1130", "2016-06-25 1130", "2016-06-25 11.30",
		"2016-06-25 11-30", "2016-06-25 11A30", "2016-06-25 11:30", "2016-06-25 11:30", "2016-06-25 11:30", "2016-06-25 11:30",
		"2016-06-25 11:30", "2016-06-25 11:30", "2016-06-25 11:30", "2016-06-25 11:30", "2016-06-25 11:30", "2016-06-25 11:30"}
	s4 := []string{"", "", "", "", "", "", "", "", "", "", "20170623", "201", "abcd", "2016-06-251130", "2016/06/25 1130",
		"2016-06-25 1130", "2016-06-25 11.30", "2016-06-25 11-30", "2016-06-25 11A30", "2016-06-25 11:29"}
	for k, v := range s3 {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Invalid arguments: time period (%s -> %s)", seq, v, s4[k]))
		if e, _ := MakeAppointmentAction(4, 1, commdef.APPOINT_ACTION_Submit, v, s4[k], ""); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
		seq++
		t.Log(fmt.Sprintf("<Case %d> Invalid arguments: time period (%s -> %s)", seq, v, s4[k]))
		if e, _ := MakeAppointmentAction(4, 1, commdef.APPOINT_ACTION_Reschedule, v, s4[k], ""); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// Comments
	t1 := "2017-09-22 17:00"
	t2 := "2017-09-22 18:00"
	x1 = []int64{commdef.APPOINT_ACTION_Confirm, commdef.APPOINT_ACTION_Reschedule, commdef.APPOINT_ACTION_Done, commdef.APPOINT_ACTION_Cancel}
	for _, v := range x1 {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Invalid arguments: comments (not set) for action: %d", seq, v))
		if e, _ := MakeAppointmentAction(4, 1, int(v), t1, t2, ""); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// Appointment receptionist not assigned
	x1 = []int64{commdef.APPOINT_ACTION_Confirm, commdef.APPOINT_ACTION_Reschedule, commdef.APPOINT_ACTION_Done, commdef.APPOINT_ACTION_Cancel}
	for _, v := range x1 {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Permission: Appointment receptionist not assigned for action: %d", seq, v))
		if e, _ := MakeAppointmentAction(4, 1, int(v), t1, t2, "test receptionist"); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// login user permission
	x1 = []int64{1, 2, 9, 11}
	for _, v := range x1 {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Permission: Login user(%d) ", seq, v))
		if e, _ := MakeAppointmentAction(v, 16, commdef.APPOINT_ACTION_Confirm, t1, t2, "test user permission"); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// create new appointment for testing
	seq++
	t.Log(fmt.Sprintf("<Case %d> Test: make appointment", seq))
	e, nid := MakeAppointment(2, 11, commdef.ORDER_TYPE_SEE_HOUSE, "", t1, t2, "test appointment")
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	t.Log("new appointment:", nid)

	t1 = "2017-09-23 18:01"
	t2 = "2017-09-23 19:01"
	x1 = []int64{commdef.APPOINT_ACTION_Confirm, commdef.APPOINT_ACTION_Reschedule, commdef.APPOINT_ACTION_Done, commdef.APPOINT_ACTION_Cancel}
	x2 := []int64{6, 11, 6, 11}
	for k, v := range x1 {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Testing add action: (%d) ", seq, v))
		if e, _ := MakeAppointmentAction(x2[k], nid, int(v), t1, t2, "test appointment action"); e != nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d> Test: del the appointment(%d) just added above", seq, nid))
	if e := DeleAppointment(nid, 5); nil != e {
		t.Error("Failed, err: ", e)
		return
	}
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

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- GetAppointmentInfo --
//
func Test_GetAppointmentInfo(t *testing.T) {
	t.Log("Test GetAppointmentInfo")
	seq := 0

	// login user
	x1 := []int64{-1, 0, 100000000}
	s1 := []string{"< 0", "is SYSTEM", "does not exist"}
	for k, v := range x1 {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Invalid arguments: user(%d) %s", seq, v, s1[k]))
		if e, _ := GetAppointmentInfo(v, 0); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// appointment
	s1 = []string{"< 0", " = 0", "does not exist"}
	for k, v := range x1 {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Invalid arguments: appointment(%d) %s", seq, v, s1[k]))
		if e, _ := GetAppointmentInfo(4, v); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// permission: user
	x1 = []int64{1, 2, 9, 11}
	for _, v := range x1 {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Permission: User(%d) do not have permission to get appointment info", seq, v))
		if e, _ := GetAppointmentInfo(v, 16); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// Testing
	x1 = []int64{4, 5, 6, 10}
	s1 = []string{"administrator", "administrator", "receptionist", "subscriber"}
	for k, v := range x1 {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Testing: User(%d) is %s", seq, v, s1[k]))
		if e, _ := GetAppointmentInfo(v, 16); e != nil {
			t.Error("Failed, err: ", e)
			return
		}
	}
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- AssignAppointmentRectptionist --
//
func Test_AssignAppointmentRectptionist(t *testing.T) {
	t.Log("Test AssignAppointmentRectptionist")
	seq := 0

	// appointment
	x1 := []int64{-1, 0, 100000000}
	s1 := []string{"< 0", "= 0", "does not exist"}
	for k, v := range x1 {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Argument: Appointment(%d) %s", seq, v, s1[k]))
		if e := AssignAppointmentRectptionist(0, v, 0); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// receptionist
	for k, v := range x1 {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Argument: Receptionist(%d) %s", seq, v, s1[k]))
		if e := AssignAppointmentRectptionist(0, 1, v); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d> Argument: Receptionist(6) not change", seq))
	if e := AssignAppointmentRectptionist(4, 16, 6); e != nil {
		t.Error("Failed, err: ", e)
		return
	}

	// login user
	for k, v := range x1 {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Argument: login user(%d) %s", seq, v, s1[k]))
		if e := AssignAppointmentRectptionist(v, 1, 6); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	x1 = []int64{1, 2, 6, 9, 10, 11}
	for _, v := range x1 {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Permission: login user(%d) is not an administrator", seq, v))
		if e := AssignAppointmentRectptionist(v, 1, 6); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	/* Testing */
	// create a appointment for testing
	t1 := "2016-06-25 12:00"
	t2 := "2016-06-25 12:30"
	seq++
	t.Log(fmt.Sprintf("<Case %d> Test: make appointment", seq))
	e, naid := MakeAppointment(6, 10, commdef.ORDER_TYPE_SEE_HOUSE, "", t1, t2, "test AssignAppointmentRectptionist")
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	t.Log("new appointment:", naid)

	// assign receptionist
	x1 = []int64{4} //, 5}
	for _, v := range x1 {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Test: login user(%d) reassign the receptionist", seq, v))
		if e := AssignAppointmentRectptionist(v, naid, 11); e != nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// delete the testing appointment
	seq++
	t.Log(fmt.Sprintf("<Case %d> Test: del the appointment(%d) just added above", seq, naid))
	if e := DeleAppointment(naid, 5); nil != e {
		t.Error("Failed, err: ", e)
		return
	}

}
