package com.example.rakshasetu.ui.notification
import androidx.compose.foundation.clickable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.rakshasetu.core.components.BottomNavigationBar
import com.example.rakshasetu.core.navigation.Screen
import com.example.rakshasetu.core.utils.safeClickable
import com.example.rakshasetu.ui.theme.*
import java.util.*

// Top Bar Color - Forest Green (#007F50)
val NotificationTopBarColor = ForestGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    navController: NavController,
    userRole: String? = null
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    var selectedTabIndex by remember { mutableStateOf(0) }

    // Get notifications based on user role
    val notifications = remember(userRole) {
        getNotificationsForRole(userRole)
    }

    val unreadCount = notifications.count { !it.isRead }
    val tabs = listOf("All", "Unread")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Notifications",
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
                actions = {
                    // Mark all as read button
                    if (unreadCount > 0) {
                        IconButton(
                            onClick = {
                                // Mark all as read
                            }
                        ) {
                            Icon(
                                Icons.Default.DoneAll,
                                contentDescription = "Mark all as read",
                                tint = Color.White
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = NotificationTopBarColor
                )
            )
        },
//        bottomBar = {
//            BottomNavigationBar(
//                navController = navController,
//                currentRoute = currentRoute,
//                userRole = userRole
//            )
//        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(paddingValues)
        ) {
            // Simple custom tab row instead of TabRow to avoid badge issues
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Surface)
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                tabs.forEachIndexed { index, title ->
                    val isSelected = selectedTabIndex == index

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .safeClickable { selectedTabIndex = index }  // Using safeClickable
                            .padding(vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Title with badge for Unread tab
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = title,
                                color = if (isSelected) Primary else TextSecondary,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                fontSize = 16.sp
                            )

                            // Show badge only on Unread tab when there are unread notifications
                            if (index == 1 && unreadCount > 0) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Box(
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clip(CircleShape)
                                        .background(Error),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = unreadCount.toString(),
                                        fontSize = 10.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        // Indicator line for selected tab
                        if (isSelected) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(2.dp)
                                    .background(Primary)
                                    .padding(top = 4.dp)
                            )
                        }
                    }
                }
            }

            // Notifications List
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val filteredNotifications = when (selectedTabIndex) {
                    0 -> notifications
                    else -> notifications.filter { !it.isRead }
                }

                if (filteredNotifications.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    Icons.Default.NotificationsOff,
                                    contentDescription = null,
                                    tint = TextHint,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "No notifications",
                                    fontSize = 16.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                } else {
                    itemsIndexed(filteredNotifications) { index, notification ->
                        NotificationCard(
                            notification = notification,
                            onClick = {
                                // Navigate based on notification type
                                handleNotificationClick(navController, notification)
                            }
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun NotificationCard(
    notification: NotificationItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .safeClickable(onClick),  // Using safeClickable on the Card
        colors = CardDefaults.cardColors(
            containerColor = if (notification.isRead) Surface else Primary.copy(alpha = 0.05f)
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(if (notification.isRead) 1.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon based on notification type
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(notification.iconColor.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    notification.icon,
                    contentDescription = null,
                    tint = notification.iconColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Content
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = notification.title,
                    fontSize = 16.sp,
                    fontWeight = if (notification.isRead) FontWeight.Normal else FontWeight.Bold,
                    color = TextPrimary
                )

                Text(
                    text = notification.message,
                    fontSize = 14.sp,
                    color = TextSecondary,
                    maxLines = 2
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = notification.time,
                    fontSize = 11.sp,
                    color = TextHint
                )
            }

            // Unread indicator
            if (!notification.isRead) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Primary)
                )
            }
        }
    }
}

