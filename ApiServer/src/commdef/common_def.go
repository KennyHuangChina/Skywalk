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
	CoverImgS    int64 // small cover image id
	CoverImgL    int64 // Large cover image id
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
