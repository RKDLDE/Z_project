//package com.example.z_project.upload
//
//import androidx.fragment.app.Fragment
//import android.content.Intent
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.emoji2.emojipicker.EmojiPickerView
//import androidx.emoji2.emojipicker.RecentEmojiProviderAdapter
//import com.example.z_project.databinding.ChooseEmojiBinding
//import com.example.z_project.upload.EmojiMainFragment
//import com.example.z_project.upload.EmojiComposeFragment
//
//
//class EmojiComposeFragment : Fragment() {
//    private var _binding: ChooseEmojiBinding? = null
//    private val binding get() = _binding!!
//    private lateinit var emojiPickerView: EmojiPickerView
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        _binding = ChooseEmojiBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // EmojiPickerView를 동적으로 생성하고 레이아웃에 추가
//        emojiPickerView = EmojiPickerView(requireContext()).apply {
//            emojiGridColumns = 15
//            layoutParams = ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                ViewGroup.LayoutParams.MATCH_PARENT
//            )
//        }
//        binding.emojiPickerLayout.addView(emojiPickerView)
//
//        // EmojiPickerView의 리스너 설정
//        emojiPickerView.setOnEmojiPickedListener { emojiViewItem ->
//            binding.editText.append(emojiViewItem.emoji)
//        }
//
//        // ToggleButton 클릭 리스너 설정
//        binding.toggleButton.setOnCheckedChangeListener { _, isChecked ->
//            if (isChecked) {
//                emojiPickerView.emojiGridColumns = 8
//                emojiPickerView.emojiGridRows = 8.3f
//            } else {
//                emojiPickerView.emojiGridColumns = 9
//                emojiPickerView.emojiGridRows = 15f
//            }
//        }
//
//        // Button 클릭 리스너 설정
//        binding.button.setOnClickListener {
//            emojiPickerView.setRecentEmojiProvider(
//                RecentEmojiProviderAdapter(CustomRecentEmojiProvider(requireContext()))
//            )
//        }
//
//        // ToggleButton 클릭 리스너 설정
//        binding.activityButton.setOnCheckedChangeListener { _, isChecked ->
//            val intent = if (isChecked) {
//                Intent(requireContext(), EmojiComposeFragment::class.java)
//            } else {
//                Intent(requireContext(), EmojiMainFragment::class.java)
//            }
//            startActivity(intent)
//        }
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}