package com.vision.exercise.vision.ui.home.explore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.vision.exercise.databinding.ItemFoodBinding
import com.vision.exercise.vision.model.Food

class ListFoodAdapter(private val listFood: List<Food>): Adapter<ListFoodAdapter.ListFoodViewHolder>() {
    inner class ListFoodViewHolder(private val binding: ItemFoodBinding): ViewHolder(binding.root) {
        fun bind(food: Food) {
            binding.apply {
                tvName.text = food.name
                tvInfo.text = food.info
                Glide.with(root).load(food.icon).into(imgFood)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListFoodViewHolder {
        return ListFoodViewHolder(
            ItemFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount() = listFood.size

    override fun onBindViewHolder(holder: ListFoodViewHolder, position: Int) {
        holder.bind(listFood[position])
    }
}
