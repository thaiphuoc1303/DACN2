package com.vision.exercise.vision.ui.home.explore

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.vision.exercise.R
import com.vision.exercise.databinding.FragmentExploreBinding
import com.vision.exercise.vision.base.BaseFragment
import com.vision.exercise.vision.model.Food
import com.vision.exercise.vision.ui.home.HomeViewModel

class ExploreFragment : BaseFragment<FragmentExploreBinding, HomeViewModel>() {
    private lateinit var mAdapter: ListFoodAdapter
    private val listFood = arrayListOf(
        Food("Coffee", R.drawable.food_coffee, "Today | 6 am"),
        Food("Salmon Nigiri", R.drawable.food_salmon_nigiri, "Today | 7 am"),
        Food("Lowfat Milk", R.drawable.food_lowfat_milk, "Today | 8 am"),
        Food("Salad", R.drawable.food_salad, "Today | 11 am"),
        Food("Honey Pancake", R.drawable.food_cake, "Today | 7 am"),
        Food("Pie", R.drawable.food_pie, "Today | 7 pm"),
        Food("Beef steak", R.drawable.food_beef_steak, "Today | 7 pm"),
    )

    companion object {
        fun newInstance() = ExploreFragment()
    }

    override fun getLazyBinding() = lazy { FragmentExploreBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = activityViewModels<HomeViewModel>()

    override fun setupInit() {
        initView()
    }

    private fun initView() {
        mAdapter = ListFoodAdapter(listFood)
        binding.rcFood.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(
                context,
                LinearLayoutManager.VERTICAL, false
            )
        }
    }
}
