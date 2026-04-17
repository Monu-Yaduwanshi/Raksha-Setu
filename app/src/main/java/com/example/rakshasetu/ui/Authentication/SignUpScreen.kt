package com.example.rakshasetu.ui.Authentication
import com.example.rakshasetu.core.utils.safeClickable
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.rakshasetu.R
import com.example.rakshasetu.core.navigation.Screen
import com.example.rakshasetu.viewModel.viewModel.auth.AuthViewModel

// Define colors locally
val PurpleMain = Color(0xFF7E3EE3)
val BlueMain = Color(0xFF5101FF)
val LavenderLight = Color(0xFFE4DFFC)
val MintLight = Color(0xFFDEF5EF)
val TealMain = Color(0xFF38BEB4)
val GreenMain = Color(0xFF0CB381)
val ForestGreen = Color(0xFF007F50)
val SageGreen = Color(0xFFB7D6B5)
val OrangeAccent = Color(0xFFFF6300)

// Semantic colors
val Primary = PurpleMain
val PrimaryVariant = BlueMain
val Secondary = TealMain
val SecondaryVariant = GreenMain
val Accent = OrangeAccent
val Background = LavenderLight.copy(alpha = 0.3f)
val Surface = Color.White
val TextPrimary = ForestGreen
val TextSecondary = TealMain
val TextHint = SageGreen
val Error = Color(0xFFD32F2F)
val ButtonPrimary = PurpleMain

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: AuthViewModel = viewModel()
) {
    val context = LocalContext.current

    // Form fields
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var societyName by remember { mutableStateOf("") }
    var societyAddress by remember { mutableStateOf("") }

    // UI states
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var termsAccepted by remember { mutableStateOf(false) }

    // Validation states
    var isNameValid by remember { mutableStateOf(true) }
    var isEmailValid by remember { mutableStateOf(true) }
    var isPasswordValid by remember { mutableStateOf(true) }
    var isConfirmPasswordValid by remember { mutableStateOf(true) }
    var isPhoneValid by remember { mutableStateOf(true) }
    var isSocietyNameValid by remember { mutableStateOf(true) }
    var isSocietyAddressValid by remember { mutableStateOf(true) }
    var isTermsAccepted by remember { mutableStateOf(true) }

    val isLoading by viewModel.isLoading.collectAsState()

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
                    modifier = Modifier.size(80.dp),
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

                Spacer(modifier = Modifier.height(8.dp))

                // App Name
                Text(
                    text = "Raksha Setu",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Sign Up Heading
                Text(
                    text = "Register Your Society",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary
                )

                Text(
                    text = "(Secretary Registration Only)",
                    fontSize = 14.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                // Full Name Field
                OutlinedTextField(
                    value = fullName,
                    onValueChange = {
                        fullName = it
                        isNameValid = true
                    },
                    label = { Text("Secretary Full Name", color = TextSecondary) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Name Icon",
                            tint = Primary
                        )
                    },
                    isError = !isNameValid,
                    supportingText = {
                        if (!isNameValid) {
                            Text(
                                text = "Full name is required",
                                color = Error,
                                fontSize = 12.sp
                            )
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
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
                // Phone Field
                OutlinedTextField(
                    value = phone,
                    onValueChange = {
                        phone = it
                        isPhoneValid = true
                    },
                    label = { Text("Phone Number", color = TextSecondary) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Phone,
                            contentDescription = "Phone Icon",
                            tint = Primary
                        )
                    },
                    isError = !isPhoneValid,
                    supportingText = {
                        if (!isPhoneValid) {
                            Text(
                                text = "Please enter a valid phone number",
                                color = Error,
                                fontSize = 12.sp
                            )
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
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
                // Society Name Field
                OutlinedTextField(
                    value = societyName,
                    onValueChange = {
                        societyName = it
                        isSocietyNameValid = true
                    },
                    label = { Text("Society Name", color = TextSecondary) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Home,
                            contentDescription = "Society Icon",
                            tint = Primary
                        )
                    },
                    isError = !isSocietyNameValid,
                    supportingText = {
                        if (!isSocietyNameValid) {
                            Text(
                                text = "Society name is required",
                                color = Error,
                                fontSize = 12.sp
                            )
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
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
                // Society Address Field
                OutlinedTextField(
                    value = societyAddress,
                    onValueChange = {
                        societyAddress = it
                        isSocietyAddressValid = true
                    },
                    label = { Text("Society Address", color = TextSecondary) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.LocationOn,
                            contentDescription = "Address Icon",
                            tint = Primary
                        )
                    },
                    isError = !isSocietyAddressValid,
                    supportingText = {
                        if (!isSocietyAddressValid) {
                            Text(
                                text = "Address is required",
                                color = Error,
                                fontSize = 12.sp
                            )
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
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
                        isConfirmPasswordValid = true
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
                                text = "Password must be at least 6 characters",
                                color = Error,
                                fontSize = 12.sp
                            )
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
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
                // Confirm Password Field
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        isConfirmPasswordValid = true
                    },
                    label = { Text("Confirm Password", color = TextSecondary) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = "Confirm Password Icon",
                            tint = Primary
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(
                                painter = painterResource(
                                    id = if (confirmPasswordVisible)
                                        R.drawable.visibility
                                    else
                                        R.drawable.visible
                                ),
                                contentDescription = "Toggle Password Visibility",
                                tint = Primary
                            )
                        }
                    },
                    visualTransformation = if (confirmPasswordVisible)
                        VisualTransformation.None
                    else
                        PasswordVisualTransformation(),
                    isError = !isConfirmPasswordValid,
                    supportingText = {
                        if (!isConfirmPasswordValid) {
                            Text(
                                text = "Passwords do not match",
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
                // Terms and Conditions Checkbox
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = termsAccepted,
                        onCheckedChange = {
                            termsAccepted = it
                            isTermsAccepted = true
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Primary,
                            uncheckedColor = TextSecondary
                        )
                    )
                    Text(
                        text = "I agree to the ",
                        color = TextSecondary,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "Terms & Conditions",
                        color = Primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        modifier = Modifier.safeClickable {
                            // Navigate to Terms & Conditions
                        }
                    )
                }

                if (!isTermsAccepted) {
                    Text(
                        text = "Please accept terms & conditions",
                        color = Error,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .align(Alignment.Start as Alignment)
                            .padding(start = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            item {
                // Sign Up Button
                Button(
                    onClick = {
                        // Validate inputs
                        isNameValid = fullName.isNotBlank()
                        isEmailValid = email.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
                        isPhoneValid = phone.isNotBlank() && phone.length >= 10
                        isSocietyNameValid = societyName.isNotBlank()
                        isSocietyAddressValid = societyAddress.isNotBlank()
                        isPasswordValid = password.length >= 6
                        isConfirmPasswordValid = password == confirmPassword && password.isNotBlank()
                        isTermsAccepted = termsAccepted

                        if (isNameValid && isEmailValid && isPhoneValid &&
                            isSocietyNameValid && isSocietyAddressValid &&
                            isPasswordValid && isConfirmPasswordValid && isTermsAccepted) {

                            // Call secretarySignUp (role is automatically set to "secretary")
                            viewModel.secretarySignUp(
                                fullName = fullName.trim(),
                                email = email.trim(),
                                password = password.trim(),
                                phone = phone.trim(),
                                societyName = societyName.trim(),
                                societyAddress = societyAddress.trim()
                            ) { success, message ->
                                if (success) {
                                    Toast.makeText(context, "Society registered successfully! Please login.", Toast.LENGTH_LONG).show()
                                    navController.navigate(Screen.SignIn.route) {
                                        popUpTo(Screen.SignUp.route) { inclusive = true }
                                    }
                                } else {
                                    Toast.makeText(context, "Registration failed: $message", Toast.LENGTH_LONG).show()
                                }
                            }
                        } else {
                            when {
                                !isNameValid -> Toast.makeText(context, "Please enter your full name", Toast.LENGTH_SHORT).show()
                                !isEmailValid -> Toast.makeText(context, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                                !isPhoneValid -> Toast.makeText(context, "Please enter a valid phone number", Toast.LENGTH_SHORT).show()
                                !isSocietyNameValid -> Toast.makeText(context, "Please enter society name", Toast.LENGTH_SHORT).show()
                                !isSocietyAddressValid -> Toast.makeText(context, "Please enter society address", Toast.LENGTH_SHORT).show()
                                !isPasswordValid -> Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
                                !isConfirmPasswordValid -> Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                                !isTermsAccepted -> Toast.makeText(context, "Please accept terms & conditions", Toast.LENGTH_SHORT).show()
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
                            text = "Register Society",
                            color = Surface,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                // Navigate to SignIn
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                ) {
                    Text(
                        text = "Already have an account? ",
                        color = TextSecondary
                    )
                    Text(
                        text = "Sign In",
                        color = Primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.safeClickable {
                            navController.navigate(Screen.SignIn.route)
                        }
                    )
                }
            }
        }
    }
}