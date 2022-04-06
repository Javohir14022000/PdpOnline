package com.example.pdponline

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import com.example.pdponline.adapters.CourseAdapter
import com.example.pdponline.database.AppDatabase
import com.example.pdponline.databinding.CustomDialogBinding
import com.example.pdponline.databinding.DialogDeleteBinding
import com.example.pdponline.databinding.FragmentSettingsBinding
import com.example.pdponline.entity.Course

import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception


class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val binding: FragmentSettingsBinding by viewBinding(FragmentSettingsBinding::bind)
    lateinit var appDatabase: AppDatabase
    lateinit var course: Course
    lateinit var courseAdapter: CourseAdapter
    private var currentPhotoPath: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appDatabase = AppDatabase.getInstance(requireContext())
        binding.apply {

            imageView.setOnClickListener {
                getPermission.launch(
                    arrayOf(
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                )
                alertDialog()
            }


            cardAddBtn.setOnClickListener {

                val courseName = courseNameEt.text.toString()
                val imageIcon = currentPhotoPath
                if (courseName.isNotEmpty() && imageIcon.isNotEmpty()) {
                    val course = Course(courseName = courseName, image = imageIcon)
                    appDatabase.courseDao().addCourse(course)
                    courseNameEt.text.clear()
                    imageView.setImageURI(Uri.EMPTY)
                    Toast.makeText(requireContext(), "Qo'shildi", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Ma'lumotlar to'liq kiritilmadi",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            courseAdapter = CourseAdapter(listener = object : CourseAdapter.OnItemClickListener {
                override fun onItemClick(course: Course, position: Int) {
                    val bundle = Bundle()
                    bundle.putInt("course", course.id)
                    findNavController().navigate(R.id.addModulFragment, bundle)
                }

                override fun onEditClick(course: Course, position: Int) {
                    val bundle = Bundle()
                    bundle.putInt("course_edit", course.id)
                    findNavController().navigate(R.id.editCourseFragment, bundle)
                }

                override fun onDeleteClick(course: Course, position: Int) {
                    val dialog = AlertDialog.Builder(requireContext())
                    val bindingDialog = DialogDeleteBinding.inflate(layoutInflater)
                    val create = dialog.create()
                    create.setView(bindingDialog.root)
                    bindingDialog.apply {
                        accept.setOnClickListener {
                            appDatabase.courseDao().deleteCourse(course)
                            create.dismiss()
                        }
                        close.setOnClickListener { create.dismiss() }
                    }
                    create.show()
                }

            })
            appDatabase.courseDao().getListCourse()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    courseAdapter.submitList(it)
                },
                    {
                        Log.d("Tag", "onCreate: ${it.message}")
                    })
            rv.adapter = courseAdapter

//          ``1``
        }
    }

    private fun alertDialog() {
        val dialog = AlertDialog.Builder(requireContext())
        val customDialogBinding = CustomDialogBinding.inflate(layoutInflater)
        val create = dialog.create() as AlertDialog
        create.setView(customDialogBinding.root)
        create.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        customDialogBinding.apply {
            galereya.setOnClickListener {
                getGalleryContent.launch("image/*")
                create.hide()
            }
            camera.setOnClickListener {
                getCameraNewMethod()
                create.hide()
            }

        }
        create.show()
    }


    private val getPermission =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {

        }

    private fun getCameraNewMethod() {
        val photoFile = try {
            createImageFile()
        } catch (e: Exception) {
            null
        }
        photoFile?.also {
            val photoUri =
                FileProvider.getUriForFile(
                    requireContext(),
                    BuildConfig.APPLICATION_ID,
                    it
                )
            getTakePhoto.launch(photoUri)
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val m = System.currentTimeMillis()
        val externalFilesDir = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val file = File.createTempFile(m.toString(), ".jpg", externalFilesDir)
        currentPhotoPath = file.absolutePath
        Log.d("Path", "Path : $currentPhotoPath")

        return file
    }

    private val getTakePhoto = registerForActivityResult(ActivityResultContracts.TakePicture()) {
        if (it) {
            binding.imageView.setImageURI(Uri.fromFile(File(currentPhotoPath)))
        }
    }
    private val getGalleryContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            if (it == null) return@registerForActivityResult
            binding.imageView.setImageURI(it)
            val m = System.currentTimeMillis()
            val openInputStream = activity?.contentResolver?.openInputStream(it)
            val file = File(activity?.filesDir, "$m.jpg")
            val fileOutputStream = FileOutputStream(file)
            openInputStream?.copyTo(fileOutputStream)
            openInputStream?.close()
            fileOutputStream.close()
            val absolutePath = file.absolutePath
            currentPhotoPath = file.absolutePath
            Log.d("Path", "Path: $absolutePath")
        }


}