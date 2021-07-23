package com.example.excursionstest.ui.step_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.excursionstest.databinding.StepBinding
import com.example.excursionstest.domain.entities.Step

class StepListAdapter : ListAdapter<Step, StepListAdapter.ViewHolder>(StepDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind("${position+1}/${itemCount}", getItem(position))
    }

    class ViewHolder private constructor(private val binding: StepBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(position: String, step: Step){
            binding.stepNumber.text = position.toString()
            binding.stepName.text = step.name
        }

        companion object{

            fun from(parent: ViewGroup) : ViewHolder{

                val layoutInflater = LayoutInflater.from(parent.context)

                return ViewHolder(StepBinding.inflate(layoutInflater, parent, false))
            }
        }
    }

    class StepDiffCallback : DiffUtil.ItemCallback<Step>(){
        override fun areItemsTheSame(oldItem: Step, newItem: Step): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Step, newItem: Step): Boolean {
            return oldItem == newItem
        }
    }
}