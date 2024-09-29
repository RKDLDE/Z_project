package com.example.z_project.upload

//import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.util.concurrent.Futures
//import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.util.concurrent.ListenableFuture
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
//import androidx.emoji2.bundled.BundledEmojiCompatConfig
import androidx.emoji2.emojipicker.RecentEmojiAsyncProvider
import androidx.emoji2.emojipicker.RecentEmojiProviderAdapter
//import androidx.emoji2.text.EmojiCompat
import androidx.fragment.app.Fragment
import com.example.z_project.databinding.FragmentEmojiBinding
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // EmojiCompat 초기화
//        val config = BundledEmojiCompatConfig(this)
//            .setReplaceAll(true)  // 지원되지 않는 이모티콘을 대체 텍스트로 교체
//        EmojiCompat.init(config)
    }
}

class EmojiFragment : Fragment()  {

    private var _binding: FragmentEmojiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmojiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emojiPickerView = binding.emojiPicker
        val editText = binding.editText
        /*val toggleButton = binding.toggleButton*/
        /*val activityButton = binding.activityButton*/
        val button = binding.sendingBtn

        // 이모지 선택 리스너 설정
        emojiPickerView.setOnEmojiPickedListener {
            editText.append(it.emoji)
        }

        // 그리드 레이아웃 변경 처리
//        toggleButton.setOnCheckedChangeListener { _, isChecked ->
//            if (isChecked) {
//                emojiPickerView.emojiGridColumns = 8
//                emojiPickerView.emojiGridRows = 8.3f
//            } else {
//                emojiPickerView.emojiGridColumns = 9
//                emojiPickerView.emojiGridRows = 15f
//            }
//        }

        // 최근 이모지 제공자 설정
        button.setOnClickListener {
            emojiPickerView.setRecentEmojiProvider(
                RecentEmojiProviderAdapter(CustomRecentEmojiProvider(requireContext()))
            )
        }

        // 액티비티 전환 처리
//        activityButton.setOnCheckedChangeListener { _, isChecked ->
//            val intent = if (isChecked) {
//                Intent(requireContext(), ComposeFragment::class.java)
//            } else {
//                Intent(requireContext(), EmojiFragment::class.java)
//            }
//            startActivity(intent)
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

/** Define a custom recent emoji provider which shows most frequently used emoji */
internal class CustomRecentEmojiProvider(context: Context) : RecentEmojiAsyncProvider {

    companion object {
        private const val PREF_KEY_CUSTOM_EMOJI_FREQ = "pref_key_custom_emoji_freq"
        private const val RECENT_EMOJI_LIST_FILE_NAME =
            "androidx.emoji2.emojipicker.sample.preferences"
        private const val SPLIT_CHAR = ","
        private const val KEY_VALUE_DELIMITER = "="
    }

    private val sharedPreferences =
        context.getSharedPreferences(RECENT_EMOJI_LIST_FILE_NAME, Context.MODE_PRIVATE)

    private val emoji2Frequency: MutableMap<String, Int> by lazy {
        sharedPreferences
            .getString(PREF_KEY_CUSTOM_EMOJI_FREQ, null)
            ?.split(SPLIT_CHAR)
            ?.associate { entry ->
                entry
                    .split(KEY_VALUE_DELIMITER, limit = 2)
                    .takeIf { it.size == 2 }
                    ?.let { it[0] to it[1].toInt() } ?: ("" to 0)
            }
            ?.toMutableMap() ?: mutableMapOf()
    }

    override fun getRecentEmojiListAsync(): ListenableFuture<List<String>> =
        Futures.immediateFuture(
            emoji2Frequency.toList().sortedByDescending { it.second }.map { it.first }
        )

    override fun recordSelection(emoji: String) {
        emoji2Frequency[emoji] = (emoji2Frequency[emoji] ?: 0) + 1
        saveToPreferences()
    }

    private fun saveToPreferences() {
        sharedPreferences
            .edit()
            .putString(PREF_KEY_CUSTOM_EMOJI_FREQ, emoji2Frequency.entries.joinToString(SPLIT_CHAR))
            .commit()
    }
}

