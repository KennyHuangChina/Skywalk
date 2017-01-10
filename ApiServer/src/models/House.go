package models

import (
	"ApiServer/commdef"
	// "fmt"
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/orm"
)

/*
	Get house information by id
	Arguments:
		id - house id
	Returns
		err - error info
		hif - house info
*/
func GetHouseInfo(hid int64) (err error, hif commdef.HouseInfo) {
	FN := "[GetUserInfo] "
	beego.Trace(FN, "hid:", hid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	o := orm.NewOrm()

	house := TblHouse{Id: hid}
	if err1 := o.Read(&house); nil != err1 {
		// beego.Error(FN, err1)
		if orm.ErrNoRows == err1 || orm.ErrMissPK == err1 {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_RES_NOTFOUND, ErrInfo: err1.Error()}
		} else {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: err1.Error()}
		}
		return
	}

	hif.Id = house.Id
	hif.Property = house.Property.Id
	hif.BuddingNo = house.BuildingNo
	hif.FloorTotal = house.FloorTotal
	hif.FloorThis = house.FloorThis
	hif.HouseNo = house.HouseNo
	hif.Bedrooms = house.Bedrooms
	hif.Livingrooms = house.Livingrooms
	hif.Bathrooms = house.Bathrooms
	hif.Acreage = house.Acreage

	return
}
