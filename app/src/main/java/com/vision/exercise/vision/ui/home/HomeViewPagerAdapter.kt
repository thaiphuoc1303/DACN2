package com.vision.exercise.vision.ui.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.vision.exercise.vision.ui.home.explore.ExploreFragment
import com.vision.exercise.vision.ui.home.home.HomeFragment
import com.vision.exercise.vision.ui.home.profile.ProfileFragment
import com.vision.exercise.vision.ui.home.statistical.StatisticalFragment

class HomeViewPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount() = 4

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> HomeFragment.newInstance()
            1 -> ExploreFragment.newInstance()
            2 -> StatisticalFragment.newInstance()
            else -> ProfileFragment.newInstance()
        }
    }
}