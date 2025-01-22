package com.takanakonbu.communitytodoapp.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.takanakonbu.communitytodoapp.TodoApplication
import com.takanakonbu.communitytodoapp.data.Todo
import com.takanakonbu.communitytodoapp.data.TodoDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class TodoViewModel(application: Application) : AndroidViewModel(application) {
    private val todoDao: TodoDao = (application as TodoApplication).database.todoDao()
    val allTodos: Flow<List<Todo>> = todoDao.getAllTodos()

    fun addTodo(title: String, content: String, isDontWantToDo: Boolean) {
        val todo = Todo(
            title = title,
            content = content,
            updatedAt = LocalDateTime.now(),
            isDontWantToDo = isDontWantToDo
        )
        viewModelScope.launch {
            try {
                todoDao.insertTodo(todo)
                Log.d("TodoViewModel", "Todo saved successfully: $todo")
            } catch (e: Exception) {
                Log.e("TodoViewModel", "Error saving todo", e)
            }
        }
    }

    fun updateTodo(todo: Todo) {
        val updatedTodo = todo.copy(updatedAt = LocalDateTime.now())
        viewModelScope.launch {
            todoDao.updateTodo(updatedTodo)
        }
    }

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch {
            todoDao.deleteTodo(todo)
        }
    }
}