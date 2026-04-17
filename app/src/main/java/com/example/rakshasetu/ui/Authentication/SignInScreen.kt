package com.example.rakshasetu.ui.Authentication

import com.example.rakshasetu.core.utils.safeClickable
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AdminPanelSettings
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.rakshasetu.R
import com.example.rakshasetu.core.navigation.Screen
import com.example.rakshasetu.viewModel.viewModel.auth.AuthViewModel

// Define colors locally
//val PurpleMain = Color(0xFF7E3EE3)
//val BlueMain = Color(0xFF5101FF)
//val LavenderLight = Color(0xFFE4DFFC)
//val MintLight = Color(0xFFDEF5EF)
//val TealMain = Color(0xFF38BEB4)
//val GreenMain = Color(0xFF0CB381)
//val ForestGreen = Color(0xFF007F50)
//val SageGreen = Color(0xFFB7D6B5)
//val OrangeAccent = Color(0xFFFF6300)
//
//// Semantic colors
//val Primary = PurpleMain
//val PrimaryVariant = BlueMain
//val Secondary = TealMain
//val SecondaryVariant = GreenMain
//val Accent = OrangeAccent
//val Background = LavenderLight.copy(alpha = 0.3f)
//val Surface = Color.White
//val TextPrimary = ForestGreen
//val TextSecondary = TealMain
//val TextHint = SageGreen
//val Error = Color(0xFFD32F2F)
//val ButtonPrimary = PurpleMain

// Data class for role selection
data class UserRole(
    val name: String,
    val value: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val color: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    navController: NavController,
    viewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Role selection state - default to secretary
    var selectedRole by remember { mutableStateOf("secretary") }

    // Validation states
    var isEmailValid by remember { mutableStateOf(true) }
    var isPasswordValid by remember { mutableStateOf(true) }
    var isRoleValid by remember { mutableStateOf(true) }

    // Observe loading state
    val isLoading by viewModel.isLoading.collectAsState()

    // Define available roles with icons
    val roles = listOf(
        UserRole(
            name = "Secretary",
            value = "secretary",
            selectedIcon = Icons.Filled.AdminPanelSettings,
            unselectedIcon = Icons.Outlined.AdminPanelSettings,
            color = Primary
        ),
        UserRole(
            name = "Watchman",
            value = "watchman",
            selectedIcon = Icons.Filled.Security,
            unselectedIcon = Icons.Outlined.Security,
            color = Secondary
        ),
        UserRole(
            name = "Resident",
            value = "resident",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            color = TealMain
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item {
                // Company Logo in Circle
                Card(
                    modifier = Modifier.size(100.dp),
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.securityguard),
                        contentDescription = "App Logo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // App Name
                Text(
                    text = "Raksha Setu",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Sign In Heading
                Text(
                    text = "Welcome Back",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )

                Text(
                    text = "Select your role and sign in",
                    fontSize = 16.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                // Email Field
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        isEmailValid = true
                    },
                    label = { Text("Email Address", color = TextSecondary) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Email,
                            contentDescription = "Email Icon",
                            tint = Primary
                        )
                    },
                    isError = !isEmailValid,
                    supportingText = {
                        if (!isEmailValid) {
                            Text(
                                text = "Please enter a valid email",
                                color = Error,
                                fontSize = 12.sp
                            )
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Surface,
                        unfocusedContainerColor = Surface,
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = TextHint,
                        cursorColor = Primary
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                // Password Field
                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        isPasswordValid = true
                    },
                    label = { Text("Password", color = TextSecondary) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = "Password Icon",
                            tint = Primary
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                painter = painterResource(
                                    id = if (passwordVisible)
                                        R.drawable.visibility
                                    else
                                        R.drawable.visible
                                ),
                                contentDescription = "Toggle Password Visibility",
                                tint = Primary
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    isError = !isPasswordValid,
                    supportingText = {
                        if (!isPasswordValid) {
                            Text(
                                text = "Password cannot be empty",
                                color = Error,
                                fontSize = 12.sp
                            )
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Surface,
                        unfocusedContainerColor = Surface,
                        focusedBorderColor = Primary,
                        unfocusedBorderColor = TextHint,
                        cursorColor = Primary
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                // Role Selection Section
                Text(
                    text = "Select Your Role",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )

                // Role Selection Cards - Using Row with weight
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Secretary Card
                    RoleCard(
                        role = roles[0],
                        isSelected = selectedRole == "secretary",
                        onClick = {
                            selectedRole = "secretary"
                            isRoleValid = true
                        }
                    )

                    // Watchman Card
                    RoleCard(
                        role = roles[1],
                        isSelected = selectedRole == "watchman",
                        onClick = {
                            selectedRole = "watchman"
                            isRoleValid = true
                        }
                    )

                    // Resident Card
                    RoleCard(
                        role = roles[2],
                        isSelected = selectedRole == "resident",
                        onClick = {
                            selectedRole = "resident"
                            isRoleValid = true
                        }
                    )
                }

                if (!isRoleValid) {
                    Text(
                        text = "Please select your role",
                        color = Error,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .align(Alignment.Start as Alignment)
                            .padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                // Forgot Password
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "Forgot Password?",
                        color = Primary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.safeClickable {
                            Toast.makeText(
                                context,
                                "Contact your society secretary",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                // Sign In Button
                Button(
                    onClick = {
                        // Validate inputs
                        isEmailValid = email.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                        isPasswordValid = password.isNotBlank()
                        isRoleValid = selectedRole.isNotBlank()

                        if (isEmailValid && isPasswordValid && isRoleValid) {
                            // Show toast for debugging
                            Toast.makeText(context, "Signing in as: $selectedRole", Toast.LENGTH_SHORT).show()

                            // Call signIn with selected role
                            viewModel.signIn(
                                email = email.trim(),
                                password = password.trim(),
                                selectedRole = selectedRole,
                                navController = navController,
                                showToast = { message ->
                                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                                }
                            )
                        } else {
                            when {
                                !isEmailValid -> Toast.makeText(context, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                                !isPasswordValid -> Toast.makeText(context, "Password cannot be empty", Toast.LENGTH_SHORT).show()
                                !isRoleValid -> Toast.makeText(context, "Please select your role", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonPrimary,
                        disabledContainerColor = ButtonPrimary.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(14.dp),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Surface,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            text = "Sign In",
                            color = Surface,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                // Navigate to SignUp (Secretary only)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    ) {
                        Text(
                            text = "New Society? ",
                            color = TextSecondary
                        )
                        Text(
                            text = "Register as Secretary",
                            color = Primary,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.safeClickable {
                                navController.navigate(Screen.SignUp.route)
                            }
                        )
                    }

                    Text(
                        text = "Residents & Watchmen: Use credentials provided by secretary",
                        fontSize = 12.sp,
                        color = TextHint,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun RoleCard(
    role: UserRole,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(100.dp)  // Fixed width instead of weight
            .safeClickable  { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) role.color else Surface
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(if (isSelected) 4.dp else 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icon with background
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected)
                            Surface.copy(alpha = 0.2f)
                        else
                            role.color.copy(alpha = 0.1f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isSelected) role.selectedIcon else role.unselectedIcon,
                    contentDescription = role.name,
                    tint = if (isSelected) Surface else role.color,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Role Name
            Text(
                text = role.name,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Surface else TextPrimary
            )
        }
    }
}