package com.example.studentsdb2.ui.students

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentsdb2.data.AppDatabase
import com.example.studentsdb2.data.model.Group
import com.example.studentsdb2.data.model.Student
import com.example.studentsdb2.data.repository.GroupRepository
import com.example.studentsdb2.data.repository.StudentRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class StudentsViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val studentRepository = StudentRepository(database.studentDao())
    private val groupRepository = GroupRepository(database.groupDao())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _groupFilter = MutableStateFlow<String?>(null)
    val groupFilter: StateFlow<String?> = _groupFilter

    private val _groups = MutableStateFlow<List<Group>>(emptyList())
    val groups: StateFlow<List<Group>> = _groups

    val students = combine(
        searchQuery,
        groupFilter,
        studentRepository.getAllStudents()
    ) { query, group, allStudents ->
        allStudents.filter { student ->
            val matchesQuery = if (query.isEmpty()) {
                true
            } else {
                student.lastName.contains(query, ignoreCase = true)
            }
            
            val matchesGroup = if (group == null) {
                true
            } else {
                groups.value.find { it.id == student.groupId }?.number == group
            }
            
            matchesQuery && matchesGroup
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        loadGroups()
    }

    private fun loadGroups() {
        viewModelScope.launch {
            groupRepository.getAllGroups().collect {
                _groups.value = it
            }
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setGroupFilter(group: String?) {
        _groupFilter.value = group
    }

    fun addStudent(student: Student) {
        viewModelScope.launch {
            studentRepository.addStudent(student)
        }
    }

    fun updateStudent(student: Student) {
        viewModelScope.launch {
            studentRepository.updateStudent(student)
        }
    }

    fun deleteStudent(student: Student) {
        viewModelScope.launch {
            studentRepository.deleteStudent(student)
        }
    }
} 