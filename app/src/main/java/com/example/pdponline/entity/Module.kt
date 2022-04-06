package com.example.pdponline.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "module")
data class Module(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "module_name")
    var moduleName: String,
    @ColumnInfo(name = "module_number")
    var moduleNumber: String,
    @ColumnInfo(name = "course_id")
    var courseId: Int,
)
