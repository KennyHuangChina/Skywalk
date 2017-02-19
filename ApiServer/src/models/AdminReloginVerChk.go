package models

import (
	"ApiServer/commdef"
	// "crypto/md5"
	// crand "crypto/rand"
	// "encoding/base64"
	// "encoding/hex"
	"fmt"
	"github.com/astaxie/beego"
	// "io"
	// mrand "math/rand"
	// "strconv"
	// "time"
)

func userReloginVerChk(ver int, loginName, rand, sid, sig2Chk string) (err error) {
	FN := "[UserReloginVerChk] "
	beego.Trace(FN, "ver:", ver, ", loginName:", loginName, ", rand:", rand, ", sid:", sid, ", sig2Chk:", sig2Chk)

	sig := ""
	switch ver {
	case 1:
		err, sig = userReloginVerChk_1(loginName, rand, sid)
		if nil != err {
			return
		}
	default:
		err = commdef.SwError{ErrCode: commdef.ERR_LOWER_CLIENT, ErrInfo: fmt.Sprintf("ver:%d", ver)}
		return
	}

	beego.Debug(FN, "sig:", sig)
	if sig != sig2Chk {
		err = commdef.SwError{ErrCode: commdef.ERR_USER_RELOGIN}
	}

	return
}

/**
*		res = md5(loginName + rand + sid), res = md5(res) for another 22 times
*
**/
func userReloginVerChk_1(loginName, rand, sid string) (err error, sig string) {
	FN := "[userReloginVerChk_1] "

	// src := loginName + rand + sid
	// src = copy(src, loginName+rand)
	// Md5Src := make([]byte, 0, 0)
	// Md5Src = append(Md5Src, src...)
	Md5Src := loginName + rand + sid
	beego.Debug(FN, "Md5Src:", Md5Src)
	// beego.Debug(FN, "Md5Src:", string(Md5Src))

	MD5res := ""
	for i := 0; i < 23; i++ {

		beego.Debug(FN, "===> ", i+1)
		beego.Debug(FN, "Md5Src:", Md5Src)
		// beego.Debug(FN, "Md5Src:", string(Md5Src))
		MD5res = generateMD5(string(Md5Src))
		beego.Debug(FN, "MD5res:", MD5res)

		// nLen := len(MD5res)
		// beego.Debug(FN, "MD5res[:", nLen, "]:", MD5res[:nLen])
		// copy(Md5Src, MD5res[:len(MD5res)])
		Md5Src = MD5res
		// beego.Debug(FN, "Md5Src:", Md5Src)
	}

	sig = MD5res

	return
}
