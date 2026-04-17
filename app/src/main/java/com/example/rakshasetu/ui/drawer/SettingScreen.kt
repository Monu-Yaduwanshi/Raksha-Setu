package com.example.rakshasetu.ui.drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.rakshasetu.core.components.BottomNavigationBar
import com.example.rakshasetu.ui.theme.*

// Top Bar Color - #007F50 (ForestGreen)
val SettingsTopBarColor = ForestGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    userRole: String? = null
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
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
                    containerColor = SettingsTopBarColor  // #007F50
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                SettingsSection("General Settings")
            }

            item {
                SettingsOption(
                    icon = Icons.Default.Notifications,
                    title = "Notifications",
                    description = "Manage notification preferences",
                    onClick = { /* Navigate to notification settings */ }
                )
            }

            item {
                SettingsOption(
                    icon = Icons.Default.Lock,
                    title = "Privacy",
                    description = "Manage privacy settings",
                    onClick = { /* Navigate to privacy settings */ }
                )
            }

            item {
                SettingsOption(
                    icon = Icons.Default.Language,
                    title = "Language",
                    description = "English (Selected)",
                    onClick = { /* Navigate to language settings */ }
                )
            }

            item {
                SettingsOption(
                    icon = Icons.Default.DarkMode,
                    title = "Theme",
                    description = "Light / Dark mode",
                    onClick = { /* Navigate to theme settings */ }
                )
            }

            item {
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }

            item {
                SettingsSection("App Settings")
            }

            item {
                SettingsOption(
                    icon = Icons.Default.Storage,
                    title = "Storage",
                    description = "Manage app storage",
                    onClick = { /* Navigate to storage settings */ }
                )
            }

            item {
                SettingsOption(
                    icon = Icons.Default.Update,
                    title = "Auto-update",
                    description = "Check for updates automatically",
                    onClick = { /* Navigate to update settings */ }
                )
            }

            item {
                SettingsOption(
                    icon = Icons.Default.DataUsage,
                    title = "Data Usage",
                    description = "Manage data usage",
                    onClick = { /* Navigate to data usage settings */ }
                )
            }
        }
    }
}

@Composable
fun SettingsSection(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = TextPrimary,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun SettingsOption(
    icon: ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Surface
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = TextSecondary
                )
            }

            Icon(
                Icons.Default.ChevronRight,
                contentDescription = "Navigate",
                tint = TextHint
            )
        }
    }
}