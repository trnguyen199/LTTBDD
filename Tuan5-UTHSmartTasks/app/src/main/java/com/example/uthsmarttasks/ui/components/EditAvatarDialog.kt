package com.example.uthsmarttasks.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.uthsmarttasks.ui.theme.BluePrimary

val AvatarColors = listOf(
    Color(0xFFE2E8F0) to Color(0xFF2563EB), // Blue
    Color(0xFFDCFCE7) to Color(0xFF16A34A), // Green
    Color(0xFFF3E8FF) to Color(0xFF9333EA), // Purple
    Color(0xFFFFEDD5) to Color(0xFFEA580C), // Orange
    Color(0xFFFEE2E2) to Color(0xFFDC2626)  // Red
)

@Composable
fun EditAvatarDialog(
    currentName: String,
    currentColorIndex: Int,
    onDismiss: () -> Unit,
    onSaveProfile: (name: String, colorIndex: Int) -> Unit
) {
    var name by remember { mutableStateOf(currentName) }
    var selectedColorIndex by remember { mutableStateOf(currentColorIndex.coerceIn(0, AvatarColors.size - 1)) }

    val currentInitial = if (name.isNotBlank()) name.trim().take(1).uppercase() else "K"
    val (bgCol, textCol) = AvatarColors[selectedColorIndex]

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Đổi Avatar & Tên người dùng",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Preview Avatar Circle
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(bgCol),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentInitial,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = textCol
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Name Input
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Tên người dùng") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Color choices
                Text(
                    text = "Chọn màu Avatar",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF475569),
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    AvatarColors.forEachIndexed { index, (bg, text) ->
                        val isSelected = selectedColorIndex == index
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(bg)
                                .border(
                                    width = if (isSelected) 3.dp else 0.dp,
                                    color = if (isSelected) BluePrimary else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable { selectedColorIndex = index },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = currentInitial,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = text
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Hủy", color = Color(0xFF64748B))
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            if (name.isNotBlank()) {
                                onSaveProfile(name.trim(), selectedColorIndex)
                                onDismiss()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Lưu thay đổi", color = Color.White)
                    }
                }
            }
        }
    }
}
