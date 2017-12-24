package models

import (
	"ApiServer/commdef"
	"fmt"
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/orm"
	_ "github.com/go-sql-driver/mysql"
	// "os"
	"strings"
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
	PassLogin string `orm:"size(50)"`                // login password. md5(pass+salt)
	PassTrasn string `orm:"size(50)"`                // transaction password
	IdNo      string `orm:"size(50)"`                // ID number or passport number
	Phone     string `orm:"size(50)"`                // secondary phone
	Head      string `orm:"size(50)"`                // url of head portrait
	Enable    bool   `orm:"default(true); not null"` // is this user enabled now. Administrator could enable or disable a user
	Session   string `orm:"size(50)"`
}

func (rs *TblUser) TableUnique() [][]string {
	return [][]string{
		// []string{"Phone"},	// user may not fill in the secondary phone
		[]string{"LoginName"},
		// []string{"IdNo"}, 	// user may not fill in the ID number in the first place
	}
}

type TblUserGroup struct {
	Id      int64
	Name    string                `orm:"size(100)"` // group name
	Admin   bool                  // Is this group a administrator
	Members []*TblUserGroupMember `orm:"reverse(many)"`
}

type TblUserGroupMember struct {
	Id    int64
	Group *TblUserGroup `orm:"rel(fk)"`
	User  *TblUser      `orm:"rel(fk)"`
}

type TblAgency struct {
	Id       int64
	User     int64 // user id, TblUser.Id
	RankProf int   // rank for professional. 0 ~ 500 (0.00 ~ 5.00)
	RankAtti int   // rank for attitude. 0 ~ 500 (0.00 ~ 5.00)
	Begin    int   // which year start the business
}

