package models

import (
	"ApiServer/commdef"
	"crypto/md5"
	crand "crypto/rand"
	"encoding/base64"
	"encoding/hex"
	"fmt"
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/orm"
	"io"
	mrand "math/rand"
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
	// uif.Role = u.Role
	uif.Role2Desc() // uif.RoleDesc

	return
}

/**
*	Get user salt
*	Arguments:
*		un 		- user login name
*	Returns
*		err 	- error info
*		salt 	- salt string for user
*		rand	- rand for this time
 */
func GetSaltByName(un string) (err error, salt, rand string) {
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

	err, r := newUserSalt()
	if nil != err {
		return
	}

	salt = user.Salt
	rand = r

	return
}

/**
*	User relogin automatically when session expired, by previous session id
*	Parameters:
*		ver			- version number
*		loginName	- login name
*		sid			- session id when last login
*		rand		- random
*	Return Values:
*		err		- error info
*		uid		- actual user id.
*					> 0: point to a acutal user, = 0 point to "system",
*					< 0: user not exist
 */
func Relogin(ver int, loginName, rand, sid string) (err error, uid int64) {
	FN := "[Relogin] "
	beego.Debug(FN, "sid:", sid, ", ver:", ver, ", loginName:", loginName, ", rand:", rand)

	defer func() {
		if nil != err {
			uid = -1
			beego.Error(FN, err)
		}
	}()

	/* arguments checking */
	if 0 == len(loginName) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: "login name could not be empty"}
		return
	}
	if 0 == len(rand) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: "random not set"}
		return
	}
	if 0 == len(sid) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: "session not set"}
		return
	}

	/* processing */
	o := orm.NewOrm()

	user := TblUser{LoginName: loginName}
	if errT := o.Read(&user, "LoginName"); nil != errT {
		// beego.Error(FN, errT)
		if orm.ErrNoRows == errT || orm.ErrMissPK == errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_RES_NOTFOUND, ErrInfo: fmt.Sprintf("user:", loginName)}
		} else {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		}
		return
	}
	if !user.Enable {
		err = commdef.SwError{ErrCode: commdef.ERR_USER_NOT_ENABLE}
		return
	}

	if err = userReloginVerChk(ver, loginName, rand, user.Session, sid); nil != err {
		return
	}

	uid = user.Id
	return
}

/**
*	Login by user & password
*	Parameters:
*		loginName
*		passwd
*		rand
*	Return Values:
*		err		- error info
*		uid		- actual user id.
*					> 0: point to a acutal user, = 0 point to "system",
*					< 0: user not exist
 */
