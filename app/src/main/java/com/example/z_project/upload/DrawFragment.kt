package com.example.z_project.upload

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.z_project.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.IOException

class DrawFragment : Fragment() {
    private lateinit var drawCanvas: DrawCanvas
    private lateinit var fbPen: FloatingActionButton
    private lateinit var fbEraser: FloatingActionButton
    private lateinit var fbSave: FloatingActionButton
    private lateinit var fbOpen: FloatingActionButton
    private lateinit var canvasContainer: ConstraintLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_draw, container, false)

        // Initialize UI elements
        canvasContainer = view.findViewById(R.id.lo_canvas)
        fbPen = view.findViewById(R.id.fb_pen)
        fbEraser = view.findViewById(R.id.fb_eraser)
        fbSave = view.findViewById(R.id.fb_save)
//        fbOpen = view.findViewById(R.id.fb_open)

        drawCanvas = DrawCanvas(requireContext())
        canvasContainer.addView(drawCanvas)

        setOnClickListener()

        return view
    }

    private fun setOnClickListener() {
        fbPen.setOnClickListener {
            drawCanvas.changeTool(DrawCanvas.MODE_PEN)
        }

        fbEraser.setOnClickListener {
            drawCanvas.changeTool(DrawCanvas.MODE_ERASER)
        }

        fbSave.setOnClickListener {
            drawCanvas.invalidate()
            val saveBitmap = drawCanvas.getCurrentCanvas()
            CanvasIO.saveBitmap(requireContext(), saveBitmap)
        }

//        fbOpen.setOnClickListener {
//            drawCanvas.init()
//            drawCanvas.loadDrawImage = CanvasIO.openBitmap(requireContext())
//            drawCanvas.invalidate()
//        }
    }

    class DrawCanvas(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
        companion object {
            const val MODE_PEN = 1
            const val MODE_ERASER = 0
        }

        private val PEN_SIZE = 3
        private val ERASER_SIZE = 30

        private val drawCommandList = ArrayList<Pen>()
        private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        var loadDrawImage: Bitmap? = null
        private var color = Color.BLACK
        private var size = PEN_SIZE

        init {
            initCanvas()
        }

        private fun initCanvas() {
            paint.color = color
            paint.strokeWidth = size.toFloat()
        }

        fun getCurrentCanvas(): Bitmap {
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            draw(canvas)
            return bitmap
        }

        fun changeTool(toolMode: Int) {
            if (toolMode == MODE_PEN) {
                color = Color.BLACK
                size = PEN_SIZE
            } else {
                color = Color.WHITE
                size = ERASER_SIZE
            }
            paint.color = color
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

                if (p.isMove) {
                    val prevP = drawCommandList[i - 1]
                    canvas.drawLine(prevP.x, prevP.y, p.x, p.y, paint)
                }
            }
        }

        override fun onTouchEvent(event: MotionEvent): Boolean {
            val state = if (event.action == MotionEvent.ACTION_DOWN) Pen.STATE_START else Pen.STATE_MOVE
            drawCommandList.add(Pen(event.x, event.y, state, color, size))
            invalidate()
            return true
        }
    }

    class Pen(var x: Float, var y: Float, var moveStatus: Int, var color: Int, var size: Int) {
        companion object {
            const val STATE_START = 0
            const val STATE_MOVE = 1
        }

        val isMove: Boolean
            get() = moveStatus == STATE_MOVE
    }

    object CanvasIO {
        private const val TAG = "CanvasIO"
        private const val FILE_NAME = "draw_file.jpg"

        fun saveBitmap(context: Context, saveFile: Bitmap) {
            try {
                val fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)
                saveFile.compress(Bitmap.CompressFormat.PNG, 100, fos)
                fos.close()
            } catch (e: IOException) {
                Log.e(TAG, "캔버스를 저장하지 못했습니다.")
                e.printStackTrace()
            }
        }

        fun openBitmap(context: Context): Bitmap? {
            var result: Bitmap? = null

            try {
                val fis = context.openFileInput(FILE_NAME)
                result = BitmapFactory.decodeStream(fis)
                fis.close()
            } catch (e: IOException) {
                Log.e(TAG, "캔버스를 열지 못했습니다.")
                e.printStackTrace()
            }

            return result
        }
    }
}
