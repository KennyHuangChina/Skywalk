package commdef

const (
	MD5_BYTES = 16
)

////////////////////////////////////////////////////////////////////////////////////////////////////
//
//		User
//
const (
	USER_TYPE_Manger   = 0
	USER_TYPE_Agent    = 1
	USER_TYPE_Customer = 10
)

// TODO: user role type shoule be removed, use user group instead

type UserInfo struct {
	Id           int64
	Name         string
	IdNo         string
	Phone        string
	HeadPortrait string
	Role         int
	RoleDesc     string
}

type AgencyInfo struct {
	Id           int64
	Name         string
	Sex          int
	IdNo         string
	Phone        string
	Portrait     string
	Attitude     int
	Professional int
}

func (this *UserInfo) Role2Desc() {
	switch this.Role {
	case USER_TYPE_Manger:
		this.RoleDesc = "Manager"
	case USER_TYPE_Agent:
		this.RoleDesc = "Agent"
	case USER_TYPE_Customer:
		this.RoleDesc = "Customer"
	default:
		this.RoleDesc = "Unknown"
	}
}

////////////////////////////////////////////////////////////////////////////////////////////////////
//
//		House
//
const (
	DECORATION_Workblank = 0
	DECORATION_Simple    = 1
	DECORATION_Medium    = 2
	DECORATION_Refined   = 3
	DECORATION_Luxury    = 4
)

type HouseTags struct {
	TagId   int64
	TagDesc string
}

type HouseDigest struct {
	Id           int64
	Property     string
	PropertyAddr string
	Bedrooms     int
	Livingrooms  int
	Bathrooms    int
	Acreage      int
	Rental       int
	Pricing      int
	PropFee      bool   // if the rental involve the property fee
	CoverImg     int64  // cover image id
	CovImgUrlS   string //cover image url for small size
	CovImgUrlM   string // cover image url for moderate size
	Tags         []HouseTags
}

type HouseInfo struct {
	Id          int64
	Property    int64
	BuildingNo  string
	FloorTotal  int
	FloorThis   int
	HouseNo     string
	Bedrooms    int
	Livingrooms int
	Bathrooms   int
	Acreage     int
	ForSale     bool   // house is for saling
	ForRent     bool   // house is for renting
	RentStat    int    // rent state. ref to HOUSE_RENT_XXX
	Decoration  int    // TODO
	BuyDate     string // purchase date
	ModifyDate  string // last modify date
	Landlord    int64  // house landlord id
	Agency      int64  // house agency id
	SubmitTime  string // landlord submit time
	CertStat    int    // House certification status. HOUSE_CERT_STAT_XXX
	CertTime    string // house certification time
	CertDesc    string // house certification description
}

const (
	HOUSE_CERT_STAT_Unknown = 0
	HOUSE_CERT_STAT_WAIT    = 1 // house is waiting for certification
	HOUSE_CERT_STAT_PASSED  = 2 // house certification passed
	HOUSE_CERT_STAT_FAILED  = 3 // house certification failed
)

type HouseCert struct {
	Id      int64
	Uid     int64
	User    string
	Phone   string
	Time    string
	Stat    int
	CertTxt string
}

type HousePrice struct {
	Id           int64
	RentalTag    int    // rental, tag price
	RentalMin    int    // rental, bottom price
	PropFee      bool   // if the rental involve the property fee
	SalePriceTag int    // selling price, tag price
	SalePriceMin int    // selling price, bottom price
	Who          string // who set the price
	When         string // when set the price
}

type PropInfo struct {
	Id          int64
	PropName    string
	PropAddress string
	PropDesc    string
}

type HouseShowTime struct {
	Id      int64
	Period1 int // show period for working day
	Period2 int // show period for weekend and vacation
	Desc    string
	Who     string
	When    string
}

