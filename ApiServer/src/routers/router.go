package routers

import (
	"ApiServer/controllers"
	"github.com/astaxie/beego"
)

func init() {
	beego.Router("/", &controllers.MainController{})

	ns := beego.NewNamespace("/v1",
		beego.NSNamespace("/admin",
			beego.NSInclude(
				&controllers.AdminController{},
			),
		),
		beego.NSNamespace("/house",
			beego.NSInclude(
				&controllers.HouseController{},
			),
		),
		beego.NSNamespace("/pic",
			beego.NSInclude(
				&controllers.PictureController{},
			),
		),
	)
	beego.AddNamespace(ns)
}
