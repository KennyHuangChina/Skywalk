package controllers

import (
	"ApiServer/commdef"
	"fmt"
	"github.com/astaxie/beego"
	"io"
	"mime/multipart"
	"os"
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

	// picture
	file, fHead, errT := this.GetFile("pic")
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("no picture attached, err:%s", errT.Error())}
		return
	}
	for k, v := range fHead.Header {
		beego.Debug(FN, k, ":", v)
	}
	// file type
	fType := fHead.Header["Content-Type"][0]
	if "image/jpeg" != fType && "image/png" != fType {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("invalid Content-Type::%s", fType)}
		return
	}
	if err = saveLocal(file, fHead.Filename); nil != err {
		return
	}

	/*
	 *	Processing
	 */
	err, id := models.AddPicture(hid, uid, pt, desc)
	if nil == err {
		result.Id = id
	}
}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//		Internal Functions
//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
func saveLocal(file multipart.File, filename string) (err error) {
	FN := "[saveLocal] "

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
