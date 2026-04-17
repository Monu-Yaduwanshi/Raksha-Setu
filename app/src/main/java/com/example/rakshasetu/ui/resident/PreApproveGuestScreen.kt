//package com.example.rakshasetu.ui.resident
//
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//import com.example.rakshasetu.ui.theme.*
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun PreApproveGuestScreen(
//    navController: NavController,
//    userRole: String? = "resident"
//) {
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        text = "Pre-approve Guest",
//                        color = Color.White,
//                        fontSize = 20.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(
//                            Icons.Default.ArrowBack,
//                            contentDescription = "Back",
//                            tint = Color.White
//                        )
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = Color(0xFF007F50)
//                )
//            )
//        }
//    ) { paddingValues ->
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Background)
//                .padding(paddingValues)
//                .padding(16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(
//                text = "Pre-approve Guest Screen",
//                fontSize = 18.sp,
//                color = TextPrimary
//            )
//        }
//    }
//}
package com.example.rakshasetu.ui.resident
//
//import android.widget.Toast
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.verticalScroll
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import com.example.rakshasetu.core.navigation.Screen
//import com.example.rakshasetu.ui.theme.*
//
//import java.util.*
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun PreApproveGuestScreen(
//    navController: NavController,
//    userRole: String? = "resident"
//) {
//    val viewModel: ResidentViewModel = viewModel()
//    val context = LocalContext.current
//    val preApprovedGuests by viewModel.preApprovedGuests.collectAsState()
//    val isLoading by viewModel.isLoading.collectAsState()
//
//    var showAddDialog by remember { mutableStateOf(false) }
//
//    LaunchedEffect(Unit) {
//        viewModel.loadPreApprovedGuests()
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = {
//                    Text(
//                        text = "Pre-approve Guest",
//                        color = Color.White,
//                        fontSize = 20.sp,
//                        fontWeight = FontWeight.Bold
//                    )
//                },
//                navigationIcon = {
//                    IconButton(onClick = { navController.popBackStack() }) {
//                        Icon(
//                            Icons.Default.ArrowBack,
//                            contentDescription = "Back",
//                            tint = Color.White
//                        )
//                    }
//                },
//                actions = {
//                    IconButton(onClick = { showAddDialog = true }) {
//                        Icon(
//                            Icons.Default.Add,
//                            contentDescription = "Add",
//                            tint = Color.White
//                        )
//                    }
//                },
//                colors = TopAppBarDefaults.topAppBarColors(
//                    containerColor = SecretaryTopBarColor
//                )
//            )
//        }
//    ) { paddingValues ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Background)
//                .padding(paddingValues)
//        ) {
//            if (isLoading) {
//                CircularProgressIndicator(
//                    modifier = Modifier.align(Alignment.Center),
//                    color = Primary
//                )
//            } else if (preApprovedGuests.isEmpty()) {
//                Column(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(16.dp),
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Center
//                ) {
//                    Icon(
//                        Icons.Default.PersonAdd,
//                        contentDescription = null,
//                        tint = TextHint,
//                        modifier = Modifier.size(64.dp)
//                    )
//                    Text(
//                        text = "No pre-approved guests",
//                        fontSize = 16.sp,
//                        color = TextSecondary,
//                        modifier = Modifier.padding(top = 8.dp)
//                    )
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Button(
//                        onClick = { showAddDialog = true },
//                        colors = ButtonDefaults.buttonColors(
//                            containerColor = Primary
//                        )
//                    ) {
//                        Text("Add Guest")
//                    }
//                }
//            } else {
//                LazyColumn(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(16.dp),
//                    verticalArrangement = Arrangement.spacedBy(12.dp)
//                ) {
//                    items(preApprovedGuests) { guest ->
//                        PreApprovedGuestCard(
//                            guest = guest,
//                            onDelete = {
//                                viewModel.deletePreApprovedGuest(guest.id) { success, message ->
//                                    if (success) {
//                                        Toast.makeText(context, "Guest deleted", Toast.LENGTH_SHORT).show()
//                                    } else {
//                                        Toast.makeText(context, "Error: $message", Toast.LENGTH_SHORT).show()
//                                    }
//                                }
//                            }
//                        )
//                    }
//
//                    item {
//                        Spacer(modifier = Modifier.height(16.dp))
//                    }
//                }
//            }
//        }
//    }
//
//    // Add Guest Dialog
//    if (showAddDialog) {
//        AddPreApprovedGuestDialog(
//            onDismiss = { showAddDialog = false },
//            onAdd = { guestName, guestPhone, purpose, schedule, validFrom, validUntil ->
//                viewModel.addPreApprovedGuest(
//                    guestName = guestName,
//                    guestPhone = guestPhone,
//                    purpose = purpose,
//                    schedule = schedule,
//                    validFrom = validFrom,
//                    validUntil = validUntil
//                ) { success, message ->
//                    if (success) {
//                        Toast.makeText(context, "Guest added successfully", Toast.LENGTH_SHORT).show()
//                        showAddDialog = false
//                    } else {
//                        Toast.makeText(context, "Error: $message", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        )
//    }
//}
//
//@Composable
//fun PreApprovedGuestCard(
//    guest: PreApprovedGuest,
//    onDelete: () -> Unit
//) {
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        colors = CardDefaults.cardColors(
//            containerColor = Surface
//        ),
//        shape = RoundedCornerShape(12.dp)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Box(
//                    modifier = Modifier
//                        .size(48.dp)
//                        .clip(CircleShape)
//                        .background(Success.copy(alpha = 0.1f)),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text(
//                        text = guest.guestName.first().toString(),
//                        fontSize = 18.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = Success
//                    )
//                }
//
//                Spacer(modifier = Modifier.width(12.dp))
//
//                Column(
//                    modifier = Modifier.weight(1f)
//                ) {
//                    Text(
//                        text = guest.guestName,
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = TextPrimary
//                    )
//                    Text(
//                        text = guest.guestPhone,
//                        fontSize = 14.sp,
//                        color = TextSecondary
//                    )
//                }
//
//                IconButton(onClick = onDelete) {
//                    Icon(
//                        Icons.Default.Delete,
//                        contentDescription = "Delete",
//                        tint = Error
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            // Details
//            Row(
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(
//                    Icons.Default.Info,
//                    contentDescription = null,
//                    tint = TextHint,
//                    modifier = Modifier.size(16.dp)
//                )
//                Spacer(modifier = Modifier.width(4.dp))
//                Text(
//                    text = guest.purpose,
//                    fontSize = 12.sp,
//                    color = TextHint
//                )
//            }
//
//            if (guest.schedule.isNotBlank()) {
//                Row(
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Icon(
//                        Icons.Default.Schedule,
//                        contentDescription = null,
//                        tint = TextHint,
//                        modifier = Modifier.size(16.dp)
//                    )
//                    Spacer(modifier = Modifier.width(4.dp))
//                    Text(
//                        text = guest.schedule,
//                        fontSize = 12.sp,
//                        color = TextHint
//                    )
//                }
//            }
//
//            guest.validFrom?.let { validFrom ->
//                guest.validUntil?.let { validUntil ->
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        Icon(
//                            Icons.Default.DateRange,
//                            contentDescription = null,
//                            tint = TextHint,
//                            modifier = Modifier.size(16.dp)
//                        )
//                        Spacer(modifier = Modifier.width(4.dp))
//                        Text(
//                            text = "${ResidentViewModel().formatDate(validFrom)} - ${ResidentViewModel().formatDate(validUntil)}",
//                            fontSize = 12.sp,
//                            color = TextHint
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun AddPreApprovedGuestDialog(
//    onDismiss: () -> Unit,
//    onAdd: (String, String, String, String, Date, Date) -> Unit
//) {
//    var guestName by remember { mutableStateOf("") }
//    var guestPhone by remember { mutableStateOf("") }
//    var purpose by remember { mutableStateOf("Guest") }
//    var schedule by remember { mutableStateOf("") }
//    var validFrom by remember { mutableStateOf("") }
//    var validUntil by remember { mutableStateOf("") }
//
//    val purposes = listOf("Guest", "House Help", "Driver", "Maintenance", "Other")
//
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        title = { Text("Add Pre-approved Guest") },
//        text = {
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .heightIn(max = 400.dp)
//                    .verticalScroll(rememberScrollState())
//            ) {
//                OutlinedTextField(
//                    value = guestName,
//                    onValueChange = { guestName = it },
//                    label = { Text("Guest Name *") },
//                    modifier = Modifier.fillMaxWidth(),
//                    shape = RoundedCornerShape(8.dp)
//                )
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                OutlinedTextField(
//                    value = guestPhone,
//                    onValueChange = { guestPhone = it },
//                    label = { Text("Phone Number *") },
//                    modifier = Modifier.fillMaxWidth(),
//                    shape = RoundedCornerShape(8.dp)
//                )
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                Text(
//                    text = "Purpose",
//                    fontSize = 14.sp,
//                    color = TextSecondary,
//                    modifier = Modifier.padding(bottom = 4.dp)
//                )
//
//                Row(
//                    horizontalArrangement = Arrangement.spacedBy(8.dp)
//                ) {
//                    purposes.forEach { p ->
//                        FilterChip(
//                            selected = purpose == p,
//                            onClick = { purpose = p },
//                            label = { Text(p) },
//                            colors = FilterChipDefaults.filterChipColors(
//                                selectedContainerColor = Primary,
//                                selectedLabelColor = Color.White,
//                                labelColor = TextSecondary
//                            )
//                        )
//                    }
//                }
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                OutlinedTextField(
//                    value = schedule,
//                    onValueChange = { schedule = it },
//                    label = { Text("Schedule (e.g., Mon-Fri 9am-6pm)") },
//                    modifier = Modifier.fillMaxWidth(),
//                    shape = RoundedCornerShape(8.dp)
//                )
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                OutlinedTextField(
//                    value = validFrom,
//                    onValueChange = { validFrom = it },
//                    label = { Text("Valid From (DD/MM/YYYY)") },
//                    placeholder = { Text("DD/MM/YYYY") },
//                    modifier = Modifier.fillMaxWidth(),
//                    shape = RoundedCornerShape(8.dp)
//                )
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                OutlinedTextField(
//                    value = validUntil,
//                    onValueChange = { validUntil = it },
//                    label = { Text("Valid Until (DD/MM/YYYY)") },
//                    placeholder = { Text("DD/MM/YYYY") },
//                    modifier = Modifier.fillMaxWidth(),
//                    shape = RoundedCornerShape(8.dp)
//                )
//            }
//        },
//        confirmButton = {
//            Button(
//                onClick = {
//                    if (guestName.isBlank() || guestPhone.isBlank()) {
//                        return@Button
//                    }
//
//                    // Parse dates
//                    val format = java.text.SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
//                    val fromDate = try { format.parse(validFrom) } catch (e: Exception) { Date() }
//                    val untilDate = try { format.parse(validUntil) } catch (e: Exception) { Date() }
//
//                    onAdd(guestName, guestPhone, purpose, schedule, fromDate, untilDate)
//                },
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Success
//                )
//            ) {
//                Text("Add Guest")
//            }
//        },
//        dismissButton = {
//            TextButton(onClick = onDismiss) {
//                Text("Cancel")
//            }
//        }
//    )
//}


import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.rakshasetu.core.navigation.Screen
import com.example.rakshasetu.ui.theme.*
import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Date
import java.util.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.rakshasetu.data.models.PreApprovedGuestItem

//import com.example.rakshasetu.viewModel.viewModel.resident.PreApprovedGuestItem
//import com.example.rakshasetu.viewModel.viewModel.resident.ResidentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreApproveGuestScreen(
    navController: NavController,
    userRole: String? = "resident"
) {
    val viewModel: ResidentViewModel = viewModel()
    val context = LocalContext.current
    val preApprovedGuests by viewModel.preApprovedGuests.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadPreApprovedGuests()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Pre-approve Guest",
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
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SecretaryTopBarColor
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
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Primary
                )
            } else if (preApprovedGuests.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.PersonAdd,
                        contentDescription = null,
                        tint = TextHint,
                        modifier = Modifier.size(64.dp)
                    )
                    Text(
                        text = "No pre-approved guests",
                        fontSize = 16.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = { showAddDialog = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Primary
                        )
                    ) {
                        Text("Add Guest")
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(preApprovedGuests) { guest ->
                        PreApprovedGuestCard(
                            guest = guest,
                            onDelete = {
                                viewModel.deletePreApprovedGuest(guest.id) { success, message ->
                                    if (success) {
                                        Toast.makeText(context, "Guest deleted", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "Error: $message", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }

    // Add Guest Dialog
    if (showAddDialog) {
        AddPreApprovedGuestDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { guestName, guestPhone, purpose, schedule, validFrom, validUntil ->
                viewModel.addPreApprovedGuest(
                    guestName = guestName,
                    guestPhone = guestPhone,
                    purpose = purpose,
                    schedule = schedule,
                    validFrom = validFrom,
                    validUntil = validUntil
                ) { success, message ->
                    if (success) {
                        Toast.makeText(context, "Guest added successfully", Toast.LENGTH_SHORT).show()
                        showAddDialog = false
                    } else {
                        Toast.makeText(context, "Error: $message", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )
    }
}

@Composable
fun PreApprovedGuestCard(
    guest: PreApprovedGuestItem,
    onDelete: () -> Unit
) {
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Success.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = guest.guestName.first().toString(),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Success
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = guest.guestName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Text(
                        text = guest.guestPhone,
                        fontSize = 14.sp,
                        color = TextSecondary
                    )
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Error
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Details
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = null,
                    tint = TextHint,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = guest.purpose,
                    fontSize = 12.sp,
                    color = TextHint
                )
            }

            if (guest.schedule.isNotBlank()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Schedule,
                        contentDescription = null,
                        tint = TextHint,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = guest.schedule,
                        fontSize = 12.sp,
                        color = TextHint
                    )
                }
            }

            guest.validFrom?.let { validFrom ->
                guest.validUntil?.let { validUntil ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = null,
                            tint = TextHint,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${formatDate(validFrom)} - ${formatDate(validUntil)}",
                            fontSize = 12.sp,
                            color = TextHint
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AddPreApprovedGuestDialog(
    onDismiss: () -> Unit,
    onAdd: (String, String, String, String, Date, Date) -> Unit
) {
    var guestName by remember { mutableStateOf("") }
    var guestPhone by remember { mutableStateOf("") }
    var purpose by remember { mutableStateOf("Guest") }
    var schedule by remember { mutableStateOf("") }
    var validFrom by remember { mutableStateOf("") }
    var validUntil by remember { mutableStateOf("") }

    val purposes = listOf("Guest", "House Help", "Driver", "Maintenance", "Other")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Pre-approved Guest") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 400.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = guestName,
                    onValueChange = { guestName = it },
                    label = { Text("Guest Name *") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = guestPhone,
                    onValueChange = { guestPhone = it },
                    label = { Text("Phone Number *") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Purpose",
                    fontSize = 14.sp,
                    color = TextSecondary,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    purposes.forEach { p ->
                        FilterChip(
                            selected = purpose == p,
                            onClick = { purpose = p },
                            label = { Text(p) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Primary,
                                selectedLabelColor = Color.White,
                                labelColor = TextSecondary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = schedule,
                    onValueChange = { schedule = it },
                    label = { Text("Schedule (e.g., Mon-Fri 9am-6pm)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = validFrom,
                    onValueChange = { validFrom = it },
                    label = { Text("Valid From (DD/MM/YYYY)") },
                    placeholder = { Text("DD/MM/YYYY") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = validUntil,
                    onValueChange = { validUntil = it },
                    label = { Text("Valid Until (DD/MM/YYYY)") },
                    placeholder = { Text("DD/MM/YYYY") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (guestName.isBlank() || guestPhone.isBlank()) {
                        return@Button
                    }

                    // Parse dates
                    val format = java.text.SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val fromDate = try { format.parse(validFrom) } catch (e: Exception) { Date() }
                    val untilDate = try { format.parse(validUntil) } catch (e: Exception) { Date() }

                    onAdd(guestName, guestPhone, purpose, schedule, fromDate, untilDate)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Success
                )
            ) {
                Text("Add Guest")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

fun formatDate(timestamp: Timestamp?): String {
    if (timestamp == null) return ""
    val format = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    return format.format(timestamp.toDate())
}