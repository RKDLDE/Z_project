package com.example.z_project.chat.main_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.compose.rememberAsyncImagePainter
import com.example.z_project.R
import com.example.z_project.chat.model.PersonalChat
import com.example.z_project.chat.ui.theme.ChatUITheme

@Composable
fun MainScreen(
    uiState: MainUiState,
    navigateToPersonalChatScreen: (PersonalChat) -> Unit
) {
    // 처음 렌더링될 때 친구 목록을 가져옵니다.
    Box(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (chatRoom, bottomBar) = createRefs()

            Column(
                modifier = Modifier
                    .constrainAs(chatRoom) {
                        top.linkTo(parent.top, 14.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(bottomBar.top)

                        height = Dimension.fillToConstraints
                        width = Dimension.fillToConstraints
                    }
            ) {
                Column {
                    PersonalChatList(
                        chats = uiState.personalChats,
                        itemClick = { navigateToPersonalChatScreen(it) }
                    )
                }
            }
            BottomBar(
               modifier = Modifier.constrainAs(bottomBar) {
                    bottom.linkTo(parent.bottom)
               }
            )
        }
    }
}

@Composable
fun PersonalChatList(
    modifier: Modifier = Modifier,
    chats: List<PersonalChat>,
    itemClick: (PersonalChat) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {
        items(chats.size) { index ->
            PersonalChatItem(
                chatRoom = chats[index],
                onClick = itemClick
            )
            if (index < chats.size - 1)
                HorizontalDivider()
        }
    }
}

@Composable
fun PersonalChatItem(
    modifier: Modifier = Modifier,
    chatRoom: PersonalChat,
    onClick: (PersonalChat) -> Unit
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(chatRoom) }
            .padding(start = 14.dp, end = 14.dp, top = 12.dp, bottom = 12.dp)
    ) {
        val (image, person, message, count) = createRefs()

        Image(
            modifier = Modifier
                .constrainAs(image) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                }
                .size(68.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop,
            painter = rememberAsyncImagePainter(model = chatRoom.profile?.profileImageRes),
            contentDescription = "ProfileImage"
        )
        chatRoom.profile?.name?.let {
            Text(
                modifier = Modifier.constrainAs(person) {
                    top.linkTo(parent.top)
                    start.linkTo(image.end, 16.dp)
                },
                text = it,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
        chatRoom.previewMessage?.let {
            Text(
                modifier = Modifier.constrainAs(message) {
                    top.linkTo(person.bottom)
                    start.linkTo(image.end, 16.dp)
                },
                text = it,
            )
        }
        chatRoom.chatCount?.let {
            ChatCount(
                modifier = Modifier.constrainAs(count) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                },
                chatCount = it
            )
        }
    }
}

@Composable
fun BottomBar(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.weight(0.25f),
            contentAlignment = Alignment.Center
        ) {

        }
        Box(
            modifier = Modifier.weight(0.25f),
            contentAlignment = Alignment.Center
        ) {

        }
        Box(
            modifier = Modifier.weight(0.25f),
            contentAlignment = Alignment.Center
        ){

        }
        Box(
            modifier = Modifier.weight(0.25f),
            contentAlignment = Alignment.Center
        ) {

        }
    }
}

@Composable
fun ChatCount(
    modifier: Modifier = Modifier,
    chatCount: Int
) {
    if (chatCount > 0) {
        Box(
            modifier = modifier
                .background(
                    color = Color.Red,
                    shape = CircleShape
                )
                .size(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = chatCount.toString(),
                color = Color.White
            )
        }
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    ChatUITheme {
        MainScreen(
            uiState = MainUiState(),
            navigateToPersonalChatScreen = {}
        )
    }
}
