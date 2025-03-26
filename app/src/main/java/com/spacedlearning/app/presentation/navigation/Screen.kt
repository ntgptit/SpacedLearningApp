package com.spacedlearning.app.presentation.navigation

sealed class Screen(val route: String) {
    // Auth
    object Login : Screen("login_screen")
    object Register : Screen("register_screen")

    // Main screens
    object Home : Screen("home_screen")
    object BookList : Screen("book_list_screen")
    object BookDetail : Screen("book_detail_screen/{bookId}") {
        fun createRoute(bookId: String) = "book_detail_screen/$bookId"
    }

    // Module screens
    object ModuleList : Screen("module_list_screen/{bookId}") {
        fun createRoute(bookId: String) = "module_list_screen/$bookId"
    }
    object ModuleDetail : Screen("module_detail_screen/{moduleId}") {
        fun createRoute(moduleId: String) = "module_detail_screen/$moduleId"
    }

    // Progress screens
    object ProgressList : Screen("progress_list_screen")
    object DueStudy : Screen("due_study_screen")

    // User screens
    object Profile : Screen("profile_screen")
    object Settings : Screen("settings_screen")

    // Navigation arguments
    companion object {
        const val BOOK_ID_KEY = "bookId"
        const val MODULE_ID_KEY = "moduleId"
        const val PROGRESS_ID_KEY = "progressId"
    }
}