# Picture APIs

##
###1. Get picture url
	[Security]	based on who is using, some pictures are pulbic, but some pictures are private
	[Request]
  		* GET /v1/pic/:id?sid=xxx&size=
	  		* sid 			string	// session id, by which server could know who send the request
	  		* size			int		// picture size, 1 - small; 2 - moderate; 3 - large
	[Response]
		* SUCCESS:200 
			* Url			string 		// picture path url
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
