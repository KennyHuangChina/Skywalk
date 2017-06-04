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
*		id 	- user id to fetch
*		ln	- login user. 	> 0: real login user; = 0: system; < 0: no user specified
*	Returns
*		err - error info
*		uif - user info
 */
func GetAgencyList(begin, cnt int) (err error, total int64, agencys []commdef.AgencyInfo) {
	FN := "[GetAgencyList] "
	beego.Trace(FN, "begin:", begin, ", cnt:", cnt)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/* Argument checking */

	/* Permission checking */

	/* Processing */
	// calculate the count
	o := orm.NewOrm()
	sql := `SELECT COUNT(*) AS count
				FROM tbl_user_group_member AS gm, tbl_user AS u, tbl_user_group AS g 
				WHERE u.Enable AND u.id = gm.user_id AND gm.group_id = g.id AND g.admin`
	var Count int64
	errT := o.Raw(sql).QueryRow(&Count)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	total = Count
	if 0 == total || 0 == cnt { // user just want to calculate the total number
		return
	}

	if int64(begin) >= total {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("begin position: %d", begin)}
		return
	}

	// get real records
	sql = `SELECT u.id, u.name, u.id_no, u.phone, u.head AS head_portrait
				FROM tbl_user_group_member AS gm, tbl_user AS u, tbl_user_group AS g 
				WHERE u.Enable AND u.id = gm.user_id AND gm.group_id = g.id AND g.admin
				LIMIT ?, ?`
	var as []commdef.AgencyInfo
	numb, errT := o.Raw(sql, begin, cnt).QueryRows(&as)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}
	beego.Debug(FN, numb, "records found")

	agencys = as
	nameUnset := getSpecialString(KEY_USER_NAME_NOT_SET) // "未设置"
	for k, _ := range agencys {
		if 0 == len(agencys[k].Name) {
			agencys[k].Name = nameUnset
			// beego.Debug(FN, k, ":", agencys[k].Name)
		}
	}

	return
}

/**
*	Get user information by id
*	Arguments:
*		id 	- user id to fetch
*		ln	- login user. 	> 0: real login user; = 0: system; < 0: no user specified
*	Returns
*		err - error info
*		uif - user info
 */
func GetUserInfo(id, ln int64) (err error, uif commdef.UserInfo) {
	FN := "[GetUserInfo] "
	beego.Trace(FN, "id:", id, ", ln:", ln)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	err, u := GetUser(id)
	if nil != err {
		return
	}

	// permission checking
	// Only the house owner himself, his agency and administrator could view the private info,
	//	like user's phone and login name which actually is user's phone number, id number
	//	all other people could just view the user's public info, including name, head portaint, role
	bPrivacy := false
	if id == ln { // login user is landlord himself
		beego.Debug(FN, "landlord himself")
		bPrivacy = true
	} else if _, bAdmin := isAdministrator(ln); bAdmin { // login user is administrator
		beego.Debug(FN, "admin")
		bPrivacy = true
	} else if bAgency := isOwnerAgency(id, ln); bAgency { // login user is agency of this landlord
		beego.Debug(FN, "agency")
		bPrivacy = true
	}

	uif.Id = u.Id
	uif.Name = u.Name
	if 0 == len(uif.Name) {
		uif.Name = getSpecialString(KEY_USER_NAME_NOT_SET) // "未设置"
	}
	uif.HeadPortrait = u.Head
	// uif.Role = u.Role
	uif.Role2Desc() // uif.RoleDesc

	if bPrivacy {
		beego.Debug(FN, "bPrivacy:", bPrivacy)
		uif.IdNo = u.IdNo
		uif.Phone = u.Phone
	}
	beego.Debug(FN, fmt.Sprintf("user info:%+v", uif))

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
*		newSid		- current session
*	Return Values:
*		err		- error info
*		uid		- actual user id.
*					> 0: point to a acutal user, = 0 point to "system",
*					< 0: user not exist
 */
func Relogin(ver int, loginName, rand, sid, newSid string) (err error, uid int64) {
	FN := "[Relogin] "
	beego.Debug(FN, "sid:", sid, ", ver:", ver, ", loginName:", loginName, ", rand:", rand, ", newSid:", newSid)

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
	if 0 == len(user.Session) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_NOT_LOGIN}
		return
	}

	if err = userReloginVerChk(ver, loginName, rand, user.Session, sid); nil != err {
		return
	}

	uid = user.Id

	// update session for relogin
	err = updateUserSession(uid, newSid)
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
	err = updateUserSession(uid, sid)
	return
}

