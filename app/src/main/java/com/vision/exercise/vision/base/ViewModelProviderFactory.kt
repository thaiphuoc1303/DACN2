package com.vision.exercise.vision.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vision.exercise.vision.ui.camera.TestCameraXViewModel
import com.vision.exercise.vision.ui.exercise.ExerciseActivity
import com.vision.exercise.vision.ui.exercise.ExerciseViewModel
import com.vision.exercise.vision.ui.exercise_details.ExerciseDetailActivity
import com.vision.exercise.vision.ui.exercise_details.ExerciseDetailViewModel
import com.vision.exercise.vision.ui.home.HomeViewModel
import com.vision.exercise.vision.ui.splash.SplashViewModel
import com.vision.exercise.vision.ui.target.TodayTargetViewModel

class ViewModelProviderFactory(private val input: BaseInput) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                return SplashViewModel(input as BaseInput.NormalInput) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                return HomeViewModel(input as BaseInput.NormalInput) as T
            }
            modelClass.isAssignableFrom(TestCameraXViewModel::class.java) -> {
                return TestCameraXViewModel(input as BaseInput.NormalInput) as T
            }
            modelClass.isAssignableFrom(ExerciseViewModel::class.java) -> {
                return ExerciseViewModel(input as BaseInput.ExerciseInput) as T
            }
            modelClass.isAssignableFrom(ExerciseDetailViewModel::class.java) -> {
                return ExerciseDetailViewModel(input as BaseInput.ExerciseDetailInput) as T
            }
            modelClass.isAssignableFrom(TodayTargetViewModel::class.java) -> {
                return TodayTargetViewModel(input as BaseInput.TodayTargetInput) as T
            }
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}