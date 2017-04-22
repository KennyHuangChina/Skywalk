# House Info APIs

##
###1. Get house info
	[Security]	public/private
	[Request]
  		* GET /v1/house/:id?p=1
	  		* p					bool	// if get private info. 1: get; 0: not get
	[Response]
		* SUCCESS:200 
			* Id    			int		// house id
			* Property			int		// property id which the house belong to
			* BuildingNo		int		// the building number the house belong to. private info. 
			* FloorTotal		int 	// total floors
			* FloorThis			int		// exact floor the house resident. private info, 
											1)  < FloorTotal, actual floor of house
											2)	= FloorTotal + 1, means low storied
											3)	= FloorTotal + 2, means middle storied
											4)	= FloorTotal + 3, means high storied
			* HouseNo			string		// exact house number. like house 1305#. private info 
			* Bedrooms			int		// how many bedrooms whitin house
			* Livingrooms		int		// how many living rooms within house
			* Bathrooms			int		// how many bathrooms within house
			* Acreage			int 	// the exact acreage of house license, 100 times than real value, so it should be devided by 100 before using, for example, value 9848 mean 98.48 square meters
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
##


###2. Get brief public house info
	[Security]	public
	[Request]
  		* GET /v1/house/digest/id?sid=xxxxx
	  		* sid 			string	// session id, by which server could know who send the request
	[Response]
		* SUCCESS:200 
			* Id    		int		// id
			* Property		string	// property name
			* PropertyAddr	string	// property address
			* Bedrooms		int		// bedrooms quantity
			* LivingRooms	int		// living room quantity
			* Bathrooms		int		// bathroom quantity
			* Acreage		int		// the exact acreage of house license, 100 times than real value, so it should be devided by 100 before using, for example, value 9848 mean 98.48 square meters
			* Rental		int		// the exact rental that the house owner published, in 0.01RMB
			* Pricing		int		// <0: Rental depreciate; 0: Even, no change; >0: Rental raise
			* CoverImg		int		// cover image id
			* Tags			array	// house tags
				* TagId		int 	// tag id
				* TagDesc	string	// tag description string
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
##

###3. Get house list
	[Security]	public
	[Request]
  		* GET /v1/house/list?type=<>?bgn=<>&cnt=<>&sid=xxxxx
	  		* type			int 	// house type. 0: all; 1: recommend; 2: deducted; 3: new
	  		* bgn			int		// from which item to fetch
	  		* cnt			int		// how many item need to fetch. set to Zero to fetch total number
	  		* sid 			string	// session id, by which server could know who send the request
	[Response]
		* SUCCESS:200 
			* Total    		int		// total number
			* Count			int		// how many items fetched
			* IDs			array
				* Id		int		// house id
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
##


### 4. Add New House (By owner)
	[Security]	private
	[Request]
  		* POST /v1/house/commit
	  		* prop			int 	// property id
	  		* build			int		// building number
	  		* house			string 	// house number
	  		* floor_total	int		// total floor
	  		* floor_this	int		// this floor
			* Bedrooms		int		// bedrooms quantity
			* LivingRooms	int		// living room quantity
			* Bathrooms		int		// bathroom quantity
			* Acreage		int		// the exact acreage of house license, 100 times than real value, so it should be devided by 100 before using, for example, value 9848 mean 98.48 square meters
			* agen			int		// agency id
			* 4sale			int		// submit for sale. 1: yes; 0: no
			* 4rent			int		// submit for rent. 1: yes; 0: no
	[Response]
		* SUCCESS:200 
			* Id    		int		// new house id
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
##

### 5. modify House (By owner and agency)
	[Security]	private
	[Request]
  		* PUT /v1/house/:id
	  		* prop			int 	// property id
	  		* build			int		// building number
	  		* house			string 	// house number
	  		* floor_total	int		// total floor
	  		* floor_this	int		// this floor
			* Bedrooms		int		// bedrooms quantity
			* LivingRooms	int		// living room quantity
			* Bathrooms		int		// bathroom quantity
			* Acreage		int		// the exact acreage of house license, 100 times than real value, so it should be devided by 100 before using, for example, value 9848 mean 98.48 square meters
			* 4sale			int		// submit for sale. 1: yes; 0: no
			* 4rent			int		// submit for rent. 1: yes; 0: no
	[Response]
		* SUCCESS:200 
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
##

### 6. certificate House (By agency)
	[Security]	private
	[Request]
  		* POST /v1/house/cert/:id
	  		* cc			string		// certificate comment
	  		* ps			bool		// certificate result, pass or not
	[Response]
		* SUCCESS:200 
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
##

### 7. Set House cover image (By house owner and agency)
	[Security]	private
	[Request]
  		* PUT /v1/house/covimg/:id
	  		* img			int			// cover image id
	[Response]
		* SUCCESS:200 
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
##

### 8. recommend/unrecommend house (By agency)
	[Security]	private
	[Request]
  		* PUT /v1/house/recommend/:id
	  		* act    		int		// 1: recommend; 2: unrecommend
	[Response]
		* SUCCESS:200 
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
##

### 9. My represent houses (By agency)
	[Security]	private
	[Request]
  		* GET /v1/house/behalf?type=<>?bgn=<>&cnt=<>
	  		* type			int 	// house list type. 0: all; 1: to rent; 2: rented; 3: to sale
	  		* bgn			int		// from which item to fetch
	  		* cnt			int		// how many item need to fetch. set to Zero to fetch total number
	[Response]
		* SUCCESS:200 
			* Total    		int		// total number
			* Count			int		// how many items fetched
			* IDs			array
				* Id		int		// house id
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
##

