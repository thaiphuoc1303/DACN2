package com.vision.exercise.vision.ui.exercise_details

import androidx.lifecycle.MutableLiveData
import com.vision.exercise.vision.base.BaseInput
import com.vision.exercise.vision.base.BaseViewModel
import com.vision.exercise.vision.model.Exercise
import com.vision.exercise.vision.model.ExerciseExt

class ExerciseDetailViewModel(private val input: BaseInput.ExerciseDetailInput): BaseViewModel(input) {
    private val _exercise = MutableLiveData<ExerciseExt>()

    init {
        input.exercise?.let {
            _exercise.value = it
        }
    }

    fun getExercise() = _exercise
}