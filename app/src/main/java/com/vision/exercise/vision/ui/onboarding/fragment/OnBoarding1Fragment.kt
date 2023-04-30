package com.vision.exercise.vision.ui.onboarding.fragment

import androidx.fragment.app.activityViewModels
import com.vision.exercise.vision.base.BaseFragment
import com.vision.exercise.vision.ui.onboarding.OnBoardingViewModel
import com.vision.exercise.databinding.FragmentOnBoarding1Binding

class OnBoarding1Fragment: BaseFragment<FragmentOnBoarding1Binding, OnBoardingViewModel>() {
    companion object {
        fun newInstance() = OnBoarding1Fragment()
    }
    override fun getLazyBinding() = lazy { FragmentOnBoarding1Binding.inflate(layoutInflater) }

    override fun getLazyViewModel() = activityViewModels<OnBoardingViewModel>()

    override fun setupInit() {
//        TODO("Not yet implemented")
    }
}