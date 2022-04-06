package com.example.pdponline

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.pdponline.adapters.ModuleAddAdapter1
import com.example.pdponline.database.AppDatabase
import com.example.pdponline.databinding.DialogDeleteModuleBinding
import com.example.pdponline.databinding.FragmentAddModulBinding
import com.example.pdponline.entity.Module
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers


class AddModulFragment : Fragment(R.layout.fragment_add_modul) {

    private val binding: FragmentAddModulBinding by viewBinding(FragmentAddModulBinding::bind)
    lateinit var appDatabase: AppDatabase
    lateinit var module: Module
    lateinit var moduleAddAdapter1: ModuleAddAdapter1
    var course: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            course = it.getInt("course", 0)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appDatabase = AppDatabase.getInstance(requireContext())

        val courseId = appDatabase.courseDao().getListCourseIdMenu(course)


        binding.apply {

//            if (courseList.isNotEmpty()) {
//                rememberTv.visibility = View.INVISIBLE
//            }else{
//                rememberTv.visibility = View.VISIBLE
//            }

            titleSettings.text = courseId.courseName
            cardAddBtn.setOnClickListener {
                val moduleName = moduleNameEt.text.toString()
                val moduleNumber = moduleNumberEt.text.toString()
                if (moduleName.isNotEmpty() && moduleNumber.isNotEmpty()) {
                    val moduleList = appDatabase.moduleDao().getModuleIdCourse(course)
                    var index = -1
                    val replayIndex = moduleNumberEt.text.toString()
                    moduleList.forEach {
                        if (it.moduleNumber.equals(replayIndex)) {
                            index = 1
                        }
                    }
                    val module = Module(
                        moduleName = moduleName,
                        moduleNumber = moduleNumber,
                        courseId = course,
                    )
                    if (index == 1) {
                        Toast.makeText(
                            requireContext(),
                            "Bu raqamdagi modul mavjud, iltimos moshqa raqam tanlang!!! ",
                            Toast.LENGTH_SHORT
                        ).show()
                        moduleNumberEt.text.clear()
                    } else {
                        appDatabase.moduleDao().addModule(module)
                        Toast.makeText(requireContext(), "Qo'shildi!!!", Toast.LENGTH_SHORT).show()
                        moduleNameEt.text.clear()
                        moduleNumberEt.text.clear()
                    }


                } else {
                    Toast.makeText(
                        requireContext(),
                        "Ma'lumotlar to'liq kiritilmadi",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

            moduleAddAdapter1 =
                ModuleAddAdapter1(listener = object : ModuleAddAdapter1.OnItemClickListener {
                    override fun onClick(module: Module) {
                        val bundle = Bundle()
                        bundle.putInt("module_add", module.id)
                        bundle.putInt("course_id", module.courseId)
                        findNavController().navigate(R.id.lessonFragment, bundle)
                    }

                    override fun onDelete(module: Module) {
                        val dialog = AlertDialog.Builder(requireContext())
                        val bindingDialog = DialogDeleteModuleBinding.inflate(layoutInflater)
                        val create = dialog.create()
                        create.setView(bindingDialog.root)
                        bindingDialog.apply {
                            accept.setOnClickListener {
                                appDatabase.moduleDao().deleteModule(module)
                                create.dismiss()
                            }
                            close.setOnClickListener { create.dismiss() }
                        }
                        create.show()
                    }

                    override fun onEdit(module: Module) {
                        val bundle = Bundle()
                        bundle.putInt("module_edit", module.id)
                        findNavController().navigate(R.id.editModulFragment, bundle)
                    }

                }, context = requireContext())

            appDatabase.moduleDao().getModuleByIdCourse(course)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    moduleAddAdapter1.submitList(it)
                }, {
                    Log.d("Tag", "onCreate: ${it.message}")

                })
            rv.adapter = moduleAddAdapter1
        }
    }

}