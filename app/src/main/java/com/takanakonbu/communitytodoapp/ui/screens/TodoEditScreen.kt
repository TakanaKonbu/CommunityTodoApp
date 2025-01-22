package com.takanakonbu.communitytodoapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.takanakonbu.communitytodoapp.data.Todo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoEditScreen(
    todo: Todo? = null,
    onSaveClick: (String, String, Boolean) -> Unit,  // パラメータ追加
    onBackClick: () -> Unit
) {
    var title by remember { mutableStateOf(todo?.title ?: "") }
    var content by remember { mutableStateOf(todo?.content ?: "") }
    var isDontWantToDo by remember { mutableStateOf(todo?.isDontWantToDo ?: false) }  // 追加

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (todo == null) "Add Todo" else "Edit Todo") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (title.isNotBlank()) {
                                onSaveClick(title, content, isDontWantToDo)  // パラメータ追加
                            }
                        }
                    ) {
                        Icon(Icons.Default.Check, "Save")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            TextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Content") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                minLines = 3
            )
            // チェックボックスを追加
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isDontWantToDo,
                    onCheckedChange = { isDontWantToDo = it }
                )
                Text(
                    text = "やりたくない",
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
    }
}