package com.example.pdponline

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.pdponline.database.AppDatabase
import com.example.pdponline.databinding.FragmentAboutLessonBinding
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding


class AboutLessonFragment : Fragment(R.layout.fragment_about_lesson) {

    private val binding: FragmentAboutLessonBinding by viewBinding(FragmentAboutLessonBinding::bind)
    lateinit var appDatabase: AppDatabase
    private var lesson = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            lesson = it.getInt("lesson")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appDatabase = AppDatabase.getInstance(requireContext())
        binding.apply {
            val lessonId = appDatabase.lessonDao().getLessonId(lesson)
            lessonNumberTv.text = "${lessonId.lessonNumber} - dars"
            lessonNameTv.text = lessonId.lessonName
            infoTv.text = lessonId.lessonInfo

            exitIcon.setOnClickListener {
                findNavController().popBackStack()
            }
        }


    }

}