package com.example.pdponline.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pdponline.databinding.ItemMenuModulBinding
import com.example.pdponline.entity.Course
import com.example.pdponline.entity.Module

class MenuModelAdapter(val listeners: OnItemClickListeners) :
    ListAdapter<Module, MenuModelAdapter.Vh>(MyDiffUtil()) {

    class MyDiffUtil : DiffUtil.ItemCallback<Module>() {
        override fun areItemsTheSame(oldItem: Module, newItem: Module): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Module, newItem: Module): Boolean {
            return oldItem == newItem
        }

    }

    inner class Vh(val itemMenuModulBinding: ItemMenuModulBinding) :
        RecyclerView.ViewHolder(itemMenuModulBinding.root) {

        fun onBind(module: Module) {
            itemMenuModulBinding.apply {
                modulName.text = module.moduleName

                allshw.setOnClickListener {
                    listeners.onClick(module)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemMenuModulBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position))
    }

    interface OnItemClickListeners {
        fun onClick(module: Module)
    }
}