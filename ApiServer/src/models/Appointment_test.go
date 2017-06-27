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

	xid := []int64{-1, 100000000, 11}
	xdesc := []string{"< 0", "does not exist", "not published"}

	// house
	for k, v := range xid {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Invalid arguments: house(%d) %s", seq, v, xdesc[k]))
		if e, _ := MakeAppointment(v, -1, -1, "", "", "", ""); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// appointment type
	xid = []int64{commdef.ORDER_TYPE_BEGIN - 1, commdef.ORDER_TYPE_END + 1}
	xdesc = []string{"< commdef.ORDER_TYPE_BEGIN(%d)", "> commdef.ORDER_TYPE_END(%d)"}
	xid2 := []int{commdef.ORDER_TYPE_BEGIN, commdef.ORDER_TYPE_END}
	for k, v := range xid {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Invalid arguments: appointment type(%d) %s", seq, v, fmt.Sprintf(xdesc[k], xid2[k])))
		if e, _ := MakeAppointment(0, 0, int(v), "", "", "", ""); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// appoint period, begin
	xitem := []string{"20170623", "201", "abcd", "2016-06-251130", "2016/06/25 1130", "2016-06-25 1130",
		"2016-06-25 11.30", "2016-06-25 11-30", "2016-06-25 11A30"}
	for _, v := range xitem {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Invalid arguments: appoint time begin(%s) Incorrect", seq, v))
		if e, _ := MakeAppointment(0, 0, commdef.ORDER_TYPE_SEE_APARTMENT, "", v, "", ""); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// appoint period, end
	xitem = []string{"20170623", "201", "abcd", "2016-06-251130", "2016/06/25 1130", "2016-06-25 1130",
		"2016-06-25 11.30", "2016-06-25 11-30", "2016-06-25 11A30"}
	for _, v := range xitem {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Invalid arguments: appoint time end(%s) Incorrect", seq, v))
		if e, _ := MakeAppointment(0, 0, commdef.ORDER_TYPE_SEE_APARTMENT, "", "2017-06-25 17:00", v, ""); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	t1 := "2017-06-25 17:00"
	t2 := "2017-06-25 16:30"
	seq++
	t.Log(fmt.Sprintf("<Case %d> Invalid arguments: appoint time end(%s) early than begin(%s)", seq, t2, t1))
	if e, _ := MakeAppointment(0, 0, commdef.ORDER_TYPE_SEE_APARTMENT, "", t1, t2, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	// description
	seq++
	t.Log(fmt.Sprintf("<Case %d> Invalid arguments: appointment description not set", seq))
	if e, _ := MakeAppointment(0, 0, commdef.ORDER_TYPE_SEE_APARTMENT, "", t2, t1, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	// user not login, and phone not set
	xitem = []string{"", "1", "1530626", "153062618044", "153a6261804", "153 6261804"}
	xdesc = []string{"not set", "incomplete", "incomplete", "over length", "include non-numeric character", "include non-numeric character"}
	for k, v := range xitem {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Invalid arguments: appoint phone(%s) %s", seq, v, xdesc[k]))
		if e, _ := MakeAppointment(0, 0, commdef.ORDER_TYPE_SEE_APARTMENT, v, t2, t2, "test"); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// Appointment already exist
	seq++
	t.Log(fmt.Sprintf("<Case %d> Invalid arguments: appointment already exist", seq))
	if e, _ := MakeAppointment(2, 0, commdef.ORDER_TYPE_SEE_APARTMENT, "15306261804", t2, t2, "test"); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	// Make Appointment
	seq++
	t.Log(fmt.Sprintf("<Case %d> Test: make appointment", seq))
	e, nid := MakeAppointment(2, 0, commdef.ORDER_TYPE_SEE_APARTMENT, "13862601240", t2, t2, "test")
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	t.Log("new appointment:", nid)

	seq++
	t.Log(fmt.Sprintf("<Case %d> Test: delte the appointment(%d) just added above", seq, nid))
	if e := DeleAppointment(nid, 5); nil != e {
		t.Error("Failed, err: ", e)
		return
	}

	// t.Error("NOT Implement")
}
