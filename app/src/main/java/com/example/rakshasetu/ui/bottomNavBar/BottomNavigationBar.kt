package com.example.rakshasetu.ui.bottomNavBar


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.rakshasetu.core.navigation.Screen
import com.example.rakshasetu.ui.bottomNavBar.BottomNavItem  // IMPORT FROM YOUR EXISTING FILE

// Bottom Navigation Colors
val BottomNavBackgroundColor = Color(0xFF0CB381)
val BottomNavSelectedColor = Color(0xFF0046CC)
val BottomNavUnselectedColor = Color.White.copy(alpha = 0.7f)

@Composable
fun BottomNavigationBar(
    navController: NavController,
    currentRoute: String?,
    userRole: String?
) {
    NavigationBar(
        containerColor = BottomNavBackgroundColor,
        tonalElevation = 0.dp
    ) {
        BottomNavItem.items.forEach { item ->
            val isSelected = when (item) {
                BottomNavItem.Home -> {
                    currentRoute == Screen.SecretaryHome.route ||
                            currentRoute == Screen.WatchmanHome.route ||
                            currentRoute == Screen.ResidentHome.route
                }
                else -> currentRoute == item.route
            }

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    when (item) {
                        BottomNavItem.Home -> {
                            when (userRole) {
                                "secretary" -> {
                                    navController.navigate(Screen.SecretaryHome.route) {
                                        popUpTo(Screen.SecretaryHome.route) { inclusive = true }
                                        launchSingleTop = true
                                    }
                                }
                                "watchman" -> {
                                    navController.navigate(Screen.WatchmanHome.route) {
                                        popUpTo(Screen.WatchmanHome.route) { inclusive = true }
                                        launchSingleTop = true
                                    }
                                }
                                "resident" -> {
                                    navController.navigate(Screen.ResidentHome.route) {
                                        popUpTo(Screen.ResidentHome.route) { inclusive = true }
                                        launchSingleTop = true
                                    }
                                }
                                else -> {
                                    navController.navigate(Screen.SignIn.route) {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            }
                        }
                        else -> {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                        contentDescription = item.title,
                        tint = if (isSelected) BottomNavSelectedColor else BottomNavUnselectedColor
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        color = if (isSelected) BottomNavSelectedColor else BottomNavUnselectedColor,
                        fontSize = 12.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = BottomNavSelectedColor,
                    selectedTextColor = BottomNavSelectedColor,
                    unselectedIconColor = BottomNavUnselectedColor,
                    unselectedTextColor = BottomNavUnselectedColor,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}