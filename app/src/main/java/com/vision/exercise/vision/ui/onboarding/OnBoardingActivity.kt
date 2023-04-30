package com.vision.exercise.vision.ui.onboarding

import androidx.activity.viewModels
import com.vision.exercise.vision.base.BaseActivity
import com.vision.exercise.vision.base.BaseInput
import com.vision.exercise.vision.base.ViewModelProviderFactory
import com.vision.exercise.databinding.ActivityOnBoardingBinding
import com.vision.exercise.vision.extension.getBaseConfig
import com.vision.exercise.vision.extension.showToast

class OnBoardingActivity: BaseActivity<ActivityOnBoardingBinding, OnBoardingViewModel>() {
    private lateinit var viewPagerAdapter : OnBoardingViewPagerAdapter

    override fun getLazyBinding() = lazy { ActivityOnBoardingBinding.inflate(layoutInflater)}

    override fun getLazyViewModel() = viewModels<OnBoardingViewModel>{
        ViewModelProviderFactory(BaseInput.NormalInput(application))
    }

    override fun setupInit() {
        binding.apply {
            viewpage.apply {
                viewPagerAdapter = OnBoardingViewPagerAdapter(this@OnBoardingActivity)
                adapter = viewPagerAdapter
            }
            wormDotsIndicator.attachTo(viewpage)

            btnNext.setOnClickListener {
                if (viewpage.currentItem == OnBoardingViewPagerAdapter.MAX_ITEM - 1) {
                    toConfigActivity()
                }
                else {
                    viewpage.currentItem = viewpage.currentItem + 1
                }
            }
        }
    }

    private fun toConfigActivity() {
//        TODO("Not yet implemented")
        showToast("To config activity")
        getBaseConfig().showOnBoardingActivity = false
    }
}