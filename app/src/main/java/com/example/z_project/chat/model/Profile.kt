package com.example.z_project.chat.model

import com.example.z_project.chat.model.R
import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val profileImageRes: Int,
    val name: String,
)

fun getDefaultProfileList() = listOf(
    Profile(
        profileImageRes = R.drawable.person1,
        name = "냠"
    ),
    Profile(
        profileImageRes = R.drawable.person2,
        name = "현"
    ),
    Profile(
        profileImageRes = R.drawable.person3,
        name = "고도리"
    ),
    Profile(
        profileImageRes = R.drawable.person4,
        name = "도금"
    )
)