package com.example.z_project.chat.personal_chat_screen

import android.content.Context
import android.util.Log
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.z_project.chat.main_screen.DisposableEffectWithLifeCycle

@Composable
fun PersonalChatRoute(
    viewModel: PersonalChatViewModel = hiltViewModel(), // Hilt를 사용하여 PersonalChatViewModel을 주입
    onNavigateUp: () -> Unit, // 상위 화면으로 돌아가는 콜백 함수
    onExitPersonalChat: (Long) -> Unit, // 채팅 종료 시 호출되는 콜백 함수, 채팅방 ID를 매개변수로 받음
    context: Context // 애플리케이션의 컨텍스트 객체
) {
    // viewModel의 uiState 값을 감시하며 최신 상태를 수집
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // SharedPreferences에서 사용자 ID 가져오기
    val sharedPreferences = context.getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
    val userId = sharedPreferences.getString("UNIQUE_CODE", null) ?: ""

    DisposableEffectWithLifeCycle {
        viewModel.removeListener()
    }

    Surface(color = Color.White) {
        PersonalChatScreen(
            uiState = uiState, // PersonalChatScreen에 전달되는 UI 상태,
            userId = userId,
            onNavigateUp = onNavigateUp, // 상위 화면으로 이동하기 위한 콜백 함수
            onClickMenu = viewModel::openExitMenu,  // 메뉴 열기 콜백, exit 메뉴를 열기 위한 ViewModel 함수 참조
            onSwipeSelect = viewModel::selectReplyChat, // 채팅 메시지를 선택했을 때의 콜백 함수, 응답할 메시지를 선택
            onClickExit = {
                onExitPersonalChat(
                    uiState.personalChat?.id?.toLong() ?: 0L
                )
            }, // 채팅 종료 콜백, ID가 null인 경우 0L로 대체
            onClickSend = { message ->  // message 매개변수를 val로 선언
                if (message.isNotBlank()) { // 메시지가 비어있지 않을 때만 처리
                    viewModel.sendMessage(
                        message,
                        message
                    ) // userId와 메시지를 사용해 ViewModel의 sendMessage 호출
                } else {
                    // 메시지가 비어있을 경우 처리할 로직
                    Log.e("PersonalChatRoute", "Cannot send an empty message")
                }
            },
            onClickRemoveReply = { viewModel.selectReplyChat(null) } // 응답 메시지를 제거하는 콜백, null을 전달해 응답 메시지를 초기화
        )

    }
}