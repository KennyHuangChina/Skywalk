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
##

### 2. Get appointments of house seeing
	[Security]	private: landlorad, House Agency and administrator
				public: could see how many people already ordered
	[Request]
  		* GET /v1/appointment/house/:id/seelist?
	  		* bgn				int
	  		* fCnt				int
	[Response]
		* SUCCESS:200 
			* Total    			int		// total number
			* Count				int		// how many items fetched
			* Appointments		array
				* Id				int
				* ApomtType			int
				* TypeDesc			string
				* House				int		// house id
				* Phone				string	// contact phone
				* Subscriber		string	// subscriber
				* ApomtTimeBgn		string	// subscribe period, begin
				* ApomtTimeEnd		string	// subscribe period, end
				* ApomtDesc			string	// subscribe description
				* SubscribTime		string	// subscribe time
				* CloseTime			string	// order close time
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
