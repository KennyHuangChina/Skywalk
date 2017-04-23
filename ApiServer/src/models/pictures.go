package models

import (
	"ApiServer/commdef"
	"fmt"
	"github.com/astaxie/beego"
	"github.com/astaxie/beego/orm"
	// z "github.com/nutzam/zgo"
	// "graphics"
	// "github.com/BurntSushi/graphics-go/graphics" // auto exported from code.google.com/p/graphics-go/graphics
	"github.com/KennyHuangChina/graphics-go/graphics"
	"image/draw"
	// "code.google.com/p/graphics-go/graphics"
	"image"
	// "image/png"
	"image/jpeg"
	"os"
	"strings"
)

/**
*	Get picture url
*	Arguments:
*		hid 	- house id
*		uid 	- login user id
*		pt		- picture type
*		pfn		- picture file name, relative path against picture base dir
*		pbd		- picture base dir
*		desc	- picture description
*	Returns
*		err - error info
*		nid - new picture id
**/
func AddPicture(hid, uid int64, pt int, desc, pfn, pbd string) (err error, nid int64) {
	FN := "[AddPicture] "
	beego.Trace(FN, "hid:", hid, ", uid:", uid, ", pt:", pt, ", desc:", desc)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/* Arguments checking */
	if 0 != hid {
		errT, h := getHouse(hid)
		if nil != errT {
			err = errT
			return
		}
		if h.Owner.Id != uid || h.Agency.Id != uid {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION}
			return
		}
	}
	if 0 == len(pbd) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("picture base dir:%s", pbd)}
		return
	}
	if 0 == len(pfn) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("picture file name:%s", pfn)}
		return
	}
	if pos := strings.LastIndex(pfn, "/"); pos < 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("picture file name:%s", pfn)}
		return
	}

	if 0 == len(desc) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("picture desc:%s", desc)}
		return
	}
	// picture type
	err, majorType, minorType := checkPictureType(pt)
	if nil != err {
		return
	}
	if commdef.PIC_TYPE_HOUSE == majorType && 0 == hid {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("hid:%d", hid)}
		return
	}
	// check if the picture exist
	_, errT := os.Stat(pbd + pfn)
	if nil == errT || os.IsExist(errT) { // picture exist
	} else {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("picture(%s) does not exist", pbd+pfn)}
		return
	}

	/* Processing */
	switch majorType {
	case commdef.PIC_TYPE_USER:
	case commdef.PIC_TYPE_HOUSE:
		return addPicHouse(hid, minorType, desc, pfn, pbd)
	case commdef.PIC_TYPE_RENTAL:
	default:
	}

	err = commdef.SwError{ErrCode: commdef.ERR_NOT_IMPLEMENT}
	return
}

/**
*	Get picture url
*	Arguments:
*		pid 	- picture id
*		uid 	- user id
*		size	- picture size: PIC_SIZE_ALL, PIC_SIZE_SMALL, PIC_SIZE_MODERATE, PIC_SIZE_LARGE,
*	Returns
*		err - error info
*		url_s - small picture url
*		url_m - moderate picture url
*		url_l - large picture url
**/
func GetPicUrl(pid, uid int64, size int) (err error, url_s, url_m, url_l string) {
	FN := "[GetPicUrl] "
	beego.Trace(FN, "pid:", pid, ", uid:", uid, ", size:", size)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	if pid <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("picture id:%d", pid)}
		return
	}
	if uid <= 0 {
		// err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("user id:%d", uid)}
		// return
	}
	if size < commdef.PIC_SIZE_ALL || size > commdef.PIC_SIZE_LARGE {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("size:%d", size)}
		return
	}

	o := orm.NewOrm()

	sql := fmt.Sprintf("SELECT size, url FROM tbl_pic_set WHERE pic_id='%d'", pid)
	if size != commdef.PIC_SIZE_ALL {
		sql = sql + fmt.Sprintf(" AND size=%d", size)
	}
	beego.Debug(FN, "sql:", sql)

	var pss []TblPicSet
	numb, errTmp := o.Raw(sql).QueryRows(&pss)
	if nil != errTmp {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errTmp.Error()}
		return
	}
	if numb > 3 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("Inalid picture number:%d", numb)}
		return
	}
	beego.Debug(FN, "numb:", numb)

	for /*k*/ _, v := range pss {
		beego.Debug(FN, "v:", v)
		switch v.Size {
		case commdef.PIC_SIZE_SMALL:
			if commdef.PIC_SIZE_ALL == size || commdef.PIC_SIZE_SMALL == size {
				url_s = v.Url
			}
		case commdef.PIC_SIZE_MODERATE:
			if commdef.PIC_SIZE_ALL == size || commdef.PIC_SIZE_MODERATE == size {
				url_m = v.Url
			}
		case commdef.PIC_SIZE_LARGE:
			if commdef.PIC_SIZE_ALL == size || commdef.PIC_SIZE_LARGE == size {
				url_l = v.Url
			}
		}
	}

	beego.Warn(FN, "TODO: picture access right varification")

	return
}

