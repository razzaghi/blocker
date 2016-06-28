package com.nad.utility.blocker.util;

import android.util.Log;

import com.nad.utility.blocker.BuildConfig;

public class Logger {
    private static boolean DEBUG = BuildConfig.DEBUG;

    public static void log(String msg) {
        if (DEBUG) {
            try {
                Log.d("Mr","[HaoBlocker] " + msg);
            } catch (Throwable t) {
                Log.i("[HaoBlocker]", msg);
            }
        }
    }

    public static void log(Throwable t) {
        if (DEBUG) {
            try {
                Log.d("Mr",t.toString());
            } catch (Throwable t1) {
                Log.i("[HaoBlocker]", "", t);
            }
        }
    }
}
