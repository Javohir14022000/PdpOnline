package com.example.pdponline

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.pdponline.adapters.MenuLessonAdapter
import com.example.pdponline.database.AppDatabase
import com.example.pdponline.databinding.FragmentLessonListBinding
import com.example.pdponline.entity.Lesson
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers


class LessonListFragment : Fragment(R.layout.fragment_lesson_list) {

    private val binding: FragmentLessonListBinding by viewBinding(FragmentLessonListBinding::bind)
    lateinit var appDatabase: AppDatabase
    lateinit var menuLessonAdapter: MenuLessonAdapter
    var module: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            module = it.getInt("lesson_list")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appDatabase = AppDatabase.getInstance(requireContext())
        binding.apply {
            val listLesson = ArrayList(appDatabase.lessonDao().getLessonByModule(module))
            if (listLesson.isEmpty()) {
                rememberTv.visibility = View.VISIBLE
            }
            menuLessonAdapter = MenuLessonAdapter(object : MenuLessonAdapter.OnItemClickListener {
                override fun onClick(lesson: Lesson, position: Int) {

                    val bundle = Bundle()
                    bundle.putInt("lesson", lesson.id)
                    findNavController().navigate(R.id.aboutLessonFragment, bundle)

                }

            })
            appDatabase.lessonDao().getLessonByIdModule(module)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe {
                    menuLessonAdapter.submitList(it)
                }
            rv.adapter = menuLessonAdapter
        }
    }

}