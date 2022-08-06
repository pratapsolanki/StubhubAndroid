package com.stubhub.event.utils

import android.util.Log
import com.stubhub.event.BuildConfig

/**
 * Global Log class (don't need to write log.d everytime)
 */
class Logger {
    companion object {

        private const val TAG: String = "Stubhub"

        fun d(msg: String) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, msg)
            }
        }

        fun e(msg: String) {
            if (BuildConfig.DEBUG) {
                Log.e(TAG, msg)
            }
        }

        fun i(msg: String) {
            if (BuildConfig.DEBUG) {
                Log.i(TAG, msg)
            }
        }

        fun w(msg: String) {
            if (BuildConfig.DEBUG) {
                Log.w(TAG, msg)
            }
        }

    }
}