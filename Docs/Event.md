# Event APIs

##
###1. Get new message count
	[Security]	private
	[Request]
  		* GET /v1/event/count?sid=xxx
	  		* sid 			string	// session id, by which server could know who send the request
	[Response]
		* SUCCESS:200 
			* NewEvent		int 		// how many new messages
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description

##
###2. Get new message info by house
	[Security]	private
	[Request]
  		* GET /v1/event/houses?sid=xxx
	  		* sid 			string	// session id, by which server could know who send the request
	[Response]
		* SUCCESS:200 
			* House				array
				* HouseId		int 	// house id
				* Property		string	// property name
				* Building		int		// bulding number
				* HouseNo		string	// house number
				* Picture		int		// picture id
				* EventCnt		int		// new event number
				* Time			string	// newest event time
				* Desc			string	// newest event description
		* ERR: 4XX,5XX
	  		* ErrCode			int			// error code
	  		* ErrDesc			string		// error description
