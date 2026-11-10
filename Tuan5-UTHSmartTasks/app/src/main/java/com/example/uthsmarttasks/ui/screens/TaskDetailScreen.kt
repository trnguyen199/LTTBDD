package com.example.uthsmarttasks.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uthsmarttasks.data.model.SubTask
import com.example.uthsmarttasks.data.model.Task
import com.example.uthsmarttasks.ui.components.EditTaskDialog
import com.example.uthsmarttasks.ui.theme.*

@Composable
fun TaskDetailScreen(
    task: Task?,
    onBack: () -> Unit,
    onToggleTaskDone: (Task) -> Unit,
    onToggleSubtaskDone: (taskId: Int, subtaskId: Int) -> Unit,
    onAddSubtask: (taskId: Int, title: String) -> Unit,
    onEditSubtaskTitle: (taskId: Int, subtaskId: Int, newTitle: String) -> Unit,
    onDeleteSubtask: (taskId: Int, subtaskId: Int) -> Unit,
    onEditTask: (
        taskId: Int,
        title: String,
        note: String,
        category: String,
        date: String,
        time: String,
        priority: String,
        remind: Boolean
    ) -> Unit,
    onDeleteTask: (taskId: Int) -> Unit
) {
    if (task == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Không tìm thấy công việc", color = Color(0xFF64748B))
        }
        return
    }

    var showEditTaskDialog by remember { mutableStateOf(false) }
    var showAddSubtaskDialog by remember { mutableStateOf(false) }
    var newSubtaskTitle by remember { mutableStateOf("") }
    var editingSubtask by remember { mutableStateOf<SubTask?>(null) }
    var editSubtaskText by remember { mutableStateOf("") }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    val completedSubtasks = task.subtasks.count { it.done }
    val totalSubtasks = task.subtasks.size

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BackgroundLight)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back button "< Trang chủ"
                Row(
                    modifier = Modifier.clickable { onBack() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = BluePrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Trang chủ",
                        fontSize = 15.sp,
                        color = BluePrimary,
                        fontWeight = FontWeight.Medium
                    )
                }

                Text(
                    text = "Chi tiết",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )

                TextButton(onClick = { showEditTaskDialog = true }) {
                    Text(
                        text = "Sửa",
                        fontSize = 15.sp,
                        color = BluePrimary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { onToggleTaskDone(task) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (task.done) Color(0xFF64748B) else BluePrimary
                    )
                ) {
                    Text(
                        text = if (task.done) "Đánh dấu chưa hoàn thành" else "Đánh dấu hoàn thành",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = { showDeleteConfirmDialog = true }) {
                    Text(
                        text = "Xóa công việc",
                        fontSize = 14.sp,
                        color = Color(0xFFEF4444),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    val (catBg, catText) = if (task.category.equals("study", ignoreCase = true)) {
                        CategoryStudyBg to CategoryStudyText
                    } else {
                        CategoryLifeBg to CategoryLifeText
                    }

                    Surface(
                        color = catBg,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = task.getCategoryDisplayName(),
                            fontSize = 12.sp,
                            color = catText,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Surface(
                        color = Color(0xFFF1F5F9),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = task.getPriorityDisplayName(),
                            fontSize = 12.sp,
                            color = Color(0xFF475569),
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = task.title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = "Deadline",
                                tint = Color(0xFF64748B),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Hạn chót",
                                fontSize = 14.sp,
                                color = Color(0xFF64748B),
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = task.getFormattedDateTime(),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF0F172A)
                            )
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = Color(0xFFF1F5F9)
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Reminder",
                                tint = Color(0xFF64748B),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Nhắc nhở",
                                fontSize = 14.sp,
                                color = Color(0xFF64748B),
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = if (task.remind) "Trước 15 phút" else "Không",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF0F172A)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Ghi chú",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF64748B)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Text(
                        text = task.note.ifBlank { "Không có ghi chú" },
                        fontSize = 14.sp,
                        color = if (task.note.isBlank()) Color(0xFF94A3B8) else Color(0xFF334155),
                        modifier = Modifier.padding(16.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Việc nhỏ",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B)
                    )

                    Text(
                        text = "$completedSubtasks/$totalSubtasks",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF64748B)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            items(task.subtasks, key = { it.id }) { subtask ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { onToggleSubtaskDone(task.id, subtask.id) },
                            modifier = Modifier.size(24.dp)
                        ) {
                            if (subtask.done) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Done",
                                    tint = BluePrimary,
                                    modifier = Modifier.size(22.dp)
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Outlined.Circle,
                                    contentDescription = "Not Done",
                                    tint = Color(0xFFCBD5E1),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Text(
                            text = subtask.title,
                            fontSize = 14.sp,
                            color = if (subtask.done) Color(0xFF94A3B8) else Color(0xFF0F172A),
                            textDecoration = if (subtask.done) TextDecoration.LineThrough else TextDecoration.None,
                            modifier = Modifier.weight(1f)
                        )

                        IconButton(
                            onClick = {
                                editingSubtask = subtask
                                editSubtaskText = subtask.title
                            },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = Color(0xFF94A3B8),
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        IconButton(
                            onClick = { onDeleteSubtask(task.id, subtask.id) },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color(0xFF94A3B8),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            item {
                TextButton(
                    onClick = { showAddSubtaskDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add",
                        tint = BluePrimary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Thêm việc nhỏ",
                        fontSize = 14.sp,
                        color = BluePrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    // Dialog Edit Task
    if (showEditTaskDialog) {
        EditTaskDialog(
            task = task,
            onDismiss = { showEditTaskDialog = false },
            onSaveTask = { title, note, category, date, time, priority, remind ->
                onEditTask(task.id, title, note, category, date, time, priority, remind)
            }
        )
    }

    // Dialog Thêm việc nhỏ
    if (showAddSubtaskDialog) {
        AlertDialog(
            onDismissRequest = { showAddSubtaskDialog = false },
            title = { Text("Thêm việc nhỏ") },
            text = {
                OutlinedTextField(
                    value = newSubtaskTitle,
                    onValueChange = { newSubtaskTitle = it },
                    placeholder = { Text("Tên việc nhỏ...") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (newSubtaskTitle.isNotBlank()) {
                            onAddSubtask(task.id, newSubtaskTitle.trim())
                            newSubtaskTitle = ""
                            showAddSubtaskDialog = false
                        }
                    }
                ) {
                    Text("Thêm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddSubtaskDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }

    // Dialog Edit việc nhỏ
    if (editingSubtask != null) {
        AlertDialog(
            onDismissRequest = { editingSubtask = null },
            title = { Text("Chỉnh sửa việc nhỏ") },
            text = {
                OutlinedTextField(
                    value = editSubtaskText,
                    onValueChange = { editSubtaskText = it },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (editSubtaskText.isNotBlank()) {
                            onEditSubtaskTitle(task.id, editingSubtask!!.id, editSubtaskText.trim())
                            editingSubtask = null
                        }
                    }
                ) {
                    Text("Lưu")
                }
            },
            dismissButton = {
                TextButton(onClick = { editingSubtask = null }) {
                    Text("Hủy")
                }
            }
        )
    }

    // Dialog Xác nhận Xóa task
    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text("Xóa công việc") },
            text = { Text("Bạn có chắc chắn muốn xóa công việc này không?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteTask(task.id)
                        showDeleteConfirmDialog = false
                        onBack()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
                ) {
                    Text("Xóa", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }
}
