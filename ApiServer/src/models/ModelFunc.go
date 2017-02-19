package models

import (
	// "ApiServer/commdef"
	"crypto/md5"
	// crand "crypto/rand"
	// "encoding/base64"
	// "encoding/hex"
	"fmt"
	// "github.com/astaxie/beego"
	// "github.com/astaxie/beego/orm"
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
