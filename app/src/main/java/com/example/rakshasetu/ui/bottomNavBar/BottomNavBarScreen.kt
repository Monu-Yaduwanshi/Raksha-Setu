package com.example.rakshasetu.ui.bottomNavBar

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Announcement
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material.icons.outlined.Announcement
import androidx.compose.material.icons.outlined.Contacts
import androidx.compose.material.icons.outlined.Person
import com.example.rakshasetu.core.navigation.Screen

sealed class BottomNavItem(
    val route: String,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    object Home : BottomNavItem(
        route = "home", // This is a placeholder, actual route determined by role
        title = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    )

    object Emergency : BottomNavItem(
        route = Screen.Emergency.route,
        title = "Emergency",
        selectedIcon = Icons.Filled.Warning,
        unselectedIcon = Icons.Outlined.Warning
    )

    object Announcements : BottomNavItem(
        route = Screen.Announcements.route,
        title = "Announcements",
        selectedIcon = Icons.Filled.Announcement,
        unselectedIcon = Icons.Outlined.Announcement
    )

    object Directory : BottomNavItem(
        route = Screen.Directory.route,
        title = "Directory",
        selectedIcon = Icons.Filled.Contacts,
        unselectedIcon = Icons.Outlined.Contacts
    )

    object Profile : BottomNavItem(
        route = Screen.Profile.route,
        title = "Profile",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person
    )

    companion object {
        val items = listOf(
            Home,
            Emergency,
            Announcements,
            Directory,
            Profile
        )
    }
}