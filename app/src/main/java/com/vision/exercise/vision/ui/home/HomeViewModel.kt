package com.vision.exercise.vision.ui.home

import androidx.lifecycle.MutableLiveData
import com.vision.exercise.vision.base.BaseInput
import com.vision.exercise.vision.base.BaseViewModel
import com.vision.exercise.vision.common.LINE_CHART_DATE_FORMAT
import com.vision.exercise.vision.database.AppDBHelper
import com.vision.exercise.vision.database.RealTimeDataBase
import com.vision.exercise.vision.extension.convertCurrentDateToChartDate
import com.vision.exercise.vision.extension.observeOnUiThread
import com.vision.exercise.vision.model.Exercise
import com.vision.exercise.vision.model.Practice
import com.vision.exercise.vision.util.DateFormatUtils.formatDateTime
import com.vision.exercise.vision.util.TimeUtils.getStringDayOfWeek
import java.util.Calendar
import java.util.Date

class HomeViewModel(input: BaseInput.NormalInput) : BaseViewModel(input) {

    private var dbHelper = AppDBHelper(input.application)
    private val _dataExercise = MutableLiveData<List<Exercise>>()

    init {
        addDisposables(
            RealTimeDataBase().getListExercise()
                .observeOnUiThread()
                .subscribe { list ->
                    _dataExercise.value = list
                }
        )
    }

    fun getPractice() = dbHelper.selectAllPractice()

    fun getListTotalCalo(listPractice: ArrayList<Practice>): List<Pair<String, Int>> {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val listCalo = arrayListOf<Pair<String, Int>>()

        repeat(7) { day ->
            listCalo.add(
                Pair(
                    getStringDayOfWeek(calendar),
                    listPractice.filter {
                        formatDateTime(it.timeStamp) == formatDateTime(calendar.timeInMillis)
                    }.sumOf { it.calo }
                )
            )
            calendar.add(Calendar.DATE, -1)
        }
        return listCalo.reversed()
    }

    fun getExercises() = _dataExercise
}
