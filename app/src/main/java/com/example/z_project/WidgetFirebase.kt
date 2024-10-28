package com.example.z_project

import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.content.ComponentName
import android.content.SharedPreferences
import android.os.IBinder
import android.util.Log
import com.example.z_project.AppWidgetProvider
import com.google.firebase.firestore.FirebaseFirestore

class WidgetFirebase : Service() {
    private lateinit var firestore: FirebaseFirestore
    private var userId: String? = null
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        firestore = FirebaseFirestore.getInstance()
        sharedPreferences = getSharedPreferences("MY_PREFS", MODE_PRIVATE)
        userId = sharedPreferences.getString("UNIQUE_CODE", null)

        setupFirestoreListener()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        setupFirestoreListener() // 서비스 시작 시 Firestore 리스너 설정
        return START_STICKY // 서비스가 종료된 후에도 재시작하도록 설정
    }

    private fun setupFirestoreListener() {
        // 친구 목록 가져오기
        firestore.collection("friends")
            .document(userId!!)
            .collection("friendsList")
            .get()
            .addOnSuccessListener { friendSnapshots ->
                val friendCodes =
                    friendSnapshots.documents.mapNotNull { it.getString("friendCode") }

                // 친구 코드가 있는 경우에만 리스너 설정
                if (friendCodes.isNotEmpty()) {
                    // 친구의 데이터에 대한 리스너 설정
                    firestore.collection("images")
                        .whereIn("uniqueCode", friendCodes) // 친구들의 uniqueCode에 해당하는 데이터만
                        .addSnapshotListener { snapshots, e ->
                            if (e != null) {
                                Log.w("Service", "Listen failed.", e)
                                return@addSnapshotListener
                            }

                            if (snapshots != null && !snapshots.isEmpty) {
                                for (document in snapshots.documentChanges) {
                                    if (document.type == com.google.firebase.firestore.DocumentChange.Type.ADDED) {
                                        Log.d(
                                            "Service",
                                            "친구의 새 데이터가 추가되었습니다: ${document.document.data}"
                                        )
                                        updateWidget(true) // 위젯 업데이트 호출
                                    }
                                }
                            }
                        }
                } else {
                    Log.d("Service", "친구가 없습니다.")
                }
            }
            .addOnFailureListener { e ->
                Log.w("Service", "친구 목록 가져오기 실패.", e)
            }
    }

        private fun updateWidget(condition: Boolean) {
        val intent = Intent(if (condition) AppWidgetManager.ACTION_APPWIDGET_UPDATE else "com.example.z_project.UPDATE_CONDITION")
        val widgetIds = AppWidgetManager.getInstance(this).getAppWidgetIds(
            ComponentName(this, AppWidgetProvider::class.java)
        )
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, widgetIds)
        sendBroadcast(intent)
    }

    // 서비스가 종료될 때 리스너 제거 (optional)
    override fun onDestroy() {
        super.onDestroy()
        // 리스너 제거 로직 추가 가능
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
