package com.sovnem.lib

import android.app.Activity
import com.sovnem.annotation.Constants

class OtherBuffer {
    companion object {
        @JvmStatic
        fun bind(activity: Activity) {
            val clazz =
                Class.forName(Constants.PACKAGE + "." + activity.javaClass.simpleName + Constants.CLASS_TAIL)
            clazz.getConstructor(Activity::class.java).newInstance(activity)
        }
    }
}