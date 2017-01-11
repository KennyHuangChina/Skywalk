package models

import (
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/orm"
	_ "github.com/go-sql-driver/mysql"
	// "os"
	// "time"
)

/***************************************************************************
	tables for system user
***************************************************************************/
type TblUser struct {
	Id    int64
	Name  string `orm:"size(64)"`
	Salt  string `orm:"size(32)"`
	Pass  string `orm:"size(50)"` // login password
	IdNo  string `orm:"size(50)"`
	Phone string `orm:"size(50)"`
	Head  string `orm:"size(50)"` // url of head portrait
	Role  int    // USER_TYPE_xxx
}

type TblProperty struct {
	Id      int64
	Name    string      `orm:"size(50)"`
	Address string      `orm:"size(200)"`
	Desc    string      `orm:"size(1000)"`
	Houses  []*TblHouse `orm:"reverse(many)"`
}

type TblHouse struct {
	Id          int64
	BuildingNo  int
	FloorTotal  int
	FloorThis   int
	HouseNo     string `orm:"size(50)"`
	Bedrooms    int
	Livingrooms int
	Bathrooms   int
	Acreage     int
	Property    *TblProperty `orm:"rel(fk)"`
}

func TmpUse() {
	// TODO: this function should be removed once this file could be compiled automatic
}

// before using please makere sure the database already created, else use the following sentence to create it
//	CREATE DATABASE IF NOT EXISTS yourdbname DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

/***************************************************************************/
func init() {
	beego.Warn("init")

	// 需要在init中注册定义的model
	orm.RegisterModel(new(TblUser),
		new(TblProperty), new(TblHouse))

	orm.RegisterDriver("mysql", orm.DRMySQL)

	// sample of new configuation item created by ourself
	// testMode, _ := beego.AppConfig.Bool("testmode")
	// beego.Warn("testMode:", testMode)

	runmode := beego.BConfig.RunMode
	var db_url, db_acc, db_name = "", "", ""
	if runmode == "dev" {
		db_url = "localhost:3306"
		db_acc = "root:root"
		orm.Debug = true // show orm detail info
	} else if runmode == "prod" {
		db_url = "localhost:3306"
		db_acc = "ssdbadmin:Cshdb-0571"
		// orm.RegisterDataBase("default", "mysql", "ssdbadmin:Cshdb-0571@tcp(rds5407d1j24tno1h5vs.mysql.rds.aliyuncs.com:3306)/ssdb?charset=utf8")
		orm.Debug = false // disable the detail info of orm
	}
	db_name = "rtdb" // realty database
	beego.Debug(db_acc + "@" + "tcp(" + db_url + ")/" + db_name + "?charset=utf8")
	orm.RegisterDataBase("default", "mysql", db_acc+"@"+"tcp("+db_url+")/"+db_name+"?charset=utf8")

	// o := orm.NewOrm()
	// // o.Using("default")
	// o.Using("rtdb")
	// dr := o.Driver()
	// beego.Warn("dr:", dr)

	orm.RunSyncdb("default", false, true) // sync tables
}
