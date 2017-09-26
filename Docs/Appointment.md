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
##

### 3. Get house list of house seeing appointment
	[Security]	private: landlorad, House Agency and administrator
				public: could see how many people already ordered
	[Request]
  		* GET /v1/appointment/seeHouselist
	  		* bgn				int
	  		* fCnt				int
	[Response]
		* SUCCESS:200 
			* Total    			int		// total number
			* Count				int		// how many items fetched
			* HouseDigests		array	// house public brief info list
				* house public brief info
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
##

### 4. make action for an appointment
	[Security]	private: landlorad, House Agency and administrator
	[Request]
  		* POST /v1/appointment/:id/act
	  		* act				int		// appointment action. 
	  		* tb				string	// time begin
	  		* te				string	// time end
	  		* ac				string	// action comments
	[Response]
		* SUCCESS:200 
			* Id			int			// new action id
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
##

### 5. assign appointment receptionist
	[Security]	private: administrator
	[Request]
  		* PUT /v1/appointment/:id/recpt?r=<>
	  		* r				int		// appointment receptionist id. 
	[Response]
		* SUCCESS:200 
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
##

### 6. get appointment info
	[Security]	private: appointment subscriber, appointment receptionist & administrator
	[Request]
  		* GET /v1/appointment/:id
	[Response]
		* SUCCESS:200
			* House
				* HouseId		int 		// house id
				* Property		string		// property name
				* BuildingNo	int			// building number
				* HouseNo		string		// house number
				* Livingrooms	int			// livingroom quantity
				* Bedrooms		int			// bedroom quantity
				* Bathrooms		int			// bathroom quantity 
			* Date				string		// appointment date
			* TimeBegin			string		// appointment begin time
			* TimeEnd			string		// appointment end time
			* Subscriber		string		// subscriber name
			* SubscriberPhone	string		// phone number of subscriber
			* Receptionist		string		// receptionist name
			* ReceptionistPhone	string		// phone number of receptionist
			* ApmtDesc			string 		// appointment description
			* SubscribeTime		string		// suberibe time
			* Actions
				* Id			int			// action id
				* Act			int 		// action type
				* Who			string		// people who made the action
				* When			string		// when the people made the action
				* TimeBegin		string		// new begin time
				* TimeEnd		string		// new end time
				* Comment		string		// action comments
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
