package models

import (
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/orm"
	_ "github.com/go-sql-driver/mysql"
	// "os"
	"time"
)

/***************************************************************************
	tables for system user
***************************************************************************/
type TblUser struct {
	Id        int64
	LoginName string `orm:"size(64)"` // login name, user phone
	Name      string `orm:"size(64)"`
	Salt      string `orm:"size(32)"`
	PassLogin string `orm:"size(50)"` // login password
	PassTrasn string `orm:"size(50)"` // transaction password
	IdNo      string `orm:"size(50)"` // ID number or passport number
	Phone     string `orm:"size(50)"`
	Head      string `orm:"size(50)"` // url of head portrait
	Role      int    // USER_TYPE_xxx
}

func (rs *TblUser) TableUnique() [][]string {
	return [][]string{
		[]string{"Phone"},
		[]string{"LoginName"},
		// []string{"IdNo"}, 	// user may not fill in the ID number
	}
}

/***************************************************************************
	tables for house
***************************************************************************/
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
	HouseNo     string `orm:"size(50)"` // private info, only the house owner and agent could see
	Bedrooms    int
	Livingrooms int
	Bathrooms   int
	Acreage     int
	Property    *TblProperty `orm:"rel(fk)"`
}

/***************************************************************************
	tables for sms fetching
***************************************************************************/
/*
	tmp table to store the phone and sms code mapping table
*/
type TblSmsCode struct {
	Id      int64
	Phone   string    `orm:"size(50)"`                    // phone number
	SmsCode string    `orm:"size(50)"`                    // sms code
	Expire  time.Time `orm:"auto_now_add;type(datetime)"` // expire time for this sms code
}

func (sc *TblSmsCode) TableUnique() [][]string {
	return [][]string{
		[]string{"Phone"},
	}
}

// before using please makere sure the database already created, else use the following sentence to create it
//	CREATE DATABASE IF NOT EXISTS yourdbname DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

/***************************************************************************/
func init() {
	// tables need to be registered in init() function
	orm.RegisterModel(new(TblUser),
		new(TblProperty), new(TblHouse),
		new(TblSmsCode))

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
