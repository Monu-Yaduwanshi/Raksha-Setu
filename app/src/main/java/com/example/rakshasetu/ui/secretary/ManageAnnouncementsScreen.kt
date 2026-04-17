package com.example.rakshasetu.ui.secretary

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.rakshasetu.core.navigation.Screen
import com.example.rakshasetu.data.models.Announcement
import com.example.rakshasetu.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageAnnouncementsScreen(
    navController: NavController,
    userRole: String? = "secretary",
    societyId: String
) {
    val viewModel: SecretaryViewModel = viewModel()
    val context = LocalContext.current
    val isLoading by viewModel.isLoading.collectAsState()
    val announcements by viewModel.announcements.collectAsState()

    var selectedAnnouncement by remember { mutableStateOf<Announcement?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(societyId) {
        viewModel.loadAnnouncements(societyId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Manage Announcements",
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
                    IconButton(
                        onClick = {
                            navController.navigate(Screen.SendAnnouncement.route)
                        }
                    ) {
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
            } else if (announcements.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Announcement,
                        contentDescription = null,
                        tint = TextHint,
                        modifier = Modifier.size(64.dp)
                    )
                    Text(
                        text = "No announcements yet",
                        fontSize = 16.sp,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {
                            navController.navigate(Screen.SendAnnouncement.route)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Primary
                        )
                    ) {
                        Text("Create First Announcement")
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(announcements) { announcement ->
                        AnnouncementCard(
                            announcement = announcement,
                            onDelete = {
                                selectedAnnouncement = announcement
                                showDeleteDialog = true
                            }
                        )
                    }
                }
            }
        }
    }

    // Delete Confirmation Dialog
    if (showDeleteDialog && selectedAnnouncement != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Announcement") },
            text = {
                Text(
                    "Are you sure you want to delete '${selectedAnnouncement!!.title}'?",
                    color = TextPrimary
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteAnnouncement(selectedAnnouncement!!.announcementId) { success, message ->
                            if (success) {
                                Toast.makeText(
                                    context,
                                    "Announcement deleted",
                                    Toast.LENGTH_SHORT
                                ).show()
                                viewModel.loadAnnouncements(societyId)
                            } else {
                                Toast.makeText(
                                    context,
                                    "Error: $message",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            showDeleteDialog = false
                        }
                    }
                ) {
                    Text("Delete", color = Error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun AnnouncementCard(
    announcement: Announcement,
    onDelete: () -> Unit
) {
    val backgroundColor = when (announcement.priority) {
        "urgent" -> Error.copy(alpha = 0.1f)
        "high" -> Warning.copy(alpha = 0.1f)
        else -> Primary.copy(alpha = 0.1f)
    }

    val iconColor = when (announcement.priority) {
        "urgent" -> Error
        "high" -> Warning
        else -> Primary
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Surface
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header with priority indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(backgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        when (announcement.priority) {
                            "urgent" -> Icons.Default.Warning
                            "high" -> Icons.Default.PriorityHigh
                            else -> Icons.Default.Info
                        },
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = announcement.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )

                    announcement.createdAt?.toDate()?.let { date ->
                        val dateFormat = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                        Text(
                            text = dateFormat.format(date),
                            fontSize = 12.sp,
                            color = TextHint
                        )
                    }
                }

                // Delete button
                IconButton(onClick = onDelete) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Error
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Message
            Text(
                text = announcement.message,
                fontSize = 14.sp,
                color = TextSecondary,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Footer with audience and stats
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Audience chip
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Group,
                        contentDescription = null,
                        tint = TextHint,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = when (announcement.audience) {
                            "residents" -> "Residents"
                            "watchmen" -> "Watchmen"
                            else -> "All Members"
                        },
                        fontSize = 12.sp,
                        color = TextHint
                    )
                }

                // Priority badge
                if (announcement.priority != "normal") {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(iconColor.copy(alpha = 0.1f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = announcement.priority.replaceFirstChar { it.uppercase() },
                            fontSize = 10.sp,
                            color = iconColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}