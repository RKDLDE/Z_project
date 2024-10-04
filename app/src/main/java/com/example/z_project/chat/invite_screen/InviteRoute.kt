package com.example.z_project.chat.invite_screen

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.z_project.chat.model.Profile


@Composable
fun InviteRoute(
    viewModel: InviteViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit,
    onClickInvite: (List<Profile>) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Surface {
        InviteScreen(
            uiState = uiState,
            onSelected = viewModel::selectMember,
            onNavigateUp = onNavigateUp,
            onClickInvite = { viewModel.inviteMember(onClickInvite) }
        )
    }
}