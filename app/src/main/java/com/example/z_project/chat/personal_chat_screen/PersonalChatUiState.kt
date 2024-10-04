package com.example.z_project.chat.personal_chat_screen

import com.example.z_project.chat.model.Chat
import com.example.z_project.chat.model.PersonalChat

data class PersonalChatUiState(
    val personalChat: PersonalChat? = null,
    val isExit: Boolean = false,
    val chats: List<Chat> = emptyList()
)
