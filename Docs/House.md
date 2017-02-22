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
			* Properties	array
				* Id			int		// property id
				* PropName		string	// property name
				* PropAddress	string	// property address
				* PropDesc		string	// property description
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
			* 4sale			int		// submit for sale. 1: yes; 0: no
			* 4rent			int		// submit for rent. 1: yes; 0: no
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
			* 4sale			int		// submit for sale. 1: yes; 0: no
			* 4rent			int		// submit for rent. 1: yes; 0: no
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
##

### 11. Update property info (By house owner and agency)
	[Security]	private
	[Request]
  		* PUT /v1/house/property/:id
	  		* name    		string		// property name
	  		* addr 			string		// property location
	  		* desc    		string		// property description
	[Response]
		* SUCCESS:200 
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
##

### 12. recommend/unrecommend house (By agency)
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

### 13. My represent houses (By agency)
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

### 14. Add deliverable (By administrator)
	[Security]	private
	[Request]
  		* POST /v1/house/deliverable
	  		* name			string		// new deliverable name
	[Response]
		* SUCCESS:200 
			* Id			int			// new deliverable id
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
##

### 15. Get deliverable list (By logined user)
	[Security]	private
	[Request]
  		* GET /v1/house/delivelst
	[Response]
		* SUCCESS:200 
			* Deliverable	array
				* Id		int			// deliverable id
				* Name		string		// deliverable name
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
##

### 16. Add deliverable for house (By logined user)
	[Security]	private
	[Request]
  		* POST /v1/house/housedeliv/:id
	  		* did			int			// deliverable id
	  		* qty			int			// quantity. 0 means delete this house deliverable
	  		* desc			string		// description 
  	[Response]
		* SUCCESS:200 
			* Deliverable	array
				* Id		int			// new house deliverable id
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
##

### 17. Get House deliverable list (By logined user)
	[Security]	private
	[Request]
  		* GET /v1/house/hdl/:id
  	[Response]
		* SUCCESS:200 
			* DeliverableList	array
				* DeliverId		int		// house deliverable id
				* DeliverName	string	// house deliverable name
				* Qty			int		// house deliverable quantity
				* Desc			string	// house deliverable description 
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
##

### 18. New Facility Type (By administrator)
	[Security]	private
	[Request]
  		* POST /v1/house/facilitytype
	  		* name				string	// facility type name
  	[Response]
		* SUCCESS:200 
			* Id				int		// new facility type id
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
##

### 19. Get Facility Type List (By logined user)
	[Security]	private
	[Request]
  		* GET /v1/house/facitypelst
  	[Response]
		* SUCCESS:200 
			* Total				int			// total type quantity
			* FacilityTypes		array
				* Id			int			// faility type id
				* Name			string		// facility type name
		* ERR: 4XX,5XX
	  		* ErrCode			int			// error code
	  		* ErrDesc			string		// error description
##

### 20. New Facility (By administrator)
	[Security]	private
	[Request]
  		* POST /v1/house/facility
	  		* name			string		// facility name
	  		* type			int			// facility type id
  	[Response]
		* SUCCESS:200 
			* Id			int			// new facility id
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
##

### 21. Get Facility List (By login user)
	[Security]	private
	[Request]
  		* GET /v1/house/facilitys?type=
	  		* type			int		// facility type id. 0 means all type
  	[Response]
		* SUCCESS:200
			* Total			int			// total facility quantity
			* Facilities	array 
				* Id		int			// facility id
				* Name		string		// facility name
				* Type		string		// facility type
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
##

### 22. Add House Facility (By house owner, agency and administrator)
	[Security]	private
	[Request]
  		* POST /v1/house/housefacilities/:id
	  		* numb			int			// facility number
	  		* fid_0			int			// facility_0 id
	  		* fqty_0		int			// facility_0 quantity
	  		* fdesc_0		string		// facility_0 description
	  		* ...
	  		* fid_n			int			// facility_n id
	  		* fqty_n		int			// facility_n quantity
	  		* fdesc_n		string		// facility_n description
  	[Response]
		* SUCCESS:200
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
##

### 23. Get House Facility List (By house owner, agency and administrator)
	[Security]	public
	[Request]
  		* GET /v1/house/housefacilities/:id
  	[Response]
		* SUCCESS:200
	  		* Total			int			// facility item quantity
	  		* Facilities	array
		  		* Id		int			// facility id
		  		* Name		string		// facility name
		  		* Type		string		// facility type name
		  		* Qty		ing 		// facility quantity
		  		* Desc		string		// facility description
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
