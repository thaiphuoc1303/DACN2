package com.vision.exercise.vision.ui.onboarding.fragment

import androidx.fragment.app.activityViewModels
import com.vision.exercise.vision.ui.onboarding.OnBoardingViewModel
import com.vision.exercise.databinding.FragmentOnBoarding4Binding
import com.vision.exercise.vision.base.BaseFragment

class OnBoarding4Fragment : BaseFragment<FragmentOnBoarding4Binding, OnBoardingViewModel>() {
    companion object {
        fun newInstance() = OnBoarding4Fragment()
    }

    override fun getLazyBinding() = lazy { FragmentOnBoarding4Binding.inflate(layoutInflater) }

    override fun getLazyViewModel() = activityViewModels<OnBoardingViewModel>()

    override fun setupInit() {
//        TODO("Not yet implemented")
    }
}