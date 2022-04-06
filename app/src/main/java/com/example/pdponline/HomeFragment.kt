package com.example.pdponline

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.pdponline.adapters.MenuCourseAdapter
import com.example.pdponline.adapters.RvAdapter
import com.example.pdponline.database.AppDatabase
import com.example.pdponline.databinding.FragmentHomeBinding
import com.example.pdponline.entity.Course
import com.example.pdponline.entity.Module
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var handler: Handler
    private var doubleclick = false
    private val binding: FragmentHomeBinding by viewBinding(FragmentHomeBinding::bind)

    lateinit var appDatabase: AppDatabase
    lateinit var list: List<Course>
    lateinit var menuCourseAdapter: MenuCourseAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appDatabase = AppDatabase.getInstance(requireContext())
        binding.apply {


            if (appDatabase.courseDao().getListCourseMenu().isNotEmpty()) {
                animationView.visibility = View.INVISIBLE
            } else {
                animationView.visibility = View.VISIBLE
            }

            menuCourseAdapter =
                MenuCourseAdapter(
                    object : MenuCourseAdapter.OnItemClickListener {
                        override fun onClick(course: Course) {
                            val bundle = Bundle()
                            bundle.putInt("module_list", course.id)
                            findNavController().navigate(R.id.courseListFragment, bundle)
                        }

                        override fun onRvClick(module: Module) {
                            val bundle = Bundle()
                            bundle.putInt("lesson_list", module.id)
                            findNavController().navigate(R.id.lessonListFragment, bundle)
                        }


                    }, requireContext()
                )
            appDatabase.courseDao().getListCourse()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe {
                    menuCourseAdapter.submitList(it)
                }

            rvModule.adapter = menuCourseAdapter

            cardSettings.setOnClickListener {
                findNavController().navigate(R.id.settingsFragment)
            }
        }
    }

//    override fun onDetach() {
//        if (doubleclick) {
//            super.onDetach()
//            return
//        }
//        handler = Handler(Looper.getMainLooper())
//        handler.postDelayed({
//            doubleclick = false
//        }, 2000)
//        doubleclick = true
//        Toast.makeText(
//            requireContext(),
//            "Dasturdan chiqish uchun yana bir marta bosing",
//            Toast.LENGTH_SHORT
//        ).show()
//    }
//    override fun onStop() {
//        if (doubleclick){
//            super.onStop()
//            return
//        }
//        handler = Handler(Looper.getMainLooper())
//        handler.postDelayed({
//            doubleclick = false
//        },2000)
//        doubleclick = true
//        Toast.makeText(requireContext(), "Dasturdan chiqish uchun yana bir marta bosing", Toast.LENGTH_SHORT).show()
//    }

//  fun onBackPressed() {
//        if (doubleclick) {
//            super.onBackPressed()
//            return
//        }
//        handler = Handler(Looper.getMainLooper())
//        handler.postDelayed({
//            doubleclick =false
//        }, 2000)
//        doubleclick = true
//      Toast.makeText(requireContext(), "Dasturdan chiqish uchun yana bir marta bosing", Toast.LENGTH_SHORT).show()
//    }
}