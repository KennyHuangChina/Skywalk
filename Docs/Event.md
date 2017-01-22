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
