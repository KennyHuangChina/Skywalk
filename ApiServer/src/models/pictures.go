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
	"image/jpeg"
	"image/png"
	"os"
	"strings"
)

/**
*	add new picture
*	Arguments:
*		hid 	- house id
*		uid 	- login user
*		pt		- picture type
*		XId		- reference id, depend what type it is
*		pfn		- picture file name, relative path against picture base dir
*		pbd		- picture base dir
*		desc	- picture description
*	Returns
*		err - error info
*		nid - new picture id
*	Comments:
**/
func AddPicture(hid, uid, XId int64, pt int, desc, md5, pfn, pbd string) (err error, nid int64) {
	FN := "[AddPicture] "
	beego.Trace(FN, "house:", hid, ", user:", uid, ", picture type:", pt, ", desc:", desc, ", md5:", md5, ", pic file:", pfn, ", base dir:", pbd)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/* Arguments checking */
	// house id 0 means this picture is not related with house, it maybe the agency's portrait
	if 0 != hid {
		/* Permission checking */
		if err = canModifyHouse(uid, hid); nil != err {
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
	if pos := strings.LastIndex(pfn, "."); pos < 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("picture file name:%s", pfn)}
		return
	}

	if 0 == len(desc) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("picture desc:%s", desc)}
		return
	}

	if 0 == len(md5) {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("picture md5:%s", md5)}
		return
	}
	// picture type
	err, majorType, minorType := checkPictureType(pt)
	if nil != err {
		return
	}
	if commdef.PIC_TYPE_HOUSE == majorType && 0 == hid {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("house:%d for PIC_TYPE_HOUSE", hid)}
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
	case commdef.PIC_TYPE_HOUSE:
		return addPicHouse(hid, minorType, desc, md5, pfn, pbd)
	case commdef.PIC_TYPE_USER:
		return addPicUser(XId, minorType, desc, md5, pfn, pbd)
	case commdef.PIC_TYPE_RENTAL:
	default:
	}

	err = commdef.SwError{ErrCode: commdef.ERR_NOT_IMPLEMENT}
	return
}

/**
*	Get picture url
*	Arguments:
*		hid 	- house id
*		uid 	- login user id
*		size	- picture size: PIC_SIZE_ALL, PIC_SIZE_SMALL, PIC_SIZE_MODERATE, PIC_SIZE_LARGE,
*	Returns
*		err 	- error info
*		picList	- picture list
**/
func GetHousePicList(hid, uid int64, subType int) (err error, picList []commdef.HousePicture) {
	FN := "[GetHousePicList] "
	beego.Trace(FN, "house:", hid, ", login user:", uid, ", sub type:", subType)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/* Argument Checking */
	err, h := getHouse(hid)
	if nil != err {
		return
	}

	if subType < 0 || subType > commdef.PIC_HOUSE_END {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("subType:%d", subType)}
		return
	}

	// checkPicturePermission(pic, uid)
	sts := getHousePicSubtypes(h, uid, subType)
	if 0 == len(sts) {
		return
	}

	/* Querying */
	o := orm.NewOrm()
	qs := o.QueryTable("tbl_pictures").Filter("TypeMajor", commdef.PIC_TYPE_HOUSE).Filter("TypeMinor__in", sts)
	qs = qs.Filter("RefId", hid)

	var pl []TblPictures
	numb, errT := qs.OrderBy("TypeMinor", "RefId").All(&pl)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("err:%s", errT)}
		return
	}
	beego.Debug(FN, "", numb, "pictures")

	for _, v := range pl {
		np := commdef.HousePicture{Id: v.Id, Desc: v.Desc, SubType: v.TypeMinor, Checksum: v.Md5}
		picList = append(picList, np)
	}

	return
}

