# Picture APIs

##
### 1. Get picture url
	[Security]	based on who is asking, some pictures are pulbic, but some pictures are private
	[Request]
  		* GET /v1/pic/:id?sid=xxx&size=
	  		* sid 			string	// session id, by which server could know who send the request
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
	  		* type			int			// picture type
	  		* desc			string		// picture description
	  		* pic			file 		// binary file to store the picture to upload
	[Response]
		* SUCCESS:200 
			* Id			string 		// new picture id
		* ERR: 4XX,5XX
	  		* ErrCode		int			// error code
	  		* ErrDesc		string		// error description