// delete the image specified
func DelImage(image string) (err error) {
	FN := "[DelImage] "

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	if 0 == len(image) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("image path:%s", image)}
		return
	}

	_, errT := os.Stat(image)
	if nil == errT || os.IsExist(errT) { // picture exist
		err = os.Remove(image)
	}

	return
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//		Internal Functions
//
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
func checkPictureType(picType int) (err error, majorType, minorType int) {
	FN := "[checkPictureType] "

	if picType <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT}
		return
	}

	typeMajor := picType / 100 * 100
	typeMinor := picType % 100
	beego.Debug(FN, "Major:", typeMajor, ", minor:", typeMinor)

	switch typeMajor {
	case commdef.PIC_TYPE_USER:
		err = commdef.SwError{ErrCode: commdef.ERR_NOT_IMPLEMENT}
	case commdef.PIC_TYPE_HOUSE:
		switch typeMinor {
		case commdef.PIC_HOUSE_FLOOR:
			fallthrough
		case commdef.PIC_HOUSE_FURNITURE:
			fallthrough
		case commdef.PIC_HOUSE_APPLIANCE:
			majorType = typeMajor
			minorType = typeMinor
		default:
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT}
		}
	case commdef.PIC_TYPE_RENTAL:
		err = commdef.SwError{ErrCode: commdef.ERR_NOT_IMPLEMENT}
	default:
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT}
	}

	return
}

func addPicHouse(hid int64, minotType int, desc, pfn, pbd string) (err error, nid int64) {
	FN := "[addPicHouse] "

	defer func() {
		if nil != err {
			beego.Error(FN, err)
			nid = 0
		}
	}()

	pos := strings.LastIndex(pfn, ".")
	if pos < 1 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("picture file name:%s", pfn)}
		return
	}
	picName := pfn[:pos]
	// extName := pfn[pos:]
	// beego.Debug(FN, "picName:", picName, ", extName:", extName)

	psn := ""
	pln := ""

	o := orm.NewOrm()

	o.Begin()
	defer func() {
		if nil != err {
			o.Rollback()
			// delete the pictures created in this function
			if len(psn) > 0 {
				DelImage(pbd + psn)
			}
			if len(pln) > 0 {
				DelImage(pbd + pln)
			}
		} else {
			o.Commit()
		}
	}()

	newPic := TblPictures{TypeMajor: commdef.PIC_TYPE_HOUSE, TypeMiner: minotType, RefId: hid, Desc: desc}
	id, errT := o.Insert(&newPic)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}
	nid = id

	// watermark

	// generate picture set, original, large size and small size
	po := TblPicSet{PicId: nid, Size: commdef.PIC_SIZE_ORIGINAL, Url: pfn}
	_, errT = o.Insert(&po)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("Fail to create original picture, err:%s", errT.Error())}
		return
	}

	src, err, dx, dy := loadImage(pbd + pfn)
	if nil != err {
		return
	}

	// small size
	psn = picName + "_s.jpg" // + extName
	sw, _ := beego.AppConfig.Int("small_pic_w")
	sh, _ := beego.AppConfig.Int("small_pic_h")
	if dx > sw || dy > sh {
		if err = resizeImage(src, pbd+psn, sw, sh, 50, nil); nil != err {
			return
		}
	} else { // original image is small than defined "small size", use original image
		psn = pfn
	}
	ps := TblPicSet{PicId: nid, Size: commdef.PIC_SIZE_SMALL, Url: psn}
	if _, errT = o.Insert(&ps); nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("Fail to create small picture, err:%s", errT.Error())}
		return
	}

	// Large size
	pln = picName + "_l.jpg" // + extName
	lw, _ := beego.AppConfig.Int("lare_pic_w")
	lh, _ := beego.AppConfig.Int("lare_pic_h")
	if dx < lw && dy < lh { // original image is small than defined "large size", make a copy
		lw = dx
		lh = dy
	}
	if err = resizeImage(src, pbd+pln, lw, lh, 100, watermarking); nil != err {
		return
	}

	pl := TblPicSet{PicId: nid, Size: commdef.PIC_SIZE_LARGE, Url: pln}
	_, errT = o.Insert(&pl)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("Fail to create large picture, err:%s", errT.Error())}
		return
	}

	// Notice: enable the following code to test the image deleting in case of error
	// err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED}
	return
}

