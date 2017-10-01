# House Info APIs

##
###1. Get house info
	[Security]	public/private
	[Request]
  		* GET /v1/house/:id?be=1
	  		* be				bool	// if is for back end using, 1 -- back end, 2 -- front end
	[Response]
		* SUCCESS:200 
			* Id    			int		// house id
			* Property			int		// property id which the house belong to
			* BuildingNo		string	// the building number the house belong to. private info. 
			* FloorTotal		int 	// total floors
			* FloorThis			int		// exact floor the house resident. private info, 
											1)  < FloorTotal, actual floor of house
											2)	= FloorTotal + 1, means low storied
											3)	= FloorTotal + 2, means middle storied
											4)	= FloorTotal + 3, means high storied
			* HouseNo			string		// exact house number. like house 1305#. private info 
			* Bedrooms			int		// how many bedrooms whitin house
			* Livingrooms		int		// how many living rooms within house
			* Bathrooms			int		// how many bathrooms within house
			* Acreage			int 	// the exact acreage of house license, 100 times than real value, so it should be devided by 100 before using, for example, value 9848 mean 98.48 square meters
			* BuyDate			string	// the exact date buying this house
			* ModifyDate		string	// the date of modifying the house info
			* Agency			int 	// house agency id
			* ForSale			boolean	// house for sale
			* ForRent			boolean	// house for rent
			* RentStat			int 	// 1: wait for renting, 2: rented, 3: Due, open for ordering
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
##


###2. Get brief public house info
	[Security]	public
	[Request]
  		* GET /v1/house/:id/digest
	[Response]
		* SUCCESS:200 
			* Id    		int		// id
			* Property		string	// property name
			* PropertyAddr	string	// property address
			* Bedrooms		int		// bedrooms quantity
			* LivingRooms	int		// living room quantity
			* Bathrooms		int		// bathroom quantity
			* Acreage		int		// the exact acreage of house license, 100 times than real value, so it should be devided by 100 before using, for example, value 9848 mean 98.48 square meters
			* Rental		int		// the exact rental that the house owner published, in 0.01RMB
			* Pricing		int		// <0: Rental depreciate; 0: Even, no change; >0: Rental raise
			* CoverImg		int		// cover image id
			* CovImgUrlS	string	// cover image url for small size
			* CovImgUrlM	string	// cover image url for modrate size
			* Tags			array	// house tags
				* TagId		int 	// tag id
				* TagDesc	string	// tag description string
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
##

###3. Get house list
	[Security]	public
	[Request]
  		* GET /v1/house/list?type=<>?bgn=<>&cnt=<>
	  		* type			int 	// house type. 0: all; 1: recommend; 2: deducted; 3: new
	  		* bgn			int		// from which item to fetch
	  		* cnt			int		// how many item need to fetch. set to Zero to fetch total number
	  		* rtop			int 	// operator. o: ==; 1: <; 2: <=; 3: >; 4: >=; 5: Between
	  		* rt			int     // rental values
	  		* lvop			int		// 
	  		* lr			int		// livingroom number values
	  		* berop			int		// 
	  		* ber			int 	// bedroom number values
	  		* barop			int		// 
	  		* bar			int		// bathroom number values
	  		* acop			int		// 
	  		* ac			int		// acreage values
	  		* tags			string	// tags array. like 1,3,5,7
	  		* sort			int		// sort condition array. like 1,3,5,7 
			  						//  1: publish time, 	 2: publish time desc
									//  3: rental, 			 4: rental desc
									//  5: appointment		 6: appointment desc
	[Response]
		* SUCCESS:200 
			* Total    		int		// total number
			* Count			int		// how many items fetched. -1 means just fetch the total number
			* HouseDigests	array	// house public brief info list
				* house public brief info
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
##


### 4. Add New House (By owner)
	[Security]	private
	[Request]
  		* POST /v1/house/commit
	  		* prop			int 	// property id
	  		* build			string	// building number
	  		* house			string 	// house number
	  		* floor_total	int		// total floor
	  		* floor_this	int		// this floor
			* Bedrooms		int		// bedrooms quantity
			* LivingRooms	int		// living room quantity
			* Bathrooms		int		// bathroom quantity
			* Acreage		int		// the exact acreage of house license, 100 times than real value, so it should be devided by 100 before using, for example, value 9848 mean 98.48 square meters
			* agen			int		// agency id
			* 4sale			int		// submit for sale. 1: yes; 0: no
			* 4rent			int		// submit for rent. 1: yes; 0: no
			* decor			int		// decoration. 0 - Workblank / 1 - Simple / 2 - Medium / 3 -Refined / 4 - Luxury
			* buy_date		string	// buying date. 2016-01-02
	[Response]
		* SUCCESS:200 
			* Id    		int		// new house id
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
##

