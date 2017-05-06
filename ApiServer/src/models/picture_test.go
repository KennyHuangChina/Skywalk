package models

import (
	"ApiServer/commdef"
	"fmt"
	// "github.com/astaxie/beego"
	"testing"
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
	// seq++
	// t.Log(fmt.Sprintf("<Case %d>", seq), "Invalid Argument: PIC_TYPE_HOUSE but house is not set")
	// if e, _ := AddPicture(0, 10, commdef.PIC_TYPE_HOUSE+commdef.PIC_HOUSE_APPLIANCE, "picture desc", "ab.c", picBaseDir); e == nil {
	// 	t.Error("Failed, err: ", e)
	// 	return
	// }

	// add again, this time it should be error

	return
}
