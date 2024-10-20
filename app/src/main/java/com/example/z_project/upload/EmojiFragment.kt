package com.example.z_project.upload

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.z_project.R
import com.example.z_project.databinding.FragmentEmojiBinding


class EmojiFragment : Fragment()  {

    private lateinit var binding: FragmentEmojiBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding = FragmentEmojiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val emojiPickerView = binding.emojiPicker
        val textView = binding.textView
        val button = binding.sendingBtn

        // 이모지 선택 리스너 설정
        emojiPickerView.setOnEmojiPickedListener {
            textView.text = it.emoji
            // 이모지 객체의 정보를 로그에 출력
            Log.d("EmojiSelected", "Selected emoji: $it")
        }

        binding.sendingBtn.setOnClickListener{
            val finalFragment = FinalFragment()

            // 이모티콘 데이터를 번들로 전달
            val bundle = Bundle()
            bundle.putString("emoji", textView.text.toString()) // 선택한 이모티콘
            finalFragment.arguments = bundle
            Log.d("Emoji", "Selected emoji: {$textView.text.toString()}")

            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.main_frm, finalFragment)
            fragmentTransaction.addToBackStack(null)  // 백스택에 추가하여 뒤로 가기 가능하게 함
            fragmentTransaction.commit()
        }
    }


}