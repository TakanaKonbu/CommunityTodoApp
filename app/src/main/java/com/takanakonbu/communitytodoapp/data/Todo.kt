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
    val dueDate: LocalDateTime?, // 追加：完了予定日（null許容）
    val isDontWantToDo: Boolean = false
)