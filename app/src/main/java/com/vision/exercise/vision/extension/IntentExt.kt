package com.vision.exercise.vision.extension

import android.content.Intent
import android.os.Parcelable
import com.vision.exercise.vision.util.AppUtils
import java.io.Serializable

inline fun <reified T : Parcelable> Intent.parcelableArrayListExtra(key: String): ArrayList<T>? =
    when {
        AppUtils.isAndroid_TIRAMISU_AndAbove() -> getParcelableArrayListExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableArrayListExtra(key)
    }

inline fun <reified T : Parcelable> Intent.parcelableExtra(key: String): T? = when {
    AppUtils.isAndroid_TIRAMISU_AndAbove() -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}

inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
    AppUtils.isAndroid_TIRAMISU_AndAbove() -> getSerializableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
}