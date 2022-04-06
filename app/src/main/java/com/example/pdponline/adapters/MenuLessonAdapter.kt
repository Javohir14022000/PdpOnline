package com.example.pdponline.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pdponline.database.AppDatabase
import com.example.pdponline.databinding.ItemLessonMenuBinding
import com.example.pdponline.entity.Lesson

class MenuLessonAdapter(val listener: OnItemClickListener) :
    ListAdapter<Lesson, MenuLessonAdapter.Vh>(MyDiffUtil()) {

    class MyDiffUtil : DiffUtil.ItemCallback<Lesson>() {
        override fun areItemsTheSame(oldItem: Lesson, newItem: Lesson): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Lesson, newItem: Lesson): Boolean {
            return oldItem == newItem
        }

    }

    inner class Vh(private val itemLessonMenuBinding: ItemLessonMenuBinding) :
        RecyclerView.ViewHolder(itemLessonMenuBinding.root) {
        fun onBind(lesson: Lesson) {

            itemLessonMenuBinding.apply {
                lessonNumber.text = lesson.lessonNumber

                cardEnter.setOnClickListener {
                    listener.onClick(lesson, adapterPosition)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemLessonMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position))
    }

    interface OnItemClickListener {
        fun onClick(lesson: Lesson, position: Int)
    }
}