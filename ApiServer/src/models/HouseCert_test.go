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
		if e, _, _ := GetHouseCertHist(v, 0); e == nil {
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
		if e, _, _ := GetHouseCertHist(17, v); e == nil {
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
		e, hcs, _ := GetHouseCertHist(17, v)
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

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- RecommitHouseCert --
//
func Test_RecommitHouseCert(t *testing.T) {
	t.Log("Test_RecommitHouseCert")
	seq := 0

	// comments
	seq++
	t.Log(fmt.Sprintf("<Case %d> Arguments: comment not set", seq))
	if e, _ := RecommitHouseCert(0, 0, ""); nil == e {
		t.Error("Failed, err: ", e)
		return
	}

	// house
	x1 := []int64{-1, 0, 100000000}
	s1 := []string{"< 0", "= 0", "does not exist"}
	for k, v := range x1 {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Arguments: House(%d) %s", seq, v, s1[k]))
		if e, _ := RecommitHouseCert(v, 0, "Test_RecommitHouseCert"); nil == e {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// user
	for k, v := range x1 {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Arguments: User(%d) %s", seq, v, s1[k]))
		if e, _ := RecommitHouseCert(17, v, "Test_RecommitHouseCert"); nil == e {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// house not publish
	seq++
	t.Log(fmt.Sprintf("<Case %d> Arguments: house not published", seq))
	if e, _ := RecommitHouseCert(17, 0, "Test_RecommitHouseCert"); nil == e {
		t.Error("Failed, err: ", e)
		return
	}

	// user permission
	x1 = []int64{1, 2, 4, 5, 6, 10, 11}
	for _, v := range x1 {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Permission: User(%d) is not landlord", seq, v))
		if e, _ := RecommitHouseCert(17, v, "Test_RecommitHouseCert"); nil == e {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// house cert status
	h := int64(17)
	seq++
	t.Log(fmt.Sprintf("<Case %d> status: House(%d) status is not not correct", seq, h))
	if e, _ := RecommitHouseCert(h, 9, "Test_RecommitHouseCert"); nil == e {
		t.Error("Failed, err: ", e)
		return
	}

	h = int64(4)
	seq++
	t.Log(fmt.Sprintf("<Case %d> Testing: Recommit certification request for House(%d)", seq, h))
	e, hcid := RecommitHouseCert(h, 4, "Test_RecommitHouseCert")
	if nil != e {
		t.Error("Failed, err: ", e)
		return
	}

	// delete the house certification record just added above
	seq++
	t.Log(fmt.Sprintf("<Case %d> Testing: delete the house certification record(%d) just created above", seq, hcid))
	if e = deleHouseCertRec(hcid, nil); nil != e {
		if nil != e {
			t.Error("Failed, err: ", e)
			return
		}
	}
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- House Certification --
//
func Test_HouseCertification(t *testing.T) {
	t.Log("Test_HouseCertification")
	seq := 0

	/* Create new house for testing */
	t.Log("-- Create new house for testing --")
	hif := commdef.HouseInfo{Property: 7, BuildingNo: "999B", FloorTotal: 20, FloorThis: 16,
		HouseNo: "1608", Bedrooms: 9, Livingrooms: 8, Bathrooms: 7, Acreage: 200000,
		ForSale: true, ForRent: true, Decoration: 3, BuyDate: "2017-09-29"}
	err, nHouse := CommitHouseByOwner(&hif, 2, 11)
	if nil != err {
		t.Error("Error:", err)
		return
	}
	t.Log("new house:", nHouse)

	defer func() {
		/* Delete the house, house certifications, system messages just created above */
		t.Log(fmt.Sprintf("-- Delete house: %d --", nHouse))
		delHouse(nHouse, 5)
	}()

	/* testing */
	t.Log("-- Status: House Commited --")
	x1 := []int64{2, 11, 5}
	s1 := []string{"landlord", "agency", "administration"}
	x2 := []int{0, 0x01, 0x01}
	for k, v := range x1 {
		seq++
		t.Log(fmt.Sprintf("<Case %d> User(%d) is %s, ops: %d", seq, v, s1[k], x2[k]))
		err, _, ops := GetHouseCertHist(nHouse, v)
		if nil != err {
			t.Error("Error:", err)
			return
		}
		if ops != x2[k] {
			t.Error("ops:", ops)
			return
		}
	}

	t.Log("-- Status: Certification Failed --")
	x2 = []int{0, 1, 0}
	for k, v := range x1 {
		seq++
		t.Log(fmt.Sprintf("<Case %d> User(%d) is %s, certificate house should be %d", seq, v, s1[k], x2[k]))
		err := CertHouse(nHouse, v, false, "test 房源审核失败")
		if x2[k] > 0 {
			if nil != err {
				t.Error("Error:", err)
				return
			}
		} else {
			if nil == err {
				t.Error("Fail")
				return
			}
		}
	}

	x2 = []int{0x02, 0, 0}
	for k, v := range x1 {
		seq++
		t.Log(fmt.Sprintf("<Case %d> User(%d) is %s, ops: %d", seq, v, s1[k], x2[k]))
		err, _, ops := GetHouseCertHist(nHouse, v)
		if nil != err {
			t.Error("Error:", err)
			return
		}
		if ops != x2[k] {
			t.Error("ops:", ops)
			return
		}
	}

	//
	t.Log("-- Status: Recommit house certification --")
	x1 = []int64{11, 5, 2}
	s1 = []string{"agency", "administration", "landlord"}
	x2 = []int{0, 0, 1}
	for k, v := range x1 {
		seq++
		t.Log(fmt.Sprintf("<Case %d> User(%d) is %s, recommit should be %d", seq, v, s1[k], x2[k]))
		err, hcid := RecommitHouseCert(nHouse, v, "test 重新提交审核")
		if x2[k] > 0 {
			if nil != err {
				t.Error("Error:", err)
				return
			}
			t.Log("new house certification:", hcid)
		} else {
			if nil == err {
				t.Error("Fail")
				return
			}
		}
	}

	x1 = []int64{2, 11, 5}
	s1 = []string{"landlord", "agency", "administration"}
	x2 = []int{0, 0x01, 0x01}
	for k, v := range x1 {
		seq++
		t.Log(fmt.Sprintf("<Case %d> User(%d) is %s, ops: %d", seq, v, s1[k], x2[k]))
		err, _, ops := GetHouseCertHist(nHouse, v)
		if nil != err {
			t.Error("Error:", err)
			return
		}
		if ops != x2[k] {
			t.Error("ops:", ops)
			return
		}
	}

	t.Log("-- Status: Certification Success --")
	x1 = []int64{2, 5, 11}
	s1 = []string{"landlord", "administration", "agency"}
	x2 = []int{0, 1, 0}
	for k, v := range x1 {
		seq++
		t.Log(fmt.Sprintf("<Case %d> User(%d) is %s, certificate house should be %d", seq, v, s1[k], x2[k]))
		err := CertHouse(nHouse, v, true, "test 房源审核成功")
		if x2[k] > 0 {
			if nil != err {
				t.Error("Error:", err)
				return
			}
		} else {
			if nil == err {
				t.Error("Fail")
				return
			}
		}
	}

	x1 = []int64{2, 11, 5}
	x2 = []int{0, 0x04, 0x04}
	for k, v := range x1 {
		seq++
		t.Log(fmt.Sprintf("<Case %d> User(%d) is %s, ops: %d", seq, v, s1[k], x2[k]))
		err, _, ops := GetHouseCertHist(nHouse, v)
		if nil != err {
			t.Error("Error:", err)
			return
		}
		if ops != x2[k] {
			t.Error("ops:", ops)
			return
		}
	}

	// t.Error("TODO: ")

}
