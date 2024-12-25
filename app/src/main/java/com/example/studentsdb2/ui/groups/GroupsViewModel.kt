package com.example.studentsdb2.ui.groups

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentsdb2.data.AppDatabase
import com.example.studentsdb2.data.model.Group
import com.example.studentsdb2.data.repository.GroupRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GroupsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: GroupRepository
    private val _groups = MutableStateFlow<List<Group>>(emptyList())
    val groups: StateFlow<List<Group>> = _groups

    init {
        val database = AppDatabase.getDatabase(application)
        repository = GroupRepository(database.groupDao())
        loadGroups()
    }

    private fun loadGroups() {
        viewModelScope.launch {
            repository.getAllGroups().collect {
                _groups.value = it
            }
        }
    }

    fun addGroup(group: Group) {
        viewModelScope.launch {
            repository.addGroup(group)
        }
    }

    fun updateGroup(group: Group) {
        viewModelScope.launch {
            repository.updateGroup(group)
        }
    }

    suspend fun canDeleteGroup(groupId: Long): Boolean {
        return repository.canDeleteGroup(groupId)
    }

    fun deleteGroup(group: Group) {
        viewModelScope.launch {
            repository.deleteGroup(group)
        }
    }
} 