package com.takanakonbu.communitytodoapp

import android.app.Application
import com.takanakonbu.communitytodoapp.data.TodoDatabase

class TodoApplication : Application() {
    val database: TodoDatabase by lazy { TodoDatabase.getDatabase(this) }
}