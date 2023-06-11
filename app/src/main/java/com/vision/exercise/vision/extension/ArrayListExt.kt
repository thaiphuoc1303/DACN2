package com.vision.exercise.vision.extension

import android.graphics.drawable.Drawable
import com.straiberry.android.charts.extenstions.getValues
import com.straiberry.android.common.extensions.getDate
import java.util.concurrent.TimeUnit

//internal fun ArrayList<HashMap<String, Int?>>.toChartData(): List<Triple<Drawable?, String, Float>> {
//    val currentTime = System.currentTimeMillis()
//
//    return listOf(
//        Triple(
//            null,
//            (currentTime - TimeUnit.DAYS.toMillis(SixDay)).getDate(),
//            try {
//                this.getValues((currentTime - TimeUnit.DAYS.toMillis(SixDay)))
//            } catch (e: Exception) {
//                EmptyChartData
//            }
//        ),
//        Triple(
//            null,
//            (currentTime - TimeUnit.DAYS.toMillis(FiveDay)).getDate(),
//            try {
//                this.getValues((currentTime - TimeUnit.DAYS.toMillis(FiveDay)))
//            } catch (e: Exception) {
//                EmptyChartData
//            }
//        ),
//        Triple(
//            null,
//            (currentTime - TimeUnit.DAYS.toMillis(FourDay)).getDate(),
//            try {
//                this.getValues((currentTime - TimeUnit.DAYS.toMillis(FourDay)))
//            } catch (e: Exception) {
//                EmptyChartData
//            }
//        ),
//        Triple(
//            null,
//            (currentTime - TimeUnit.DAYS.toMillis(ThreeDay)).getDate(),
//            try {
//                this.getValues((currentTime - TimeUnit.DAYS.toMillis(ThreeDay)))
//            } catch (e: Exception) {
//                EmptyChartData
//            }
//        ),
//        Triple(
//            null,
//            (currentTime - TimeUnit.DAYS.toMillis(TwoDay)).getDate(),
//            try {
//                this.getValues((currentTime - TimeUnit.DAYS.toMillis(TwoDay)))
//            } catch (e: Exception) {
//                EmptyChartData
//            }
//        ),
//        Triple(
//            null,
//            (currentTime - TimeUnit.DAYS.toMillis(OneDay)).getDate(),
//            try {
//                this.getValues((currentTime - TimeUnit.DAYS.toMillis(OneDay)))
//            } catch (e: Exception) {
//                EmptyChartData
//            }
//        ),
//        Triple(
//            null,
//            "",
//            try {
//                this.getValues((currentTime))
//            } catch (e: Exception) {
//                EmptyChartData
//            }
//        )
//    )
//}
//internal fun List<HashMap<String, Int?>>.toChartData(): List<Triple<Drawable?, String, Float>> {
//    val currentTime = System.currentTimeMillis()
//
//    return listOf(
//        Triple(
//            null,
//            (currentTime - TimeUnit.DAYS.toMillis(SixDay)).getDate(),
//            try {
//                this.getValues((currentTime - TimeUnit.DAYS.toMillis(SixDay)))
//            } catch (e: Exception) {
//                EmptyChartData
//            }
//        ),
//        Triple(
//            null,
//            (currentTime - TimeUnit.DAYS.toMillis(FiveDay)).getDate(),
//            try {
//                this.getValues((currentTime - TimeUnit.DAYS.toMillis(FiveDay)))
//            } catch (e: Exception) {
//                EmptyChartData
//            }
//        ),
//        Triple(
//            null,
//            (currentTime - TimeUnit.DAYS.toMillis(FourDay)).getDate(),
//            try {
//                this.getValues((currentTime - TimeUnit.DAYS.toMillis(FourDay)))
//            } catch (e: Exception) {
//                EmptyChartData
//            }
//        ),
//        Triple(
//            null,
//            (currentTime - TimeUnit.DAYS.toMillis(ThreeDay)).getDate(),
//            try {
//                this.getValues((currentTime - TimeUnit.DAYS.toMillis(ThreeDay)))
//            } catch (e: Exception) {
//                EmptyChartData
//            }
//        ),
//        Triple(
//            null,
//            (currentTime - TimeUnit.DAYS.toMillis(TwoDay)).getDate(),
//            try {
//                this.getValues((currentTime - TimeUnit.DAYS.toMillis(TwoDay)))
//            } catch (e: Exception) {
//                EmptyChartData
//            }
//        ),
//        Triple(
//            null,
//            (currentTime - TimeUnit.DAYS.toMillis(OneDay)).getDate(),
//            try {
//                this.getValues((currentTime - TimeUnit.DAYS.toMillis(OneDay)))
//            } catch (e: Exception) {
//                EmptyChartData
//            }
//        ),
//        Triple(
//            null,
//            "",
//            try {
//                this.getValues((currentTime))
//            } catch (e: Exception) {
//                EmptyChartData
//            }
//        )
//    )
//}

internal fun ArrayList<HashMap<String, Int?>>.toChartData1(): List<Triple<Drawable?, String, Float>> {
    val currentTime = System.currentTimeMillis()

    return listOf(
        Triple(
            null,
            (currentTime - TimeUnit.DAYS.toMillis(SixDay)).getDate(),
            try {
                this.getValues((currentTime - TimeUnit.DAYS.toMillis(SixDay)))
            } catch (e: Exception) {
                EmptyChartData
            }
        ),
        Triple(
            null,
            (currentTime - TimeUnit.DAYS.toMillis(FiveDay)).getDate(),
            try {
                this.getValues((currentTime - TimeUnit.DAYS.toMillis(FiveDay)))
            } catch (e: Exception) {
                EmptyChartData
            }
        ),
        Triple(
            null,
            (currentTime - TimeUnit.DAYS.toMillis(FourDay)).getDate(),
            try {
                this.getValues((currentTime - TimeUnit.DAYS.toMillis(FourDay)))
            } catch (e: Exception) {
                EmptyChartData
            }
        ),
        Triple(
            null,
            (currentTime - TimeUnit.DAYS.toMillis(ThreeDay)).getDate(),
            try {
                this.getValues((currentTime - TimeUnit.DAYS.toMillis(ThreeDay)))
            } catch (e: Exception) {
                EmptyChartData
            }
        ),
        Triple(
            null,
            (currentTime - TimeUnit.DAYS.toMillis(TwoDay)).getDate(),
            try {
                this.getValues((currentTime - TimeUnit.DAYS.toMillis(TwoDay)))
            } catch (e: Exception) {
                EmptyChartData
            }
        ),
        Triple(
            null,
            (currentTime - TimeUnit.DAYS.toMillis(OneDay)).getDate(),
            try {
                this.getValues((currentTime - TimeUnit.DAYS.toMillis(OneDay)))
            } catch (e: Exception) {
                EmptyChartData
            }
        ),
        Triple(
            null,
            "",
            try {
                this.getValues((currentTime))
            } catch (e: Exception) {
                EmptyChartData
            }
        )
    )
}

data class LineChartValue(val value: Array<Value>)
data class Value(val date: String, val score: List<String?>)

private const val EmptyChartData = -1f
private const val OneDay = 1L
private const val TwoDay = 2L
private const val ThreeDay = 3L
private const val FourDay = 4L
private const val FiveDay = 5L
private const val SixDay = 6L
