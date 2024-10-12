package com.example.z_project.chat.invite_screen

import com.example.z_project.R
import com.example.z_project.chat.model.Profile
//import com.example.z_project.chat.model.getDefaultProfileList

data class InviteUiState(
    val members: List<Profile> = getDefaultProfileList(),
    val selectedMap: Map<Int, Boolean> = mapOf()
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
        profileImageRes = R.drawable.person4,
        name = "도금"
    )
)
