package com.example.z_project.chat.model

import com.example.z_project.R
import kotlinx.serialization.Serializable

@Serializable
data class PersonalChat(
    val id: Long = System.currentTimeMillis(),
    val previewMessage: String,
    val chatCount: Int,
    val profile: Profile,
    val chats: List<Chat> = emptyList()
)

fun getDefaultPersonalChats(
    profile: Profile,
    lastMessage: String
) = emptyList<Chat>()

fun getDefaultPersonalChatList() = listOf(
    PersonalChat(
        id = 0,
        previewMessage = "언제쯤 도착함???",
        chatCount = 0,
        profile = Profile(
            profileImageRes = R.drawable.person1,
            name = "냠",
        )
    ),
    PersonalChat(
        id = 1,
        previewMessage = "야 내말이",
        chatCount = 0,
        profile = Profile(
            profileImageRes = R.drawable.person2,
            name = "현",
        )
    ),
    PersonalChat(
        id = 2,
        previewMessage = "언제쯤 도착함??",
        chatCount = 0,
        profile = Profile(
            profileImageRes = R.drawable.person3,
            name = "고도리",
        )
    ),
    PersonalChat(
        id = 3,
        previewMessage = "ㅋㅋㅋㅋㅋㅋ",
        chatCount = 0,
        profile = Profile(
            profileImageRes = R.drawable.person4,
            name = "도금",
        )
    )
)