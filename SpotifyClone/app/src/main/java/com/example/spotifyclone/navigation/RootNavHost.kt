package com.example.spotifyclone.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.spotifyclone.ui.auth.LoginOptionsScreen
import com.example.spotifyclone.ui.auth.LoginScreen
import com.example.spotifyclone.ui.auth.SignUpOptionsScreen
import com.example.spotifyclone.ui.auth.SignUpScreen
import com.example.spotifyclone.ui.auth.WelcomeScreen
import com.example.spotifyclone.ui.main.MainScreen
import com.example.spotifyclone.utils.SessionManager

@Composable
fun RootNavHost(navController: NavHostController) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val userToken by sessionManager.token.collectAsState(initial = "CHECKING")

    if (userToken == "CHECKING") return

    NavHost(
        navController = navController,
        startDestination = if (userToken != null) AppRoute.MAIN else AppRoute.WELCOME
    ) {
        composable(AppRoute.WELCOME) {
            WelcomeScreen(
                onSignUpClick = { navController.navigate(AppRoute.SIGN_UP_OPTIONS) },
                onLoginClick = { navController.navigate(AppRoute.LOGIN_OPTIONS) }
            )
        }

        composable(AppRoute.SIGN_UP_OPTIONS) {
            SignUpOptionsScreen(
                onBackClick = { navController.popBackStack() },
                onEmailClick = { navController.navigate(AppRoute.REGISTER) },
                onLoginClick = {
                    navController.navigate(AppRoute.LOGIN_OPTIONS) {
                        popUpTo(AppRoute.WELCOME)
                    }
                }
            )
        }

        composable(AppRoute.LOGIN_OPTIONS) {
            LoginOptionsScreen(
                onBackClick = { navController.popBackStack() },
                onEmailClick = { navController.navigate(AppRoute.LOGIN) },
                onSignUpClick = {
                    navController.navigate(AppRoute.SIGN_UP_OPTIONS) {
                        popUpTo(AppRoute.WELCOME)
                    }
                }
            )
        }

        composable(AppRoute.REGISTER) {
            SignUpScreen(
                onBackClick = { navController.popBackStack() },
                sessionManager = sessionManager,
                onSignUpSuccess = {
                    navController.navigate(AppRoute.MAIN) {
                        popUpTo(0)
                    }
                }
            )
        }

        composable(AppRoute.LOGIN) {
            LoginScreen(
                onBackClick = { navController.popBackStack() },
                sessionManager = sessionManager,
                onLoginSuccess = {
                    navController.navigate(AppRoute.MAIN) {
                        popUpTo(0)
                    }
                }
            )
        }

        composable(AppRoute.MAIN) {
            MainScreen(rootNavController = navController)
        }
    }
}