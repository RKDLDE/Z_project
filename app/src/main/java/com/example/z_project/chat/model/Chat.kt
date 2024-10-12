package com.example.z_project.chat.model

import kotlinx.serialization.Serializable

@Serializable
data class Chat(
    val profile: Profile,
    val message: String,
    val imageRes: Int? = null,
    val isOther: Boolean,
    val time: String,
    val replyChat: Chat? = null
)
