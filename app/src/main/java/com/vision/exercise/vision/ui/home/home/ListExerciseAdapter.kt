package com.vision.exercise.vision.ui.home.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vision.exercise.vision.model.Exercise
import com.vision.exercise.databinding.ItemExerciseBinding
import com.vision.exercise.vision.util.DrawableUtils.getType

class ListExerciseAdapter(
    private val listExercise: List<Exercise>,
    private val itemClickCallback: (item: Exercise) -> Unit
) : RecyclerView.Adapter<ListExerciseAdapter.ListExerciseHolder>() {

    inner class ListExerciseHolder(private val binding: ItemExerciseBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Exercise) {
            binding.apply {
                tvExerciseName.text = item.name
                val content = "${item.category} | ${item.calo} calo"
                tvExerciseContent.text = content
                // TODO:
                ivThumbnail.setImageResource(getType(item.name))
                root.setOnClickListener {
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