package com.example.studentsdb2.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.studentsdb2.data.model.Group

@Entity(tableName = "groups")
data class GroupEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val number: String,
    val facultyName: String
) {
    fun toGroup() = Group(id, number, facultyName)

    companion object {
        fun fromGroup(group: Group) = GroupEntity(group.id, group.number, group.facultyName)
    }
} 