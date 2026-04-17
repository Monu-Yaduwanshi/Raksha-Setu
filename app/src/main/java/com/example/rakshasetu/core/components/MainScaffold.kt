package com.example.rakshasetu.core.components

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.rakshasetu.core.navigation.Screen
import com.example.rakshasetu.core.utils.safeClickable
import com.example.rakshasetu.ui.theme.*
import kotlinx.coroutines.launch
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.example.rakshasetu.ui.bottomNavBar.BottomNavItem
import com.example.surakshasetu.ui.theme.DividerColor

// Hardcoded colors as requested
val BottomNavBackgroundColor = Color(0xFF007f52)  // #0046CC   0xFF0046CC
val BottomNavSelectedColor = Color(0xFF0046CC)   // #0CB381
//val BottomNavBackgroundColor = Color(0xFF0046CC)  // #0046CC
//val BottomNavSelectedColor = Color(0xFF0CB381)   // #0CB381
val BottomNavUnselectedColor = Color.White.copy(alpha = 0.7f)

// Top Bar Color - #5101FF (BlueMain)
val TopBarColor = BlueMain

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    navController: NavController,
    userRole: String?,
    userName: String,
    userFlat: String = "",
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    // Show bottom bar on all screens except auth screens
    val showBottomBar = currentRoute != Screen.SignIn.route &&
            currentRoute != Screen.SignUp.route &&
            currentRoute != Screen.Splash.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            // Drawer takes 70% of screen width
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(260.dp)
            ) {
                // Drawer content
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
                        // Common items for all users
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
                                scope.launch { drawerState.close() }
                            }
                        )

                        DrawerItem(
                            icon = Icons.Default.Warning,
                            label = "Emergency",
                            onClick = {
                                navController.navigate(Screen.Emergency.route)
                                scope.launch { drawerState.close() }
                            }
                        )

                        DrawerItem(
                            icon = Icons.Default.Announcement,
                            label = "Announcements",
                            onClick = {
                                navController.navigate(Screen.Announcements.route)
                                scope.launch { drawerState.close() }
                            }
                        )

                        DrawerItem(
                            icon = Icons.Default.Contacts,
                            label = "Directory",
                            onClick = {
                                navController.navigate(Screen.Directory.route)
                                scope.launch { drawerState.close() }
                            }
                        )

                        DrawerItem(
                            icon = Icons.Default.Person,
                            label = "Profile",
                            onClick = {
                                navController.navigate(Screen.Profile.route)
                                scope.launch { drawerState.close() }
                            }
                        )

                        Divider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = DividerColor
                        )

                        // Role-specific items
                        if (userRole == "secretary") {
                            DrawerItem(
                                icon = Icons.Default.PersonAdd,
                                label = "Add Resident",
                                onClick = {
                                    navController.navigate(Screen.CreateResident.route)
                                    scope.launch { drawerState.close() }
                                }
                            )

                            DrawerItem(
                                icon = Icons.Default.Security,
                                label = "Add Watchman",
                                onClick = {
                                    navController.navigate(Screen.CreateWatchman.route)
                                    scope.launch { drawerState.close() }
                                }
                            )

                            DrawerItem(
                                icon = Icons.Default.GroupAdd,
                                label = "Join Requests",
                                onClick = {
                                    navController.navigate(Screen.JoinRequests.route)
                                    scope.launch { drawerState.close() }
                                }
                            )
                        } else if (userRole == "watchman") {
                            DrawerItem(
                                icon = Icons.Default.AddCircle,
                                label = "Add Visitor",
                                onClick = {
                                    navController.navigate(Screen.AddVisitor.route)
                                    scope.launch { drawerState.close() }
                                }
                            )

                            DrawerItem(
                                icon = Icons.Default.History,
                                label = "Visitor History",
                                onClick = {
                                    navController.navigate(Screen.VisitorHistory.route)
                                    scope.launch { drawerState.close() }
                                }
                            )
                        } else if (userRole == "resident") {
                            DrawerItem(
                                icon = Icons.Default.PersonAdd,
                                label = "Pre-approve Guest",
                                onClick = {
                                    navController.navigate(Screen.PreApproveGuest.route)
                                    scope.launch { drawerState.close() }
                                }
                            )
                        }

                        Divider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = DividerColor
                        )

                        // Settings and Help
                        DrawerItem(
                            icon = Icons.Default.Settings,
                            label = "Settings",
                            onClick = {
                                navController.navigate(Screen.Settings.route)
                                scope.launch { drawerState.close() }
                            }
                        )

                        DrawerItem(
                            icon = Icons.Default.Help,
                            label = "Help & Support",
                            onClick = {
                                navController.navigate(Screen.HelpSupport.route)
                                scope.launch { drawerState.close() }
                            }
                        )

                        DrawerItem(
                            icon = Icons.Default.Info,
                            label = "About App",
                            onClick = {
                                navController.navigate(Screen.AboutApp.route)
                                scope.launch { drawerState.close() }
                            }
                        )

                        DrawerItem(
                            icon = Icons.Default.RateReview,
                            label = "Feedback",
                            onClick = {
                                navController.navigate(Screen.Feedback.route)
                                scope.launch { drawerState.close() }
                            }
                        )
                    }

                    // Logout Button at bottom
                    DrawerItem(
                        icon = Icons.Default.ExitToApp,
                        label = "Logout",
                        onClick = {
                            scope.launch { drawerState.close() }
                            // Navigate to sign in
                            navController.navigate(Screen.SignIn.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        tint = Error
                    )
                }
            }
        },
        gesturesEnabled = true
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = getScreenTitle(currentRoute, userRole),
                            color = Surface,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch { drawerState.open() }
                            }
                        ) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Surface
                            )
                        }
                    },
                    actions = {
                        // Notification Icon
                        IconButton(
                            onClick = {
                                // Navigate to notifications
                            }
                        ) {
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = Surface
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = TopBarColor
                    )
                )
            },
            bottomBar = {
                if (showBottomBar) {
                    BottomNavigationBar(
                        navController = navController,
                        currentRoute = currentRoute,
                        userRole = userRole
                    )
                }
            }
        ) { paddingValues ->
            content(paddingValues)
        }
    }
}

