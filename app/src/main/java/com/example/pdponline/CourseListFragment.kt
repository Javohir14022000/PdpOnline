package com.example.pdponline

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.pdponline.adapters.ModuleListAdapter
import com.example.pdponline.adapters.ModuleRecAdapter
import com.example.pdponline.database.AppDatabase
import com.example.pdponline.databinding.FragmentCourseListBinding
import com.example.pdponline.entity.Module
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers


class CourseListFragment : Fragment(R.layout.fragment_course_list) {

    private val binding: FragmentCourseListBinding by viewBinding(FragmentCourseListBinding::bind)
    lateinit var appDatabase: AppDatabase
    private lateinit var adapter: ModuleListAdapter
    private lateinit var moduleRecAdapter: ModuleRecAdapter
    lateinit var module: Module
    private var course: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            course = it.getInt("module_list")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            appDatabase = AppDatabase.getInstance(requireContext())
            val courseId = appDatabase.courseDao().getListCourseIdMenu(course)
            courseName.text = courseId.courseName

            adapter =
                ModuleListAdapter(requireContext(), object : ModuleListAdapter.OnCLickListener {
                    override fun onclick(module: Module, position: Int) {

                        val bundle = Bundle()
                        bundle.putInt("lesson_list", module.id)
                        findNavController().navigate(R.id.lessonListFragment, bundle)
                    }

                })
            appDatabase.moduleDao().getModuleByIdCourse(course)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe {
                    adapter.submitList(it)
                }

        }
        binding.rvCourse.adapter = adapter
    }
}