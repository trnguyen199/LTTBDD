package com.example.tuan4_bt01_quanlythuvien.model

// 1. TÍNH TRỪU TƯỢNG (Abstraction): Interface định nghĩa hành vi mượn trả tài liệu
interface Borrowable {
    fun borrowItem(): Boolean
    fun returnItem(): Boolean
}

// 2. TÍNH TRỪU TƯỢNG (Abstraction): Lớp trừu tượng Person đại diện cho con người trong hệ thống
abstract class Person(private var id: String, private var name: String) {
    // 3. TÍNH ĐÓNG GÓI (Encapsulation): Thuộc tính private, truy xuất thông qua getter/setter
    fun getId(): String = id
    fun setId(id: String) { this.id = id }

    open fun getName(): String = name
    open fun setName(name: String) { this.name = name }

    // Phương thức trừu tượng minh họa TÍNH ĐA HÌNH (Polymorphism)
    abstract fun getRole(): String
}

// 4. TÍNH KẾ THỪA (Inheritance): Student kế thừa từ Person
class Student(id: String, name: String, private var studentCode: String) : Person(id, name) {
    private val borrowedBooks = mutableListOf<Book>()

    fun getStudentCode(): String = studentCode
    fun setStudentCode(code: String) { this.studentCode = code }

    fun borrowBook(book: Book): Boolean {
        return if (book.borrowItem()) {
            borrowedBooks.add(book)
            true
        } else {
            false
        }
    }

    fun returnBook(book: Book): Boolean {
        return if (borrowedBooks.contains(book) && book.returnItem()) {
            borrowedBooks.remove(book)
            true
        } else {
            false
        }
    }

    fun getBorrowedBooks(): List<Book> = borrowedBooks

    // TÍNH ĐA HÌNH (Polymorphism): Ghi đè phương thức getRole()
    override fun getRole(): String {
        return "Sinh viên"
    }
}

// TÍNH TRỪU TƯỢNG (Abstraction): Lớp trừu tượng LibraryItem thực thi interface Borrowable
abstract class LibraryItem(private var code: String, private var title: String) : Borrowable {
    private var isAvailable: Boolean = true

    fun getCode(): String = code
    fun setCode(code: String) { this.code = code }

    fun getTitle(): String = title
    fun setTitle(title: String) { this.title = title }

    fun isAvailable(): Boolean = isAvailable
    fun setAvailable(available: Boolean) { isAvailable = available }

    abstract fun getItemType(): String
}

// TÍNH KẾ THỪA (Inheritance) & TÍNH ĐA HÌNH (Polymorphism): Book kế thừa LibraryItem
class Book(code: String, title: String, private var author: String) : LibraryItem(code, title) {
    fun getAuthor(): String = author
    fun setAuthor(author: String) { this.author = author }

    override fun getItemType(): String {
        return "Sách giáo khoa / Tài liệu"
    }

    override fun borrowItem(): Boolean {
        return if (isAvailable()) {
            setAvailable(false)
            true
        } else {
            false
        }
    }

    override fun returnItem(): Boolean {
        return if (!isAvailable()) {
            setAvailable(true)
            true
        } else {
            false
        }
    }
}
