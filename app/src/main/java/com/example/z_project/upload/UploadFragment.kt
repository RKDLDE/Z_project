package com.example.z_project.upload

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.z_project.databinding.FragmentUploadBinding
import android.Manifest
import android.app.Activity
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Camera
import android.os.Build
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.z_project.R
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.appcompat.app.AppCompatActivity
import com.example.z_project.databinding.ActivityMainBinding
import java.io.FileOutputStream

class UploadFragment : Fragment() {
    lateinit var binding: FragmentUploadBinding

    lateinit var cameraPermission: ActivityResultLauncher<String>
    lateinit var storagePermission: ActivityResultLauncher<String>
    lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    lateinit var galleryLauncher: ActivityResultLauncher<String>

    private var photoUri:Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUploadBinding.inflate(inflater, container, false)

        storagePermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                setViews()
            } else {
                Toast.makeText(requireContext(), "권한을 승인해야 앱을 사용할 수 있습니다.",Toast.LENGTH_SHORT).show()
                requireActivity().finish()
            }
        }
        cameraPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                openCamera()
            } else {
                Toast.makeText(requireContext(), "권한을 승인해야 카메라를 사용할 수 있습니다.",Toast.LENGTH_SHORT).show()
            }
        }
        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) {

        }
        storagePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        return binding.root
    }

    private fun setViews() {
        binding.cameraBtn.setOnClickListener {
            cameraPermission.launch(Manifest.permission.CAMERA)
        }
    }

    private fun openCamera() {
        val photoFile = File.createTempFile(
            "IMG_",
            ".jpg",
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        )

        photoUri = FileProvider.getUriForFile(
            requireContext(), "${requireContext().packageName}.provider", photoFile
        )

        cameraLauncher.launch(photoUri)
    }

    private fun openGallery() {
        galleryLauncher.launch("image/*")
    }



}