func (rs *TblAgency) TableUnique() [][]string {
	return [][]string{
		[]string{"User"},
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

func (p *TblProperty) TableUnique() [][]string {
	return [][]string{
		[]string{"Name"},
	}
}

// ALTER TABLE tbl_house CHANGE building_no building_no VARCHAR(40) NOT NULL
type TblHouse struct {
	Id          int64
	BuildingNo  string `orm:"size(40);not null"`
	FloorTotal  int
	FloorThis   int
	HouseNo     string `orm:"size(20)"` // private info, only the house owner and agent could see
	Bedrooms    int
	Livingrooms int
	Bathrooms   int
	Acreage     int
	CoverImg    int64        // the picture id of house cover image
	Property    *TblProperty `orm:"rel(fk)"`
	Decoration  int          // house decoration. DECORATION_xxx

	Public   bool `orm:"default(true); not null"` // if house opened for every agency, else only the agency who behalf this house could access
	ForSale  bool // house is for sale
	ForRent  bool // house is for rent
	RentStat int  // current rent status, ref to HOUSE_RENT_xxx

	Owner  *TblUser `orm:"rel(fk)"` // house owner
	Agency *TblUser `orm:"rel(fk)"` // house agency

	PurchaseDate time.Time `orm:"type(datetime);null"`              // exact date when purchase the house
	SubmitTime   time.Time `orm:"auto_now_add;type(datetime)"`      // the time the owner submited
	PublishTime  time.Time `orm:"auto_now_add;type(datetime);null"` // the time the agency certificated and published
	ModifyTime   time.Time `orm:"auto_now_add;type(datetime);null"` // the time the house has been modified
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

// type TblHouseWatch struct {
// 	Id      int64
// 	House   int64
// 	Watcher int64     // who watch the house
// 	When    time.Time `orm:"auto_now_add;type(datetime)"` // when watch the house
// }

// func (h *TblHouseWatch) TableIndex() [][]string {
// 	return [][]string{
// 		[]string{"House"},
// 		[]string{"Watcher"},
// 		[]string{"House", "Watcher"},
// 	}
// }

type TblAppointment struct {
	Id           int64
	OrderType    int       // order type. ref to ORDER_TYPE_xxx
	House        int64     // House id
	Subscriber   int64     `orm:"not null; default(0)"` // who make the subscription
	Phone        string    // contace phone number. it is obligatory if user not login
	Receptionist int64     `orm:"not null; default(0)"`        // appointment receptionist, 0 means the receptionist will be assigned by Admin
	ApomtTimeBgn time.Time `orm:"type(datetime); not null"`    // appointment period begin, like 2017-06-23 9:00
	ApomtTimeEnd time.Time `orm:"type(datetime); not null"`    // appointment period end, like 2017-06-23 9:30
	ApomtDesc    string    `orm:"size(200)"`                   // appointment description
	SubscTime    time.Time `orm:"auto_now_add;type(datetime)"` // when make the subscription
	CloseTime    time.Time `orm:"type(datetime);null;default(null)"`
}

func (h *TblAppointment) TableIndex() [][]string {
	return [][]string{
		[]string{"House"},
		[]string{"Subscriber"},
		[]string{"OrderType", "House", "Subscriber", "Phone"},
	}
}

type TblAppointmentAction struct {
	Id      int64
	Appoint int64 // TblAppointment.Id
	Action  int   // APPOINT_ACTION_xxx
	Who     int64
	When    time.Time `orm:"auto_now_add;type(datetime);default(now)"`
	TimeBgn time.Time `orm:"type(datetime);null"` // appointment begin time
	TimeEnd time.Time `orm:"type(datetime);null"` // appointment end time
	Comment string    `orm:"size(200)"`
}

func (h *TblAppointmentAction) TableIndex() [][]string {
	return [][]string{
		[]string{"Appoint"},
	}
}

// type TblHousePrice struct {
// 	Id            int64
// 	House         int64     // house
// 	RentalTag     int64     // rental, tag price
// 	RentalBottom  int64     // rental, bottom price
// 	PropFee       bool      `orm:"not null; default(false)"` // if the rental involve the property fee
// 	SellingTag    int64     // selling, tag price
// 	SellingBottom int64     // selling, bottom price
// 	Who           int64     // who set the price
// 	When          time.Time `orm:"auto_now_add;type(datetime)"` // when set the price
// }

// func (h *TblHousePrice) TableIndex() [][]string {
// 	return [][]string{
// 		[]string{"House"},
// 	}
// }

// house certification history
type TblHouseCert struct {
	Id        int64
	House     int64     // house id
	Who       int64     // who made the certification
	When      time.Time `orm:"auto_now_add;type(datetime)"` // when made the certification
	Comment   string    `orm:"size(200)"`                   // certification comments
	CertStatu int       `orm:"default(0)"`                  // HOUSE_CERT_STAT_xxx, default to HOUSE_CERT_STAT_Unknown
}

func (h *TblHouseCert) TableIndex() [][]string {
	return [][]string{
		[]string{"House"},
		[]string{"Who", "When"},
	}
}

type TblRental struct {
	Id           int64
	HouseId      int64
	RentalBid    int       // rental Bid price, in 0.01. So 120050 means 1200.50
	RentalBottom int       // rental bottom price, in 0.01. So 120050 means 1200.50
	PropFee      bool      `orm:"not null; default(false)"` // if the rental involve the property fee
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
	Pic  int    `orm:"not null; default(0)"` // pic id
}

func (hr *TblDeliverables) TableUnique() [][]string {
	return [][]string{
		[]string{"Name"},
		[]string{"Pic"},
	}
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
	Pic  string `orm:"size(300); not null"` // icon url
}

func (f *TblFacilitys) TableUnique() [][]string {
	return [][]string{
		[]string{"Type", "Name"},
	}
}

func (hd *TblFacilitys) TableIndex() [][]string {
	return [][]string{
		[]string{"Type"},
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

type TblHouseShowTime struct {
	Id      int64 // house id
	Period1 int   // period for working day
	Period2 int   // period for weekend and vacation
	Desc    string
	Who     int64
	When    time.Time `orm:"auto_now_add;type(datetime)"`
}

/***************************************************************************
	tables for events
***************************************************************************/
// TODO: should be replaced by TblMessage
type TblHouseEvent struct {
	Id         int64
	House      int64 // house id
	Sender     int64
	Receiver   int64
	CreateTime time.Time               `orm:"auto_now_add;type(datetime)"`
	ReadTime   time.Time               `orm:"auto_now_add;type(datetime);null"`
	Type       int                     // event type, ref to HOUSE_EVENT_xxx
	Desc       string                  `orm:"size(200)"`
	CloseTime  time.Time               `orm:"auto_now_add;type(datetime);null"`
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
	Who   int64          // who made the processing
	When  time.Time      `orm:"auto_now_add;type(datetime)"` // when made the processing
	Type  int            // event process type, ref to HOUSE_EVENT_PROC_XXXX
	Desc  string         `orm:"size(200)"`
	Event *TblHouseEvent `orm:"rel(fk)"`
}

func (he *TblHouseEventProcess) TableIndex() [][]string {
	return [][]string{
		[]string{"Event", "When"},
	}
}

/***************************************************************************
	tables for system messages
***************************************************************************/
type TblMessage struct {
	Id         int64
	Type       int       // message type. ref to MSG_xxxx
	RefId      int64     // reference id, depend on what type the message is
	Msg        string    `orm:"size(200)"`
	Priority   int       `orm:"not null; default(0)"` // MSG_PRIORITY_xxx, default to MSG_PRIORITY_Info
	Receiver   int64     // who the message send to
	CreateTime time.Time `orm:"auto_now_add;type(datetime);not null"`
	ReadTime   time.Time `orm:"type(datetime);null"` // when the receiver read the message
}

func (he *TblMessage) TableIndex() [][]string {
	return [][]string{
		[]string{"Receiver"},
		[]string{"Type", "RefId"},
		[]string{"Receiver", "ReadTime", "CreateTime"},
	}
}

/***************************************************************************
	tables for pictures
***************************************************************************/
type TblPictures struct {
	Id        int64
	TypeMajor int       // picture major type. ref to commdef.PIC_TYPE_xxxx
	TypeMinor int       // picture minor type. ref to commdef.PIC_xxx, based on what major type is. for example, if TypeMajor is PIC_TYPE_HOUSE, then TypeMinor should be PIC_HOUSE_xxx
	RefId     int64     // picture reference id, based on what type it is. for example if type is PIC_TYPE_HOUSE, then the RefId is house id
	Desc      string    `orm:"size(100)"`                   // picture description
	Md5       string    `orm:"size(50)"`                    // picture fingerprint, use md5
	Submit    time.Time `orm:"auto_now_add;type(datetime)"` // picture submit time
}

func (p *TblPictures) TableIndex() [][]string {
	return [][]string{
		[]string{"TypeMajor", "TypeMinor", "RefId"},
		[]string{"Md5"},
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
		[]string{"PicId"},
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

type TblStrings struct {
	Id    int64
	Key   string `orm:"size(100)"`
	Value string `orm:"size(100)"`
}

func (sc *TblStrings) TableUnique() [][]string {
	return [][]string{
		[]string{"Key"},
	}
}

// before using please makere sure the database already created, else use the following sentence to create it
//	CREATE DATABASE IF NOT EXISTS yourdbname DEFAULT CHARSET utf8 COLLATE utf8_general_ci;

/***************************************************************************/
func init() {
	orm.DefaultTimeLoc = time.UTC
	// tables need to be registered in init() function
	orm.RegisterModel(new(TblUser), new(TblUserGroup), new(TblUserGroupMember), new(TblAgency),
		new(TblProperty), new(TblHouse), new(TblRental), new(TblTag), new(TblHouseTag), new(TblHouseRecommend),
		new(TblDeliverables), new(TblHouseDeliverable), new(TblHouseCert), // new(TblHousePrice),
		new(TblFacilityType), new(TblFacilitys), new(TblHouseFacility), new(TblHouseShowTime),
		/*new(TblHouseEvent), new(TblHouseEventProcess), */ new(TblAppointment), new(TblAppointmentAction) /*new(TblHouseWatch),*/, new(TblMessage),
		new(TblPictures), new(TblPicSet),
		new(TblSmsCode),
		new(TblStrings))

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

	beego.Debug("models.init")
	orm.RunSyncdb("default", false, true) // sync tables

	//////////////////////////////////////////////////////////////////////////////////////////
	//
	// 			Special processing
	//
	var STRING_MAP map[string]string
	STRING_MAP = make(map[string]string)
	STRING_MAP[KEY_USER_SYSTEM] = "系统"
	STRING_MAP[KEY_UNKNOWN] = "未知"
	STRING_MAP[KEY_USER_NAME_NOT_SET] = "未设置"
	STRING_MAP[KEY_LANDLORD_SUBMIT_NEW_HOUSE] = "业主委托新房源"
	STRING_MAP[KEY_HOUSE_CERTIFICATE_BEGIN] = "房源审核中"
	STRING_MAP[KEY_HOUSE_CERTIFICATE_FAILED] = "房源审核失败"
	STRING_MAP[KEY_HOUSE_CERTIFICATE_PASS] = "房源审核通过"
	STRING_MAP[KEY_HOUSE_EVENT_PROC_FOLLOW] = "跟进"
	STRING_MAP[KEY_HOUSE_EVENT_PROC_CLOSE] = "结案"

	STRING_MAP[KEY_APPOINTMENT_TYPE_SEE_HOUSE] = "预约看房"

	o := orm.NewOrm()
	for k, v := range STRING_MAP {
		if !o.QueryTable("tbl_strings").Filter("Key", k).Exist() {
			kv := TblStrings{Key: k, Value: v}
			o.Insert(&kv)
		}
	}

	DELIVERABLE_MAP := map[string]int{
		"大门钥匙":    1,
		"门禁卡":     2,
		"水电卡/存折":  3,
		"有线电视用户证": 4,
		"水表箱钥匙":   5,
		"电表箱钥匙":   6,
		"信报箱钥匙":   7,
		"保险柜钥匙":   8,
		"燃气卡":     9,
	}
	for k, v := range DELIVERABLE_MAP {
		// beego.Debug("k:", k, ", v:", v)
		if !o.QueryTable("tbl_deliverables").Filter("name", k).Exist() {
			kv := TblDeliverables{Name: k, Pic: v}
			o.Insert(&kv)
		}
	}

	// Facility type
	FACILITY_TYPE_MAP := map[string]int64{
		"家用电器": 1,
		"家具":   2,
		"厨房用品": 3,
		"其它":   4,
	}
	for k, v := range FACILITY_TYPE_MAP {
		// beego.Debug("k:", k, ", v:", v)
		if !o.QueryTable("tbl_facility_type").Filter("name", k).Exist() {
			kv := TblFacilityType{Id: v, Name: k}
			o.Insert(&kv)
		}
	}

	// Facility
	FACILITY_MAP := map[string]int64{
		"电冰箱":      1,
		"酒水柜":      1,
		"洗衣机":      1,
		"电视机":      1,
		"有线电视/机顶盒": 1,
		"卫星电视":     1,
		"立式空调":     1,
		"音响":       1,
		"壁挂空调":     1,
		"电脑":       1,
		"宽带网络/路由器": 1,
		"电风扇":      1,
		"取暖器":      1,
		"电熨斗":      1,

		"沙发+茶几": 2,
		"电视柜":   2,
		"床":     2,
		"衣柜":    2,
		"书桌":    2,
		"书柜":    2,

		"天然气":   3,
		"液化石油气": 3,
		"灶具":    3,
		"油烟机":   3,
		"微波炉":   3,
		"电磁炉":   3,
		"烤箱":    3,
		"洗碗机":   3,
		"消毒柜":   3,

		"鞋柜": 4,
		"花架": 4,
	}
	for k, v := range FACILITY_MAP {
		// beego.Debug("k:", k, ", v:", v)
		if !o.QueryTable("tbl_facilitys").Filter("name", k).Exist() {
			kv := TblFacilitys{Type: v, Name: k}
			o.Insert(&kv)
		}
	}

	// Create view for published house
	// _, errT := o.Raw(`SHOW TABLES LIKE 'v_house_published'`).Exec()	// doesn't work
	_, errT := o.Raw(`SELECT id FROM v_house_published LIMIT 0, 1`).Exec()
	// beego.Debug("warn:", errT)
	if nil != errT {
		beego.Warn("warn:", errT.Error(), ", Create now")
		if strings.Contains(errT.Error(), "Error 1146") {
			// beego.Warn("View does not exist")
			o.Raw(`CREATE VIEW v_house_published AS SELECT * FROM tbl_house WHERE publish_time IS NOT NULL`).Exec()
		}
	}

	// Create view for unpublished house
	_, errT = o.Raw(`SELECT id FROM v_house_unpublished LIMIT 0, 1`).Exec()
	if nil != errT {
		beego.Warn("warn:", errT.Error(), ", Create now")
		if strings.Contains(errT.Error(), "Error 1146") {
			// beego.Warn("View does not exist")
			o.Raw(`CREATE VIEW v_house_unpublished AS SELECT * FROM tbl_house WHERE publish_time IS NULL`).Exec()
		}
	}

	// Create view for user's headportrait
	_, errT = o.Raw(`SELECT id FROM v_pic_user_portrait LIMIT 0, 1`).Exec()
	if nil != errT {
		beego.Warn("warn:", errT.Error(), ", Create now")
		if strings.Contains(errT.Error(), "Error 1146") {
			// beego.Warn("View does not exist")
			sql := fmt.Sprintf(`CREATE VIEW v_pic_user_portrait AS 
									SELECT * FROM tbl_pictures WHERE type_major=%d AND type_minor=%d`,
				commdef.PIC_TYPE_USER, commdef.PIC_USER_HEAD_PORTRAIT)
			o.Raw(sql).Exec()
		}
	}

	// Create view for agency
	_, errT = o.Raw(`SELECT id FROM v_user_agent LIMIT 0, 1`).Exec()
	if nil != errT {
		beego.Warn("warn:", errT.Error(), ", Create now")
		if strings.Contains(errT.Error(), "Error 1146") {
			// beego.Warn("View does not exist")
			o.Raw(`CREATE VIEW v_user_agent AS 
					SELECT u.* FROM tbl_user AS u, tbl_user_group AS ug, tbl_user_group_member AS ugm 
						WHERE ugm.group_id=ug.id AND !ug.admin AND ugm.user_id=u.id`).Exec()
		}
	}

	// Create view for administrator
	_, errT = o.Raw(`SELECT id FROM v_user_admin LIMIT 0, 1`).Exec()
	if nil != errT {
		beego.Warn("warn:", errT.Error(), ", Create now")
		if strings.Contains(errT.Error(), "Error 1146") {
			// beego.Warn("View does not exist")
			o.Raw(`CREATE VIEW v_user_admin AS 
					SELECT u.* FROM tbl_user AS u, tbl_user_group AS ug, tbl_user_group_member AS ugm 
						WHERE ugm.group_id=ug.id AND ug.admin AND ugm.user_id=u.id`).Exec()
		}
	}

	// Create view for unclosed appointment
	_, errT = o.Raw(`SELECT id FROM v_appointment_actived LIMIT 0, 1`).Exec()
	if nil != errT {
		beego.Warn("warn:", errT.Error(), ", Create now")
		if strings.Contains(errT.Error(), "Error 1146") {
			// beego.Warn("View does not exist")
			o.Raw(`CREATE VIEW v_appointment_actived AS 
					SELECT * FROM tbl_appointment WHERE close_time IS NULL`).Exec()
		}
	}
}
