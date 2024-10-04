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
import androidx.fragment.app.viewModels
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.z_project.chat.group_chat_screen.GroupChatRoute
import com.example.z_project.chat.group_chat_screen.GroupChatScreen
import com.example.z_project.chat.invite_screen.InviteScreen
import com.example.z_project.chat.main_screen.MainScreen
import com.example.z_project.chat.personal_chat_screen.PersonalChatScreen
import com.example.z_project.chat.group_chat_screen.GroupChatScreen
import com.example.z_project.chat.invite_screen.InviteRoute
import com.example.z_project.chat.invite_screen.InviteScreen
import com.example.z_project.chat.main_screen.MainRoute
import com.example.z_project.chat.main_screen.MainScreen
import com.example.z_project.chat.model.GroupChat
import com.example.z_project.chat.model.PersonalChat
import com.example.z_project.chat.personal_chat_screen.PersonalChatRoute
import com.example.z_project.chat.personal_chat_screen.PersonalChatScreen
import com.example.z_project.chat.ui.theme.ChatUITheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.json.Json
import kotlin.text.Typography.dagger


@AndroidEntryPoint
class MainFragment : Fragment() {
    private val gViewModel by viewModels<GlobalViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return ComposeView(requireContext()).apply {
            setContent {
                ChatUITheme {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = UiScreen.MainScreen.route
                    ) {
                        composable(UiScreen.MainScreen.route) { backStackEntry ->
                            val members = gViewModel.getMembers()
                            val isRemoveGroupChat = gViewModel.getRemoveGroupChatId() != -1L
                            val removeChatId = if (isRemoveGroupChat) {
                                gViewModel.getRemoveGroupChatId()
                            } else if (gViewModel.getRemovePersonalChatId() != -1L) {
                                gViewModel.getRemovePersonalChatId()
                            } else {
                                -1L
                            }

                            MainRoute(
                                addedMembers = members,
                                onAdded = { gViewModel.setMembers(emptyList()) },
                                navigateToInviteScreen = {
                                    navController.navigate(UiScreen.InviteScreen.route)
                                },
                                removeChatId = removeChatId,
                                isRemoveGroup = isRemoveGroupChat,
                                onRemoved = {
                                    if (isRemoveGroupChat) {
                                        gViewModel.setRemoveGroupChatId(-1L)
                                    } else {
                                        gViewModel.setRemovePersonalChatId(-1L)
                                    }
                                },
                                navigateToGroupChatScreen = {
                                    val groupChatJson = Json.encodeToString(GroupChat.serializer(), it)
                                    navController.navigate(
                                        UiScreen.GroupChatScreen.route + "?groupChat=$groupChatJson"
                                    )
                                },
                                navigateToPersonalChatScreen = {
                                    val personalChatJson =
                                        Json.encodeToString(PersonalChat.serializer(), it)
                                    navController.navigate(
                                        UiScreen.PersonalChatScreen.route + "?personalChat=${personalChatJson}"
                                    )
                                }
                            )
                        }
                        composable(UiScreen.InviteScreen.route) {
                            InviteRoute(
                                onNavigateUp = { navController.navigateUp() },
                                onClickInvite = {
                                    gViewModel.setMembers(it)
                                    navController.popBackStack()
                                }
                            )
                        }
                        composable(
                            UiScreen.GroupChatScreen.route + "?groupChat={groupChat}",
                            arguments = listOf(
                                navArgument("groupChat") { type = NavType.StringType }
                            )
                        ) {
                            GroupChatRoute(
                                onNavigateUp = { navController.navigateUp() },
                                onExitGroupChat = {
                                    gViewModel.setRemoveGroupChatId(it)
                                    navController.navigateUp()
                                }
                            )
                        }
                        composable(
                            UiScreen.PersonalChatScreen.route + "?personalChat={personalChat}",
                            arguments = listOf(
                                navArgument("personalChat") { type = NavType.StringType }
                            )
                        ) {
                            PersonalChatRoute(
                                onNavigateUp = { navController.navigateUp() },
                                onExitPersonalChat = {
                                    gViewModel.setRemovePersonalChatId(it)
                                    navController.navigateUp()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

