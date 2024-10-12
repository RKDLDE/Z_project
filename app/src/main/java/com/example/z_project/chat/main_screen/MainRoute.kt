package com.example.z_project.chat.main_screen

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.z_project.chat.model.GroupChat
import com.example.z_project.chat.model.PersonalChat
import com.example.z_project.chat.model.Profile

@Composable
fun MainRoute(
    viewModel: MainViewModel = hiltViewModel(),
    addedMembers: List<Profile>,
    onAdded: () -> Unit, //멤버가 추가되었을 때 호출되는 콜백 함수
    removeChatId: Long,
    isRemoveGroup: Boolean,
    onRemoved: () -> Unit,
    navigateToInviteScreen: () -> Unit, //초대 화면으로 이동하기 위한 콜백 함수
    navigateToGroupChatScreen: (GroupChat) -> Unit, //그룹 채팅 화면으로 이동하기 위한 콜백 함수
    navigateToPersonalChatScreen: (PersonalChat) -> Unit //개인 채팅 화면으로 이동하기 위한 콜백 함수
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (addedMembers.isNotEmpty()) {
        viewModel.addChat(addedMembers)
        onAdded()
    }

    if (removeChatId != -1L) {
        viewModel.removeChat(removeChatId, isRemoveGroup)
        onRemoved()
    }

    Surface {
        MainScreen(
            uiState = uiState,
            onClickChatType = { viewModel.openChatTypeMenu(true) },
            onChangeChatType = { viewModel.changeChatTypeMenu(it) },
            navigateToInviteScreen = navigateToInviteScreen,
            navigateToGroupChatScreen = navigateToGroupChatScreen,
            navigateToPersonalChatScreen = navigateToPersonalChatScreen
        )
    }
}