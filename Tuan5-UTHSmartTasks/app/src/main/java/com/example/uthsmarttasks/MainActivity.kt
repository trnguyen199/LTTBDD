package com.example.uthsmarttasks

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uthsmarttasks.ui.components.AddTaskDialog
import com.example.uthsmarttasks.ui.components.EditAvatarDialog
import com.example.uthsmarttasks.ui.screens.HomeScreen
import com.example.uthsmarttasks.ui.screens.TaskDetailScreen
import com.example.uthsmarttasks.ui.screens.TaskListScreen
import com.example.uthsmarttasks.ui.theme.BackgroundLight
import com.example.uthsmarttasks.ui.theme.BluePrimary
import com.example.uthsmarttasks.ui.theme.UTHSmartTasksTheme
import com.example.uthsmarttasks.ui.viewmodel.TaskViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: TaskViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UTHSmartTasksTheme {
                MainAppScreen(viewModel = viewModel)
            }
        }
    }
}

enum class Screen {
    HOME, TASK_LIST, TASK_DETAIL, NOTIFICATIONS, SETTINGS
}

@Composable
fun MainAppScreen(viewModel: TaskViewModel) {
    val tasks by viewModel.tasks.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedTaskDetail by viewModel.selectedTaskDetail.collectAsState()
    val userName by viewModel.userName.collectAsState()
    val userAvatarBgIndex by viewModel.userAvatarBgIndex.collectAsState()

    var currentScreen by remember { mutableStateOf(Screen.HOME) }
    var showAddTaskDialog by remember { mutableStateOf(false) }
    var showEditAvatarDialog by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            if (currentScreen != Screen.TASK_DETAIL) {
                BottomAppBar(
                    containerColor = Color.White,
                    contentColor = Color(0xFF64748B),
                    tonalElevation = 8.dp,
                    actions = {
                        // Tab 0: Trang chủ
                        NavigationBarItem(
                            selected = currentScreen == Screen.HOME,
                            onClick = { currentScreen = Screen.HOME },
                            icon = { Icon(Icons.Default.Home, contentDescription = "Trang chủ") },
                            label = { Text("Trang chủ", fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = BluePrimary,
                                selectedTextColor = BluePrimary,
                                unselectedIconColor = Color(0xFF94A3B8),
                                unselectedTextColor = Color(0xFF94A3B8),
                                indicatorColor = Color.Transparent
                            )
                        )

                        // Tab 1: Danh sách
                        NavigationBarItem(
                            selected = currentScreen == Screen.TASK_LIST,
                            onClick = { currentScreen = Screen.TASK_LIST },
                            icon = { Icon(Icons.Default.FormatListBulleted, contentDescription = "Danh sách") },
                            label = { Text("Danh sách", fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = BluePrimary,
                                selectedTextColor = BluePrimary,
                                unselectedIconColor = Color(0xFF94A3B8),
                                unselectedTextColor = Color(0xFF94A3B8),
                                indicatorColor = Color.Transparent
                            )
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        // Tab 2: Thông báo
                        NavigationBarItem(
                            selected = currentScreen == Screen.NOTIFICATIONS,
                            onClick = { currentScreen = Screen.NOTIFICATIONS },
                            icon = { Icon(Icons.Default.Notifications, contentDescription = "Thông báo") },
                            label = { Text("Thông báo", fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = BluePrimary,
                                selectedTextColor = BluePrimary,
                                unselectedIconColor = Color(0xFF94A3B8),
                                unselectedTextColor = Color(0xFF94A3B8),
                                indicatorColor = Color.Transparent
                            )
                        )

                        // Tab 3: Cài đặt
                        NavigationBarItem(
                            selected = currentScreen == Screen.SETTINGS,
                            onClick = { currentScreen = Screen.SETTINGS },
                            icon = { Icon(Icons.Default.Settings, contentDescription = "Cài đặt") },
                            label = { Text("Cài đặt", fontSize = 11.sp) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = BluePrimary,
                                selectedTextColor = BluePrimary,
                                unselectedIconColor = Color(0xFF94A3B8),
                                unselectedTextColor = Color(0xFF94A3B8),
                                indicatorColor = Color.Transparent
                            )
                        )
                    }
                )
            }
        },
        floatingActionButton = {
            if (currentScreen != Screen.TASK_DETAIL) {
                FloatingActionButton(
                    onClick = { showAddTaskDialog = true },
                    shape = CircleShape,
                    containerColor = BluePrimary,
                    contentColor = Color.White,
                    modifier = Modifier.offset(y = 40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Thêm công việc",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                Screen.HOME -> {
                    HomeScreen(
                        tasks = tasks,
                        userName = userName,
                        userAvatarBgIndex = userAvatarBgIndex,
                        onEditAvatarClick = { showEditAvatarDialog = true },
                        onToggleTaskDone = { viewModel.toggleTaskDone(it) },
                        onTaskClick = { task ->
                            viewModel.selectTask(task)
                            currentScreen = Screen.TASK_DETAIL
                        },
                        onNavigateToTaskList = { categoryFilter ->
                            if (!categoryFilter.isNullOrBlank()) {
                                viewModel.selectCategory(categoryFilter)
                            }
                            currentScreen = Screen.TASK_LIST
                        }
                    )
                }

                Screen.TASK_LIST -> {
                    TaskListScreen(
                        tasks = tasks,
                        searchQuery = searchQuery,
                        onSearchQueryChange = { viewModel.updateSearchQuery(it) },
                        selectedCategory = selectedCategory,
                        onSelectCategory = { viewModel.selectCategory(it) },
                        onToggleTaskDone = { viewModel.toggleTaskDone(it) },
                        onTaskClick = { task ->
                            viewModel.selectTask(task)
                            currentScreen = Screen.TASK_DETAIL
                        }
                    )
                }

                Screen.TASK_DETAIL -> {
                    TaskDetailScreen(
                        task = selectedTaskDetail,
                        onBack = { currentScreen = Screen.HOME },
                        onToggleTaskDone = { viewModel.toggleTaskDone(it) },
                        onToggleSubtaskDone = { taskId, subtaskId ->
                            viewModel.toggleSubtaskDone(taskId, subtaskId)
                        },
                        onAddSubtask = { taskId, title ->
                            viewModel.addSubtask(taskId, title)
                        },
                        onEditSubtaskTitle = { taskId, subtaskId, newTitle ->
                            viewModel.editSubtaskTitle(taskId, subtaskId, newTitle)
                        },
                        onDeleteSubtask = { taskId, subtaskId ->
                            viewModel.deleteSubtask(taskId, subtaskId)
                        },
                        onEditTask = { taskId, title, note, category, date, time, priority, remind ->
                            viewModel.editTask(taskId, title, note, category, date, time, priority, remind)
                        },
                        onDeleteTask = { taskId ->
                            viewModel.deleteTask(taskId)
                            currentScreen = Screen.HOME
                        }
                    )
                }

                Screen.NOTIFICATIONS -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(BackgroundLight)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Thông báo",
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
                                Text("Nhắc nhở công việc", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = BluePrimary)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Sắp đến hạn: Nộp bài tập Lập trình di động (09:00)", fontSize = 13.sp, color = Color(0xFF334155))
                                Text("Trước 15 phút", fontSize = 11.sp, color = Color(0xFF94A3B8))
                            }
                        }
                    }
                }

                Screen.SETTINGS -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(BackgroundLight)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Cài đặt",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Thông tin người dùng", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Tên hiện tại: $userName", fontSize = 13.sp, color = Color(0xFF334155))
                                Spacer(modifier = Modifier.height(10.dp))
                                Button(
                                    onClick = { showEditAvatarDialog = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Text("Đổi tên & Avatar")
                                }
                            }
                        }

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Cấu hình hệ thống", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text("API URL: https://share.tryapps.store/mock/teach/tasks", fontSize = 12.sp, color = Color(0xFF64748B))
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(
                                    onClick = { viewModel.loadTasks() },
                                    colors = ButtonDefaults.buttonColors(containerColor = BluePrimary),
                                    shape = RoundedCornerShape(10.dp)
                                ) {
                                    Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Tải lại dữ liệu từ API")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showEditAvatarDialog) {
        EditAvatarDialog(
            currentName = userName,
            currentColorIndex = userAvatarBgIndex,
            onDismiss = { showEditAvatarDialog = false },
            onSaveProfile = { name, colorIndex ->
                viewModel.updateUserProfile(name, colorIndex)
            }
        )
    }

    if (showAddTaskDialog) {
        AddTaskDialog(
            onDismiss = { showAddTaskDialog = false },
            onCreateTask = { title, note, category, date, time, priority, remind, subtasks ->
                viewModel.createTask(title, note, category, date, time, priority, remind, subtasks)
            }
        )
    }
}
