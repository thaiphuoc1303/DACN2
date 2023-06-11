package com.vision.exercise.vision.database

import androidx.lifecycle.LiveData
import com.vision.exercise.vision.model.Practice
import io.reactivex.rxjava3.core.Single

interface DBHelper {
    fun insertPractice(practice: Practice): Single<Long>
    fun selectAllPractice(): LiveData<List<Practice>>
}