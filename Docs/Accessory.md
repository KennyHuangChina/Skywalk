# Accessory APIs

### 1. Add deliverable (By administrator)
	[Security]	private
	[Request]
  		* POST /v1/accessory/deliverable
	  		* name			string		// new deliverable name
	[Response]
		* SUCCESS:200 
			* Id			int			// new deliverable id
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description


### 2. Get deliverable list (By logined user)
	[Security]	private
	[Request]
  		* GET /v1/accessory/deliverables
	[Response]
		* SUCCESS:200 
			* Total			int			// total number
			* List			array
				* Id		int			// deliverable id
				* Name		string		// deliverable name
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description

### 3. Modify Deliverable (By administrator)
	[Security]	private
	[Request]
  		* PUT /v1/accessory/deliverable/:id
	[Response]
	  		* name			string		// new deliverable name
	[Response]
		* SUCCESS:200 
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description

### 4. Delete Deliverable (By administrator)

### 5. Add deliverable for house (By logined user)
	[Security]	private
	[Request]
  		* POST /v1/accessory/house/:id/deliverable
	  		* did			int			// deliverable id
	  		* qty			int			// quantity. 0 means delete this house deliverable
	  		* desc			string		// description 
  	[Response]
		* SUCCESS:200 
			* Id			int			// new house deliverable id
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description

### 6. Modify house deliverable (By logined user)
	[Security]	private
	[Request]
  		* PUT /v1/accessory/deliverable/:id
	  		* did			int			// deliverable id
	  		* qty			int			// quantity. 0 means delete this house deliverable
	  		* desc			string		// description 
  	[Response]
		* SUCCESS:200 
			* Id			int			// new house deliverable id
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description

### 7. delete house deliverable (By logined user)

### 8. Get House deliverable list (By logined user)
	[Security]	private
	[Request]
  		* GET /v1/accessory/house/:id/deliverables
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


### 9. New Facility Type (By administrator)
	[Security]	private
	[Request]
  		* POST /v1/accessory/facility/type
	  		* name				string	// facility type name
  	[Response]
		* SUCCESS:200 
			* Id				int		// new facility type id
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description


### 10. Get Facility Type List (By logined user)
	[Security]	private
	[Request]
  		* GET /v1/accessory/facitypelst
  	[Response]
		* SUCCESS:200 
			* Total				int			// total type quantity
			* FacilityTypes		array
				* Id			int			// faility type id
				* Name			string		// facility type name
		* ERR: 4XX,5XX
	  		* ErrCode			int			// error code
	  		* ErrDesc			string		// error description

### 11. Modify Facility Type (By administrator)
	[Security]	private
	[Request]
  		* PUT /v1/accessory/facility/type/:id
	  		* id 				int 		// facility type id
	  		* name				string		// type name
  	[Response]
		* SUCCESS:200 
		* ERR: 4XX,5XX
	  		* ErrCode			int			// error code
	  		* ErrDesc			string		// error description

### 12. delete Facility Type (By administrator)
	[Security]	private
	[Request]
  		* DELETE /v1/accessory/facility/type/:id
	  		* id 				int 		// facility type id
  	[Response]
		* SUCCESS:200 
		* ERR: 4XX,5XX
	  		* ErrCode			int			// error code
	  		* ErrDesc			string		// error description

### 13. New Facility (By administrator)
	[Security]	private
	[Request]
  		* POST /v1/accessory/facility
	  		* name			string		// facility name
	  		* type			int			// facility type id
		  	* 			    file		// icon file
  	[Response]
		* SUCCESS:200 
			* Id			int			// new facility id
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description


### 14. Get Facility List (By login user)
	[Security]	private
	[Request]
  		* GET /v1/accessory/facilitys?type=
	  		* type			int		// facility type id. 0 means all type
  	[Response]
		* SUCCESS:200
			* Total			int			// total facility quantity
			* Facilities	array 
				* Id		int			// facility id
				* Name		string		// facility name
				* Type		string		// facility type
				* Icon		string 		// Icon url
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description

### 15. Modify Facility (By administrator)
	[Security]	private
	[Request]
  		* PUT /v1/accessory/facility/:id
	  		* id			int 		// facility id
	  		* name			string		// facility name
	  		* type			int			// facility type id
		  	* 			    file		// icon file
  	[Response]
		* SUCCESS:200
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description

### 16. Delete Facility (By administrator) ** Not Implement **
	[Security]	private
	[Request]
  		* DELETE /v1/accessory/facility/:id
	  		* id			int 		// facility id
  	[Response]
		* SUCCESS:200
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description


### 17. Add House Facility (By house owner, agency and administrator)
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

### 18. Get House Facility List 
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
		  		* Icon		string 		// facility Icon url
		  		* Qty		ing 		// facility quantity
		  		* Desc		string		// facility description
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description

### 19. Modify House Facility (landlord, its agency, administrator) 
	[Security]	private
	[Request]
  		* PUT /v1/accessory/housefacility/:id
	  		* fid			int			// facility id
	  		* fqty			int			// facility quantity
	  		* fdesc			string		// facility description
  	[Response]
		* SUCCESS:200
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description

### 20. Delete House Facility (landlord, its agency, administrator) 
	[Security]	private
	[Request]
  		* DELETEDELETE /v1/accessory/housefacility/:id
  	[Response]
		* SUCCESS:200
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
