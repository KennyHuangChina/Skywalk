package models

import (
	"ApiServer/commdef"
	"fmt"
	"github.com/astaxie/beego"
	"os"
	"testing"
	"time"
)

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- AddPicture --
//
func Test_AddPicture(t *testing.T) {
	t.Log("Test AddPicture")
	// seq := 1

	t.Error("NOT IMPLEMENT")

	// t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: house < 0")
	// if e, _ := AddPicture(-1, -1, -1, "", "", "", ""); e == nil {
	// 	t.Error("Failed, err: ", e)
	// 	return
	// }

	// seq++
	// t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: house does not exist")
	// if e, _ := AddPicture(100000000, -1, -1, "", "", "", ""); e == nil {
	// 	t.Error("Failed, err: ", e)
	// 	return
	// }

	// seq++
	// t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user not login")
	// if e, _ := AddPicture(3, -1, -1, "", "", "", ""); e == nil {
	// 	t.Error("Failed, err: ", e)
	// 	return
	// }

	// seq++
	// t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user does not exist")
	// if e, _ := AddPicture(3, 100000000, -1, "", "", "", ""); e == nil {
	// 	t.Error("Failed, err: ", e)
	// 	return
	// }

	// seq++
	// t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user is a regular user")
	// if e, _ := AddPicture(3, 9, -1, "", "", "", ""); e == nil {
	// 	t.Error("Failed, err: ", e)
	// 	return
	// }

	// seq++
	// t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user is an agency, but not for this house")
	// if e, _ := AddPicture(3, 6, -1, "", "", "", ""); e == nil {
	// 	t.Error("Failed, err: ", e)
	// 	return
	// }

	// // seq++
	// // t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user is house agency")
	// // if e, _ := AddPicture(3, 4, -1, "", "", ""); e == nil {
	// // 	t.Error("Failed, err: ", e)
	// // 	return
	// // }

	// // seq++
	// // t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user is house owner")
	// // if e, _ := AddPicture(3, 10, -1, "", "", ""); e == nil {
	// // 	t.Error("Failed, err: ", e)
	// // 	return
	// // }

	// // seq++
	// // t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user is administrator")
	// // if e, _ := AddPicture(3, 5, -1, "", "", ""); e == nil {
	// // 	t.Error("Failed, err: ", e)
	// // 	return
	// // }

	// seq++
	// t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: base dir not set")
	// if e, _ := AddPicture(3, 10, -1, "", "", "", ""); e == nil {
	// 	t.Error("Failed, err: ", e)
	// 	return
	// }

	// seq++
	// t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: picture file name not set")
	// picBaseDir := "./pics/" // beego.AppConfig.String("PicBaseDir") // os.Getwd()
	// t.Log("picBaseDir:", picBaseDir)
	// if e, _ := AddPicture(3, 10, -1, "", "", "", picBaseDir); e == nil {
	// 	t.Error("Failed, err: ", e)
	// 	return
	// }

	// seq++
	// t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: picture file name do not include char '.'")
	// if e, _ := AddPicture(3, 10, -1, "", "", "abc", picBaseDir); e == nil {
	// 	t.Error("Failed, err: ", e)
	// 	return
	// }

	// seq++
	// t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: picture desc not set")
	// if e, _ := AddPicture(3, 10, -1, "", "", "ab.c", picBaseDir); e == nil {
	// 	t.Error("Failed, err: ", e)
	// 	return
	// }

	// seq++
	// t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: picture md5 not set")
	// if e, _ := AddPicture(3, 10, -1, "picture desc", "", "ab.c", picBaseDir); e == nil {
	// 	t.Error("Failed, err: ", e)
	// 	return
	// }

	// /* picture types */
	// typeMajor := 0
	// typeMinor := 0

	// seq++
	// t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: <type> major:", typeMajor, ", minor:", typeMinor)
	// if e, _ := AddPicture(3, 10, typeMajor+typeMinor, "picture desc", "abcdefghijklmn", "ab.c", picBaseDir); e == nil {
	// 	t.Error("Failed, err: ", e)
	// 	return
	// }

	// typeMajor = commdef.PIC_TYPE_USER - 1
	// seq++
	// t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: <type> major:", typeMajor, ", minor:", typeMinor)
	// if e, _ := AddPicture(3, 10, typeMajor+typeMinor, "picture desc", "abcdefghijklmn", "ab.c", picBaseDir); e == nil {
	// 	t.Error("Failed, err: ", e)
	// 	return
	// }

	// typeMajor = commdef.PIC_TYPE_USER
	// seq++
	// t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: <type> major:", typeMajor, ", minor:", typeMinor)
	// if e, _ := AddPicture(3, 10, typeMajor+typeMinor, "picture desc", "abcdefghijklmn", "ab.c", picBaseDir); e == nil {
	// 	t.Error("Failed, err: ", e)
	// 	return
	// }

	// typeMajor = commdef.PIC_TYPE_RENTAL
	// seq++
	// t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: <type> major:", typeMajor, ", minor:", typeMinor)
	// if e, _ := AddPicture(3, 10, typeMajor+typeMinor, "picture desc", "abcdefghijklmn", "ab.c", picBaseDir); e == nil {
	// 	t.Error("Failed, err: ", e)
	// 	return
	// }

	// typeMajor = commdef.PIC_TYPE_RENTAL + 1
	// seq++
	// t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: <type> major:", typeMajor, ", minor:", typeMinor)
	// if e, _ := AddPicture(3, 10, typeMajor+typeMinor, "picture desc", "abcdefghijklmn", "ab.c", picBaseDir); e == nil {
	// 	t.Error("Failed, err: ", e)
	// 	return
	// }

	// typeMajor = commdef.PIC_TYPE_HOUSE
	// typeMinor = commdef.PIC_HOUSE_BEGIN - 1
	// seq++
	// t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: <type> major:", typeMajor, ", minor:", typeMinor)
	// if e, _ := AddPicture(3, 10, typeMajor+typeMinor, "picture desc", "abcdefghijklmn", "ab.c", picBaseDir); e == nil {
	// 	t.Error("Failed, err: ", e)
	// 	return
	// }

	// typeMajor = commdef.PIC_TYPE_HOUSE
	// typeMinor = commdef.PIC_HOUSE_END + 1
	// seq++
	// t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: <type> major:", typeMajor, ", minor:", typeMinor)
	// if e, _ := AddPicture(3, 10, typeMajor+typeMinor, "picture desc", "abcdefghijklmn", "ab.c", picBaseDir); e == nil {
	// 	t.Error("Failed, err: ", e)
	// 	return
	// }

	// seq++
	// t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: PIC_TYPE_HOUSE but house is not set")
	// if e, _ := AddPicture(0, 10, commdef.PIC_TYPE_HOUSE+commdef.PIC_HOUSE_APPLIANCE, "picture desc", "abcdefghijklmn", "ab.c", picBaseDir); e == nil {
	// 	t.Error("Failed, err: ", e)
	// 	return
	// }

	// /* add picure for house */
	// seq++
	// t.Log(fmt.Sprintf("<Case %d>", seq), "Add house picture")
	// picBaseDir = "./"
	// // copy a image for testing
	// e, pfn := copyImage("./test.jpg")
	// if e != nil {
	// 	t.Error("Failed, err: ", e)
	// 	return
	// }
	// t.Log("new picture:", pfn)
	// e, nid := AddPicture(3, 10, commdef.PIC_TYPE_HOUSE+commdef.PIC_HOUSE_FURNITURE, "picture desc", "abcdefghijklmn", pfn, picBaseDir)
	// if e != nil {
	// 	t.Error("Failed, err: ", e)
	// 	DelImageFile(picBaseDir + pfn)
	// 	return
	// }
	// t.Log("new picture:", nid)

	// // // add again, this time it should be error
	// // seq++
	// // t.Log(fmt.Sprintf("<Case %d>", seq), "Add house picture again, it should be error")
	// // e, _ = AddPicture(3, 10, commdef.PIC_TYPE_HOUSE+commdef.PIC_HOUSE_FURNITURE, "picture desc", "test.jpg", picBaseDir)
	// // if e == nil {
	// // 	t.Error("Failed, err: ", e)
	// // 	return
	// // }
	// seq++
	// t.Log(fmt.Sprintf("<Case %d>", seq), "Clean test, remove picture just added")
	// if e = DelImage(nid, 4); e != nil {
	// 	t.Error("Failed, err: ", e)
	// 	return
	// }

	beego.Warn("[Test_AddPicture] TODO: adding picture for other types")

	return
}

