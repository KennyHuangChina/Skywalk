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

###2. User Login by password
	[Request]
  		* POST /v1/admin/loginpass
	  		* ver			int			// version number
	  		* ln			string 		// login name
	  		* pw			string		// password
	  		* rd			string		// random
	  		* typ			ing 		// client type. 0 - web; 1 - APP
	  		* psid			string		// picture session id. ref to captcha GUID of API sec_pic
	  		* pss			string 		// picture result
	[Response]
		* SUCCESS:200 
			* Sid			string		// session id
		* ERR: 4XX,5XX
	  		* errCode		int			// error code
	  		* errDesc		string		// error description
##

###3. Get User Salt
	[Request]
		* GET /v1/admin/salt?un=xxx
			* un			string 		// user login name
	[Response]
		* SUCCESS:200
			* Salt   	string			// user salt
			* Random 	string			// random for this time
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

###7. login by sms
	[Scope]			APP/Web
	[Private]		public
	[Request]
  		* POST /v1/admin/loginsms
	  		* ver			int 		// API version
	  		* ln			string 		// login name. should be phone number
  			* sms			string 		// sms code
	  		//* rd			string		// random
	  		* typ			ing 		// client type. 0 - web; 1 - APP
	  		* psid			string		// picture session id. ref to captcha GUID of API sec_pic
	  		* pss			string 		// picture result
	[Response]
		* SUCCESS:200 
			* Sid			string		// session id
		* ERR: 4XX,5XX
	  		* errCode		int			// error code
	  		* errDesc		string		// error description
##

###8. logout
	[Scope]			APP/Web
	[Private]		public
	[Request]
  		* POST /v1/admin/logout
	[Response]
		* SUCCESS:200 
		* ERR: 4XX,5XX
	  		* errCode		int			// error code
	  		* errDesc		string		// error description
##

###9. Relogin: User relogin automatically when session expired, by previous session id
	[Scope]			APP
	[Private]		public
	[Request]
  		* POST /v1/admin/relogin/:id
	  		* :id			string		// last session id
	  		* ver			int			// version number
	  		* ln			string		// login name, phone number or login name
	  		* rd			string		// random
	[Response]
		* SUCCESS:200 
		* ERR: 4XX,5XX
	  		* errCode		int			// error code
	  		* errDesc		string		// error description
##

### 10. Get Agency List
	[Scope]			APP
	[Private]		private: all logined user
	[Request]
  		* GET /v1/admin/AgencyList?bgn=<>&cnt=<>
	  		* bgn			int		// from which item to fetch
	  		* cnt			int		// how many item need to fetch. set to Zero to fetch total number
	[Response]
		* SUCCESS:200 
			* Total    			int		// total number
			* Count				int		// how many items fetched
			* Agencys			array
				* Id			int 		// agency id
				* Name			string		// agency name
				* Sex			int 		// agency sex. 0 - male / 1 - female
				* IDNo			string		// agency ID card
				* Professional	int			// professional rank. 0 ~ 50 (0.0 ~ 5.0)
				* Attitude		int			// attitude rank. 0 ~50 (0.0 ~ 5.0)
				* Portrait		string		// head portraint picture URL
				* Phone			string		// phone number
		* ERR: 4XX,5XX
	  		* errCode		int			// error code
	  		* errDesc		string		// error description