const (
	HOUSE_LIST_Unknown   = 0
	HOUSE_LIST_All       = 0
	HOUSE_LIST_Recommend = 1
	HOUSE_LIST_Deducted  = 2
	HOUSE_LIST_New       = 3

	HOUSE_LIST_Max = 3
)

const (
	BEHALF_TYPE_Begin      = 0
	BEHALF_TYPE_ALL        = 0 //
	BEHALF_TYPE_TO_RENT    = 1 // house is waiting for renting
	BEHALF_TYPE_RENTED     = 2 // house is rented
	BEHALF_TYPE_TO_SALE    = 3 // house is waiting for salesing
	BEHALF_TYPE_TO_APPROVE = 4 // house is waiting for approving
	BEHALF_TYPE_End        = 4
)

const (
	HOUSE_RENT_WAIT   = 1 // wait for renting
	HOUSE_RENT_RENTED = 2 // already rented
	HOUSE_RENT_DUE    = 3 // expire will be dued, typical is 1 month ago
)

type CommonListItem struct {
	Id   int64
	Name string
}

type Deliverable struct {
	CommonListItem
	Pic int
}

type HouseDeliverable struct {
	Id   int64
	Name string // house id
	Qty  int    // deliverable quantity
	Desc string
}

type Facility struct {
	Id   int64  // facility id
	Name string // facility name
	Type string // facility type name
	Icon string // facility icon url
}

type AddHouseFacility struct {
	Id       int64
	Facility int64
	Qty      int
	Desc     string
}

type HouseFacility struct {
	Id   int64  // facility id
	Name string // facility name
	Type string // facility type name
	Icon string // facility icon url
	Qty  int    // facility quantity
	Desc string // facility description
}

const (
	HOUSE_SHOW_PERIOD_Min = 0 // 0 | 0 | 0

	HOUSE_SHOW_PERIOD_MORNING   = 1
	HOUSE_SHOW_PERIOD_AFTERNOON = 2
	HOUSE_SHOW_PERIOD_NIGHT     = 4

	HOUSE_SHOW_PERIOD_Max = 7 // 1 | 2 | 4
)

const (
	HOUSE_FILTER_TYPE_EQ      = 1 // Less Than.     =
	HOUSE_FILTER_TYPE_LT      = 2 // Less Than.     <
	HOUSE_FILTER_TYPE_LE      = 3 // Less Equal.	<=
	HOUSE_FILTER_TYPE_GT      = 4 // Greater Than.  >
	HOUSE_FILTER_TYPE_GE      = 5 // Greater Equal. >=
	HOUSE_FILTER_TYPE_BETWEEN = 6 // Between.       >= && <=
	HOUSE_FILTER_TYPE_IN      = 7 // In.			IN(...)
)

const (
	HOUSE_SORT_PUBLISH      = 1 // sort by publish time, from early to late
	HOUSE_SORT_PUBLISH_DESC = 2 // sort by publish time, from late to early
	HOUSE_SORT_RENTAL       = 3 // sort by rental, from low to high
	HOUSE_SORT_RENTAL_DESC  = 4 // sort by rental, from high to low
	HOUSE_SORT_APPOINT      = 5 // sort by appointment number, from low to high
	HOUSE_SORT_APPOINT_DESC = 6 // sort by appointment number, from high to low
)

////////////////////////////////////////////////////////////////////////////////////////////////////
//
//		Appointment, Order
//
const (
	ORDER_TYPE_BEGIN = 1

	ORDER_TYPE_SEE_HOUSE = 1 // make a appointment to see house

	ORDER_TYPE_END = 1
)

type AppointmentInfo struct {
	Id           int64
	ApomtType    int
	TypeDesc     string
	House        int64
	Phone        string
	Subscriber   string
	ApomtTimeBgn string
	ApomtTimeEnd string
	ApomtDesc    string
	SubscribTime string
	CloseTime    string
}

