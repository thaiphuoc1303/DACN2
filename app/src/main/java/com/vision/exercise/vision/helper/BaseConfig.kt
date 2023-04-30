package com.vision.exercise.vision.helper

import android.content.Context
import android.os.Build
import com.vision.exercise.vision.common.*

open class BaseConfig(context: Context) {
    private val prefs = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)

    companion object {
        fun newInstance(context: Context) = BaseConfig(context)
    }

    var userName: String
        get() = prefs.getString(KEY_USER_NAME, Build.MODEL) ?: Build.MODEL
        set(name) = prefs.edit().putString(KEY_USER_NAME, name).apply()

    var showOnBoardingActivity: Boolean
        get() = prefs.getBoolean(KEY_SHOW_ON_BOARDING, true)
        set(isShow) = prefs.edit().putBoolean(KEY_SHOW_ON_BOARDING, isShow).apply()

    var userAge: Int
        get() = prefs.getInt(KEY_AGE, 18)
        set(age: Int) = prefs.edit().putInt(KEY_AGE, age).apply()

    var userGender: Int
        get() = prefs.getInt(KEY_GENDER, MALE)
        set(gender: Int) = prefs.edit().putInt(KEY_GENDER, gender).apply()

    var userWeight : Int
        get() = prefs.getInt(KEY_WEIGHT, 50)
        set(weight: Int) = prefs.edit().putInt(KEY_WEIGHT, weight).apply()

    var useHeight: Int
        get() = prefs.getInt(KEY_HEIGHT, 160)
        set(height: Int) = prefs.edit().putInt(KEY_HEIGHT, height).apply()

    var purpose: Int
        get() = prefs.getInt(KEY_PURPOSE, Purpose.BODY.ordinal)
        set(purpose) = prefs.edit().putInt(KEY_PURPOSE, purpose).apply()
}
