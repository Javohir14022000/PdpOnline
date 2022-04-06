package com.example.pdponline.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pdponline.databinding.ItemMenuModulBinding
import com.example.pdponline.databinding.ItemModelMenuBinding
import com.example.pdponline.entity.Module

class ModuleRecAdapter(
    val context: Context,
    var list: List<Module>,
    var listener: OnItemClickListener
) : RecyclerView.Adapter<ModuleRecAdapter.Vh>() {

    inner class Vh(val itemModelMenuBinding: ItemModelMenuBinding) :
        RecyclerView.ViewHolder(itemModelMenuBinding.root) {

        fun onBind(module: Module, position: Int) {
            itemModelMenuBinding.apply {
                modulName.text = list[position].moduleName
                moduleNumberTv.text = list[position].moduleNumber

                itemView.setOnClickListener {
                    listener.onClick(module, position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemModelMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size

    interface OnItemClickListener {
        fun onClick(module: Module, position: Int)
    }
}