package models

import (
	// "ApiServer/commdef"
	"fmt"
	// "strconv"
	// "github.com/astaxie/beego"
	"testing"
)

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- CertHouse --
//
func Test_CertHouse(t *testing.T) {
	t.Log("Test CertHouse")
	seq := 0

	// comment
	t.Log(fmt.Sprintf("<Case %d> Invalid argument: no comment", seq))
	if e := CertHouse(2, -1, false, ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	// House
	hids := []int64{-1, 0, 100000000}
	hdesc := []string{"< 0", "= 0", "does not exist"}
	for k, v := range hids {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Invalid argument: house (%d) %s", seq, v, hdesc[k]))
		if e := CertHouse(v, -1, false, "test"); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// house has been published
	seq++
	t.Log(fmt.Sprintf("<Case %d> Invalid argument: already published", seq))
	if e := CertHouse(2, -1, true, "test pass"); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	// user permission
	uids := []int64{-1, 0, 100000000, 2, 9}
	udesc := []string{"not login", "is SYSTEM", "does not exist", "is a regular user", "is landlord"}
	for k, v := range uids {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Permission checking: user (%d) %s", seq, v, udesc[k]))
		if e := CertHouse(2, v, false, "test"); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// processing
	uids = []int64{6, 5}
	udesc = []string{"House Agency", "Administrator"}
	for k, v := range uids {
		seq++
		t.Log(fmt.Sprintf("<Case %d> %s (%d) revoke the certification", seq, udesc[k], v))
		if e := CertHouse(2, v, false, "cancel the certification"); e != nil {
			t.Error("Failed, err: ", e)
			return
		}

		seq++
		t.Log(fmt.Sprintf("<Case %d> %s (%d) pass certification", seq, udesc[k], v))
		if e := CertHouse(2, v, true, "certification passed"); e != nil {
			t.Error("Failed, err: ", e)
			return
		}
	}
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- GetHouseCertHist --
//
func Test_GetHouseCertHist(t *testing.T) {
	t.Log("Test GetHouseCertHist")
	seq := 0

	// House
	x1 := []int64{-1, 0, 100000000}
	d1 := []string{"< 0", " = 0", "does not exist"}
	for k, v := range x1 {
		seq++
		t.Log(fmt.Sprintf("<Case %d> House (%d)  %s", seq, v, d1[k]))
		if e, _ := GetHouseCertHist(v, 0); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// User
	x1 = []int64{-1, 0, 100000000, 10}
	d1 = []string{"not login", " is a SYSTEM", "does not exist", "is not a legal user to see the house cert"}
	for k, v := range x1 {
		seq++
		t.Log(fmt.Sprintf("<Case %d> User (%d)  %s", seq, v, d1[k]))
		if e, _ := GetHouseCertHist(17, v); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	//
	x1 = []int64{9, 6, 4, 5}
	x2 := []int64{4, 4, 4, 4}
	x3 := []int64{1, 0, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 0, 1, 1, 1}
	// d1 = []string{"< 0"}
	for k, v := range x1 {
		seq++
		t.Log(fmt.Sprintf("<Case %d> House (17) has %d certification records", seq, x2[k]))
		e, hcs := GetHouseCertHist(17, v)
		if e != nil {
			t.Error("Failed, err: ", e)
			return
		}
		if x2[k] != int64(len(hcs)) {
			t.Error("hist count:", len(hcs))
			return
		}

		for k1, v1 := range hcs {
			bPrivateInfo := x3[k*4+k1]
			// beego.Debug("bPrivateInfo:", bPrivateInfo)
			if 0 != bPrivateInfo {
				if v1.Uid == 0 || 0 == len(v1.Phone) {
					t.Error("Error 1, :", fmt.Sprintf("%+v", v1))
					return
				}
			} else {
				if v1.Uid != 0 || len(v1.Phone) > 0 {
					t.Error("Error 2, :", fmt.Sprintf("%+v", v1))
					return
				}
			}
		}
	}
}
