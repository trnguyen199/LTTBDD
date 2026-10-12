package com.example.studentexercises

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Navy = Color(0xFF304A78)
private val ScreenBackground = Color(0xFFF3F3F8)
private val MutedText = Color(0xFF777D87)
private val StudentData = listOf(
    Student("Nguyễn Văn An", "an@sv.edu.vn", "0901 234 567"),
    Student("Trần Thị Bình", null, "0912 888 999"),
    Student("Lê Minh Chi", "chi@sv.edu.vn", null),
    Student("Phạm Quốc Dũng", null, null),
    Student("Võ Thanh Em", "em@sv.edu.vn", "0933 111 222")
)
private val GradeData = listOf(
    9.0 to "Nguyễn Văn An",
    7.0 to "Trần Thị Bình",
    5.5 to "Lê Minh Chi",
    3.0 to "Phạm Quốc Dũng",
    11.0 to "Võ Thanh Em"
)
private val ScoreData = listOf(
    StudentScore("Nguyễn Văn An", 9.0),
    StudentScore("Trần Thị Bình", 7.2),
    StudentScore("Lê Minh Chi", null),
    StudentScore("Phạm Quốc Dũng", 4.5),
    StudentScore("Võ Thanh Em", 5.8)
)

private data class Student(val name: String, val email: String?, val phone: String?)
private data class StudentScore(val name: String, val score: Double?)

private enum class ExerciseTab(val title: String, val shortTitle: String) {
    Students("Danh sách sinh viên", "Bài 1"),
    Grades("Xếp loại học lực", "Bài 2"),
    Summary("Bảng điểm lớp", "Bài 3")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                StudentExercisesApp()
            }
        }
    }
}

@Composable
private fun StudentExercisesApp() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tab = ExerciseTab.entries[selectedTab]

    Scaffold(
        containerColor = ScreenBackground,
        topBar = { AppHeader(title = tab.title) },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                ExerciseTab.entries.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = {
                            Text(
                                text = "${index + 1}",
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(
                                        color = if (selectedTab == index) Navy else ScreenBackground,
                                        shape = CircleShape
                                    )
                                    .padding(top = 2.dp),
                                color = if (selectedTab == index) Color.White else MutedText,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        },
                        label = { Text(item.shortTitle) },
                        alwaysShowLabel = true
                    )
                }
            }
        }
    ) { innerPadding ->
        when (ExerciseTab.entries[selectedTab]) {
            ExerciseTab.Students -> StudentsScreen(Modifier.padding(innerPadding))
            ExerciseTab.Grades -> GradesScreen(Modifier.padding(innerPadding))
            ExerciseTab.Summary -> SummaryScreen(Modifier.padding(innerPadding))
        }
    }
}

@Composable
private fun AppHeader(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(Navy)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun StudentsScreen(modifier: Modifier = Modifier) {
    var missingEmailCount = 0
    for (student in StudentData) {
        if (student.email == null) missingEmailCount++
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(StudentData) { student ->
            StudentCard(student)
        }
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE5EDF7)),
                shape = RoundedCornerShape(10.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Tổng: ${StudentData.size} sinh viên", color = Navy, fontSize = 13.sp)
                    Text("Thiếu email: $missingEmailCount", color = Navy, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
private fun StudentCard(student: Student) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 11.dp)) {
            Text(student.name, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            DetailLine("Email", student.email)
            DetailLine("SĐT", student.phone)
        }
    }
}

@Composable
private fun DetailLine(label: String, value: String?) {
    Row {
        Text("$label: ", color = MutedText, fontSize = 12.sp)
        Text(
            text = value ?: "Chưa cập nhật",
            color = if (value == null) Color(0xFFB36B55) else MutedText,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun GradesScreen(modifier: Modifier = Modifier) {
    val sampleScore = 8.5
    val sampleGrade = classify(sampleScore)

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Điểm trung bình", color = MutedText, fontSize = 12.sp)
                    Text(
                        text = sampleScore.toString(),
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF202630)
                    )
                    GradeBadge(sampleGrade, Modifier.fillMaxWidth())
                }
            }
        }
        item {
            Text(
                "KIỂM TRA THÊM",
                fontSize = 11.sp,
                color = MutedText,
                fontWeight = FontWeight.SemiBold
            )
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(horizontal = 12.dp)) {
                    GradeData.forEachIndexed { index, (score, name) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Điểm ${score.cleanNumber()}", fontSize = 13.sp)
                            GradeBadge(classify(score))
                        }
                        if (index < GradeData.lastIndex) {
                            Spacer(
                                Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(ScreenBackground)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GradeBadge(grade: String, modifier: Modifier = Modifier) {
    val color = when (grade) {
        "Giỏi" -> Color(0xFF4E805B)
        "Khá" -> Color(0xFF5477A8)
        "Trung bình" -> Color(0xFFA87939)
        "Yếu" -> Color(0xFFB46455)
        else -> MutedText
    }
    Text(
        text = grade,
        modifier = modifier
            .background(color.copy(alpha = 0.12f), RoundedCornerShape(7.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        color = color,
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        textAlign = androidx.compose.ui.text.style.TextAlign.Center
    )
}

private fun classify(score: Double): String = when {
    score !in 0.0..10.0 -> "Không hợp lệ"
    score >= 8.0 -> "Giỏi"
    score >= 6.5 -> "Khá"
    score >= 5.0 -> "Trung bình"
    else -> "Yếu"
}

@Composable
private fun SummaryScreen(modifier: Modifier = Modifier) {
    var total = 0.0
    var gradedCount = 0
    var excellentCount = 0
    var missingCount = 0
    var highestScore: Double? = null

    for (student in ScoreData) {
        val score = student.score
        if (score == null) {
            missingCount++
        } else {
            total += score
            gradedCount++
            if (classify(score) == "Giỏi") excellentCount++
            if (highestScore == null || score > highestScore) highestScore = score
        }
    }
    val average = if (gradedCount > 0) total / gradedCount else 0.0

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Điểm trung bình lớp", color = Color(0xFFD8E2F2), fontSize = 12.sp)
                    Text(
                        text = average.cleanNumber(2),
                        color = Color.White,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SummaryMetric("Giỏi", excellentCount.toString())
                        SummaryMetric("Chưa có điểm", missingCount.toString())
                        SummaryMetric("Cao nhất", highestScore?.cleanNumber() ?: "—")
                    }
                }
            }
        }
        item {
            Text(
                "DANH SÁCH · ${ScoreData.size} SINH VIÊN",
                color = MutedText,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
        items(ScoreData) { student ->
            ScoreCard(student)
        }
    }
}

@Composable
private fun SummaryMetric(label: String, value: String) {
    Column {
        Text(label, color = Color(0xFFD8E2F2), fontSize = 10.sp)
        Text(value, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ScoreCard(student: StudentScore) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(11.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(student.name, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Text(
                    student.score?.let { classify(it) } ?: "Chưa có điểm",
                    color = student.score?.let { gradeColor(classify(it)) } ?: MutedText,
                    fontSize = 11.sp
                )
            }
            Text(
                student.score?.cleanNumber() ?: "—",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF202630)
            )
        }
    }
}

private fun gradeColor(grade: String): Color = when (grade) {
    "Giỏi" -> Color(0xFF4E805B)
    "Khá" -> Color(0xFF5477A8)
    "Trung bình" -> Color(0xFFA87939)
    "Yếu" -> Color(0xFFB46455)
    else -> MutedText
}

private fun Double.cleanNumber(maxFractionDigits: Int = 1): String =
    "%.${maxFractionDigits}f".format(this).trimEnd('0').trimEnd('.')
