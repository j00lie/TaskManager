package com.example.taskmanager

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmanager.databinding.ActivityMainBinding
import java.util.Date

class MainActivity : AppCompatActivity() {

    // variables for the binding object, adapter and launcher
    private lateinit var binding: ActivityMainBinding
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var editTaskLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Create task adapter and initialise with empty list for tasks
        taskAdapter = TaskAdapter(mutableListOf())
        // Use task adapter to display items in the recycler view
        binding.rvTaskItems.adapter = taskAdapter
        // Use normal vertical list
        binding.rvTaskItems.layoutManager = LinearLayoutManager(this)

        // Add a task, check that title is entered
        binding.btnAddTask.setOnClickListener {
            val taskTitle = binding.etTaskTitle.text.toString()
            if (taskTitle.isNotEmpty()){
                val task = Task(taskTitle)
                taskAdapter.addTask(task)
                binding.etTaskTitle.text.clear()
            }
        }

        // Delete a task
        binding.btnDeleteTask.setOnClickListener{
            taskAdapter.deleteTask()
        }
        // Initialize the ActivityResultLauncher
        editTaskLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val position = result.data?.getIntExtra("task_position", -1)
                val updatedDeadline = result.data?.getSerializableExtra("task_deadline") as? Date

                if (position != null && position != -1 && updatedDeadline != null) {
                    taskAdapter.updateTaskDeadline(position, updatedDeadline)
                }
            }
        }
        enableEdgeToEdge()

        // Apply window insets using the binding's root view
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


    }
    // function to launch TaskDetailActivity with a result
    fun openTaskDetail(task: Task, position: Int) {
        val intent = Intent(this, TaskDetailActivity::class.java).apply {
            putExtra("task_title", task.title)
            putExtra("task_status", task.isChecked)
            putExtra("task_deadline", task.deadline)
            putExtra("task_position", position)
        }
        editTaskLauncher.launch(intent)
    }
}
