# Property Info APIs
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
	  		* prop			string 		// property name
	  		* addr			string 		// property address
	  		* desc			string 		// property description
	[Response]
		* SUCCESS:200 
			* Id    		int		// new property id
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
##

### 11. Update property info (By house owner and agency)
	[Security]	private
	[Request]
  		* PUT /v1/property/:id/info
	  		* prop    		string		// property name
	  		* addr 			string		// property location
	  		* desc    		string		// property description
	[Response]
		* SUCCESS:200 
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description

###2. Get Property info
	[Security]	public
	[Request]
  		* GET /v1/house/property/:id
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
