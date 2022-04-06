package com.example.pdponline.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pdponline.database.AppDatabase
import com.example.pdponline.databinding.ItemLessonBinding
import com.example.pdponline.entity.Lesson
import java.io.File

class LessonAdapter(var lessonId: Int, val context: Context, val listener: OnItemClickListener) :
    ListAdapter<Lesson, LessonAdapter.Vh>(MyDiffUtil()) {

    class MyDiffUtil : DiffUtil.ItemCallback<Lesson>() {
        override fun areItemsTheSame(oldItem: Lesson, newItem: Lesson): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Lesson, newItem: Lesson): Boolean {
            return oldItem == newItem
        }

    }

    inner class Vh(private val itemLessonBinding: ItemLessonBinding) :
        RecyclerView.ViewHolder(itemLessonBinding.root) {
        lateinit var appDatabase: AppDatabase
        fun onBind(lesson: Lesson) {
            itemLessonBinding.apply {
                appDatabase = AppDatabase.getInstance(context)
                lessonNameTv.text = "${lesson.lessonNumber} - dars"
                lessonInfoTv.text = lesson.lessonName
                imgCourseIcon.setImageURI(Uri.fromFile(File(appDatabase.courseDao().getListCourseIdMenu(lessonId).image)))

                delete.setOnClickListener {
                    listener.onDelete(lesson, adapterPosition)
                }

                edit.setOnClickListener {
                    listener.onEdit(lesson, adapterPosition)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemLessonBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position))
    }

    interface OnItemClickListener {
        fun onDelete(lesson: Lesson, position: Int)
        fun onEdit(lesson: Lesson, position: Int)
    }
}