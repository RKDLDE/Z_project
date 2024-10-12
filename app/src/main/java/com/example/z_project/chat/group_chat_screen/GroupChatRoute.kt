package com.example.z_project.chat.group_chat_screen

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel



@Composable
fun GroupChatRoute(
    viewModel: GroupChatViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit,
    onExitGroupChat: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Surface {
        GroupChatScreen(
            uiState = uiState,
            onNavigateUp = onNavigateUp,
            onClickMenu = viewModel::openExitMenu,
            onSwipeSelect = viewModel::selectReplyChat,
            onClickExit = { onExitGroupChat(uiState.groupChat?.id ?: 0L) },
            onClickSend = viewModel::sendChat,
            onClickRemoveReply = { viewModel.selectReplyChat(null) }
        )
    }
}