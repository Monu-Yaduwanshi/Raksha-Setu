package com.example.rakshasetu.ui.watchMan


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rakshasetu.ui.theme.*

/**
 * Shared StatusChip component - use this everywhere
 */
//@Composable
//fun StatusChip(status: String) {
//    val (color, text) = when (status.lowercase()) {
//        "approved" -> Success to "Approved"
//        "pending" -> Warning to "Pending"
//        "rejected", "denied" -> Error to "Rejected"
//        else -> TextHint to status
//    }
//
//    Box(
//        modifier = Modifier
//            .clip(RoundedCornerShape(16.dp))
//            .background(color.copy(alpha = 0.1f))
//            .padding(horizontal = 8.dp, vertical = 4.dp)
//    ) {
//        Text(
//            text = text,
//            fontSize = 11.sp,
//            color = color,
//            fontWeight = FontWeight.Medium
//        )
//    }
//}
@Composable
fun StatusChip(status: String) {
    val (color, text) = when (status.lowercase()) {
        "approved" -> Success to "Approved"
        "pending" -> Warning to "Pending"
        "rejected", "denied" -> Error to "Rejected"
        "leave_at_gate" -> TealMain to "Leave at Gate"
        else -> TextHint to status
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(color.copy(alpha = 0.1f))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            color = color,
            fontWeight = FontWeight.Medium
        )
    }
}