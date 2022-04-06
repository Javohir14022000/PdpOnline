package com.example.pdponline

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.pdponline.database.AppDatabase
import com.example.pdponline.databinding.FragmentEditModulBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding


class EditModulFragment : Fragment(R.layout.fragment_edit_modul) {
    private val binding: FragmentEditModulBinding by viewBinding(FragmentEditModulBinding::bind)
    lateinit var appDatabase: AppDatabase
    var module: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            module = it.getInt("module_edit")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appDatabase = AppDatabase.getInstance(requireContext())

        val moduleId = appDatabase.moduleDao().getModuleId(module)

        binding.apply {
            titleSettings.text = moduleId.moduleName
            nameModule.setText(moduleId.moduleName)
            numberModule.setText(moduleId.moduleNumber)

            cardAddBtn.setOnClickListener {
                if (nameModule.text.isNotEmpty() && numberModule.text.isNotEmpty()) {
                    val moduleList = appDatabase.moduleDao().getModuleIdCourse(module)
                    var index = -1
                    val replayIndex = numberModule.text.toString()
                    moduleList.forEach {
                        if (it.moduleNumber.equals(replayIndex)) {
                            index = 1
                        }
                    }
                    moduleId.moduleName = nameModule.text.toString()
                    moduleId.moduleNumber = numberModule.text.toString()

                    if (index == 1){
                        Toast.makeText(
                            requireContext(),
                            "Bu raqamdagi modul mavjud, iltimos moshqa raqam tanlang!!! ",
                            Toast.LENGTH_SHORT
                        ).show()
                        numberModule.text.clear()
                    }else{
                        appDatabase.moduleDao().editModule(moduleId)
                        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }

                } else {
                    Toast.makeText(
                        requireContext(),
                        "Ma'lumotlar to'liq to'ldirilmadi!!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }


            }

        }
    }

}