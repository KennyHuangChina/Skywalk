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
		beego.NSNamespace("/property",
			beego.NSInclude(
				&controllers.PropertyController{},
			),
		),
		beego.NSNamespace("/accessory",
			beego.NSInclude(
				&controllers.AccessoryController{},
			),
		),
		beego.NSNamespace("/pic",
			beego.NSInclude(
				&controllers.PictureController{},
			),
		),
		beego.NSNamespace("/event",
			beego.NSInclude(
				&controllers.EventController{},
			),
		),
		beego.NSNamespace("/appointment",
			beego.NSInclude(
				&controllers.AppointmentController{},
			),
		),
	)
	beego.AddNamespace(ns)
}
