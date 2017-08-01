# House Watch APIs

##
### 1. Make house watch		// TODO:
	[Security]	private
	[Request]
  		* POST /v1/housewatch/see/house/:id
	  		* phone			string	// phone number. could be empty if user loggined 
	  		* pb			string	// period begin, like 9:00
	  		* pe			string 	// period end, like 9:30
	  		* desc			string	// appointment description
	[Response]
		* SUCCESS:200 
			* Aid    		int		// new appointment id
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description

### 2. Remove house watch 	// TODO:
	[Security]	private
	[Request]
  		* POST /v1/housewatch/see/house/:id
	  		* phone			string	// phone number. could be empty if user loggined 
	  		* pb			string	// period begin, like 9:00
	  		* pe			string 	// period end, like 9:30
	  		* desc			string	// appointment description
	[Response]
		* SUCCESS:200 
			* Aid    		int		// new appointment id
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description

### 3. Get House Watch list, by login user
	[Security]	private
	[Request]
  		* GET /v1/housewatch/user
	  		* bgn				int
	  		* fCnt				int
	[Response]
		* SUCCESS:200 
			* Total    			int		// total number
			* Count				int		// how many items fetched
			* HouseDigests		array	// house public brief info list
				* house public brief info
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description

### 4. Get House Watch list, by house 	// TODO: 
	[Security]	private
	[Request]
  		* GET /v1/housewatch/see/house/:id
	  		* phone			string	// phone number. could be empty if user loggined 
	  		* pb			string	// period begin, like 9:00
	  		* pe			string 	// period end, like 9:30
	  		* desc			string	// appointment description
	[Response]
		* SUCCESS:200 
			* Aid    		int		// new appointment id
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
