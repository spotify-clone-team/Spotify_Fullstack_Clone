package com.example.spotifyclone.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.spotifyclone.ui.auth.LoginScreen
import com.example.spotifyclone.ui.auth.SignUpScreen
import com.example.spotifyclone.ui.auth.WelcomeScreen
import com.example.spotifyclone.ui.main.MainScreen

@Composable
fun RootNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AppRoute.Welcome.route
    ) {
        composable(AppRoute.Welcome.route) {
            WelcomeScreen(
                onNavigateToLogin = {
                    navController.navigate(AppRoute.Login.route)
                },
                onNavigateToSignUp = {
                    navController.navigate(AppRoute.SignUp.route)
                }
            )
        }

        composable(AppRoute.Login.route) {
            LoginScreen(
                onBack = { navController.popBackStack() },
                onLoginSuccess = {
                    navController.navigate(AppRoute.Main.route) {
                        popUpTo(AppRoute.Welcome.route) { inclusive = true }
                    }
                }
            )
        }

        composable(AppRoute.SignUp.route) {
            SignUpScreen(
                onBack = { navController.popBackStack() },
                onSignUpSuccess = {
                    navController.navigate(AppRoute.Main.route) {
                        popUpTo(AppRoute.Welcome.route) { inclusive = true }
                    }
                }
            )
        }

        composable(AppRoute.Main.route) {
            MainScreen()
        }
    }
}