package com.example.z_project.chat.main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.z_project.R
import com.example.z_project.chat.model.GroupChat
import com.example.z_project.chat.model.PersonalChat
import com.example.z_project.chat.model.Profile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.text.Typography.dagger


@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel()  {
    // UI State
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainUiState()
    )

    fun openChatTypeMenu(isOpen: Boolean) {
        if (_uiState.value.isOpenMenu != isOpen) {
            _uiState.update { it.copy(isOpenMenu = isOpen) }
        }
    }

    fun changeChatTypeMenu(isPersonal: Boolean) {
        _uiState.update { it.copy(isPersonal = isPersonal) }
        openChatTypeMenu(false)
    }

    fun removeChat(id: Long, isRemoveGroupChat: Boolean) {
        _uiState.update {
            if (isRemoveGroupChat) {
                it.copy(
                    groupChats = it.groupChats.filter { groupChat -> groupChat.id != id }
                )
            } else {
                it.copy(
                    personalChats = it.personalChats.filter { groupChat -> groupChat.id != id }
                )
            }
        }
    }

    fun addChat(members: List<Profile>) {
        if (members.size == 1) {
            val personalChats = _uiState.value.personalChats.toMutableList()
            personalChats.add(
                0,
                PersonalChat(
                    previewMessage = "",
                    chatCount = 0,
                    profile = members[0],
                    chats = emptyList()
                )
            )

            _uiState.update { it.copy(personalChats = personalChats) }
        } else {
            val groupChats = _uiState.value.groupChats.toMutableList()
            groupChats.add(
                0,
                GroupChat(
                    image = R.drawable.group,
                    title = "그룹",
                    chatCount = 0,
                    member = members,
                    chats = emptyList()
                )
            )

            _uiState.update { it.copy(groupChats = groupChats) }
        }
    }
}