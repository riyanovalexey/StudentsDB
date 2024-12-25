package com.example.studentsdb2.data.repository

import com.example.studentsdb2.data.dao.StudentDao
import com.example.studentsdb2.data.entity.StudentEntity
import com.example.studentsdb2.data.model.Student
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StudentRepository(private val studentDao: StudentDao) {
    fun getAllStudents(): Flow<List<Student>> {
        return studentDao.getAllStudents().map { entities ->
            entities.map { it.toStudent() }
        }
    }

    fun searchStudents(query: String): Flow<List<Student>> {
        return studentDao.searchStudents(query).map { entities ->
            entities.map { it.toStudent() }
        }
    }

    suspend fun addStudent(student: Student): Long {
        return studentDao.insertStudent(StudentEntity.fromStudent(student))
    }

    suspend fun updateStudent(student: Student) {
        studentDao.updateStudent(StudentEntity.fromStudent(student))
    }

    suspend fun deleteStudent(student: Student) {
        studentDao.deleteStudent(StudentEntity.fromStudent(student))
    }
} 