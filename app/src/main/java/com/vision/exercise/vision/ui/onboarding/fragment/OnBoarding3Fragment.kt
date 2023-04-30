package com.vision.exercise.vision.ui.onboarding.fragment

import androidx.fragment.app.activityViewModels
import com.vision.exercise.vision.ui.onboarding.OnBoardingViewModel
import com.vision.exercise.databinding.FragmentOnBoarding3Binding
import com.vision.exercise.vision.base.BaseFragment

class OnBoarding3Fragment : BaseFragment<FragmentOnBoarding3Binding, OnBoardingViewModel>() {
    companion object {
        fun newInstance() = OnBoarding3Fragment()
    }

    override fun getLazyBinding() = lazy { FragmentOnBoarding3Binding.inflate(layoutInflater) }

    override fun getLazyViewModel() = activityViewModels<OnBoardingViewModel>()

    override fun setupInit() {
//        TODO("Not yet implemented")
    }
}