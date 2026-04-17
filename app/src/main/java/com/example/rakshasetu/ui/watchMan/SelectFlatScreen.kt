package com.example.rakshasetu.ui.watchMan

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.rakshasetu.core.navigation.Screen
import com.example.rakshasetu.core.utils.safeClickable
import com.example.rakshasetu.ui.theme.*
import com.example.rakshasetu.viewModel.viewModel.watchman.FlatOwner
import com.example.rakshasetu.viewModel.viewModel.watchman.WatchmanViewModel


val SecretaryTopBarColor = Color(0xFF0046CC)

// SelectFlatScreen.kt - Add debug button temporarily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectFlatScreen(
    navController: NavController,
    userRole: String? = "watchman"
) {
    val viewModel: WatchmanViewModel = viewModel()
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var isSearchActive by remember { mutableStateOf(false) }
    val flats by viewModel.flats.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val societyId by viewModel.societyId.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    // Load flats when screen opens
    LaunchedEffect(Unit) {
        viewModel.loadFlats()
        // Optional: Run debug check
        // viewModel.debugCheckFirestoreData()
    }

    // Handle back press when search is active
    BackHandler(enabled = isSearchActive) {
        isSearchActive = false
        searchQuery = ""
    }

    // Filter flats based on search
    val filteredFlats = remember(searchQuery, flats) {
        if (searchQuery.isBlank()) {
            flats
        } else {
            flats.filter {
                it.flatNumber.contains(searchQuery, ignoreCase = true) ||
                        it.ownerName.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (isSearchActive) "Search Flat" else "Select Flat",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (isSearchActive) {
                            isSearchActive = false
                            searchQuery = ""
                        } else {
                            navController.popBackStack()
                        }
                    }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    if (!isSearchActive) {
                        // ✅ TEMPORARY DEBUG BUTTON - Remove after fixing
                        IconButton(onClick = {
                            viewModel.debugCheckFirestoreData()
                            Toast.makeText(context, "Check Logcat for debug info", Toast.LENGTH_SHORT).show()
                        }) {
                            Icon(
                                Icons.Default.BugReport,
                                contentDescription = "Debug",
                                tint = Color.White
                            )
                        }
                        IconButton(onClick = { isSearchActive = true }) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color.White
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SecretaryTopBarColor
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(paddingValues)
        ) {
            // ✅ Show Society ID for debugging (remove in production)
            if (societyId.isNotEmpty()) {
                Text(
                    text = "Society ID: ${societyId.take(10)}...",
                    fontSize = 10.sp,
                    color = TextHint,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }

            // Animated Search Bar
            AnimatedVisibility(
                visible = isSearchActive,
                enter = fadeIn() + slideInVertically(),
                exit = fadeOut() + slideOutVertically()
            ) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shadowElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = {
                                Text(
                                    "Enter flat number (e.g., A-101)",
                                    color = TextHint
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = Primary
                                )
                            },
                            trailingIcon = {
                                if (searchQuery.isNotBlank()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(
                                            Icons.Default.Close,
                                            contentDescription = "Clear",
                                            tint = TextHint
                                        )
                                    }
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = Surface,
                                unfocusedContainerColor = Surface,
                                focusedBorderColor = Primary,
                                unfocusedBorderColor = TextHint,
                                cursorColor = Primary
                            ),
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true
                        )

                        Button(
                            onClick = {
                                keyboardController?.hide()
                                isSearchActive = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Primary
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Done")
                        }
                    }
                }
            }

            // Results count
            AnimatedVisibility(
                visible = !isSearchActive && searchQuery.isNotBlank(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Found ${filteredFlats.size} flats",
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                    TextButton(onClick = { searchQuery = "" }) {
                        Text("Clear Search", fontSize = 12.sp)
                    }
                }
            }

            // Content
            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = Primary)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Loading flats...", fontSize = 12.sp, color = TextSecondary)
                        }
                    }
                }
                filteredFlats.isEmpty() && searchQuery.isNotBlank() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.SearchOff,
                                contentDescription = "No results",
                                tint = TextHint,
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No flats found for \"$searchQuery\"",
                                fontSize = 14.sp,
                                color = TextSecondary
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    searchQuery = ""
                                    isSearchActive = false
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Clear Search")
                            }
                        }
                    }
                }
                filteredFlats.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Default.Home,
                                contentDescription = "No flats",
                                tint = TextHint,
                                modifier = Modifier.size(64.dp)
                            )
                            Text(
                                text = "No flats found in your society",
                                fontSize = 14.sp,
                                color = TextSecondary
                            )
                            Text(
                                text = "Society ID: ${societyId.take(15)}...",
                                fontSize = 12.sp,
                                color = TextHint,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    viewModel.loadFlats()
                                    viewModel.debugCheckFirestoreData()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Retry")
                            }
                        }
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(
                            items = filteredFlats,
                            key = { it.flatId }
                        ) { flat ->
                            if (flat.isOccupied) {
                                FlatOwnerCard(
                                    flat = flat,
                                    onClick = {
                                        navController.navigate("add_visitor/${flat.flatNumber}")
                                    }
                                )
                            } else {
                                VacantFlatCard(flat)
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(80.dp))
                        }
                    }
                }
            }
        }
    }
}
// SelectFlatScreen.kt - Update FlatOwnerCard to use safeClickable

@Composable
fun FlatOwnerCard(
    flat: FlatOwner,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .safeClickable(onClick = onClick),  // ✅ Using safeClickable
        colors = CardDefaults.cardColors(
            containerColor = Surface
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
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
                    .background(Primary.copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = flat.flatNumber.take(4),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = flat.ownerName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
                Text(
                    text = "Flat ${flat.flatNumber} • ${flat.phone.takeLast(10)}",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }

            Icon(
                Icons.Default.ChevronRight,
                contentDescription = "Select",
                tint = Primary
            )
        }
    }
}
//@Composable
//fun FlatOwnerCard(
//    flat: FlatOwner,
//    onClick: () -> Unit
//) {
//    Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .safeClickable(onClick),
//        colors = CardDefaults.cardColors(
//            containerColor = Surface
//        ),
//        shape = RoundedCornerShape(12.dp),
//        elevation = CardDefaults.cardElevation(2.dp)
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Box(
//                modifier = Modifier
//                    .size(48.dp)
//                    .background(Primary.copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp)),
//                contentAlignment = Alignment.Center
//            ) {
//                Text(
//                    text = flat.flatNumber,
//                    fontSize = 14.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = Primary
//                )
//            }
//
//            Spacer(modifier = Modifier.width(12.dp))
//
//            Column(
//                modifier = Modifier.weight(1f)
//            ) {
//                Text(
//                    text = flat.ownerName,
//                    fontSize = 16.sp,
//                    fontWeight = FontWeight.Bold,
//                    color = TextPrimary
//                )
//                Text(
//                    text = flat.flatNumber,
//                    fontSize = 14.sp,
//                    color = TextSecondary
//                )
//            }
//
//            Icon(
//                Icons.Default.ChevronRight,
//                contentDescription = "Select",
//                tint = Primary
//            )
//        }
//    }
//}

@Composable
fun VacantFlatCard(flat: FlatOwner) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Surface.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(12.dp)
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
                    .background(Warning.copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = flat.flatNumber,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Warning
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Vacant",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Warning
                )
                Text(
                    text = flat.flatNumber,
                    fontSize = 14.sp,
                    color = TextHint
                )
            }
        }
    }
}