package com.vision.exercise.vision.extension

import android.content.Context


fun  Float.dpFromPx(context: Context) = this / context.resources.displayMetrics.density
fun  Float.pxFromDp(context: Context) = this / context.resources.displayMetrics.density

