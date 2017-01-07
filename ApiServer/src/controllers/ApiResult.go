package controllers

import (
	"ApiServer/commdef"
	"ApiServer/models"
)

// result of API get security picture
type ResAdminGetSecurePic struct {
	commdef.ResCommon
	Capt models.Captcha
}
