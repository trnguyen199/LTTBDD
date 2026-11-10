package com.example.uthsmarttasks.data.model

import com.google.gson.annotations.SerializedName

data class Task(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("title") val title: String = "",
    @SerializedName("note") val note: String = "",
    @SerializedName("category") val category: String = "life", // "study", "life", "work", etc.
    @SerializedName("date") val date: String = "", // "YYYY-MM-DD"
    @SerializedName("time") val time: String = "", // "HH:mm"
    @SerializedName("priority") val priority: String = "medium", // "low", "medium", "high"
    @SerializedName("remind") val remind: Boolean = false,
    @SerializedName("done") val done: Boolean = false,
    @SerializedName("subtasks") val subtasks: List<SubTask> = emptyList()
) {
    fun getCategoryDisplayName(): String {
        return when (category.lowercase()) {
            "study" -> "Học tập"
            "life" -> "Cuộc sống"
            "work" -> "Công việc"
            else -> category.ifEmpty { "Khác" }
        }
    }

    fun getPriorityDisplayName(): String {
        return when (priority.lowercase()) {
            "high" -> "Ưu tiên cao"
            "medium" -> "Ưu tiên vừa"
            "low" -> "Ưu tiên thấp"
            else -> "Bình thường"
        }
    }

    fun getFormattedDateTime(): String {
        val datePart = if (date.isNotBlank()) date else "Hôm nay"
        val timePart = if (time.isNotBlank()) time else ""
        return if (timePart.isNotBlank()) "$datePart, $timePart" else datePart
    }
}

data class SubTask(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("title") val title: String = "",
    @SerializedName("done") val done: Boolean = false
)
