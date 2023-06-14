package com.vision.exercise.vision.model

import android.os.Parcelable
import com.vision.exercise.vision.common.EMPTY_STRING
import kotlinx.parcelize.Parcelize

@Parcelize
class ExerciseExt(
    var name: String,
    var description: String,
    var calo: Int,
    var thumbnail: String,
    var videoUrl: String,
    var steps: List<StepExt>,
    var type: Int = 0,
    var category: String = ""
): Parcelable {
    @Parcelize
    data class StepExt(
        var pose: String = EMPTY_STRING,
        var time: Int = 0,
        var position: Int = 0
    ): Parcelable {
    }
}