/**
*	Get picture url
*	Arguments:
*		pid 	- picture id
*		uid 	- login user id
*		size	- picture size: PIC_SIZE_ALL, PIC_SIZE_SMALL, PIC_SIZE_MODERATE, PIC_SIZE_LARGE,
*	Returns
*		err - error info
*		url_s - small picture url
*		url_m - moderate picture url
*		url_l - large picture url
**/
func GetPicUrl(pid, uid int64, size int) (err error, url_s, url_m, url_l string) {
	FN := "[GetPicUrl] "
	beego.Trace(FN, "picture:", pid, ", login user:", uid, ", size:", size)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/* Argument Checking */
	err, pic := getPicture(pid)
	if nil != err {
		return
	}

	if err = checkPicturePermission(&pic, uid); nil != err {
		return
	}
	// if uid <= 0 {
	// 	// err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("user id:%d", uid)}
	// 	// return
	// }
	if size < commdef.PIC_SIZE_ALL || size > commdef.PIC_SIZE_LARGE {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("size:%d", size)}
		return
	}

	/* Querying */
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

	// beego.Debug(FN, "s:", url_s, ", m:", url_m, ", l:", url_l)
	return
}

/**
*	Delete picture
*	Arguments:
*		pic 	- picture id
*		uid 	- login user id
*	Returns
*		err - error info
**/
func DelImage(pic, uid int64) (err error) {
	FN := "[DelImage] "
	beego.Debug(FN, "image:", pic, ", login user:", uid)

	defer func() {
		if nil != err {
			beego.Error(FN, err)
		}
	}()

	/* Arguments checking */
	err, _ = GetUser(uid)
	if nil != err {
		return
	}
	err, p := getPicture(pic)
	if nil != err {
		return
	}

	if p.TypeMajor == commdef.PIC_TYPE_HOUSE {
		house := p.RefId
		errT, h := getHouse(house)
		if nil != errT {
			err = errT
			return
		}
		/* Permission checking */
		// Only the house owner, its agency and administrator could remove the picture
		if isHouseOwner(h, uid) || isHouseAgency(h, uid) {
		} else if _, bAdmin := isAdministrator(uid); bAdmin {
		} else {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("login user:%d", uid)}
			return
		}
	} else {
		beego.Warn("TODO: Do permission checking here for other kind of pictures")
	}

	beego.Warn(FN, "TODO: check if this picture has been used by some other object")

	/* Processing */
	o := orm.NewOrm()
	// picture set
	var psl []*TblPicSet
	numb, errT := o.QueryTable("tbl_pic_set").Filter("PicId", pic).All(&psl)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}
	beego.Debug(FN, numb, "record in database")

	picBaseDir := GetPicBaseDir()
	for _, v := range psl {
		DelImageFile(picBaseDir + v.Url)
	}
	o.Begin()
	defer func() {
		if nil == err {
			o.Commit()
		} else {
			o.Rollback()
		}
	}()

	// ps := TblPicSet{PicId: pic}
	// beego.Debug(FN, "picture to delete:", fmt.Sprintf("%+v", ps))
	// numb, errT = o.Delete(&ps)
	numb, errT = o.QueryTable("tbl_pic_set").Filter("PicId", pic).Delete()
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}
	beego.Debug(FN, "delete", numb, "records from picture set")

	// picture
	numb, errT = o.Delete(&TblPictures{Id: pic})
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}
	beego.Debug(FN, "delete", numb, "records from picture")

	return
}

// delete the image specified
func DelImageFile(image string) (err error) {
	FN := "[DelImageFile] "
	beego.Debug(FN, "image:", image)

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
	} else {
		beego.Warn(FN, "Image not found")
	}

	return
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//
//		Internal Functions
//
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
func getPicture(pic int64) (err error, p TblPictures) {
	FN := "[getPicture] "

	if pic <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("pic:%d", pic)}
		return
	}

	o := orm.NewOrm()
	p1 := TblPictures{Id: pic}
	if errT := o.Read(&p1); nil != errT {
		if orm.ErrNoRows == errT || orm.ErrMissPK == errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("pic:%d", pic)}
		} else {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		}
		return
	}

	p = p1
	beego.Debug(FN, fmt.Sprintf("%+v", p))
	return
}

