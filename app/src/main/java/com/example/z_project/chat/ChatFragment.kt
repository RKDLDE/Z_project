package com.example.z_project.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.z_project.chat.group_chat_screen.GroupChatScreen
import com.example.z_project.chat.invite_screen.InviteScreen
import com.example.z_project.chat.main_screen.MainScreen
import com.example.z_project.chat.personal_chat_screen.PersonalChatScreen
import com.example.z_project.chat.group_chat_screen.GroupChatScreen
import com.example.z_project.chat.invite_screen.InviteScreen
import com.example.z_project.chat.main_screen.MainScreen
import com.example.z_project.chat.personal_chat_screen.PersonalChatScreen
import com.example.z_project.chat.ui.theme.ChatUITheme


class ChatFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // ComposeView를 반환합니다.
        return ComposeView(requireContext()).apply {
            this.setContent {
                ChatUITheme {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = UiScreen.MainScreen.route
                    ) {
                        composable(UiScreen.MainScreen.route) {
                            MainScreen(
                                navigateToInviteScreen = {
                                    navController.navigate(UiScreen.InviteScreen.route)
                                },
                                navigateToGroupChatScreen = {
                                    navController.navigate(UiScreen.GroupChatScreen.route)
                                },
                                navigateToPersonalChatScreen = {
                                    navController.navigate(UiScreen.PersonalChatScreen.route)
                                }
                            )
                        }
                        composable(UiScreen.InviteScreen.route) {
                            InviteScreen()
                        }
                        composable(UiScreen.GroupChatScreen.route) {
                            GroupChatScreen()
                        }
                        composable(UiScreen.PersonalChatScreen.route) {
                            PersonalChatScreen()
                        }
                    }
                }
            }
        }
    }
}


