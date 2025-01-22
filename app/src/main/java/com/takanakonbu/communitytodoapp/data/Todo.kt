package com.takanakonbu.communitytodoapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "todos")
data class Todo(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val content: String,
    val updatedAt: LocalDateTime,
    val isDontWantToDo: Boolean = false  // 追加
)