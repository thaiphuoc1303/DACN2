package com.vision.exercise.vision.ui.exercise

import android.util.Log
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vision.exercise.vision.base.BaseInput
import com.vision.exercise.vision.base.BaseViewModel
import com.vision.exercise.vision.database.AppDBHelper
import com.vision.exercise.vision.extension.observeOnUiThread
import com.vision.exercise.vision.model.ExerciseExt
import com.vision.exercise.vision.model.Practice
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.ExecutionException

class ExerciseViewModel(private val input: BaseInput.ExerciseInput): BaseViewModel(input) {

    private var dbHelper = AppDBHelper(input.application)

    private var cameraProviderLiveData = MutableLiveData<ProcessCameraProvider>()
    private val _exercise = MutableLiveData<ExerciseExt>()
    private val _count = MutableLiveData(0)
    private val _currentStep = MutableLiveData<ExerciseExt.StepExt>()
    private val _isPause = MutableLiveData(false)
    private val _isReady = MutableLiveData(false)
    private val _isFinish = MutableLiveData(false)
    private val repeat = input.repeat
    private lateinit var currentStep: ExerciseExt.StepExt
    private lateinit var exercise: ExerciseExt
    private var step = 0
    private var maxStep = 0

    init {
        input.exercise?.let {
            currentStep = it.steps.first()
            _exercise.value = it
            maxStep = it.steps.size
            exercise = it
        }
    }

    fun changePause() {
        _isPause.value = !isPause()
    }

    fun setIsReady(ready: Boolean) {
        _isReady.value = ready
    }

    fun setPause() {
        _isPause.value = true
    }

    fun setCount(value: Int) {
        _count.value = value
    }
    fun setCurrentStep(stepExt: ExerciseExt.StepExt) {
        _currentStep.value = stepExt
    }
    private val timer = Timer()
    private var seconds = 0L
    val elapsedTime = MutableLiveData<Long>()

    fun startTimer() {
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                if (isPause()) return
                seconds++
                elapsedTime.postValue(seconds)
            }
        }, 0, 1000)
    }

    fun getIsFinish() = _isFinish
    fun getIsReady() = _isReady
    fun getExercise() = _exercise
    fun getIsPause() = _isPause
    fun getCount() = _count

    fun isFinish() = _isFinish.value?: false
    fun isReady() = _isReady.value?: false
    fun isPause() = _isPause.value?: true
    fun getRepeat() = repeat
    fun count() = _count.value?: 0

    fun insertPractice() {
        dbHelper.insertPractice(Practice(exercise.name, exercise.type, seconds, repeat, System.currentTimeMillis(), exercise.calo * repeat))
            .observeOnUiThread()
            .subscribe()
    }

    fun checkPose(pose: String) {
        if (pose.contains(currentStep.pose) && !isPause()) {
            step++
            if (step == maxStep) {
                step = 0
                _count.value = count() + 1
                if (count() == repeat) {
                    _isFinish.value = true
                }
            }
            currentStep = exercise.steps[step]
        }
    }

    fun getProcessCameraProvider(): LiveData<ProcessCameraProvider> {
        cameraProviderLiveData = MutableLiveData()
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
