package models

import (
	"testing"
)

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
	t.Log("Test GetUserInfo, user exist")
	uid := int64(4)
	if e, uif := GetUserInfo(uid, 1); e != nil {
		t.Error("Failed, err: ", e)
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
