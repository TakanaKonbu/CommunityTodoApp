package com.takanakonbu.communitytodoapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.takanakonbu.communitytodoapp.data.Todo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoEditScreen(
    todo: Todo? = null,
    onSaveClick: (String, String) -> Unit,
    onBackClick: () -> Unit
) {
    var title by remember { mutableStateOf(todo?.title ?: "") }
    var content by remember { mutableStateOf(todo?.content ?: "") }

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
                                onSaveClick(title, content)
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
        }
    }
}