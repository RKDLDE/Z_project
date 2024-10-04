package com.example.z_project.chat.group_chat_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.z_project.chat.model.Chat
import com.example.z_project.chat.model.GroupChat
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
class GroupChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    // UI State
    private val _uiState = MutableStateFlow(GroupChatUiState())
    val uiState = _uiState.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = GroupChatUiState()
    )

    init {
        val groupChatJson = savedStateHandle.get<String>("groupChat")
        if (groupChatJson != null) {
            val groupChat = Json.decodeFromString<GroupChat>(groupChatJson)
            _uiState.update {
                it.copy(
                    groupChat = groupChat,
                    chats = groupChat.chats
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