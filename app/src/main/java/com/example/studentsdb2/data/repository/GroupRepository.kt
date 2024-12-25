package com.example.studentsdb2.data.repository

import com.example.studentsdb2.data.dao.GroupDao
import com.example.studentsdb2.data.entity.GroupEntity
import com.example.studentsdb2.data.model.Group
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GroupRepository(private val groupDao: GroupDao) {
    fun getAllGroups(): Flow<List<Group>> {
        return groupDao.getAllGroups().map { entities ->
            entities.map { it.toGroup() }
        }
    }

    suspend fun addGroup(group: Group): Long {
        return groupDao.insertGroup(GroupEntity.fromGroup(group))
    }

    suspend fun updateGroup(group: Group) {
        groupDao.updateGroup(GroupEntity.fromGroup(group))
    }

    suspend fun deleteGroup(group: Group) {
        groupDao.deleteGroup(GroupEntity.fromGroup(group))
    }

    suspend fun canDeleteGroup(groupId: Long): Boolean {
        return groupDao.getStudentCountInGroup(groupId) == 0
    }
} 