func checkPictureType(picType int) (err error, majorType, minorType int) {
	FN := "[checkPictureType] "

	if picType <= 0 {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("picType:%d", picType)}
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
		case commdef.PIC_HOUSE_FLOOR_PLAN:
			fallthrough
		case commdef.PIC_HOUSE_FURNITURE:
			fallthrough
		case commdef.PIC_HOUSE_APPLIANCE:
			fallthrough
		case commdef.PIC_HOUSE_OwnershipCert:
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

func addPicUser(uid int64, minorType int, desc, md5, pfn, pbd string) (err error, nid int64) {
	FN := "[addPicUser] "
	beego.Debug(FN, "user:", uid, ", minor type:", minorType, ", desc:", desc, ", md5:", md5, ", pic file:", pfn, ", base dir:", pbd)

	switch minorType {
	case commdef.PIC_USER_HEAD_PORTRAIT:
		err, nid = addPic(uid, commdef.PIC_TYPE_USER, minorType, desc, md5, pfn, pbd, UserPortrait)
	case commdef.PIC_OWNER_IDCard:
		fallthrough
	default:
		err, nid = addPic(uid, commdef.PIC_TYPE_USER, minorType, desc, md5, pfn, pbd, CommonPicSL)
	}
	return
}

func addPicHouse(hid int64, minorType int, desc, md5, pfn, pbd string) (err error, nid int64) {
	FN := "[addPicHouse] "
	beego.Debug(FN, "house:", hid, ", minor type:", minorType, ", desc:", desc, ", md5:", md5, ", pic file:", pfn, ", base dir:", pbd)

	err, nid = addPic(hid, commdef.PIC_TYPE_HOUSE, minorType, desc, md5, pfn, pbd, CommonPicAll)
	return
}

func addPic(id int64, majorType, minorType int, desc, md5, pfn, pbd string, size map[string]PicResize) (err error, nid int64) {
	FN := "[addPic] "
	beego.Debug(FN, "ref id:", id, "type:(", majorType, ",", minorType, "), desc:", desc, ", md5:", md5, ", pic file:", pfn, ", base dir:", pbd)

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
	extName := pfn[pos:]
	beego.Debug(FN, "pic:", picName, ", ext:", extName)

	psn := "" // picture name for small size
	pmn := "" // picture name for modrate size
	pln := "" // picture name for large size
	bsp, bmp, blp := false, false, false

	o := orm.NewOrm()

	o.Begin()
	defer func() {
		if nil != err {
			o.Rollback()
			// delete the pictures created in this function
			if len(psn) > 0 && bsp {
				DelImageFile(pbd + psn)
			}
			if len(pmn) > 0 && bmp {
				DelImageFile(pbd + pmn)
			}
			if len(pln) > 0 && blp {
				DelImageFile(pbd + pln)
			}
			nid = 0
		} else {
			o.Commit()
		}
	}()

	// picture major record
	newPic := TblPictures{TypeMajor: majorType, TypeMinor: minorType, RefId: id, Desc: desc, Md5: md5}
	id, errT := o.Insert(&newPic)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: errT.Error()}
		return
	}
	nid = id
	beego.Debug(FN, "new pic:", nid)

	// generate picture set, original, large size, modrate size and small size
	// original picture
	po := TblPicSet{PicId: nid, Size: commdef.PIC_SIZE_ORIGINAL, Url: pfn}
	_, errT = o.Insert(&po)
	if nil != errT {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("Fail to create original picture, err:%s", errT.Error())}
		return
	}

	src, err, dx, dy := loadImage(pbd + pfn)
	beego.Debug(FN, "original pic:", dx, "x", dy)
	if nil != err {
		return
	}

	// Small size
	if v, ok := size[PIC_SIZE_SMALL_STR]; ok {
		beego.Debug(FN, PIC_SIZE_SMALL_STR)
		psn = picName + "_s.jpg" // + extName
		sw, sh := v.w, v.h
		beego.Debug(FN, fmt.Sprintf("size: %d x %d", sw, sh))
		if dx*dy > sw*sh {
			if err = resizeImage(src, pbd+psn, sw, sh, 50, nil); nil != err {
				return
			}
			bsp = true
		} else { // original image is small than defined "small size", use original image
			psn = pfn
		}
		ps := TblPicSet{PicId: nid, Size: commdef.PIC_SIZE_SMALL, Url: psn}
		if _, errT = o.Insert(&ps); nil != errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("Fail to create small picture, err:%s", errT.Error())}
			return
		}
	}

	// Modrate size
	if v, ok := size[PIC_SIZE_MODERATE_STR]; ok {
		beego.Debug(FN, PIC_SIZE_MODERATE_STR)
		pmn = picName + "_m.jpg" // + extName
		mw, mh := v.w, v.h
		beego.Debug(FN, fmt.Sprintf("size: %d x %d", mw, mh))
		if dx*dy > mw*mh {
			if err = resizeImage(src, pbd+pmn, mw, mh, 80, nil); nil != err {
				return
			}
			bmp = true
		} else { // original image is small than defined "modrate size", use original image
			pmn = pfn
		}
		ps := TblPicSet{PicId: nid, Size: commdef.PIC_SIZE_MODERATE, Url: pmn}
		if _, errT = o.Insert(&ps); nil != errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("Fail to create modrate picture, err:%s", errT.Error())}
			return
		}
	}

	// Large size
	if v, ok := size[PIC_SIZE_LARGE_STR]; ok {
		beego.Debug(FN, PIC_SIZE_LARGE_STR)
		pln = picName + "_l.jpg" // + extName
		lw, lh := v.w, v.h
		beego.Debug(FN, fmt.Sprintf("size: %d x %d", lw, lh))
		if dx*dy < lw*lh { // original image is small than defined "large size", make a copy
			lw = dx
			lh = dy
			beego.Debug(FN, "large size:", lw, "x", lh)
		}
		if v.wm {
			err = resizeImage(src, pbd+pln, lw, lh, 100, watermarking)
		} else {
			err = resizeImage(src, pbd+pln, lw, lh, 100, nil)
		}
		if nil != err {
			return
		}
		blp = true

		pl := TblPicSet{PicId: nid, Size: commdef.PIC_SIZE_LARGE, Url: pln}
		_, errT = o.Insert(&pl)
		if nil != errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("Fail to create large picture, err:%s", errT.Error())}
			return
		}
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
	beego.Debug(FN, "image:", path)

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

	img, _, errT = image.Decode(file) // common image decoder, include jpe, png ...
	if nil != errT {
		// image.Decode may failed for png, try specialized png decoder again
		img, errT = png.Decode(file)
		if nil != errT {
			err = commdef.SwError{ErrCode: commdef.ERR_COMMON_UNEXPECTED, ErrInfo: fmt.Sprintf("Decode image, err:%s", errT.Error())}
			return
		}
	}

	w = img.Bounds().Dx()
	h = img.Bounds().Dy()

	return
}

