package com.vision.exercise.vision.model

import android.os.Parcelable
import com.vision.exercise.vision.common.EMPTY_STRING
import kotlinx.parcelize.Parcelize

@Parcelize
data class Exercise(
    var name: String = EMPTY_STRING,
    var description: String = EMPTY_STRING,
    var calo: Int = 0,
    var thumbnail: String = EMPTY_STRING,
    var videoUrl: String = EMPTY_STRING,
    var steps: List<Step> = arrayListOf(),
    var type: Int = 0
):Parcelable {
    constructor() : this("", "",  0, "", "", arrayListOf(),0)
    @Parcelize
    data class Step(
        var pose: String = EMPTY_STRING,
        var time: Int = 0,
        var position: Int = 0
    ):Parcelable {
        constructor() : this (EMPTY_STRING, 0, 0)
    }

    fun convertToExt(): ExerciseExt {
        val listStepExt = arrayListOf<ExerciseExt.StepExt>()
        this.steps.forEach {
            listStepExt.add(
                ExerciseExt.StepExt(
                it.pose,
                it.time,
                it.position
            ))
        }
        return ExerciseExt(
            this.name,
            this.description,
            this.calo,
            this.thumbnail,
            this.videoUrl,
            listStepExt,
            this.type
        )
    }
}
