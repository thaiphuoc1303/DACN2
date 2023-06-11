package com.vision.exercise.vision.ui.pose

import android.util.Log
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vision.exercise.vision.base.BaseInput
import com.vision.exercise.vision.base.BaseViewModel
import com.vision.exercise.vision.demo.kotlin.posedetector.classification.PUSHUP_DOWN_POSE
import com.vision.exercise.vision.demo.kotlin.posedetector.classification.PUSHUP_UP_POSE
import com.vision.exercise.vision.model.Exercise
import com.vision.exercise.vision.model.ExerciseExt
import java.util.concurrent.ExecutionException

class CameraXLiveViewModel(private val input: BaseInput.ExerciseDetailInput): BaseViewModel(input) {
    private val cameraProviderLiveData = MutableLiveData<ProcessCameraProvider>()

    private val _count  = MutableLiveData(0)
    private var count = 0
    private var pose = ""
    private val _exercise = MutableLiveData<ExerciseExt>()

    init {
        input.exercise?.let {
            _exercise.value = it
        }
    }

    fun getExercise() = _exercise

    fun setPose(newPose: String) {
        if (newPose.isNotEmpty() && newPose != pose) {
            if (pose == PUSHUP_DOWN_POSE && newPose == PUSHUP_UP_POSE) {
                _count.value = count++
            }
            this.pose = newPose
        }
    }

    fun getCount() = _count

    fun getProcessCameraProvider(): LiveData<ProcessCameraProvider> {
        val cameraProviderFuture =
            ProcessCameraProvider.getInstance(input.application)
        cameraProviderFuture.addListener(
            {
                try {
                    cameraProviderLiveData.setValue(cameraProviderFuture.get())
                } catch (e: ExecutionException) {
                    // Handle any errors (including cancellation) here.
                    Log.e(
                        "VIEW MODEL ERROR",
                        "Unhandled exception",
                        e
                    )
                } catch (e: InterruptedException) {
                    Log.e(
                        "VIEW MODEL ERROR",
                        "Unhandled exception",
                        e
                    )
                }
            },
            ContextCompat.getMainExecutor(input.application)
        )
        return cameraProviderLiveData
    }
}