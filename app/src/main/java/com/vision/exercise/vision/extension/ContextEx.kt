package com.vision.exercise.vision.extension

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import com.vision.exercise.vision.helper.BaseConfig

fun Context?.showToast(mes: String) {
    this?.let {
        Toast.makeText(this, mes, Toast.LENGTH_SHORT).show()
    }
}

fun Context?.showToast(@StringRes mes: Int) {
    this?.let {
        Toast.makeText(this, mes, Toast.LENGTH_SHORT).show()
    }
}

fun Context.getBaseConfig(): BaseConfig {
    return BaseConfig.newInstance(this)
}
