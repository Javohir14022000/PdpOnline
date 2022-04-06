package com.example.pdponline.dao

import androidx.room.*
import com.example.pdponline.entity.Course
import io.reactivex.rxjava3.core.Flowable

@Dao
interface CourseDao {

    @Insert
    fun addCourse(course: Course)

    @Update
    fun editCourse(course: Course)

    @Delete
    fun deleteCourse(course: Course)

    @Query("select * from course")
    fun getListCourse(): Flowable<List<Course>>

    @Query("select * from course")
    fun getListCourseMenu(): List<Course>

    @Query("select * from course where id =:id")
    fun getListCourseIdMenu(id: Int): Course

    @Query("select * from course where id =:id")
    fun getListCourseIdMenuId(id: Int): List<Course>
}