package com.vision.exercise.vision.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vision.exercise.vision.database.dao.PracticeDao
import com.vision.exercise.vision.model.Practice
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Database(
    entities = [Practice::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase: RoomDatabase()  {
    abstract val practiceDao: PracticeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        val executorService: ExecutorService = Executors.newFixedThreadPool(4)

        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class.java) {
                    INSTANCE = Room
                        .databaseBuilder(context, AppDatabase::class.java, "database.db")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }

            return INSTANCE!!
        }
    }
}
