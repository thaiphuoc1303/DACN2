package com.vision.exercise.vision.base

import android.app.Application
import com.anychart.core.Base
import com.vision.exercise.vision.model.Exercise
import com.vision.exercise.vision.model.ExerciseExt

sealed class BaseInput {
    object NoInput : BaseInput()
    data class MainInput(val testText1: String, val testText2: String) : BaseInput()

    data class NormalInput(val application: Application): BaseInput()

    data class ExerciseInput(val application: Application, val exercise: ExerciseExt?, val repeat: Int): BaseInput()

    data class ExerciseDetailInput(val application: Application, val exercise: ExerciseExt?): BaseInput()

    data class TodayTargetInput(val application: Application, val listExercise: List<Exercise>?): BaseInput()

}