fun handleNotificationClick(navController: NavController, notification: NotificationItem) {
    when (notification.type) {
        NotificationType.VISITOR_REQUEST -> {
            // Navigate to visitor approval with ID
            navController.navigate("visitor_approval/${notification.targetId}")
        }
        NotificationType.VISITOR_APPROVED -> {
            // Navigate to visitor details
        }
        NotificationType.JOIN_REQUEST -> {
            navController.navigate(Screen.JoinRequests.route)
        }
        NotificationType.ANNOUNCEMENT -> {
            navController.navigate(Screen.Announcements.route)
        }
        NotificationType.EMERGENCY -> {
            navController.navigate(Screen.Emergency.route)
        }
        NotificationType.SOS_ALERT -> {
            navController.navigate(Screen.Emergency.route)
        }
        else -> {
            // Default action - do nothing
        }
    }
}

enum class NotificationType {
    VISITOR_REQUEST,
    VISITOR_APPROVED,
    VISITOR_DENIED,
    PACKAGE_LEFT,
    JOIN_REQUEST,
    JOIN_APPROVED,
    ANNOUNCEMENT,
    EMERGENCY,
    SOS_ALERT,
    SYSTEM
}

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val time: String,
    val type: NotificationType,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val iconColor: Color,
    val isRead: Boolean = false,
    val targetId: String = ""
)

