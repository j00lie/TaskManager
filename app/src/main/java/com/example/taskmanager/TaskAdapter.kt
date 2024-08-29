package com.example.taskmanager

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.databinding.ItemTaskBinding

class TaskAdapter(
    private val tasks: MutableList<Task>
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }
    private fun toggleStrikeThrough(tvTaskTitle: TextView, isChecked: Boolean){
        if (isChecked){
            tvTaskTitle.paintFlags
        }
    }
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = tasks[position]
        holder.binding.tvTaskTitle.text = currentTask.title
        holder.binding.cbDone.isChecked = currentTask.isChecked
    }

    override fun getItemCount(): Int {
        return tasks.size
    }
}
