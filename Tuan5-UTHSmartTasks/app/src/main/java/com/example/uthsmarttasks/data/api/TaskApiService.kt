package com.example.uthsmarttasks.data.api

import com.example.uthsmarttasks.data.model.Task
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface TaskApiService {

    @GET("mock/teach/tasks")
    suspend fun getTasks(
        @Header("Authorization") token: String = "Bearer mock_token"
    ): Response<List<Task>>

    @GET("mock/teach/tasks/{id}")
    suspend fun getTaskById(
        @Path("id") id: Int,
        @Header("Authorization") token: String = "Bearer mock_token"
    ): Response<Task>

    @POST("mock/teach/tasks")
    suspend fun createTask(
        @Body task: Task,
        @Header("Authorization") token: String = "Bearer mock_token"
    ): Response<Task>

    @PATCH("mock/teach/tasks/{id}")
    suspend fun updateTask(
        @Path("id") id: Int,
        @Body taskUpdates: Map<String, @JvmSuppressWildcards Any?>,
        @Header("Authorization") token: String = "Bearer mock_token"
    ): Response<Task>

    @DELETE("mock/teach/tasks/{id}")
    suspend fun deleteTask(
        @Path("id") id: Int,
        @Header("Authorization") token: String = "Bearer mock_token"
    ): Response<Unit>
}
