package com.example.studentsdb2.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.studentsdb2.data.model.Student
import java.util.Date

@Entity(
    tableName = "students",
    foreignKeys = [
        ForeignKey(
            entity = GroupEntity::class,
            parentColumns = ["id"],
            childColumns = ["groupId"],
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class StudentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val lastName: String,
    val firstName: String,
    val middleName: String,
    val birthDate: Date,
    val groupId: Long
) {
    fun toStudent() = Student(id, lastName, firstName, middleName, birthDate, groupId)

    companion object {
        fun fromStudent(student: Student) = StudentEntity(
            student.id,
            student.lastName,
            student.firstName,
            student.middleName,
            student.birthDate,
            student.groupId
        )
    }
} 