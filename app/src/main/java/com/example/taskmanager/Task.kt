package com.example.taskmanager

import java.util.Date

data class Task(
    val title: String,
    var isChecked: Boolean = false,
    var deadline: Date? = null
)