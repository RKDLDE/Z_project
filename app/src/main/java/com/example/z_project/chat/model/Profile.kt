package com.example.z_project.chat.model


import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val profileImageRes: String? = null,
    val name: String? = null,
)

fun getDefaultProfileList() = emptyList<Profile>()