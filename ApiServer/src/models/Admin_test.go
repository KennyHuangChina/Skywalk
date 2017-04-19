package models

import (
	// "github.com/astaxie/beego"
	"testing"
)

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- GetUserInfo --
//
func Test_GetUserInfo_1(t *testing.T) {
	t.Log("Test GetUserInfo, user does not exist")
	uid := int64(10000000)
	if e, uif := GetUserInfo(uid, 1); e == nil {
		t.Error("Failed, err: ", e)
	} else {
		t.Log("Pass, user:", uif)
	}
}

func Test_GetUserInfo_2(t *testing.T) {
	t.Log("Test GetUserInfo, user exist, login user is landload")
	uid := int64(4)
	if e, uif := GetUserInfo(uid, uid); e != nil || /*0 == len(uif.IdNo) ||*/ 0 == len(uif.Phone) {
		t.Error("Failed, err: ", e)
		t.Error("user:", uif)
	} else {
		t.Log("Pass, user:", uif)
	}
}

func Test_GetUserInfo_3(t *testing.T) {
	t.Log("Test GetUserInfo, user exist, login user is administrator")
	uid := int64(4)
	if e, uif := GetUserInfo(uid, 5); e != nil || /*0 == len(uif.IdNo) ||*/ 0 == len(uif.Phone) {
		t.Error("Failed, err: ", e)
		t.Error("user:", uif)
	} else {
		t.Log("Pass, user:", uif)
	}
}

func Test_GetUserInfo_4(t *testing.T) {
	t.Log("Test GetUserInfo, user exist, login user is landlord's agency")
	uid := int64(4)
	if e, uif := GetUserInfo(uid, 6); e != nil || /*0 == len(uif.IdNo) ||*/ 0 == len(uif.Phone) {
		t.Error("Failed, err: ", e)
		t.Error("user:", uif)
	} else {
		t.Log("Pass, user:", uif)
	}
}

func Test_GetUserInfo_5(t *testing.T) {
	t.Log("Test GetUserInfo, user exist, login user is nothing")
	uid := int64(4)
	if e, uif := GetUserInfo(uid, 7); e != nil || len(uif.Phone) > 0 || len(uif.IdNo) > 0 {
		t.Error("Failed, err: ", e)
		t.Error("user:", uif)
	} else {
		t.Log("Pass, user:", uif)
	}
}

func Benchmark_GetUserInfo(b *testing.B) {
	for i := 0; i < b.N; i++ { //use b.N for looping
		GetUserInfo(4, 1)
		// Div(4, 5)
	}
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- GetSaltByName --
//
func Test_GetSaltByName_1(t *testing.T) {
	t.Log("Test GetSaltByName, user not exist")
	if e, salt, rand := GetSaltByName("unknown"); e == nil {
		t.Error("err: ", e)
		t.Error("salt:", salt, ", rand:", rand)
	} else {
		t.Log("salt:", salt, ", rand:", rand)
	}
}

func Test_GetSaltByName_2(t *testing.T) {
	t.Log("Test GetSaltByName, user exist")
	if e, salt, rand := GetSaltByName("15306261804"); e != nil || 0 == len(salt) || 0 == len(rand) {
		t.Error("err: ", e)
		t.Error("salt:", salt, ", rand:", rand)
	} else {
		t.Log("salt:", salt, ", rand:", rand)
	}
}

func Benchmark_GetSaltByName(b *testing.B) {
	for i := 0; i < b.N; i++ { //use b.N for looping
		GetSaltByName("13777777777")
		// Div(4, 5)
	}
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- Logout --
//
func Test_Logout_1(t *testing.T) {
	t.Log("Test Logout, user not exist")
	if e := Logout(100000); e == nil {
		t.Error("err: ", e)
	} else {
	}
}

func Test_Logout_2(t *testing.T) {
	t.Log("Test Logout, user exist")
	if e := Logout(6); e != nil {
		t.Error("err: ", e)
	} else {
	}
}

/////////////////////////////////////////////////////////////////////////////////////////////////
//
//	-- FetchSms --
//
func Test_FetchSms_1(t *testing.T) {
	t.Log("Test FetchSms, phone number incorrect")

	// beego.Debug("phone number is empty")
	if e, sms := FetchSms(""); e == nil || len(sms) > 0 {
		t.Error("err: ", e)
	} else {
		// t.Log("sms:", sms)
	}

	// phone number is not long enough
	if e, sms := FetchSms("12345678"); e == nil || len(sms) > 0 {
		t.Error("err: ", e)
	} else {
		// t.Log("sms:", sms)
	}

	// phone number is over long
	if e, sms := FetchSms("12345678901234"); e == nil || len(sms) > 0 {
		t.Error("err: ", e)
	} else {
		// t.Log("sms:", sms)
	}

	// phone number is not digital
	if e, sms := FetchSms("1234567xyz9"); e == nil || len(sms) > 0 {
		t.Error("err: ", e)
	} else {
		// t.Log("sms:", sms)
	}

	// phone number is not a valid "Phone Number", 13x, 15x, 177x, and so on
	if e, sms := FetchSms("05306261804"); e == nil || len(sms) > 0 {
		t.Error("err: ", e)
	} else {
		// t.Log("sms:", sms)
	}
	if e, sms := FetchSms("25306261804"); e == nil || len(sms) > 0 {
		t.Error("err: ", e)
	} else {
		// t.Log("sms:", sms)
	}
}

func Test_FetchSms_2(t *testing.T) {
	t.Log("Test FetchSms, fail to post sms_code")

	// Kenny: I have no idea how to test the sms-code posting
	// 			maybe we need to cut off the network connection to sms getway, or set wrong sms getway
	// if e, sms := FetchSms("15306261804"); e == nil || len(sms) > 0 {
	// 	t.Error("err: ", e)
	// } else {
	// 	// t.Log("sms:", sms)
	// }
}

func Test_FetchSms_3(t *testing.T) {
	t.Log("Test FetchSms")

	if e, sms := FetchSms("15306261804"); e != nil || 0 == len(sms) {
		t.Error("err: ", e, ", sms:", sms)
	} else {
		t.Log("sms:", sms)
	}
}
