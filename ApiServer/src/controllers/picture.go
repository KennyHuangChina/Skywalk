package controllers

import (
	"ApiServer/commdef"
	"encoding/base64"
	"fmt"
	"github.com/astaxie/beego"
	"io"
	"math/rand"
	"mime/multipart"
	"os"
	"time"
	// "github.com/astaxie/beego/orm"
	"ApiServer/models"
)

type Size interface {
	Size() int64
}

type PictureController struct {
	beego.Controller
}

func (p *PictureController) URLMapping() {
	p.Mapping("GetPicUrl", p.GetPicUrl)
	p.Mapping("AddPic", p.AddPic)
	p.Mapping("DelPic", p.DelPic)
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
	// uid = 4

	hid, _ := this.GetInt64("house")
	pt, _ := this.GetInt("type")
	desc := this.GetString("desc")
	tmp, _ := base64.URLEncoding.DecodeString(desc)
	desc = string(tmp)
	beego.Debug(FN, "house:", hid, ", type:", pt, ", desc:", desc)

	// picture
	file, fHead, errT := this.GetFile("file") // pic")
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("no picture attached, err:%s", errT.Error())}
		return
	}
	for k, v := range fHead.Header {
		beego.Debug(FN, k, ":", v)
	}
	// file type
	fType := fHead.Header["Content-Type"][0]
	if !checkPicType(fType) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("invalid Content-Type::%s", fType)}
		return
	}

	picBaseDir := beego.AppConfig.String("PicBaseDir") // os.Getwd()
	// beego.Warn("picBaseDir:", picBaseDir)
	err, picFileName := generatePicFileName(picBaseDir, getPicExtName(fType))
	if nil != err {
		return
	}
	// Notice: we need to save the picture right now, if we save the picture after models.AddPicture(), the picture file name
	//	maybe used by other API calling, so we should occupy this file name first.
	// Warning: This solution could not prevent picture file name conflict completely, there is chance that file conflict
	//	between generatePicFileName() and savePicture(), although it's very low possibility
	if err = savePicture(file, picBaseDir+picFileName /*fHead.Filename*/); nil != err {
		return
	}

	/*
	 *	Processing
	 */
	err, id := models.AddPicture(hid, uid, pt, desc, picFileName, picBaseDir)
	if nil == err {
		result.Id = id
	} else {
		models.DelImageFile(picBaseDir + picFileName)
	}
}

// @Title DelPic
// @Description delete a picture
// @Success 200 {string}
// @Failure 403 body is empty
// @router /:id [delete]
func (this *PictureController) DelPic() {
	FN := "[DelPic] "
	beego.Warn("[--- API: DelPic ---]")

	var result ResCommon
	var err error

	defer func() {
		err = api_result(err, this.Controller, &result)
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

	pic, _ := this.GetInt64(":id")

	err = models.DelImage(pic, uid)

	return
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//		Internal Functions
//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
func generatePicFileName(baseDir, extName string) (err error, fileName string) {
	FN := "[generatePicFileName] "

	defer func() {
		if nil != err {
			beego.Error(FN, err)
			fileName = ""
		}
	}()

	if 0 == len(baseDir) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("baseDir:%s", baseDir)}
		return
	}
	if 0 == len(extName) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("extName:%s", extName)}
		return
	}

	// file path = base_dir + date_dir + file_name
	timeNow := time.Now()
	dateDir := fmt.Sprintf("%.4d%.2d/", timeNow.Year(), timeNow.Month())
	picDir := baseDir + dateDir
	beego.Debug(FN, "picDir:", picDir)
	/*fileinfo*/ _, errT := os.Stat(picDir)
	if nil == errT { // date dir already exist

	} else if os.IsNotExist(errT) {
		beego.Warn(FN, "dir:", dateDir, " does not exist, create it")
		errT = os.MkdirAll(picDir, 0700)
		if nil != err {
			err = commdef.SwError{ErrCode: commdef.ERR_SYS_IO_CREATE_DIR, ErrInfo: errT.Error()}
			return
		}
	} else {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}

	MAX_TRY_TIMES := 10
	nTryTimes := 0
	for nTryTimes = 0; nTryTimes < MAX_TRY_TIMES; nTryTimes++ {

		nameBase := fmt.Sprintf("%.2d%.2d%.2d%.2d", timeNow.Day(), timeNow.Hour(), timeNow.Minute(), timeNow.Second())
		// nameBase := fmt.Sprintf("%.2d%.2d%.2d%.2d", timeNow.Day(), 0, 0, 0)
		// beego.Debug(FN, "nameBase:", nameBase)

		r := rand.New(rand.NewSource(time.Now().UnixNano()))
		rand := r.Intn(999999)
		nameRand := fmt.Sprintf("%.6d", rand)
		// nameRand := fmt.Sprintf("%.6d", nTryTimes)
		// beego.Debug(FN, "nameRand:", nameRand)

		// fileName = picDir + nameBase + nameRand + extName
		// beego.Debug(FN, "fileName:", fileName)

		// check if the file name is conflict
		_, errT = os.Stat(picDir + nameBase + nameRand + extName)
		if nil == errT || os.IsExist(errT) {
			// file already exist, try again
			time.Sleep(10 * time.Millisecond)
		} else {
			fileName = dateDir + nameBase + nameRand + extName
			// beego.Debug(FN, "Done")
			return
		}
	}

	err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("Try %d times, failed", nTryTimes)}
	return
}

