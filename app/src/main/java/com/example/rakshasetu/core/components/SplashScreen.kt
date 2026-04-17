package com.example.rakshasetu.core.components
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.delay
import com.example.rakshasetu.R

// Define colors locally
val PurpleMain = Color(0xFF7E3EE3)
val BlueMain = Color(0xFF5101FF)
val TealMain = Color(0xFF38BEB4)

@Composable
fun SplashScreen(
    navController: NavController,
    isUserLoggedIn: Boolean,
    userRole: String?
) {
    // Animation states
    val infiniteTransition = rememberInfiniteTransition()

    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val rotation by infiniteTransition.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Get screen dimensions for responsiveness
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val isTablet = screenWidth > 600.dp

    // Determine logo size based on screen size
    val logoSize = when {
        isTablet -> 280.dp
        screenWidth > 400.dp -> 200.dp
        else -> 150.dp
    }

    // Title font size based on screen size
    val titleFontSize = when {
        isTablet -> 48.sp
        screenWidth > 400.dp -> 36.sp
        else -> 28.sp
    }

    // Tagline font size
    val taglineFontSize = when {
        isTablet -> 20.sp
        screenWidth > 400.dp -> 16.sp
        else -> 14.sp
    }

    val context = LocalContext.current

    // Splash delay - navigation is handled by AppNavHost
    LaunchedEffect(Unit) {
        delay(10000) // 3 seconds splash time
        // Navigation is handled in AppNavHost LaunchedEffect
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        PurpleMain,
                        BlueMain,
                        TealMain
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(1000f, 1000f)
                )
            )
    ) {
        // Animated background circles
        AnimatedBackgroundCircles()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo with multiple animations
            Box(
                modifier = Modifier
                    .size(logoSize)
            ) {
                // Logo container with shadow
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(scale)
                        .alpha(alpha)
                        .graphicsLayer {
                            rotationZ = rotation
                        },
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context)
                            .data(R.drawable.securityguard)
                           // .data(R.drawable.guard)
                            .crossfade(true)
                            .build(),
                        contentDescription = "App Logo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Animated ring around logo
                AnimatedLogoRing(logoSize)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // App Title with fade-in
            Text(
                text = "Raksha Setu",
                fontSize = titleFontSize,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(alpha)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tagline
            Text(
                text = "Your Security Bridge",
                fontSize = taglineFontSize,
                color = Color.White.copy(alpha = 0.9f),
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(alpha * 0.8f)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Loading Indicator
            LinearProgressIndicator(
                progress = { 0.3f },
                modifier = Modifier
                    .width(if (isTablet) 300.dp else 200.dp)
                    .height(4.dp),
                color = Color.White,
                trackColor = Color.White.copy(alpha = 0.3f)
            )
        }

        // Version number at bottom
        Text(
            text = "Version 1.0.0",
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.5f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        )
    }
}

@Composable
fun AnimatedBackgroundCircles() {
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

    Box(modifier = Modifier.fillMaxSize()) {
        // Top-left circle
        Box(
            modifier = Modifier
                .size(200.dp * circle1Scale)
                .offset(x = (-50).dp, y = (-50).dp)
                .background(
                    color = Color.White.copy(alpha = 0.1f),
                    shape = CircleShape
                )
        )

        // Bottom-right circle
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(250.dp * circle2Scale)
                .offset(x = 50.dp, y = (-50).dp)
                .background(
                    color = Color.White.copy(alpha = 0.1f),
                    shape = CircleShape
                )
        )

        // Center circle
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(150.dp)
                .background(
                    color = Color.White.copy(alpha = 0.05f),
                    shape = CircleShape
                )
        )
    }
}

@Composable
fun AnimatedLogoRing(
    logoSize: Dp
) {
    val infiniteTransition = rememberInfiniteTransition()

    val ringScale by infiniteTransition.animateFloat(
        initialValue = 1.2f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    val ringAlpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .scale(ringScale)
            .alpha(ringAlpha)
            .background(
                color = Color.White.copy(alpha = 0.3f),
                shape = CircleShape
            )
    )
}