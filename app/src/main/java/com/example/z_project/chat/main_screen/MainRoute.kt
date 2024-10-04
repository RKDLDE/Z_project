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
    onAdded: () -> Unit,
    removeChatId: Long,
    isRemoveGroup: Boolean,
    onRemoved: () -> Unit,
    navigateToInviteScreen: () -> Unit,
    navigateToGroupChatScreen: (GroupChat) -> Unit,
    navigateToPersonalChatScreen: (PersonalChat) -> Unit
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