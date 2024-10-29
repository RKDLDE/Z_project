package com.example.z_project.chat

sealed class UiScreen(val route: String) {
    data object MainScreen : UiScreen("MainScreen")

    data object GroupChatScreen : UiScreen("ChatScreen")
    data object PersonalChatScreen : UiScreen("PersonalChatScreen")
}