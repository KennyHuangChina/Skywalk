package routers

import (
	"github.com/astaxie/beego"
)

func init() {

	beego.GlobalControllerRouter["ApiServer/controllers:AccessoryController"] = append(beego.GlobalControllerRouter["ApiServer/controllers:AccessoryController"],
		beego.ControllerComments{
			Method: "GetHouseDeliverableList",
			Router: `/house/:id/deliverables`,
			AllowHTTPMethods: []string{"get"},
			Params: nil})

	beego.GlobalControllerRouter["ApiServer/controllers:AccessoryController"] = append(beego.GlobalControllerRouter["ApiServer/controllers:AccessoryController"],
		beego.ControllerComments{
			Method: "AddFacilityType",
			Router: `/facility/type`,
			AllowHTTPMethods: []string{"post"},
			Params: nil})

	beego.GlobalControllerRouter["ApiServer/controllers:AccessoryController"] = append(beego.GlobalControllerRouter["ApiServer/controllers:AccessoryController"],
		beego.ControllerComments{
			Method: "GetFacilityTypeList",
			Router: `/facitypelst`,
			AllowHTTPMethods: []string{"get"},
			Params: nil})

	beego.GlobalControllerRouter["ApiServer/controllers:AccessoryController"] = append(beego.GlobalControllerRouter["ApiServer/controllers:AccessoryController"],
		beego.ControllerComments{
			Method: "AddFacility",
			Router: `/facility`,
			AllowHTTPMethods: []string{"post"},
			Params: nil})

	beego.GlobalControllerRouter["ApiServer/controllers:AccessoryController"] = append(beego.GlobalControllerRouter["ApiServer/controllers:AccessoryController"],
		beego.ControllerComments{
			Method: "GetFacilityList",
			Router: `/facilitys`,
			AllowHTTPMethods: []string{"get"},
			Params: nil})

	beego.GlobalControllerRouter["ApiServer/controllers:AccessoryController"] = append(beego.GlobalControllerRouter["ApiServer/controllers:AccessoryController"],
		beego.ControllerComments{
			Method: "AddHouseFacilities",
			Router: `/house/:id/facilities`,
			AllowHTTPMethods: []string{"post"},
			Params: nil})

	beego.GlobalControllerRouter["ApiServer/controllers:AccessoryController"] = append(beego.GlobalControllerRouter["ApiServer/controllers:AccessoryController"],
		beego.ControllerComments{
			Method: "GetHouseFacilities",
			Router: `/house/:id/facilities`,
			AllowHTTPMethods: []string{"get"},
			Params: nil})

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
			Method: "Test",
			Router: `/test`,
			AllowHTTPMethods: []string{"get"},
			Params: nil})

	beego.GlobalControllerRouter["ApiServer/controllers:AdminController"] = append(beego.GlobalControllerRouter["ApiServer/controllers:AdminController"],
		beego.ControllerComments{
			Method: "Relogin",
			Router: `/relogin/:id`,
			AllowHTTPMethods: []string{"post"},
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

	beego.GlobalControllerRouter["ApiServer/controllers:EventController"] = append(beego.GlobalControllerRouter["ApiServer/controllers:EventController"],
		beego.ControllerComments{
			Method: "GetNewEventCount",
			Router: `/count`,
			AllowHTTPMethods: []string{"get"},
			Params: nil})

	beego.GlobalControllerRouter["ApiServer/controllers:EventController"] = append(beego.GlobalControllerRouter["ApiServer/controllers:EventController"],
		beego.ControllerComments{
			Method: "GetHouseNewEvents",
			Router: `/houses`,
			AllowHTTPMethods: []string{"get"},
			Params: nil})

	beego.GlobalControllerRouter["ApiServer/controllers:HouseController"] = append(beego.GlobalControllerRouter["ApiServer/controllers:HouseController"],
		beego.ControllerComments{
			Method: "GetPropertyInfo",
			Router: `/property/:id`,
			AllowHTTPMethods: []string{"get"},
			Params: nil})

	beego.GlobalControllerRouter["ApiServer/controllers:HouseController"] = append(beego.GlobalControllerRouter["ApiServer/controllers:HouseController"],
		beego.ControllerComments{
			Method: "UpdateProperty",
			Router: `/property/:id`,
			AllowHTTPMethods: []string{"put"},
			Params: nil})

	beego.GlobalControllerRouter["ApiServer/controllers:HouseController"] = append(beego.GlobalControllerRouter["ApiServer/controllers:HouseController"],
		beego.ControllerComments{
			Method: "AddProperty",
			Router: `/property`,
			AllowHTTPMethods: []string{"post"},
			Params: nil})

	beego.GlobalControllerRouter["ApiServer/controllers:HouseController"] = append(beego.GlobalControllerRouter["ApiServer/controllers:HouseController"],
		beego.ControllerComments{
			Method: "GetDeliverableList",
			Router: `/delivelst`,
			AllowHTTPMethods: []string{"get"},
			Params: nil})

	beego.GlobalControllerRouter["ApiServer/controllers:HouseController"] = append(beego.GlobalControllerRouter["ApiServer/controllers:HouseController"],
		beego.ControllerComments{
			Method: "AddDeliverable",
			Router: `/deliverable`,
			AllowHTTPMethods: []string{"post"},
			Params: nil})

	beego.GlobalControllerRouter["ApiServer/controllers:HouseController"] = append(beego.GlobalControllerRouter["ApiServer/controllers:HouseController"],
		beego.ControllerComments{
			Method: "NewHouseDeliverable",
			Router: `/housedeliv/:id`,
			AllowHTTPMethods: []string{"post"},
			Params: nil})

	beego.GlobalControllerRouter["ApiServer/controllers:HouseController"] = append(beego.GlobalControllerRouter["ApiServer/controllers:HouseController"],
		beego.ControllerComments{
			Method: "GetPropertyList",
			Router: `/property/list`,
			AllowHTTPMethods: []string{"get"},
			Params: nil})

	beego.GlobalControllerRouter["ApiServer/controllers:HouseController"] = append(beego.GlobalControllerRouter["ApiServer/controllers:HouseController"],
		beego.ControllerComments{
			Method: "CertHouse",
			Router: `/cert/:id`,
			AllowHTTPMethods: []string{"post"},
			Params: nil})

	beego.GlobalControllerRouter["ApiServer/controllers:HouseController"] = append(beego.GlobalControllerRouter["ApiServer/controllers:HouseController"],
		beego.ControllerComments{
			Method: "SetHouseCoverImage",
			Router: `/covimg/:id`,
			AllowHTTPMethods: []string{"put"},
			Params: nil})

	beego.GlobalControllerRouter["ApiServer/controllers:HouseController"] = append(beego.GlobalControllerRouter["ApiServer/controllers:HouseController"],
		beego.ControllerComments{
			Method: "RecommendHouse",
			Router: `/recommend/:id`,
			AllowHTTPMethods: []string{"put"},
			Params: nil})

	beego.GlobalControllerRouter["ApiServer/controllers:HouseController"] = append(beego.GlobalControllerRouter["ApiServer/controllers:HouseController"],
		beego.ControllerComments{
			Method: "SetHouseAgency",
			Router: `/agency/:id`,
			AllowHTTPMethods: []string{"put"},
			Params: nil})

	beego.GlobalControllerRouter["ApiServer/controllers:HouseController"] = append(beego.GlobalControllerRouter["ApiServer/controllers:HouseController"],
		beego.ControllerComments{
			Method: "ModifyHouse",
			Router: `/:id`,
			AllowHTTPMethods: []string{"put"},
			Params: nil})

	beego.GlobalControllerRouter["ApiServer/controllers:HouseController"] = append(beego.GlobalControllerRouter["ApiServer/controllers:HouseController"],
		beego.ControllerComments{
			Method: "CommitHouseByOwner",
			Router: `/commit`,
			AllowHTTPMethods: []string{"post"},
			Params: nil})

	beego.GlobalControllerRouter["ApiServer/controllers:HouseController"] = append(beego.GlobalControllerRouter["ApiServer/controllers:HouseController"],
		beego.ControllerComments{
			Method: "GetBehalfList",
			Router: `/behalf`,
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

	beego.GlobalControllerRouter["ApiServer/controllers:PictureController"] = append(beego.GlobalControllerRouter["ApiServer/controllers:PictureController"],
		beego.ControllerComments{
			Method: "AddPic",
			Router: `/newpic`,
			AllowHTTPMethods: []string{"post"},
			Params: nil})

}
