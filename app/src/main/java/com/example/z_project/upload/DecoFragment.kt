package com.example.z_project.upload

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.z_project.R
import com.example.z_project.databinding.FragmentDecoBinding

class DecoFragment : Fragment() {
    private lateinit var binding: FragmentDecoBinding
    private var undoStack = mutableListOf<String>()  // 이전 상태 저장
    private var photoUri: Uri? = null

    private lateinit var drawCanvas: DrawFragment.DrawCanvas
    private lateinit var canvasContainer: ConstraintLayout


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDecoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        photoUri = arguments?.getString("photoUri")?.let { Uri.parse(it) }
        if (photoUri != null) {
            resizeImage(photoUri!!, 300, 400)
            binding.photo.setImageUri(photoUri!!)
        }

        // 이전 상태 저장 (글자 입력이나 다른 동작이 이루어지기 전에)
        saveState()

        binding.drawBtn.setOnClickListener{
            binding.photo.enableDrawingMode(true)
            /*drawCanvas = DrawFragment.DrawCanvas(requireContext())
            canvasContainer.addView(drawCanvas)*/
            saveState()
        }

        binding.aaBtn.setOnClickListener {
            /*binding.myEditText.isEnabled = true
            binding.myEditText.requestFocus()  // 포커스를 설정하여 바로 입력 가능*/
            binding.photo.enableDrawingMode(false)
            val inputText = binding.myEditText.text.toString()
            binding.photo.setText(inputText, 0f, 0f)  // 텍스트 전달, 터치로 위치 결정
            binding.myEditText.isEnabled = true
            binding.myEditText.requestFocus()
            saveState()
        }

        binding.okayBtn.setOnClickListener {
            // Specify a file path where you want to save the drawing
            val filePath = "${context?.getExternalFilesDir(null)?.absolutePath}/saved_drawing.png"
            binding.photo.saveDrawingToFile(filePath)  // Save drawing to file

            val finalFragment = FinalFragment()
            val savedImageUri = Uri.parse("${context?.getExternalFilesDir(null)?.absolutePath}/saved_drawing.png")

            // Bundle에 URI 추가
            val bundle = Bundle().apply {
                putString("finalImageUri", savedImageUri.toString())
            }
            finalFragment.arguments = bundle

        }

        // Undo 버튼 클릭 시 이전 상태 복구
        binding.undoButton.setOnClickListener {
            binding.photo.undo()  // undo 호출
        }

        binding.emoji.setOnClickListener{
            val emojiFragment = EmojiFragment()
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.main_frm, emojiFragment)
            /*fragmentTransaction.addToBackStack(null)*/
            fragmentTransaction.commit()
        }
    }

    // 현재 상태를 저장하는 함수
    private fun saveState() {
        val currentText = binding.myEditText.text.toString()
        undoStack.add(currentText)  // 현재 상태 저장
    }
    companion object {
        private var xPos: Float = 0f  // 텍스트 입력 위치 x좌표
        private var yPos: Float = 0f  // 텍스트 입력 위치 y좌표
    }

    // 사진 크기 조정을 위한 함수
    private fun resizeImage(uri: Uri, width: Int, height: Int): Bitmap? {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val originalBitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        // Bitmap 크기 조정
        return originalBitmap?.let {
            Bitmap.createScaledBitmap(it, width, height, true)
        }
    }
}