package com.example.rakshasetu.ui.emergency


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
fun EmergencyScreen(
    navController: NavController,
    userRole: String? = null
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    // Sample emergency contacts
    val emergencyContacts = listOf(
        EmergencyContact("Police", "100", Icons.Default.LocalPolice, Color(0xFF2196F3)),
        EmergencyContact("Ambulance", "102", Icons.Default.LocalHospital, Color(0xFF4CAF50)),
        EmergencyContact("Fire", "101", Icons.Default.Fireplace, Color(0xFFFF5722)),
        EmergencyContact("Security", "+91 98765 4321", Icons.Default.Security, Color(0xFF9C27B0)),
        EmergencyContact("Disaster Management", "108", Icons.Default.Warning, Color(0xFFFF9800))
    )

    val societyContacts = listOf(
        EmergencyContact("Secretary", "+91 98765 4321", Icons.Default.Person, Primary),
        EmergencyContact("Maintenance", "+91 98765 4322", Icons.Default.Build, Secondary),
        EmergencyContact("Electrician", "+91 98765 4323", Icons.Default.ElectricBolt, TealMain)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Emergency",
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
            // SOS Button - Prominent at top
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Error
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Warning,
                        contentDescription = "SOS",
                        tint = Surface,
                        modifier = Modifier.size(48.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "SOS EMERGENCY",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Surface
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Tap to alert all members",
                        fontSize = 14.sp,
                        color = Surface.copy(alpha = 0.9f)
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Emergency Services Section
                item {
                    Text(
                        text = "Emergency Services",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                items(emergencyContacts) { contact ->
                    EmergencyContactCard(contact)
                }

                // Society Contacts Section
                item {
                    Text(
                        text = "Society Contacts",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                items(societyContacts) { contact ->
                    EmergencyContactCard(contact)
                }

                // Bottom padding
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun EmergencyContactCard(contact: EmergencyContact) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Surface
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(contact.color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    contact.icon,
                    contentDescription = null,
                    tint = contact.color,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = contact.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = contact.number,
                    fontSize = 14.sp,
                    color = TextSecondary
                )
            }

            Button(
                onClick = { /* Make call */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = contact.color
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Call")
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

data class EmergencyContact(
    val name: String,
    val number: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val color: Color
)