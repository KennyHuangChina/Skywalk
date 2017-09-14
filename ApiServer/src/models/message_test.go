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
//	-- GetNewMsgCount --
//
func Test_GetNewMsgCount(t *testing.T) {
	t.Log("Test GetNewMsgCount")
	seq := 0

	xid := []int64{-1, 0, 100000000}
	xdesc := []string{"not login", "is system user", "does not exist"}
	for k, v := range xid {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Permission: user(%d) %s", seq, v, xdesc[k]))
		if e, _ := GetNewMsgCount(v); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	u := int64(11)
	seq++
	t.Log(fmt.Sprintf("<Case %d> Testing: user(%d) do not have new message", seq, u))
	e, n := GetNewMsgCount(u)
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	if 0 != n {
		t.Error(fmt.Sprintf("Failed, user(%d) has %d messages", u, n))
		return
	}

	u = 4
	seq++
	t.Log(fmt.Sprintf("<Case %d> Testing: user(%d) have new message", seq, u))
	e, n = GetNewMsgCount(u)
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	if 0 == n {
		t.Error(fmt.Sprintf("Failed, user(%d) do not has messages", u))
		return
	}

	// t.Error("TODO: ")

	return
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- ReadMsg --
//
func Test_ReadMsg(t *testing.T) {
	t.Log("Test ReadMsg")
	seq := 0

	xid := []int64{-1, 0, 100000000, 4, 6}
	xdesc := []string{"not login", "is system user", "does not exist", "is an administrator, but not receiver", "is an agency, but not receiver"}
	for k, v := range xid {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Permission: user(%d) %s", seq, v, xdesc[k]))
		if e := ReadMsg(v, 3); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	xid = []int64{-1, 0, 100000000, 2, 1}
	xdesc = []string{"== -1", "== 0", "does not exist", "alread readed", "is not for current login user"}
	for k, v := range xid {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Permission: message(%d) %s", seq, v, xdesc[k]))
		if e := ReadMsg(10, v); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	mid := int64(3)
	seq++
	t.Log(fmt.Sprintf("<Case %d> Testing: read message(%d)", seq, mid))
	if e := ReadMsg(10, mid); e != nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log(fmt.Sprintf("<Case %d> Testing: unread message(%d)", seq, mid))
	if e := unreadMsg(mid); nil != e {
		t.Error("Failed, err: ", e)
		return
	}
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- GetSysMsg --
//
func Test_GetSysMsg(t *testing.T) {
	t.Log("Test GetSysMsg")
	seq := 0

	xid := []int64{-1, 0, 100000000, 9, 6}
	xdesc := []string{"not login", "is system user", "does not exist", "is not receiver", "is not receiver"}
	for k, v := range xid {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Permission: user(%d) %s", seq, v, xdesc[k]))
		if e, _ := GetSysMsg(v, 3); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	xid = []int64{-1, 0}
	xdesc = []string{"< 1", " = 0"}
	for k, v := range xid {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Invalid Arguments: message(%d) %s", seq, v, xdesc[k]))
		if e, _ := GetSysMsg(4, v); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	xid = []int64{10}
	xdesc = []string{"any of Administrator, house agency and agency"}
	for k, v := range xid {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Testing: user(%d) can not access house private info, due to he is not %s", seq, v, xdesc[k]))
		e, msg := GetSysMsg(v, 3)
		if e != nil {
			t.Error("Failed, err: ", e)
			return
		}
		if len(msg.House.BuildingNo) > 0 || len(msg.House.HouseNo) > 0 {
			t.Error("Failed, get hosue private info, BuildingNo:", msg.House.BuildingNo, ", HouseNo:", msg.House.HouseNo)
			return
		}
	}

	xid = []int64{4 /*9, 6,*/}
	xdesc = []string{"an administrator" /*"landlord", "house agency", "agency"*/}
	for k, v := range xid {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Testing: user(%d) can access house private info, due to he is %s", seq, v, xdesc[k]))
		e, msg := GetSysMsg(v, 3)
		if e != nil {
			t.Error("Failed, err: ", e)
			return
		}
		if 0 == len(msg.House.BuildingNo) || 0 == len(msg.House.HouseNo) {
			t.Error("Failed, get hosue private info, BuildingNo:", msg.House.BuildingNo, ", HouseNo:", msg.House.HouseNo)
			return
		}
	}

	return

	mid := int64(1)
	seq++
	t.Log(fmt.Sprintf("<Case %d> Testing: message(%d)", seq, mid))
	if e, _ := GetSysMsg(4, mid); e != nil {
		t.Error("Failed, err: ", e)
		return
	}
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- GetNewMsgList --
//
func Test_GetNewMsgList(t *testing.T) {
	t.Log("Test GetNewMsgList")
	seq := 0

	// user
	xid := []int64{-1, 0, 100000000}                                   //, 9, 6}
	xdesc := []string{"not login", "is system user", "does not exist"} //, "is not receiver", "is not receiver"}
	for k, v := range xid {
		seq++
		t.Log(fmt.Sprintf("<Case %d> User(%d) %s", seq, v, xdesc[k]))
		if e, _, _ := GetNewMsgList(v, 0, 0); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// fetch range
	xid = []int64{-1, 0}
	xid1 := []int64{0, -1}
	xdesc = []string{"begin < 0", "count < 0"}
	for k, v := range xid {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Fetch range(%d, %d) %s", seq, v, xid1[k], xdesc[k]))
		if e, _, _ := GetNewMsgList(4, v, xid1[k]); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// testing
	xid = []int64{4, 5, 10}
	xid1 = []int64{1, 0, 1}
	for k, v := range xid {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Testing: user(%d) has %d new messages", seq, v, xid1[k]))
		e, total, _ := GetNewMsgList(v, 0, 0)
		if e != nil {
			t.Error("Failed, err: ", e)
			return
		}
		if total != xid1[k] {
			t.Error("total is", total)
			return
		}
	}

	for k, v := range xid {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Testing: user(%d) has %d new messages", seq, v, xid1[k]))
		e, total, _ := GetNewMsgList(v, 0, xid1[k])
		if e != nil {
			t.Error("Failed, err: ", e)
			return
		}
		if total != xid1[k] {
			t.Error("total is", total)
			return
		}
	}

	return

}
