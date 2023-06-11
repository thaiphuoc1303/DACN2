package com.vision.exercise.vision.ui.home

import android.util.Log
import androidx.activity.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.vision.exercise.vision.base.BaseInput
import com.vision.exercise.vision.base.BasePermissionActivity
import com.vision.exercise.vision.base.ViewModelProviderFactory
import com.vision.exercise.R
import com.vision.exercise.databinding.ActivityHomeBinding

class HomeActivity: BasePermissionActivity<ActivityHomeBinding, HomeViewModel>() {
    private lateinit var viewPagerAdapter: HomeViewPagerAdapter

    override fun getLazyBinding() = lazy { ActivityHomeBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = viewModels<HomeViewModel> {
        ViewModelProviderFactory(BaseInput.NormalInput(application))
    }

    override fun setupInit() {
        initView()
        observer()
        checkPermission()
    }

    private fun observer() {
        viewModel.getExercises().observe(this) {

        }
    }

    private fun initView() {
        binding.bottomBar.itemIconTintList = null
        binding.apply {
            viewpager.apply {
                viewPagerAdapter = HomeViewPagerAdapter(this@HomeActivity)
                adapter = viewPagerAdapter
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        when(position) {
                            0-> bottomBar.selectedItemId = R.id.item_home
                            1 -> bottomBar.selectedItemId = R.id.item_explore
                            2 -> bottomBar.selectedItemId = R.id.item_statistical
                            3 -> bottomBar.selectedItemId = R.id.item_profile
                        }
                    }
                })
            }
            bottomBar.setOnItemSelectedListener {
                when(it.itemId) {
                    R.id.item_home -> viewpager.currentItem = 0
                    R.id.item_explore -> viewpager.currentItem = 1
                    R.id.item_statistical -> viewpager.currentItem = 2
                    R.id.item_profile -> viewpager.currentItem = 3
                }
                true
            }
        }
    }
}