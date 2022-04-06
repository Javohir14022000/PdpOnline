package com.example.pdponline

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.pdponline.database.AppDatabase
import com.example.pdponline.databinding.FragmentEditLessonBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding


class EditLessonFragment : Fragment(R.layout.fragment_edit_lesson) {

    private val binding: FragmentEditLessonBinding by viewBinding(FragmentEditLessonBinding::bind)
    var lesson: Int = 0
    lateinit var appDatabase: AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            lesson = it.getInt("lesson_edit")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appDatabase = AppDatabase.getInstance(requireContext())

        val lessonIdd = appDatabase.lessonDao().getLessonId(lesson)

        binding.apply {
            titleSettingss.text = "${lessonIdd.lessonNumber} - Dars"
            lessonNameEt.setText(lessonIdd.lessonName)
            lessonInfoEt.setText(lessonIdd.lessonInfo)
            lessonNumberEt.setText(lessonIdd.lessonNumber)

            cardAddBtn.setOnClickListener {
                if (lessonNameEt.text.isNotEmpty() && lessonInfoEt.text.isNotEmpty() && lessonNumberEt.text.isNotEmpty()) {
                    lessonIdd.lessonName = lessonNameEt.text.toString()
                    lessonIdd.lessonInfo = lessonInfoEt.text.toString()
                    lessonIdd.lessonNumber = lessonNumberEt.text.toString()

                    val lessonList = appDatabase.lessonDao().getLessonByModule(lesson)
                    var index = -1
                    val replayIndex = lessonNumberEt.text.toString()
                    lessonList.forEach {
                        if (it.lessonNumber.equals(replayIndex)) {
                            index = 1
                        }
                    }
                    if (index == 1){
                        Toast.makeText(
                            requireContext(),
                            "Bu raqamda dars band qilingan iltimos boshqa raqam tanlang",
                            Toast.LENGTH_SHORT
                        ).show()
                        lessonNumberEt.text.clear()
                    }else{
                        appDatabase.lessonDao().editDao(lessonIdd)
                        Toast.makeText(requireContext(), "Success", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }

                } else {
                    Toast.makeText(
                        requireContext(),
                        "Ma'lumotlar to'liq to'ldirilmagan",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
    }

}