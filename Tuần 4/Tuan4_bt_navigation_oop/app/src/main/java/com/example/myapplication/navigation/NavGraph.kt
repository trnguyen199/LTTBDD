package com.example.myapplication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.model.NavigationScreen
import com.example.myapplication.ui.screens.StartScreen
import com.example.myapplication.ui.screens.Step1Screen
import com.example.myapplication.ui.screens.Step2Screen
import com.example.myapplication.ui.screens.Step3Screen

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = NavigationScreen.Start.route
    ) {
        composable(NavigationScreen.Start.route) {
            StartScreen(
                onNext = {
                    navController.navigate(NavigationScreen.Step1.route)
                }
            )
        }
        composable(NavigationScreen.Step1.route) {
            Step1Screen(
                onNext = {
                    navController.navigate(NavigationScreen.Step2.route)
                },
                onSkip = {
                    navController.navigate(NavigationScreen.Start.route) {
                        popUpTo(NavigationScreen.Start.route) { inclusive = true }
                    }
                }
            )
        }
        composable(NavigationScreen.Step2.route) {
            Step2Screen(
                onNext = {
                    navController.navigate(NavigationScreen.Step3.route)
                },
                onSkip = {
                    navController.navigate(NavigationScreen.Start.route) {
                        popUpTo(NavigationScreen.Start.route) { inclusive = true }
                    }
                }
            )
        }
        composable(NavigationScreen.Step3.route) {
            Step3Screen(
                onFinish = {
                    navController.navigate(NavigationScreen.Start.route) {
                        popUpTo(NavigationScreen.Start.route) { inclusive = true }
                    }
                },
                onSkip = {
                    navController.navigate(NavigationScreen.Start.route) {
                        popUpTo(NavigationScreen.Start.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
