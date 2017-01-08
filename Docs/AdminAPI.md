# Admin APIs

##
###1. Get security picture
	[Request]
  		* GET /v1/admin/sec_pic
	[Response]
		* SUCCESS:200 
			* pic_session_id    string	// 16 char
			* pic_data  		string 	// picture data, in base64 format
		* ERR: 4XX,5XX
	  		* errCode		int			// error code
	  		* errDesc		string		// error description
##

###2. User Login
	[Request]
	[Response]
		* SUCCESS:200 
		* ERR: 4XX,5XX
	  		* errCode		int			// error code
	  		* errDesc		string		// error description
	
##

###3. User Info
	[Request]
  		* GET /v1/admin/user/:id?sid=xxx
  			* sid			string 		// session id, from which the server could know who send the request
	[Response]
		* SUCCESS:200 	// user info
			* Id				int			// user id
			* Name				string		// user name
			* Phone				string		// user phone number.
			* IdNo				string		// user ID number or passport number
			* HeadPortrait		string		// url of user head portrait picture
			* Role				int 		// 0 - agent manager, 1 - agent, 10 - customer, including house owner and house tenant
			* RoleDesc			string		// description of role
		* ERR: 4XX,5XX
	  		* errCode		int			// error code
	  		* errDesc		string		// error description
##

###4. agent rating
	[Request]
  		* GET /v1/admin/user/:id/rating?sid=xxx
  			* sid			string 		// session id, from which the server could know who send the request
	[Response]
		* SUCCESS:200 
			* professional	int		// agent professional rating, 0 ~5.0, it is 10 times than real value, so it should be devided by 10 before actual using, for example value 47 means 4.7 indeed
			* attitude		int 	// agent attitude rating, 0 ~ 5.0, same as prefessional rating
		* ERR: 4XX,5XX
	  		* errCode		int			// error code
	  		* errDesc		string		// error description
