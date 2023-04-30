package com.vision.exercise.vision.ui.home.statistical

import androidx.fragment.app.activityViewModels
import com.vision.exercise.vision.ui.home.HomeViewModel
import com.vision.exercise.vision.base.BaseFragment
import com.vision.exercise.databinding.FragmentStatisticalBinding

class StatisticalFragment: BaseFragment<FragmentStatisticalBinding, HomeViewModel>() {
    companion object{
        fun newInstance() = StatisticalFragment()
    }

    override fun getLazyBinding() = lazy { FragmentStatisticalBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = activityViewModels<HomeViewModel>()

    override fun setupInit() {
//        TODO("Not yet implemented")
    }
}