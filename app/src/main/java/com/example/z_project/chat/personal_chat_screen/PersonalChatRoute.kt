package com.example.z_project.chat.personal_chat_screen

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun PersonalChatRoute(
    viewModel: PersonalChatViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit,
    onExitPersonalChat: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Surface {
        PersonalChatScreen(
            uiState = uiState,
            onNavigateUp = onNavigateUp,
            onClickMenu = viewModel::openExitMenu,
            onSwipeSelect = viewModel::selectReplyChat,
            onClickExit = { onExitPersonalChat(uiState.personalChat?.id ?: 0L) },
            onClickSend = viewModel::sendChat,
            onClickRemoveReply = { viewModel.selectReplyChat(null) }

        )
    }
}