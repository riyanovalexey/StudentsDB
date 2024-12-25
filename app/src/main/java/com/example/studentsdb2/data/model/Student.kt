package com.example.studentsdb2.data.model

import java.util.Date

data class Student(
    val id: Long = 0,
    val lastName: String,
    val firstName: String,
    val middleName: String,
    val birthDate: Date,
    val groupId: Long
) 