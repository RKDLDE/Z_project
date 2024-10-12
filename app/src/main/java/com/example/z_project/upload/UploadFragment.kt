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


class UploadFragment : Fragment() {

    lateinit var binding: FragmentUploadBinding
    private lateinit var cameraPermission: ActivityResultLauncher<String>
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private var photoUri: Uri? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentUploadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cameraBtn.setOnClickListener {
            // 카메라 버튼 비활성화
            binding.cameraBtn.isEnabled = false
            cameraPermission.launch(Manifest.permission.CAMERA)
        }

        cameraPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                openCamera()
            } else {
                Toast.makeText(requireContext(), "권한을 승인해야 카메라를 사용할 수 있습니다.", Toast.LENGTH_SHORT)
                    .show()
            }
        } //이 부분을 Android 버전별 권한 승인으로 바꾸기!!

        //사진 촬영->이미지 뷰 띄우기 (Android Jetpack)
        cameraLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
                if (success && photoUri != null) {
                    binding.photo.setImageURI(photoUri)

                    // 사진 촬영 후 DecoFragment로 전환
                    openDecoFragment()
                } else {
                    // 사진 촬영이 실패했을 경우 버튼 다시 활성화
                    binding.cameraBtn.isEnabled = true
                }
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

    private fun openDecoFragment() {
        val fragmentTransaction = parentFragmentManager.beginTransaction()

        // DecoFragment에 URI를 전달하기 위해 Bundle을 생성
        val decoFragment = DecoFragment().apply {
            arguments = Bundle().apply {
                putString("photoUri", photoUri.toString()) // URI를 문자열로 변환하여 전달
            }
        }

        fragmentTransaction.replace(R.id.main_frm, decoFragment)
        /*fragmentTransaction.addToBackStack(null)*/  // 백스택에 추가하여 뒤로 가기 가능하게 함
        fragmentTransaction.commit()
    }

}
