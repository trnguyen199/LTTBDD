package com.example.tuan4_bt01_quanlythuvien

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EmailVerificationActivity : AppCompatActivity() {

    private lateinit var container: FrameLayout
    private var savedEmail: String = ""
    private var generatedCode: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_verification)
        container = findViewById(R.id.verification_container)

        showStep1()
    }

    private fun showStep1() {
        container.removeAllViews()
        val view = LayoutInflater.from(this).inflate(R.layout.screen_step1_email, container, false)
        container.addView(view)

        val etEmail = view.findViewById<EditText>(R.id.et_email)
        val btnNext = view.findViewById<Button>(R.id.btn_send_code)

        btnNext.setOnClickListener {
            val email = etEmail.text.toString().trim()
            if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                savedEmail = email
                // Sinh ngẫu nhiên mã 6 số
                generatedCode = (100000..999999).random().toString()
                showStep2()
            } else {
                Toast.makeText(this, "Vui lòng nhập email hợp lệ!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showStep2() {
        container.removeAllViews()
        val view = LayoutInflater.from(this).inflate(R.layout.screen_step2_code_display, container, false)
        container.addView(view)

        val tvCode = view.findViewById<TextView>(R.id.tv_generated_code)
        val btnNext = view.findViewById<Button>(R.id.btn_to_enter_code)

        tvCode.text = generatedCode

        btnNext.setOnClickListener {
            showStep3()
        }
    }

    private fun showStep3() {
        container.removeAllViews()
        val view = LayoutInflater.from(this).inflate(R.layout.screen_step3_enter_code, container, false)
        container.addView(view)

        val etCodeInput = view.findViewById<EditText>(R.id.et_enter_code)
        val btnVerify = view.findViewById<Button>(R.id.btn_verify_code)

        btnVerify.setOnClickListener {
            val enteredCode = etCodeInput.text.toString().trim()
            if (enteredCode == generatedCode) {
                Toast.makeText(this, "Xác thực thành công!", Toast.LENGTH_SHORT).show()
                showStep4()
            } else {
                Toast.makeText(this, "Mã 6 số không chính xác! (Mã đúng: $generatedCode)", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showStep4() {
        container.removeAllViews()
        val view = LayoutInflater.from(this).inflate(R.layout.screen_step4_password, container, false)
        container.addView(view)

        val etPassword = view.findViewById<EditText>(R.id.et_new_password)
        val btnSubmit = view.findViewById<Button>(R.id.btn_submit_password)

        btnSubmit.setOnClickListener {
            val password = etPassword.text.toString().trim()
            if (password.length >= 6) {
                Toast.makeText(this, "Submit thành công! Quay về Màn hình 1.", Toast.LENGTH_SHORT).show()
                // Quay trở lại màn hình 1
                showStep1()
            } else {
                Toast.makeText(this, "Mật khẩu phải có ít nhất 6 ký tự!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
