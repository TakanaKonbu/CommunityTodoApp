package com.takanakonbu.communitytodoapp.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.takanakonbu.communitytodoapp.data.Todo
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


@Composable
fun TodoListScreen(
    todos: List<Todo>,
    onAddClick: () -> Unit,
    onTodoClick: (Todo) -> Unit,
    onDeleteClick: (Todo) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, "Add Todo")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(todos) { todo ->
                TodoItem(
                    todo = todo,
                    onClick = { onTodoClick(todo) },
                    onDelete = { onDeleteClick(todo) }
                )
            }
        }
    }
}

@Composable
fun TodoItem(
    todo: Todo,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val daysUntilDue = todo.dueDate?.let { dueDate ->
        ChronoUnit.DAYS.between(
            LocalDate.now(),
            dueDate.toLocalDate()
        )
    }

    val isNearDue = daysUntilDue != null && daysUntilDue <= 4 && daysUntilDue >= 0

    val infiniteTransition = rememberInfiniteTransition(label = "neon")

    // メインのグロー効果
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    // パルス効果（より速い点滅）
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    // ネオンカラーの定義
    val neonColor = if (isNearDue) {
        val baseColor = Color(0xFFFF0066) // ネオンピンク
        val glowIntensity = (glowAlpha * 0.6f) + (pulseAlpha * 0.4f)
        baseColor.copy(alpha = 0.4f + (glowIntensity * 0.6f))
    } else {
        MaterialTheme.colorScheme.surface
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
            .then(
                if (isNearDue) {
                    Modifier.drawBehind {
                        // 外側のグロー効果
                        drawRect(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    neonColor.copy(alpha = 0.2f),
                                    neonColor.copy(alpha = 0.1f),
                                    Color.Transparent
                                ),
                                center = Offset(size.width / 2, size.height / 2),
                                radius = size.width
                            )
                        )
                    }
                } else Modifier
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (isNearDue) {
                neonColor.copy(alpha = 0.1f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        border = if (isNearDue) {
            BorderStroke(
                width = 2.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        neonColor.copy(alpha = glowAlpha),
                        neonColor.copy(alpha = pulseAlpha),
                        neonColor.copy(alpha = glowAlpha)
                    )
                )
            )
        } else null,
        elevation = if (isNearDue) {
            CardDefaults.cardElevation(
                defaultElevation = 8.dp * glowAlpha
            )
        } else CardDefaults.cardElevation()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = todo.title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    if (todo.isDontWantToDo) {
                        Text("😭")
                    }
                    // 期限が近い場合にバッジを表示
                    if (isNearDue) {
                        Surface(
                            color = neonColor.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(4.dp),
                            border = BorderStroke(
                                width = 1.dp,
                                color = neonColor.copy(alpha = 0.8f + (pulseAlpha * 0.2f))
                            ),
                            modifier = Modifier.padding(start = 4.dp)
                        ) {
                            if (daysUntilDue != null) {
                                Text(
                                    text = "期限まで${daysUntilDue + 1}日",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = neonColor.copy(alpha = 0.8f + (pulseAlpha * 0.2f)),
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
                Text(
                    text = todo.content,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = "更新: ${todo.updatedAt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"))}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    todo.dueDate?.let { dueDate ->
                        Text(
                            text = "期限: ${dueDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))}",
                            style = MaterialTheme.typography.bodySmall,
                            color = when {
                                dueDate.isBefore(LocalDateTime.now()) -> MaterialTheme.colorScheme.error
                                isNearDue -> neonColor
                                else -> MaterialTheme.typography.bodySmall.color
                            }
                        )
                    }
                }
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, "Delete")
            }
        }
    }
}