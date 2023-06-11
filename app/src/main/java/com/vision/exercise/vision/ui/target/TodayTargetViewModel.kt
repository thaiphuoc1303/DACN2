package com.vision.exercise.vision.ui.target

import androidx.lifecycle.MutableLiveData
import com.vision.exercise.vision.base.BaseInput
import com.vision.exercise.vision.base.BaseViewModel
import com.vision.exercise.vision.model.Exercise

class TodayTargetViewModel(private val input: BaseInput.TodayTargetInput): BaseViewModel(input) {
    private val _listPractice = input.listExercise

    fun getListExercise() = _listPractice


}