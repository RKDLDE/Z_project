package com.example.z_project.chat.personal_chat_screen

import com.example.z_project.chat.model.Chat
import com.example.z_project.chat.model.PersonalChat
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

data class PersonalChatUiState(
    val personalChat: PersonalChat? = null,
    val isExit: Boolean = false,
    val chats: List<Chat>? = null,
    val replyChat: Chat? = null,

)

