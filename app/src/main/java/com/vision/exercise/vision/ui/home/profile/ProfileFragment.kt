package com.vision.exercise.vision.ui.home.profile

import androidx.fragment.app.activityViewModels
import com.vision.exercise.vision.base.BaseFragment
import com.vision.exercise.vision.ui.home.HomeViewModel
import com.vision.exercise.databinding.FragmentProfileBinding

class ProfileFragment: BaseFragment<FragmentProfileBinding, HomeViewModel>() {
    companion object{
        fun newInstance() = ProfileFragment()
    }

    override fun getLazyBinding() = lazy { FragmentProfileBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = activityViewModels<HomeViewModel>()

    override fun setupInit() {
//        TODO("Not yet implemented")
    }
}