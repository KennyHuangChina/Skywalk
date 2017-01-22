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