// watermarking image
//		bg - background image
func watermarking(bgImg *image.RGBA) (err error) {
	FN := "[watermarking] "

	if nil == bgImg {
		err = commdef.SwError{ErrCode: commdef.ERR_COMMON_BAD_ARGUMENT, ErrInfo: fmt.Sprintf("bgImg is NULL")}
		return
	}

	dx := bgImg.Bounds().Dx()
	dy := bgImg.Bounds().Dy()

	wmp := GetPicBaseDir()
	if 0 == len(wmp) {
		beego.Warn(FN, `this is just for testing by "go test"`)
		wmp = "../pics/"
	}
	wmp += "watermark.png" // watermark path
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

func GetPicBaseDir() string {
	return beego.AppConfig.String("PicBaseDir")
}

func getPortraitSize() (w, h int) {
	w, _ = beego.AppConfig.Int("user_portrait_w")
	h, _ = beego.AppConfig.Int("user_portrait_h")
	if 0 == w || 0 == h {
		w = 400
		h = 400
	}
	return
}

func getSmallPicSize() (w, h int) {
	w, _ = beego.AppConfig.Int("small_pic_w")
	h, _ = beego.AppConfig.Int("small_pic_h")
	if 0 == w || 0 == h {
		w = 500
		h = 500
	}
	return
}

func getModeratePicSize() (w, h int) {
	w, _ = beego.AppConfig.Int("moderate_pic_w")
	h, _ = beego.AppConfig.Int("moderate_pic_h")
	if 0 == w || 0 == h {
		w = 1000
		h = 1000
	}
	return
}

func getLargePicSize() (w, h int) {
	w, _ = beego.AppConfig.Int("lare_pic_w")
	h, _ = beego.AppConfig.Int("lare_pic_h")
	if 0 == w || 0 == h {
		w = 2000
		h = 2000
	}
	return
}

func checkPicturePermission(pic *TblPictures, uid int64) (err error) {

	switch pic.TypeMajor {
	case commdef.PIC_TYPE_HOUSE:
		switch pic.TypeMinor {
		case commdef.PIC_HOUSE_OwnershipCert: // house ownership certification is only accessable for landlord, its agency and andministrator
			if err, _ = GetUser(uid); nil != err {
				return
			}
			h := TblHouse{}
			err, h = getHouse(pic.RefId)
			if nil != err {
				return
			}
			if isHouseOwner(h, uid) || isHouseAgency(h, uid) {
			} else if _, bAdmin := isAdministrator(uid); bAdmin {
			} else {
				err = commdef.SwError{ErrCode: commdef.ERR_COMMON_PERMISSION, ErrInfo: fmt.Sprintf("login user:%d", uid)}
			}
			return
		default:
		}
	case commdef.PIC_TYPE_USER:
	case commdef.PIC_TYPE_RENTAL:
	default:
	}

	/* some pictures are private, like landlord id card */
	beego.Warn("TODO: Check piture permission here by picture type")

	return
}

func getHousePicSubtypes(h TblHouse, uid int64, subType int) (stl []int) {
	FN := "[getHousePicSubtypes] "
	beego.Info(FN, "house:", fmt.Sprintf("%+v", h), ", login user:", uid, ", subType:", subType)

	if 0 == subType || subType == commdef.PIC_HOUSE_FLOOR_PLAN {
		// Public picture, all user could access
		stl = append(stl, commdef.PIC_HOUSE_FLOOR_PLAN)
	}

	if 0 == subType || subType == commdef.PIC_HOUSE_FURNITURE {
		// Public picture, all user could access
		stl = append(stl, commdef.PIC_HOUSE_FURNITURE)
	}

	if 0 == subType || subType == commdef.PIC_HOUSE_APPLIANCE {
		// Public picture, all user could access
		stl = append(stl, commdef.PIC_HOUSE_APPLIANCE)
	}

	if 0 == subType || subType == commdef.PIC_HOUSE_OwnershipCert {
		// Private picture, only the landlord, house agency and administrator could access
		bAccess := false
		if isHouseOwner(h, uid) || isHouseAgency(h, uid) {
			bAccess = true
		} else if _, bAdmin := isAdministrator(uid); bAdmin {
			bAccess = true
		}
		if bAccess {
			stl = append(stl, commdef.PIC_HOUSE_OwnershipCert)
		}
	}

	beego.Debug(FN, "sub-type list:", stl)
	return
}

type PicResize struct {
	w  int  // picture width
	h  int  // picture height
	wm bool // if overlay watermark
}

const (
	PIC_SIZE_SMALL_STR    = "small"
	PIC_SIZE_MODERATE_STR = "moderate"
	PIC_SIZE_LARGE_STR    = "large"
)

var CommonPicAll map[string]PicResize
var CommonPicSL map[string]PicResize
var UserPortrait map[string]PicResize

func init() {

	// common picture size, all sizes
	CommonPicAll = make(map[string]PicResize)
	w, h := getSmallPicSize()
	CommonPicAll[PIC_SIZE_SMALL_STR] = PicResize{w, h, false}
	w, h = getModeratePicSize()
	CommonPicAll[PIC_SIZE_MODERATE_STR] = PicResize{w, h, false}
	w, h = getLargePicSize()
	CommonPicAll[PIC_SIZE_LARGE_STR] = PicResize{w, h, true}
	beego.Debug(fmt.Sprintf("CommonPicAll: %+v", CommonPicAll))

	// common picture size, Small and Large
	CommonPicSL = make(map[string]PicResize)
	w, h = getSmallPicSize()
	CommonPicSL[PIC_SIZE_SMALL_STR] = PicResize{w, h, false}
	w, h = getLargePicSize()
	CommonPicSL[PIC_SIZE_LARGE_STR] = PicResize{w, h, true}
	beego.Debug(fmt.Sprintf("CommonPicSL: %+v", CommonPicSL))

	// user portrait picture size
	UserPortrait = map[string]PicResize{}
	w, h = getPortraitSize()
	UserPortrait[PIC_SIZE_SMALL_STR] = PicResize{w, h, false}
	beego.Debug(fmt.Sprintf("UserPortrait: %+v", UserPortrait))
}
