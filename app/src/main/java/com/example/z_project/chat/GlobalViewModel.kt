package com.example.z_project.chat

import androidx.lifecycle.ViewModel
import com.example.z_project.chat.model.Profile
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlin.text.Typography.dagger


@HiltViewModel
class GlobalViewModel @Inject constructor() : ViewModel() {
    private var _addedMembers = mutableListOf<Profile>()
    private var removeGroupChatId = -1L
    private var removePersonalChatId = -1L

    fun setMembers(members: List<Profile>) {
        _addedMembers = members.toMutableList()
    }

    fun setRemoveGroupChatId(id: Long) {
        removeGroupChatId = id
    }

    fun setRemovePersonalChatId(id: Long) {
        removePersonalChatId = id
    }

    fun getMembers() = _addedMembers.toList()

    fun getRemoveGroupChatId() = removeGroupChatId

    fun getRemovePersonalChatId() = removePersonalChatId

}