func LoginByPass(loginName, passwd, rand, sid string) (err error, uid int64) {
	FN := "[LoginByPass] "
	beego.Debug(FN, "loginName:", loginName, ", passwd:", passwd, ", rand:", rand, ", sid:", sid)

	pwd, _ := base64.URLEncoding.DecodeString(passwd)
	// beego.Debug(FN, "pwd:", pwd)
	passwd = string(pwd)
	beego.Debug(FN, "passwd:", passwd)

	defer func() {
		if nil != err {
			uid = -1
			beego.Error(FN, err)
		}
	}()

	/* agrguments checking */

	/* Processing */
	o := orm.NewOrm()

	// check if the password user keyined is empty
	if 0 == len(passwd) {
		// TODO: actually, the password the server received could not be empy
		//	cause the password is encoded by password + salt,
		//	so even the password the user keyin is actually empty, the passwd we received is still a NONE empty string
		//	so, if we need to detect if the password is empty, we need to do more further checking
		err = commdef.SwError{ErrCode: commdef.ERR_USERLOGIN_NO_PASSWORD}
		return
	}

	user := TblUser{LoginName: loginName}
	if err1 := o.Read(&user, "LoginName"); nil != err1 {
		// beego.Error(FN, err1)
		if orm.ErrNoRows == err1 || orm.ErrMissPK == err1 {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_RES_NOTFOUND, ErrInfo: fmt.Sprintf("user:", loginName)}
		} else {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: err1.Error()}
		}
		return
	}
	if !user.Enable {
		err = commdef.SwError{ErrCode: commdef.ERR_USER_NOT_ENABLE}
		return
	}

	uid = user.Id
	// beego.Debug(FN, "user.Pass:", user.PassLogin, ", user.Salt:", user.Salt)

	// check if the password user keyined is empty
	// md5(md5(pass+salt)+random)
	hasher1 := md5.New()
	hasher1.Write([]byte(user.Salt))
	hasher1.Write([]byte(""))
	rdHashPass1 := hex.EncodeToString(hasher1.Sum(nil))

	hasher2 := md5.New()
	hasher2.Write([]byte(rdHashPass1))
	hasher2.Write([]byte(rand))
	rdHashPass2 := hex.EncodeToString(hasher2.Sum(nil))
	// beego.Debug(FN, "rdHashPass2:", rdHashPass2, ", passwd:", passwd)
	if rdHashPass2 == passwd {
		// beego.Error(FN_NAME, "password couldn't be empty")
		err = commdef.SwError{ErrCode: commdef.ERR_USERLOGIN_INCORRECT_PASSWORD, ErrInfo: "Empty password"}
		return
	}

	hasher := md5.New() // md5(pass+random)
	hasher.Write([]byte(user.PassLogin))
	hasher.Write([]byte(rand))
	rdHashPath := hex.EncodeToString(hasher.Sum(nil))

	// beego.Debug(FN, "rdHashPath:", rdHashPath, ", passwd:", passwd)
	if passwd != rdHashPath {
		beego.Error(FN, "rdHashPath:", rdHashPath, ", passwd:", passwd)
		err = commdef.SwError{ErrCode: commdef.ERR_USERLOGIN_INCORRECT_PASSWORD}
		return
	}

	// record the session for login
	user.Session = sid
	/*numb _, errTmp =*/ o.Update(&user, "Session")

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
			beego.Error(FN, err)
		}
		beego.Debug(FN, "SmsCode:", SmsCode)
	}()

	// argument checking
	if err = checkPhoneNo(login_name); nil != err {
		return
	}

	// generate new sms code
	sms := generateSmsCode()

	// send sms code via sms vendor's getway
	if err = postSms(login_name, sms); nil != err {
		return
	}

	// record for using
	o := orm.NewOrm()

	s := TblSmsCode{Phone: login_name}
	errTmp := o.Read(&s, "Phone")
	bFound := false
	if nil != errTmp {
		if orm.ErrNoRows != errTmp && orm.ErrMissPK != errTmp {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errTmp.Error()}
			return
		}
		// sms code does not exist, append
	} else {
		// already exist, just update
		bFound = true
	}

	tNow := time.Now()
	tExpire := tNow.Add(time.Duration(600) * time.Second) // 10 minutes timeout
	// beego.Debug(FN, "tExpire:", tExpire.String(), ", ", tExpire.UTC().String())
	s.Expire = tExpire //.UTC()
	s.SmsCode = sms

	if bFound {
		/*numb*/ _, errTmp = o.Update(&s, "SmsCode", "Expire")
	} else {
		/*id*/ _, errTmp = o.Insert(&s)

		var sms_tmp TblSmsCode
		o.Raw("SELECT * FROM tbl_sms_code").QueryRow(&sms_tmp)
		beego.Debug(FN, "sms_tmp:", sms_tmp)

		// orm.DefaultTimeLoc = time.UTC
		// orm.NewOrm().Raw("SELECT * FROM tbl_sms_code").QueryRow(&sms_tmp)
		// beego.Debug(FN, "sms_tmp:", sms_tmp)
	}
	if nil != errTmp {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errTmp.Error()}
		return
	}

	SmsCode = sms
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
			// user.Role = commdef.USER_TYPE_Customer

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

