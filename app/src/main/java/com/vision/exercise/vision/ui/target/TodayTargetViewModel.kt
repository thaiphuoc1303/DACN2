package com.vision.exercise.vision.ui.target

import androidx.lifecycle.MutableLiveData
import com.vision.exercise.vision.base.BaseInput
import com.vision.exercise.vision.base.BaseViewModel
import com.vision.exercise.vision.database.AppDBHelper
import com.vision.exercise.vision.database.AppDatabase
import com.vision.exercise.vision.model.Exercise

class TodayTargetViewModel(private val input: BaseInput.TodayTargetInput): BaseViewModel(input) {

    private val dbHelper = AppDBHelper(input.application)

    private val _listPractice = arrayListOf<Exercise>()

    init {
        input.listExercise?.let {
            _listPractice.addAll(it)
        }
    }

    fun getListExercise() = _listPractice

    fun getLastPractice() = dbHelper.selectLastPractice()
}