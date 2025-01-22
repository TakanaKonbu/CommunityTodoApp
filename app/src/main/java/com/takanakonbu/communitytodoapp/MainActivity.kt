package com.takanakonbu.communitytodoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.takanakonbu.communitytodoapp.ui.TodoViewModel
import com.takanakonbu.communitytodoapp.ui.navigation.Screen
import com.takanakonbu.communitytodoapp.ui.screens.TodoEditScreen
import com.takanakonbu.communitytodoapp.ui.screens.TodoListScreen
import com.takanakonbu.communitytodoapp.ui.theme.CommunityTodoAppTheme

class MainActivity : ComponentActivity() {
    private val viewModel: TodoViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TodoViewModel(application) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CommunityTodoAppTheme {
                val navController = rememberNavController()
                val todos by viewModel.allTodos.collectAsState(initial = emptyList())

                NavHost(navController = navController, startDestination = Screen.TodoList.route) {
                    composable(Screen.TodoList.route) {
                        TodoListScreen(
                            todos = todos,
                            onAddClick = { navController.navigate(Screen.AddTodo.route) },
                            onTodoClick = { todo ->
                                navController.navigate(Screen.EditTodo.createRoute(todo.id))
                            },
                            onDeleteClick = { todo -> viewModel.deleteTodo(todo) }
                        )
                    }

                    composable(Screen.AddTodo.route) {
                        TodoEditScreen(
                            onSaveClick = { title, content, isDontWantToDo ->
                                viewModel.addTodo(title, content, isDontWantToDo)
                                navController.navigateUp()
                            },
                            onBackClick = { navController.navigateUp() }
                        )

                    }

                    composable(
                        route = Screen.EditTodo.route,
                        arguments = listOf(navArgument("todoId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val todoId = backStackEntry.arguments?.getInt("todoId") ?: return@composable
                        val todo = todos.find { it.id == todoId } ?: return@composable

                        TodoEditScreen(
                            todo = todo,
                            onSaveClick = { title, content, isDontWantToDo ->
                                viewModel.updateTodo(todo.copy(
                                    title = title,
                                    content = content,
                                    isDontWantToDo = isDontWantToDo
                                ))
                                navController.navigateUp()
                            },
                            onBackClick = { navController.navigateUp() }
                        )
                    }
                }
            }
        }
    }
}