/**
*	Check user
*	Arguments
*		uid		- user id
*	Returns
*		err		- error
**/
func CheckUser(uid int64) (err error) {
	// FN := "[CheckUser] "

	if uid < 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("uid:%d", uid)}
		return
	}

	o := orm.NewOrm()
	u := TblUser{Id: uid}
	errT := o.Read(&u)
	if nil != errT {
		if orm.ErrNoRows == errT || orm.ErrMissPK == errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_RES_NOTFOUND, ErrInfo: fmt.Sprintf("uid:%d", uid)}
		} else {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		}
		return
	}

	if !u.Enable {
		err = commdef.SwError{ErrCode: commdef.ERR_USER_NOT_ENABLE}
		return
	}

	return
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//		Internal Functions
//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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

	defer func() {
		// remove the sms record, it is for one-time using
		num, errTmp := o.Delete(&TblSmsCode{Id: s.Id})
		beego.Debug(FN, "num:", num, ", errtmp:", errTmp)
	}()

	if sms != s.SmsCode { // incorrect sms code
		err = commdef.SwError{ErrCode: commdef.ERR_SMS_WRONG_CODE}
		return
	}

	timeNow := time.Now()
	beego.Debug(FN, "timeNow:", timeNow, ", Expire:", s.Expire, ", expired:", timeNow.After(s.Expire))
	beego.Debug(FN, "timeNowUTC:", timeNow.UTC(), ", ExpireUTC:", s.Expire.UTC(), ", after:", timeNow.UTC().After(s.Expire.UTC()))
	// beego.Debug(FN, "timeNow.Local:", timeNow.Local(), ", Expire.Local:", s.Expire.Local(), ", after:", timeNow.Local().After(s.Expire.Local()))
	// beego.Debug(FN, "timeNowUTC:", timeNow.UTC(), ", Expire:", s.Expire, ", after:", timeNow.UTC().After(s.Expire))
	if timeNow.UTC().After(s.Expire.UTC()) { // timeNow > s.Expire
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
	_, ioerr := io.ReadFull(crand.Reader, salt)
	if nil != ioerr {
		err = commdef.SwError{ErrCode: commdef.ERR_SYS_IO_READ, ErrInfo: ioerr.Error()}
		return
	}

	// hasher := md5.New()
	ss = hex.EncodeToString(salt)
	beego.Debug(FN, "new salt:", ss, ",", salt)
	return
}

/*
*	Generate new SMS code
 */
func generateSmsCode() (smsCode string) {
	FN := "[generateSmsCode] "

	smsCode = ""
	r := mrand.New(mrand.NewSource(time.Now().UnixNano()))
	rand := r.Intn(999999)
	smsCode = fmt.Sprintf("%.6d", rand)
	beego.Debug(FN, "smsCode:", smsCode)

	return
}

/*
*	delivery sms code via sms vendoer's getway
 */
func postSms(phone, sms string) (err error) {
	FN := "[postSms] "

	beego.Warn(FN, "NOT Implement")
	// err = commdef.SwError{ErrCode: commdef.ERR_NOT_IMPLEMENT}

	return
}

/**
*	Check if the specified user is a agency
*	Arguments
*		uid		- user id
*	Returns
*		err		- error
*		agency	- true: is an agency; false: isn't an agency
**/
func isAgency(uid int64) (err error, agency bool) {
	FN := "[isAgency] "

	err = CheckUser(uid)
	if nil != err {
		return
	}

	// check if the user in agency group
	sql := fmt.Sprintf("SELECT COUNT(*) FROM tbl_user_group AS g, tbl_user_group_member AS m WHERE m.group_id=g.id AND admin=false AND user_id=%d", uid)
	nCnt := int64(0)

	o := orm.NewOrm()
	errT := o.Raw(sql).QueryRow(&nCnt)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}
	beego.Debug(FN, "nCnt:", nCnt)

	if nCnt > 0 {
		agency = true
	}

	return
}

/**
*	Check if the specified user is a administrator
*	Arguments
*		uid		- user id
*	Returns
*		err		- error
*		admin	- true: is an administrator; false: isn't an administrator
**/
func isAdministrator(uid int64) (err error, admin bool) {
	FN := "[isAdministrator] "

	err = CheckUser(uid)
	if nil != err {
		return
	}

	// check if the user in agency group
	sql := fmt.Sprintf("SELECT COUNT(*) FROM tbl_user_group AS g, tbl_user_group_member AS m WHERE m.group_id=g.id AND admin=true AND user_id=%d", uid)
	nCnt := int64(0)

	o := orm.NewOrm()
	errT := o.Raw(sql).QueryRow(&nCnt)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}
	beego.Debug(FN, "nCnt:", nCnt)

	if nCnt > 0 {
		admin = true
	}

	return
}
