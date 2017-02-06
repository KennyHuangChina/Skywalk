package controllers

import (
	// "ApiServer/commdef"
	"github.com/astaxie/beego"
	// "github.com/astaxie/beego/orm"
	"ApiServer/models"
)

type PictureController struct {
	beego.Controller
}

func (p *PictureController) URLMapping() {
	p.Mapping("GetPicUrl", p.GetPicUrl)
	p.Mapping("AddPic", p.AddPic)
}

// @Title GetPicUrl
// @Description get picture url
// @Success 200 {string}
// @Failure 403 body is empty
// @router /:id [get]
func (this *PictureController) GetPicUrl() {
	FN := "[GetPicUrl] "
	beego.Warn("[--- API: GetPicUrl ---]")

	var result ResGetPicUrl
	var err error

	defer func() {
		err = api_result(err, this.Controller, &result.ResCommon)
		if nil != err {
			beego.Error(FN, err.Error())
		}

		// export result
		this.Data["json"] = result
		this.ServeJSON()
	}()

	/*
	 *	Extract agreements
	 */
	pid, _ := this.GetInt64(":id")
	sid := this.GetString("sid")
	size, _ := this.GetInt("size")
	uid := int64(0)

	beego.Debug(FN, "pid:", pid, ", sid:", sid)

	/*
	 *	Processing
	 */
	err, url_s, url_m, url_l := models.GetPicUrl(pid, uid, size)
	if nil == err {
		result.Url_s = url_s
		result.Url_m = url_m
		result.Url_l = url_l
	}
}

// @Title AddPic
// @Description add new picture
// @Success 200 {string}
// @Failure 403 body is empty
// @router /newpic [post]
func (this *PictureController) AddPic() {
	FN := "[AddPic] "
	beego.Warn("[--- API: AddPic ---]")

	var result ResAddResource
	var err error

	defer func() {
		err = api_result(err, this.Controller, &result.ResCommon)
		if nil != err {
			beego.Error(FN, err.Error())
		}

		// export result
		this.Data["json"] = result
		this.ServeJSON()
	}()

	/*
	 *	Extract agreements
	 */
	uid, err := getLoginUser(this.Controller)
	if nil != err {
		return
	}

	hid, _ := this.GetInt64("house")
	pt, _ := this.GetInt("type")
	desc := this.GetString("desc")

	/*
	 *	Processing
	 */
	err, id := models.AddPicture(hid, uid, pt, desc)
	if nil == err {
		result.Id = id
	}
}
