package com.example.z_project

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import com.bumptech.glide.request.transition.Transition
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.google.firebase.firestore.FirebaseFirestore

class AppWidgetProvider : AppWidgetProvider() {

    private val firestore = FirebaseFirestore.getInstance()

    // AlarmManager를 통해 15분마다 업데이트 설정
    private fun scheduleUpdate(context: Context) {
        val intent = Intent(context, AppWidgetProvider::class.java).apply {
            action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        }
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // 15분마다 업데이트 설정
        alarmManager.setInexactRepeating(AlarmManager.RTC, System.currentTimeMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent)
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        // 위젯이 활성화될 때 스케줄 설정
        scheduleUpdate(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        // 위젯이 비활성화될 때 알람 취소
        val intent = Intent(context, AppWidgetProvider::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE)
        if (pendingIntent != null) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
        }
    }

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    companion object {
        fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
            val views = RemoteViews(context.packageName, R.layout.widget_feed)
            val sharedPreferences = context.getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
            /*val uniqueCode = "TN4IJVUNM8cn"*/
            val uniqueCode = sharedPreferences.getString("UNIQUE_CODE", null)

            appWidgetManager.updateAppWidget(appWidgetId, views)

            Log.d("widget", "내 코드: $uniqueCode")

            if (uniqueCode != null) {
                loadFriendList(uniqueCode, context, appWidgetManager, appWidgetId, views)
            } else {
                Log.e("widget", "고유 코드가 존재하지 않습니다.")
            }


        }

        private fun loadFriendList(uniqueCode: String, context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int, views: RemoteViews) {
            FirebaseFirestore.getInstance().collection("friends")
                .document(uniqueCode)
                .collection("friendsList")
                .addSnapshotListener { querySnapshot, exception ->
                    if (exception != null) {
                        Log.e("widget", "친구 목록 조회 실패: ${exception.message}")
                        return@addSnapshotListener
                    }

                    val friendCodes = querySnapshot?.documents?.mapNotNull { it.getString("friendCode") } ?: emptyList()
                    Log.d("widget", "친구 코드 수: ${friendCodes.size}")

                    if (friendCodes.isNotEmpty()) {
                        getLatestUploadForFriends(friendCodes, context, appWidgetManager, appWidgetId, views)
                    } else {
                        Log.d("widget", "친구가 없습니다.")
                    }
                }
        }

        private fun getLatestUploadForFriends(friendCodes: List<String>, context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int, views: RemoteViews) {
            var completedRequests = 0 // 모든 요청 완료 여부 확인을 위한 카운터

            for (friendCode in friendCodes) {
                val latestUploadRef = FirebaseFirestore.getInstance()
                    .collection("images")
                    .whereEqualTo("uniqueCode", friendCode)
                    .orderBy("uploadTime", com.google.firebase.firestore.Query.Direction.DESCENDING)
                    .limit(1)

                // 최신 업로드 문서를 가져오기
                latestUploadRef.get().addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val latestDocument = documents.first()
                        val emoji = latestDocument.getString("emoji") ?: ""
                        val text = latestDocument.getString("inputText") ?: ""
                        val uploadImageUrl = latestDocument.getString("uploadImage") // 최신 업로드의 이미지 URL
                        val uniqueCode = latestDocument.getString("uniqueCode") // 친구의 uniqueCode

                        Log.d("widget", "친구($friendCode)의 텍스트: $text")

                        // 프로필 이미지 URL 가져오기
                        if (uniqueCode != null) {
                            fetchUserProfileImage(uniqueCode, context, views, appWidgetId)
                        }

                        // 텍스트 가시성 설정
                        if (text.isNotEmpty()) {
                            views.setViewVisibility(R.id.upload_text, View.VISIBLE)
                            views.setTextViewText(R.id.upload_text, text)
                        } else {
                            views.setViewVisibility(R.id.upload_text, View.GONE)
                        }
                        views.setTextViewText(R.id.upload_emoji, emoji)
                    } else {
                        //  Log.d("widget", "${friendCode}의 최신 업로드가 없습니다.")
                    }
                    completedRequests++
                    if (completedRequests == friendCodes.size) {
                        // 모든 요청이 완료되면 위젯 업데이트
                        appWidgetManager.updateAppWidget(appWidgetId, views)
                    }
                }
            }
        }

        private fun fetchUserProfileImage(uniqueCode: String, context: Context, views: RemoteViews, appWidgetId: Int) {
            FirebaseFirestore.getInstance().collection("users").document(uniqueCode)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val profileImageUrl = document.getString("profileImage") ?: ""
                        //Log.d("widget", "프로필 이미지 URL: $profileImageUrl")

                        // 프로필 이미지 URL이 비어 있지 않을 경우 처리
                        if (profileImageUrl.isNotEmpty()) {
                            // URI가 content://로 시작하면 Glide를 사용하여 이미지 로드
                            val uri = Uri.parse(profileImageUrl)
                            Glide.with(context)
                                .asBitmap() // 비트맵으로 로드
                                .load(uri)
                                .apply(RequestOptions.circleCropTransform())
                                .into(object : CustomTarget<Bitmap>() {
                                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                        // 비트맵이 준비되면 RemoteViews에 설정
                                        views.setImageViewBitmap(R.id.profile_image, resource)
                                        // 위젯 업데이트
                                        AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, views)
                                    }

                                    override fun onLoadCleared(placeholder: Drawable?) {
                                        // Placeholder 로드 필요 시 처리
                                    }
                                })
                        } else {
                            Log.d("widget", "프로필 이미지 URL이 비어 있습니다.")
                        }
                    } else {
                        Log.d("widget", "사용자 프로필 데이터가 없습니다.")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("widget", "프로필 이미지 가져오기 실패: ${exception.message}")
                }
        }
    }
}