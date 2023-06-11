package com.vision.exercise.vision.util

import com.vision.exercise.R

object DrawableUtils {

    fun getType(type: String): Int {
        return when(type) {
            "Side bends" -> {
                R.drawable.ic_side_bends
            }

            "Pushup" -> {
                R.drawable.ic_pushup
            }
            "Side Leg Raise" -> {
                R.drawable.ic_skipping
            }

            "Back Turn" -> {
                R.drawable.ic_ab_workout
            }

            "Jumping Jack" -> {
                R.drawable.pic_jumping
            }

            else -> {
                R.drawable.ic_lowebody_workout
            }
        }
    }
}