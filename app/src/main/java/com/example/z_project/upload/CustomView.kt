package com.example.z_project.upload

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import java.io.FileOutputStream

class CustomView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr) {

    private var path = android.graphics.Path()  // 그림 그리기를 위한 Path(현재)
    private val paths = mutableListOf<android.graphics.Path>()  // 그려진 경로들을 저장할 리스트
    private val undonePaths = mutableListOf<android.graphics.Path>()  // 되돌리기 위한 경로들

    private val paint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 10f
        style = Paint.Style.STROKE
        isAntiAlias = true
    }
    private val textPaint = Paint().apply {
        color = Color.RED
        textSize = 60f
        isAntiAlias = true
    }

    private var xPos: Float = 0f
    private var yPos: Float = 0f
    private var inputText: String = ""
    private var drawingMode: Boolean = false  // 그림 그리기 모드 여부

    private var bitmap: Bitmap? = null
    private var canvas: Canvas? = null

    init {
        // Bitmap과 Canvas 초기화
        bitmap = Bitmap.createBitmap(800, 1200, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap!!)
        canvas?.drawColor(Color.WHITE)  // 배경을 흰색으로 설정
    }

    fun setColor(color: Int) {
        paint.color = color
    }

    // 이미지 Bitmap을 설정하는 함수
    fun setImageUri(uri: Uri) {
        val inputStream = context.contentResolver.openInputStream(uri)
        bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()
        if (bitmap == null) {
            // 비트맵이 null일 경우의 처리 (예: 로그 출력)
            Log.e("CustomView", "Failed to decode bitmap from uri")
        }
        invalidate()  // 뷰를 다시 그리기
    }


    override fun onDraw(canvas: Canvas) {
        if (canvas != null) {
            super.onDraw(canvas)
        } else{
            return
        }

        // Bitmap이 존재하면 그리기
        bitmap?.let {
            canvas.drawBitmap(it, 0f, 0f, null)
        }

        // 저장된 모든 경로를 그리기
        for (savedPath in paths) {
            canvas.drawPath(savedPath, paint)
        }

        // Path를 따라 그리기 (그림)
        canvas.drawPath(path, paint)

        // 사용자가 입력한 텍스트를 그리기
        if (inputText.isNotEmpty()) {
            canvas.drawText(inputText, xPos, yPos, textPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            if (drawingMode) {
                // 그림 그리기 모드일 때
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        path.moveTo(it.x, it.y)
                    }
                    MotionEvent.ACTION_MOVE -> {
                        path.lineTo(it.x, it.y)
                        invalidate()
                    }
                    MotionEvent.ACTION_UP -> {
                        paths.add(android.graphics.Path(path))  // 현재 경로를 저장
                        path.reset()
                    }
                }
            } else {
                // 텍스트 입력 모드일 때, 터치한 좌표에 텍스트 추가
                when (it.action) {
                    MotionEvent.ACTION_DOWN -> {
                        xPos = it.x
                        yPos = it.y
                        invalidate()
                    }
                }
            }
        }
        return true
    }

    // 텍스트 입력 위치와 내용을 설정
    fun setText(newText: String, x: Float, y: Float) {
        inputText = newText
        xPos = x
        yPos = y
        invalidate()  // View 다시 그리기
    }
    // 그림 그리기 모드 설정 함수
    fun enableDrawingMode(enable: Boolean) {
        drawingMode = enable
    }
    // Undo 기능을 위한 경로 제거
    fun undo() {
        if (paths.isNotEmpty()) {
            undonePaths.add(paths.removeAt(paths.size - 1))  // 가장 최근 경로를 제거하고 보관
            invalidate()  // 뷰를 다시 그리기
        }
    }

    // Function to save the current drawing state as an image
    fun saveDrawingToFile(filePath: String) {
        // Create a new bitmap with the same dimensions as the original bitmap
        val combinedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val combinedCanvas = Canvas(combinedBitmap)

        // Draw the original bitmap on the new canvas
        bitmap?.let {
            combinedCanvas.drawBitmap(it, 0f, 0f, null)
        }

        // Draw all saved paths onto the new canvas
        for (savedPath in paths) {
            combinedCanvas.drawPath(savedPath, paint)
        }

        // Save the combined bitmap to a file
        try {
            val fileOutputStream = FileOutputStream(filePath)
            combinedBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
            Log.d("CustomView", "Drawing saved to $filePath")
        } catch (e: Exception) {
            Log.e("CustomView", "Error saving drawing: ${e.message}")
        }
    }

}