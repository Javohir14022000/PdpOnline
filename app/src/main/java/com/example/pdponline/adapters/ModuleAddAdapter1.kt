package com.example.pdponline.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pdponline.database.AppDatabase
import com.example.pdponline.databinding.ItemModulBinding
import com.example.pdponline.entity.Module
import java.io.File

class ModuleAddAdapter1(val context: Context, val listener: OnItemClickListener) :
    ListAdapter<Module, ModuleAddAdapter1.Vh>(MyDiffUtil()) {

    class MyDiffUtil : DiffUtil.ItemCallback<Module>() {
        override fun areItemsTheSame(oldItem: Module, newItem: Module): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Module, newItem: Module): Boolean {
            return oldItem == newItem
        }

    }

    inner class Vh(val itemModulBinding: ItemModulBinding) :
        RecyclerView.ViewHolder(itemModulBinding.root) {
        lateinit var appDatabase: AppDatabase
        fun onBind(module: Module) {
            appDatabase = AppDatabase.getInstance(context)
//            val courseList = ArrayList(appDatabase.courseDao().getListCourseIdMenuId(module.id))
            itemModulBinding.apply {

             imgCourseIcon.setImageURI(Uri.fromFile(File(appDatabase.courseDao().getListCourseIdMenu(module.courseId).image)))
                moduleName.text = module.moduleName
                moduleNumber.text = module.moduleNumber

                itemView.setOnClickListener {
                    listener.onClick(module)
                }

                delete.setOnClickListener {
                    listener.onDelete(module)
                }
                edit.setOnClickListener {
                    listener.onEdit(module)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemModulBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position))
    }

    interface OnItemClickListener {
        fun onClick(module: Module)
        fun onDelete(module: Module)
        fun onEdit(module: Module)
    }
}