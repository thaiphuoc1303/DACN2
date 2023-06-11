package com.vision.exercise.vision.database

import android.app.Application
import androidx.lifecycle.LiveData
import com.vision.exercise.vision.model.Practice
import io.reactivex.rxjava3.core.Single

class AppDBHelper(application: Application): DBHelper {

    private val appDatabase: AppDatabase

    init {
        appDatabase = AppDatabase.getDatabase(application)
    }

    override fun insertPractice(practice: Practice): Single<Long> {
        return appDatabase.practiceDao.insert(practice)
    }

    override fun selectAllPractice(): LiveData<List<Practice>> {
        return appDatabase.practiceDao.selectAllPractice()
    }
}