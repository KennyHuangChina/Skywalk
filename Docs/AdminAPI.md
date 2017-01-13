# Admin APIs

##
###1. Get security picture
	[Request]
  		* GET /v1/admin/sec_pic
	[Response]
		* SUCCESS:200 
			* Capt
				* PicDataBase64	sring	// captcha url, like /captcha/ObjI3H2lLswp7OJ.png
				* GUID			string	// captcha GUID, like ObjI3H2lLswp7OJ
		* ERR: 4XX,5XX
	  		* errCode		int			// error code
	  		* errDesc		string		// error description
##

###2. User Login
	[Request]
  		* GET /v1/admin/login
	  		* lg			string 		// login name
	  		* pw			string		// password
	  		* rd			string		// random 
	[Response]
		* SUCCESS:200 
			* Sid			string		// session id
		* ERR: 4XX,5XX
	  		* errCode		int			// error code
	  		* errDesc		string		// error description
##

###3. Get User Salt
	[Request]
		* GET /v1/admin/salt?sid=xxx&un=xxx
  			* sid			string 		// session id, from which the server could know who send the request
			* un			string 		// user login name
	[Response]
		* SUCCESS:200 
		* ERR: 4XX,5XX
	  		* errCode		int			// error code
	  		* errDesc		string		// error description
	
##

###4. User Info
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

###5. agent rating
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
##

###6. fetch sms code
	[Scope]			APP/Web
	[Private]		public
	[Request]
  		* GET /v1/admin/fetchsms?ln=xxx
  			* ln		// login name. It is phone number actually
	[Response]
		* SUCCESS:200 
			* SmsCode		string		// sms code
		* ERR: 4XX,5XX
	  		* errCode		int			// error code
	  		* errDesc		string		// error description
##

###7. register customer
	[Scope]			APP/Web
	[Private]		public
	[Request]
  		* PUT /v1/admin/regcust
  			* ln		// login name. Typically it is phone number
  			* sms		// sms code
	[Response]
		* SUCCESS:200 
			* Uid			int			// new user id
		* ERR: 4XX,5XX
	  		* errCode		int			// error code
	  		* errDesc		string		// error description
