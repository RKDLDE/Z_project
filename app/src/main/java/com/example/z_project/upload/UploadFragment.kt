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
import android.widget.ImageView

import androidx.core.content.ContextCompat
import com.example.z_project.R
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale


class UploadFragment : Fragment() {

    private lateinit var binding: FragmentUploadBinding

//    private val CAMERA = arrayOf(Manifest.permission.CAMERA)
//    private val STORAGE = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//    private val CAMERA_CODE = 98
//    private val STORAGE_CODE = 99

    private lateinit var currentPhotoUri: Uri
    private lateinit var imageCaptureLauncher: ActivityResultLauncher<Intent>
    private lateinit var imageView: ImageView

//    private val startActivityResult =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//            if (result.resultCode == Activity.RESULT_OK) {
//                when (result.data?.extras?.get("requestCode")) {
//                    CAMERA_CODE -> {
//                        val bitmap = result.data?.extras?.get("data") as? Bitmap
//                        bitmap?.let {
//                            val uri = saveFile(RandomFileName(), "image/jpeg", it)
//                            binding.avatars.setImageURI(uri)
//                        }
//                    }
//                    STORAGE_CODE -> {
//                        val uri = result.data?.data
//                        binding.avatars.setImageURI(uri)
//                    }
//                }
//            }
//        }


//    private fun CallCamera() {
//        if (checkPermission(CAMERA, CAMERA_CODE) && checkPermission(STORAGE, STORAGE_CODE)) {
//            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            imageCaptureLauncher.launch(intent)
//        }
//    }





    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUploadBinding.inflate(inflater, container, false)

        val view = inflater.inflate(R.layout.fragment_upload, container, false)
        imageView = view.findViewById(R.id.imageView) // XML에서 ImageView 참조

        // ActivityResultLauncher 초기화
        imageCaptureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val bitmap = BitmapFactory.decodeFile(currentPhotoUri.path)
                imageView.setImageBitmap(bitmap)
            }
        }

        return binding.root
    }

    @Throws(IOException::class)
    private fun capturePhoto(view: View) {
        // 카메라 앱 호출을 위한 Intent 생성
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireContext().packageManager) != null) {
            // 이미지 파일 생성
            val imageFile: File? = createImageFile() // createImageFile()은 사진 파일을 생성하는 함수
            imageFile?.also {
                // 이미지가 저장될 파일의 URI 생성
                currentPhotoUri = FileProvider.getUriForFile(
                    requireContext(),
                    "com.example.Z_progject.fileprovider", // FileProvider authority
                    it
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoUri)
                // 카메라 인텐트를 ActivityResultLauncher로 실행
                imageCaptureLauncher.launch(takePictureIntent)
            }
        }
    }
    @Throws(IOException::class)
    private fun createImageFile(): File? {
        // 이미지 파일 생성 로직을 구현
        val storageDir: File? = requireContext().getExternalFilesDir(null)
        return File.createTempFile("IMG_", ".jpg", storageDir)
    }


//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        binding.camera.setOnClickListener {
//            CallCamera()
//        }
//    }



    private fun checkPermission(permissions: Array<out String>, type: Int): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(requireContext(), permission) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(permissions, type)
                    return false
                }
            }
        }
        return true
    }







    private fun saveFile(fileName: String, mimeType: String, bitmap: Bitmap): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, mimeType)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
        }

        val uri = requireContext().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        uri?.let {
            val outputStream = requireContext().contentResolver.openOutputStream(it)
            outputStream?.use { fos ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val updatedValues = ContentValues().apply {
                    put(MediaStore.Images.Media.IS_PENDING, 0)
                }
                requireContext().contentResolver.update(it, updatedValues, null, null)
            }
        }
        return uri
    }

    private fun RandomFileName(): String {
        return SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(System.currentTimeMillis())
    }
}
