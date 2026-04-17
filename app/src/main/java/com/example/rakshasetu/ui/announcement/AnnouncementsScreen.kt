package com.example.rakshasetu.ui.announcement
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.rakshasetu.core.navigation.Screen
import com.example.rakshasetu.ui.bottomNavBar.BottomNavItem
import com.example.rakshasetu.ui.theme.*
import java.util.*

// Top Bar Color - Hardcoded #0046CC
val TopBarColor = Color(0xFFD22030)
// Bottom Navigation Color
val BottomNavBackgroundColor = Color(0xFF007F50)
val BottomNavSelectedColor = Color(0xFF0046CC)
val BottomNavUnselectedColor = Color.White.copy(alpha = 0.7f)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnnouncementsScreen(
    navController: NavController,
    userRole: String? = null  // Add this parameter
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    // Sample announcements
    val announcements = listOf(
        AnnouncementItem(
            "Maintenance Notice",
            "Water supply will be interrupted on Sunday from 10 AM to 4 PM for maintenance work. Please store water accordingly.",
            getTimeAgo(Date(System.currentTimeMillis() - 2 * 60 * 60 * 1000)),
            Priority.HIGH,
            "Secretary"
        ),
        AnnouncementItem(
            "Security Alert",
            "Be cautious of unknown persons in the society. Report any suspicious activity to security immediately.",
            getTimeAgo(Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000)),
            Priority.URGENT,
            "Security Head"
        ),
        AnnouncementItem(
            "Society Meeting",
            "Annual general meeting will be held on 15th March at 6 PM in the community hall. All residents are requested to attend.",
            getTimeAgo(Date(System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000)),
            Priority.NORMAL,
            "Secretary"
        ),
        AnnouncementItem(
            "Festival Celebration",
            "Diwali celebration will be organized on 12th November. All residents are invited to participate.",
            getTimeAgo(Date(System.currentTimeMillis() - 3 * 24 * 60 * 60 * 1000)),
            Priority.NORMAL,
            "Cultural Committee"
        ),
        AnnouncementItem(
            "Power Cut Notice",
            "There will be a power cut on Tuesday from 9 AM to 11 AM for electrical maintenance.",
            getTimeAgo(Date(System.currentTimeMillis() - 4 * 24 * 60 * 60 * 1000)),
            Priority.HIGH,
            "Maintenance"
        ),
        AnnouncementItem(
            "Yoga Classes",
            "Free yoga classes will start from next Monday at 7 AM in the community park. Interested residents can register.",
            getTimeAgo(Date(System.currentTimeMillis() - 5 * 24 * 60 * 60 * 1000)),
            Priority.NORMAL,
            "Wellness Committee"
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Announcements",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = TopBarColor
                )
            )
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                currentRoute = currentRoute,
                userRole = userRole
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(announcements) { announcement ->
                AnnouncementCard(announcement)
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// Keep your existing enum, data class, AnnouncementCard, and getTimeAgo function here
// ...

enum class Priority {
    URGENT,
    HIGH,
    NORMAL
}

data class AnnouncementItem(
    val title: String,
    val message: String,
    val time: String,
    val priority: Priority,
    val postedBy: String
)

@Composable
fun AnnouncementCard(announcement: AnnouncementItem) {
    val backgroundColor = when (announcement.priority) {
        Priority.URGENT -> Error.copy(alpha = 0.1f)
        Priority.HIGH -> Warning.copy(alpha = 0.1f)
        Priority.NORMAL -> Primary.copy(alpha = 0.1f)
    }

    val icon = when (announcement.priority) {
        Priority.URGENT -> Icons.Default.Warning
        Priority.HIGH -> Icons.Default.PriorityHigh
        Priority.NORMAL -> Icons.Default.Info
    }

    val iconColor = when (announcement.priority) {
        Priority.URGENT -> Error
        Priority.HIGH -> Warning
        Priority.NORMAL -> Primary
    }

    val borderColor = when (announcement.priority) {
        Priority.URGENT -> Error
        Priority.HIGH -> Warning
        Priority.NORMAL -> Color.Transparent
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Surface
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(backgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = announcement.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    Text(
                        text = "Posted by: ${announcement.postedBy}",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                }

                if (announcement.priority != Priority.NORMAL) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(borderColor.copy(alpha = 0.1f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = announcement.priority.name,
                            fontSize = 10.sp,
                            color = borderColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = announcement.message,
                fontSize = 14.sp,
                color = TextSecondary,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = announcement.time,
                fontSize = 11.sp,
                color = TextHint,
                modifier = Modifier.align(Alignment.End)
            )
        }
    }
}

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

fun getTimeAgo(date: Date): String {
    val now = Date()
    val diffInSeconds = (now.time - date.time) / 1000

    return when {
        diffInSeconds < 60 -> "Just now"
        diffInSeconds < 3600 -> "${diffInSeconds / 60} minutes ago"
        diffInSeconds < 86400 -> "${diffInSeconds / 3600} hours ago"
        else -> "${diffInSeconds / 86400} days ago"
    }
}