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

////////////////////////////////////////////////////////////////////////////////////////////////////
//
//		Pictures
//
const (
	PIC_TYPE_USER   = 100
	PIC_TYPE_HOUSE  = 200
	PIC_TYPE_RENTAL = 300
)

// for PIC_TYPE_HOUSE
const (
	PIC_HOUSE_FLOOR     = 1
	PIC_HOUSE_FURNITURE = 2
	PIC_HOUSE_APPLIANCE = 3
)

const (
	PIC_SIZE_SMALL    = 1
	PIC_SIZE_MODERATE = 2
	PIC_SIZE_LARGE    = 3
	// PIC_SIZE_HEAD_PORTRAINT = 4 // head portraint, typically used for user picture
)
