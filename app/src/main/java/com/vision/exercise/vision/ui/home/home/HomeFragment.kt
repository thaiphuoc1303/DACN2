package com.vision.exercise.vision.ui.home.home

import android.content.Intent
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.vision.exercise.vision.base.BaseFragment
import com.vision.exercise.vision.model.Exercise
import com.vision.exercise.vision.ui.camera.TestCameraXActivity
import com.vision.exercise.vision.ui.home.HomeViewModel
import com.vision.exercise.R
import com.vision.exercise.databinding.FragmentHomeBinding
import com.vision.exercise.vision.common.EXERCISE_KEY
import com.vision.exercise.vision.common.LIST_EXERCISE_KEY
import com.vision.exercise.vision.ui.exercise_details.ExerciseDetailActivity
import com.vision.exercise.vision.ui.pose.CameraXLivePreviewActivity
import com.vision.exercise.vision.ui.target.TodayTargetActivity

class HomeFragment: BaseFragment<FragmentHomeBinding, HomeViewModel>() {
    private lateinit var mAdapter: ListExerciseAdapter
    private val listExercise = arrayListOf<Exercise>()

    companion object{
        fun newInstance() = HomeFragment()
    }

    override fun getLazyBinding() = lazy { FragmentHomeBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = activityViewModels<HomeViewModel>()

    override fun setupInit() {
        initView()
        observers()
        initListener()
    }

    private fun initListener() {
        binding.apply {
            btnCheckTodayTarget.setOnClickListener {
                context?.let {
                    Intent(it, TodayTargetActivity::class.java).apply {
                        putExtra(LIST_EXERCISE_KEY, listExercise)
                        startActivity(this)
                    }
                }
            }
        }
    }

    private fun initView() {
        binding.apply {
            rcExercise.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            mAdapter = ListExerciseAdapter(listExercise) {
                startDetailExercise(it)
            }
            rcExercise.adapter = mAdapter
        }
    }

    private fun startDetailExercise(exercise: Exercise) {
        Intent(context, ExerciseDetailActivity::class.java).apply {
            putExtra(EXERCISE_KEY, exercise.convertToExt())
            startActivity(this)
        }
    }

    private fun observers() {
        viewModel.getExercises().observe(this) {
            listExercise.clear()
            listExercise.addAll(it)
            mAdapter.notifyDataSetChanged()
        }
    }
}
