package com.example.z_project.chat.invite_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.z_project.chat.model.Profile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.text.Typography.dagger


@HiltViewModel
class InviteViewModel @Inject constructor() : ViewModel() {
    // UI State
    private val _uiState = MutableStateFlow(InviteUiState())
    val uiState = _uiState.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = InviteUiState()
    )

    fun selectMember(index: Int, isSelected: Boolean) {
        val selectedMap = _uiState.value.selectedMap.toMutableMap()
        selectedMap[index] = isSelected

        _uiState.value = _uiState.value.copy(selectedMap = selectedMap)
    }

    fun inviteMember(onInvite: (List<Profile>) -> Unit) {
        val filtered = _uiState.value.selectedMap.filter { it.value }
        if (filtered.isNotEmpty()) {
            onInvite(filtered.keys.map { _uiState.value.members[it] })
        }
    }
}