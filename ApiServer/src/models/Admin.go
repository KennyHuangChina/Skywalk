package models

import (
	"ApiServer/commdef"
	"crypto/md5"
	"crypto/rand"
	"encoding/hex"
	// "fmt"
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/orm"
	"io"
	"strconv"
	"time"
)

const (
	c_PW_SALT_BYTES = 16
	c_PW_HASH_BYTES = 32
)

/**
*	Get user information by id
*	Arguments:
*		id - user id
*	Returns
*		err - error info
*		uif - user info
 */
func GetUserInfo(id int64) (err error, uif commdef.UserInfo) {
	FN := "[GetUserInfo] "
	beego.Trace(FN, "id:", id)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	o := orm.NewOrm()

	u := TblUser{Id: id}
	if err1 := o.Read(&u); nil != err1 {
		// beego.Error(FN, err1)
		if orm.ErrNoRows == err1 || orm.ErrMissPK == err1 {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_RES_NOTFOUND, ErrInfo: err1.Error()}
		} else {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: err1.Error()}
		}
		return
	}

	uif.Id = u.Id
	uif.Name = u.Name
	uif.IdNo = u.IdNo
	uif.Phone = u.Phone
	uif.HeadPortrait = u.Head
	uif.Role = u.Role
	uif.Role2Desc() // uif.RoleDesc

	return
}

/**
*	Get user salt
*	Arguments:
*		un - user login name
*	Returns
*		err 	- error info
*		salt 	- salt string for user
 */
func GetSaltByName(un string) (err error, salt string) {
	FN := "[GetSaltByName] "
	beego.Trace(FN, "login user:", un)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	o := orm.NewOrm()

	user := TblUser{LoginName: un}
	if err1 := o.Read(&user, "LoginName"); nil != err1 {
		// beego.Error(FN, err1)
		if orm.ErrNoRows == err1 || orm.ErrMissPK == err1 {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_RES_NOTFOUND, ErrInfo: err1.Error()}
		} else {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: err1.Error()}
		}
		return
	}

	salt = user.Salt

	return
}

/**
*	Get user id by name
*	Parameters:
*		loginName
*		passwd
*		random
*	Return Values:
*		err		- error info
*		uid		- actual user id.
*					> 0: point to a acutal user, = 0 point to "system",
*					< 0: user not exist
 */
func LoginAccout(loginName, passwd, random string) (err error, uid int64) {
	FN := "[LoginAccout] "

	uid = -1

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	// check if the password user keyined is empty
	if 0 == len(passwd) {
		// TODO: actually, the password the server received could not be empy
		//	cause the password is encoded by password + salt,
		//	so even the password the user keyin is actually empty, the passwd we received is still a NONE empty string
		//	so, if we need to detect if the password is empty, we need to do more further checking
		err = commdef.SwError{ErrCode: commdef.ERR_USERLOGIN_NO_PASSWORD}
		return
	}

	o := orm.NewOrm()

	user := TblUser{LoginName: loginName}
	if err1 := o.Read(&user, "LoginName"); nil != err1 {
		// beego.Error(FN, err1)
		if orm.ErrNoRows == err1 || orm.ErrMissPK == err1 {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_RES_NOTFOUND, ErrInfo: err1.Error()}
		} else {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: err1.Error()}
		}
		return
	}

	beego.Debug(FN, "user.Pass:", user.PassLogin, ", user.Salt:", user.Salt)

	// check if the password user keyined is empty
	// md5(md5(pass+salt)+random)
	hasher1 := md5.New()
	hasher1.Write([]byte(user.Salt))
	hasher1.Write([]byte(""))
	rdHashPass1 := hex.EncodeToString(hasher1.Sum(nil))
	hasher2 := md5.New()
	hasher2.Write([]byte(rdHashPass1))
	hasher2.Write([]byte(random))
	rdHashPass2 := hex.EncodeToString(hasher2.Sum(nil))
	beego.Debug(FN, "rdHashPass2:", rdHashPass2, ", passwd:", passwd)
	if rdHashPass2 == passwd {
		err = commdef.SwError{ErrCode: commdef.ERR_USERLOGIN_INCORRECT_PASSWORD}
		return
	}

	hasher := md5.New() // md5(pass+random)
	hasher.Write([]byte(user.PassLogin))
	hasher.Write([]byte(random))
	rdHashPath := hex.EncodeToString(hasher.Sum(nil))

	beego.Debug(FN, "passwd:", passwd)
	beego.Debug(FN, "rdHashPath:", rdHashPath)
	if passwd != rdHashPath {
		err = commdef.SwError{ErrCode: commdef.ERR_USERLOGIN_INCORRECT_PASSWORD}
		return
	}

	uid = user.Id
	return
}

/**
*	Get user salt
*	Arguments:
*		login_name 	- login name
*	Returns
*		err 		- error info
*		SmsCode 	- sms code sent
 */
func FetchSms(login_name string) (err error, SmsCode string) {
	FN := "[FetchSms] "
	beego.Trace(FN, "login user:", login_name)

	SmsCode = ""

	defer func() {
		if nil != err {
			se, _ := err.(commdef.SwError)
			se.FillError()
			err = se
			beego.Error(FN, err)
		}
	}()

	// argument checking
	if err = checkPhoneNo(login_name); nil != err {
		return
	}

	err = commdef.SwError{ErrCode: commdef.ERR_NOT_IMPLEMENT}
	// o := orm.NewOrm()

	// user := TblUser{LoginName: un}
	// if err1 := o.Read(&user, "LoginName"); nil != err1 {
	// 	// beego.Error(FN, err1)
	// 	if orm.ErrNoRows == err1 || orm.ErrMissPK == err1 {
	// 		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_RES_NOTFOUND, ErrInfo: err1.Error()}
	// 	} else {
	// 		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: err1.Error()}
	// 	}
	// 	return
	// }

	// salt = user.Salt

	return
}

