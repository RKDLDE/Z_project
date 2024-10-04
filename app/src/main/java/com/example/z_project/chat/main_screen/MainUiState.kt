package com.example.z_project.chat.main_screen

import com.example.z_project.chat.model.GroupChat
import com.example.z_project.chat.model.PersonalChat
import com.example.z_project.chat.model.getDefaultGroupChatList
import com.example.z_project.chat.model.getDefaultPersonalChatList

data class MainUiState(
    val isOpenMenu: Boolean = false,
    val isPersonal: Boolean = false,
    val personalChats: List<PersonalChat> = getDefaultPersonalChatList(),
    val groupChats: List<GroupChat> = getDefaultGroupChatList()

)