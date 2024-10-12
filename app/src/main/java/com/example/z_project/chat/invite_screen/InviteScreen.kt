package com.example.z_project.chat.invite_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.z_project.R
import com.example.z_project.chat.main_screen.MainUiState
import com.example.z_project.chat.model.Profile
import com.example.z_project.chat.ui.theme.ChatUITheme

@Composable
fun InviteScreen(
    uiState: InviteUiState, // uiState 매개변수 추가
    onSelected: (Int, Boolean) -> Unit, // onSelected 매개변수 추가
    onNavigateUp: () -> Unit, // onNavigateUp 매개변수 추가
    onClickInvite: () -> Unit // onClickInvite 매개변수 추가
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(14.dp),
            ) {
                Icon(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .clickable { onNavigateUp() },
                    painter = painterResource(id = R.drawable.ic_baseline_close),
                    contentDescription = "Close"

                )
            }
            InviteMemberList(
                profileList = uiState.members,
                selectedMap = uiState.selectedMap,
                onSelected = onSelected

            )
        }

        InviteButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp),
            onClick = onClickInvite
        )
    }
}

@Composable
fun InviteMemberList(
    modifier: Modifier = Modifier,
    profileList: List<Profile>,
    selectedMap: Map<Int, Boolean>,
    onSelected: (Int, Boolean) -> Unit
) {
    LazyColumn(modifier = modifier.fillMaxWidth()) {
        items(profileList.size) { index ->
            InviteMember(
                profile = profileList[index],
                isSelected = selectedMap.getOrDefault(index, false),
                onSelected = { onSelected(index, it) }
            )
        }
    }
}

@Composable
fun InviteMember(
    modifier: Modifier = Modifier,
    profile: Profile,
    isSelected: Boolean,
    onSelected: (Boolean) -> Unit
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onSelected(!isSelected) }
            .padding(start = 14.dp, end = 14.dp, top = 12.dp, bottom = 12.dp)
    ) {
        val (imageLayout, person, select) = createRefs()

        Image(
            modifier = Modifier
                .constrainAs(imageLayout) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                }
                .size(72.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            painter = painterResource(id = profile.profileImageRes),
            contentDescription = "ProfileImage"
        )
        Text(
            modifier = Modifier.constrainAs(person) {
                top.linkTo(parent.top)
                start.linkTo(imageLayout.end, 16.dp)
                bottom.linkTo(parent.bottom)
            },
            text = profile.name,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Box(
            modifier = Modifier
                .constrainAs(select) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                }
                .size(32.dp)
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    color = Color.Gray,
                    shape = CircleShape
                )
                .then(
                    if (isSelected)
                        Modifier.background(color = Color.Gray, shape = CircleShape)
                    else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_check),
                    contentDescription = "Check",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun InviteButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                color = Color(0xffd9d9d9),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(top = 8.dp, bottom = 8.dp, start = 12.dp, end = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.send_21),
            contentDescription = "Invite"
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "초대하기")
    }
}

@Preview
@Composable
private fun InviteScreenPreview() {
    ChatUITheme {
        InviteScreen(
            uiState = InviteUiState(), //임의추가
            onSelected = { _, _ -> }, //임의추가
            onNavigateUp = {}, //임의추가
            onClickInvite = {}, //임의추가
        )
    }
}
