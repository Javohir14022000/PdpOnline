package com.example.pdponline.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pdponline.database.AppDatabase
import com.example.pdponline.databinding.ItemMenuCourseBinding
import com.example.pdponline.entity.Course
import com.example.pdponline.entity.Module
import java.lang.Appendable

class RvAdapter(val context: Context, var list: List<Course>, var listener: OnItemClickListener) :
    RecyclerView.Adapter<RvAdapter.Vh>() {

    lateinit var listModule: List<Module>
    lateinit var appDatabase: AppDatabase
    lateinit var moduleAdapter: ModuleAdapter

    inner class Vh(var itemMenuCourseBinding: ItemMenuCourseBinding) :
        RecyclerView.ViewHolder(itemMenuCourseBinding.root) {
        fun onBind(course: Course, position: Int) {

            appDatabase = AppDatabase.getInstance(context)

            listModule = ArrayList(appDatabase.moduleDao().getModuleIdCourse(course.id))

            moduleAdapter = ModuleAdapter(listModule, object : ModuleAdapter.OnItemClickListener {
                override fun onClick(module: Module, position: Int) {

                }

            })

            itemMenuCourseBinding.apply {
                courseNameTv.text = course.courseName
                allModuleTv.setOnClickListener {
                    listener.onClick(course, position)
                }

//                if (appDatabase.moduleDao().getModuleByIdCourse(courseId = course.id)
//                        .isNotEmpty()
//                ) {
//                    rememberText.visibility = View.INVISIBLE
//                } else {
//                    rememberText.visibility = View.GONE
//                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemMenuCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size
    interface OnItemClickListener {
        fun onClick(course: Course, position: Int)
    }
}