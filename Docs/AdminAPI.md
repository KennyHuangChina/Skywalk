# Admin APIs

##
##1. Get security picture
	[Request]
  		* GET /v1/admin/sec_pic
	[Response]
		* SUCCESS:200 
			* pic_session_id    string	// 16 char
			* pic_data  		string 	// picture data, in base64 format
		* ERR: 4XX,5XX
	  		* errCode		int			// error code
	  		* errDesc		string		// error description

