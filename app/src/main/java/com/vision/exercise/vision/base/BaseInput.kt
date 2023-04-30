package com.vision.exercise.vision.base

import android.app.Application

sealed class BaseInput {
    object NoInput : BaseInput()
    data class MainInput(val testText1: String, val testText2: String) : BaseInput()

    data class NormalInput(val application: Application): BaseInput()

}
