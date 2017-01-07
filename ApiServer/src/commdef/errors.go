package commdef

import (
	// "errors"
	"fmt"
	"github.com/astaxie/beego"
)

type SwError struct {
	ErrCode      int64  // exact error code
	Model        string // which model
	ErrDesc      string // error description
	ErrInfo      string // additional error information
	HttpRespcode int64  // http respond code. 2xx - OK,
}

func (se SwError) FillError() {
	beego.Debug("se:", se)
	se.ErrDesc = ErrorDesc[se.ErrCode]
	switch se.ErrCode {
	case ERR_NONE:
		se.HttpRespcode = 200
	// TODO: mapping the http response code with API resulut code here
	case ERR_COMMON_BAD_ARGUMENT:
		se.HttpRespcode = 400
	case ERR_COMMON_CAPTCHA_SERVER:
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
}

// Implement `error` 接口
func (se SwError) Error() string {
	if ERR_NONE == se.ErrCode {
		return "OK"
	}

	strÈrr := fmt.Sprintln("<Error> code:", se.ErrCode, ", model:", se.Model, ",", se.ErrDesc)
	if len(se.ErrInfo) > 0 {
		strÈrr = strÈrr + fmt.Sprintln("(", se.ErrInfo, ")")
	}
	return strÈrr
}

const (
	ERR_NONE              = 0
	ERR_COMMON_UNEXPECTED = 1000

	// common error
	ERR_COMMON_BAD_ARGUMENT   = 1001
	ERR_COMMON_CAPTCHA_SERVER = 1101
)

var ErrorDesc = map[int64]string{
	ERR_NONE: "OK",

	// common error
	ERR_COMMON_BAD_ARGUMENT: "invalid input argument",
	ERR_COMMON_UNEXPECTED:   "unexpect error",
}
