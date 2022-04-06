package com.example.pdponline.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pdponline.database.AppDatabase
import com.example.pdponline.databinding.ItemMenuCourseBinding
import com.example.pdponline.entity.Course
import com.example.pdponline.entity.Module
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers


class MenuCourseAdapter(val listener: OnItemClickListener, var context: Context) :
    ListAdapter<Course, MenuCourseAdapter.Vh>(MyDiffUtil()) {

    class MyDiffUtil : DiffUtil.ItemCallback<Course>() {
        override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem == newItem
        }

    }

    inner class Vh(var itemMenuCourseBinding: ItemMenuCourseBinding) :
        RecyclerView.ViewHolder(itemMenuCourseBinding.root) {

        fun onBind(course: Course) {

            val appDatabase = AppDatabase.getInstance(context)
            val menuModelAdapter = MenuModelAdapter(object :
                MenuModelAdapter.OnItemClickListeners {
                override fun onClick(module: Module) {
                    listener.onRvClick(module)
                    Log.d("TAG", "onClick: 000000000000000000000000000000000000000000000")
                }

            })
            appDatabase.moduleDao().getModuleByIdCourse(course.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe {
                    menuModelAdapter.submitList(it)
                }
            itemMenuCourseBinding.rv.adapter = menuModelAdapter
            itemMenuCourseBinding.apply {
                allModuleTv.setOnClickListener {
                    listener.onClick(course)
                }
                courseNameTv.text = course.courseName



               val list = appDatabase.moduleDao().getModuleIdCourse(course.id)

                if (list.isNotEmpty()){
                    rememberTv.visibility = View.INVISIBLE
                }else{
                    rememberTv.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemMenuCourseBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position))
    }

    interface OnItemClickListener {
        fun onClick(course: Course)
        fun onRvClick(module: Module)
    }

}