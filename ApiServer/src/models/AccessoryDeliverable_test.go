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
