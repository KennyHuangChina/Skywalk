package models

import (
	"ApiServer/commdef"
	"fmt"
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/orm"
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
	if err, _ = GetUser(uid); nil != err {
		beego.Error(FN, "err:", err)
		return
	}

	/* Processing */
	o := orm.NewOrm()
	qs := o.QueryTable("tbl_house_watch").Filter("Watcher", uid)
	numb, errT := qs.Count()
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("err:%s", errT)}
		return
	}
	total = numb
	beego.Debug(FN, "total:", total)

	if 0 == tofetch || 0 == total {
		return
	}

	sql := "SELECT house AS id FROM tbl_house_watch WHERE watcher=? LIMIT ?, ?"
	ids := []int64{}
	numb, errT = o.Raw(sql, uid, begin, tofetch).QueryRows(&ids)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("err:%s", errT)}
		return
	}
	fetched = numb
	hids = ids

	return
}
