package com.kjs.skywalk.app_android;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by sailor.zhou on 2018/1/11.
 */

// catch Runtime exception
public class kjsCrashHandler implements Thread.UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private Context mContext;
    private String TAG = this.getClass().getSimpleName();
    private static kjsCrashHandler mInstance;
    private Map<String, String> mInfoMap = new HashMap<String, String>();

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        handleException(t, e);
    }

    public static synchronized kjsCrashHandler getInstance() {
        if(mInstance == null) {
            mInstance = new kjsCrashHandler();
        }

        return mInstance;
    }

    public void init(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    private boolean handleException(Thread thread, Throwable ex) {
        Log.i(TAG, "thread: " + thread.getStackTrace().toString() + ", ex: " + ex.toString());

        collectSystemInfo(mContext);
        collectApkInfo(mContext);

        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "Sorry", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();

        saveInfo(mContext, ex);

        return true;
    }

    private void collectSystemInfo(Context context) {
        // system info
        Field[] fields = Build.class.getDeclaredFields();
        for(Field field : fields) {
            field.setAccessible(true);
            try {
                mInfoMap.put(field.getName(), field.get(null).toString());
            } catch (IllegalAccessException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.e(TAG, "an error occured when collect system info", e);
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.e(TAG, "an error occured when collect system info", e);
            }
        }
    }

    private void collectApkInfo(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pInfo = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if(pInfo != null) {
                mInfoMap.put("versionName", (pInfo.versionName == null ? "" : pInfo.versionName));
                mInfoMap.put("versionCode", String.valueOf(pInfo.versionCode));
            }
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e(TAG, "an error occured when collect apk info", e);
        }
    }

    private void saveInfo(Context context, Throwable ex) {
        StringBuffer sBuf = new StringBuffer();
        for(Map.Entry<String, String> entry : mInfoMap.entrySet()) {
            String info = entry.getKey() + " = " + entry.getValue();
            sBuf.append(info);
            sBuf.append(System.getProperty("line.separator"));
            Log.i(TAG, info);
        }

        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();

        sBuf.append(writer.toString());

        SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        long curTimeS = System.currentTimeMillis();
        String cTime = sdFormat.format(new Date());
        String fileName = "crash-" + cTime + "-" + curTimeS + ".log";
        String crashFilePath;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            crashFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "kjsCrash" + File.separator;
            File dir = new File(crashFilePath);
            if(!dir.exists()) {
                dir.mkdirs();
            }
            try {
                FileOutputStream fos = new FileOutputStream(crashFilePath + fileName);
                fos.write(sBuf.toString().getBytes());
                fos.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.e(TAG, "an error occured when writing file to " + crashFilePath + fileName, e);
            }
        }
    }
}
