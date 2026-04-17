//package com.example.rakshasetu.core.components
//
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//import com.example.rakshasetu.core.navigation.Screen
//import com.example.rakshasetu.ui.theme.*
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CreateResidentScreen(navController: NavController) {
//    PlaceholderScreen(
//        navController = navController,
//        title = "Add Resident",
//        message = "Form to add new resident will appear here"
//    )
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CreateWatchmanScreen(navController: NavController) {
//    PlaceholderScreen(
//        navController = navController,
//        title = "Add Watchman",
//        message = "Form to add new watchman will appear here"
//    )
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AddVisitorScreen(navController: NavController) {
//    PlaceholderScreen(
//        navController = navController,
//        title = "Add Visitor",
//        message = "Form to add new visitor with photo capture will appear here"
//    )
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun VisitorHistoryScreen(navController: NavController) {
//    PlaceholderScreen(
//        navController = navController,
//        title = "Visitor History",
//        message = "Visitor history list will appear here"
//    )
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun VisitorApprovalScreen(
//    navController: NavController,
//    requestId: String
//) {
//    PlaceholderScreen(
//        navController = navController,
//        title = "Visitor Approval",
//        message = "Visitor approval screen for request: $requestId"
//    )
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun PreApproveGuestScreen(navController: NavController) {
//    PlaceholderScreen(
//        navController = navController,
//        title = "Pre-approve Guest",
//        message = "Form to pre-approve guests will appear here"
//    )
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun EmergencyScreen(navController: NavController) {
//    PlaceholderScreen(
//        navController = navController,
//        title = "Emergency",
//        message = "Emergency contacts and SOS button will appear here"
//    )
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DirectoryScreen(navController: NavController) {
//    PlaceholderScreen(
//        navController = navController,
//        title = "Directory",
//        message = "Society directory with contacts will appear here"
//    )
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ProfileScreen(navController: NavController) {
//    PlaceholderScreen(
//        navController = navController,
//        title = "Profile",
//        message = "User profile information will appear here"
//    )
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SettingsScreen(navController: NavController) {
//    PlaceholderScreen(
//        navController = navController,
//        title = "Settings",
//        message = "App settings will appear here"
//    )
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun HelpSupportScreen(navController: NavController) {
//    PlaceholderScreen(
//        navController = navController,
//        title = "Help & Support",
//        message = "Help and support information will appear here"
//    )
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AboutAppScreen(navController: NavController) {
//    PlaceholderScreen(
//        navController = navController,
//        title = "About App",
//        message = "App information and version details will appear here"
//    )
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun FeedbackScreen(navController: NavController) {
//    PlaceholderScreen(
//        navController = navController,
//        title = "Feedback",
//        message = "Feedback form will appear here"
//    )
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun PlaceholderScreen(
//    navController: NavController,
//    title: String,
//    message: String
//) {
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        text = title,
//                        color = Surface,
//                        fontWeight = FontWeight.Bold
//                    )
//                },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(
//                            Icons.Default.ArrowBack,
//                            contentDescription = "Back",
//                            tint = Surface
//                        )
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = BlueMain // Using #5101FF
//                )
//            )
//        }
//    ) { paddingValues ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Background)
//                .padding(paddingValues),
//            contentAlignment = Alignment.Center
//        ) {
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text(
//                    text = title,
//                    fontSize = 24.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = TextPrimary
//                )
//                Spacer(modifier = Modifier.height(8.dp))
//                Text(
//                    text = message,
//                    fontSize = 16.sp,
//                    color = TextSecondary,
//                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
//                )
//            }
//        }
//    }
//}