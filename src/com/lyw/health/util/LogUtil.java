package com.lyw.health.util;

import android.util.Log;

public class LogUtil {

	static final boolean DEBUG = true;
	public static void d(Object object){
		
	}
	public static void d(Class<?> class1, String msg) {
		if (DEBUG) {
			Log.d(class1.getSimpleName(), msg);
		}

	}
	public static void d(Object o, String msg) {
		if (DEBUG) {
			Log.d(o.getClass().getSimpleName(), msg);
		}
		
	}

	public static void e(Class<?> class1, String msg) {
		if (DEBUG) {
			Log.e(class1.getSimpleName(), msg);
		}
	}

	public static void i(Class<?> class1, String msg) {
		if (DEBUG) {
			Log.i(class1.getSimpleName(), msg);
		}
	}

	public static void v(Class<?> class1, String msg) {
		if (DEBUG) {
			Log.v(class1.getSimpleName(), msg);
		}
	}

	public static void w(Class<?> class1, String msg) {
		if (DEBUG) {
			Log.w(class1.getSimpleName(), msg);
		}
	}
}
