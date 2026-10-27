package com.example.tuan4_bt01_quanlythuvien.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.tuan4_bt01_quanlythuvien.R
import com.example.tuan4_bt01_quanlythuvien.model.Book
import com.example.tuan4_bt01_quanlythuvien.model.Student

class BookAdapter(
    private val books: List<Book>,
    private val getCurrentStudent: () -> Student?,
    private val onActionCallback: () -> Unit
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkBox: CheckBox = itemView.findViewById(R.id.checkbox_book)
        val tvTitle: TextView = itemView.findViewById(R.id.tv_book_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.tvTitle.text = book.getTitle()

        val student = getCurrentStudent()
        val isBorrowedByCurrent = student?.getBorrowedBooks()?.contains(book) == true

        holder.checkBox.isChecked = isBorrowedByCurrent
        val isAvailableForOther = book.isAvailable() || isBorrowedByCurrent
        holder.itemView.isEnabled = isAvailableForOther
        holder.itemView.alpha = if (isAvailableForOther) 1.0f else 0.6f

        holder.itemView.setOnClickListener {
            val currStudent = getCurrentStudent()
            if (currStudent == null) {
                Toast.makeText(holder.itemView.context, "Vui lòng chọn sinh viên trước!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (isBorrowedByCurrent) {
                currStudent.returnBook(book)
                Toast.makeText(holder.itemView.context, "Đã trả: ${book.getTitle()}", Toast.LENGTH_SHORT).show()
            } else {
                if (book.isAvailable()) {
                    currStudent.borrowBook(book)
                    Toast.makeText(holder.itemView.context, "Đã mượn: ${book.getTitle()}", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(holder.itemView.context, "Sách này đang được mượn bởi người khác!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }
            onActionCallback()
        }
    }

    override fun getItemCount(): Int = books.size
}
