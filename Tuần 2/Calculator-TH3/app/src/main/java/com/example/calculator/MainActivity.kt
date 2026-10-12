package com.example.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.math.BigDecimal

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { CalculatorScreen() }
    }
}

@androidx.compose.runtime.Composable
private fun CalculatorScreen() {
    var number1 by remember { mutableStateOf("") }
    var number2 by remember { mutableStateOf("") }
    var operation by remember { mutableStateOf("+") }

    val a = number1.toDoubleOrNull()
    val b = number2.toDoubleOrNull()
    val result = when {
        number1.isBlank() || number2.isBlank() -> null
        a == null || b == null || !a.isFinite() || !b.isFinite() -> "Nhập số hợp lệ"
        operation == "/" && b == 0.0 -> "Không thể chia cho 0"
        else -> {
            val answer = when (operation) {
                "+" -> a + b
                "-" -> a - b
                "*" -> a * b
                else -> a / b
            }
            if (answer.isFinite()) {
                BigDecimal.valueOf(answer).stripTrailingZeros().toPlainString()
            } else {
                "Kết quả quá lớn"
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.weight(1f))
        Text("Thực hành 03", fontSize = 14.sp)
        Spacer(Modifier.height(56.dp))

        OutlinedTextField(
            value = number1,
            onValueChange = { number1 = it },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        Spacer(Modifier.height(18.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            listOf(
                "+" to Color(0xFFDA4C42),
                "-" to Color(0xFFE8B246),
                "*" to Color(0xFF6640DC),
                "/" to Color(0xFF1F1F1F)
            ).forEach { (symbol, color) ->
                Button(
                    onClick = { operation = symbol },
                    modifier = Modifier.size(44.dp),
                    shape = RoundedCornerShape(7.dp),
                    border = if (operation == symbol) BorderStroke(2.dp, Color.Black) else null,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = color,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = symbol,
                        color = Color.White,
                        fontSize = 18.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(Modifier.height(18.dp))
        OutlinedTextField(
            value = number2,
            onValueChange = { number2 = it },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )

        Spacer(Modifier.height(10.dp))
        Text(
            text = result?.let { "Kết quả: $it" } ?: "Kết quả:",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 14.sp
        )
        Spacer(Modifier.weight(2f))
    }
}
