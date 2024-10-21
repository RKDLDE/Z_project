package com.example.z_project.chat.group_chat_screen

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel



@Composable
fun GroupChatRoute(
    viewModel: GroupChatViewModel = hiltViewModel(), //GroupChatViewModel 인스턴스로, Hilt를 사용하여 DI(의존성 주입)로 제공됩니다. 기본적으로 hiltViewModel()을 사용하여 주입됩니다
    onNavigateUp: () -> Unit, // 뒤로 가기 동작을 위한 콜백 함수
    onExitGroupChat: (Long) -> Unit // 그룹 채팅을 나갈 때 호출되는 콜백 함수로, 그룹 ID를 매개변수로 받습니다.
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle() // viewModel에서 관리하는 uiState를 구독하고, 상태 변경에 맞춰 컴포저블을 다시 렌더링

    Surface {
        GroupChatScreen(
            uiState = uiState, //그룹 채팅의 UI 상태를 나타내며, 채팅 메시지나 그룹 정보 등이 포함
            onNavigateUp = onNavigateUp, //뒤로 가기 버튼 클릭 시 동작을 정의
            onClickMenu = viewModel::openExitMenu, //채팅방 메뉴(예: 나가기 메뉴)를 열기 위한 이벤트로, viewModel.openExitMenu 함수가 호출
            onSwipeSelect = viewModel::selectReplyChat, //특정 채팅 메시지를 선택할 때(예: 답장용) 동작하며, viewModel.selectReplyChat이 호출
            onClickExit = { onExitGroupChat(uiState.groupChat?.id ?: 0L) }, //채팅방 나가기 버튼 클릭 시 호출되며, onExitGroupChat이 실행됩니다. uiState.groupChat?.id를 가져와서 그룹 채팅의 ID를 넘겨줍니다. ID가 없을 경우 기본값 0L이 전달
            onClickSend = viewModel::sendChat, //채팅 메시지를 전송할 때 viewModel.sendChat 함수가 호출
            onClickRemoveReply = { viewModel.selectReplyChat(null) } //답장 모드를 취소할 때 viewModel.selectReplyChat(null)을 호출하여 선택한 답장을 취소
        )
    }
}