package com.vision.exercise.vision.ui.target

import androidx.activity.viewModels
import com.vision.exercise.databinding.ActivityTodayTargetBinding
import com.vision.exercise.vision.base.BaseActivity
import com.vision.exercise.vision.base.BaseInput
import com.vision.exercise.vision.base.ViewModelProviderFactory
import com.vision.exercise.vision.common.LIST_EXERCISE_KEY
import com.vision.exercise.vision.extension.parcelableArrayListExtra

class TodayTargetActivity: BaseActivity<ActivityTodayTargetBinding, TodayTargetViewModel>() {
    override fun getLazyBinding() = lazy { ActivityTodayTargetBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = viewModels<TodayTargetViewModel> {
        ViewModelProviderFactory(BaseInput.TodayTargetInput(application, intent.parcelableArrayListExtra(LIST_EXERCISE_KEY)))
    }

    override fun setupInit() {

    }
}