package com.vision.exercise.vision.ui.splash

import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.asLiveData
import com.vision.exercise.vision.base.BaseActivity
import com.vision.exercise.vision.base.BaseInput
import com.vision.exercise.vision.base.ViewModelProviderFactory
import com.vision.exercise.databinding.ActivitySplashBinding
import com.vision.exercise.vision.ui.home.HomeActivity
import com.vision.exercise.vision.ui.onboarding.OnBoardingActivity

class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>() {
    override fun getLazyBinding() = lazy { ActivitySplashBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = viewModels<SplashViewModel> {
        ViewModelProviderFactory(BaseInput.NormalInput(application))
    }

    override fun setupInit() {
        observers()
    }

    private fun observers() {
        viewModel.isFinish.asLiveData().observe(this) {
            if (it) {
                if (viewModel.isShowOnBoarding) {
                    toOnBoarding()
                }
                else {
                    toHomeScreen()
                }
            }
        }
    }

    private fun toHomeScreen() {
        Intent(this, HomeActivity::class.java).apply {
            startActivity(this)
            finish()
        }
    }

    private fun toOnBoarding() {
        Intent(this, OnBoardingActivity::class.java).apply {
            startActivity(this)
            finish()
        }
    }
}