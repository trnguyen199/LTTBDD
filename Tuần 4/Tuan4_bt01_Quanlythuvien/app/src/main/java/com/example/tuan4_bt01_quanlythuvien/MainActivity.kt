package com.example.tuan4_bt01_quanlythuvien

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tuan4_bt01_quanlythuvien.adapter.BookAdapter
import com.example.tuan4_bt01_quanlythuvien.adapter.StudentAdapter
import com.example.tuan4_bt01_quanlythuvien.model.LibraryManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigation = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_management -> {
                    loadManagementView()
                    true
                }
                R.id.nav_books -> {
                    loadBooksView()
                    true
                }
                R.id.nav_students -> {
                    loadStudentsView()
                    true
                }
                else -> false
            }
        }

        // Mặc định load tab Quản lý
        loadManagementView()
    }

    private fun loadManagementView() {
        val container = findViewById<ViewGroup>(R.id.fragment_container)
        container.removeAllViews()
        val view = LayoutInflater.from(this).inflate(R.layout.fragment_management, container, false)
        container.addView(view)

        val tvCurrentStudent = view.findViewById<TextView>(R.id.tv_current_student)
        val btnChangeStudent = view.findViewById<Button>(R.id.btn_change_student)
        val recyclerBooks = view.findViewById<RecyclerView>(R.id.recycler_books_management)
        val btnActionAdd = view.findViewById<Button>(R.id.btn_action_add)

        fun updateStudentDisplay() {
            val curr = LibraryManager.currentStudent
            if (curr != null) {
                tvCurrentStudent.text = "${curr.getName()} (${curr.getStudentCode()}) - Đang mượn: ${curr.getBorrowedBooks().size} sách"
            } else {
                tvCurrentStudent.text = "Chưa chọn sinh viên"
            }
        }

        updateStudentDisplay()

        btnChangeStudent.setOnClickListener {
            showSelectStudentDialog {
                updateStudentDisplay()
                recyclerBooks.adapter?.notifyDataSetChanged()
            }
        }

        recyclerBooks.layoutManager = LinearLayoutManager(this)
        val bookAdapter = BookAdapter(
            LibraryManager.getBooks(),
            { LibraryManager.currentStudent },
            {
                updateStudentDisplay()
                recyclerBooks.adapter?.notifyDataSetChanged()
            }
        )
        recyclerBooks.adapter = bookAdapter

        btnActionAdd.setOnClickListener {
            showAddOptionsDialog {
                recyclerBooks.adapter?.notifyDataSetChanged()
                updateStudentDisplay()
            }
        }
    }

    private fun loadBooksView() {
        val container = findViewById<ViewGroup>(R.id.fragment_container)
        container.removeAllViews()
        val view = LayoutInflater.from(this).inflate(R.layout.fragment_books, container, false)
        container.addView(view)

        val recyclerBooks = view.findViewById<RecyclerView>(R.id.recycler_books)
        val btnAddBook = view.findViewById<Button>(R.id.btn_add_book)

        recyclerBooks.layoutManager = LinearLayoutManager(this)
        val adapter = BookAdapter(
            LibraryManager.getBooks(),
            { LibraryManager.currentStudent },
            { recyclerBooks.adapter?.notifyDataSetChanged() }
        )
        recyclerBooks.adapter = adapter

        btnAddBook.setOnClickListener {
            showAddBookDialog {
                recyclerBooks.adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun loadStudentsView() {
        val container = findViewById<ViewGroup>(R.id.fragment_container)
        container.removeAllViews()
        val view = LayoutInflater.from(this).inflate(R.layout.fragment_students, container, false)
        container.addView(view)

        val recyclerStudents = view.findViewById<RecyclerView>(R.id.recycler_students)
        val btnAddStudent = view.findViewById<Button>(R.id.btn_add_student)

        recyclerStudents.layoutManager = LinearLayoutManager(this)
        val adapter = StudentAdapter(LibraryManager.getStudents()) { student ->
            LibraryManager.currentStudent = student
            Toast.makeText(this, "Đã chọn sinh viên: ${student.getName()}", Toast.LENGTH_SHORT).show()
        }
        recyclerStudents.adapter = adapter

        btnAddStudent.setOnClickListener {
            showAddStudentDialog {
                recyclerStudents.adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun showSelectStudentDialog(onSelected: () -> Unit) {
        val students = LibraryManager.getStudents()
        val names = students.map { "${it.getName()} (${it.getStudentCode()})" }.toTypedArray()

        AlertDialog.Builder(this)
            .setTitle("Chọn sinh viên")
            .setItems(names) { _, which ->
                LibraryManager.currentStudent = students[which]
                Toast.makeText(this, "Đã chọn: ${students[which].getName()}", Toast.LENGTH_SHORT).show()
                onSelected()
            }
            .show()
    }

    private fun showAddOptionsDialog(onChanged: () -> Unit) {
        val options = arrayOf("Thêm Sách mới", "Thêm Sinh viên mới")
        AlertDialog.Builder(this)
            .setTitle("Tùy chọn thêm")
            .setItems(options) { _, which ->
                if (which == 0) {
                    showAddBookDialog { onChanged() }
                } else {
                    showAddStudentDialog { onChanged() }
                }
            }
            .show()
    }

    private fun showAddBookDialog(onAdded: () -> Unit) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_book, null)
        val etCode = dialogView.findViewById<EditText>(R.id.et_book_code)
        val etTitle = dialogView.findViewById<EditText>(R.id.et_book_title)
        val etAuthor = dialogView.findViewById<EditText>(R.id.et_book_author)

        AlertDialog.Builder(this)
            .setTitle("Thêm sách mới")
            .setView(dialogView)
            .setPositiveButton("Thêm") { _, _ ->
                val code = etCode.text.toString().trim()
                val title = etTitle.text.toString().trim()
                val author = etAuthor.text.toString().trim()

                if (code.isNotEmpty() && title.isNotEmpty()) {
                    LibraryManager.addBook(code, title, author.ifEmpty { "Không rõ" })
                    Toast.makeText(this, "Đã thêm sách thành công!", Toast.LENGTH_SHORT).show()
                    onAdded()
                } else {
                    Toast.makeText(this, "Vui lòng nhập mã và tên sách!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun showAddStudentDialog(onAdded: () -> Unit) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_student, null)
        val etName = dialogView.findViewById<EditText>(R.id.et_student_name)
        val etCode = dialogView.findViewById<EditText>(R.id.et_student_code)

        AlertDialog.Builder(this)
            .setTitle("Thêm sinh viên mới")
            .setView(dialogView)
            .setPositiveButton("Thêm") { _, _ ->
                val name = etName.text.toString().trim()
                val code = etCode.text.toString().trim()

                if (name.isNotEmpty() && code.isNotEmpty()) {
                    LibraryManager.addStudent(name, code)
                    Toast.makeText(this, "Đã thêm sinh viên thành công!", Toast.LENGTH_SHORT).show()
                    onAdded()
                } else {
                    Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }
}
