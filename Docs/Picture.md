# Picture APIs

##
### 1. Get picture url
	[Security]	based on the picture type, some kind of pictures are pulbic, but some pictures are private
	[Request]
  		* GET /v1/pic/:id
	  		* size			int		// picture size, 1 - small; 2 - moderate; 3 - large; 0 or not set means fetch all 3 kind of size
	[Response]
		* SUCCESS:200 
			* Url_s			string 		// small picture path url
			* Url_m			string		// moderate picture path url
			* Url_l			string		// large picture path url
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description

##
### 2. Add picture
	[Security]	Private. house owner, agency and administrator
	[Request]
  		* POST /v1/pic/newpic
	  		* house			int			// house id. could be Zero, which means picture is not related with a certain house
	  		* rid			int 		// ref id
	  		* type			int			// picture type
	  		* desc			string		// picture description
	  		* file			file 		// binary file to store the picture to upload
	[Response]
		* SUCCESS:200 
			* Id			string 		// new picture id
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description

##
### 3. Delete picture
	[Security]	Private. house owner, agency and administrator
	[Request]
  		* DELETE /v1/pic/:id
	[Response]
		* SUCCESS:200 
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description

##
### 4. Get House Picture list
	[Security]	based on the picture type, some kind of pictures are pulbic, but some pictures are private
	[Request]
  		* GET /v1/pic/house/:id?st=
	  		* st			int 		// type: 0 means all subtype, > 0 means a certain subtype
	  		* sz			int			// pic size: 2 small, 3 moderate, 4 large
	[Response]
		* SUCCESS:200 
			* total			int 		// picture count
			* PicList
				* Id 		int			// picture id
				* Desc		string		// picture description
				* SubType	int			// picture sub-type
				* Checksum	string		// picture checksum, md5
				* Urls      
					* Url_s			string 		// small picture path url
					* Url_m			string		// moderate picture path url
					* Url_l			string		// large picture path url
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
