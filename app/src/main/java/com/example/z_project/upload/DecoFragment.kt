package com.example.z_project.upload

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.z_project.R
import com.example.z_project.databinding.FragmentDecoBinding

class DecoFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentDecoBinding
    private var undoStack = mutableListOf<String>()  // 이전 상태 저장
    private var photoUri: Uri? = null
    private val sharedViewModel: SharedViewModel by activityViewModels()  // ViewModel 인스턴스

    private var currentColor: Int = Color.BLACK


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
            Log.d("Deco","${photoUri}")
        }
        // 이전 상태 저장 (글자 입력이나 다른 동작이 이루어지기 전에)
        saveState()

        binding.colorButtonsLayout.visibility = View.GONE
        binding.drawBtn.setOnClickListener{
            binding.photo.enableDrawingMode(true)
            binding.colorButtonsLayout.visibility = View.VISIBLE
            /*drawCanvas = DrawFragment.DrawCanvas(requireContext())
            canvasContainer.addView(drawCanvas)*/
            saveState()
        }
        binding.btnRed.setOnClickListener{
            currentColor = Color.RED
            binding.photo.setColor(currentColor)
            saveState()
        }
        binding.btnGreen.setOnClickListener {
            currentColor = Color.GREEN
            binding.photo.setColor(currentColor)
            saveState()
        }
        binding.btnBlue.setOnClickListener {
            currentColor = Color.BLUE
            binding.photo.setColor(currentColor)
            saveState()
        }
        binding.btnBlack.setOnClickListener {
            currentColor = Color.BLACK
            binding.photo.setColor(currentColor)
            saveState()
        }

        binding.aaBtn.setOnClickListener {
            /*binding.myEditText.isEnabled = true
            binding.myEditText.requestFocus()  // 포커스를 설정하여 바로 입력 가능*/
            binding.photo.enableDrawingMode(false)
            binding.myEditText.visibility = View.VISIBLE // myEditText를 보이게 설정

            val inputText = binding.myEditText.text.toString()

            //binding.photo.setText(inputText, 0f, 0f)  // 텍스트 전달, 터치로 위치 결정
            binding.myEditText.doAfterTextChanged { editable ->
                sharedViewModel.setInputText(editable.toString())
            }

            binding.myEditText.isEnabled = true
            binding.myEditText.requestFocus()
            saveState()
        }

        // 키보드 내리기 위한 Listener 설정
        binding.myEditText.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // 키보드 내리기
                val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                binding.myEditText.isEnabled = false  // 키보드 내린 후 비활성화
                true
            } else {
                false
            }
        }

        // Undo 버튼 클릭 시 이전 상태 복구
        binding.undoButton.setOnClickListener {
            binding.photo.undo()  // undo 호출
        }

        binding.emoji.setOnClickListener{
            val filePath = "${context?.getExternalFilesDir(null)?.absolutePath}/saved_drawing.png"
            binding.photo.saveDrawingToFile(filePath)  // Save drawing to file
            sharedViewModel.imageUri.value = Uri.parse(filePath)  // URI를 ViewModel에 저장

            val emojiFragment = EmojiFragment()
            val fragmentManager = parentFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.main_frm, emojiFragment)
            /*fragmentTransaction.addToBackStack(null)*/
            fragmentTransaction.commit()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnRed -> {
                currentColor = Color.RED
            }
            R.id.btnGreen -> {
                currentColor = Color.GREEN
            }
            R.id.btnBlue -> {
                currentColor = Color.BLUE
            }
            R.id.btnBlack -> {
                currentColor = Color.BLACK
            }
        }
        binding.photo.setColor(currentColor)
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
    private fun resizeImage(uri: Uri, maxWidth: Int, maxHeight: Int): Bitmap? {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val originalBitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        originalBitmap?.let {
            // 이미지의 원래 가로, 세로
            val originalWidth = it.width
            val originalHeight = it.height

            // 비율을 계산
            val aspectRatio = originalWidth.toFloat() / originalHeight.toFloat()

            var newWidth = maxWidth
            var newHeight = maxHeight

            // 비율에 맞춰 크기 조정
            if (originalWidth > originalHeight) {
                newHeight = (newWidth / aspectRatio).toInt()
            } else {
                newWidth = (newHeight * aspectRatio).toInt()
            }

            // Bitmap 크기 조정
            return Bitmap.createScaledBitmap(it, newWidth, newHeight, true)
        }

        return null
    }
}