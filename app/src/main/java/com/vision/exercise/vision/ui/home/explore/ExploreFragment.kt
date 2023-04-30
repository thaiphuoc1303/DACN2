package com.vision.exercise.vision.ui.home.explore

import androidx.fragment.app.activityViewModels
import com.vision.exercise.vision.base.BaseFragment
import com.vision.exercise.vision.ui.home.HomeViewModel
import com.vision.exercise.databinding.FragmentExploreBinding

class ExploreFragment: BaseFragment<FragmentExploreBinding, HomeViewModel>() {
    companion object{
        fun newInstance() = ExploreFragment()
    }

    override fun getLazyBinding() = lazy { FragmentExploreBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = activityViewModels<HomeViewModel>()

    override fun setupInit() {
//        TODO("Not yet implemented")
    }
}