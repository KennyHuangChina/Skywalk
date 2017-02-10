package com.kjs.skywalk.app_android;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.util.Log;

public class kjsLogUtil {
	private static boolean mDebug = true;
	private final static String TAG = kjsLogUtil.class.getSimpleName();
	
	public kjsLogUtil() {
		// TODO Auto-generated constructor stub
	}
	
	public static void d(String exTag, String method, String msg) {
		if(mDebug) {
			Log.d(exTag, String.format("[%s] %s", method, msg));
		}
	}
	
	public static void d(String msg) {
		if(mDebug) {
			Log.d(TAG, String.format("%s %s", getFileMethodLine(), msg));
		}
	}	
	
	public static void e(String exTag, String method, String msg) {
		if(mDebug) {
			Log.e(exTag, String.format("[%s] %s", method, msg));
		}
	}
	
	public static void e(String msg) {
		if(mDebug) {
			Log.e(TAG, String.format("%s %s", getFileMethodLine(), msg));
		}
	}		
	
	public static void i(String exTag, String method, String msg) {
		if(mDebug) {
			Log.i(exTag, String.format("[%s] %s", method, msg));
		}
	}
	
	public static void i(String msg) {
		if(mDebug) {
			Log.i(TAG, String.format("%s %s", getFileMethodLine(), msg));
		}
	}	
	
	public static void w(String exTag, String method, String msg) {
		if(mDebug) {
			Log.w(exTag, String.format("[%s] %s", method, msg));
		}
	}
	
	public static void w(String msg) {
		if(mDebug) {
			Log.w(TAG, String.format("%s %s", getFileMethodLine(), msg));
		}
	}	
	
	public static String __FILE__() {
		StackTraceElement traceElement = ((new Throwable()).getStackTrace())[1];
		return traceElement.getFileName();
	}

	public static String __FUNC__() {
		StackTraceElement traceElement = ((new Throwable()).getStackTrace())[1];
		return traceElement.getMethodName();
	}
	
	public static String __LINE__() {
		StackTraceElement traceElement = ((new Throwable()).getStackTrace())[1];
		return String.valueOf(traceElement.getLineNumber());
	}
	
	public static String __TIME__() {
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		return sdf.format(now);
	}
	
	private static String getFileMethodLine() {
		StackTraceElement traceElement = ((new Throwable()).getStackTrace())[2];
		return String.format("[%s:%s | %s]", traceElement.getFileName(), traceElement.getLineNumber(), traceElement.getMethodName());
	}
	
}