@Composable
fun getScreenTitle(route: String?, userRole: String?): String {
    // If it's a home screen, show app name
    if (route == Screen.SecretaryHome.route ||
        route == Screen.WatchmanHome.route ||
        route == Screen.ResidentHome.route) {
        return "Suraksha Setu"
    }

    return when (route) {
        Screen.Announcements.route -> "Announcements"
        Screen.Emergency.route -> "Emergency"
        Screen.Directory.route -> "Directory"
        Screen.Profile.route -> "Profile"
        Screen.Settings.route -> "Settings"
        Screen.HelpSupport.route -> "Help & Support"
        Screen.AboutApp.route -> "About App"
        Screen.Feedback.route -> "Feedback"
        Screen.CreateResident.route -> "Add Resident"
        Screen.CreateWatchman.route -> "Add Watchman"
        Screen.JoinRequests.route -> "Join Requests"
        Screen.AddVisitor.route -> "Add Visitor"
        Screen.VisitorHistory.route -> "Visitor History"
        Screen.PreApproveGuest.route -> "Pre-approve Guest"
        Screen.SearchCommunity.route -> "Search Community"
        Screen.JoinCommunity.route -> "Join Community"
        Screen.SignIn.route -> "Sign In"
        Screen.SignUp.route -> "Sign Up"
        else -> "Suraksha Setu"
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavController,
    currentRoute: String?,
    userRole: String?
) {
    NavigationBar(
        containerColor = BottomNavBackgroundColor,  // Hardcoded #0046CC
        tonalElevation = 0.dp
    ) {
        BottomNavItem.items.forEach { item ->
            // Determine if this item is selected
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
                            // Navigate to appropriate home based on role
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
                        tint = if (isSelected) BottomNavSelectedColor else BottomNavUnselectedColor  // #0CB381 when selected, white with alpha when not
                    )
                },
                label = {
                    Text(
                        text = item.title,
                        color = if (isSelected) BottomNavSelectedColor else BottomNavUnselectedColor,  // #0CB381 when selected
                        fontSize = 12.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = BottomNavSelectedColor,
                    selectedTextColor = BottomNavSelectedColor,
                    unselectedIconColor = BottomNavUnselectedColor,
                    unselectedTextColor = BottomNavUnselectedColor,
                    indicatorColor = Color.Transparent  // No indicator background
                )
            )
        }
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