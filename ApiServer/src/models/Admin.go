package models

import (
	"ApiServer/commdef"
	"crypto/md5"
	// "crypto/rand"
	"encoding/hex"
	// "fmt"
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/orm"
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

	beego.Debug(FN, "user.Pass:", user.Pass, ", user.Salt:", user.Salt)

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
	hasher.Write([]byte(user.Pass))
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
