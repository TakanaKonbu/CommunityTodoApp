package com.takanakonbu.communitytodoapp.ui.navigation

sealed class Screen(val route: String) {
    data object TodoList : Screen("todoList")
    data object AddTodo : Screen("addTodo")
    data object EditTodo : Screen("editTodo/{todoId}") {
        fun createRoute(todoId: Int) = "editTodo/$todoId"
    }
}