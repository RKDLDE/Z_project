package com.example.z_project.chat.main_screen

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.z_project.chat.model.PersonalChat
import com.example.z_project.chat.model.Profile

@Composable
fun MainRoute(
    viewModel: MainViewModel = hiltViewModel(),
    navigateToPersonalChatScreen: (PersonalChat) -> Unit //개인 채팅 화면으로 이동하기 위한 콜백 함수
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

//    LaunchedEffect(Unit) {
//        viewModel.fetchAndSetPersonalChats() // 친구 목록 불러오기 (개인채팅)
//    }

    Surface(color = Color.White) {
        MainScreen(
            uiState = uiState,
            navigateToPersonalChatScreen = navigateToPersonalChatScreen
        )
    }
}