/**
*	Loout
*	Parameters:
*		uid		- user id
*	Return Values:
*		err		- error info
 */
func Logout(uid int64) (err error) {
	FN := "[Logout] "
	beego.Debug(FN, "uid:", uid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/* agrguments checking */

	/* Processing */
	// Kenny: updateUserSession will check user by itself
	// if err, _ = GetUser(uid); nil != err {
	// 	return
	// }

	// clear the session for login
	err = updateUserSession(uid, "")

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
		// beego.Debug(FN, "SmsCode:", SmsCode)
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
*		Sid			- login session id
*	Returns
*		err 	- error info
*		uid 	- user id
 */
func LoginSms(login_name, sms, Sid string) (err error, uid int64) {
	FN := "[loginsms] "
	beego.Trace(FN, "login user:", login_name, ", sms:", sms, ", sid:", Sid)

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

	beego.Warn(FN, "NOT IMPLEMENT")

	return
}

/**
*	get user info by id
*	Arguments
*		uid		- user id
*	Returns
*		err		- error
*		user	- user info
**/
func GetUser(uid int64) (err error, user TblUser) {
	// FN := "[GetUser] "

	if uid < 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_NOT_LOGIN, ErrInfo: fmt.Sprintf("uid:%d", uid)}
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

	user = u
	return
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//		Internal Functions
//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
func updateUserSession(uid int64, sid string) (err error) {

	o := orm.NewOrm()

	user := TblUser{Id: uid}
	errT := o.Read(&user)
	if nil != errT {
		if orm.ErrNoRows == errT || orm.ErrMissPK == errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_RES_NOTFOUND, ErrInfo: fmt.Sprintf("uid:%d", uid)}
		} else {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		}
		return
	}

	user.Session = sid
	/*numb*/ _, errT = o.Update(&user, "Session")
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("Fail to update session:%s for user:%d, err:%s", sid, uid, errT.Error())}
	}

	return
}

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

	beego.Warn(FN, "NOT Implement, please modify test code once it get implements")
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

	err, _ = GetUser(uid)
	if nil != err {
		beego.Error(FN, "err:", err)
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

	err, _ = GetUser(uid)
	if nil != err {
		beego.Error(FN, "err:", err)
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

/**
*	Check if the aid is the real "Agency" of uid who is landlord
*	Arguments:
*		uid - landlord id
*		aid	- agency id to check
*	Returns
*		bAgency - is agency or not
 */
func isOwnerAgency(uid, aid int64) (bAgency bool) {
	FN := "[isOwnerAgency] "
	beego.Trace(FN, "uid:", uid, ", aid:", aid)

	// defer func() {
	// 	if nil != err {
	// 		beego.Error(FN, err)
	// 	}
	// }()

	err, b := isAgency(aid)
	if nil != err || !b {
		beego.Error(FN, "err:", err, ", is agency:", b)
		return
	}

	// check all houses belong to this landlord
	err, hl := getHouseListByOwner(uid)
	if nil != err {
		beego.Error(FN, "err:", err)
		return
	}
	beego.Debug(FN, "house count:", len(hl))

	for _, v := range hl {
		if isHouseAgency(v, aid) {
			bAgency = true
			break
		}
	}

	return
}

func getName4System() string {
	return getSpecialString(KEY_USER_SYSTEM) // "系统"
}
