package com.example.uthsmarttasks.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.uthsmarttasks.data.model.Task
import com.example.uthsmarttasks.ui.theme.BluePrimary

@Composable
fun EditTaskDialog(
    task: Task,
    onDismiss: () -> Unit,
    onSaveTask: (
        title: String,
        note: String,
        category: String,
        date: String,
        time: String,
        priority: String,
        remind: Boolean
    ) -> Unit
) {
    var title by remember { mutableStateOf(task.title) }
    var note by remember { mutableStateOf(task.note) }
    var category by remember { mutableStateOf(task.category) }
    var date by remember { mutableStateOf(task.date) }
    var time by remember { mutableStateOf(task.time) }
    var priority by remember { mutableStateOf(task.priority) }
    var remind by remember { mutableStateOf(task.remind) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "Chỉnh sửa công việc",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Title
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Tên công việc *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Note
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Ghi chú") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Category selection
                Text("Danh mục", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = category.equals("study", ignoreCase = true),
                        onClick = { category = "study" },
                        label = { Text("Học tập") }
                    )
                    FilterChip(
                        selected = category.equals("life", ignoreCase = true),
                        onClick = { category = "life" },
                        label = { Text("Cuộc sống") }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Priority selection
                Text("Mức độ ưu tiên", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = priority.equals("low", ignoreCase = true),
                        onClick = { priority = "low" },
                        label = { Text("Thấp") }
                    )
                    FilterChip(
                        selected = priority.equals("medium", ignoreCase = true),
                        onClick = { priority = "medium" },
                        label = { Text("Vừa") }
                    )
                    FilterChip(
                        selected = priority.equals("high", ignoreCase = true),
                        onClick = { priority = "high" },
                        label = { Text("Cao (Gấp)") }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Date & Time
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = date,
                        onValueChange = { date = it },
                        label = { Text("Ngày") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = time,
                        onValueChange = { time = it },
                        label = { Text("Giờ") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Remind switch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Nhắc nhở trước 15 phút", fontSize = 14.sp, color = Color(0xFF334155))
                    Switch(
                        checked = remind,
                        onCheckedChange = { remind = it }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

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
                            if (title.isNotBlank()) {
                                onSaveTask(
                                    title.trim(),
                                    note.trim(),
                                    category,
                                    date.trim(),
                                    time.trim(),
                                    priority,
                                    remind
                                )
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
