package com.example.studentsdb2.ui.students

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.studentsdb2.R
import com.example.studentsdb2.data.model.Student
import java.text.SimpleDateFormat
import java.util.Locale

class StudentsAdapter(
    private val onEditClick: (Student) -> Unit,
    private val onDeleteClick: (Student) -> Unit
) : ListAdapter<StudentWithGroup, StudentsAdapter.StudentViewHolder>(StudentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val fullNameTextView: TextView = itemView.findViewById(R.id.fullNameTextView)
        private val birthDateTextView: TextView = itemView.findViewById(R.id.birthDateTextView)
        private val groupTextView: TextView = itemView.findViewById(R.id.groupTextView)
        private val editButton: ImageButton = itemView.findViewById(R.id.editStudentButton)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.deleteStudentButton)
        private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

        fun bind(studentWithGroup: StudentWithGroup) {
            val student = studentWithGroup.student
            fullNameTextView.text = "${student.lastName} ${student.firstName} ${student.middleName}"
            birthDateTextView.text = dateFormat.format(student.birthDate)
            groupTextView.text = studentWithGroup.groupNumber

            editButton.setOnClickListener { onEditClick(student) }
            deleteButton.setOnClickListener { onDeleteClick(student) }
        }
    }

    private class StudentDiffCallback : DiffUtil.ItemCallback<StudentWithGroup>() {
        override fun areItemsTheSame(oldItem: StudentWithGroup, newItem: StudentWithGroup): Boolean {
            return oldItem.student.id == newItem.student.id
        }

        override fun areContentsTheSame(oldItem: StudentWithGroup, newItem: StudentWithGroup): Boolean {
            return oldItem == newItem
        }
    }
}

data class StudentWithGroup(
    val student: Student,
    val groupNumber: String
) 