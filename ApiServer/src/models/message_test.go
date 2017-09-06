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
//	-- GetSysMsg --
//
func Test_GetSysMsg(t *testing.T) {
	t.Log("Test GetSysMsg")
	seq := 0

	xid := []int64{-1, 0, 100000000}
	xdesc := []string{"not login", "is system user", "does not exist"}
	for k, v := range xid {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Permission: user(%d) %s", seq, v, xdesc[k]))
		if e, _ := GetSysMsg(v, 0); e == nil {
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

	mid := int64(1)
	seq++
	t.Log(fmt.Sprintf("<Case %d> Testing: message(%d)", seq, mid))
	if e, _ := GetSysMsg(4, mid); e != nil {
		t.Error("Failed, err: ", e)
		return
	}
}
