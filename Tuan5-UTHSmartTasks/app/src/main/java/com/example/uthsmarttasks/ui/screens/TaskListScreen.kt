package com.example.uthsmarttasks.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uthsmarttasks.data.model.Task
import com.example.uthsmarttasks.ui.theme.*

@Composable
fun TaskListScreen(
    tasks: List<Task>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedCategory: String,
    onSelectCategory: (String) -> Unit,
    onToggleTaskDone: (Task) -> Unit,
    onTaskClick: (Task) -> Unit
) {
    // Filter by category and search query
    val filteredTasks = tasks.filter { task ->
        val matchesCategory = when (selectedCategory) {
            "Học tập" -> task.category.equals("study", ignoreCase = true)
            "Cuộc sống" -> task.category.equals("life", ignoreCase = true)
            else -> true
        }
        val matchesQuery = searchQuery.isBlank() ||
                task.title.contains(searchQuery, ignoreCase = true) ||
                task.note.contains(searchQuery, ignoreCase = true)
        matchesCategory && matchesQuery
    }

    val todoTasks = filteredTasks.filter { !it.done }
    val doneTasks = filteredTasks.filter { it.done }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // 1. Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Danh sách",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )

            Text(
                text = "${todoTasks.size} việc chưa xong",
                fontSize = 13.sp,
                color = Color(0xFF64748B)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            placeholder = { Text("Tìm công việc", fontSize = 14.sp, color = Color(0xFF94A3B8)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color(0xFF94A3B8)
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = BluePrimary,
                unfocusedBorderColor = Color(0xFFE2E8F0)
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(14.dp))

        // 3. Filter Chips (Tất cả, Học tập, Cuộc sống)
        val categories = listOf("Tất cả", "Học tập", "Cuộc sống")
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { category ->
                val isSelected = selectedCategory == category
                FilterChip(
                    selected = isSelected,
                    onClick = { onSelectCategory(category) },
                    label = {
                        Text(
                            text = category,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF0F172A),
                        selectedLabelColor = Color.White,
                        containerColor = Color(0xFFE2E8F0),
                        labelColor = Color(0xFF475569)
                    ),
                    border = null
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 4. Task List Sections (CẦN LÀM & ĐÃ XONG)
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // CẦN LÀM Section
            if (todoTasks.isNotEmpty()) {
                item {
                    Text(
                        text = "CẦN LÀM",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                items(todoTasks, key = { it.id }) { task ->
                    TaskListItem(
                        task = task,
                        onToggleDone = { onToggleTaskDone(task) },
                        onClick = { onTaskClick(task) }
                    )
                }
            }

            // ĐÃ XONG Section
            if (doneTasks.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "ĐÃ XONG",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF64748B),
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                items(doneTasks, key = { it.id }) { task ->
                    TaskListItem(
                        task = task,
                        onToggleDone = { onToggleTaskDone(task) },
                        onClick = { onTaskClick(task) }
                    )
                }
            }

            if (filteredTasks.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Không tìm thấy công việc nào",
                            color = Color(0xFF94A3B8),
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TaskListItem(
    task: Task,
    onToggleDone: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox
            IconButton(
                onClick = onToggleDone,
                modifier = Modifier.size(28.dp)
            ) {
                if (task.done) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Done",
                        tint = BluePrimary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.Circle,
                        contentDescription = "Not Done",
                        tint = Color(0xFFCBD5E1),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (task.done) Color(0xFF94A3B8) else Color(0xFF0F172A),
                    textDecoration = if (task.done) TextDecoration.LineThrough else TextDecoration.None,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    val (catBg, catText) = if (task.category.equals("study", ignoreCase = true)) {
                        CategoryStudyBg to CategoryStudyText
                    } else {
                        CategoryLifeBg to CategoryLifeText
                    }

                    Surface(
                        color = catBg,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = task.getCategoryDisplayName(),
                            fontSize = 10.sp,
                            color = catText,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = task.getFormattedDateTime(),
                        fontSize = 11.sp,
                        color = Color(0xFF64748B)
                    )
                }
            }

            // High Priority "Gấp" Badge
            if (!task.done && task.priority.equals("high", ignoreCase = true)) {
                Surface(
                    color = PriorityHighBg,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Gấp",
                        fontSize = 11.sp,
                        color = PriorityHighText,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }
        }
    }
}
