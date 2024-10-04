package com.example.z_project.chat.model

import com.example.z_project.chat.model.R
import kotlinx.serialization.Serializable

@Serializable
data class PersonalChat(
    val id: Long = System.currentTimeMillis(),
    val previewMessage: String,
    val chatCount: Int,
    val profile: Profile,
    val chats: List<Chat> = getDefaultPersonalChats(profile, previewMessage)
)

fun getDefaultPersonalChats(
    profile: Profile,
    lastMessage: String
) = listOf(
    Chat(
        profile = profile,
        message = "언제쯤 도착함???",
        imageRes = null,
        isOther = true,
        time = "18:00"
    ),
    Chat(
        profile = Profile(0, ""),
        message = "몰라?",
        imageRes = null,
        isOther = false,
        time = "18:05"
    ),
    Chat(
        profile = profile,
        message = "",
        imageRes = R.drawable.image,
        isOther = true,
        time = ""
    ),
    Chat(
        profile = profile,
        message = "ㅋㅋㅋㅋㅋ 개웃겨",
        imageRes = null,
        isOther = true,
        time = "18:10"
    ),
    Chat(
        profile = profile,
        message = "ㅋㅋㅋㅋㅋ 개웃겨",
        imageRes = null,
        isOther = true,
        time = ""
    ),
    Chat(
        profile = profile,
        message = "ㅋㅋㅋㅋㅋ 개웃겨",
        imageRes = null,
        isOther = true,
        time = ""
    ),
    Chat(
        profile = profile,
        message = lastMessage,
        imageRes = null,
        isOther = true,
        time = "18:11"
    ),
)

fun getDefaultPersonalChatList() = listOf(
    PersonalChat(
        id = 0,
        previewMessage = "언제쯤 도착함???",
        chatCount = 1,
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
        chatCount = 4,
        profile = Profile(
            profileImageRes = R.drawable.person4,
            name = "도금",
        )
    )
)