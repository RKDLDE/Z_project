package com.example.z_project

import android.appwidget.AppWidgetManager
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.z_project.AppWidgetProvider

class WidgetUpdateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("WidgetUpdateReceiver", "onReceive called with action: ${intent.action}")

        // 위젯 업데이트 액션인지 확인
        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE == intent.action) {
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS)


            // 위젯 ID가 null이 아닐 경우 각 위젯을 업데이트
            if (appWidgetIds != null) {
                for (appWidgetId in appWidgetIds) {
                    AppWidgetProvider.updateAppWidget(context, appWidgetManager, appWidgetId)
                }
            }
        }
    }
}
