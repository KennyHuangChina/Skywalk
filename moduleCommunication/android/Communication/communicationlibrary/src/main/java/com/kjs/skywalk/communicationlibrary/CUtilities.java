package com.kjs.skywalk.communicationlibrary;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * Created by kenny on 2017/5/9.
 */

class CUtilities {

    // get the file MIME type, like image/png, image/jpg and so on
    static public String getMimeType(String fileUrl) throws java.io.IOException {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String type = fileNameMap.getContentTypeFor(fileUrl);
        return type;
    }

    static public boolean isFileExist(String filePath) {
        if (filePath.isEmpty()) {
            Log.w("[isFileExist] ", "upload file not set");
            return false;
        }

        File upFile = new File(filePath);
        return upFile.exists();
    }

    static public boolean isPicture(String filePath) {

        if (!isFileExist(filePath)) {
            return false;
        }

        String contentType = "";
        try {
            contentType = getMimeType(filePath);
            if (null != contentType && !contentType.isEmpty() &&
                    contentType.toLowerCase().startsWith("image/")) {
                return true;
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    static private boolean isNeedScalDown(String imagePath, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; //不加载bitmap到内存中
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        options.inSampleSize = 1;
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 获取这个图片的宽和高，注意此处的bitmap为null
        int hsrc = options.outHeight;
        int wsrc = options.outWidth;
        if (wsrc * hsrc > width * height) {
            return true;
        }

        return false;
    }

    static public Bitmap getImageThumbnail(String imagePath, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; //不加载bitmap到内存中,只是为了读取宽度和高度
        // 获取这个图片的宽和高，注意此处的bitmap为null
//        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 计算缩放比
/*        int hsrc = options.outHeight;
        int wsrc = options.outWidth;
*/
        // get original picture
        options.inSampleSize = 1;
        options.inJustDecodeBounds = false; // 加载bitmap到内存中
        bitmap = BitmapFactory.decodeFile(imagePath, options);
        int hsrc = options.outHeight;
        int wsrc = options.outWidth;

        if (wsrc < hsrc && width > height) {    // rotate the out rectangle
            int tmp = width;
            width = height;
            height = tmp;
        }
        if (wsrc * hsrc <= width * height) { // do not scale down the picture, just export origianl picture
            return bitmap;
        } else {    // scale down the picture by rectangle defined by width x height
            int w1 = width;
            int h1 = width * hsrc / wsrc;
            if (h1 <= height) {
            } else {
                h1 = height;
                w1 = wsrc * height / hsrc;
            }
            return Bitmap.createScaledBitmap(bitmap, w1, h1, false);
        }

/*
        // 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        saveThePicture(bitmap, "/sdcard/test_thum2.jpeg");

        return bitmap; */
    }

    static public boolean saveThePicture(Bitmap bitmap, String thumFile)
    {
        File file = new File(thumFile);  // "/sdcard/test_thum.jpeg");
        try
        {
            FileOutputStream fos = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos))
            {
                fos.flush();
                fos.close();
            }
        }
        catch(FileNotFoundException e1)
        {
            e1.printStackTrace();
            return false;
        }
        catch(IOException e2)
        {
            e2.printStackTrace();
            return false;
        }

        return true;
    }

    static public InputStream getResizedPicure(String filePath) throws FileNotFoundException {
        int nBoundary_W = 1920;
        int nBoundary_H = 1080;

        if (isNeedScalDown(filePath, nBoundary_W, nBoundary_H)) {
            Bitmap bm = getImageThumbnail(filePath, nBoundary_W, nBoundary_H);
//        saveThePicture(bm, "/sdcard/test_thumb.jpg");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            InputStream sbs = new ByteArrayInputStream(baos.toByteArray());
            return sbs;
        }

        // just use the picture directly
        return new FileInputStream(filePath);
    }

    static public String PicFullUrl(String pic) {
        if (pic.startsWith("./")) {
            pic = pic.substring(1, pic.length());
        } else if (!pic.startsWith("/")) {
            pic = "/" + pic;
        }
        return ServerURL.mServerUri + pic;
    }
}
