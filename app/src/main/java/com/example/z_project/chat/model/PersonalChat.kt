package com.example.z_project.chat.model

import com.example.z_project.R
import kotlinx.serialization.Serializable

@Serializable
data class PersonalChat(
    val id: String? = null,
    val previewMessage: String? = null,
    val chatCount: Int? = null,
    val profile: Profile? = null,
    val chats: List<Chat> = emptyList(),
    val timestamp: Long = 0L
)

fun getDefaultPersonalChats(
    profile: Profile,
    lastMessage: String
) = emptyList<Chat>()

fun getDefaultPersonalChatList() = emptyList<PersonalChat>()