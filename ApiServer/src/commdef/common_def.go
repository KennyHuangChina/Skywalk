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
	CoverImg     int64 // cover image id
	Tags         []HouseTags
}

type HouseInfo struct {
	Id          int64
	Property    int64
	BuildingNo  int
	FloorTotal  int
	FloorThis   int
	HouseNo     string
	Bedrooms    int
	Livingrooms int
	Bathrooms   int
	Acreage     int
	ForSale     bool
	ForRent     bool
	Decoration  int // TODO
}

type PropInfo struct {
	Id          int64
	PropName    string
	PropAddress string
	PropDesc    string
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
	BEHALF_TYPE_ALL     = 0
	BEHALF_TYPE_TO_RENT = 1
	BEHALF_TYPE_RENTED  = 2
	BEHALF_TYPE_TO_SALE = 3
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
	Qty  int    // facility quantity
	Desc string // facility description
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
	Building  int
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
	Building   int    // bulding number
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
	PIC_OWNER_ID           = 1 // house owern's ID card, including ID card and passport. only house owner and house agent could access
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
