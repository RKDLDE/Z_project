package com.example.z_project.upload

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View


class DrawView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : View(context, attrs, defStyle) {

    companion object {
        const val MODE_PEN = 1  // 펜 모드
        const val MODE_ERASER = 0 // 지우개 모드
    }

    private val PEN_SIZE = 5   // 펜 사이즈
    private val ERASER_SIZE = 30 // 지우개 사이즈

    private var drawCommandList: ArrayList<Pen> = ArrayList() // 그리기 경로 기록
    private var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)   // 펜
    var loadDrawImage: Bitmap? = null // 호출된 이전 그림
    private var color: Int = Color.BLACK // 현재 펜 색상
    private var size: Int = PEN_SIZE     // 현재 펜 크기

    init {
        initDrawCanvas()
    }


    private fun initDrawCanvas() {
        drawCommandList.clear()
        paint = Paint(Paint.ANTI_ALIAS_FLAG)
        color = Color.BLACK
        size = PEN_SIZE
    }

    fun getCurrentCanvas(): Bitmap {
        val bitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        this.draw(canvas)
        return bitmap
    }

    fun changeTool(toolMode: Int) {
        if (toolMode == MODE_PEN) {
            color = Color.BLACK
            size = PEN_SIZE
        } else if(toolMode == MODE_ERASER) {
            color = Color.WHITE
            size = ERASER_SIZE
        } else{

        }
        paint.color = color
        paint.strokeWidth = size.toFloat()
    }

    fun setImage(bitmap: Bitmap) {
        loadDrawImage = bitmap
        invalidate()
    }


    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.WHITE)

        loadDrawImage?.let {
            canvas.drawBitmap(it, 0f, 0f, null)
        }

        for (i in drawCommandList.indices) {
            val p = drawCommandList[i]
            paint.color = p.color
            paint.strokeWidth = p.size.toFloat()

            if (p.isMove() && i > 0) {
                val prevP = drawCommandList[i - 1]
                canvas.drawLine(prevP.x, prevP.y, p.x, p.y, paint)
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        val state = if (action == MotionEvent.ACTION_DOWN) Pen.STATE_START else Pen.STATE_MOVE
        drawCommandList.add(Pen(event.x, event.y, state, color, size))
        invalidate()
        return true
    }

    data class Pen(
        val x: Float,
        val y: Float,
        val moveStatus: Int,
        val color: Int,
        val size: Int
    ) {
        companion object {
            const val STATE_START = 0
            const val STATE_MOVE = 1
        }

        fun isMove(): Boolean {
            return moveStatus == STATE_MOVE
        }
    }
}








//    private val doodle = Paint(Paint.ANTI_ALIAS_FLAG).apply {  //그림그리기
//        color = Color.BLACK
//        style = Paint.Style.FILL
//        strokeWidth = 5f
//    }
////    private var pointerX: Float = 0f  // 터치 X 좌표
////    private var pointerY: Float = 0f  // 터치 Y 좌표
////    private val pointerSize: Float = 20f  // 원의 반지름
//
//
//    var isDrawing = false
//    private val touchPoints = mutableListOf<Pair<Float, Float>>()  // 터치 좌표를 저장할 리스트
//
//    private var backgroundImage: Bitmap? = null
//    // Set the image for drawing
//    fun setImage(bitmap: Bitmap) {
//        backgroundImage = bitmap
//        invalidate()  // Redraw with the new image
//    }
//
//    public override fun onDraw(canvas: Canvas) {
//        super.onDraw(canvas);
//
//        // Draw the background image if available
//        backgroundImage?.let {
//            canvas.drawBitmap(it, 0f, 0f, null)
//        }
//
//        // Draw user touches if in drawing mode
//        if (isDrawing) {
//            for (point in touchPoints) {
//                canvas.drawCircle(point.first, point.second, 10f, doodle)  // Draw circles at touch points
//            }
//        }
//        // 사용자가 터치한 위치에 선 그리기
//        /*canvas.drawLine(0f, pointerY, pointerX, pointerY, doodle)*/
//        // 사용자가 터치한 위치에 원 그리기
//        /*canvas.drawCircle(pointerX, pointerY, pointerSize, doodle)*/
//    }
//
//
//    override fun onTouchEvent(event: MotionEvent): Boolean {
////        when (event.action) {
////            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
////                // 터치 좌표를 업데이트
////                pointerX = event.x
////                pointerY = event.y
////                // 터치 좌표를 저장
////                touchPoints.add(Pair(pointerX, pointerY))
////                invalidate()  // 화면을 다시 그리도록 요청
////            }
////        }
//        if (isDrawing && (event.action == MotionEvent.ACTION_DOWN || event.action == MotionEvent.ACTION_MOVE)) {
//            touchPoints.add(Pair(event.x, event.y))
//            invalidate()  // Redraw the view
//        }
//        return true
//    }
//
//    fun startDrawing() {
//        isDrawing = true  // 그리기 활성화
//    }
//
//    fun stopDrawing() {
//        isDrawing = false  // 그리기 비활성화
//    }


//    init {
//        init() // 뷰 초기화
//    }
//
//    private fun init() {
//        textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
//        textPaint.color = textColor
//        if (textHeight == 0f) {
//            textHeight = textPaint.textSize
//        } else {
//            textPaint.textSize = textHeight
//        }
//    }


//    private val path = android.graphics.Path()
//    fun clear() {
//        path.reset()
//        invalidate()
//    }
