package com.example.z_project.chat.main_screen

import com.example.z_project.chat.model.Chat

import com.example.z_project.chat.model.PersonalChat

import com.example.z_project.chat.model.getDefaultPersonalChatList
import com.example.z_project.chatting2.GroupChat

data class MainUiState(
    val isOpenMenu: Boolean = false,
    val isPersonal: Boolean = false,
    val personalChats: List<PersonalChat> = getDefaultPersonalChatList()
)
