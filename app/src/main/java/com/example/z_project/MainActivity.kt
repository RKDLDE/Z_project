package com.example.z_project


import android.content.pm.PackageManager
import android.net.Uri

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import androidx.fragment.app.Fragment
import com.example.z_project.chat.ChatFragment
import com.example.z_project.databinding.ActivityMainBinding
import com.example.z_project.mypage.MypageFragment
import com.example.z_project.record.RecordFragment
import com.example.z_project.upload.UploadFragment

import com.example.z_project.mypage.LogoutFragment
import com.example.z_project.MainActivity
import android.Manifest


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initBottomNavigation()

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