package com.example.tuan4_bt01_quanlythuvien.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tuan4_bt01_quanlythuvien.R
import com.example.tuan4_bt01_quanlythuvien.model.Student

class StudentAdapter(
    private val students: List<Student>,
    private val onStudentClick: ((Student) -> Unit)? = null
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_student_name)
        val tvCode: TextView = itemView.findViewById(R.id.tv_student_code)
        val tvBorrowedCount: TextView = itemView.findViewById(R.id.tv_borrowed_count)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = students[position]
        holder.tvName.text = student.getName()
        holder.tvCode.text = "Mã: ${student.getStudentCode()} | Vai trò: ${student.getRole()}"
        holder.tvBorrowedCount.text = "Số sách đang mượn: ${student.getBorrowedBooks().size}"

        holder.itemView.setOnClickListener {
            onStudentClick?.invoke(student)
        }
    }

    override fun getItemCount(): Int = students.size
}
