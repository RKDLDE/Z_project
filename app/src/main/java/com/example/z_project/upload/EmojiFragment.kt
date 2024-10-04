package com.example.z_project.upload

//import androidx.emoji2.bundled.BundledEmojiCompatConfig
//import androidx.emoji2.text.EmojiCompat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
        }



    }

}




/** Define a custom recent emoji provider which shows most frequently used emoji */
//internal class CustomRecentEmojiProvider(context: Context) : RecentEmojiAsyncProvider {
//
//    companion object {
//        private const val PREF_KEY_CUSTOM_EMOJI_FREQ = "pref_key_custom_emoji_freq"
//        private const val RECENT_EMOJI_LIST_FILE_NAME =
//            "androidx.emoji2.emojipicker.sample.preferences"
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
//                entry
//                    .split(KEY_VALUE_DELIMITER, limit = 2)
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

