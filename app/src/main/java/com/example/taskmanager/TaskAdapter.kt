package com.example.taskmanager

import android.content.Intent
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.databinding.ItemTaskBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TaskAdapter(

    private val tasks: MutableList<Task>

) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    class TaskViewHolder(val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root)
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }


    // Add a task to the end of the list
    fun addTask(task: Task){
        tasks.add(task)
        notifyItemInserted(tasks.size-1)
    }

    //Delete checked tasks, i.e. task needs to be checked to remove it
    fun deleteTask(){
        tasks.removeAll { task: Task ->
            task.isChecked
        }
        notifyDataSetChanged()
    }

    fun updateTaskDeadline(position: Int, deadline: Date) {
        tasks[position].deadline = deadline
        notifyItemChanged(position)
    }

    private fun toggleStrikeThrough(tvTaskTitle: TextView, isChecked: Boolean){
        if (isChecked){
            tvTaskTitle.paintFlags = tvTaskTitle.paintFlags or STRIKE_THRU_TEXT_FLAG
        }else{
            tvTaskTitle.paintFlags = tvTaskTitle.paintFlags and STRIKE_THRU_TEXT_FLAG.inv()
        }
    }
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = tasks[position]
        holder.binding.tvTaskTitle.text = currentTask.title
        holder.binding.cbDone.isChecked = currentTask.isChecked

        // Display the deadline if set
        holder.binding.tvDeadline.text = currentTask.deadline?.let {
            dateFormat.format(it)
        } ?: "No deadline set"

        toggleStrikeThrough(holder.binding.tvTaskTitle, currentTask.isChecked)
        holder.binding.cbDone.setOnCheckedChangeListener { _, isChecked ->
            toggleStrikeThrough(holder.binding.tvTaskTitle, isChecked)
            currentTask.isChecked = !currentTask.isChecked
        }

        // Add click listener to navigate to TaskDetailActivity using the function in MainActivity
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            if (context is MainActivity) {
                context.openTaskDetail(currentTask, position)
            }
        }
    }
    // get amount of items in recycler view
    override fun getItemCount(): Int {
        return tasks.size
    }
}
