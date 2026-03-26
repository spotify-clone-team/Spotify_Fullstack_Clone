package com.example.spotifyclone.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.spotifyclone.ui.auth.LoginScreen
import com.example.spotifyclone.ui.auth.SignUpOptionsScreen
import com.example.spotifyclone.ui.auth.SignUpScreen
import com.example.spotifyclone.ui.auth.WelcomeScreen
import com.example.spotifyclone.ui.main.MainScreen

@Composable
fun RootNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = AppRoute.Welcome.route,
        modifier = modifier
    ) {
        composable(AppRoute.Welcome.route) {
            WelcomeScreen(
                onSignUpClick = {
                    navController.navigate(AppRoute.SignUpOptions.route)
                },
                onLoginClick = {
                    navController.navigate(AppRoute.Login.route)
                }
            )
        }

        composable(AppRoute.SignUpOptions.route) {
            SignUpOptionsScreen(
                onBackClick = { navController.popBackStack() },
                onContinueClick = {
                    navController.navigate(AppRoute.SignUp.route)
                },
                onLoginClick = {
                    navController.navigate(AppRoute.Login.route)
                }
            )
        }

        composable(AppRoute.Login.route) {
            LoginScreen(
                onBackClick = { navController.popBackStack() },
                onLoginSuccess = {
                    navController.navigate(AppRoute.Main.route) {
                        popUpTo(AppRoute.Welcome.route) { inclusive = true }
                    }
                },
                onGoToSignUp = {
                    navController.navigate(AppRoute.SignUp.route)
                }
            )
        }

        composable(AppRoute.SignUp.route) {
            SignUpScreen(
                onBackClick = { navController.popBackStack() },
                onSignUpSuccess = {
                    navController.navigate(AppRoute.Main.route) {
                        popUpTo(AppRoute.Welcome.route) { inclusive = true }
                    }
                },
                onGoToLogin = {
                    navController.navigate(AppRoute.Login.route)
                }
            )
        }

        composable(AppRoute.Main.route) {
            MainScreen()
        }
    }
}