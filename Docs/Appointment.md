# Appointment APIs

##
###1. make appointment of house seeing
	[Security]	public
	[Request]
  		* POST /v1/appointment/see/house/:id
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
