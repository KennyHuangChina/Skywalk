package controllers

import (
	// "encoding/json"
	"ApiServer/commdef"
	"ApiServer/models"
	// "encoding/base64"
	// "fmt"
	"github.com/astaxie/beego"
	// "strconv"
	// "strings"
)

type HouseWatchController struct {
	beego.Controller
}

func (h *HouseWatchController) URLMapping() {
	h.Mapping("GetUserWatchList", h.GetUserWatchList)
}

// @Title GetUserWatchList
// @Description get house watch list by login user
// @Success 200 {string}
// @Failure 403 body is empty
// @router /user [get]
func (this *HouseWatchController) GetUserWatchList() {
	FN := "[GetUserWatchList] "
	beego.Warn("[--- API: GetUserWatchList ---]")

	var result ResGetHouseDigestList
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

	begin, _ := this.GetInt64("bgn")
	count, _ := this.GetInt64("cnt")

	// beego.Debug(FN, "type:", tp, ", begin:", begin, ", count:", count, ", uid:", uid)

	/*
	 *	Processing
	 */
	err, total, fetched, ids := models.GetUserWatchList(begin, count, uid)
	if nil == err {
		result.Total = total
		if count > 0 {
			// result.Count = fetched
			// result.IDs = ids
			if fetched > 0 {
				for _, v := range ids {
					// beego.Debug(FN, "v:", fmt.Sprintf("%+v", v))
					hdi := commdef.HouseDigest{}
					err, hdi = models.GetHouseDigestInfo(v, uid)
					if nil != err {
						return
					}
					// beego.Debug(FN, "add")
					result.HouseDigests = append(result.HouseDigests, hdi)
				}
			}
			result.Count = int64(len(result.HouseDigests))
		} else {
			result.Count = -1
		}
	}
}
