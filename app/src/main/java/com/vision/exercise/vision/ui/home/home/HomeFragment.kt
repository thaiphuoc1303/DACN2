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

class HomeFragment: BaseFragment<FragmentHomeBinding, HomeViewModel>() {
    private lateinit var mAdapter: ListExerciseAdapter
    private val listExercise = arrayListOf<Exercise>(
        Exercise("Fullbody Workout", "11 Exercises | 32 mins", R.drawable.ic_skipping),
        Exercise("Lowebody Workout", "12 Exercises | 40 mins", R.drawable.ic_lowebody_workout),
        Exercise("AB Workout", "14 Exercises | 20 mins", R.drawable.ic_ab_workout)
    )

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
                Intent(context, TestCameraXActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }
    }

    private fun initView() {
        binding.apply {
            rcExercise.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            mAdapter = ListExerciseAdapter(listExercise) {
                // TODO
            }
            rcExercise.adapter = mAdapter
        }
    }

    private fun observers() {
//        viewModel.getExercises().observe(this) {
//            listExercise.clear()
//            listExercise.addAll(it)
//            mAdapter.notifyDataSetChanged()
//        }
    }
}