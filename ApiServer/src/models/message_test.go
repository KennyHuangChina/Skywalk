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
		t.Log(fmt.Sprintf("<Case %d> user(%d) %s", seq, v, xdesc[k]))
		if e, _ := GetNewMsgCount(v); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	t.Error("TODO: ")

	return
}
