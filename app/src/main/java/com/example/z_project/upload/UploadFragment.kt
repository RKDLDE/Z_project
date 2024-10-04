package com.example.z_project.upload

import android.Manifest
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View

import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.example.z_project.R
import com.example.z_project.databinding.FragmentUploadBinding
import java.io.File
import java.io.FileOutputStream


class UploadFragment : Fragment() {
    private var _binding: FragmentUploadBinding? = null
    private val binding get() = _binding!!
    private val dialog = CustomFragment()


    private lateinit var cameraPermission: ActivityResultLauncher<String>
    private lateinit var storagePermission: ActivityResultLauncher<String>
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private lateinit var galleryLauncher: ActivityResultLauncher<String>

    private var photoUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUploadBinding.inflate(inflater, container, false)

        storagePermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                binding.cameraBtn.setOnClickListener {
                    cameraPermission.launch(Manifest.permission.CAMERA)
                }

            } else {
                Toast.makeText(requireContext(), "권한을 승인해야 앱을 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
                requireActivity().finish()
            }
        }

        cameraPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                openCamera()
            } else {
                Toast.makeText(requireContext(), "권한을 승인해야 카메라를 사용할 수 있습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (photoUri != null){
                binding.cameraBtn.setImageURI(photoUri)

                if (success) {
                    // 사진 촬영 성공 시 크기 조정
                    val resizedBitmap = resizeImage(photoUri!!, 800, 1120) // 원하는 크기 입력
                    if (resizedBitmap != null) {
                        binding.cameraBtn.setImageBitmap(resizedBitmap)
                        // 크기 조정된 이미지를 사용할 수 있습니다
                        saveResizedImage(resizedBitmap) // 원한다면 이미지 저장
                    }
                }

            }
        }

        storagePermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        // 버튼 클릭 시 CustomFragment로 전환 --- ViewBinding 사용 시 findViewById 사용할 필요 없음!
        binding.sss.setOnClickListener {
            openCustomFragment()
        }

        return binding.root
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

    // 사진 크기 조정을 위한 함수
    private fun resizeImage(uri: Uri, width: Int, height: Int): Bitmap? {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val originalBitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        // Bitmap 크기 조정
        return originalBitmap?.let {
            Bitmap.createScaledBitmap(it, width, height, true)
        }
    }

    // 크기 조정된 이미지 저장
    private fun saveResizedImage(bitmap: Bitmap) {
        val file = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "resized_image.jpg")
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    private fun openCustomFragment() {
        val customFragment = CustomFragment()
        parentFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            .replace(R.id.custom_view, customFragment) // fragment_container는 교체될 Fragment의 container ID
            .addToBackStack(null)
            .commit()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null /*여기서 null을 쓰려고 binding 대신 _binding을 새로 설정하는 듯*/
    }

}
