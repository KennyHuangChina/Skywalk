package com.kjs.skywalk.communicationlibrary;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
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
import android.media.ThumbnailUtils;
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

    static public boolean isPicture(String filePath) {
        String contentType = "";

        try {
            contentType = getMimeType(filePath);
            if (!contentType.isEmpty() &&
                    contentType.toLowerCase().startsWith("image/")) {
                return true;
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    /**
     * 根据指定的图像路径和大小来获取缩略图
     * 此方法有两点好处：
     *     1. 使用较小的内存空间，第一次获取的bitmap实际上为null，只是为了读取宽度和高度，
     *        第二次读取的bitmap是根据比例压缩过的图像，第三次读取的bitmap是所要的缩略图。
     *     2. 缩略图对于原图像来讲没有拉伸，这里使用了2.2版本的新工具ThumbnailUtils，使
     *        用这个工具生成的图像不会被拉伸。
     * @param imagePath 图像的路径
     * @param width 指定输出图像的宽度
     * @param height 指定输出图像的高度
     * @return 生成的缩略图
     */
    static public Bitmap getImageThumbnail(String imagePath, int width, int height) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; //不加载bitmap到内存中
        // 获取这个图片的宽和高，注意此处的bitmap为null
//        bitmap = BitmapFactory.decodeFile(imagePath, options);
        // 计算缩放比
/*        int hsrc = options.outHeight;
        int wsrc = options.outWidth;
        int be = 1;
        Log.d(TAG, "be:" + be);
        int beWidth = w / width;
        int beHeight = h / height;
        if (beWidth < beHeight) {
            be = beWidth;
        } else {
            be = beHeight;
        }
        if (be <= 0) {
            be = 1;
        }
        options.inSampleSize = be;
        // 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
        options.inJustDecodeBounds = false; // 加载bitmap到内存中
        bitmap = BitmapFactory.decodeFile(imagePath, options);
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

    static public InputStream getResizedPicure(String filePath) {
        Bitmap bm = getImageThumbnail(filePath, 1920, 1080);
//        saveThePicture(bm, "/sdcard/test_thumb.jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        InputStream sbs = new ByteArrayInputStream(baos.toByteArray());
        return sbs;
    }
}
