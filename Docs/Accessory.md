# Accessory APIs

### 1. Add deliverable (By administrator)
	[Security]	private
	[Request]
  		* POST /v1/house/deliverable
	  		* name			string		// new deliverable name
	[Response]
		* SUCCESS:200 
			* Id			int			// new deliverable id
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description


###2. Get deliverable list (By logined user)
	[Security]	private
	[Request]
  		* GET /v1/house/delivelst
	[Response]
		* SUCCESS:200 
			* Total			int			// total number
			* List			array
				* Id		int			// deliverable id
				* Name		string		// deliverable name
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description


### 3. Add deliverable for house (By logined user)
	[Security]	private
	[Request]
  		* POST /v1/house/housedeliv/:id
	  		* did			int			// deliverable id
	  		* qty			int			// quantity. 0 means delete this house deliverable
	  		* desc			string		// description 
  	[Response]
		* SUCCESS:200 
			* Id			int			// new house deliverable id
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description


### 4. Get House deliverable list (By logined user)
	[Security]	private
	[Request]
  		* GET /v1/house/hdl/:id
  	[Response]
		* SUCCESS:200 
			* DeliverableList	array
				* DeliverId		int		// house deliverable id
				* DeliverName	string	// house deliverable name
				* Qty			int		// house deliverable quantity
				* Desc			string	// house deliverable description 
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description


### 5. New Facility Type (By administrator)
	[Security]	private
	[Request]
  		* POST /v1/house/facilitytype
	  		* name				string	// facility type name
  	[Response]
		* SUCCESS:200 
			* Id				int		// new facility type id
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description


### 6. Get Facility Type List (By logined user)
	[Security]	private
	[Request]
  		* GET /v1/house/facitypelst
  	[Response]
		* SUCCESS:200 
			* Total				int			// total type quantity
			* FacilityTypes		array
				* Id			int			// faility type id
				* Name			string		// facility type name
		* ERR: 4XX,5XX
	  		* ErrCode			int			// error code
	  		* ErrDesc			string		// error description


### 7. New Facility (By administrator)
	[Security]	private
	[Request]
  		* POST /v1/house/facility
	  		* name			string		// facility name
	  		* type			int			// facility type id
  	[Response]
		* SUCCESS:200 
			* Id			int			// new facility id
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description


### 8. Get Facility List (By login user)
	[Security]	private
	[Request]
  		* GET /v1/house/facilitys?type=
	  		* type			int		// facility type id. 0 means all type
  	[Response]
		* SUCCESS:200
			* Total			int			// total facility quantity
			* Facilities	array 
				* Id		int			// facility id
				* Name		string		// facility name
				* Type		string		// facility type
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description


### 9. Add House Facility (By house owner, agency and administrator)
	[Security]	private
	[Request]
  		* POST /v1/accessory/house/:id/facilities
	  		* numb			int			// facility number
	  		* fid_0			int			// facility_0 id
	  		* fqty_0		int			// facility_0 quantity
	  		* fdesc_0		string		// facility_0 description
	  		* ...
	  		* fid_n			int			// facility_n id
	  		* fqty_n		int			// facility_n quantity
	  		* fdesc_n		string		// facility_n description
  	[Response]
		* SUCCESS:200
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description


### 10. Get House Facility List (By house owner, agency and administrator)
	[Security]	public
	[Request]
  		* GET /v1/accessory/house/:id/facilities
  	[Response]
		* SUCCESS:200
	  		* Total			int			// facility item quantity
	  		* Facilities	array
		  		* Id		int			// facility id
		  		* Name		string		// facility name
		  		* Type		string		// facility type name
		  		* Qty		ing 		// facility quantity
		  		* Desc		string		// facility description
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
