# House Info APIs

##
###1. Get house info
	[Security]	public
	[Request]
  		* GET /v1/house/id?sid=xxx
	  		* sid 			string	// session id, by which server could know who send the request
	[Response]
		* SUCCESS:200 
			* Id    			int		// house id
			* Property			int		// property id which the house belong to
			* BuildingNo		int		// the building number the house belong to
			* FloorTotal		int 	// total floors
			* Floorthis			int		// exact floor the house resident
			* HouseNo			string		// exact house number. like house 1305# 
			* Bedrooms			int		// how many bedrooms whitin house
			* Livingrooms		int		// how many living rooms within house
			* Bathrooms			int		// how many bathrooms within house
			* Acreage			int 	// the exact acreage of house license, 100 times than real value, so it should be devided by 100 before using, for example, value 9848 mean 98.48 square meters
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
##

###2. Get Property info
	[Security]	public
	[Request]
  		* GET /v1/house/property/id?sid=xxx
	  		* sid 			string	// session id, by which server could know who send the request
	[Response]
		* SUCCESS:200 
			* Id    		int		// property id
			* PropName		string	// property name
			* PropAddress	string	// property address
			* PropDesc		string	// property description
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
##

###3. Get brief public house info
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

###4. Get house list
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

###5. List properties
	[Security]	public
	[Request]
  		* GET /v1/house/property/list?name=<>&bgn=<>&cnt=<>
	  		* name			string 	// property name, whole or partial
	  		* bgn			int		// from which item to fetch
	  		* cnt			int		// how many item need to fetch. set to Zero to fetch total number
	[Response]
		* SUCCESS:200 
			* Total    		int		// total number
			* Count			int		// how many items fetched
			* properties	array
				* Id		int		// property id
				* PropName	string	// property name
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
##

### 6. Add New property
	[Security]	private
	[Request]
  		* POST /v1/house/property
	  		* prop			string 	// property name
	[Response]
		* SUCCESS:200 
			* Id    		int		// new property id
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
##

### 7. Add New House (By owner)
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
	[Response]
		* SUCCESS:200 
			* Id    		int		// new house id
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
##

### 8. modify House (By owner and agency)
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
	[Response]
		* SUCCESS:200 
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
##

### 9. certificate House (By agency)
	[Security]	private
	[Request]
  		* POST /v1/house/cert/:id
	[Response]
		* SUCCESS:200 
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
##

### 10. Set House cover image (By house owner and agency)
	[Security]	private
	[Request]
  		* PUT /v1/house/covimg/:id
	  		* img			int			// cover image id
	[Response]
		* SUCCESS:200 
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
