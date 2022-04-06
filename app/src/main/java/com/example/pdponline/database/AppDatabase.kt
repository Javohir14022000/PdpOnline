package com.example.pdponline.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pdponline.dao.CourseDao
import com.example.pdponline.dao.LessonDao
import com.example.pdponline.dao.ModuleDao
import com.example.pdponline.entity.Course
import com.example.pdponline.entity.Lesson
import com.example.pdponline.entity.Module


@Database(entities = [Lesson::class, Module::class, Course::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun lessonDao(): LessonDao
    abstract fun moduleDao(): ModuleDao
    abstract fun courseDao(): CourseDao

    companion object {

        private var appDatabase: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            if (appDatabase == null) {
                appDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "my_db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return appDatabase!!
        }
    }
}