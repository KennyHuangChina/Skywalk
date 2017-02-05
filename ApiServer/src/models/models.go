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
	SaltTmp   string `orm:"size(32)"`
	PassLogin string `orm:"size(50)"` // login password
	PassTrasn string `orm:"size(50)"` // transaction password
	IdNo      string `orm:"size(50)"` // ID number or passport number
	Phone     string `orm:"size(50)"`
	Head      string `orm:"size(50)"`                // url of head portrait
	Enable    bool   `orm:"default(true); not null"` // is this user enabled now. Administrator could enable or disable a user
	// Role      int    // USER_TYPE_xxx
}

func (rs *TblUser) TableUnique() [][]string {
	return [][]string{
		[]string{"Phone"},
		[]string{"LoginName"},
		// []string{"IdNo"}, 	// user may not fill in the ID number
	}
}

type TblUserGroup struct {
	Id      int64
	Name    string                `orm:"size(100)"`
	Admin   bool                  // Is this group a administrator
	Members []*TblUserGroupMember `orm:"reverse(many)"`
}

type TblUserGroupMember struct {
	Id    int64
	Group *TblUserGroup `orm:"rel(fk)"`
	User  *TblUser      `orm:"rel(fk)"`
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

func (p *TblProperty) TableUnique() [][]string {
	return [][]string{
		[]string{"Name"},
	}
}

type TblHouse struct {
	Id          int64
	BuildingNo  int
	FloorTotal  int
	FloorThis   int
	HouseNo     string `orm:"size(20)"` // private info, only the house owner and agent could see
	Bedrooms    int
	Livingrooms int
	Bathrooms   int
	Acreage     int
	CoverImg    int64        // the picture id of house cover image
	Property    *TblProperty `orm:"rel(fk)"`

	ForSale  bool // house is for sale
	ForRent  bool // house is for rent
	RentStat int  // current rent status, ref to HOUSE_RENT_xxx

	Owner  *TblUser `orm:"rel(fk)"` // house owner
	Agency *TblUser `orm:"rel(fk)"` // house agency

	SubmitTime  time.Time `orm:"auto_now_add;type(datetime)"`      // the time the owner submited
	PublishTime time.Time `orm:"auto_now_add;type(datetime);null"` // the time the agency certificated and published
	ModifyTime  time.Time `orm:"auto_now_add;type(datetime);null"` // the time the house has been modified
}

func (h *TblHouse) TableIndex() [][]string {
	return [][]string{
		[]string{"Property"},
		[]string{"Agency"},
		[]string{"ForSale"},
		[]string{"ForRent"},
	}
}

func (h *TblHouse) TableUnique() [][]string {
	return [][]string{
		[]string{"Property", "BuildingNo", "HouseNo"},
	}
}

type TblRental struct {
	Id           int64
	HouseId      int64
	RentalBid    int       // rental Bid price, in 0.01. So 120050 means 1200.50
	RentalBottom int       // rental bottom price, in 0.01. So 120050 means 1200.50
	Who          int       // who made this price, ref to TblUser. typically it is house owner
	When         time.Time `orm:"auto_now_add;type(datetime)"` // when made this price
	Active       bool      // Is the price still open. when house has been rented, this field should be set to false which means there is no available rental
}

func (r *TblRental) TableIndex() [][]string {
	return [][]string{
		[]string{"HouseId"},
	}
}

type TblTag struct {
	Id  int64
	Tag string `orm:"size(50)"`
}

type TblHouseTag struct {
	Id    int64
	House int64 // house id
	Tag   int64 // tag id
}

func (ht *TblHouseTag) TableIndex() [][]string {
	return [][]string{
		[]string{"House"},
	}
}
func (ht *TblHouseTag) TableUnique() [][]string {
	return [][]string{
		[]string{"House", "Tag"},
	}
}

type TblHouseRecommend struct {
	Id    int64
	House int64     // house id
	Who   int64     // agent id
	When  time.Time `orm:"auto_now_add;type(datetime)"` // recommendation time
}

func (hr *TblHouseRecommend) TableUnique() [][]string {
	return [][]string{
		[]string{"House"},
	}
}

type TblDeliverables struct {
	Id   int64
	Name string `orm:"size(50)"`
}

type TblHouseDeliverable struct {
	Id          int64
	House       int64  // house id
	Deliverable int64  // deliverable id
	Qty         int    // deliverable quantity
	Desc        string `orm:"size(100)"`
}

func (hd *TblHouseDeliverable) TableIndex() [][]string {
	return [][]string{
		[]string{"House"},
	}
}

func (hd *TblHouseDeliverable) TableUnique() [][]string {
	return [][]string{
		[]string{"House", "Deliverable"},
	}
}

type TblFacilityType struct {
	Id   int64
	Name string `orm:"size(50)"`
}

// CREATE UNIQUE INDEX type_name ON tbl_facility_type (name)
func (f *TblFacilityType) TableUnique() [][]string {
	return [][]string{
		[]string{"Name"},
	}
}

type TblFacilitys struct {
	Id   int64
	Type int64
	Name string `orm:"size(50)"`
}

func (f *TblFacilitys) TableUnique() [][]string {
	return [][]string{
		[]string{"Type", "Name"},
	}
}

type TblHouseFacility struct {
	Id       int64
	House    int64  // house id
	Facility int64  // facility id
	Qty      int    // facility quantity
	Desc     string `orm:"size(50)"`
}

func (hd *TblHouseFacility) TableIndex() [][]string {
	return [][]string{
		[]string{"House"},
	}
}

func (f *TblHouseFacility) TableUnique() [][]string {
	return [][]string{
		[]string{"House", "Facility"},
	}
}

/***************************************************************************
	tables for events
***************************************************************************/
type TblHouseEvent struct {
	Id         int64
	House      int64 // house id
	Sender     int64
	Receiver   int64
	CreateTime time.Time               `orm:"auto_now_add;type(datetime)"`
	ReadTime   time.Time               `orm:"auto_now_add;type(datetime);null"`
	Type       int                     // event type, ref to HOUSE_EVENT_xxx
	Desc       string                  `orm:"size(200)"`
	Process    []*TblHouseEventProcess `orm:"reverse(many)"`
}

func (he *TblHouseEvent) TableIndex() [][]string {
	return [][]string{
		[]string{"House"},
	}
}

type TblHouseEventProcess struct {
	Id int64
	// Event int64     // event id
	Who   int64          // who made thie process
	When  time.Time      `orm:"auto_now_add;type(datetime)"` // process time
	Type  int            // event process type, ref to HOUSE_EVENT_PROC_XXXX
	Desc  string         `orm:"size(200)"`
	Event *TblHouseEvent `orm:"rel(fk)"`
}

/***************************************************************************
	tables for pictures
***************************************************************************/
type TblPictures struct {
	Id        int64
	TypeMajor int    // picture major type. ref to commdef.PIC_TYPE_xxxx
	TypeMiner int    // picture miner type. ref to commdef.PIC_xxx, based on what major type is. for example, if TypeMajor is PIC_TYPE_HOUSE, then TypeMiner should be PIC_HOUSE_xxx
	RefId     int64  // picture reference id, based on what type it is. for example if type is PIC_TYPE_HOUSE, then the RefId is house id
	Desc      string `orm:"size(100)"` // picture description
}

func (p *TblPictures) TableIndex() [][]string {
	return [][]string{
		[]string{"TypeMajor", "TypeMiner", "RefId"},
	}
}

type TblPicSet struct {
	Id    int64
	PicId int64  // parent picture id, ref to TblPictures.Id
	Size  int    // picture size, ref to commdef.PIC_SIZE_xxx
	Url   string `orm:"size(100)"` // the path where the picture placed
}

func (ps *TblPicSet) TableIndex() [][]string {
	return [][]string{
		[]string{"PicId", "Size"},
	}
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
	orm.DefaultTimeLoc = time.UTC
	// tables need to be registered in init() function
	orm.RegisterModel(new(TblUser), new(TblUserGroup), new(TblUserGroupMember),
		new(TblProperty), new(TblHouse), new(TblRental), new(TblTag), new(TblHouseTag), new(TblHouseRecommend),
		new(TblDeliverables), new(TblHouseDeliverable),
		new(TblFacilityType), new(TblFacilitys), new(TblHouseFacility),
		new(TblHouseEvent), new(TblHouseEventProcess),
		new(TblPictures), new(TblPicSet),
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
	orm.RegisterDataBase("default", "mysql", db_acc+"@"+"tcp("+db_url+")/"+db_name+"?charset=utf8&loc=Asia%2FShanghai")

	// o := orm.NewOrm()
	// // o.Using("default")
	// o.Using("rtdb")
	// dr := o.Driver()
	// beego.Warn("dr:", dr)

	// /*_, err :=*/ o.Raw("CREATE UNIQUE INDEX house_id ON tbl_house (property_id, building_no, house_no)").Exec()
	// beego.Debug("[init] err:", err.Error())

	orm.RunSyncdb("default", false, true) // sync tables
}
