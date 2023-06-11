package com.vision.exercise.vision.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Practice(
    val name: String,
    val type: Int,
    val time: Long,
    val repeat: Int,
    val timeStamp: Long,
    val calo: Int
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
}
