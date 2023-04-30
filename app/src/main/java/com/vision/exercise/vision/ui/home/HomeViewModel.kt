package com.vision.exercise.vision.ui.home

import androidx.lifecycle.MutableLiveData
import com.vision.exercise.vision.base.BaseInput
import com.vision.exercise.vision.base.BaseViewModel
import com.vision.exercise.vision.model.Exercise
import com.vision.exercise.R

class HomeViewModel(input: BaseInput.NormalInput): BaseViewModel(input) {
    private val _dataExercise = MutableLiveData<List<Exercise>>()

    private val dataEx = arrayListOf<Exercise>(
        Exercise("Fullbody Workout", "11 Exercises | 32mins", R.drawable.ic_skipping),
        Exercise("Lowebody Workout", "12 Exercises | 40mins", R.drawable.ic_lowebody_workout),
        Exercise("AB Workout", "14 Exercises | 20mins", R.drawable.ic_ab_workout)
    )

    init {
        _dataExercise.value = dataEx
    }

    fun getExercises() = _dataExercise
}