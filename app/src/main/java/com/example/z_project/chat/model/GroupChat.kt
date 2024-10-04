package com.example.z_project.chat.model

import com.example.z_project.chat.model.R
import kotlinx.serialization.Serializable

@Serializable
data class GroupChat(
    val id: Long = System.currentTimeMillis(),
    val image: Int,
    val title: String,
    val chatCount: Int,
    val member: List<Profile>,
    val chats: List<Chat> = getDefaultGroupChats(),
    val timestamp: Long = System.currentTimeMillis()
)

fun getDefaultGroupChats() = listOf(
    Chat(
        profile = Profile(
            profileImageRes = R.drawable.person1,
            name = "냠"
        ),
        message = "언제쯤 도착함???",
        imageRes = null,
        isOther = true,
        time = "18:00"
    ),
    Chat(
        profile = Profile(
            profileImageRes = 0,
            name = ""
        ),
        message = "몰라?",
        imageRes = null,
        isOther = false,
        time = "18:05"
    ),
    Chat(
        profile = Profile(
            profileImageRes = R.drawable.person2,
            name = "현"
        ),
        message = "ㅋㅋㅋㅋㅋ 개웃겨",
        imageRes = null,
        isOther = true,
        time = "18:10"
    ),
    Chat(
        profile = Profile(
            profileImageRes = R.drawable.person2,
            name = "현"
        ),
        message = "ㅋㅋㅋㅋㅋ 개웃겨",
        imageRes = null,
        isOther = true,
        time = ""
    ),
    Chat(
        profile = Profile(
            profileImageRes = R.drawable.person2,
            name = "현"
        ),
        message = "ㅋㅋㅋㅋㅋ 개웃겨",
        imageRes = null,
        isOther = true,
        time = ""
    ),
    Chat(
        profile = Profile(
            profileImageRes = R.drawable.person4,
            name = "도금"
        ),
        message = "언제쯤 도착함???",
        imageRes = null,
        isOther = true,
        time = "18:11"
    ),
    Chat(
        profile = Profile(
            profileImageRes = 0,
            name = ""
        ),
        message = "빨리 와",
        imageRes = null,
        isOther = false,
        time = "18:13"
    ),
    Chat(
        profile = Profile(
            profileImageRes = R.drawable.person1,
            name = "냠"
        ),
        message = "시러",
        imageRes = null,
        isOther = true,
        time = "18:14"
    ),
    Chat(
        profile = Profile(
            profileImageRes = 0,
            name = ""
        ),
        message = "^^",
        imageRes = null,
        isOther = false,
        time = "18:16"
    ),
)

fun getDefaultGroupChatList() = listOf(
    GroupChat(
        id = 0,
        image = R.drawable.image,
        title = "JSND",
        chatCount = 3,
        member = getDefaultProfileList()
    ),
    GroupChat(
        id = 1,
        image = R.drawable.group,
        title = "그룹2",
        chatCount = 1,
        member = getDefaultProfileList()
    ),
    GroupChat(
        id = 2,
        image = R.drawable.group,
        title = "그룹3",
        chatCount = 1,
        member = getDefaultProfileList()
    ),
    GroupChat(
        id = 3,
        image = R.drawable.group,
        title = "그룹4",
        chatCount = 0,
        member = getDefaultProfileList()
    ),
)