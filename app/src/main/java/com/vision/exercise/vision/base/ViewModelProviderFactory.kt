package com.vision.exercise.vision.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vision.exercise.vision.ui.home.HomeViewModel
import com.vision.exercise.vision.ui.splash.SplashViewModel

class ViewModelProviderFactory(private val input: BaseInput) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                return SplashViewModel(input as BaseInput.NormalInput) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                return HomeViewModel(input as BaseInput.NormalInput) as T
            }
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}