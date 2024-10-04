package com.example.z_project.chat.invite_screen

import com.example.z_project.chat.model.Profile
import com.example.z_project.chat.model.getDefaultProfileList

data class InviteUiState(
    val members: List<Profile> = getDefaultProfileList(),
    val selectedMap: Map<Int, Boolean> = mapOf()
)