////////////////////////////////////////////////////////////////////////////////////////////////////
//
//		System Message
//
const (
	MSG_Begin = 1

	MSG_HouseCertification = 1 // messages of house certificaion, ref to TblHouseCert
	MSG_AppointSeeHouse    = 2 // messages of house watching

	MSG_End = 2
)

const (
	MSG_PRIORITY_Begin   = 0
	MSG_PRIORITY_Info    = 0
	MSG_PRIORITY_Warning = 1
	MSG_PRIORITY_Error   = 2
	MSG_PRIORITY_End     = 2
)

type MsgHouse struct {
	Property    string
	BuildingNo  string
	HouseNo     string
	Bedrooms    int
	Livingrooms int
	Bathrooms   int
}

type SysMessage struct {
	Id         int64
	Type       int // MSG_XXX
	Receiver   int64
	Priority   int // MSG_PRIORITY_xxx
	Msg        string
	CreateTime string
	ReadTime   string
	House      MsgHouse
}

////////////////////////////////////////////////////////////////////////////////////////////////////
//
//		Events
//
const (
	HOUSE_EVENT_Begin = 1

	HOUSE_EVENT_Submit              = 1 // house owner submit a new house
	HOUSE_EVENT_Certification_Begin = 2
	HOUSE_EVENT_Certification_Fail  = 3
	HOUSE_EVENT_Certification_OK    = 4
	// HOUSE_EVENT_

	HOUSE_EVENT_End = 4
)

const (
	HOUSE_EVENT_PROC_Follow = 1
	HOUSE_EVENT_PROC_Close  = 2
)

type HouseEvents struct {
	HouseId   int64
	Property  string
	Building  string
	HouseNo   string
	Picture   int64
	EventCnt  int
	Time      string
	EventDesc string
}

type HouseEventInfo struct {
	Id         int64  // event id
	HouseId    int64  // house id
	Property   string // property name
	Building   string // bulding number
	HouseNo    string // house number
	Sender     string // Event Sender
	Receiver   string // Event Receiver
	CreateTime string // Event create time
	ReadTime   string // Event read time
	Type       string // Event type
	Desc       string // Event Description
	ProcCount  int64  // How many processing follows the event
}

type HouseEventProc struct {
	Id   int64  // proc id
	User string // who made this proc
	Time string // when made this proc
	Op   string // proc operation
	Desc string // proc description
}

////////////////////////////////////////////////////////////////////////////////////////////////////
//
//		Pictures
//
const (
	PIC_TYPE_USER   = 100
	PIC_TYPE_HOUSE  = 200
	PIC_TYPE_RENTAL = 300
)

// for PIC_TYPE_USER
const (
	PIC_OWNER_IDCard       = 1 // house owern's ID card, including ID card and passport. only house owner and house agent could access
	PIC_USER_HEAD_PORTRAIT = 2 // user's head portrait, anyone could access it
)

// for PIC_TYPE_HOUSE
const (
	PIC_HOUSE_BEGIN = 1

	PIC_HOUSE_FLOOR_PLAN    = 1 // house plan, anyone could access
	PIC_HOUSE_FURNITURE     = 2 // house furnitures, anyone could access
	PIC_HOUSE_APPLIANCE     = 3 // house appliance, andy could access
	PIC_HOUSE_OwnershipCert = 4 // house ownership certification, only the landlord, its agency and administrator could access

	PIC_HOUSE_END = 4
)

const (
	PIC_SIZE_ALL      = 0
	PIC_SIZE_ORIGINAL = 1
	PIC_SIZE_SMALL    = 2
	PIC_SIZE_MODERATE = 3
	PIC_SIZE_LARGE    = 4
	// PIC_SIZE_HEAD_PORTRAINT = 5 // head portraint, typically used for user picture
)

type HousePicture struct {
	Id       int64  // house picture id
	Desc     string // house picture description
	SubType  int    // house picture sub-type
	Checksum string // house picture checksum, md5
}
