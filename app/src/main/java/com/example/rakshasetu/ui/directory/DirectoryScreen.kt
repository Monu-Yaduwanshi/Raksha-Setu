package com.example.rakshasetu.ui.directory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import com.example.rakshasetu.ui.bottomNavBar.BottomNavItem
import com.example.rakshasetu.ui.theme.*

// Top Bar Color - Hardcoded #0046CC
val TopBarColor = Color(0xFFD22030)
// Bottom Navigation Color
val BottomNavBackgroundColor = Color(0xFF007F50)
val BottomNavSelectedColor = Color(0xFF0046CC)
val BottomNavUnselectedColor = Color.White.copy(alpha = 0.7f)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DirectoryScreen(
    navController: NavController,
    userRole: String? = null
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    var selectedTab by remember { mutableStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }

    val tabs = listOf("Residents", "Watchmen", "Emergency")

    // Sample data
    val residents = listOf(
        DirectoryItem("Rahul Sharma", "A-101", "+91 98765 4321", "Owner", Primary),
        DirectoryItem("Priya Singh", "A-102", "+91 98765 4322", "Tenant", Primary),
        DirectoryItem("Amit Kumar", "B-201", "+91 98765 4323", "Owner", Primary),
        DirectoryItem("Neha Gupta", "B-202", "+91 98765 4324", "Owner", Primary),
        DirectoryItem("Rajesh Verma", "C-301", "+91 98765 4325", "Tenant", Primary),
        DirectoryItem("Sunita Patel", "C-302", "+91 98765 4326", "Owner", Primary)
    )

    val watchmen = listOf(
        DirectoryItem("Vikram Singh", "Morning Shift", "+91 98765 4321", "Gate 1", Secondary),
        DirectoryItem("Raj Kumar", "Morning Shift", "+91 98765 4322", "Gate 2", Secondary),
        DirectoryItem("Manoj Yadav", "Evening Shift", "+91 98765 4323", "Gate 1", Secondary),
        DirectoryItem("Suresh Rai", "Evening Shift", "+91 98765 4324", "Gate 2", Secondary),
        DirectoryItem("Dinesh Patel", "Night Shift", "+91 98765 4325", "Gate 1", Secondary)
    )

    val emergency = listOf(
        DirectoryItem("Police", "100", "Emergency", "24x7", Color(0xFF2196F3)),
        DirectoryItem("Ambulance", "102", "Emergency", "24x7", Color(0xFF4CAF50)),
        DirectoryItem("Fire", "101", "Emergency", "24x7", Color(0xFFFF5722)),
        DirectoryItem("Security", "+91 98765 4300", "Society", "24x7", Color(0xFF9C27B0)),
        DirectoryItem("Secretary", "+91 98765 4301", "Office Hours", "9 AM - 6 PM", Primary),
        DirectoryItem("Maintenance", "+91 98765 4302", "Office Hours", "9 AM - 6 PM", Secondary)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Directory",
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
                    containerColor = TopBarColor  // Hardcoded #0046CC
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(paddingValues)
        ) {
            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search in directory...", color = TextHint) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Primary
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Surface,
                    unfocusedContainerColor = Surface,
                    focusedBorderColor = Primary,
                    unfocusedBorderColor = TextHint,
                    cursorColor = Primary
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            // Tabs
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                containerColor = Surface,
                edgePadding = 0.dp,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = Primary
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                color = if (selectedTab == index) Primary else TextSecondary,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            // Content
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val currentList = when (selectedTab) {
                    0 -> residents
                    1 -> watchmen
                    else -> emergency
                }

                val filteredList = if (searchQuery.isBlank()) {
                    currentList
                } else {
                    currentList.filter {
                        it.name.contains(searchQuery, ignoreCase = true) ||
                                it.detail.contains(searchQuery, ignoreCase = true)
                    }
                }

                items(filteredList) { item ->
                    DirectoryItemCard(item)
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

data class DirectoryItem(
    val name: String,
    val detail: String,
    val phone: String,
    val role: String,
    val color: Color
)

@Composable
fun DirectoryItemCard(item: DirectoryItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Surface
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(item.color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.name.first().toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = item.color
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = item.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = item.detail,
                    fontSize = 14.sp,
                    color = TextSecondary
                )
                Text(
                    text = item.role,
                    fontSize = 12.sp,
                    color = TextHint
                )
            }

            IconButton(
                onClick = { /* Make call */ }
            ) {
                Icon(
                    Icons.Default.Call,
                    contentDescription = "Call",
                    tint = Primary
                )
            }
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