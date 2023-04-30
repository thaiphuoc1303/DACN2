package com.vision.exercise.vision.ui.onboarding.fragment

import androidx.fragment.app.activityViewModels
import com.vision.exercise.vision.ui.onboarding.OnBoardingViewModel
import com.vision.exercise.databinding.FragmentOnBoarding2Binding
import com.vision.exercise.vision.base.BaseFragment

class OnBoarding2Fragment : BaseFragment<FragmentOnBoarding2Binding, OnBoardingViewModel>() {
    companion object {
        fun newInstance() = OnBoarding2Fragment()
    }

    override fun getLazyBinding() = lazy { FragmentOnBoarding2Binding.inflate(layoutInflater) }

    override fun getLazyViewModel() = activityViewModels<OnBoardingViewModel>()

    override fun setupInit() {
//        TODO("Not yet implemented")
    }
}