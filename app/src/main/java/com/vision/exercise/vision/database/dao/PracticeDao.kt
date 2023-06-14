package com.vision.exercise.vision.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.vision.exercise.vision.model.Practice
import io.reactivex.rxjava3.core.Single

@Dao
interface PracticeDao {
    @Insert
    fun insert(practice: Practice): Single<Long>

    @Query("SELECT * FROM practice ORDER BY timeStamp DESC")
    fun selectAllPractice(): LiveData<List<Practice>>

    @Query("SELECT * FROM practice ORDER BY timeStamp DESC LIMIT 1")
    fun selectLastPractice(): LiveData<Practice?>
}
