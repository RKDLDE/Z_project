package com.example.z_project.upload

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.io.path.Path

class CustomView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {
    private val paint = Paint().apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
        strokeWidth = 10f
        isAntiAlias = true
    }

    private val path = android.graphics.Path()
    var isDrawing = false // 그림 그릴지 여부를 제어하는 플래그

    public override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isDrawing) {
            canvas.drawPath(path, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                path.moveTo(x, y)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(x, y)
            }
        }
        invalidate() // View를 다시 그립니다.
        return true
    }

    // "sss" 버튼이 클릭될 때 호출
    fun startDrawing() {
        isDrawing = true
        invalidate() // 뷰를 다시 그리도록 트리거
    }


    fun clear() {
        path.reset()
        invalidate()
    }
}