/*
*	Resizing the image
*		src		- original image
*		tip		- target image path.
*		tx		- target width
*		ty		- target height
*	Returns
*		err		- error
 */
func resizeImage(src image.Image, tip string, tx, ty, quality int, markFun func(bgImg *image.RGBA) (err error)) (err error) {
	FN := "[resizeImage] "
	beego.Info(FN, "tip:", tip, ", tx:", tx, ", ty:", ty)

	defer func() {
		if nil != err {
		}
	}()

	if tx <= 0 || ty <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("tx:%d, ty:%d", tx, ty)}
		return
	}
	if 0 == len(tip) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("tip:%s", tip)}
		return
	}
	if quality <= 0 || quality > 100 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("quality:%d", quality)}
		return
	}

	// typef := z.FileType(path)
	// beego.Debug(FN, "typef:", typef)

	bound := src.Bounds()
	dx := bound.Dx()
	dy := bound.Dy()
	beego.Debug(FN, "dx:", dx, ", dy:", dy)
	if dx <= 0 || dy <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("dx:%d, dy:%d", dx, dy)}
		return
	}

	// calculate the real target image width and height
	nx := tx
	ny := ty
	ny0 := dy * tx / dx
	if ny0 > ty {
		nx = dx * ty / dy
	} else {
		ny = ny0
	}
	beego.Debug(FN, "nx:", nx, ", ny:", ny)

	dst := image.NewRGBA(image.Rect(0, 0, nx, ny)) // newdx, newdx * dy / dx))
	if errT := graphics.Scale(dst, src); nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("Scan image, err:%s", errT.Error())}
		return
	}

	if nil != markFun { // wartermarking
		if err = markFun(dst); nil != err {
			return
		}
	}

	// save resized image
	err = saveImage(dst, tip, quality)

	return
}

func saveImage(img *image.RGBA, file string, quality int) (err error) {
	// FN := "[saveImage] "

	// save image in jpeg format
	imgfile, errT := os.Create(file)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("fail to create image, err:%s", errT.Error())}
		return
	}
	defer imgfile.Close()

	errT = jpeg.Encode(imgfile, img, &jpeg.Options{quality}) // encoding the image
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("fail to encode image, err:%s", errT.Error())}
		return
	}

	return
}

// Load Image decodes an image from a file of image.
func loadImage(path string) (img image.Image, err error, w, h int) {
	FN := "[loadImage] "
	// beego.Debug(FN, "path:", path)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	if 0 == len(path) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("path:%s", path)}
		return
	}

	file, errT := os.Open(path)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("Open image, err:%s", errT.Error())}
		return
	}
	defer file.Close()

	img, _, errT = image.Decode(file)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("Decode image, err:%s", errT.Error())}
		return
	}

	w = img.Bounds().Dx()
	h = img.Bounds().Dy()

	return
}

// watermarking image
//		bg - background image
func watermarking(bgImg *image.RGBA) (err error) {

	if nil == bgImg {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("bgImg is NULL")}
		return
	}

	dx := bgImg.Bounds().Dx()
	dy := bgImg.Bounds().Dy()

	wmp := beego.AppConfig.String("PicBaseDir") + "watermark.png" // watermark path
	watermark, err, wM, hM := loadImage(wmp)
	if nil != err {
		return
	}
	if wM <= 0 || hM <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("invalid watermark, width:%d, height:%d", wM, hM)}
		return
	}

	if dx < wM || dy < hM {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT,
			ErrInfo: fmt.Sprintf("backgroud image is small than watermark, image:%dx%d, watermark:%dx%d", dx, dy, wM, hM)}
		return
	}

	// m := image.NewNRGBA(bgImg.Bounds())
	// background
	// draw.Draw(m, m.Bounds(), bgImg, bgImg.Bounds().Min, draw.Src)
	// watermark
	offset := image.Pt((bgImg.Bounds().Dx()-watermark.Bounds().Dx())/2, (bgImg.Bounds().Dy()-watermark.Bounds().Dy())/2)
	draw.Draw(bgImg, watermark.Bounds().Add(offset), watermark, watermark.Bounds().Min, draw.Over)

	return
}
