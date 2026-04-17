package com.example.rakshasetu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.rakshasetu.core.navigation.AppNavHost
import com.example.rakshasetu.core.navigation.Screen
import com.example.rakshasetu.ui.theme.RakshaSetuTheme
import com.example.rakshasetu.viewModel.viewModel.auth.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RakshaSetuTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RakshaSetuApp()
                }
            }
        }
    }
}

@Composable
fun RakshaSetuApp() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()

    // Observe authentication state
    val isUserLoggedIn by authViewModel.isUserLoggedIn.collectAsStateWithLifecycle()
    val userRole by authViewModel.userRole.collectAsStateWithLifecycle()
    val userData by authViewModel.userData.collectAsStateWithLifecycle()
    val isLoading by authViewModel.isLoading.collectAsStateWithLifecycle()

    // Determine start destination - ALWAYS START WITH SPLASH SCREEN
    val startDestination = Screen.Splash.route

    // If user is already logged in, navigate to appropriate home after splash
    LaunchedEffect(isUserLoggedIn, userRole) {
        if (isUserLoggedIn) {
            // Small delay to ensure splash screen is shown
            kotlinx.coroutines.delay(100)
            when (userRole) {
                "secretary" -> {
                    navController.navigate(Screen.SecretaryHome.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
                "watchman" -> {
                    navController.navigate(Screen.WatchmanHome.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
                "resident" -> {
                    navController.navigate(Screen.ResidentHome.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            }
        }
    }

    // Set up navigation
    AppNavHost(
        navController = navController,
        startDestination = startDestination,
        isUserLoggedIn = isUserLoggedIn,
        userRole = userRole,
        userData = userData
    )
}



//
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Modifier
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.compose.rememberNavController
//import com.example.rakshasetu.core.navigation.AppNavHost
//import com.example.rakshasetu.core.navigation.Screen
//import com.example.rakshasetu.ui.theme.RakshaSetuTheme
//import com.example.rakshasetu.viewModel.viewModel.auth.AuthViewModel
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            RakshaSetuTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    RakshaSetuApp()
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun RakshaSetuApp() {
//    val navController = rememberNavController()
//    val authViewModel: AuthViewModel = viewModel()
//
//    // Observe authentication state
//    val isUserLoggedIn by authViewModel.isUserLoggedIn.collectAsStateWithLifecycle()
//    val userRole by authViewModel.userRole.collectAsStateWithLifecycle()
//    val userData by authViewModel.userData.collectAsStateWithLifecycle()
//
//    // Set up navigation - ALWAYS START WITH SPLASH SCREEN
//    AppNavHost(
//        navController = navController,
//        startDestination = Screen.Splash.route,
//        isUserLoggedIn = isUserLoggedIn,
//        userRole = userRole,
//        userData = userData
//    )
//}