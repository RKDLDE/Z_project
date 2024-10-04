package com.example.z_project.chat.personal_chat_screen

import androidx.lifecycle.SavedStateHandle
import androidx.emoji2.text.EmojiCompat.init
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.z_project.chat.model.Chat
import com.example.z_project.chat.model.PersonalChat
import com.example.z_project.chat.model.Profile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json
import javax.inject.Inject
import kotlin.text.Typography.dagger


@HiltViewModel
class PersonalChatViewModel @Inject constructor {
    savedStateHandle: SavedStateHandle
}: ViewModel() {
    // UI State
    private val _uiState = MutableStateFlow(PersonalChatUiState())
    val uiState = _uiState.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PersonalChatUiState()
    )

    init {
        val personalChatJson = savedStateHandle.get<String>("personalChat")
        if (personalChatJson != null) {
            val personalChat = Json.decodeFromString(PersonalChat.serializer(), personalChatJson)
            _uiState.update {
                it.copy(
                    personalChat = personalChat,
                    chats = personalChat.chats
                )
            }
        }
    }

    fun openExitMenu(isExit: Boolean) {
        _uiState.update {
            it.copy(isExit = isExit)
        }
    }

    fun sendChat(message: String) {
        _uiState.update {
            it.copy(
                chats = it.chats.plus(
                    Chat(
                        profile = Profile(
                            profileImageRes = 0,
                            name = ""
                        ),
                        message = message,
                        imageRes = null,
                        isOther = false,
                        time = "18:20"
                    )
                )
            )
        }
    }
}