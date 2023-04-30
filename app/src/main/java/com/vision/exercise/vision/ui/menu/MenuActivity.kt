package com.vision.exercise.vision.ui.menu

import androidx.activity.viewModels
import com.vision.exercise.vision.base.BaseActivity
import com.vision.exercise.vision.base.BaseInput
import com.vision.exercise.vision.base.ViewModelProviderFactory
import com.vision.exercise.databinding.ActivityMenuBinding

class MenuActivity: BaseActivity<ActivityMenuBinding, MenuViewModel>() {
    override fun getLazyBinding() = lazy { ActivityMenuBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = viewModels<MenuViewModel> {
        ViewModelProviderFactory(BaseInput.NormalInput(application))
    }

    override fun setupInit() {
        initListener()
    }

    private fun initListener() {
        binding.itemTest1.setOnClickListener {
            // TODO
        }
    }
}