package com.vision.exercise.vision.ui.splash

import com.vision.exercise.vision.base.BaseInput
import com.vision.exercise.vision.base.BaseViewModel
import com.vision.exercise.vision.extension.getBaseConfig
import com.vision.exercise.vision.extension.observeOnIOThread
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.TimeUnit

class SplashViewModel(private val input: BaseInput.NormalInput): BaseViewModel(input) {
    val isFinish = MutableStateFlow(false)
    val isShowOnBoarding = input.application.getBaseConfig().showOnBoardingActivity
    init {
        addDisposables(
            Observable.timer(3, TimeUnit.SECONDS)
            .observeOnIOThread()
            .subscribe {
                isFinish.value = true
            }
        )
    }
}