# System Message APIs

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
### 2. Get message list 
	[Security]	private
	[Request]
  		* GET /v1/sysmsg/lst?bgn=<>&cnt=<>&ff=<>&nm=<>
	  		* bgn			int 	// from where to begin fetch. zero-based
	  		* cnt			int		// how many records try to fetch
	  		* ff			int		// full fetch? 1: yes(msg info), 0: no(id only)
	  		* nm			int		// new message? 1: yes, only feth new message; 0: no, fetch all message
	[Response]
		* SUCCESS:200 
			* Total				int 		// total number
			* Count				int			// actual fetched number
			* Msgs
				* Id 			int 		// message id 
				* Type			int			// message type. 1 - House Certification. 2 - Planed House Watch
				* Receiver		int			// message receiver
				* Priority		int			// message priority. 0 - info. 1 - Warning. 2 - Error
				* Msg			string		// message text
				* CreateTime	string		// message create time
				* ReadTime		string		// message read time
				* House
					* HouseId		int 		// house id
					* Property		string		// property name
					* BuildingNo	int			// building number
					* HouseNo		string		// house number
					* Livingrooms	int			// livingroom quantity
					* Bedrooms		int			// bedroom quantity
					* Bathrooms		int			// bathroom quantity 
		* ERR: 4XX,5XX
	  		* ErrCode			int			// error code
	  		* ErrDesc			string		// error description

##
### 3. Read New message
	[Security]	private. Only the message receiver could set the read status
	[Request]
  		* PUT /v1/sysmsg/:id/read
	  		* :id				int 		// message id
	[Response]
		* SUCCESS:200
		* ERR: 4XX,5XX
	  		* ErrCode			int			// error code
	  		* ErrDesc			string		// error description

##
### 4. Get message by id 
	[Security]	private
	[Request]
  		* GET /v1/sysmsg/:id
	  		* :id				int 		// message id
	[Response]
		* SUCCESS:200
			* Msg
				* Id 			int 		// message id 
				* Type			int			// message type. 1 - House Certification. 2 - Planed House Watch
				* Receiver		int			// message receiver
				* Priority		int			// message priority. 0 - info. 1 - Warning. 2 - Error
				* Msg			string		// message text
				* CreateTime	string		// message create time
				* ReadTime		string		// message read time
				* House
					* HouseId		int 		// house id
					* Property		string		// property name
					* BuildingNo	int			// building number
					* HouseNo		string		// house number
					* Livingrooms	int			// livingroom quantity
					* Bedrooms		int			// bedroom quantity
					* Bathrooms		int			// bathroom quantity 
		* ERR: 4XX,5XX
	  		* ErrCode			int			// error code
	  		* ErrDesc			string		// error description


