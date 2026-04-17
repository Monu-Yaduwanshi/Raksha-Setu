package com.example.rakshasetu.ui.secretary

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.rakshasetu.core.components.BottomNavigationBar
import com.example.rakshasetu.core.navigation.Screen
import com.example.rakshasetu.ui.drawer.DrawerContent

import com.example.rakshasetu.ui.theme.*
import com.example.rakshasetu.viewModel.viewModel.auth.AuthViewModel
import kotlinx.coroutines.launch

val HomeTopBarColor = Color(0xFFD22030)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecretaryHomeScreen(
    navController: NavController,
    userRole: String? = "secretary"
) {
    val secretaryViewModel: SecretaryViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()
    val context = LocalContext.current
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val isLoading by secretaryViewModel.isLoading.collectAsState()
    val society by secretaryViewModel.society.collectAsState()
    val residents by secretaryViewModel.residents.collectAsState()
    val watchmen by secretaryViewModel.watchmen.collectAsState()
    val joinRequests by secretaryViewModel.joinRequests.collectAsState()
    val userData by authViewModel.userData.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    LaunchedEffect(userData) {
        val societyId = userData?.societyId
        if (!societyId.isNullOrEmpty()) {
            secretaryViewModel.loadSociety(societyId)
        }
    }
//    // Load society data when user has a society
//    LaunchedEffect(userData?.societyId) {
//        if (!userData?.societyId.isNullOrEmpty()) {
//            secretaryViewModel.loadSociety(userData!!.societyId)
//        }
//    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(300.dp)
                    .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
            ) {
                DrawerContent(
                    navController = navController,
                    userRole = userData?.role,
                    userName = userData?.fullName ?: "Secretary",
                    userFlat = society?.societyName ?: "",
                    onCloseDrawer = { scope.launch { drawerState.close() } }
                )
            }
        },
        gesturesEnabled = true
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.AdminPanelSettings,
                                    contentDescription = "Logo",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Raksha Setu",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = { scope.launch { drawerState.open() } }
                        ) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        BadgedBox(
                            badge = {
                                if (joinRequests.isNotEmpty()) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(Color.Red)
                                    )
                                }
                            }
                        ) {
                            IconButton(
                                onClick = { navController.navigate(Screen.Notifications.route) }
                            ) {
                                Icon(
                                    Icons.Default.Notifications,
                                    contentDescription = "Notifications",
                                    tint = Color.White
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = HomeTopBarColor
                    )
                )
            },
            bottomBar = {
                BottomNavigationBar(
                    navController = navController,
                    currentRoute = currentRoute,
                    userRole = userData?.role
                )
            }
        ) { paddingValues ->
            if (userData?.societyId.isNullOrEmpty()) {
                // No society created yet
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Background)
                        .padding(paddingValues)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Business,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = Primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No Society Found",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = "Create your society to get started",
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { navController.navigate(Screen.CreateSociety.route) },
                        colors = ButtonDefaults.buttonColors(containerColor = Primary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Create Society")
                    }
                }
            } else if (society == null && isLoading) {
                // Loading
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Primary)
                }
            } else if (society == null) {
                // Society not loaded
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Background)
                        .padding(paddingValues)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Error,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = Warning
                    )
                    Text(
                        text = "Failed to load society data",
                        fontSize = 16.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            userData?.societyId?.let { secretaryViewModel.loadSociety(it) }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Primary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Retry")
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Background)
                        .padding(paddingValues)
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Welcome Card
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Surface),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(20.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Welcome back,",
                                        fontSize = 14.sp,
                                        color = TextSecondary
                                    )
                                    Text(
                                        text = userData?.fullName?.split(" ")?.firstOrNull() ?: "Secretary",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Text(
                                        text = society!!.societyName,
                                        fontSize = 14.sp,
                                        color = TextSecondary
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Primary.copy(alpha = 0.1f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.AdminPanelSettings,
                                        contentDescription = null,
                                        tint = Primary,
                                        modifier = Modifier.size(32.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Stats Cards
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            StatsCard(
                                title = "Residents",
                                value = residents.size.toString(),
                                icon = Icons.Default.People,
                                color = Primary,
                                modifier = Modifier.weight(1f)
                            )
                            StatsCard(
                                title = "Watchmen",
                                value = watchmen.size.toString(),
                                icon = Icons.Default.Security,
                                color = Secondary,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            StatsCard(
                                title = "Flats",
                                value = society!!.totalFlats.toString(),
                                icon = Icons.Default.Apartment,
                                color = TealMain,
                                modifier = Modifier.weight(1f)
                            )
                            StatsCard(
                                title = "Requests",
                                value = joinRequests.size.toString(),
                                icon = Icons.Default.GroupAdd,
                                color = Warning,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    // Quick Actions Title
                    item {
                        Text(
                            text = "Quick Actions",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    // Quick Actions
                    item {
                        ActionCard(
                            title = "Add Resident",
                            description = "Register new resident",
                            icon = Icons.Default.PersonAdd,
                            color = Primary,
                            onClick = { navController.navigate(Screen.CreateResident.route) }
                        )
                    }

                    item {
                        ActionCard(
                            title = "Add Watchman",
                            description = "Register security guard",
                            icon = Icons.Default.Security,
                            color = Secondary,
                            onClick = { navController.navigate(Screen.CreateWatchman.route) }
                        )
                    }

                    item {
                        ActionCard(
                            title = "Join Requests",
                            description = "Approve new members",
                            icon = Icons.Default.GroupAdd,
                            color = TealMain,
                            badge = if (joinRequests.isNotEmpty()) joinRequests.size.toString() else null,
                            onClick = { navController.navigate(Screen.JoinRequests.route) }
                        )
                    }

                    item {
                        ActionCard(
                            title = "Send Announcement",
                            description = "Notify all members",
                            icon = Icons.Default.Send,
                            color = Success,
                            onClick = { navController.navigate(Screen.SendAnnouncement.route) }
                        )
                    }

                    item {
                        ActionCard(
                            title = "Manage Society",
                            description = "View and manage society details",
                            icon = Icons.Default.Settings,
                            color = TealMain,
                            onClick = { navController.navigate(Screen.ManageSociety.route) }
                        )
                    }

                    // Bottom Padding
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun StatsCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(title, fontSize = 12.sp, color = TextSecondary)
            }
        }
    }
}

@Composable
fun ActionCard(
    title: String,
    description: String,
    icon: ImageVector,
    color: Color,
    badge: String? = null,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = title, tint = color, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                Text(description, fontSize = 14.sp, color = TextSecondary)
            }
            if (badge != null) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(color),
                    contentAlignment = Alignment.Center
                ) {
                    Text(badge, fontSize = 12.sp, color = Surface, fontWeight = FontWeight.Bold)
                }
            } else {
                Icon(Icons.Default.ChevronRight, contentDescription = "Navigate", tint = TextHint)
            }
        }
    }
}