package com.example.pdponline.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pdponline.databinding.ItemMenuModulBinding
import com.example.pdponline.entity.Course
import com.example.pdponline.entity.Module

class ModuleAdapter(val list: List<Module>, val listener: OnItemClickListener) : RecyclerView.Adapter<ModuleAdapter.Vh>() {

    inner class Vh(var itemMenuModulBinding: ItemMenuModulBinding) :
        RecyclerView.ViewHolder(itemMenuModulBinding.root) {

            fun onBind(module: Module, position: Int){
                itemMenuModulBinding.apply {
                    modulName.text = module.moduleName

                    allshw.setOnClickListener {
                        listener.onClick(module, position)
                    }
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemMenuModulBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClickListener{
        fun onClick(module: Module, position: Int)
    }

}