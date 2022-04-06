package com.example.pdponline.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pdponline.databinding.ItemCourseBinding
import com.example.pdponline.entity.Course
import java.io.File

class CourseAdapter(val listener: OnItemClickListener) : ListAdapter<Course, CourseAdapter.Vh>(MyDiffUtil()) {

    class MyDiffUtil : DiffUtil.ItemCallback<Course>() {
        override fun areItemsTheSame(oldItem: Course, newItem: Course): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Course, newItem: Course): Boolean {

            return oldItem == newItem
        }

    }

    inner class Vh(val itemCourseBinding: ItemCourseBinding) :
        RecyclerView.ViewHolder(itemCourseBinding.root) {

        fun onBind(course: Course) {
            itemCourseBinding.apply {
                courseName.text = course.courseName
                imgCourseIcon.setImageURI(Uri.fromFile(File(course.image)))

                edit.setOnClickListener {
                    listener.onEditClick(course, position)
                }
                delete.setOnClickListener {
                    listener.onDeleteClick(course, position)
                }
                itemView.setOnClickListener {
                    listener.onItemClick(course, position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemCourseBinding.inflate(LayoutInflater.from(parent.context), parent,false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position))
    }

    interface OnItemClickListener{
        fun onItemClick(course: Course, position: Int)
        fun onEditClick(course: Course, position: Int)
        fun onDeleteClick(course: Course, position: Int)
    }
}