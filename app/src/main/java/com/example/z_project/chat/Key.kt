package com.example.z_project.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.auth.User
import com.google.firebase.ktx.Firebase
import java.util.*

class RegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Region이 US일 경우
        val database = Firebase.database
        val myRef = database.getReference("message")
        myRef.setValue("Hello, World!")
    }
}
//private val database: DatabaseReference =
//    Firebase.database("https://z-project-3116b-default-rtdb.firebaseio.com/").reference
//
//// user 라는 경로에 변수 userId에 해당하는 값(user라는 데이터 클래스의 값)을 넣는다
//fun addUser(userId: String, user: User) {
//    database.child("users").child(userId).setValue(user)
//}






//import com.google.firebase.database.DatabaseReference
//
//
//private lateinit var database : DatabaseReference
//class Key {
//    companion object{
//        const val DB_URL = "https://z-project-3116b-default-rtdb.firebaseio.com/"
//        const val DB_USERS = "Users"
//        const val DB_SHAT_ROOMS = "ChatRooms"
//        const val DB_CHATS = "CHATS"
//
//
//        // 1. FirebaseRealtimeDatabase의 객체 가져오기
//        val database = Firebase.database.reference
//    }


