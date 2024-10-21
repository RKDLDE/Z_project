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
    private val paths = mutableListOf<Pair<android.graphics.Path, Paint>>()  // 그려진 경로들을 저장할 리스트
    private var undonePaths = mutableListOf<Pair<android.graphics.Path, Paint>>()  // 되돌리기 위한 경로들

    private var currentPaint = Paint().apply {
        color = Color.BLACK
        strokeWidth = 10f
        style = Paint.Style.STROKE
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
        currentPaint = Paint(currentPaint).apply {
            this.color = color
        }
    }

    // 이미지 Bitmap을 설정하는 함수
    fun setImageUri(uri: Uri) {
        val inputStream = context.contentResolver.openInputStream(uri)
        val originalBitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        if (originalBitmap != null) {
            // 비트맵을 뷰 크기에 맞게 비율에 맞게 조정
            bitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, true)
        } else {
            Log.e("CustomView", "Failed to decode bitmap from uri")
        }
        invalidate()  // 뷰를 다시 그리기
    }

    // 새로운 메서드 추가
    fun setImageBitmap(bitmap: Bitmap?) {
        this.bitmap = rotateBitmap(bitmap) // 전달된 비트맵을 현재 비트맵으로 설정
        invalidate() // 뷰를 다시 그리기
    }

    private fun rotateBitmap(bitmap: Bitmap?): Bitmap?  {
        bitmap?.let {
            // 90도 회전하기 위한 Matrix 생성
            val matrix = android.graphics.Matrix().apply {
                postRotate(90f) // 90도 회전
            }

            // 회전된 비트맵 생성
            val rotatedBitmap = Bitmap.createBitmap(it, 0, 0, it.width, it.height, matrix, true)

            // XML에서 설정한 크기 가져오기
            // 탭 500, 600
            val targetWidth = 1200
            val targetHeight = 1300

            // 비율 유지: 원래 크기를 기준으로 비율을 계산
            val originalWidth = rotatedBitmap.width
            val originalHeight = rotatedBitmap.height
            val aspectRatio = originalWidth.toFloat() / originalHeight.toFloat()

            // 새 크기 계산 (XML의 크기에 맞춤)
            val newWidth: Int
            val newHeight: Int

            if (targetWidth > targetHeight) {
                newWidth = targetWidth
                newHeight = (targetWidth / aspectRatio).toInt()
            } else {
                newHeight = targetHeight
                newWidth = (targetHeight * aspectRatio).toInt()
            }

            // 고품질 비트맵 생성
            val scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(scaledBitmap)

            // 품질 높은 비트맵을 새로운 크기로 그리기
            canvas.drawBitmap(rotatedBitmap, null, android.graphics.Rect(0, 0, newWidth, newHeight), null)

            return scaledBitmap
        }
        return null
    }

    override fun onDraw(canvas: Canvas) {
        // Bitmap이 존재하면 그리기
        bitmap?.let {

            canvas.drawBitmap(it, 0f, 0f, null)
        }

        // 저장된 모든 경로를 그리기 (각 경로의 색상으로)
        for ((savedPath, savedPaint) in paths) {
            canvas.drawPath(savedPath, savedPaint)
        }

        // Path를 따라 그리기 (현재 그리는 그림)
        canvas.drawPath(path, currentPaint)
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
                        paths.add(Pair(android.graphics.Path(path), Paint(currentPaint)))  // 현재 경로와 색상을 저장
                        path.reset()
                    }
                }
            }
        }
        return true
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

        // 모든 저장된 경로를 새로운 캔버스에 그리기
        for ((savedPath, savedPaint) in paths) {
            combinedCanvas.drawPath(savedPath, savedPaint)
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
