package com.example.studentsdb2.data.dao

import androidx.room.*
import com.example.studentsdb2.data.entity.GroupEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GroupDao {
    @Query("SELECT * FROM groups ORDER BY number")
    fun getAllGroups(): Flow<List<GroupEntity>>

    @Insert
    suspend fun insertGroup(group: GroupEntity): Long

    @Update
    suspend fun updateGroup(group: GroupEntity)

    @Delete
    suspend fun deleteGroup(group: GroupEntity)

    @Query("SELECT COUNT(*) FROM students WHERE groupId = :groupId")
    suspend fun getStudentCountInGroup(groupId: Long): Int
} 