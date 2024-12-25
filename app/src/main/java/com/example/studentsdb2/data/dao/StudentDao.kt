package com.example.studentsdb2.data.dao

import androidx.room.*
import com.example.studentsdb2.data.entity.StudentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StudentDao {
    @Query("SELECT * FROM students ORDER BY lastName, firstName, middleName")
    fun getAllStudents(): Flow<List<StudentEntity>>

    @Query("SELECT * FROM students WHERE lastName LIKE '%' || :query || '%' OR groupId IN (SELECT id FROM groups WHERE number LIKE '%' || :query || '%')")
    fun searchStudents(query: String): Flow<List<StudentEntity>>

    @Insert
    suspend fun insertStudent(student: StudentEntity): Long

    @Update
    suspend fun updateStudent(student: StudentEntity)

    @Delete
    suspend fun deleteStudent(student: StudentEntity)
} 