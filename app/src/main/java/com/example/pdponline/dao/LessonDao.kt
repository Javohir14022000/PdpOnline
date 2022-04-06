package com.example.pdponline.dao

import androidx.room.*
import com.example.pdponline.entity.Lesson
import io.reactivex.rxjava3.core.Flowable

@Dao
interface LessonDao {

    @Insert
    fun addLesson(lesson: Lesson)

    @Update
    fun editDao(lesson: Lesson)

    @Delete
    fun deleteDao(lesson: Lesson)

    @Query("select * from lesson where module_id =:moduleId order by lesson_number asc")
    fun getLessonByIdModule(moduleId: Int): Flowable<List<Lesson>>

    @Query("select * from lesson where module_id =:moduleId")
    fun getLessonByModule(moduleId: Int): List<Lesson>

    @Query("select * from lesson where id =:id")
    fun getLessonId(id: Int): Lesson

    @Query("select * from lesson where id =:id")
    fun getListLessonId(id: Int): List<Lesson>


    @Query("select * from lesson where lesson_number =:id")
    fun getListLessonNumber(id: String): List<Lesson>
}