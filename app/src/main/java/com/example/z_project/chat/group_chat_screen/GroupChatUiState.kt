package com.example.z_project.chat.group_chat_screen

import com.example.z_project.chat.model.Chat
import com.example.z_project.chat.model.GroupChat

data class GroupChatUiState (
    val groupChat: GroupChat? = null,
    val isExit: Boolean = false,
    val chats: List<Chat> = emptyList(),
    val replyChat: Chat? = null

)