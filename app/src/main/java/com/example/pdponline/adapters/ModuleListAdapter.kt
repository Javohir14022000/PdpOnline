package com.example.pdponline.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pdponline.database.AppDatabase
import com.example.pdponline.databinding.ItemModelMenuBinding
import com.example.pdponline.entity.Module
import java.io.File

class ModuleListAdapter(val context: Context, val listener: OnCLickListener) :
    ListAdapter<Module, ModuleListAdapter.Vh>(MyDiffUtil()) {

    class MyDiffUtil : DiffUtil.ItemCallback<Module>() {
        override fun areItemsTheSame(oldItem: Module, newItem: Module): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Module, newItem: Module): Boolean {
            return oldItem == newItem
        }

    }

    inner class Vh(private val itemModelMenuBinding: ItemModelMenuBinding) :
        RecyclerView.ViewHolder(itemModelMenuBinding.root) {
        lateinit var appDatabase: AppDatabase
        fun onBind(module: Module) {
            appDatabase = AppDatabase.getInstance(context)


            itemModelMenuBinding.apply {
                modulName.text = module.moduleName
                moduleNumberTv.setText("${appDatabase.lessonDao().getLessonByModule(module.id).size}")
                courseName.text =
                    appDatabase.courseDao().getListCourseIdMenu(module.courseId).courseName
                imgCourseIcon.setImageURI(
                    Uri.fromFile(
                        File(
                            appDatabase.courseDao().getListCourseIdMenu(module.courseId).image
                        )
                    )
                )

                itemView.setOnClickListener {
                    listener.onclick(module, adapterPosition)
                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemModelMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position))
    }

    interface OnCLickListener {
        fun onclick(module: Module, position: Int)
    }
}