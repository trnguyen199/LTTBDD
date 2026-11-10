package com.example.uthsmarttasks.data.repository

import com.example.uthsmarttasks.data.api.TaskApiService
import com.example.uthsmarttasks.data.model.SubTask
import com.example.uthsmarttasks.data.model.Task
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class TaskRepository {

    private val BASE_URL = "https://share.tryapps.store/"

    private val apiService: TaskApiService by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TaskApiService::class.java)
    }

    private val localTasks = mutableListOf(
        Task(
            id = 4,
            title = "Tập thể dục 30 phút",
            note = "",
            category = "life",
            date = "2026-10-20",
            time = "06:30",
            priority = "medium",
            remind = true,
            done = true,
            subtasks = emptyList()
        ),
        Task(
            id = 1,
            title = "Nộp bài tập Lập trình di động",
            note = "Nộp file .zip lên hệ thống của trường. Nhớ kèm ảnh chụp màn hình app.",
            category = "study",
            date = "2026-10-20",
            time = "09:00",
            priority = "high",
            remind = true,
            done = true,
            subtasks = listOf(
                SubTask(id = 0, title = "Hoàn thiện màn hình đăng nhập", done = true),
                SubTask(id = 1, title = "Chụp ảnh màn hình app", done = true),
                SubTask(id = 2, title = "Nén file và nộp bài", done = false)
            )
        ),
        Task(
            id = 2,
            title = "Ôn chương 3 – Cấu trúc dữ liệu",
            note = "Tập trung phần cây nhị phân và bảng băm.",
            category = "study",
            date = "2026-10-20",
            time = "14:00",
            priority = "high",
            remind = true,
            done = false,
            subtasks = listOf(
                SubTask(id = 0, title = "Đọc lại slide bài giảng", done = true),
                SubTask(id = 1, title = "Làm 10 bài tập cuối chương", done = false)
            )
        ),
        Task(
            id = 3,
            title = "Đi chợ mua đồ cho tuần",
            note = "Mua thực phẩm tươi sống và sữa.",
            category = "life",
            date = "2026-10-20",
            time = "17:30",
            priority = "medium",
            remind = false,
            done = false,
            subtasks = emptyList()
        ),
        Task(
            id = 5,
            title = "Gọi điện về nhà",
            note = "Hỏi thăm sức khỏe bố mẹ.",
            category = "life",
            date = "2026-10-21",
            time = "21:00",
            priority = "low",
            remind = true,
            done = false,
            subtasks = emptyList()
        ),
        Task(
            id = 6,
            title = "Họp nhóm đồ án",
            note = "Thảo luận phân chia công việc.",
            category = "study",
            date = "2026-10-22",
            time = "20:00",
            priority = "high",
            remind = true,
            done = false,
            subtasks = emptyList()
        )
    )

    fun getLocalTasks(): List<Task> = localTasks.toList()

    suspend fun getTasks(): List<Task> {
        return try {
            val response = apiService.getTasks()
            if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                response.body()!!
            } else {
                localTasks
            }
        } catch (e: Exception) {
            localTasks
        }
    }

    suspend fun getTaskById(id: Int): Task? {
        return try {
            val response = apiService.getTaskById(id)
            if (response.isSuccessful && response.body() != null) {
                response.body()
            } else {
                localTasks.find { it.id == id }
            }
        } catch (e: Exception) {
            localTasks.find { it.id == id }
        }
    }

    suspend fun createTask(task: Task): Task {
        return try {
            val response = apiService.createTask(task)
            if (response.isSuccessful && response.body() != null) {
                val created = response.body()!!
                localTasks.add(created)
                created
            } else {
                val newId = (localTasks.maxOfOrNull { it.id } ?: 0) + 1
                val newLocalTask = task.copy(id = newId)
                localTasks.add(newLocalTask)
                newLocalTask
            }
        } catch (e: Exception) {
            val newId = (localTasks.maxOfOrNull { it.id } ?: 0) + 1
            val newLocalTask = task.copy(id = newId)
            localTasks.add(newLocalTask)
            newLocalTask
        }
    }

    suspend fun updateTask(id: Int, updatedTask: Task): Task {
        val index = localTasks.indexOfFirst { it.id == id }
        if (index != -1) {
            localTasks[index] = updatedTask
        }
        try {
            val updates = mapOf<String, Any?>(
                "done" to updatedTask.done,
                "title" to updatedTask.title,
                "note" to updatedTask.note,
                "subtasks" to updatedTask.subtasks
            )
            apiService.updateTask(id, updates)
        } catch (_: Exception) {
        }
        return updatedTask
    }

    suspend fun deleteTask(id: Int): Boolean {
        localTasks.removeAll { it.id == id }
        try {
            apiService.deleteTask(id)
        } catch (_: Exception) {
        }
        return true
    }
}
