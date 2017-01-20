package models

import (
	// 	"ApiServer/commdef"
	"fmt"
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/orm"
)

/**
*	Get picture url
*	Arguments:
*		pid 	- picture id
*		uid 	- user id
*		size	- picture size: PIC_SIZE_SMALL, PIC_SIZE_MODERATE, PIC_SIZE_LARGE
*	Returns
*		err - error info
*		url - picture url
**/
func GetPicUrl(pid, uid int64, size int) (err error, url string) {
	FN := "[GetPicUrl] "
	beego.Trace(FN, "pid:", pid, ", uid:", uid, ", size:", size)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	if pid <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("invalid picture id:%d", pid)}
		return
	}
	if uid <= 0 {
		// err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("invalid user id:%d", uid)}
		// return
	}
	if size <= 0 || size > commdef.PIC_SIZE_LARGE {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("invalid size:%d", size)}
		return
	}

	// o := orm.NewOrm()

	// sql := fmt.Sprintf("SELECT FROM tbl_pictures WHERE id='%d'", pid)
	// o.Raw(sql).Exec()

	beego.Warn(FN, "TODO: picture access right varification")

	return
}
