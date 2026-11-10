package com.example.uthsmarttasks.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.uthsmarttasks.ui.theme.BluePrimary

@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onCreateTask: (
        title: String,
        note: String,
        category: String,
        date: String,
        time: String,
        priority: String,
        remind: Boolean,
        subtasks: List<String>
    ) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("study") }
    var date by remember { mutableStateOf("2026-10-20") }
    var time by remember { mutableStateOf("09:00") }
    var priority by remember { mutableStateOf("medium") }
    var remind by remember { mutableStateOf(true) }

    var newSubtaskText by remember { mutableStateOf("") }
    val subtasksList = remember { mutableStateListOf<String>() }

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
                    text = "Thêm công việc mới",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Tên công việc *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Ghi chú") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 3
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Danh mục", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = category == "study",
                        onClick = { category = "study" },
                        label = { Text("Học tập") }
                    )
                    FilterChip(
                        selected = category == "life",
                        onClick = { category = "life" },
                        label = { Text("Cuộc sống") }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text("Mức độ ưu tiên", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = priority == "low",
                        onClick = { priority = "low" },
                        label = { Text("Thấp") }
                    )
                    FilterChip(
                        selected = priority == "medium",
                        onClick = { priority = "medium" },
                        label = { Text("Vừa") }
                    )
                    FilterChip(
                        selected = priority == "high",
                        onClick = { priority = "high" },
                        label = { Text("Cao (Gấp)") }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

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

                // Subtasks Addition Field
                Text("Việc nhỏ", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF475569))
                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newSubtaskText,
                        onValueChange = { newSubtaskText = it },
                        placeholder = { Text("Thêm việc nhỏ...", fontSize = 13.sp) },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            if (newSubtaskText.isNotBlank()) {
                                subtasksList.add(newSubtaskText.trim())
                                newSubtaskText = ""
                            }
                        }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Subtask", tint = BluePrimary)
                    }
                }

                if (subtasksList.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        subtasksList.forEachIndexed { index, itemText ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFF1F5F9),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(itemText, fontSize = 13.sp, color = Color(0xFF334155))
                                    IconButton(
                                        onClick = { subtasksList.removeAt(index) },
                                        modifier = Modifier.size(20.dp)
                                    ) {
                                        Icon(Icons.Default.Close, contentDescription = "Remove", tint = Color(0xFF94A3B8))
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

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
                                onCreateTask(
                                    title.trim(),
                                    note.trim(),
                                    category,
                                    date.trim(),
                                    time.trim(),
                                    priority,
                                    remind,
                                    subtasksList.toList()
                                )
                                onDismiss()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Tạo công việc", color = Color.White)
                    }
                }
            }
        }
    }
}
