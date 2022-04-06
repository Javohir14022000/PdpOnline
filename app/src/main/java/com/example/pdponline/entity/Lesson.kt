package com.example.pdponline.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lesson")
data class Lesson(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "lesson_name")
    var lessonName: String,
    @ColumnInfo(name = "lesson_info")
    var lessonInfo: String,
    @ColumnInfo(name = "lesson_number")
    var lessonNumber: String,
    @ColumnInfo(name = "module_id")
    var moduleId: Int


)
