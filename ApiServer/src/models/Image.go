package models

import (
	"ApiServer/commdef"
	"fmt"
	// "github.com/astaxie/beego"
	"github.com/astaxie/beego/orm"
	// "time"
)

/****************************************************************************************************************************
*
*		Internal Functions
*
*****************************************************************************************************************************/
func checkImage(iid int64) (err error) {

	if iid <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("iid:%d", iid)}
		return
	}

	o := orm.NewOrm()

	p := TblPictures{Id: iid}
	errT := o.Read(&p)
	if nil != errT {
		if orm.ErrNoRows == errT || orm.ErrMissPK == errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_RES_NOTFOUND, ErrInfo: fmt.Sprintf("iid:%d", iid)}
		} else {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		}
		return
	}

	return
}
