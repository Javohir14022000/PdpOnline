package com.example.pdponline

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.navigation.fragment.findNavController
import com.example.pdponline.database.AppDatabase
import com.example.pdponline.databinding.CustomDialogBinding
import com.example.pdponline.databinding.FragmentEditCourseBinding
import com.example.pdponline.entity.Course
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception


class EditCourseFragment : Fragment(R.layout.fragment_edit_course) {

    private val binding: FragmentEditCourseBinding by viewBinding(FragmentEditCourseBinding::bind)
    lateinit var appDatabase: AppDatabase
    private var currentPhotoPath: String = ""


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appDatabase = AppDatabase.getInstance(requireContext())

        val courseId = arguments?.getInt("course_edit", 0)

        val course = appDatabase.courseDao().getListCourseIdMenu(courseId ?: 1)

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

            titleSettings.setText(course.courseName)
            courseName.setText(course.courseName)
            imageView.setImageURI(Uri.fromFile(File(course.image)))

            cardAddBtn.setOnClickListener {
                if (courseName.text.isNotEmpty() && currentPhotoPath.isNotEmpty()) {
                    course.courseName = courseName.text.toString()
                    course.image = currentPhotoPath
                    appDatabase.courseDao().editCourse(course)
                    findNavController().popBackStack()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Ma'lumotlar to'liq kiritilmadi",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

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