func savePicture(file multipart.File, filename string) (err error) {
	FN := "[savePicture] "

	defer func() {
		if nil != err {
			beego.Error(FN, err.Error())
		}
	}()

	// file size
	fileSize := int64(0)
	if sizeIntf, ok := file.(Size); ok {
		fileSize = sizeIntf.Size()
	}
	if fileSize <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("picture file, size:", fileSize)}
		return
	}
	if fileSize > 3*1024*1024 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("picture size too large (< 3MB):%d", fileSize)}
		return
	}
	// beego.Debug(FN, "file name:", fHead.Header["Content-Disposition"][0])
	beego.Debug(FN, "file name:", filename, ", size:", fileSize)

	buffSize := int64(20 * 1024) // default buffer size 20KB
	if fileSize < 20*1024 {
		buffSize = fileSize
	}
	buff := make([]byte, buffSize)

	// save to local disk
	localFile, errT := os.Create(filename)
	defer localFile.Close()
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_SYS_IO_CREATE_FILE, ErrInfo: errT.Error()}
		return
	}

	writeBytes := int64(0)
	file.Seek(0, 0)
	for {
		// read data from file
		numb, errT := file.Read(buff)
		// beego.Debug(FN, "numb:", numb, ", errT:", errT, ", buff:", buff)
		if io.EOF == errT /*|| 0 == numb*/ { // EOF
			beego.Debug(FN, "errT:", errT, ", numb:", numb)
			break
		} else if nil != errT {
			err = commdef.SwError{ErrCode: commdef.ERR_SYS_IO_READ, ErrInfo: errT.Error()}
			return
		}

		buff2write := buff[:numb]
		n, errT := localFile.Write(buff2write)
		if nil != errT {
			err = commdef.SwError{ErrCode: commdef.ERR_SYS_IO_WRITE, ErrInfo: errT.Error()}
			return
		}
		writeBytes = writeBytes + int64(n)
	}
	beego.Debug(FN, "writeBytes:", writeBytes)

	return
}

func checkPicType(ft string) bool {
	// beego.Debug("ft:", ft)
	for _, v := range PIC_TYPES {
		// beego.Debug("v.picType:", v.picType)
		if ft == v.picType {
			return true
		}
	}
	return false
}

func getPicExtName(ft string) string {
	for _, v := range PIC_TYPES {
		if ft == v.picType {
			return v.extName
		}
	}
	return ""
}

type PicType struct {
	picType string // picture type
	extName string // picture file external name
}

var PIC_TYPES []PicType

func init() {
	PIC_TYPES = append(PIC_TYPES, PicType{"image/jpeg", ".jpg"})
	PIC_TYPES = append(PIC_TYPES, PicType{"image/png", ".png"})
}
