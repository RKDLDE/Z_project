package com.example.z_project.chat.model

import kotlinx.serialization.Serializable

@Serializable
data class Chat(
    val chatRoomId: String? = null,  // 채팅방 ID
    val messageId: String? = null,    // 메시지 ID
    val userId: String? = null,       // 사용자 ID
    val message: String? = null,      // 메시지 내용
    val time: Long? = null,        // 타임스탬프
    val profileImageRes: Int? = null,
    val imageRes: Int? = null, // 추가된 프로필 이미지 리소스 ID
    val replyChat: Chat? = null,
    val profile: Profile? = null,
    val isOther: Boolean = false,
    val read: Boolean = false
)



