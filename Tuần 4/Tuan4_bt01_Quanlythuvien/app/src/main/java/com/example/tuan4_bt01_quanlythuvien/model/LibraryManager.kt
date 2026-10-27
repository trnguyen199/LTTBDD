package com.example.tuan4_bt01_quanlythuvien.model

object LibraryManager {
    private val students = mutableListOf<Student>()
    private val books = mutableListOf<Book>()
    var currentStudent: Student? = null

    init {
        // Tạo sẵn 3 sinh viên theo yêu cầu
        val studentA = Student("1", "Nguyen Van A", "SV001")
        val studentB = Student("2", "Tran Thi B", "SV002")
        val studentC = Student("3", "Le Van C", "SV003")

        students.add(studentA)
        students.add(studentB)
        students.add(studentC)

        currentStudent = studentA

        // Tạo sẵn sách mẫu
        val book1 = Book("B001", "Sách 01", "Tác giả 1")
        val book2 = Book("B002", "Sách 02", "Tác giả 2")
        val book3 = Book("B003", "Sách 03", "Tác giả 3")

        books.add(book1)
        books.add(book2)
        books.add(book3)

        // Mặc định sinh viên A mượn Sách 01 và Sách 02 giống hình minh họa slide 63
        studentA.borrowBook(book1)
        studentA.borrowBook(book2)
    }

    fun getStudents(): List<Student> = students
    fun getBooks(): List<Book> = books

    fun addStudent(name: String, studentCode: String): Student {
        val id = (students.size + 1).toString()
        val newStudent = Student(id, name, studentCode)
        students.add(newStudent)
        return newStudent
    }

    fun addBook(code: String, title: String, author: String): Book {
        val newBook = Book(code, title, author)
        books.add(newBook)
        return newBook
    }
}
