package models

import (
	// "ApiServer/commdef"
	"fmt"
	// "github.com/astaxie/beego"
	// "os"
	"testing"
	// "time"
)

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- NewEventRead --
//
func Test_NewEventRead(t *testing.T) {
	t.Log("Test NewEventRead")
	seq := 0

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: event < 0")
	if e := NewEventRead(-1, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: event = 0")
	if e := NewEventRead(-1, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: event does not exist")
	if e := NewEventRead(-1, 100000000); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: event has readed")
	if e := NewEventRead(-1, 7); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user not login")
	if e := NewEventRead(-1, 5); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user is SYSTEM")
	if e := NewEventRead(0, 5); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user does not exist")
	if e := NewEventRead(100000000, 5); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user is not a event receiver")
	if e := NewEventRead(11, 5); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "READ the new event")
	if e := NewEventRead(4, 5); e != nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Recover to UnRead the new event")
	if e := newEventUnread(4, 5); e != nil {
		t.Error("Failed, err: ", e)
		return
	}

}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- GetEventInfo --
//
func Test_GetEventInfo(t *testing.T) {
	t.Log("Test GetEventInfo")
	seq := 0

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: event < 0")
	if e, _ := GetEventInfo(-1, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: event = 0")
	if e, _ := GetEventInfo(-1, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: event does not exist")
	if e, _ := GetEventInfo(-1, 100000000); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user not log in")
	if e, _ := GetEventInfo(-1, 1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user is SYSTEM")
	if e, _ := GetEventInfo(0, 1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user does not exist")
	if e, _ := GetEventInfo(100000000, 1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user is not one of: sender, receiver, landlord, agency, administrator")
	if e, _ := GetEventInfo(11, 1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user is sender")
	if e, _ := GetEventInfo(2, 1); e != nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user is receiver")
	if e, _ := GetEventInfo(6, 1); e != nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user is landloard")
	if e, _ := GetEventInfo(10, 1); e != nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user is house agency")
	if e, _ := GetEventInfo(4, 1); e != nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user is administrator")
	e, ei := GetEventInfo(5, 1)
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	t.Log("event:", fmt.Sprintf("%+v", ei))

	return
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- GetEventProcList --
//
func Test_GetEventProcList(t *testing.T) {
	t.Log("Test GetEventProcList")
	seq := 0

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "")
	if e, _ := GetEventProcList(4, 7); e != nil {
		t.Error("Failed, err: ", e)
		return
	}

	return
}