/**
*	Login by phone number + sms code, if phone does not exist, create new user
*	Arguments:
*		login_name 	- login name
*		sms			- sms code
*	Returns
*		err 	- error info
*		uid 	- user id
 */
func LoginSms(login_name, sms string) (err error, uid int64) {
	FN := "[loginsms] "
	beego.Trace(FN, "login user:", login_name)

	uid = 0

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
		beego.Debug(FN, "uid:", uid)
	}()

	// argument checking
	if err = checkPhoneNo(login_name); nil != err {
		return
	}

	// check if the sms code is valid
	if err = checkSms(login_name, sms); nil != err {
		return
	}

	o := orm.NewOrm()

	user := TblUser{LoginName: login_name}
	err1 := o.Read(&user, "LoginName")
	if nil != err1 {
		if orm.ErrNoRows != err1 && orm.ErrMissPK != err1 {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: err1.Error()}
			return
		} else {
			beego.Debug(FN, "user:", login_name, "does not exist, create for it")
			err, user.Salt = newUserSalt()
			if nil != err {
				return
			}
			user.LoginName = login_name
			user.Phone = login_name
			user.Role = commdef.USER_TYPE_Customer

			id, errTmp := o.Insert(&user)
			if nil != errTmp {
				err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errTmp.Error()}
				return
			}
			uid = id
		}
	} else {
		uid = user.Id
	}

	return
}

/////////////////////////////////////////////////////////////////////////////////////////
//
//		Internal Functions
//
func checkPhoneNo(phone string) (err error) {
	FN := "[checkPhoneNo] "
	beego.Trace(FN, "phone:", phone)

	if lenLN := len(phone); 11 != lenLN {
		if 0 == lenLN {
			err = commdef.SwError{ErrCode: commdef.ERR_SMS_EMPTY_PHONE}
		} else if 11 != len(phone) { //
			err = commdef.SwError{ErrCode: commdef.ERR_SMS_PHONE_LEN}
		}
		return
	}

	nPhoneNo, err1 := strconv.ParseInt(phone, 10, 64)
	if nil != err1 {
		err = commdef.SwError{ErrCode: commdef.ERR_SMS_PHONE_NO_NUMERIC, ErrInfo: err1.Error()}
		return
	}

	// China. cell phone start with number 1, it could be 13, 15, 17, 18, or maybe more in the feature
	if nPhoneNo < 10000000000 || nPhoneNo >= 20000000000 {
		err = commdef.SwError{ErrCode: commdef.ERR_SMS_INVALID_PHONE_NO}
		return
	}

	return
}

/*
 *	create new salt for user
 */
func checkSms(phone, sms string) (err error) {
	FN := "[checkSms] "
	beego.Trace(FN, "phone:", phone, ", sms:", sms)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	o := orm.NewOrm()
	s := TblSmsCode{Phone: phone}

	if errTmp := o.Read(&s, "Phone"); nil != errTmp { // sms code does not exist
		if orm.ErrNoRows == errTmp || orm.ErrMissPK == errTmp {
			err = commdef.SwError{ErrCode: commdef.ERR_SMS_NOT_FOUND}
		} else {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errTmp.Error()}
		}
		return
	}

	if sms != s.SmsCode { // incorrect sms code
		err = commdef.SwError{ErrCode: commdef.ERR_SMS_WRONG_CODE}
		return
	}

	timeNow := time.Now()
	beego.Debug(FN, "timeNow:", timeNow, ", Expire:", s.Expire, ", expired:", timeNow.After(s.Expire))
	beego.Debug(FN, "timeNowUTC:", timeNow.UTC(), ", ExpireUTC:", s.Expire.UTC(), ", after:", timeNow.UTC().After(s.Expire.UTC()))
	// beego.Debug(FN, "timeNow.Local:", timeNow.Local(), ", Expire.Local:", s.Expire.Local(), ", after:", timeNow.Local().After(s.Expire.Local()))
	// beego.Debug(FN, "timeNowUTC:", timeNow.UTC(), ", Expire:", s.Expire, ", after:", timeNow.UTC().After(s.Expire))
	if timeNow.After(s.Expire) { // timeNow > s.Expire
		err = commdef.SwError{ErrCode: commdef.ERR_SMS_EXPIRED}
		return
	}

	return
}

/*
 *	create new salt for user
 */
func newUserSalt() (err error, ss string) {
	FN := "[newUserSalt] "
	// beego.Trace(FN, "phone:", phone)

	salt := make([]byte, c_PW_SALT_BYTES)
	_, ioerr := io.ReadFull(rand.Reader, salt)
	if nil != ioerr {
		err = commdef.SwError{ErrCode: commdef.ERR_SYS_IO_READ, ErrInfo: ioerr.Error()}
		return
	}

	// hasher := md5.New()
	ss = hex.EncodeToString(salt)
	beego.Debug(FN, "new salt:", ss, ",", salt)
	return
}
