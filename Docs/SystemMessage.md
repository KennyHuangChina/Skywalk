# Event APIs

##
### 1. Get new message count
	[Security]	private
	[Request]
  		* GET /v1/sysmsg/newmsg
	[Response]
		* SUCCESS:200 
			* NewMsgCnt		int 		// how many new messages
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description

##
### 2. Get new message info by house (TODO)
	[Security]	private
	[Request]
  		* GET /v1/event/houses
	[Response]
		* SUCCESS:200 
			* Houses				array
				* HouseId		int 		// house id
				* Property		string		// property name
				* Building		int			// bulding number
				* HouseNo		string		// house number
				* Picture		int			// picture id
				* EventCnt		int			// new event number
				* Time			string		// newest event time
				* Desc			string		// newest event description
		* ERR: 4XX,5XX
	  		* ErrCode			int			// error code
	  		* ErrDesc			string		// error description

##
### 3. Read New Event (TODO)
	[Security]	private. Only the event receiver could set the read status
	[Request]
  		* PUT /v1/event/:id/read
	  		* :id				int 		// event id
	[Response]
		* SUCCESS:200
		* ERR: 4XX,5XX
	  		* ErrCode			int			// error code
	  		* ErrDesc			string		// error description

##
### 4. Get message by id (TODO)
	[Security]	private
	[Request]
  		* GET /v1/sysmsg/:id
	  		* :id				int 		// event id
	[Response]
		* SUCCESS:200
			* Msg
				* Id 			int 		// event id 
				* Type			int			// message type. 1 - House Certification. 2 - Planed House Watch
				* Priority		int			// message priority. 0 - info. 1 - Warning. 2 - Error
				* Msg			string		// message text
				* CreateTime	string		// Event create time
				* ReadTime		string		// Event read time
				* House
					* HouseId	int 		// house id
					* Property	string		// property name
					* Building	int			// building number
					* HouseNo	string		// house number
					* Lrooms	int			// livingroom quantity
					* bedrooms	int			// bedroom quantity
					* bathrooms	int			// bathroom quantity 
		* ERR: 4XX,5XX
	  		* ErrCode			int			// error code
	  		* ErrDesc			string		// error description


