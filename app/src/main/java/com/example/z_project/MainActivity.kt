package com.example.z_project


import android.Manifest
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.z_project.chat.ChatFragment
//import com.example.z_project.chatting2.ChatFragment
import com.example.z_project.databinding.ActivityMainBinding
import com.example.z_project.mypage.MypageFragment
import com.example.z_project.qna.QuestionFeedFragment
import com.example.z_project.record.RecordFragment
import com.example.z_project.upload.FinalFragment
import com.example.z_project.upload.UploadFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startService(Intent(this, WidgetFirebase::class.java))
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_main)

        // Intent에서 Fragment 정보를 확인하여 해당 Fragment로 이동
        val openFragment = intent.getStringExtra("OPEN_FRAGMENT")
        if (openFragment == "QuestionFeedFragment") {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, UploadFragment())
                .commit()
        } else {
            // 기본 Fragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, UploadFragment())
                .commit()
        }

        initBottomNavigation()

        handleIntentData()  // 푸시 알림 데이터 처리
        fetchFcmToken()      // FCM 토큰 가져오기
    }

    private fun handleIntentData() {
        val intent = getIntent()
        if(intent != null) {
            val notificationData = intent.getStringExtra("test")
            if(notificationData != null) {
                Log.d("FCM_TEST", notificationData)
            }
        }
    }

    private fun fetchFcmToken() {
        FirebaseMessaging.getInstance().getToken()
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("MainActivity", "Fetching FCM registration token failed")
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result
                Log.d("MainActivity", "FCM Token: $token")
            })
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
                    // MypageFragment로 이동할 때 사용자 정보 전달
                    val userName = intent.getStringExtra("USER_NAME")
                    val profileImageUrl = intent.getStringExtra("PROFILE_IMAGE")
                    val token = intent.getStringExtra("TOKEN")

                    val mypageFragment = MypageFragment().apply {
                        arguments = Bundle().apply {
                            putString("USER_NAME", userName)
                            putString("PROFILE_IMAGE", profileImageUrl)
                            putString("TOKEN", token)
                        }
                    }
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, mypageFragment)
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
    private val CAMERA_REQUEST_CODE = 100
    private val STORAGE_REQUEST_CODE = 101

    private fun checkAndRequestPermissions() {
        // 카메라 권한 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
        }

        // 저장소 권한 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), STORAGE_REQUEST_CODE)
        }
    }

    // 권한 결과 처리
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // 카메라 권한이 거부됨
                    Toast.makeText(this, "카메라 권한이 필요합니다.", Toast.LENGTH_LONG).show()
                }
            }
            STORAGE_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // 저장소 권한이 거부됨
                    Toast.makeText(this, "저장소 권한이 필요합니다.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}