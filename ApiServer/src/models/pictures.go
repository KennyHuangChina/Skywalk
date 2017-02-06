package models

import (
	"ApiServer/commdef"
	"fmt"
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/orm"
)

/**
*	Get picture url
*	Arguments:
*		hid 	- house id
*		uid 	- login user id
*		pt		- picture type
*		desc	- picture description
*	Returns
*		err - error info
*		nid - new picture id
**/
func AddPicture(hid, uid int64, pt int, desc string) (err error, nid int64) {
	FN := "[AddPicture] "
	beego.Trace(FN, "hid:", hid, ", uid:", uid, ", pt:", pt, ", desc:", desc)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/* Arguments checking */
	if 0 != hid {
		errT, h := checkHouse(hid)
		if nil != errT {
			err = errT
			return
		}
		if h.Owner.Id != uid || h.Agency.Id != uid {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION}
			return
		}
	}
	if 0 == len(desc) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("picture desc:%s", desc)}
		return
	}
	// picture type
	if err = checkPictureType(pt); nil != err {
		return
	}
	if commdef.PIC_TYPE_HOUSE == pt/100*100 && 0 == hid {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("hid:%d", hid)}
		return
	}

	/* Processing */

	err = commdef.SwError{ErrCode: commdef.ERR_NOT_IMPLEMENT}
	return
}

/**
*	Get picture url
*	Arguments:
*		pid 	- picture id
*		uid 	- user id
*		size	- picture size: PIC_SIZE_ALL, PIC_SIZE_SMALL, PIC_SIZE_MODERATE, PIC_SIZE_LARGE,
*	Returns
*		err - error info
*		url_s - small picture url
*		url_m - moderate picture url
*		url_l - large picture url
**/
func GetPicUrl(pid, uid int64, size int) (err error, url_s, url_m, url_l string) {
	FN := "[GetPicUrl] "
	beego.Trace(FN, "pid:", pid, ", uid:", uid, ", size:", size)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	if pid <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("picture id:%d", pid)}
		return
	}
	if uid <= 0 {
		// err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("user id:%d", uid)}
		// return
	}
	if size < commdef.PIC_SIZE_ALL || size > commdef.PIC_SIZE_LARGE {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("size:%d", size)}
		return
	}

	o := orm.NewOrm()

	sql := fmt.Sprintf("SELECT size, url FROM tbl_pic_set WHERE pic_id='%d'", pid)
	if size != commdef.PIC_SIZE_ALL {
		sql = sql + fmt.Sprintf(" AND size=%d", size)
	}
	beego.Debug(FN, "sql:", sql)

	var pss []TblPicSet
	numb, errTmp := o.Raw(sql).QueryRows(&pss)
	if nil != errTmp {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errTmp.Error()}
		return
	}
	if numb > 3 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("Inalid picture number:%d", numb)}
		return
	}
	beego.Debug(FN, "numb:", numb)

	for /*k*/ _, v := range pss {
		beego.Debug(FN, "v:", v)
		switch v.Size {
		case commdef.PIC_SIZE_SMALL:
			if commdef.PIC_SIZE_ALL == size || commdef.PIC_SIZE_SMALL == size {
				url_s = v.Url
			}
		case commdef.PIC_SIZE_MODERATE:
			if commdef.PIC_SIZE_ALL == size || commdef.PIC_SIZE_MODERATE == size {
				url_m = v.Url
			}
		case commdef.PIC_SIZE_LARGE:
			if commdef.PIC_SIZE_ALL == size || commdef.PIC_SIZE_LARGE == size {
				url_l = v.Url
			}
		}
	}

	beego.Warn(FN, "TODO: picture access right varification")

	return
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//		Internal Functions
//
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
func checkPictureType(picType int) (err error) {
	FN := "[checkPictureType] "

	if picType <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT}
		return
	}

	typeMajor := picType / 100 * 100
	typeMinor := picType % 100
	beego.Debug(FN, "Major:", typeMajor, ", minor:", typeMinor)

	switch typeMajor {
	case commdef.PIC_TYPE_USER:
		err = commdef.SwError{ErrCode: commdef.ERR_NOT_IMPLEMENT}
	case commdef.PIC_TYPE_HOUSE:
		switch typeMinor {
		case commdef.PIC_HOUSE_FLOOR:
			fallthrough
		case commdef.PIC_HOUSE_FURNITURE:
			fallthrough
		case commdef.PIC_HOUSE_APPLIANCE:
		default:
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT}
		}
	case commdef.PIC_TYPE_RENTAL:
		err = commdef.SwError{ErrCode: commdef.ERR_NOT_IMPLEMENT}
	default:
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT}
	}

	return
}
