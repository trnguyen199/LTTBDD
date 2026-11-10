package com.example.uthsmarttasks.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.uthsmarttasks.data.model.SubTask
import com.example.uthsmarttasks.data.model.Task
import com.example.uthsmarttasks.data.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskViewModel(
    private val repository: TaskRepository = TaskRepository()
) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(repository.getLocalTasks())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow("Tất cả")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _selectedTaskDetail = MutableStateFlow<Task?>(null)
    val selectedTaskDetail: StateFlow<Task?> = _selectedTaskDetail.asStateFlow()

    private val _userName = MutableStateFlow("Khánh")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _userAvatarBgIndex = MutableStateFlow(0)
    val userAvatarBgIndex: StateFlow<Int> = _userAvatarBgIndex.asStateFlow()

    init {
        loadTasks()
    }

    fun loadTasks() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getTasks()
            if (result.isNotEmpty()) {
                _tasks.value = result
            }
            _isLoading.value = false

            val currentSelected = _selectedTaskDetail.value
            if (currentSelected != null) {
                _selectedTaskDetail.value = _tasks.value.find { it.id == currentSelected.id }
            }
        }
    }

    fun updateUserProfile(name: String, colorIndex: Int) {
        if (name.isNotBlank()) {
            _userName.value = name.trim()
        }
        _userAvatarBgIndex.value = colorIndex
    }

    fun selectTask(task: Task?) {
        _selectedTaskDetail.value = task
    }

    fun selectTaskById(taskId: Int) {
        val found = _tasks.value.find { it.id == taskId }
        _selectedTaskDetail.value = found
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun toggleTaskDone(task: Task) {
        viewModelScope.launch {
            val updatedTask = task.copy(done = !task.done)
            val newList = _tasks.value.map { if (it.id == task.id) updatedTask else it }
            _tasks.value = newList
            if (_selectedTaskDetail.value?.id == task.id) {
                _selectedTaskDetail.value = updatedTask
            }
            repository.updateTask(task.id, updatedTask)
        }
    }

    fun toggleSubtaskDone(taskId: Int, subtaskId: Int) {
        val task = _tasks.value.find { it.id == taskId } ?: return
        viewModelScope.launch {
            val updatedSubtasks = task.subtasks.map {
                if (it.id == subtaskId) it.copy(done = !it.done) else it
            }
            val updatedTask = task.copy(subtasks = updatedSubtasks)
            val newList = _tasks.value.map { if (it.id == taskId) updatedTask else it }
            _tasks.value = newList
            if (_selectedTaskDetail.value?.id == taskId) {
                _selectedTaskDetail.value = updatedTask
            }
            repository.updateTask(taskId, updatedTask)
        }
    }

    fun addSubtask(taskId: Int, title: String) {
        if (title.isBlank()) return
        val task = _tasks.value.find { it.id == taskId } ?: return
        viewModelScope.launch {
            val nextId = (task.subtasks.maxOfOrNull { it.id } ?: -1) + 1
            val newSubtask = SubTask(id = nextId, title = title, done = false)
            val updatedSubtasks = task.subtasks + newSubtask
            val updatedTask = task.copy(subtasks = updatedSubtasks)
            val newList = _tasks.value.map { if (it.id == taskId) updatedTask else it }
            _tasks.value = newList
            if (_selectedTaskDetail.value?.id == taskId) {
                _selectedTaskDetail.value = updatedTask
            }
            repository.updateTask(taskId, updatedTask)
        }
    }

    fun editSubtaskTitle(taskId: Int, subtaskId: Int, newTitle: String) {
        if (newTitle.isBlank()) return
        val task = _tasks.value.find { it.id == taskId } ?: return
        viewModelScope.launch {
            val updatedSubtasks = task.subtasks.map {
                if (it.id == subtaskId) it.copy(title = newTitle.trim()) else it
            }
            val updatedTask = task.copy(subtasks = updatedSubtasks)
            val newList = _tasks.value.map { if (it.id == taskId) updatedTask else it }
            _tasks.value = newList
            if (_selectedTaskDetail.value?.id == taskId) {
                _selectedTaskDetail.value = updatedTask
            }
            repository.updateTask(taskId, updatedTask)
        }
    }

    fun deleteSubtask(taskId: Int, subtaskId: Int) {
        val task = _tasks.value.find { it.id == taskId } ?: return
        viewModelScope.launch {
            val updatedSubtasks = task.subtasks.filterNot { it.id == subtaskId }
            val updatedTask = task.copy(subtasks = updatedSubtasks)
            val newList = _tasks.value.map { if (it.id == taskId) updatedTask else it }
            _tasks.value = newList
            if (_selectedTaskDetail.value?.id == taskId) {
                _selectedTaskDetail.value = updatedTask
            }
            repository.updateTask(taskId, updatedTask)
        }
    }

    fun createTask(
        title: String,
        note: String,
        category: String,
        date: String,
        time: String,
        priority: String,
        remind: Boolean,
        subtasks: List<String>
    ) {
        viewModelScope.launch {
            val subtaskList = subtasks.mapIndexed { index, subTitle ->
                SubTask(id = index, title = subTitle, done = false)
            }
            val newTask = Task(
                title = title,
                note = note,
                category = category,
                date = date,
                time = time,
                priority = priority,
                remind = remind,
                done = false,
                subtasks = subtaskList
            )
            val created = repository.createTask(newTask)
            val newList = _tasks.value + created
            _tasks.value = newList
        }
    }

    fun editTask(
        taskId: Int,
        title: String,
        note: String,
        category: String,
        date: String,
        time: String,
        priority: String,
        remind: Boolean
    ) {
        val task = _tasks.value.find { it.id == taskId } ?: return
        viewModelScope.launch {
            val updatedTask = task.copy(
                title = title,
                note = note,
                category = category,
                date = date,
                time = time,
                priority = priority,
                remind = remind
            )
            val newList = _tasks.value.map { if (it.id == taskId) updatedTask else it }
            _tasks.value = newList
            if (_selectedTaskDetail.value?.id == taskId) {
                _selectedTaskDetail.value = updatedTask
            }
            repository.updateTask(taskId, updatedTask)
        }
    }

    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            repository.deleteTask(taskId)
            _tasks.value = _tasks.value.filterNot { it.id == taskId }
            if (_selectedTaskDetail.value?.id == taskId) {
                _selectedTaskDetail.value = null
            }
        }
    }
}
