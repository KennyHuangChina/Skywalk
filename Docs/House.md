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

###3. Get bref public house info
	[Security]	public
	[Request]
  		* GET /v1/house/digest/id?sid=xxxxx
	  		* sid 			string	// session id, by which server could know who send the request
	[Response]
		* SUCCESS:200 
			* Id    		int		// id
			* Property		string	// property name
			* PropertyAddr	string	// property address
			* Bedrooms		int		// bathrooms quantity
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
