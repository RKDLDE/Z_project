package com.example.z_project

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.MediaStore
import android.view.View
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.z_project.chat.ChatFragment
import com.example.z_project.databinding.ActivityMainBinding
import com.example.z_project.login.LoginFragment
import com.example.z_project.mypage.MypageFragment
import com.example.z_project.record.RecordFragment
import com.example.z_project.upload.UploadFragment
import com.example.z_project.mypage.LogoutFragment
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var imageCaptureLauncher: ActivityResultLauncher<Intent>
    private lateinit var currentPhotoUri: Uri
    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBottomNavigation()

        imageView = findViewById(R.id.imageView) // XML에서 ImageView 참조
        // ActivityResultLauncher 초기화
        imageCaptureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val bitmap = BitmapFactory.decodeFile(currentPhotoUri.path)
                imageView.setImageBitmap(bitmap)
            }
        }

    }

    @Throws(IOException::class)
    fun capturePhoto(view: View) {
        // 카메라 앱 호출을 위한 Intent 생성
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            // 이미지 파일 생성
            val imageFile: File? = createImageFile()
            imageFile?.also {
                // 이미지가 저장될 파일의 URI 생성
                currentPhotoUri = FileProvider.getUriForFile(
                    this,
                    "com.example.myapp.fileprovider", // FileProvider authority
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
        val storageDir: File? = getExternalFilesDir(null)
        return File.createTempFile("IMG_", ".jpg", storageDir)
    }


    private fun initBottomNavigation(){

        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, UploadFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener{ item ->
            when (item.itemId) {

                R.id.uploadFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, UploadFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.recordFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, RecordFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.chatFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, ChatFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.mypageFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, MypageFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_frm, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

//    btn1.setOnClickListener {
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.view, LoginFragment())
//            .commit()
//    }
}