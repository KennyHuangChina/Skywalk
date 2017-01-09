# House Info APIs

##
###1. Get house info
	[Request]
  		* GET /v1/house/id?sid=xxx
	  		* sid 			string	// session id, by which server could know who send the request
	[Response]
		* SUCCESS:200 
			* House_id    	int		// house id
			* Property		int		// property id which the house belong to
			* Building_No	int		// the building number the house belong to
			* Floor_Total	int 	// total floors
			* Floor_this	int		// exact floor the house resident
			* House_No		int		// exact house number. like house 1305# 
			* Bedrooms		int		// how many bedrooms whitin house
			* Livingrooms	int		// how many living rooms within house
			* Bathrooms		int		// how many bathrooms within house
			* Acreage		int 	// the exact acreage of house license, 100 times than real value, so please devided by 100 before using, for example, value 9848 mean 98.48 square meters
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
