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
	// fmt.Printf("%x", raw)
	return fmt.Sprintf("%x", raw)
}

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
