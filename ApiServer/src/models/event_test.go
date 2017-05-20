package models

import (
	"ApiServer/commdef"
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

	// seq++
	// t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user is sender")
	// if e, _ := GetEventInfo(2, 1); e != nil {
	// 	t.Error("Failed, err: ", e)
	// 	return
	// }

	// seq++
	// t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user is receiver")
	// if e, _ := GetEventInfo(6, 1); e != nil {
	// 	t.Error("Failed, err: ", e)
	// 	return
	// }

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
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: event id < 0")
	if e, _ := GetEventProcList(-1, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: event id = 0")
	if e, _ := GetEventProcList(-1, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: event does not exist")
	if e, _ := GetEventProcList(-1, 100000000); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user not log in")
	if e, _ := GetEventProcList(-1, 7); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user does not exist")
	if e, _ := GetEventProcList(100000000, 7); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user is a regular user")
	if e, _ := GetEventProcList(10, 7); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user is an agency, but not behalf this house")
	if e, _ := GetEventProcList(11, 7); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user is landlord")
	if e, _ := GetEventProcList(9, 7); e != nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user is agency of this house")
	if e, _ := GetEventProcList(6, 7); e != nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user is administrator")
	e, epl := GetEventProcList(5, 7)
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	if 2 != len(epl) {
		t.Error("Failed, proc records:", len(epl))
		return
	}
	if epl[0].Id <= epl[1].Id {
		t.Error("Failed, incorrect order, 0:", epl[0].Id, ", 1:", epl[1].Id)
		return
	}

	return
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- GetHouseEventList --
//
func Test_GetHouseEventList(t *testing.T) {
	t.Log("Test GetHouseEventList")
	seq := 0

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Arguments: house < 0")
	if e, _, _ := GetHouseEventList(-1, -1, 0, 10, 0, 0, false); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Arguments: house = 0")
	if e, _, _ := GetHouseEventList(-1, 0, 0, 10, 0, 0, false); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Arguments: house does not exist")
	if e, _, _ := GetHouseEventList(-1, 100000000, 0, 10, 0, 0, false); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user not login")
	if e, _, _ := GetHouseEventList(-1, 2, 0, 10, 0, 0, false); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user is SYSTEM")
	if e, _, _ := GetHouseEventList(0, 2, 0, 10, 0, 0, false); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user does not exist")
	if e, _, _ := GetHouseEventList(100000000, 2, 0, 10, 0, 0, false); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Arguments: bgn < 0")
	if e, _, _ := GetHouseEventList(4, 2, -1, 10, 0, 0, false); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Arguments: cnt < 0")
	if e, _, _ := GetHouseEventList(4, 2, 0, -1, 0, 0, false); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Arguments: stat < EVENT_STAT_Begin")
	if e, _, _ := GetHouseEventList(4, 2, 0, 0, EVENT_STAT_Begin-1, 0, false); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Arguments: stat > EVENT_STAT_End")
	if e, _, _ := GetHouseEventList(4, 2, 0, 0, EVENT_STAT_End+1, 0, false); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Arguments: et < 0")
	if e, _, _ := GetHouseEventList(4, 2, 0, 0, 0, -1, false); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Arguments: et > commdef.HOUSE_EVENT_End")
	if e, _, _ := GetHouseEventList(4, 2, 0, 0, 0, commdef.HOUSE_EVENT_End+1, false); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user is a regular user")
	if e, _, _ := GetHouseEventList(10, 2, 0, 0, 0, 0, false); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user is an agency, but not for this house")
	if e, _, _ := GetHouseEventList(11, 2, 0, 0, 0, 0, false); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Landlord get event list")
	if e, _, _ := GetHouseEventList(9, 2, 0, 0, 0, 0, false); e != nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "house agency get event list")
	if e, _, _ := GetHouseEventList(6, 2, 0, 0, 0, 0, false); e != nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "administrator get event list")
	if e, _, _ := GetHouseEventList(5, 2, 0, 0, 0, 0, false); e != nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "get all events")
	e, total, _ := GetHouseEventList(6, 2, 0, 0, EVENT_STAT_All, 0, false)
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	if 3 != total {
		t.Error("Failed, incorrect event count:", total)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "get new events")
	e, total, _ = GetHouseEventList(6, 2, 0, 0, EVENT_STAT_New, 0, false)
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	if 1 != total {
		t.Error("Failed, incorrect event count:", total)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "get ongoing events")
	e, total, _ = GetHouseEventList(6, 2, 0, 0, EVENT_STAT_Ongoin, 0, false)
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	if 1 != total {
		t.Error("Failed, incorrect event count:", total)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "get closed events")
	e, total, _ = GetHouseEventList(6, 2, 0, 0, EVENT_STAT_Closed, 0, false)
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	if 1 != total {
		t.Error("Failed, incorrect event count:", total)
		return
	}

	// seq++
	// t.Log(fmt.Sprintf("<Case %d>", seq), "")
	// if e, _, _ := GetHouseEventList(5, 2, 0, 10, 0, 0, false); e != nil {
	// 	t.Error("Failed, err: ", e)
	// 	return
	// }

	return
}
