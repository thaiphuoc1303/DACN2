package com.vision.exercise.vision.util

import android.os.Build

object AppUtils {

    fun isAndroid_Q_AndAbove() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    fun isAndroid_R_AndAbove() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R

}
