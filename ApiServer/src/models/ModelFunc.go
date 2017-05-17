package models

import (
	// "ApiServer/commdef"
	"crypto/md5"
	// crand "crypto/rand"
	// "encoding/base64"
	// "encoding/hex"
	"fmt"
	// "github.com/astaxie/beego"
	"github.com/astaxie/beego/orm"
	// "io"
	// mrand "math/rand"
	// "strconv"
	// "time"
)

func generateMD5(content string) string {
	raw := md5.Sum([]byte(content))
	fmt.Println("%x", raw)
	return fmt.Sprintf("%x", raw)
}

const (
	KEY_UNKNOWN                   = "KEY_UNKNOWN"
	KEY_USER_SYSTEM               = "KEY_USER_SYSTEM"
	KEY_USER_NAME_NOT_SET         = "KEY_USER_NAME_NOT_SET"
	KEY_LANDLORD_SUBMIT_NEW_HOUSE = "KEY_LANDLORD_SUBMIT_NEW_HOUSE"
	KEY_HOUSE_CERTIFICATE_BEGIN   = "KEY_HOUSE_CERTIFICATE_BEGIN"
	KEY_HOUSE_CERTIFICATE_FAILED  = "KEY_HOUSE_CERTIFICATE_FAILED"
	KEY_HOUSE_CERTIFICATE_PASS    = "KEY_HOUSE_CERTIFICATE_PASS"
)

func getSpecialString(key string) (value string) {
	o := orm.NewOrm()

	kv := TblStrings{Key: key}
	err := o.QueryTable("tbl_strings").Filter("Key", key).One(&kv)
	//	err := o.Read(&kv)
	if nil == err {
		return kv.Value
	}

	return ""
}
