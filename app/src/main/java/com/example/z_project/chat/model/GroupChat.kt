package com.example.z_project.chat.model

import androidx.compose.ui.res.painterResource
import com.example.z_project.R
import com.example.z_project.chat.invite_screen.getDefaultProfileList
import kotlinx.serialization.Serializable



@Serializable
data class GroupChat(
    val id: Long = System.currentTimeMillis(),
    val image: Int,
    val title: String,
    val chatCount: Int,
    val member: List<Profile>,
    val chats: List<Chat> = emptyList(),
    val timestamp: Long = System.currentTimeMillis()
)

fun getDefaultGroupChats() = emptyList<Chat>()

fun getDefaultGroupChatList() = emptyList<Chat>()
    // listOf(
//    GroupChat(
//        id = 0,
//        image = R.drawable.baseline_groups_24,
//        title = "그룹1",
//        chatCount = 3,
//        member = getDefaultProfileList()
//    ),
//    GroupChat(
//        id = 1,
//        image = R.drawable.baseline_groups_24,
//        title = "그룹2",
//        chatCount = 1,
//        member = getDefaultProfileList()
//    ),
//    GroupChat(
//        id = 2,
//        image =  R.drawable.baseline_groups_24,
//        title = "그룹3",
//        chatCount = 1,
//        member = getDefaultProfileList()
//    ),
//    GroupChat(
//        id = 3,
//        image = R.drawable.baseline_groups_24,
//        title = "그룹4",
//        chatCount = 0,
//        member = getDefaultProfileList()
//    ),
//)

fun getDefaultGroupChatList() = listOf(
    GroupChat(
        id = 0,
        image = R.drawable.kakaotalk_sharing_btn_medium,
        title = "JSND",
        chatCount = 3,
        member = getDefaultProfileList()
    ),
    GroupChat(
        id = 1,
        image = R.drawable.kakaotalk_sharing_btn_medium,
        title = "그룹2",
        chatCount = 1,
        member = getDefaultProfileList()
    ),
    GroupChat(
        id = 2,
        image = R.drawable.kakaotalk_sharing_btn_medium,
        title = "그룹3",
        chatCount = 1,
        member = getDefaultProfileList()
    ),
    GroupChat(
        id = 3,
        image = R.drawable.logo,
        title = "그룹4",
        chatCount = 0,
        member = getDefaultProfileList()
    ),
)
