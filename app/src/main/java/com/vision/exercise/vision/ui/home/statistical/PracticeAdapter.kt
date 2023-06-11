package com.vision.exercise.vision.ui.home.statistical

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.vision.exercise.R
import com.vision.exercise.databinding.ItemPracticeBinding
import com.vision.exercise.vision.model.Practice
import com.vision.exercise.vision.util.DateFormatUtils.formatDateTime
import com.vision.exercise.vision.util.DrawableUtils.getType
import com.vision.exercise.vision.util.TimeUtils.getPrettyTime
import com.vision.exercise.vision.util.TimeUtils.getTimeFromSecond

class PracticeAdapter(
    private val listPractice: ArrayList<Practice>
): RecyclerView.Adapter<PracticeAdapter.PracticeViewHolder>() {
    inner class PracticeViewHolder(val binding: ItemPracticeBinding): ViewHolder(binding.root) {
        fun bind(practice: Practice) {
            binding.apply {
                tvName.text = practice.name
                tvDate.text = getPrettyTime(practice.timeStamp)
                Glide.with(binding.root)
                    .load(getType(practice.name))
                    .into(imgType)
                tvRepeat.text = "${practice.repeat}"
                tvTime.text = getTimeFromSecond(practice.time)
                tvCalo.text = "${practice.calo}"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PracticeViewHolder {
        return PracticeViewHolder(
            ItemPracticeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount() = listPractice.size

    override fun onBindViewHolder(holder: PracticeViewHolder, position: Int) {
        holder.bind(listPractice[position])
    }

    fun setData(list: List<Practice>) {
        listPractice.clear()
        listPractice.addAll(list)
        notifyDataSetChanged()
    }
}
