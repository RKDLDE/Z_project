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
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

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

        // 최근 업로드 정보를 저장할 데이터 클래스
        data class LatestUpload(val friendCode: String, val emoji: String, val text: String, val uniqueCode: String?, val uploadTime: Timestamp?)

        private fun getLatestUploadForFriends(friendCodes: List<String>, context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int, views: RemoteViews) {
            val latestUploads = mutableListOf<LatestUpload>() // 최신 업로드를 수집할 리스트
            var completedRequests = 0 // 모든 요청이 완료되었는지 확인하기 위한 카운터

            for (friendCode in friendCodes) {
                FirebaseFirestore.getInstance()
                    .collection("images")
                    .whereEqualTo("uniqueCode", friendCode)
                    .orderBy("uploadTime", com.google.firebase.firestore.Query.Direction.DESCENDING)
                    .limit(1)
                    .addSnapshotListener { querySnapshot, exception ->
                        if (exception != null) {
                            Log.e("widget", "친구($friendCode)의 최신 업로드 조회 실패: ${exception.message}")
                            return@addSnapshotListener
                        }

                        if (querySnapshot != null && !querySnapshot.isEmpty) {
                            val latestDocument = querySnapshot.documents.first()
                            val emoji = latestDocument.getString("emoji") ?: ""
                            val text = latestDocument.getString("inputText") ?: ""
                            val uniqueCode = latestDocument.getString("uniqueCode") // 친구의 uniqueCode
                            val uploadTime = latestDocument.getTimestamp("uploadTime") // 업로드 시간 가져오기

                            // 최신 업로드를 수집
                            latestUploads.add(LatestUpload(friendCode, emoji, text, uniqueCode, uploadTime))
                        }

                        completedRequests++ // 요청 완료 카운터 증가
                        if (completedRequests == friendCodes.size) {
                            // 모든 요청이 완료되면 위젯 업데이트
                            updateWidgetWithLatestUploads(latestUploads, views, appWidgetManager, appWidgetId, context)
                        }
                    }
            }
        }

        // 최신 업로드 정보를 기반으로 위젯 업데이트
        private fun updateWidgetWithLatestUploads(latestUploads: List<LatestUpload>, views: RemoteViews, appWidgetManager: AppWidgetManager, appWidgetId: Int, context: Context) {
            val latestUpload = latestUploads.maxByOrNull { it.uploadTime?.toDate() ?: Date(0) }
            latestUpload?.let {
                views.setViewVisibility(R.id.upload_text, if (it.text.isNotEmpty()) View.VISIBLE else View.GONE)
                views.setTextViewText(R.id.upload_text, it.text)
                views.setTextViewText(R.id.upload_emoji, it.emoji)

                fetchUserProfileImage(it.uniqueCode ?: "", context, views, appWidgetId) // context 전달
            }

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        private fun fetchUserProfileImage(uniqueCode: String, context: Context, views: RemoteViews, appWidgetId: Int) {
            FirebaseFirestore.getInstance().collection("users").document(uniqueCode)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val profileImageUrl = document.getString("profileImage") ?: ""
                        if (profileImageUrl.isNotEmpty()) {
                            val uri = Uri.parse(profileImageUrl)
                            Glide.with(context)
                                .asBitmap()
                                .load(uri)
                                .apply(RequestOptions.circleCropTransform())
                                .into(object : CustomTarget<Bitmap>() {
                                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                        views.setImageViewBitmap(R.id.profile_image, resource)
                                        AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, views)
                                    }

                                    override fun onLoadCleared(placeholder: Drawable?) {
                                        // Handle placeholder if needed
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
