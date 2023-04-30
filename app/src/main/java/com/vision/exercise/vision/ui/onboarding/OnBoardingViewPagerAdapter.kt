package com.vision.exercise.vision.ui.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.vision.exercise.vision.ui.onboarding.fragment.OnBoarding1Fragment
import com.vision.exercise.vision.ui.onboarding.fragment.OnBoarding2Fragment
import com.vision.exercise.vision.ui.onboarding.fragment.OnBoarding3Fragment
import com.vision.exercise.vision.ui.onboarding.fragment.OnBoarding4Fragment

class OnBoardingViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    companion object {
        const val MAX_ITEM = 4
    }

    override fun getItemCount() = MAX_ITEM

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OnBoarding1Fragment.newInstance()
            1 -> OnBoarding2Fragment.newInstance()
            2 -> OnBoarding3Fragment.newInstance()
            else -> OnBoarding4Fragment.newInstance()
        }
    }
}