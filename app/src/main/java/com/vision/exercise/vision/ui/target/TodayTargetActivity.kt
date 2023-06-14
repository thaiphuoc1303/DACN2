package com.vision.exercise.vision.ui.target

import android.content.Intent
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.vision.exercise.databinding.ActivityTodayTargetBinding
import com.vision.exercise.vision.base.BaseActivity
import com.vision.exercise.vision.base.BaseInput
import com.vision.exercise.vision.base.ViewModelProviderFactory
import com.vision.exercise.vision.common.EXERCISE_KEY
import com.vision.exercise.vision.common.LIST_EXERCISE_KEY
import com.vision.exercise.vision.common.REPEAT_KEY
import com.vision.exercise.vision.extension.onClick
import com.vision.exercise.vision.extension.parcelableArrayListExtra
import com.vision.exercise.vision.model.Practice
import com.vision.exercise.vision.ui.exercise.ExerciseActivity
import com.vision.exercise.vision.util.DrawableUtils
import com.vision.exercise.vision.util.TimeUtils
import com.vision.exercise.vision.util.TimeUtils.getTimeFromSecond

class TodayTargetActivity: BaseActivity<ActivityTodayTargetBinding, TodayTargetViewModel>() {
    private lateinit var practice: Practice

    override fun getLazyBinding() = lazy { ActivityTodayTargetBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = viewModels<TodayTargetViewModel> {
        ViewModelProviderFactory(BaseInput.TodayTargetInput(application, intent.parcelableArrayListExtra(LIST_EXERCISE_KEY)))
    }

    override fun setupInit() {
        initListener()
        observe()
    }

    private fun initListener() {
        binding.apply {
            btnReturn.onClick {
                onBackPressed()
            }
            
            btnBack.onClick {
                onBackPressed()
            }

            btnContinueTraining.onClick {
                viewModel.getListExercise().firstOrNull { it.name == practice.name }?.let {
                    Intent(this@TodayTargetActivity, ExerciseActivity::class.java).apply {
                        val repeat = practice.repeat + 2
                        putExtra(EXERCISE_KEY, it)
                        putExtra(REPEAT_KEY, repeat)
                        startActivity(this)
                    }
                }

            }
        }
    }

    private fun observe() {
        viewModel.getLastPractice().observe(this) {
            it?.let {
                setView(it)
                binding.llEmpty.isVisible = false
                binding.llPractice.isVisible = true
                practice = it
            }?: kotlin.run {
                binding.llEmpty.isVisible = true
                binding.llPractice.isVisible = false
            }
        }
    }

    private fun setView(practice: Practice) {
        binding.itemPractice.apply {
            tvCalo.text = practice.calo.toString()
            tvName.text = practice.name
            tvTime.text = getTimeFromSecond(practice.time)
            Glide.with(binding.root)
                .load(DrawableUtils.getType(practice.name))
                .into(imgType)
            tvDate.text = TimeUtils.getPrettyTime(practice.timeStamp)
        }
    }
}