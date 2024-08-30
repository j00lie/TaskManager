package com.example.taskmanager

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.taskmanager.databinding.ActivityTaskDetailBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TaskDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskDetailBinding

    private var selectedDeadline: Date? = null
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private var taskPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate the layout using View Binding
        binding = ActivityTaskDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the task details from the intent
        val taskTitle = intent.getStringExtra("task_title")
        val taskStatus = intent.getBooleanExtra("task_status", false)
        selectedDeadline = intent.getSerializableExtra("task_deadline") as? Date
        taskPosition = intent.getIntExtra("task_position", -1)

        // Set the initial task details in the view
        binding.tvTaskDetailTitle.text = taskTitle
        binding.tvTaskDetailStatus.text = if (taskStatus) "Completed" else "Incomplete"
        selectedDeadline?.let {
            binding.tvDeadline.text = dateFormat.format(it)
        }

        // set the deadline selector button
        binding.btnSelectDeadline.setOnClickListener {
            showDatePickerDialog(binding.tvDeadline)
        }
        // Send deadline back to main activity and close current activity
        binding.btnSave.setOnClickListener {
            // Return the updated deadline to MainActivity
            val returnIntent = Intent().apply {
                putExtra("task_position", taskPosition)
                putExtra("task_deadline", selectedDeadline)
            }
            setResult(RESULT_OK, returnIntent)
            finish()
        }
    }
    // Open calendar and choose deadline
    private fun showDatePickerDialog(tvDeadline: TextView) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            calendar.set(selectedYear, selectedMonth, selectedDay)
            selectedDeadline = calendar.time
            tvDeadline.text = dateFormat.format(selectedDeadline)

            // Pass the selected deadline back to MainActivity
            intent.putExtra("task_deadline", selectedDeadline)
        }, year, month, day)

        datePickerDialog.show()
    }
}
