package com.vision.exercise.vision.ui.home.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vision.exercise.vision.model.Exercise
import com.vision.exercise.databinding.ItemExerciseBinding

class ListExerciseAdapter(
    private val listExercise: List<Exercise>,
    private val itemClickCallback: (item: Exercise) -> Unit
) : RecyclerView.Adapter<ListExerciseAdapter.ListExerciseHolder>() {

    inner class ListExerciseHolder(private val binding: ItemExerciseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Exercise) {
            binding.apply {
                tvExerciseName.text = item.name
                tvExerciseContent.text = item.description
                ivThumbnail.setImageResource(item.thumbnail)
                btnViewMore.setOnClickListener {
                    itemClickCallback.invoke(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListExerciseHolder {
        return ListExerciseHolder(
            ItemExerciseBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = listExercise.size

    override fun onBindViewHolder(holder: ListExerciseHolder, position: Int) {
        holder.bind(listExercise[position])
    }
}