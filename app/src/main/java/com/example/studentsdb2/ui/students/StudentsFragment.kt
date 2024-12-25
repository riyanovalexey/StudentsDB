package com.example.studentsdb2.ui.students

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.studentsdb2.R
import com.example.studentsdb2.data.model.Student
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class StudentsFragment : Fragment(R.layout.fragment_students) {
    private val viewModel: StudentsViewModel by viewModels()
    private lateinit var adapter: StudentsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView(view)
        setupSearch(view)
        setupGroupFilter(view)
        setupFab(view)
        observeData()
    }

    private fun setupRecyclerView(view: View) {
        adapter = StudentsAdapter(
            onEditClick = { student ->
                showStudentDialog(student)
            },
            onDeleteClick = { student ->
                viewModel.deleteStudent(student)
            }
        )
        view.findViewById<RecyclerView>(R.id.studentsRecyclerView).adapter = adapter
    }

    private fun setupSearch(view: View) {
        view.findViewById<TextInputEditText>(R.id.searchEditText).doAfterTextChanged { text ->
            viewModel.setSearchQuery(text?.toString() ?: "")
        }
    }

    private fun setupGroupFilter(view: View) {
        val groupFilterAutoComplete = view.findViewById<AutoCompleteTextView>(R.id.groupFilterAutoComplete)
        val groupFilterLayout = view.findViewById<TextInputLayout>(R.id.groupFilterLayout)
        
        val groupAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            mutableListOf<String>()
        )
        groupFilterAutoComplete.setAdapter(groupAdapter)

        var selectedGroup: String? = null

        fun updateEndIcon(hasSelection: Boolean) {
            if (hasSelection) {
                groupFilterLayout.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
                groupFilterLayout.setEndIconOnClickListener {
                    groupFilterAutoComplete.setText("")
                    selectedGroup = null
                    viewModel.setGroupFilter(null)
                    updateEndIcon(false)
                }
            } else {
                groupFilterLayout.endIconMode = TextInputLayout.END_ICON_DROPDOWN_MENU
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.groups.collect { groups ->
                groupAdapter.clear()
                groupAdapter.addAll(groups.map { it.number })
                groupAdapter.notifyDataSetChanged()
            }
        }

        groupFilterAutoComplete.setOnItemClickListener { _, _, position, _ ->
            val group = groupAdapter.getItem(position) ?: return@setOnItemClickListener
            selectedGroup = group
            viewModel.setGroupFilter(group)
            updateEndIcon(true)
        }

        updateEndIcon(false)

        groupFilterAutoComplete.doAfterTextChanged { text ->
            if (text.isNullOrEmpty() && selectedGroup != null) {
                selectedGroup = null
                viewModel.setGroupFilter(null)
                updateEndIcon(false)
            }
        }
    }

    private fun setupFab(view: View) {
        view.findViewById<FloatingActionButton>(R.id.addStudentFab).setOnClickListener {
            showStudentDialog(null)
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            combine(
                viewModel.students,
                viewModel.groups
            ) { students, groups ->
                students.map { student ->
                    StudentWithGroup(
                        student = student,
                        groupNumber = groups.find { it.id == student.groupId }?.number ?: ""
                    )
                }
            }.collect { studentsWithGroups ->
                adapter.submitList(studentsWithGroups)
            }
        }
    }

    private fun showStudentDialog(student: Student?) {
        viewLifecycleOwner.lifecycleScope.launch {
            val groups = viewModel.groups.value
            if (groups.isNotEmpty()) {
                StudentDialog(
                    student = student,
                    groups = groups,
                    onSave = { newStudent ->
                        if (student == null) {
                            viewModel.addStudent(newStudent)
                        } else {
                            viewModel.updateStudent(newStudent)
                        }
                    }
                ).show(childFragmentManager, "student_dialog")
            }
        }
    }
} 