### 5. modify House (By owner and agency)
	[Security]	private
	[Request]
  		* PUT /v1/house/:id
	  		* prop			int 	// property id
	  		* build			string	// building number
	  		* house			string 	// house number
	  		* floor_total	int		// total floor
	  		* floor_this	int		// this floor
			* Bedrooms		int		// bedrooms quantity
			* LivingRooms	int		// living room quantity
			* Bathrooms		int		// bathroom quantity
			* Acreage		int		// the exact acreage of house license, 100 times than real value, so it should be devided by 100 before using, for example, value 9848 mean 98.48 square meters
			* 4sale			int		// submit for sale. 1: yes; 0: no
			* 4rent			int		// submit for rent. 1: yes; 0: no
			* decor			int		// decoration. 0 - Workblank / 1 - Simple / 2 - Medium / 3 -Refined / 4 - Luxury
			* buy_date		string	// buying date. 2016-01-02
	[Response]
		* SUCCESS:200 
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
##

### 6. certificate House (By agency & administrator)
	[Security]	private, agency and administrator
	[Request]
  		* POST /v1/house/cert/:id
	  		* cc			string		// certificate comment
	  		* ps			bool		// certificate result, pass or not
	[Response]
		* SUCCESS:200 
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
##

### 7. Set House cover image (By house owner and agency)
	[Security]	private
	[Request]
  		* PUT /v1/house/covimg/:id
	  		* img			int			// cover image id
	[Response]
		* SUCCESS:200 
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
##

### 8. recommend/unrecommend house (By agency)
	[Security]	private
	[Request]
  		* PUT /v1/house/recommend/:id
	  		* act    		int		// 1: recommend; 2: unrecommend
	[Response]
		* SUCCESS:200 
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
##

### 9. My represent houses (By agency)
	[Security]	private
	[Request]
  		* GET /v1/house/behalf?type=<>?bgn=<>&cnt=<>
	  		* type			int 	// house list type. 0: all; 1: to rent; 2: rented; 3: to sale; 4: to approve
	  		* bgn			int		// from which item to fetch
	  		* cnt			int		// how many item need to fetch. set to Zero to fetch total number
	[Response]
		* SUCCESS:200 
			* Total    		int		// total number
			* Count			int		// how many items fetched. -1 means just fetch the total number.
			* HouseDigests	array	// house public brief info list
				* house public brief info
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
##

### 10. Set House Price
	[Security]	private: landlorad, house agency, administrator
	[Request]
  		* POST /v1/house/:id/price
	  		* r_tp			int 	// rental, tag price
	  		* r_bp			int		// rental, bottom price
	  		* pf			bool	// if the rental involve the property fee
	  		* p_tp			int		// selling price, tag price
	  		* p_bp			int 	// selling price, bottom price
	[Response]
		* SUCCESS:200 
			* Id    		int		// new price record id
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
##

### 11. Get House Price history
	[Security]	private: landlorad, agency, administrator
	[Request]
  		* GET /v1/house/:id/price
	  		* bgn			int		// begin position
	  		* cnt			int		// how many records want to fetch
	[Response]
		* SUCCESS:200 
			* Total    			int		// total number
			* Count				int		// how many items fetched
			* Prices			array
				* Id				int		// 
				* RentalTag			int		// rental, tag price
				* RentalMin			int		// rental, bottom price
				* PropFee			bool	// if the rental involve the property fee
				* SalePriceTag		int		// selling price, tag price
				* SalePriceMin		int		// selling price, bottom price
				* Who				String	// who set the price
				* When				String	// when set the price
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
##

### 12. Set House showing time
	[Security]	private: landlorad, administrator
	[Request]
  		* PUT /v1/house/:id/showtime
	  		* prdw			int			// house showing period for working day. 1 - morning, 2 - afternoon, 3 - night
	  		* prdv			int			// house showing period for vacation and weekend. 1 - morning, 2 - afternoon, 3 - night
	  		* desc			string		// period desc when period is out of pre-defined
	[Response]
		* SUCCESS:200 
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
##

### 13. Get House showing time
	[Security]	private: landlorad, House Agency and administrator
	[Request]
  		* GET /v1/house/:id/showtime
	[Response]
		* SUCCESS:200 
			* Id 			int			// House id
			* Period1		int 		// house showing period for working day. 1 - morning, 2 - afternoon, 3 - night
			* Period2		int 		// house showing period for weekend and vacation. 1 - morning, 2 - afternoon, 3 - night
			* Desc			sring 		// period desc when period is out of pre-defined
			* Who			string		// who set the house showing time
			* When			string		// when set the house showing time
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description

##
### 14. GetHouseCertList
	[Security]	private: landlorad, House Agency and administrator
	[Request]
  		* GET /v1/house/:id/certhist
	[Response]
		* SUCCESS:200 
			* Total    		int			// total number
			* Count			int			// how many items fetched. -1 means just fetch the total number
			* CertHist					// house certification hist
				* Id			int			// certificate id
				* Uid			int			// user id
				* User			string		// user name
				* Phone			string		// user phone
				* Time			string		// certificate time
				* CertStat		int			// certificate status
				* Comm			string		// certificate comments
			* Ops			int 		// valid operations
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
##

### 15. reommit the house certification request (By landlord)
	[Security]	private, landlord
	[Request]
  		* POST /v1/house/:id/recert
	  		* cc			string		// comments of recommit certification
	[Response]
		* SUCCESS:200 
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
		  		* Id 		int 		// new house certification id
	  		* ErrDesc		string		// error description
