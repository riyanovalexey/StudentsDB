package com.example.studentsdb2.ui.students

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.studentsdb2.R
import com.example.studentsdb2.data.model.Group
import com.example.studentsdb2.data.model.Student
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.*

class StudentDialog(
    private val student: Student? = null,
    private val groups: List<Group>,
    private val onSave: (Student) -> Unit
) : DialogFragment() {

    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
    private var selectedDate: Date = student?.birthDate ?: Date()
    private var selectedGroupId: Long = student?.groupId ?: groups.firstOrNull()?.id ?: 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_student, null)

        val lastNameEdit = view.findViewById<TextInputEditText>(R.id.lastNameEditText)
        val firstNameEdit = view.findViewById<TextInputEditText>(R.id.firstNameEditText)
        val middleNameEdit = view.findViewById<TextInputEditText>(R.id.middleNameEditText)
        val birthDateEdit = view.findViewById<TextInputEditText>(R.id.birthDateEditText)
        val groupAutoComplete = view.findViewById<android.widget.AutoCompleteTextView>(R.id.groupAutoComplete)

        student?.let {
            lastNameEdit.setText(it.lastName)
            firstNameEdit.setText(it.firstName)
            middleNameEdit.setText(it.middleName)
            birthDateEdit.setText(dateFormat.format(it.birthDate))
        }

        birthDateEdit.setOnClickListener {
            showDatePicker(birthDateEdit)
        }

        val groupAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            groups.map { it.number }
        )
        groupAutoComplete.setAdapter(groupAdapter)
        groupAutoComplete.setOnItemClickListener { _, _, position, _ ->
            selectedGroupId = groups[position].id
        }

        groups.find { it.id == student?.groupId }?.let {
            groupAutoComplete.setText(it.number, false)
        }

        builder.setView(view)
            .setTitle(if (student == null) R.string.add_student else R.string.edit)
            .setPositiveButton(R.string.save) { _, _ ->
                val newStudent = Student(
                    id = student?.id ?: 0,
                    lastName = lastNameEdit.text.toString(),
                    firstName = firstNameEdit.text.toString(),
                    middleName = middleNameEdit.text.toString(),
                    birthDate = selectedDate,
                    groupId = selectedGroupId
                )
                onSave(newStudent)
            }
            .setNegativeButton(R.string.cancel, null)

        return builder.create()
    }

    private fun showDatePicker(birthDateEdit: TextInputEditText) {
        val calendar = Calendar.getInstance().apply { time = selectedDate }
        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                calendar.set(year, month, day)
                selectedDate = calendar.time
                birthDateEdit.setText(dateFormat.format(selectedDate))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
} 