package com.example.z_project.upload

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.emoji2.emojipicker.EmojiPickerView
import androidx.emoji2.emojipicker.EmojiViewItem
import androidx.fragment.app.Fragment
import com.example.z_project.R
import com.example.z_project.databinding.FragmentEmojiBinding

class ComposeFragment : Fragment() {

    private lateinit var context: Context
    private lateinit var mainLayout: View
    private var _binding: FragmentEmojiBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                EmojiPicker()
            }
        }
    }

    @Composable
    private fun EmojiPicker() {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                val view =
                    LayoutInflater.from(context)
                        .inflate(
                            R.layout.fragment_emoji, /* root= */
                            null, /* attachToRoot= */
                            false
                        )

                val emojiPickerView = view.findViewById<EmojiPickerView>(R.id.emoji_picker)
                emojiPickerView.setOnEmojiPickedListener(this@ComposeFragment::updateEditText)

                view.findViewById<Button>(R.id.sendingBtn).visibility = View.GONE


                view
            },
            update = { view -> mainLayout = view }
        )
    }

    private fun updateEditText(emojiViewItem: EmojiViewItem) {
        val editText = mainLayout.findViewById<EditText>(R.id.edit_text)
        editText.append(emojiViewItem.emoji)

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
