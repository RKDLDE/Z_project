package com.example.z_project.chatting2
import kotlinx.serialization.Serializable

@Serializable
data class GroupChat(
    val profile: String? = null,
    val name: String? = null,
    val alarmCount: Int? = null,
)

data class PersonalChat(
    val profile: String? = null,
    val name: String? = null,
    val content: String? = null,
    val alarmCount: Int? = null,
)
