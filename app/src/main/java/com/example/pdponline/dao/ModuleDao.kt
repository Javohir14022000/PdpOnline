package com.example.pdponline.dao

import androidx.room.*
import com.example.pdponline.entity.Module
import io.reactivex.rxjava3.core.Flowable

@Dao
interface ModuleDao {

    @Insert
    fun addModule(module: Module)

    @Update
    fun editModule(module: Module)

    @Delete
    fun deleteModule(module: Module)

    @Query("select * from module where course_id = :courseId order by module_number asc ")
    fun getModuleByIdCourse(courseId: Int): Flowable<List<Module>>

    @Query("select * from module  where course_id = :courseId")
    fun getModuleIdCourse(courseId: Int): List<Module>

    @Query("select * from module  where id = :id")
    fun getModuleId(id: Int): Module
}