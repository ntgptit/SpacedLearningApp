package com.spacedlearning.app.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.spacedlearning.app.presentation.screens.auth.AuthViewModel
import com.spacedlearning.app.presentation.screens.auth.LoginScreen
import com.spacedlearning.app.presentation.screens.auth.RegisterScreen
import com.spacedlearning.app.presentation.screens.books.BookDetailScreen
import com.spacedlearning.app.presentation.screens.books.BookListScreen
import com.spacedlearning.app.presentation.screens.books.BookViewModel
import com.spacedlearning.app.presentation.screens.books.CreateBookScreen
import com.spacedlearning.app.presentation.screens.books.EditBookScreen
import com.spacedlearning.app.presentation.screens.home.HomeScreen
import com.spacedlearning.app.presentation.screens.modules.CreateModuleScreen
import com.spacedlearning.app.presentation.screens.modules.EditModuleScreen
import com.spacedlearning.app.presentation.screens.modules.ModuleDetailScreen
import com.spacedlearning.app.presentation.screens.modules.ModuleListScreen
import com.spacedlearning.app.presentation.screens.modules.ModuleViewModel
import com.spacedlearning.app.presentation.screens.profile.ProfileScreen
import com.spacedlearning.app.presentation.screens.profile.ProfileViewModel
import com.spacedlearning.app.presentation.screens.progress.DueStudyScreen
import com.spacedlearning.app.presentation.screens.progress.ProgressScreen
import com.spacedlearning.app.presentation.screens.progress.ProgressViewModel
import com.spacedlearning.app.presentation.screens.repetition.RepetitionScreen
import com.spacedlearning.app.presentation.screens.settings.SettingsScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(
    navController: NavHostController
) {
    // Get the auth view model
    val authViewModel: AuthViewModel = hiltViewModel()
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Screen.Home.route else Screen.Login.route
    ) {
        // Auth screens
        composable(route = Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onLoginSuccess = { navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Login.route) { inclusive = true }
                }},
                viewModel = authViewModel
            )
        }

        composable(route = Screen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = { navController.navigate(Screen.Login.route) },
                onRegisterSuccess = { navController.navigate(Screen.Login.route) },
                viewModel = authViewModel
            )
        }

        // Main screens
        composable(route = Screen.Home.route) {
            HomeScreen(
                onNavigateToBooks = { navController.navigate(Screen.BookList.route) },
                onNavigateToDueStudy = { navController.navigate(Screen.DueStudy.route) },
                onNavigateToProgress = { navController.navigate(Screen.ProgressList.route) },
                onNavigateToProfile = { navController.navigate(Screen.Profile.route) }
            )
        }

        // Book screens
        composable(route = Screen.BookList.route) {
            val bookViewModel: BookViewModel = hiltViewModel()
            BookListScreen(
                onBookClick = { bookId ->
                    navController.navigate(Screen.BookDetail.createRoute(bookId))
                },
                onCreateBookClick = {
                    navController.navigate(Screen.CreateBook.route)
                },
                onBackClick = { navController.popBackStack() },
                viewModel = bookViewModel
            )
        }

        composable(route = Screen.CreateBook.route) {
            CreateBookScreen(
                onNavigateBack = { navController.popBackStack() },
                onBookCreated = { bookId ->
                    navController.navigate(Screen.BookDetail.createRoute(bookId)) {
                        popUpTo(Screen.BookList.route)
                    }
                }
            )
        }

        composable(
            route = Screen.BookDetail.route,
            arguments = listOf(
                navArgument(Screen.BOOK_ID_KEY) { type = NavType.StringType }
            )
        ) { entry ->
            val bookId = entry.arguments?.getString(Screen.BOOK_ID_KEY) ?: ""
            val bookViewModel: BookViewModel = hiltViewModel()
            BookDetailScreen(
                bookId = bookId,
                onModulesClick = {
                    navController.navigate(Screen.ModuleList.createRoute(bookId))
                },
                onEditClick = {
                    navController.navigate(Screen.EditBook.createRoute(bookId))
                },
                onCreateModuleClick = {
                    navController.navigate(Screen.CreateModule.createRoute(bookId))
                },
                onBackClick = { navController.popBackStack() },
                viewModel = bookViewModel
            )
        }

        composable(
            route = Screen.EditBook.route,
            arguments = listOf(
                navArgument(Screen.BOOK_ID_KEY) { type = NavType.StringType }
            )
        ) { entry ->
            val bookId = entry.arguments?.getString(Screen.BOOK_ID_KEY) ?: ""
            EditBookScreen(
                bookId = bookId,
                onNavigateBack = { navController.popBackStack() },
                onBookUpdated = { navController.popBackStack() }
            )
        }

        // Module screens
        composable(
            route = Screen.CreateModule.route,
            arguments = listOf(
                navArgument(Screen.BOOK_ID_KEY) { type = NavType.StringType }
            )
        ) { entry ->
            val bookId = entry.arguments?.getString(Screen.BOOK_ID_KEY) ?: ""
            CreateModuleScreen(
                bookId = bookId,
                onNavigateBack = { navController.popBackStack() },
                onModuleCreated = { moduleId ->
                    navController.navigate(Screen.ModuleDetail.createRoute(moduleId)) {
                        popUpTo(Screen.BookDetail.route)
                    }
                }
            )
        }

        composable(
            route = Screen.ModuleList.route,
            arguments = listOf(
                navArgument(Screen.BOOK_ID_KEY) { type = NavType.StringType }
            )
        ) { entry ->
            val bookId = entry.arguments?.getString(Screen.BOOK_ID_KEY) ?: ""
            val moduleViewModel: ModuleViewModel = hiltViewModel()
            ModuleListScreen(
                bookId = bookId,
                onModuleClick = { moduleId ->
                    navController.navigate(Screen.ModuleDetail.createRoute(moduleId))
                },
                onBackClick = { navController.popBackStack() },
                viewModel = moduleViewModel
            )
        }

        composable(
            route = Screen.ModuleDetail.route,
            arguments = listOf(
                navArgument(Screen.MODULE_ID_KEY) { type = NavType.StringType }
            )
        ) { entry ->
            val moduleId = entry.arguments?.getString(Screen.MODULE_ID_KEY) ?: ""
            val moduleViewModel: ModuleViewModel = hiltViewModel()
            ModuleDetailScreen(
                moduleId = moduleId,
                onBackClick = { navController.popBackStack() },
                onEditClick = {
                    navController.navigate(Screen.EditModule.createRoute(moduleId))
                },
                onViewRepetitionsClick = {
                    // We need the progress ID, for simplicity, we'll assume we can get it from the module
                    navController.navigate(Screen.RepetitionDetail.createRoute("progress-id"))
                },
                viewModel = moduleViewModel
            )
        }

        composable(
            route = Screen.EditModule.route,
            arguments = listOf(
                navArgument(Screen.MODULE_ID_KEY) { type = NavType.StringType }
            )
        ) { entry ->
            val moduleId = entry.arguments?.getString(Screen.MODULE_ID_KEY) ?: ""
            EditModuleScreen(
                moduleId = moduleId,
                onNavigateBack = { navController.popBackStack() },
                onModuleUpdated = { navController.popBackStack() }
            )
        }

        // Progress screens
        composable(route = Screen.ProgressList.route) {
            val progressViewModel: ProgressViewModel = hiltViewModel()
            ProgressScreen(
                onBackClick = { navController.popBackStack() },
                onProgressClick = { progressId ->
                    navController.navigate(Screen.RepetitionDetail.createRoute(progressId))
                },
                viewModel = progressViewModel
            )
        }

        composable(route = Screen.DueStudy.route) {
            val progressViewModel: ProgressViewModel = hiltViewModel()
            DueStudyScreen(
                onProgressClick = { progressId ->
                    navController.navigate(Screen.RepetitionDetail.createRoute(progressId))
                },
                onBackClick = { navController.popBackStack() },
                viewModel = progressViewModel
            )
        }

        // Repetition screens
        composable(
            route = Screen.RepetitionDetail.route,
            arguments = listOf(
                navArgument(Screen.PROGRESS_ID_KEY) { type = NavType.StringType }
            )
        ) { entry ->
            val progressId = entry.arguments?.getString(Screen.PROGRESS_ID_KEY) ?: ""
            RepetitionScreen(
                progressId = progressId,
                onBackClick = { navController.popBackStack() }
            )
        }

        // Profile screens
        composable(route = Screen.Profile.route) {
            val profileViewModel: ProfileViewModel = hiltViewModel()
            ProfileScreen(
                onSettingsClick = { navController.navigate(Screen.Settings.route) },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onBackClick = { navController.popBackStack() },
                viewModel = profileViewModel
            )
        }

        composable(route = Screen.Settings.route) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}