package com.example.z_project.chat

import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.z_project.chat.main_screen.MainRoute
import com.example.z_project.chat.model.PersonalChat
import com.example.z_project.chat.personal_chat_screen.PersonalChatRoute
import com.example.z_project.chat.ui.theme.ChatUITheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.json.Json
import java.net.URLEncoder

@AndroidEntryPoint
class ChatFragment : Fragment() {
    private val gViewModel by viewModels<GlobalViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                ChatUITheme {
                    val navController = rememberNavController()

                    // 입력 필드와 버튼을 위한 상태를 정의합니다.
                    var message by remember { mutableStateOf("") }
                    NavHost(
                        navController = navController,
                        startDestination = UiScreen.MainScreen.route
                    ) {
                        composable(UiScreen.MainScreen.route) { backStackEntry ->
                            MainRoute(
                                navigateToPersonalChatScreen = {
                                    val personalChatJson =
                                        Json.encodeToString(PersonalChat.serializer(), it)
                                    val base64EncodedJson = Base64.encodeToString(personalChatJson.toByteArray(), Base64.NO_WRAP)
//                                    val encodedJson = URLEncoder.encode(personalChatJson, "UTF-8")  // URL-safe encoding

                                    navController.navigate(
                                        UiScreen.PersonalChatScreen.route + "?personalChat=${base64EncodedJson}"
                                    )
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
                                },
                                context = requireContext()
                            )
                        }
                    }
                }
            }
        }
    }
}
//                    // 메시지 입력 필드 및 전송 버튼 추가
//                    Column {
//                        TextField(
//                            value = message,
//                            onValueChange = { message = it },
//                            label = { Text("메시지를 입력하세요") },
//                            modifier = Modifier.fillMaxWidth()
//                        )
//                        Button(
//                            onClick = {
//                                // 실제 사용자 ID와 채팅 방 ID를 가져 와야 함
//                                val userId = "exampleUserId" // 로그인 한 사용자의 ID
//                                val chatRoomId = "exampleChatRoomId" // 현재 채팅 방 ID
//
//                                // handleUserChat 함수 호출
//                                handleUserChat(userId, chatRoomId, message)
//
//                                // 메시지 입력 필드 초기화
//                                message = ""
//                            },
//                            modifier = Modifier.align(Alignment.End)
//                        ) {
//                            Text("전송")
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private fun Button(onClick: () -> Unit, modifier: Modifier, function: @Composable () -> Unit): Button {
//
//        return TODO("Provide the return value")
//    }
//}

