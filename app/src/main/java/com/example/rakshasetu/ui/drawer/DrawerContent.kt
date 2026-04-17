package com.example.rakshasetu.ui.drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.rakshasetu.core.navigation.Screen
import com.example.rakshasetu.core.utils.safeClickable
import com.example.rakshasetu.ui.theme.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.rakshasetu.viewModel.viewModel.auth.AuthViewModel

val DividerColor = Color.LightGray.copy(alpha = 0.5f)

@Composable
fun DrawerContent(
    navController: NavController,
    userRole: String?,
    userName: String,
    userFlat: String,
    onCloseDrawer: () -> Unit
) {
    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Surface)
    ) {
        // Drawer Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(Primary, PrimaryVariant)
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Profile Image
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(Surface)
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = Primary,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = userName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Surface,
                    maxLines = 1
                )

                Text(
                    text = userRole?.replaceFirstChar { it.uppercase() } ?: "",
                    fontSize = 12.sp,
                    color = Surface.copy(alpha = 0.8f)
                )

                if (userFlat.isNotBlank()) {
                    Text(
                        text = userFlat,
                        fontSize = 11.sp,
                        color = Surface.copy(alpha = 0.8f)
                    )
                }
            }
        }

        // Drawer Items - Scrollable
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 8.dp)
        ) {
            // Home (navigates to role-specific home)
            DrawerItem(
                icon = Icons.Default.Home,
                label = "Home",
                onClick = {
                    when (userRole) {
                        "secretary" -> navController.navigate(Screen.SecretaryHome.route) {
                            popUpTo(Screen.SecretaryHome.route) { inclusive = true }
                        }
                        "watchman" -> navController.navigate(Screen.WatchmanHome.route) {
                            popUpTo(Screen.WatchmanHome.route) { inclusive = true }
                        }
                        "resident" -> navController.navigate(Screen.ResidentHome.route) {
                            popUpTo(Screen.ResidentHome.route) { inclusive = true }
                        }
                    }
                    onCloseDrawer()
                }
            )

            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = DividerColor
            )

            // ==================== SECRETARY ROLE ITEMS ====================
            if (userRole == "secretary") {
                // Society Management Section
                Text(
                    text = "SOCIETY MANAGEMENT",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
                )

                DrawerItem(
                    icon = Icons.Default.AddBusiness,
                    label = "Create Society",
                    onClick = {
                        navController.navigate(Screen.CreateSociety.route)
                        onCloseDrawer()
                    }
                )

                DrawerItem(
                    icon = Icons.Default.Settings,
                    label = "Manage Society",
                    onClick = {
                        navController.navigate(Screen.ManageSociety.route)
                        onCloseDrawer()
                    }
                )

                DrawerItem(
                    icon = Icons.Default.Apartment,
                    label = "Add Flats",
                    onClick = {
                        navController.navigate(Screen.AddFlats.route)
                        onCloseDrawer()
                    }
                )

                Divider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = DividerColor
                )

                // User Management Section
                Text(
                    text = "USER MANAGEMENT",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
                )

                DrawerItem(
                    icon = Icons.Default.PersonAdd,
                    label = "Add Resident",
                    onClick = {
                        navController.navigate(Screen.CreateResident.route)
                        onCloseDrawer()
                    }
                )

                DrawerItem(
                    icon = Icons.Default.Security,
                    label = "Add Watchman",
                    onClick = {
                        navController.navigate(Screen.CreateWatchman.route)
                        onCloseDrawer()
                    }
                )

                DrawerItem(
                    icon = Icons.Default.GroupAdd,
                    label = "Join Requests",
                    onClick = {
                        navController.navigate(Screen.JoinRequests.route)
                        onCloseDrawer()
                    }
                )

                Divider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = DividerColor
                )

                // Announcements Section
                Text(
                    text = "ANNOUNCEMENTS",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
                )

                DrawerItem(
                    icon = Icons.Default.Send,
                    label = "Send Announcement",
                    onClick = {
                        navController.navigate(Screen.SendAnnouncement.route)
                        onCloseDrawer()
                    }
                )

                DrawerItem(
                    icon = Icons.Default.List,
                    label = "Manage Announcements",
                    onClick = {
                        navController.navigate(Screen.ManageAnnouncements.route)
                        onCloseDrawer()
                    }
                )

                Divider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = DividerColor
                )

                // Visitor Management Section
                Text(
                    text = "VISITOR MANAGEMENT",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
                )

                DrawerItem(
                    icon = Icons.Default.People,
                    label = "All Visitors",
                    onClick = {
                        navController.navigate(Screen.AllVisitors.route)
                        onCloseDrawer()
                    }
                )
            }

            // ==================== WATCHMAN ROLE ITEMS ====================
            else if (userRole == "watchman") {
                // Visitor Management Section
                Text(
                    text = "VISITOR MANAGEMENT",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
                )

                DrawerItem(
                    icon = Icons.Default.AddCircle,
                    label = "Add Visitor",
                    onClick = {
                        navController.navigate(Screen.SelectFlat.route)
                        onCloseDrawer()
                    }
                )

                DrawerItem(
                    icon = Icons.Default.History,
                    label = "Visitor History",
                    onClick = {
                        navController.navigate(Screen.VisitorHistory.route)
                        onCloseDrawer()
                    }
                )

                DrawerItem(
                    icon = Icons.Default.CheckCircle,
                    label = "Pre-Approved List",
                    onClick = {
                        navController.navigate(Screen.PreApprovedList.route)
                        onCloseDrawer()
                    }
                )

                DrawerItem(
                    icon = Icons.Default.FilterList,
                    label = "Filter Visitors",
                    onClick = {
                        navController.navigate(Screen.FilterVisitors.route)
                        onCloseDrawer()
                    }
                )

                Divider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = DividerColor
                )

                // Emergency Section
                Text(
                    text = "EMERGENCY",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
                )

                DrawerItem(
                    icon = Icons.Default.Warning,
                    label = "Send Emergency Message",
                    onClick = {
                        navController.navigate(Screen.SendEmergencyMessage.route)
                        onCloseDrawer()
                    }
                )
            }

            // ==================== RESIDENT ROLE ITEMS ====================
            else if (userRole == "resident") {
                // Visitor Management Section
                Text(
                    text = "VISITOR MANAGEMENT",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
                )

                DrawerItem(
                    icon = Icons.Default.PersonAdd,
                    label = "Visitor Requests",
                    onClick = {
                        navController.navigate(Screen.VisitorRequests.route)
                        onCloseDrawer()
                    }
                )
                DrawerItem(
                    icon = Icons.Default.Approval,
                    label = "Visitor Approval",
                    onClick = {
                        navController.navigate(Screen.VisitorApproval.route)
                        onCloseDrawer()
                    }
                )

                DrawerItem(
                    icon = Icons.Default.History,
                    label = "My Visitors",
                    onClick = {
                        navController.navigate(Screen.VisitorHistoryResident.route)
                        onCloseDrawer()
                    }
                )

                DrawerItem(
                    icon = Icons.Default.FilterList,
                    label = "Filter Visitors",
                    onClick = {
                        navController.navigate(Screen.FilterVisitorsResident.route)
                        onCloseDrawer()
                    }
                )

                Divider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = DividerColor
                )

                // Guest Management Section
                Text(
                    text = "GUEST MANAGEMENT",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
                )

                DrawerItem(
                    icon = Icons.Default.PersonAdd,
                    label = "Pre-approve Guest",
                    onClick = {
                        navController.navigate(Screen.PreApproveGuest.route)
                        onCloseDrawer()
                    }
                )

                DrawerItem(
                    icon = Icons.Default.Group,
                    label = "Household Members",
                    onClick = {
                        navController.navigate(Screen.HouseholdMembers.route)
                        onCloseDrawer()
                    }
                )
            }

            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = DividerColor
            )

            // ==================== COMMUNITY SECTION (COMMON FOR ALL) ====================
            Text(
                text = "COMMUNITY",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
            )

            // Show different community options based on whether user has society
            if (userFlat.isBlank() || userFlat == "Gate Security" || userFlat == "N/A") {
                // User hasn't joined a society yet
                DrawerItem(
                    icon = Icons.Default.Search,
                    label = "Search Community",
                    onClick = {
                        navController.navigate(Screen.SearchCommunity.route)
                        onCloseDrawer()
                    }
                )
            }

            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = DividerColor
            )

            // ==================== SETTINGS AND HELP (COMMON FOR ALL) ====================
            Text(
                text = "SETTINGS & SUPPORT",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextSecondary,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 4.dp)
            )

            DrawerItem(
                icon = Icons.Default.Settings,
                label = "Settings",
                onClick = {
                    navController.navigate(Screen.Settings.route)
                    onCloseDrawer()
                }
            )

            DrawerItem(
                icon = Icons.Default.Help,
                label = "Help & Support",
                onClick = {
                    navController.navigate(Screen.HelpSupport.route)
                    onCloseDrawer()
                }
            )

            DrawerItem(
                icon = Icons.Default.Info,
                label = "About App",
                onClick = {
                    navController.navigate(Screen.AboutApp.route)
                    onCloseDrawer()
                }
            )

            DrawerItem(
                icon = Icons.Default.RateReview,
                label = "Feedback",
                onClick = {
                    navController.navigate(Screen.Feedback.route)
                    onCloseDrawer()
                }
            )
        }
// Logout Button at bottom - Navigate to LogoutScreen first
        DrawerItem(
            icon = Icons.Default.ExitToApp,
            label = "Logout",
            onClick = {
                onCloseDrawer()
                // Navigate to logout screen with animation
                navController.navigate(Screen.Logout.route) {
                    popUpTo(Screen.SecretaryHome.route) { inclusive = false }
                    launchSingleTop = true
                }
            },
            tint = Error
        )
        // Logout Button at bottom - FIXED VERSION
//        DrawerItem(
//            icon = Icons.Default.ExitToApp,
//            label = "Logout",
//            onClick = {
//                onCloseDrawer()
//                // Call signOut from ViewModel and navigate to SignIn
//                authViewModel.signOut {
//                    navController.navigate(Screen.SignIn.route) {
//                        popUpTo(0) { inclusive = true }
//                        launchSingleTop = true
//                    }
//                }
//            },
//            tint = Error
//        )
    }
}

@Composable
fun DrawerItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit,
    tint: Color = TextPrimary
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .safeClickable(onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = label,
            tint = tint,
            modifier = Modifier.size(22.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = label,
            fontSize = 15.sp,
            color = tint,
            modifier = Modifier.weight(1f)
        )
    }
}