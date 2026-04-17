package com.example.rakshasetu.ui.resident


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import com.example.rakshasetu.data.models.Visitor
import com.example.rakshasetu.ui.drawer.DrawerContent

import com.example.rakshasetu.ui.theme.*
import com.example.rakshasetu.viewModel.viewModel.auth.AuthViewModel
//import com.example.rakshasetu.viewModel.viewModel.resident.ResidentViewModel
//import com.example.rakshasetu.viewModel.viewModel.resident.VisitorRequestItem

import kotlinx.coroutines.launch





import android.widget.Toast
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource

import com.google.firebase.auth.FirebaseAuth

import kotlinx.coroutines.launch

import androidx.compose.ui.layout.ContentScale

import coil.compose.AsyncImage


val HomeTopBarColor = Color(0xFFD22030)
val SecretaryTopBarColor = Color(0xFF0046CC)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResidentHomeScreen(
    navController: NavController,
    viewModel: ResidentViewModel = viewModel(),
    authViewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val isLoading by viewModel.isLoading.collectAsState()
    val pendingVisitors by viewModel.pendingVisitors.collectAsState()
    val userName by viewModel.userName.collectAsState()
    val flatNumber by viewModel.flatNumber.collectAsState()
    val userData by authViewModel.userData.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val currentUser = FirebaseAuth.getInstance().currentUser

    // Debug logging
    LaunchedEffect(Unit) {
        val userId = currentUser?.uid
        Log.d("RESIDENT_DEBUG", "=== ResidentHomeScreen Debug ===")
        Log.d("RESIDENT_DEBUG", "Current User UID: $userId")
        if (userId != null) {
            viewModel.loadPendingVisitors(userId)
        }
    }

    LaunchedEffect(pendingVisitors) {
        Log.d("RESIDENT_DEBUG", "Pending visitors updated: ${pendingVisitors.size}")
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
                    userFlat = "Flat ${userData?.flatNumber ?: flatNumber}",
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
                                    Icons.Default.Home,
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
                                if (pendingVisitors.isNotEmpty()) {
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
                                    text = "Welcome home,",
                                    fontSize = 14.sp,
                                    color = TextSecondary
                                )
                                Text(
                                    text = userName.split(" ").firstOrNull() ?: "Resident",
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Flat ${userData?.flatNumber ?: flatNumber}",
                                    fontSize = 14.sp,
                                    color = TextSecondary
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(TealMain.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Home,
                                    contentDescription = null,
                                    tint = TealMain,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                }

                // Pending Approvals Section
                if (pendingVisitors.isNotEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Warning.copy(alpha = 0.1f)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Pending Approvals",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(Warning),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = pendingVisitors.size.toString(),
                                            fontSize = 12.sp,
                                            color = Surface,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                pendingVisitors.take(3).forEach { visitor ->
                                    PendingRequestCard(
                                        visitor = visitor,
                                        onClick = {
                                            navController.navigate(Screen.VisitorApproval.createRoute(visitor.id))
                                        }
                                    )
                                }
                                if (pendingVisitors.size > 3) {
                                    TextButton(
                                        onClick = { navController.navigate(Screen.VisitorRequests.route) },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("View All (${pendingVisitors.size})", color = Primary)
                                    }
                                }
                            }
                        }
                    }
                } else {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Surface),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Success,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "No pending requests",
                                    fontSize = 14.sp,
                                    color = TextSecondary
                                )
                            }
                        }
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
                    ResidentActionCard(
                        title = "Visitor Requests",
                        description = "View all pending requests",
                        icon = Icons.Default.Notifications,
                        color = Primary,
                        badge = if (pendingVisitors.isNotEmpty()) pendingVisitors.size.toString() else null,
                        onClick = { navController.navigate(Screen.VisitorRequests.route) }
                    )
                }

                item {
                    ResidentActionCard(
                        title = "Pre-approve Guest",
                        description = "Allow guests without request",
                        icon = Icons.Default.PersonAdd,
                        color = Success,
                        onClick = { navController.navigate(Screen.PreApproveGuest.route) }
                    )
                }

                item {
                    ResidentActionCard(
                        title = "My Visitors",
                        description = "View visitor history",
                        icon = Icons.Default.History,
                        color = TealMain,
                        onClick = { navController.navigate(Screen.VisitorHistoryResident.route) }
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun PendingRequestCard(
    visitor: Visitor,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = LocalIndication.current,
                interactionSource = interactionSource
            ) { onClick() }
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ✅ FIXED: Show actual visitor photo using AsyncImage
            if (visitor.visitorPhotoUrl.isNotEmpty()) {
                AsyncImage(
                    model = visitor.visitorPhotoUrl,
                    contentDescription = "Visitor Photo",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Fallback icon when no photo URL
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        when (visitor.purpose.lowercase()) {
                            "delivery" -> Icons.Default.LocalShipping
                            "guest" -> Icons.Default.Person
                            "cab" -> Icons.Default.TaxiAlert
                            else -> Icons.Default.Person
                        },
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = visitor.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary
                )
                Text(
                    text = "${visitor.purpose} • ${visitor.flatNumber}",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
                visitor.entryTime?.let {
                    Text(
                        text = "Arrived: ${formatTime(it)}",
                        fontSize = 10.sp,
                        color = TextHint
                    )
                }
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(Warning.copy(alpha = 0.2f))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "Pending",
                    fontSize = 10.sp,
                    color = Warning,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun ResidentActionCard(
    title: String,
    description: String,
    icon: ImageVector,
    color: Color,
    badge: String? = null,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    indication = LocalIndication.current,
                    interactionSource = interactionSource
                ) { onClick() }
                .padding(16.dp),
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

// Helper function for formatting time
fun formatTime(timestamp: com.google.firebase.Timestamp?): String {
    if (timestamp == null) return ""
    val format = java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault())
    return format.format(timestamp.toDate())
}
//val HomeTopBarColor = Color(0xFF0046CC)
//val SecretaryTopBarColor = Color(0xFF0CB381)
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ResidentHomeScreen(
//    navController: NavController,
//    viewModel: ResidentViewModel = viewModel(),
//    authViewModel: AuthViewModel = viewModel()
//) {
//    val context = LocalContext.current
//    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
//    val isLoading by viewModel.isLoading.collectAsState()
//    val pendingVisitors by viewModel.pendingVisitors.collectAsState()
//    val userName by viewModel.userName.collectAsState()
//    val flatNumber by viewModel.flatNumber.collectAsState()
//    val userData by authViewModel.userData.collectAsState()
//
//    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
//    val scope = rememberCoroutineScope()
//
//    val currentUser = FirebaseAuth.getInstance().currentUser
//
//    // ✅ DEBUG: Log resident ID for verification
//    LaunchedEffect(Unit) {
//        val userId = currentUser?.uid
//        Log.d("RESIDENT_DEBUG", "=== ResidentHomeScreen Debug ===")
//        Log.d("RESIDENT_DEBUG", "Current User UID: $userId")
//        Log.d("RESIDENT_DEBUG", "Current User Email: ${currentUser?.email}")
//
//        // ✅ STEP 4: Check if UID matches Firestore
//        if (userId != null) {
//            Log.d("RESIDENT_DEBUG", "✅ User is logged in with UID: $userId")
//            Log.d("RESIDENT_DEBUG", "⚠️ Make sure Firestore visitor has residentId = $userId")
//
//            // ✅ Load pending visitors with correct residentId
//            viewModel.loadPendingVisitors(userId)
//        } else {
//            Log.e("RESIDENT_DEBUG", "❌ No user logged in!")
//            Toast.makeText(context, "Please login again", Toast.LENGTH_LONG).show()
//        }
//    }
//
//    // ✅ Log when pending visitors change
//    LaunchedEffect(pendingVisitors) {
//        Log.d("RESIDENT_DEBUG", "=========================================")
//        Log.d("RESIDENT_DEBUG", "Pending visitors updated: ${pendingVisitors.size} requests")
//        if (pendingVisitors.isEmpty()) {
//            Log.d("RESIDENT_DEBUG", "⚠️ No pending requests found!")
//            Log.d("RESIDENT_DEBUG", "Check:")
//            Log.d("RESIDENT_DEBUG", "  1. Is residentId correct in Firestore?")
//            Log.d("RESIDENT_DEBUG", "  2. Is status = 'pending'?")
//            Log.d("RESIDENT_DEBUG", "  3. Is collection name 'visitors'?")
//        } else {
//            pendingVisitors.forEach { visitor ->
//                Log.d("RESIDENT_DEBUG", "  ✅ ${visitor.name} (${visitor.purpose}) from flat ${visitor.flatNumber}")
//            }
//        }
//        Log.d("RESIDENT_DEBUG", "=========================================")
//    }
//
//    ModalNavigationDrawer(
//        drawerState = drawerState,
//        drawerContent = {
//            Box(
//                modifier = Modifier
//                    .fillMaxHeight()
//                    .width(300.dp)
//                    .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
//            ) {
//                DrawerContent(
//                    navController = navController,
//                    userRole = userData?.role,
//                    userName = userData?.fullName ?: userName,
//                    userFlat = "Flat ${userData?.flatNumber ?: flatNumber}",
//                    onCloseDrawer = { scope.launch { drawerState.close() } }
//                )
//            }
//        },
//        gesturesEnabled = true
//    ) {
//        Scaffold(
//            topBar = {
//                TopAppBar(
//                    title = {
//                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
//                            modifier = Modifier.fillMaxWidth()
//                        ) {
//                            Box(
//                                modifier = Modifier
//                                    .size(32.dp)
//                                    .clip(CircleShape)
//                                    .background(Color.White.copy(alpha = 0.2f)),
//                                contentAlignment = Alignment.Center
//                            ) {
//                                Icon(
//                                    Icons.Default.Home,
//                                    contentDescription = "Logo",
//                                    tint = Color.White,
//                                    modifier = Modifier.size(20.dp)
//                                )
//                            }
//                            Spacer(modifier = Modifier.width(8.dp))
//                            Text(
//                                text = "Suraksha Setu",
//                                color = Color.White,
//                                fontSize = 20.sp,
//                                fontWeight = FontWeight.Bold
//                            )
//                        }
//                    },
//                    navigationIcon = {
//                        IconButton(
//                            onClick = { scope.launch { drawerState.open() } }
//                        ) {
//                            Icon(
//                                Icons.Default.Menu,
//                                contentDescription = "Menu",
//                                tint = Color.White
//                            )
//                        }
//                    },
//                    actions = {
//                        BadgedBox(
//                            badge = {
//                                if (pendingVisitors.isNotEmpty()) {
//                                    Box(
//                                        modifier = Modifier
//                                            .size(8.dp)
//                                            .clip(CircleShape)
//                                            .background(Color.Red)
//                                    )
//                                }
//                            }
//                        ) {
//                            IconButton(
//                                onClick = { navController.navigate(Screen.Notifications.route) }
//                            ) {
//                                Icon(
//                                    Icons.Default.Notifications,
//                                    contentDescription = "Notifications",
//                                    tint = Color.White
//                                )
//                            }
//                        }
//                    },
//                    colors = TopAppBarDefaults.topAppBarColors(
//                        containerColor = HomeTopBarColor
//                    )
//                )
//            },
//            bottomBar = {
//                BottomNavigationBar(
//                    navController = navController,
//                    currentRoute = currentRoute,
//                    userRole = userData?.role
//                )
//            }
//        ) { paddingValues ->
//            LazyColumn(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .background(Background)
//                    .padding(paddingValues)
//                    .padding(16.dp),
//                verticalArrangement = Arrangement.spacedBy(16.dp)
//            ) {
//                // Welcome Card
//                item {
//                    Card(
//                        modifier = Modifier.fillMaxWidth(),
//                        colors = CardDefaults.cardColors(containerColor = Surface),
//                        shape = RoundedCornerShape(16.dp),
//                        elevation = CardDefaults.cardElevation(2.dp)
//                    ) {
//                        Row(
//                            modifier = Modifier.fillMaxWidth().padding(20.dp),
//                            verticalAlignment = Alignment.CenterVertically
//                        ) {
//                            Column(modifier = Modifier.weight(1f)) {
//                                Text(
//                                    text = "Welcome home,",
//                                    fontSize = 14.sp,
//                                    color = TextSecondary
//                                )
//                                Text(
//                                    text = userName.split(" ").firstOrNull() ?: "Resident",
//                                    fontSize = 24.sp,
//                                    fontWeight = FontWeight.Bold,
//                                    color = TextPrimary
//                                )
//                                Text(
//                                    text = "Flat ${userData?.flatNumber ?: flatNumber}",
//                                    fontSize = 14.sp,
//                                    color = TextSecondary
//                                )
//                                // ✅ Show UID for debugging (remove in production)
//                                Text(
//                                    text = "UID: ${currentUser?.uid?.take(12)}...",
//                                    fontSize = 9.sp,
//                                    color = TextHint
//                                )
//                            }
//                            Box(
//                                modifier = Modifier
//                                    .size(60.dp)
//                                    .clip(RoundedCornerShape(12.dp))
//                                    .background(TealMain.copy(alpha = 0.1f)),
//                                contentAlignment = Alignment.Center
//                            ) {
//                                Icon(
//                                    Icons.Default.Home,
//                                    contentDescription = null,
//                                    tint = TealMain,
//                                    modifier = Modifier.size(32.dp)
//                                )
//                            }
//                        }
//                    }
//                }
//
//                // Pending Approvals Section
//                if (pendingVisitors.isNotEmpty()) {
//                    item {
//                        Card(
//                            modifier = Modifier.fillMaxWidth(),
//                            colors = CardDefaults.cardColors(containerColor = Warning.copy(alpha = 0.1f)),
//                            shape = RoundedCornerShape(12.dp)
//                        ) {
//                            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
//                                Row(
//                                    modifier = Modifier.fillMaxWidth(),
//                                    horizontalArrangement = Arrangement.SpaceBetween,
//                                    verticalAlignment = Alignment.CenterVertically
//                                ) {
//                                    Text(
//                                        text = "Pending Approvals",
//                                        fontSize = 16.sp,
//                                        fontWeight = FontWeight.Bold,
//                                        color = TextPrimary
//                                    )
//                                    Box(
//                                        modifier = Modifier
//                                            .size(24.dp)
//                                            .clip(RoundedCornerShape(12.dp))
//                                            .background(Warning),
//                                        contentAlignment = Alignment.Center
//                                    ) {
//                                        Text(
//                                            text = pendingVisitors.size.toString(),
//                                            fontSize = 12.sp,
//                                            color = Surface,
//                                            fontWeight = FontWeight.Bold
//                                        )
//                                    }
//                                }
//                                Spacer(modifier = Modifier.height(12.dp))
//                                pendingVisitors.take(3).forEach { visitor ->
//                                    PendingRequestCard(
//                                        visitor = visitor,
//                                        onClick = {
//                                            navController.navigate(Screen.VisitorApproval.createRoute(visitor.id))
//                                        }
//                                    )
//                                }
//                                if (pendingVisitors.size > 3) {
//                                    TextButton(
//                                        onClick = { navController.navigate(Screen.VisitorRequests.route) },
//                                        modifier = Modifier.fillMaxWidth()
//                                    ) {
//                                        Text("View All (${pendingVisitors.size})", color = Primary)
//                                    }
//                                }
//                            }
//                        }
//                    }
//                } else {
//                    // Show message when no pending requests
//                    item {
//                        Card(
//                            modifier = Modifier.fillMaxWidth(),
//                            colors = CardDefaults.cardColors(containerColor = Surface),
//                            shape = RoundedCornerShape(12.dp)
//                        ) {
//                            Row(
//                                modifier = Modifier.fillMaxWidth().padding(16.dp),
//                                horizontalArrangement = Arrangement.Center,
//                                verticalAlignment = Alignment.CenterVertically
//                            ) {
//                                Icon(
//                                    Icons.Default.CheckCircle,
//                                    contentDescription = null,
//                                    tint = Success,
//                                    modifier = Modifier.size(24.dp)
//                                )
//                                Spacer(modifier = Modifier.width(8.dp))
//                                Text(
//                                    text = "No pending requests",
//                                    fontSize = 14.sp,
//                                    color = TextSecondary
//                                )
//                            }
//                        }
//                    }
//                }
//
//                // Quick Actions Title
//                item {
//                    Text(
//                        text = "Quick Actions",
//                        fontSize = 18.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = TextPrimary,
//                        modifier = Modifier.padding(vertical = 8.dp)
//                    )
//                }
//
//                // Quick Actions
//                item {
//                    ResidentActionCard(
//                        title = "Visitor Requests",
//                        description = "View all pending requests",
//                        icon = Icons.Default.Notifications,
//                        color = Primary,
//                        badge = if (pendingVisitors.isNotEmpty()) pendingVisitors.size.toString() else null,
//                        onClick = { navController.navigate(Screen.VisitorRequests.route) }
//                    )
//                }
//
//                item {
//                    ResidentActionCard(
//                        title = "Pre-approve Guest",
//                        description = "Allow guests without request",
//                        icon = Icons.Default.PersonAdd,
//                        color = Success,
//                        onClick = { navController.navigate(Screen.PreApproveGuest.route) }
//                    )
//                }
//
//                item {
//                    ResidentActionCard(
//                        title = "My Visitors",
//                        description = "View visitor history",
//                        icon = Icons.Default.History,
//                        color = TealMain,
//                        onClick = { navController.navigate(Screen.VisitorHistoryResident.route) }
//                    )
//                }
//
//                // Bottom Padding
//                item {
//                    Spacer(modifier = Modifier.height(16.dp))
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun PendingRequestCard(
//    visitor: Visitor,
//    onClick: () -> Unit
//) {
//    val interactionSource = remember { MutableInteractionSource() }
//
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .clickable(
//                indication = LocalIndication.current,
//                interactionSource = interactionSource
//            ) { onClick() }
//            .padding(vertical = 4.dp),
//        colors = CardDefaults.cardColors(containerColor = Surface),
//        shape = RoundedCornerShape(12.dp),
//        elevation = CardDefaults.cardElevation(1.dp)
//    ) {
//        Row(
//            modifier = Modifier.fillMaxWidth().padding(12.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Box(
//                modifier = Modifier
//                    .size(40.dp)
//                    .clip(RoundedCornerShape(10.dp))
//                    .background(Primary.copy(alpha = 0.1f)),
//                contentAlignment = Alignment.Center
//            ) {
//                Icon(
//                    when (visitor.purpose.lowercase()) {
//                        "delivery" -> Icons.Default.LocalShipping
//                        "guest" -> Icons.Default.Person
//                        "cab" -> Icons.Default.TaxiAlert
//                        else -> Icons.Default.Person
//                    },
//                    contentDescription = null,
//                    tint = Primary,
//                    modifier = Modifier.size(20.dp)
//                )
//            }
//
//            Spacer(modifier = Modifier.width(12.dp))
//
//            Column(modifier = Modifier.weight(1f)) {
//                Text(
//                    text = visitor.name,
//                    fontSize = 14.sp,
//                    fontWeight = FontWeight.Medium,
//                    color = TextPrimary
//                )
//                Text(
//                    text = "${visitor.purpose} • ${visitor.flatNumber}",
//                    fontSize = 12.sp,
//                    color = TextSecondary
//                )
//                visitor.entryTime?.let {
//                    Text(
//                        text = "Arrived: ${formatTime(it)}",
//                        fontSize = 10.sp,
//                        color = TextHint
//                    )
//                }
//            }
//
//            Box(
//                modifier = Modifier
//                    .clip(RoundedCornerShape(4.dp))
//                    .background(Warning.copy(alpha = 0.2f))
//                    .padding(horizontal = 8.dp, vertical = 4.dp)
//            ) {
//                Text(
//                    text = "Pending",
//                    fontSize = 10.sp,
//                    color = Warning,
//                    fontWeight = FontWeight.Medium
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun ResidentActionCard(
//    title: String,
//    description: String,
//    icon: ImageVector,
//    color: Color,
//    badge: String? = null,
//    onClick: () -> Unit
//) {
//    val interactionSource = remember { MutableInteractionSource() }
//
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        colors = CardDefaults.cardColors(containerColor = Surface),
//        shape = RoundedCornerShape(12.dp),
//        elevation = CardDefaults.cardElevation(2.dp)
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .clickable(
//                    indication = LocalIndication.current,
//                    interactionSource = interactionSource
//                ) { onClick() }
//                .padding(16.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Box(
//                modifier = Modifier
//                    .size(48.dp)
//                    .clip(RoundedCornerShape(12.dp))
//                    .background(color.copy(alpha = 0.1f)),
//                contentAlignment = Alignment.Center
//            ) {
//                Icon(icon, contentDescription = title, tint = color, modifier = Modifier.size(24.dp))
//            }
//            Spacer(modifier = Modifier.width(16.dp))
//            Column(modifier = Modifier.weight(1f)) {
//                Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
//                Text(description, fontSize = 14.sp, color = TextSecondary)
//            }
//            if (badge != null) {
//                Box(
//                    modifier = Modifier
//                        .size(24.dp)
//                        .clip(CircleShape)
//                        .background(color),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(badge, fontSize = 12.sp, color = Surface, fontWeight = FontWeight.Bold)
//                }
//            } else {
//                Icon(Icons.Default.ChevronRight, contentDescription = "Navigate", tint = TextHint)
//            }
//        }
//    }
//}
//
//// Helper function for formatting time
//fun formatTime(timestamp: com.google.firebase.Timestamp?): String {
//    if (timestamp == null) return ""
//    val format = java.text.SimpleDateFormat("hh:mm a", java.util.Locale.getDefault())
//    return format.format(timestamp.toDate())
//}