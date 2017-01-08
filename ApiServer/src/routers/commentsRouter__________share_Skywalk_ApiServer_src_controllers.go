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

}
