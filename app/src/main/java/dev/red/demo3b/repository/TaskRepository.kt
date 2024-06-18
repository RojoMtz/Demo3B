package dev.red.demo3b.repository

import dev.red.demo3b.database.TaskDatabase
import dev.red.demo3b.model.Task

class TaskRepository(private val db: TaskDatabase) {
    suspend fun insertTask(task: Task) = db.getTaskDao().insertTask(task)
    suspend fun updateTask(task: Task) = db.getTaskDao().updateTask(task)
    suspend fun deleteTask(task: Task) = db.getTaskDao().deleteTask(task)

    fun getAllTasks() = db.getTaskDao().getAllTasks()
    fun searchTask(query:String?) = db.getTaskDao().searchTask(query)
}