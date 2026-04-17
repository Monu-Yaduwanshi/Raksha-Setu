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
val HelpTopBarColor = ForestGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpSupportScreen(
    navController: NavController,
    userRole: String? = null
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Help & Support",
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
                    containerColor = HelpTopBarColor  // #007F50
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
                HelpOption(
                    icon = Icons.Default.QuestionAnswer,
                    title = "FAQ",
                    description = "Frequently asked questions",
                    onClick = { /* Navigate to FAQ */ }
                )
            }

            item {
                HelpOption(
                    icon = Icons.Default.ContactSupport,
                    title = "Contact Support",
                    description = "Get help from our support team",
                    onClick = { /* Navigate to contact support */ }
                )
            }

            item {
                HelpOption(
                    icon = Icons.Default.VideoLibrary,
                    title = "Video Tutorials",
                    description = "Watch tutorial videos",
                    onClick = { /* Navigate to tutorials */ }
                )
            }

            item {
                HelpOption(
                    icon = Icons.Default.Description,
                    title = "User Manual",
                    description = "Read the user manual",
                    onClick = { /* Navigate to user manual */ }
                )
            }

            item {
                HelpOption(
                    icon = Icons.Default.Report,
                    title = "Report a Problem",
                    description = "Report technical issues",
                    onClick = { /* Navigate to report problem */ }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Surface
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Contact Information",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        ContactInfoRow("Email", "support@rakshasetu.com", Icons.Default.Email)
                        ContactInfoRow("Phone", "+91 7828454802", Icons.Default.Phone)
                        ContactInfoRow("Website", "www.rakshasetu.com", Icons.Default.Language)
                    }
                }
            }
        }
    }
}

@Composable
fun HelpOption(
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
                    .background(Secondary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = Secondary,
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

@Composable
fun ContactInfoRow(label: String, value: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = Primary,
            modifier = Modifier.size(16.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "$label:",
            fontSize = 14.sp,
            color = TextSecondary,
            modifier = Modifier.width(70.dp)
        )

        Text(
            text = value,
            fontSize = 14.sp,
            color = TextPrimary,
            fontWeight = FontWeight.Medium
        )
    }
}