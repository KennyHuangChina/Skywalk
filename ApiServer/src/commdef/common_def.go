package commdef

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
	BuddingNo   int
	FloorTotal  int
	FloorThis   int
	HouseNo     string
	Bedrooms    int
	Livingrooms int
	Bathrooms   int
	Acreage     int
	ForSale     bool
	ForRent     bool
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
	HOUSE_EVENT_Submit              = 1 // house owner submit a new house
	HOUSE_EVENT_Certification_Begin = 2
	HOUSE_EVENT_Certification_Fail  = 3
	HOUSE_EVENT_Certification_OK    = 4
	// HOUSE_EVENT_
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
	PIC_HOUSE_FLOOR     = 1 // house type image, anyone could access
	PIC_HOUSE_FURNITURE = 2 // house furnitures, anyone could access
	PIC_HOUSE_APPLIANCE = 3 // house appliance, andy could access
)

const (
	PIC_SIZE_ALL      = 0
	PIC_SIZE_SMALL    = 1
	PIC_SIZE_MODERATE = 2
	PIC_SIZE_LARGE    = 3
	// PIC_SIZE_HEAD_PORTRAINT = 4 // head portraint, typically used for user picture
)