fun getNotificationsForRole(role: String?): List<NotificationItem> {
    val now = Date()
    val allNotifications = mutableListOf<NotificationItem>()

    // Common notifications for all roles
    allNotifications.addAll(
        listOf(
            NotificationItem(
                id = "1",
                title = "Security Alert",
                message = "Unknown person spotted near Block A. Please be vigilant.",
                time = getTimeAgo(Date(now.time - 30 * 60 * 1000)),
                type = NotificationType.EMERGENCY,
                icon = Icons.Default.Warning,
                iconColor = Error,
                isRead = false
            ),
            NotificationItem(
                id = "2",
                title = "Maintenance Notice",
                message = "Water supply will be interrupted tomorrow from 10 AM to 4 PM.",
                time = getTimeAgo(Date(now.time - 2 * 60 * 60 * 1000)),
                type = NotificationType.ANNOUNCEMENT,
                icon = Icons.Default.Announcement,
                iconColor = Primary,
                isRead = true
            ),
            NotificationItem(
                id = "3",
                title = "Diwali Celebration",
                message = "Diwali celebration will be held on 12th November at 7 PM.",
                time = getTimeAgo(Date(now.time - 24 * 60 * 60 * 1000)),
                type = NotificationType.ANNOUNCEMENT,
                icon = Icons.Default.Star,
                iconColor = TealMain,
                isRead = false
            )
        )
    )

    // Role-specific notifications
    when (role) {
        "secretary" -> {
            allNotifications.addAll(
                listOf(
                    NotificationItem(
                        id = "s1",
                        title = "New Join Request",
                        message = "Rahul Sharma (Flat A-101) requests to join the society.",
                        time = getTimeAgo(Date(now.time - 15 * 60 * 1000)),
                        type = NotificationType.JOIN_REQUEST,
                        icon = Icons.Default.PersonAdd,
                        iconColor = Primary,
                        isRead = false,
                        targetId = "req123"
                    ),
                    NotificationItem(
                        id = "s2",
                        title = "Watchman Shift Change",
                        message = "Vikram Singh has requested a shift change to evening.",
                        time = getTimeAgo(Date(now.time - 45 * 60 * 1000)),
                        type = NotificationType.SYSTEM,
                        icon = Icons.Default.Security,
                        iconColor = Secondary,
                        isRead = false
                    ),
                    NotificationItem(
                        id = "s3",
                        title = "New Resident Added",
                        message = "Priya Sharma has been added to Flat B-202.",
                        time = getTimeAgo(Date(now.time - 3 * 60 * 60 * 1000)),
                        type = NotificationType.SYSTEM,
                        icon = Icons.Default.Person,
                        iconColor = Success,
                        isRead = true
                    )
                )
            )
        }
        "watchman" -> {
            allNotifications.addAll(
                listOf(
                    NotificationItem(
                        id = "w1",
                        title = "Visitor Approved",
                        message = "Zomato delivery for Flat A-101 has been approved.",
                        time = getTimeAgo(Date(now.time - 5 * 60 * 1000)),
                        type = NotificationType.VISITOR_APPROVED,
                        icon = Icons.Default.CheckCircle,
                        iconColor = Success,
                        isRead = false,
                        targetId = "vis123"
                    ),
                    NotificationItem(
                        id = "w2",
                        title = "Visitor Request",
                        message = "Guest for Flat C-301 waiting at gate.",
                        time = getTimeAgo(Date(now.time - 10 * 60 * 1000)),
                        type = NotificationType.VISITOR_REQUEST,
                        icon = Icons.Default.Person,
                        iconColor = Warning,
                        isRead = true,
                        targetId = "vis456"
                    ),
                    NotificationItem(
                        id = "w3",
                        title = "Package Delivered",
                        message = "Amazon package left at security for Flat B-204.",
                        time = getTimeAgo(Date(now.time - 20 * 60 * 1000)),
                        type = NotificationType.PACKAGE_LEFT,
                        icon = Icons.Default.ShoppingBag,
                        iconColor = TealMain,
                        isRead = false
                    )
                )
            )
        }
        "resident" -> {
            allNotifications.addAll(
                listOf(
                    NotificationItem(
                        id = "r1",
                        title = "Visitor Request",
                        message = "Zomato delivery person at gate for you.",
                        time = getTimeAgo(Date(now.time - 2 * 60 * 1000)),
                        type = NotificationType.VISITOR_REQUEST,
                        icon = Icons.Default.LocalShipping,
                        iconColor = Primary,
                        isRead = false,
                        targetId = "vis789"
                    ),
                    NotificationItem(
                        id = "r2",
                        title = "Guest Arrived",
                        message = "Your guest Rajesh is at the gate.",
                        time = getTimeAgo(Date(now.time - 8 * 60 * 1000)),
                        type = NotificationType.VISITOR_REQUEST,
                        icon = Icons.Default.Person,
                        iconColor = Secondary,
                        isRead = true,
                        targetId = "vis101"
                    ),
                    NotificationItem(
                        id = "r3",
                        title = "Package Delivered",
                        message = "Your Amazon package has been delivered to security.",
                        time = getTimeAgo(Date(now.time - 25 * 60 * 1000)),
                        type = NotificationType.PACKAGE_LEFT,
                        icon = Icons.Default.ShoppingBag,
                        iconColor = Success,
                        isRead = false
                    ),
                    NotificationItem(
                        id = "r4",
                        title = "Join Request Approved",
                        message = "Your request to join has been approved.",
                        time = getTimeAgo(Date(now.time - 2 * 60 * 60 * 1000)),
                        type = NotificationType.JOIN_APPROVED,
                        icon = Icons.Default.CheckCircle,
                        iconColor = Success,
                        isRead = true
                    )
                )
            )
        }
    }

    // Sort by time (most recent first)
    return allNotifications.sortedByDescending { parseTimeAgo(it.time) }
}

// Helper function to parse time for sorting
fun parseTimeAgo(timeString: String): Long {
    return when {
        timeString.contains("min") -> {
            val minutes = timeString.replace(Regex("[^0-9]"), "").toIntOrNull() ?: 0
            System.currentTimeMillis() - (minutes * 60 * 1000)
        }
        timeString.contains("hour") -> {
            val hours = timeString.replace(Regex("[^0-9]"), "").toIntOrNull() ?: 0
            System.currentTimeMillis() - (hours * 60 * 60 * 1000)
        }
        timeString.contains("day") -> {
            val days = timeString.replace(Regex("[^0-9]"), "").toIntOrNull() ?: 0
            System.currentTimeMillis() - (days * 24 * 60 * 60 * 1000)
        }
        else -> System.currentTimeMillis()
    }
}

fun getTimeAgo(date: Date): String {
    val now = Date()
    val diffInSeconds = (now.time - date.time) / 1000

    return when {
        diffInSeconds < 60 -> "Just now"
        diffInSeconds < 3600 -> "${diffInSeconds / 60} min ago"
        diffInSeconds < 86400 -> "${diffInSeconds / 3600} hours ago"
        else -> "${diffInSeconds / 86400} days ago"
    }
}