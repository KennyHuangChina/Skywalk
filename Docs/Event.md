# Event APIs

##
### 1. Get new message count
	[Security]	private
	[Request]
  		* GET /v1/event/count
	[Response]
		* SUCCESS:200 
			* NewEvent		int 		// how many new messages
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description

##
### 2. Get new message info by house
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
### 3. Read New Event
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
### 4. Get event by id
	[Security]	private
	[Request]
  		* GET /v1/event/:id
	  		* :id				int 		// event id
	[Response]
		* SUCCESS:200
			* Event
				* Id 			int 		// event id 
				* HouseId		int 		// house id
				* Property		string		// property name
				* Building		int			// bulding number
				* HouseNo		string		// house number
				* Sender		string 		// Event Sender
				* Receiver		string		// Event Receiver
				* CreateTime	string		// Event create time
				* ReadTime		string		// Event read time
				* Type			string		// Event type
				* Desc			string		// Event Description
				* ProcCount		int 		// How many processing follows the event
		* ERR: 4XX,5XX
	  		* ErrCode			int			// error code
	  		* ErrDesc			string		// error description

##
### 5. Get event processing
	[Security]	private
	[Request]
  		* GET /v1/event/:id/proc
	  		* :id				int 		// event id
	[Response]
		* SUCCESS:200
			* Total				int			// total number of event proc
			* Count 			int			// event proc number fetched
			* ProcList
				* Id 			int 		// proc id
				* User			string		// who made this proc
				* Time			string		// when made this proc
				* Op			string		// proc operation
				* Desc			string		// proc description
		* ERR: 4XX,5XX
	  		* ErrCode			int			// error code
	  		* ErrDesc			string		// error description

##
### 6. Get house event list
	[Security]	private
	[Request]
  		* GET /v1/event/list/house/:id?stat=..&type=..&bgn=..&cnt=..&ido=..
	  		* :id				int 		// house id
	  		* stat				int 		// 0 - all events; 1 - new events; 2 - unclosed event; 3 - closed events
	  		* type				int 		// event type. 0 means all kind of events
	  		* bgn				int 		// begin position from where to fetch
	  		* cnt				int 		// how many records want to fetch
	  		* ido				int 		// if only fetch event id. true - fetch evvent id / false - fetch whole event info
	[Response]
		* SUCCESS:200
			* Total				int			// total number of event proc
			* Count 			int			// event proc number fetched
			* EventLst
				* Id 			int 		// event id 
				* HouseId		int 		// house id
				* Property		string		// property name
				* Building		int			// bulding number
				* HouseNo		string		// house number
				* Sender		string 		// Event Sender
				* Receiver		string		// Event Receiver
				* CreateTime	string		// Event create time
				* ReadTime		string		// Event read time
				* Type			string		// Event type
				* Desc			string		// Event Description
				* ProcCount		int 		// How many processing follows the event
		* ERR: 4XX,5XX
	  		* ErrCode			int			// error code
	  		* ErrDesc			string		// error description

##
### 7. Update event info
	[Security]	private
	[Request]
  		* PUT /v1/event/:id
	  		* :id				int 		// house id
	  		* desc				string 		// event description
	[Response]
		* SUCCESS:200
		* ERR: 4XX,5XX
	  		* ErrCode			int			// error code
	  		* ErrDesc			string		// error description
