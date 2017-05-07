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
	seq := 1

	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: house < 0")
	if e, _ := AddPicture(-1, -1, -1, "", "", ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: house does not exist")
	if e, _ := AddPicture(100000000, -1, -1, "", "", ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user not login")
	if e, _ := AddPicture(3, -1, -1, "", "", ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user does not exist")
	if e, _ := AddPicture(3, 100000000, -1, "", "", ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user is a regular user")
	if e, _ := AddPicture(3, 9, -1, "", "", ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user is an agency, but not for this house")
	if e, _ := AddPicture(3, 6, -1, "", "", ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	// seq++
	// t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user is house agency")
	// if e, _ := AddPicture(3, 4, -1, "", "", ""); e == nil {
	// 	t.Error("Failed, err: ", e)
	// 	return
	// }

	// seq++
	// t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user is house owner")
	// if e, _ := AddPicture(3, 10, -1, "", "", ""); e == nil {
	// 	t.Error("Failed, err: ", e)
	// 	return
	// }

	// seq++
	// t.Log(fmt.Sprintf("<Case %d>", seq), "Permission: user is administrator")
	// if e, _ := AddPicture(3, 5, -1, "", "", ""); e == nil {
	// 	t.Error("Failed, err: ", e)
	// 	return
	// }

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: base dir not set")
	if e, _ := AddPicture(3, 10, -1, "", "", ""); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: picture file name not set")
	picBaseDir := "./pics/" // beego.AppConfig.String("PicBaseDir") // os.Getwd()
	t.Log("picBaseDir:", picBaseDir)
	if e, _ := AddPicture(3, 10, -1, "", "", picBaseDir); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: picture file name do not include char '.'")
	if e, _ := AddPicture(3, 10, -1, "", "abc", picBaseDir); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: picture desc not set")
	if e, _ := AddPicture(3, 10, -1, "", "ab.c", picBaseDir); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	/* picture types */
	typeMajor := 0
	typeMinor := 0

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: <type> major:", typeMajor, ", minor:", typeMinor)
	if e, _ := AddPicture(3, 10, typeMajor+typeMinor, "picture desc", "ab.c", picBaseDir); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	typeMajor = commdef.PIC_TYPE_USER - 1
	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: <type> major:", typeMajor, ", minor:", typeMinor)
	if e, _ := AddPicture(3, 10, typeMajor+typeMinor, "picture desc", "ab.c", picBaseDir); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	typeMajor = commdef.PIC_TYPE_USER
	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: <type> major:", typeMajor, ", minor:", typeMinor)
	if e, _ := AddPicture(3, 10, typeMajor+typeMinor, "picture desc", "ab.c", picBaseDir); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	typeMajor = commdef.PIC_TYPE_RENTAL
	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: <type> major:", typeMajor, ", minor:", typeMinor)
	if e, _ := AddPicture(3, 10, typeMajor+typeMinor, "picture desc", "ab.c", picBaseDir); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	typeMajor = commdef.PIC_TYPE_RENTAL + 1
	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: <type> major:", typeMajor, ", minor:", typeMinor)
	if e, _ := AddPicture(3, 10, typeMajor+typeMinor, "picture desc", "ab.c", picBaseDir); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	typeMajor = commdef.PIC_TYPE_HOUSE
	typeMinor = commdef.PIC_HOUSE_FLOOR - 1
	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: <type> major:", typeMajor, ", minor:", typeMinor)
	if e, _ := AddPicture(3, 10, typeMajor+typeMinor, "picture desc", "ab.c", picBaseDir); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	typeMajor = commdef.PIC_TYPE_HOUSE
	typeMinor = commdef.PIC_HOUSE_APPLIANCE + 1
	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: <type> major:", typeMajor, ", minor:", typeMinor)
	if e, _ := AddPicture(3, 10, typeMajor+typeMinor, "picture desc", "ab.c", picBaseDir); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: PIC_TYPE_HOUSE but house is not set")
	if e, _ := AddPicture(0, 10, commdef.PIC_TYPE_HOUSE+commdef.PIC_HOUSE_APPLIANCE, "picture desc", "ab.c", picBaseDir); e == nil {
		t.Error("Failed, err: ", e)
		return
	}

	/* add picure for house */
	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Add house picture")
	picBaseDir = "./"
	// copy a image for testing
	e, pfn := copyImage("./test.jpg")
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	t.Log("new picture:", pfn)
	e, nid := AddPicture(3, 10, commdef.PIC_TYPE_HOUSE+commdef.PIC_HOUSE_FURNITURE, "picture desc", pfn, picBaseDir)
	if e != nil {
		t.Error("Failed, err: ", e)
		return
	}
	t.Log("new picture:", nid)

	// // add again, this time it should be error
	// seq++
	// t.Log(fmt.Sprintf("<Case %d>", seq), "Add house picture again, it should be error")
	// e, _ = AddPicture(3, 10, commdef.PIC_TYPE_HOUSE+commdef.PIC_HOUSE_FURNITURE, "picture desc", "test.jpg", picBaseDir)
	// if e == nil {
	// 	t.Error("Failed, err: ", e)
	// 	return
	// }
	seq++
	t.Log(fmt.Sprintf("<Case %d>", seq), "Clean test, remove picture just added")
	// e, _ = AddPicture(3, 10, commdef.PIC_TYPE_HOUSE+commdef.PIC_HOUSE_FURNITURE, "picture desc", "test.jpg", picBaseDir)
	// if e == nil {
	// 	t.Error("Failed, err: ", e)
	// 	return
	// }

	beego.Warn("[Test_AddPicture] adding picture for other types")

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
