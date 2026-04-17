package com.example.rakshasetu.ui.watchMan

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.rakshasetu.core.components.BottomNavigationBar
import com.example.rakshasetu.core.navigation.Screen
import com.example.rakshasetu.core.utils.safeClickable
import com.example.rakshasetu.ui.drawer.DrawerContent
import com.example.rakshasetu.ui.theme.*
import com.example.rakshasetu.viewModel.viewModel.auth.AuthViewModel
import com.example.rakshasetu.viewModel.viewModel.watchman.VisitorItem
import com.example.rakshasetu.viewModel.viewModel.watchman.WatchmanViewModel
import kotlinx.coroutines.launch

val HomeTopBarColor = Color(0xFFD22030)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchmanHomeScreen(
    navController: NavController,
    viewModel: WatchmanViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val isLoading by viewModel.isLoading.collectAsState()
    val todayCount by viewModel.todayVisitorsCount.collectAsState()
    val pendingCount by viewModel.pendingVisitorsCount.collectAsState()
    val recentVisitors by viewModel.recentVisitors.collectAsState()
    val userName by viewModel.userName.collectAsState()
    val societyName by viewModel.societyName.collectAsState()
    val userData by authViewModel.userData.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Initialize view model with society ID
    LaunchedEffect(userData?.societyId) {
        userData?.societyId?.let {
            viewModel.initialize(it)
        }
    }

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
                    userName = userData?.fullName ?: userName,
                    userFlat = "Gate Security",
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
                                    Icons.Default.Security,
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
                                if (pendingCount > 0) {
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Background)
                    .padding(paddingValues)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Welcome Card (no clickable - keep as is)
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
                                    text = "Welcome,",
                                    fontSize = 14.sp,
                                    color = TextSecondary
                                )
                                Text(
                                    text = userData?.fullName?.split(" ")?.firstOrNull() ?: userName,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = userData?.societyName ?: societyName,
                                    fontSize = 14.sp,
                                    color = TextSecondary
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Secondary.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Security,
                                    contentDescription = null,
                                    tint = Secondary,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                }

                // Add Visitor Button (Button - keep as is, no change needed)
                item {
                    Button(
                        onClick = { navController.navigate(Screen.SelectFlat.route) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ButtonPrimary),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.AddCircle, contentDescription = null, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("ADD NEW VISITOR", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Stats Cards (no clickable - keep as is)
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = Surface),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(16.dp)
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Primary)
                                } else {
                                    Text(todayCount.toString(), fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Primary)
                                }
                                Text("Today's Visitors", fontSize = 14.sp, color = TextSecondary)
                            }
                        }

                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(containerColor = Surface),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(16.dp)
                            ) {
                                if (isLoading) {
                                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Warning)
                                } else {
                                    Text(pendingCount.toString(), fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Warning)
                                }
                                Text("Pending", fontSize = 14.sp, color = TextSecondary)
                            }
                        }
                    }
                }

                // Quick Actions
                item {
                    Text(
                        text = "Quick Actions",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                // ✅ FIXED: ActionCard without onClick parameter, using safeClickable modifier
                item {
                    ActionCard(
                        title = "Pre-Approved List",
                        description = "View pre-approved guests",
                        icon = Icons.Default.CheckCircle,
                        color = Success,
                        onClick = { navController.navigate(Screen.PreApprovedList.route) }
                    )
                }

                item {
                    ActionCard(
                        title = "Send Emergency Alert",
                        description = "Alert all society members",
                        icon = Icons.Default.Warning,
                        color = Error,
                        onClick = { navController.navigate(Screen.SendEmergencyMessage.route) }
                    )
                }

                // Recent Visitors Title
                item {
                    Text(
                        text = "Recent Visitors",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                // Recent Visitors List
                if (isLoading && recentVisitors.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth().height(100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Primary)
                        }
                    }
                } else if (recentVisitors.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Surface),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "No visitors today",
                                modifier = Modifier.padding(16.dp),
                                color = TextSecondary,
                                fontSize = 14.sp
                            )
                        }
                    }
                } else {
                    items(recentVisitors) { visitor ->
                        // ✅ FIXED: Using safeClickable in VisitorCard
                        VisitorCard(visitor = visitor, onClick = {
                            // Navigate to visitor details when implemented
                        })
                    }
                }

                // View All Button
                item {
                    TextButton(
                        onClick = { navController.navigate(Screen.VisitorHistory.route) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("View All History", color = Primary, fontWeight = FontWeight.Medium)
                    }
                }

                // Bottom Padding
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

// ✅ FIXED: ActionCard without Card(onClick = {})
@Composable
fun ActionCard(
    title: String,
    description: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .safeClickable(onClick = onClick),  // ✅ Using safeClickable instead of onClick parameter
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
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
            Icon(Icons.Default.ChevronRight, contentDescription = "Navigate", tint = TextHint)
        }
    }
}

// ✅ FIXED: VisitorCard with safeClickable instead of clickable
@Composable
fun VisitorCard(visitor: VisitorItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .safeClickable(onClick = onClick),  // ✅ Using safeClickable
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Secondary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = Secondary, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(visitor.name, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = TextPrimary)
                Text("${visitor.flat} • ${visitor.time}", fontSize = 12.sp, color = TextSecondary)
            }
            StatusChip(status = visitor.status)
        }
    }
}