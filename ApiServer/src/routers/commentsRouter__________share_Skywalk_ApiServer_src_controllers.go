package routers

import (
	"github.com/astaxie/beego"
)

func init() {

	beego.GlobalControllerRouter["ApiServer/controllers:AdminController"] = append(beego.GlobalControllerRouter["ApiServer/controllers:AdminController"],
		beego.ControllerComments{
			Method: "GetUserInfo",
			Router: `/user/:id`,
			AllowHTTPMethods: []string{"get"},
			Params: nil})

	beego.GlobalControllerRouter["ApiServer/controllers:AdminController"] = append(beego.GlobalControllerRouter["ApiServer/controllers:AdminController"],
		beego.ControllerComments{
			Method: "GetSecurePic",
			Router: `/sec_pic`,
			AllowHTTPMethods: []string{"get"},
			Params: nil})

	beego.GlobalControllerRouter["ApiServer/controllers:AdminController"] = append(beego.GlobalControllerRouter["ApiServer/controllers:AdminController"],
		beego.ControllerComments{
			Method: "GetSaltForUser",
			Router: `/salt`,
			AllowHTTPMethods: []string{"get"},
			Params: nil})

	beego.GlobalControllerRouter["ApiServer/controllers:AdminController"] = append(beego.GlobalControllerRouter["ApiServer/controllers:AdminController"],
		beego.ControllerComments{
			Method: "Loginpass",
			Router: `/loginpass`,
			AllowHTTPMethods: []string{"post"},
			Params: nil})

	beego.GlobalControllerRouter["ApiServer/controllers:AdminController"] = append(beego.GlobalControllerRouter["ApiServer/controllers:AdminController"],
		beego.ControllerComments{
			Method: "Logout",
			Router: `/logout`,
			AllowHTTPMethods: []string{"post"},
			Params: nil})

	beego.GlobalControllerRouter["ApiServer/controllers:AdminController"] = append(beego.GlobalControllerRouter["ApiServer/controllers:AdminController"],
		beego.ControllerComments{
			Method: "FetchSms",
			Router: `/fetchsms`,
			AllowHTTPMethods: []string{"get"},
			Params: nil})

	beego.GlobalControllerRouter["ApiServer/controllers:AdminController"] = append(beego.GlobalControllerRouter["ApiServer/controllers:AdminController"],
		beego.ControllerComments{
			Method: "Loginsms",
			Router: `/loginsms`,
			AllowHTTPMethods: []string{"post"},
			Params: nil})

	beego.GlobalControllerRouter["ApiServer/controllers:HouseController"] = append(beego.GlobalControllerRouter["ApiServer/controllers:HouseController"],
		beego.ControllerComments{
			Method: "GetPropertyInfo",
			Router: `/property/:id`,
			AllowHTTPMethods: []string{"get"},
			Params: nil})

	beego.GlobalControllerRouter["ApiServer/controllers:HouseController"] = append(beego.GlobalControllerRouter["ApiServer/controllers:HouseController"],
		beego.ControllerComments{
			Method: "GetHouseList",
			Router: `/list`,
			AllowHTTPMethods: []string{"get"},
			Params: nil})

	beego.GlobalControllerRouter["ApiServer/controllers:HouseController"] = append(beego.GlobalControllerRouter["ApiServer/controllers:HouseController"],
		beego.ControllerComments{
			Method: "GetHouseDigestInfo",
			Router: `/digest/:id`,
			AllowHTTPMethods: []string{"get"},
			Params: nil})

	beego.GlobalControllerRouter["ApiServer/controllers:HouseController"] = append(beego.GlobalControllerRouter["ApiServer/controllers:HouseController"],
		beego.ControllerComments{
			Method: "GetHouseInfo",
			Router: `/:id`,
			AllowHTTPMethods: []string{"get"},
			Params: nil})

	beego.GlobalControllerRouter["ApiServer/controllers:PictureController"] = append(beego.GlobalControllerRouter["ApiServer/controllers:PictureController"],
		beego.ControllerComments{
			Method: "GetPicUrl",
			Router: `/:id`,
			AllowHTTPMethods: []string{"get"},
			Params: nil})

}
