package com.example.z_project.upload

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.z_project.R
import com.example.z_project.databinding.FragmentDecoBinding
import com.example.z_project.databinding.FragmentDrawBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.IOException

//class DecoFragment : Fragment() {
//    private lateinit var binding: FragmentDecoBinding
//    private lateinit var _binding: FragmentDrawBinding
//
//    private lateinit var img: ImageView // 이미지를 표시할 ImageView(=canvasContainer)
//
//    private lateinit var fbPen: FloatingActionButton
//    private lateinit var fbEraser: FloatingActionButton
//    private lateinit var save: FloatingActionButton
//
//    private lateinit var drawCanvas: DrawFragment.DrawCanvas
//    private lateinit var canvasContainer: ConstraintLayout
//
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
//        binding = FragmentDecoBinding.inflate(inflater, container, false)
//        val view = inflater.inflate(R.layout.fragment_draw, container, false)
//        img = binding.photo
//
//        arguments?.getString("photoUri")?.let { uriString ->
//            val photoUri = Uri.parse(uriString)
//
//            // Glide를 사용하여 이미지 로드
//            Glide.with(this)
//                .load(photoUri)
//                .into(img) // photoLayout이 ImageView이어야 함
//
//        // Initialize UI elements
//        /*canvasContainer = view.findViewById(R.id.lo_canvas)*/
//        fbPen = view.findViewById(R.id.fb_pen)
//        fbEraser = view.findViewById(R.id.fb_eraser)
//        save = view.findViewById(R.id.fb_save)
//
//
//        binding.sss.setOnClickListener{
//            drawCanvas = DrawFragment.DrawCanvas(requireContext())
//            canvasContainer.addView(drawCanvas)
//            arguments?.getString("photoUri")?.let { uriString ->
//                val photoUri = Uri.parse(uriString)
//
//                // Glide를 사용하여 이미지 로드
//                Glide.with(this)
//                    .load(photoUri)
//                    .into(binding.photo) // photoLayout이 ImageView이어야 함
//            }
//        }
//
//       /* setOnClickListener()*/
//
//        return binding.root
//    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        binding.sss.setOnClickListener{
//            drawCanvas = DrawFragment.DrawCanvas(requireContext())
//            canvasContainer.addView(drawCanvas)
//            arguments?.getString("photoUri")?.let { uriString ->
//                val photoUri = Uri.parse(uriString)
//
//                // Glide를 사용하여 이미지 로드
//                Glide.with(this)
//                    .load(photoUri)
//                    .into(binding.photo) // photoLayout이 ImageView이어야 함
//            }
//        }
//
//
//        binding.emoji.setOnClickListener{
//            val emojiFragment = EmojiFragment()
//            val fragmentManager = parentFragmentManager
//            val fragmentTransaction = fragmentManager.beginTransaction()
//            fragmentTransaction.replace(R.id.container, emojiFragment)
//            /*fragmentTransaction.addToBackStack(null)*/
//            fragmentTransaction.commit()
//        }
//    }
//
//    private fun setOnClickListener() {
//        fbPen.setOnClickListener {
//            drawCanvas.changeTool(DrawFragment.DrawCanvas.MODE_PEN)
//        }
//
//        fbEraser.setOnClickListener {
//            drawCanvas.changeTool(DrawFragment.DrawCanvas.MODE_ERASER)
//        }
//
//        save.setOnClickListener {
//            drawCanvas.invalidate()
//            val saveBitmap = drawCanvas.getCurrentCanvas()
//            DrawFragment.CanvasIO.saveBitmap(requireContext(), saveBitmap)
//        }

//        fbOpen.setOnClickListener {
//            drawCanvas.init()
//            drawCanvas.loadDrawImage = CanvasIO.openBitmap(requireContext())
//            drawCanvas.invalidate()
//        }
    }

//    class DrawCanvas(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
//        companion object {
//            const val MODE_PEN = 1
//            const val MODE_ERASER = 0
//        }
//
//        private val PEN_SIZE = 3
//        private val ERASER_SIZE = 30
//
//        private val drawCommandList = ArrayList<DrawFragment.Pen>()
//        private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
//        var loadDrawImage: Bitmap? = null
//        private var color = Color.BLACK
//        private var size = PEN_SIZE
//
//        init {
//            initCanvas()
//        }
//
//        private fun initCanvas() {
//            paint.color = color
//            paint.strokeWidth = size.toFloat()
//        }
//
//        fun getCurrentCanvas(): Bitmap {
//            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
//            val canvas = Canvas(bitmap)
//            draw(canvas)
//            return bitmap
//        }
//
//        fun changeTool(toolMode: Int) {
//            if (toolMode == MODE_PEN) {
//                color = Color.BLACK
//                size = PEN_SIZE
//            } else {
//                color = Color.WHITE
//                size = ERASER_SIZE
//            }
//            paint.color = color
//        }
//
//        override fun onDraw(canvas: Canvas) {
//            canvas.drawColor(Color.WHITE)
//
//            loadDrawImage?.let {
//                canvas.drawBitmap(it, 0f, 0f, null)
//            }
//
//            for (i in drawCommandList.indices) {
//                val p = drawCommandList[i]
//                paint.color = p.color
//                paint.strokeWidth = p.size.toFloat()
//
//                if (p.isMove) {
//                    val prevP = drawCommandList[i - 1]
//                    canvas.drawLine(prevP.x, prevP.y, p.x, p.y, paint)
//                }
//            }
//        }
//
//        override fun onTouchEvent(event: MotionEvent): Boolean {
//            val state = if (event.action == MotionEvent.ACTION_DOWN) Pen.STATE_START else Pen.STATE_MOVE
//            drawCommandList.add(Pen(event.x, event.y, state, color, size))
//            invalidate()
//            return true
//        }
//    }
//
//    class Pen(var x: Float, var y: Float, var moveStatus: Int, var color: Int, var size: Int) {
//        object {
//            const val STATE_START = 0
//            const val STATE_MOVE = 1
//        }
//
//        val isMove: Boolean
//            get() = moveStatus == STATE_MOVE
//    }
//
//    // 사진 크기 조정을 위한 함수
//    private fun resizeImage(uri: Uri, width: Int, height: Int): Bitmap? {
//        val inputStream = requireContext().contentResolver.openInputStream(uri)
//        val originalBitmap = BitmapFactory.decodeStream(inputStream)
//        inputStream?.close()
//        // Bitmap 크기 조정
//        return originalBitmap?.let {
//            Bitmap.createScaledBitmap(it, width, height, true)
//        }
//    }
}