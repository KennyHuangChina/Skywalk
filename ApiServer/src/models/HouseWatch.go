package models

import (
	"ApiServer/commdef"
	"fmt"
	"github.com/astaxie/beego"
	// "github.com/astaxie/beego/orm"
	// "strconv"
	// "time"
)

/**
*	Get watch house list by login user
*	Arguments:
*		begin	- from which item to fetch
*		tofetch	- how many items to fetch
*		uid		- login user
*	Returns
*		err 	- error info
*		total 	- total number
*		fetched	- fetched quantity
*		hids	- house id list
**/
func GetUserWatchList(begin, tofetch, uid int64) (err error, total, fetched int64, hids []int64) {
	FN := "[GetUserWatchList] "
	beego.Trace(FN, "fetch:(", begin, ",", tofetch, "), login user:", uid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	fetched = -1

	/* Argeuments checking */
	if begin < 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("begin:%d", begin)}
		return
	}
	if tofetch < 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("tofetch:%d", tofetch)}
		return
	}

	/* Permission checking */
	err, u := GetUser(uid)
	if nil != err {
		beego.Error(FN, "err:", err)
		return
	}

	/* Processing */

	return
}
