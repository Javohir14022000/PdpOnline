package com.example.pdponline

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.pdponline.adapters.LessonAdapter
import com.example.pdponline.database.AppDatabase
import com.example.pdponline.databinding.DialogDeleteLessonBinding
import com.example.pdponline.databinding.FragmentLessonBinding
import com.example.pdponline.entity.Lesson
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class LessonFragment : Fragment(R.layout.fragment_lesson) {

    private val binding: FragmentLessonBinding by viewBinding(FragmentLessonBinding::bind)
    lateinit var appDatabase: AppDatabase
    lateinit var lessonAdapter: LessonAdapter
    private var module: Int = -1
    private var lesson: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            module = it.getInt("module_add")
            lesson = it.getInt("course_id")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appDatabase = AppDatabase.getInstance(requireContext())
        val moduleId = appDatabase.moduleDao().getModuleId(module)

        val array = arrayListOf<String>()
        binding.apply {
            titleSettings.text = moduleId.moduleName
            cardAddBtn.setOnClickListener {
                val lessonName = nameEt.text.toString()
                val lessonInfo = infoEt.text.toString()
                val number = lessonNumberTv.text.toString()
                if (lessonName.isNotEmpty() && lessonInfo.isNotEmpty() && number.isNotEmpty()) {

                    val lessonList = appDatabase.lessonDao().getLessonByModule(module)
                    var index = -1
                    val replayIndex = lessonNumberTv.text.toString()
                    lessonList.forEach {
                        if (it.lessonNumber == replayIndex) {
                            index = 1
                        }
                    }
                    val lesson = Lesson(
                        lessonName = lessonName,
                        lessonInfo = lessonInfo,
                        lessonNumber = number,
                        moduleId = module
                    )
                    if (index == 1) {
                        Toast.makeText(
                            requireContext(),
                            "Bu raqamda dars band qilingan iltimos boshqa raqam tanlang",
                            Toast.LENGTH_SHORT
                        ).show()
                        lessonNumberTv.text.clear()
                    } else {
                        appDatabase.lessonDao().addLesson(lesson)
                        array.add(number)
                        nameEt.text.clear()
                        infoEt.text.clear()
                        lessonNumberTv.text.clear()
                        Toast.makeText(requireContext(), "Qo`shildi", Toast.LENGTH_SHORT).show()
                    }


                } else {
                    Toast.makeText(
                        requireContext(),
                        "Ma'lumotlar to'liq to'ldirilmadi",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            lessonAdapter = LessonAdapter(lesson,
                requireContext(),
                listener = object : LessonAdapter.OnItemClickListener {
                    override fun onDelete(lesson: Lesson, position: Int) {
                        val dialog = AlertDialog.Builder(requireContext())
                        val bindingDialog = DialogDeleteLessonBinding.inflate(layoutInflater)
                        val create = dialog.create()
                        create.setView(bindingDialog.root)
                        bindingDialog.apply {
                            accept.setOnClickListener {

                                appDatabase.lessonDao().deleteDao(lesson)
                                create.dismiss()
                            }
                            close.setOnClickListener { create.dismiss() }
                        }
                        create.show()
                    }

                    override fun onEdit(lesson: Lesson, position: Int) {
                        val bundle = Bundle()
                        bundle.putInt("lesson_edit", lesson.id)
                        findNavController().navigate(R.id.editLessonFragment, bundle)
                    }

                })

            appDatabase.lessonDao().getLessonByIdModule(module)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    lessonAdapter.submitList(it)
                }, {
                    Log.d("Tag", "onCreate: ${it.message}")

                })
            rv.adapter = lessonAdapter
        }

    }
}