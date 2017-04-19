package models

import (
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
