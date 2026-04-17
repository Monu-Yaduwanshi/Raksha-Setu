package com.example.rakshasetu.core.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.rakshasetu.core.navigation.Screen
import com.example.rakshasetu.ui.theme.*
import com.example.rakshasetu.viewModel.viewModel.auth.AuthViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Top Bar Color - #0CB381
val LogoutTopBarColor = Color(0xFF007F50)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun LogoutScreen(
    navController: NavController,
    userName: String = "User",
    userRole: String? = null
) {
    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel()
    val scope = rememberCoroutineScope()

    var showConfirmation by remember { mutableStateOf(true) }
    var isLoggingOut by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Animation states
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Logout",
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
                    containerColor = LogoutTopBarColor
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(paddingValues)
        ) {
            // Animated Background Elements
            AnimatedBackgroundElements()

            // Main Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Animated Icon
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .scale(scale)
                        .clip(CircleShape)
                        .background(Error.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Logout,
                        contentDescription = null,
                        tint = Error,
                        modifier = Modifier
                            .size(60.dp)
                            .graphicsLayer {
                                rotationZ = if (isLoggingOut) rotation else 0f
                            }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Title
                Text(
                    text = "Logout",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // User Info
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Surface.copy(alpha = 0.9f)
                    ),
                    shape = RoundedCornerShape(16.dp)
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
                                .background(Primary.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = userName.first().toString(),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Primary
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = userName,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = userRole?.replaceFirstChar { it.uppercase() } ?: "",
                                fontSize = 14.sp,
                                color = TextSecondary
                            )
                        }

                        Icon(
                            Icons.Default.VerifiedUser,
                            contentDescription = null,
                            tint = Success,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Error Message (if any)
                if (errorMessage != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Error.copy(alpha = 0.1f)
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = errorMessage!!,
                            color = Error,
                            modifier = Modifier.padding(12.dp),
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Confirmation Message
                if (showConfirmation) {
                    AnimatedVisibility(
                        visible = showConfirmation,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Are you sure you want to logout?",
                                fontSize = 18.sp,
                                color = TextSecondary,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            // Action Buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Cancel Button
                                Button(
                                    onClick = {
                                        navController.popBackStack()
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(50.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Surface
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    elevation = ButtonDefaults.buttonElevation(4.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = null,
                                        tint = TextSecondary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Cancel",
                                        color = TextSecondary,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }

                                // Logout Button
                                Button(
                                    onClick = {
                                        isLoggingOut = true
                                        showConfirmation = false
                                        errorMessage = null

                                        scope.launch {
                                            // Show logging out animation for 1.5 seconds
                                            delay(1500)

                                            try {
                                                // Call signOut - this is NOT a suspend function in your AuthViewModel
                                                authViewModel.signOut {
                                                    // This callback runs after successful signout
                                                    showSuccess = true

                                                    // Show success for 1 second then navigate
                                                    scope.launch {
                                                        delay(1000)
                                                        // Navigate to sign in screen
                                                        navController.navigate(Screen.SignIn.route) {
                                                            popUpTo(0) { inclusive = true }
                                                            launchSingleTop = true
                                                        }
                                                    }
                                                }
                                            } catch (e: Exception) {
                                                // Handle any errors
                                                isLoggingOut = false
                                                showConfirmation = true
                                                errorMessage = "Logout failed: ${e.message}"
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(50.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Error
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    elevation = ButtonDefaults.buttonElevation(4.dp),
                                    enabled = !isLoggingOut
                                ) {
                                    Icon(
                                        Icons.Default.Logout,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Logout",
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                // Logging Out Animation
                if (isLoggingOut && !showSuccess) {
                    AnimatedVisibility(
                        visible = isLoggingOut,
                        enter = fadeIn() + scaleIn()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(
                                color = Error,
                                modifier = Modifier.size(60.dp),
                                strokeWidth = 4.dp
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Logging out...",
                                fontSize = 18.sp,
                                color = TextSecondary,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Please wait",
                                fontSize = 14.sp,
                                color = TextHint
                            )
                        }
                    }
                }

                // Success Animation
                if (showSuccess) {
                    AnimatedVisibility(
                        visible = showSuccess,
                        enter = fadeIn() + scaleIn()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Success Check Animation
                            Box(
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .background(Success.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = Success,
                                    modifier = Modifier.size(50.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Logged Out Successfully!",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Success
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Redirecting to login...",
                                fontSize = 14.sp,
                                color = TextSecondary
                            )
                        }
                    }
                }

                // Additional Options
                if (showConfirmation) {
                    Spacer(modifier = Modifier.height(32.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Surface.copy(alpha = 0.8f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "What happens when you logout?",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )

                            LogoutInfoRow(
                                icon = Icons.Default.Info,
                                text = "You'll be redirected to the login screen",
                                color = Primary
                            )

                            LogoutInfoRow(
                                icon = Icons.Default.Lock,
                                text = "Your session will be securely ended",
                                color = Secondary
                            )

                            LogoutInfoRow(
                                icon = Icons.Default.Notifications,
                                text = "You'll stop receiving notifications",
                                color = TealMain
                            )

                            LogoutInfoRow(
                                icon = Icons.Default.Security,
                                text = "You'll need to login again to access the app",
                                color = Success
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LogoutInfoRow(
    icon: ImageVector,
    text: String,
    color: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(16.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = text,
            fontSize = 12.sp,
            color = TextSecondary
        )
    }
}

@Composable
fun AnimatedBackgroundElements() {
    val infiniteTransition = rememberInfiniteTransition()

    val circle1Scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val circle2Scale by infiniteTransition.animateFloat(
        initialValue = 1.2f,
        targetValue = 1.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val circle1Alpha by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(modifier = Modifier.fillMaxSize()) {
        // Top-left circle
        Box(
            modifier = Modifier
                .size(150.dp * circle1Scale)
                .offset(x = (-30).dp, y = (-30).dp)
                .background(
                    color = Primary.copy(alpha = circle1Alpha),
                    shape = CircleShape
                )
        )

        // Bottom-right circle
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(200.dp * circle2Scale)
                .offset(x = 30.dp, y = (-30).dp)
                .background(
                    color = Secondary.copy(alpha = 0.1f),
                    shape = CircleShape
                )
        )

        // Center circle
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(100.dp)
                .background(
                    color = TealMain.copy(alpha = 0.05f),
                    shape = CircleShape
                )
        )
    }
}