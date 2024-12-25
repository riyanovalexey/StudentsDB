package com.example.studentsdb2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.studentsdb2.data.dao.GroupDao
import com.example.studentsdb2.data.dao.StudentDao
import com.example.studentsdb2.data.entity.GroupEntity
import com.example.studentsdb2.data.entity.StudentEntity

@Database(
    entities = [GroupEntity::class, StudentEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun groupDao(): GroupDao
    abstract fun studentDao(): StudentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "students_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
} 