package com.example.z_project.chat.model

import kotlinx.serialization.Serializable

@Serializable
data class ReplyChat(
    val profile: Profile?= null,  // 프로필 정보
    val message: String?= null
)

