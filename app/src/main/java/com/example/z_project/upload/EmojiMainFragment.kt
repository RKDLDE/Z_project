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
//import com.example.z_project.MainActivity
//import com.example.z_project.databinding.ChooseEmojiBinding
//
//import android.content.Context
//import androidx.emoji2.emojipicker.RecentEmojiAsyncProvider
//import com.google.common.util.concurrent.Futures
//import com.google.common.util.concurrent.ListenableFuture
//import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.util.concurrent.ListenableFuture
//
//class CustomRecentEmojiProvider(context: Context) : RecentEmojiAsyncProvider {
//
//    companion object {
//        private const val PREF_KEY_CUSTOM_EMOJI_FREQ = "pref_key_custom_emoji_freq"
//        private const val RECENT_EMOJI_LIST_FILE_NAME = "androidx.emoji2.emojipicker.sample.preferences"
//        private const val SPLIT_CHAR = ","
//        private const val KEY_VALUE_DELIMITER = "="
//    }
//
//    private val sharedPreferences =
//        context.getSharedPreferences(RECENT_EMOJI_LIST_FILE_NAME, Context.MODE_PRIVATE)
//
//    private val emoji2Frequency: MutableMap<String, Int> by lazy {
//        sharedPreferences
//            .getString(PREF_KEY_CUSTOM_EMOJI_FREQ, null)
//            ?.split(SPLIT_CHAR)
//            ?.associate { entry ->
//                entry.split(KEY_VALUE_DELIMITER, limit = 2)
//                    .takeIf { it.size == 2 }
//                    ?.let { it[0] to it[1].toInt() } ?: ("" to 0)
//            }
//            ?.toMutableMap() ?: mutableMapOf()
//    }
//
//    override fun getRecentEmojiListAsync(): ListenableFuture<List<String>> =
//        Futures.immediateFuture(
//            emoji2Frequency.toList().sortedByDescending { it.second }.map { it.first }
//        )
//
//    override fun recordSelection(emoji: String) {
//        emoji2Frequency[emoji] = (emoji2Frequency[emoji] ?: 0) + 1
//        saveToPreferences()
//    }
//
//    private fun saveToPreferences() {
//        sharedPreferences
//            .edit()
//            .putString(PREF_KEY_CUSTOM_EMOJI_FREQ, emoji2Frequency.entries.joinToString(SPLIT_CHAR))
//            .commit()
//    }
//}
//
//class EmojiMainFragment : Fragment() {
//    private var _binding: ChooseEmojiBinding? = null
//    private val binding get() = _binding!!
//    private lateinit var emojiPickerView: EmojiPickerView
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // ViewBinding 초기화
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
//        emojiPickerView.setOnEmojiPickedListener { emoji ->
//            binding.editText.append(emoji.emoji)
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
//
//}