func copyImage(src string) (err error, dest string) {

	tNow := time.Now().UnixNano()
	nfn := fmt.Sprintf("./test_%d.jpg", tNow)
	beego.Debug("src:", src, ", new file:", nfn)

	// create a new image name
	dstFile, createErr := os.Create(nfn)
	defer dstFile.Close()
	if createErr != nil {
		beego.Error(createErr)
		return
	}
	// open the source file
	srcFile, openErr := os.Open(src)
	defer srcFile.Close()
	if openErr != nil {
		beego.Error(openErr)
		return
	}

	// create a cache, 1024 byte
	buf := make([]byte, 1024)
	for {
		len, readErr := srcFile.Read(buf)
		if len == 0 {
			break // EOF
		}
		if readErr != nil {
			beego.Error(readErr)
			return
		}
		_, writeErr := dstFile.Write(buf)
		if writeErr != nil {
			beego.Error(writeErr)
			return
		}
	}

	dest = nfn
	return
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- DelImage --
//
func Test_DelImage(t *testing.T) {
	t.Log("Test DelImage")
	seq := 0

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user not login")
	if e := DelImage(-1, -1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user is system")
	if e := DelImage(-1, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: pic < 0")
	if e := DelImage(-1, 11); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: pic = 0")
	if e := DelImage(0, 11); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: pic does not exist")
	if e := DelImage(100000000, 11); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user is a regular user")
	if e := DelImage(38, 2); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user is an agency, but not for this house")
	if e := DelImage(38, 6); e == nil {
		t.Error("Failed, err: ", e)
		return
	}
	t.Log("Please ref to Test_AddPicture for actual picture deleting")

	beego.Warn("[Test_DelImage] TODO: add cases for other picture type")

	return
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- GetPicUrl --
//
func Test_GetPicUrl(t *testing.T) {
	t.Log("Test GetPicUrl")
	seq := 1

	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: picture < 0")
	if e, _, _, _ := GetPicUrl(-1, -1, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: picture = 0")
	if e, _, _, _ := GetPicUrl(0, -1, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: picture does not exist")
	if e, _, _, _ := GetPicUrl(100000000, -1, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: picture size < PIC_SIZE_ALL")
	if e, _, _, _ := GetPicUrl(50, -1, commdef.PIC_SIZE_ALL-1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: picture size > PIC_SIZE_LARGE")
	if e, _, _, _ := GetPicUrl(50, -1, commdef.PIC_SIZE_LARGE+1); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Get urls of all size")
	e, s, m, l := GetPicUrl(50, -1, commdef.PIC_SIZE_ALL)
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	t.Log("\nsmall:\t", s, "\nmiddle:\t", m, "\nlarge:\t", l)

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Get url of small")
	e, s, m, l = GetPicUrl(39, -1, commdef.PIC_SIZE_SMALL)
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	t.Log("\nsmall:\t", s, "\nmiddle:\t", m, "\nlarge:\t", l)
	if 0 == len(s) {
		t.Error("Failed, small picture not found")
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Get url of middle")
	e, s, m, l = GetPicUrl(39, -1, commdef.PIC_SIZE_MODERATE)
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	t.Log("\nsmall:\t", s, "\nmiddle:\t", m, "\nlarge:\t", l)
	// if 0 == len(m) {	// we do not generate the picture for moderate size
	// 	t.Error("Failed, middle picture not found")
	// 	return
	// }

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Get url of large")
	e, s, m, l = GetPicUrl(39, -1, commdef.PIC_SIZE_LARGE)
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	t.Log("\nsmall:\t", s, "\nmiddle:\t", m, "\nlarge:\t", l)
	if 0 == len(l) {
		t.Error("Failed, large picture not found")
		return
	}

	/* check permission for house ownership certification */
	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user not login")
	if e, _, _, _ := GetPicUrl(61, -1, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user does not exist")
	if e, _, _, _ := GetPicUrl(61, 100000000, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user is a regular user")
	if e, _, _, _ := GetPicUrl(61, 9, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user is an agency, but not behalf this house")
	if e, _, _, _ := GetPicUrl(61, 11, 0); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user is an agency who stand for this house")
	if e, _, _, _ := GetPicUrl(61, 4, 0); e != nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user is an landlord")
	if e, _, _, _ := GetPicUrl(33, 4, 0); e != nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user is an administrator")
	if e, _, _, _ := GetPicUrl(61, 5, 0); e != nil {
		t.Error("Failed, err: ", e)
		return
	}

	t.Log("**** Check permission for other picture type ****")

	return
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- GetHousePicList --
//
func Test_GetHousePicList(t *testing.T) {
	t.Log("Test GetHousePicList")
	seq := 0

	// house
	x1 := []int64{-1, 0, 100000000}
	s1 := []string{"< 0", " = 0", "does not exist"}
	for k, v := range x1 {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Invalid Argument: house(%d) %s", seq, v, s1[k]))
		if e, _ := GetHousePicList(v, -1, 0, 0); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// type
	x1 = []int64{commdef.PIC_HOUSE_ALL - 1, commdef.PIC_HOUSE_END + 1}
	for _, v := range x1 {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Invalid Argument: type(%d) <%d ~ %d>", seq, v, commdef.PIC_HOUSE_ALL, commdef.PIC_HOUSE_END))
		if e, _ := GetHousePicList(3, 4, int(v), 0); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// Permission:
	// // user
	// s1 = []string{"not login", "is SYSTEM", "does not exist"}
	// for k, v := range x1 {
	// 	seq++
	// 	t.Log(fmt.Sprintf("<Case %d> Invalid Argument: login user(%d) %s", seq, v, s1[k]))
	// 	if e, _ := GetHousePicList(3, v, 0, 0); e == nil {
	// 		t.Error("Failed, err: ", e)
	// 		return
	// 	}
	// }

	// public house pictures
	x1 = []int64{commdef.PIC_HOUSE_ALL, commdef.PIC_HOUSE_FLOOR_PLAN, commdef.PIC_HOUSE_REAL_MAP, commdef.PIC_HOUSE_FURNITURE, commdef.PIC_HOUSE_APPLIANCE}
	x2 := []int64{1, 2, 4, 5, 6, 9, 10, 11}
	s1 = []string{"is a regular user", "is a regular user", "is an admin", "is an admin", "is an agency", "is a regular user", "is a regular user", "is a agency"}
	for k1, _ := range x1 {
		for k2, _ := range x2 {
			seq++
			t.Log(fmt.Sprintf("<Case %d> Permission: type(%d) user(%d) %s", seq, x1[k1], x2[k2], s1[k2]))
			e, _ /*picLst*/ := GetHousePicList(3, x2[k2], int(x1[k1]), 0)
			if e != nil {
				t.Error("Failed, err: ", e)
				return
			}
		}
	}

	// private house pictures
	x1 = []int64{commdef.PIC_HOUSE_OwnershipCert}
	s1 = []string{"is a regular user", "is a regular user", "is an admin", "is an admin", "is an agency, but not behalf this house", "is a regular user", "is landlord", "is a agency"}
	x3 := []int64{0, 0, 4, 5, 0, 0, 10, 0} // landlord:10, agency:4
	for k1, _ := range x1 {
		for k2, _ := range x2 {
			seq++
			t.Log(fmt.Sprintf("<Case %d> Permission: type(%d) user(%d) %s, expect result(%d)", seq, x1[k1], x2[k2], s1[k2], x3[k2]))
			e, _ /*picLst*/ := GetHousePicList(3, x2[k2], int(x1[k1]), 0)
			if 0 == x3[k2] {
				if e == nil {
					t.Error("Failed, err: ", e)
					return
				}
			} else {
				if e != nil {
					t.Error("Failed, err: ", e)
					return
				}
			}
		}
	}

	// picture count
	x1 = []int64{commdef.PIC_HOUSE_ALL, commdef.PIC_HOUSE_FLOOR_PLAN, commdef.PIC_HOUSE_REAL_MAP, commdef.PIC_HOUSE_FURNITURE, commdef.PIC_HOUSE_APPLIANCE, commdef.PIC_HOUSE_OwnershipCert}
	x2 = []int64{5, -1}
	x4 := [][]int64{{17, 16}, {1, 1}, {3, 3}, {12, 12}, {1, -1}}
	s1 = []string{"admin login", "user not login"}
	for k1, _ := range x1 {
		for k2, _ := range x2 {
			seq++
			t.Log(fmt.Sprintf("<Case %d> type(%d), %s, expect pic count(%d)", seq, x1[k1], s1[k2], x4[k1][k2]))
			e, picLst := GetHousePicList(3, x2[k2], int(x1[k1]), 0)
			if x4[k1][k2] < 0 {
				if e == nil {
					t.Error("Failed, err: ", e)
					return
				}
			} else {
				if e != nil {
					t.Error("Failed, err: ", e)
					return
				}
				if x4[k1][k2] != int64(len(picLst)) {
					t.Error("Failed, len:", len(picLst))
					return
				}
			}
		}
	}
	beego.Warn("TODO: Add code here to test other kind of house picture")

	return
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- GetUserPicList --
//
func Test_GetUserPicList(t *testing.T) {
	t.Log("Test GetUserPicList")
	seq := 0

	x1 := []int64{-1, 0, 100000000}
	s1 := []string{"<0", "== 0", "does not exist"}

	// target user
	for k, v := range x1 {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Invalid Argument: target user(%d) %s", seq, v, s1[k]))
		if e, _ := GetUserPicList(v, -1, 0, 0); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// login user
	for k, v := range x1 {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Invalid Argument: login user(%d) %s", seq, v, s1[k]))
		if e, _ := GetUserPicList(11, v, 0, 0); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// sub type
	x1 = []int64{commdef.PIC_USER_ALL - 1, commdef.PIC_USER_END + 1}
	for _, v := range x1 {
		seq++
		t.Log(fmt.Sprintf("<Case %d> Invalid Argument: sub type(%d) <%d ~ %d>", seq, v, commdef.PIC_USER_ALL, commdef.PIC_USER_END))
		if e, _ := GetUserPicList(11, 4, int(v), 0); e == nil {
			t.Error("Failed, err: ", e)
			return
		}
	}

	// Permission: user portrait
	x1 = []int64{0, commdef.PIC_USER_HEAD_PORTRAIT} // picture sub type
	x2 := []int64{4, 6, 9, 11}                      // target user to check
	x3 := []int64{1, 2, 4, 5, 6, 9, 10, 11}         // login user
	for k1, _ := range x1 {
		for k2, _ := range x2 {
			for k3, _ := range x3 {
				seq++
				t.Log(fmt.Sprintf("<Case %d> Permission: type(%d), target user(%d), login user(%d)", seq, x1[k1], x2[k2], x3[k3]))
				if e, _ := GetUserPicList(x2[k2], x3[k3], int(x1[k1]), 0); e != nil {
					t.Error("Failed, err: ", e)
					return
				}
			}
		}
	}

	// Permission: user ID card or passport
	x1 = []int64{commdef.PIC_OWNER_IDCard} // picture sub type
	x2 = []int64{1, 2, 4, 6, 9, 11}        // target user to check
	x3 = []int64{1, 2, 4, 5, 6, 9, 10, 11} // login user
	x4 := [][]int{{1, 0, 1, 1, 1, 0, 0, 1}, {0, 1, 1, 1, 1, 0, 0, 1}, {0, 0, 1, 1, 1, 0, 0, 1}, {0, 0, 1, 1, 1, 0, 0, 1}, {0, 0, 1, 1, 1, 1, 0, 1}, {0, 0, 1, 1, 1, 0, 0, 1}}
	for k1, _ := range x1 {
		for k2, _ := range x2 {
			for k3, _ := range x3 {
				seq++
				t.Log(fmt.Sprintf("<Case %d> Permission: type(%d), target user(%d), login user(%d), expect result(%d)",
					seq, x1[k1], x2[k2], x3[k3], x4[k2][k3]))
				e, _ := GetUserPicList(x2[k2], x3[k3], int(x1[k1]), 0)
				if 0 == x4[k2][k3] {
					if e == nil {
						t.Error("Failed, err: ", e)
						return
					}
				} else {
					if e != nil {
						t.Error("Failed, err: ", e)
						return
					}
				}
			}
		}
	}
}
