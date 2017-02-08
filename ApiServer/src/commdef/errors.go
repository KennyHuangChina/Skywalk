package commdef

import (
	// "errors"
	"fmt"
	// "github.com/astaxie/beego"
)

type SwError struct {
	ErrCode      int64  // exact error code
	Model        string // which model
	ErrDesc      string // error description
	ErrInfo      string // additional error information
	HttpRespcode int64  // http respond code. 2xx - OK,
}

func (se *SwError) FillError() {
	// beego.Error("se:", se)
	se.ErrDesc = ErrorDesc[se.ErrCode]
	switch se.ErrCode {
	case ERR_NONE:
		se.HttpRespcode = 200
	// TODO: mapping the http response code with API resulut code here
	case ERR_COMMON_BAD_ARGUMENT:
		se.HttpRespcode = 400
	case ERR_USERLOGIN_CAPTCHA_SERVER:
		se.HttpRespcode = 503
	// case ERR_ADMIN_LOGIN_NAME_DUPLICATE:
	// case ERR_ADMIN_LOGIN_CAPTCHA_FAIL:
	// 	se.HttpRespcode = 400
	// case ERR_COMMON_INTERNAL_DB:
	// 	fallthrough
	// case ERR_COMMON_ID_NOT_EXIST:
	// 	fallthrough
	default:
		se.HttpRespcode = 500 // TODO: this is a example to remapping the Cidana error code to http response code
	}
	// beego.Error("se:", se)
}

func (se SwError) GetErrorString() string {
	if ERR_NONE == se.ErrCode {
		return "No Error"
	}

	ErrInfo := ""
	if len(se.ErrInfo) > 0 {
		ErrInfo = fmt.Sprintln("(", se.ErrInfo, ")")
	}
	Model := ""
	if len(se.Model) > 0 {
		Model = "model:" + se.Model + ", "
	}
	strErr := Model + se.ErrDesc + ErrInfo
	return strErr
}

// Implement `error` 接口
func (se SwError) Error() string {
	strErr := fmt.Sprintf("<Error> code: %d, %s", se.ErrCode, se.GetErrorString())
	return strErr
}

const (
	ERR_NONE          = 0
	ERR_NOT_IMPLEMENT = 1001
	ERR_LOWER_CLIENT  = 1002

	// common error
	ERR_COMMON_BAD_ARGUMENT = 1101
	ERR_COMMON_RES_NOTFOUND = 1102
	ERR_COMMON_UNEXPECTED   = 1103
	ERR_COMMON_NOT_LOGIN    = 1104
	ERR_COMMON_DUPLICATE    = 1105
	ERR_COMMON_PERMISSION   = 1106

	// user
	ERR_USERLOGIN_CAPTCHA_SERVER     = 1201
	ERR_USERLOGIN_NO_PASSWORD        = 1202
	ERR_USERLOGIN_INCORRECT_PASSWORD = 1203
	ERR_USER_NOT_ENABLE              = 1204

	// fetch sms
	ERR_SMS_EMPTY_PHONE      = 1301
	ERR_SMS_PHONE_LEN        = 1302
	ERR_SMS_PHONE_NO_NUMERIC = 1303
	ERR_SMS_INVALID_PHONE_NO = 1304
	ERR_SMS_NOT_FOUND        = 1305
	ERR_SMS_WRONG_CODE       = 1306
	ERR_SMS_EXPIRED          = 1307

	// system
	ERR_SYS_IO_READ        = 1401
	ERR_SYS_IO_CREATE_FILE = 1402
	ERR_SYS_IO_WRITE       = 1403
	ERR_SYS_IO_CREATE_DIR  = 1404
)

var ErrorDesc = map[int64]string{
	ERR_NONE:          "OK",
	ERR_NOT_IMPLEMENT: "Not Implement",
	ERR_LOWER_CLIENT:  "Client is too low, please upgrade",

	// common error
	ERR_COMMON_BAD_ARGUMENT: "Invalid input argument",
	ERR_COMMON_RES_NOTFOUND: "Resource not found",
	ERR_COMMON_UNEXPECTED:   "Unexpect error",
	ERR_COMMON_NOT_LOGIN:    "User not login",
	ERR_COMMON_DUPLICATE:    "Duplicate resource",
	ERR_COMMON_PERMISSION:   "Permission Denied",

	// user login
	ERR_USERLOGIN_CAPTCHA_SERVER:     "Captchar server error",
	ERR_USERLOGIN_NO_PASSWORD:        "empty password",
	ERR_USERLOGIN_INCORRECT_PASSWORD: "incorrect password",
	ERR_USER_NOT_ENABLE:              "User not enabled",

	// fetch sms
	ERR_SMS_EMPTY_PHONE:      "Phone number is empty",
	ERR_SMS_PHONE_LEN:        "Wrong phone number length",
	ERR_SMS_PHONE_NO_NUMERIC: "Phone number include no-numeric char",
	ERR_SMS_INVALID_PHONE_NO: "Invalid phone number",
	ERR_SMS_NOT_FOUND:        "No sms code found",
	ERR_SMS_WRONG_CODE:       "Incorrect SMS code",
	ERR_SMS_EXPIRED:          "SMS code was expired",

	// system
	ERR_SYS_IO_READ:        "System IO read error",
	ERR_SYS_IO_CREATE_FILE: "Fail to create file",
	ERR_SYS_IO_WRITE:       "Fail to write file",
	ERR_SYS_IO_CREATE_DIR:  "Fail to create dir",
}
