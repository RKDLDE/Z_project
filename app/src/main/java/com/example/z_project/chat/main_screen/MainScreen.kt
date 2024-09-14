package com.example.z_project.chat.main_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.z_project.R
import com.example.z_project.chat.ui.theme.ChatUITheme

@Composable
fun MainScreen(
    navigateToInviteScreen: () -> Unit,
    navigateToGroupChatScreen: () -> Unit,
    navigateToPersonalChatScreen: () -> Unit
) {
    var isOpen by remember { mutableStateOf(false) }
    var isPersonal by remember { mutableStateOf(false) }
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (changeType, inviteBtn, chatRoom, bottomBar) = createRefs()
        ChangeType(
            modifier = Modifier.constrainAs(changeType) {
                start.linkTo(parent.start)
                top.linkTo(parent.top, 14.dp)
                end.linkTo(parent.end)
            },
            isOpen = isOpen,
            isPersonal = isPersonal,
            onClick = { isOpen = !isOpen },
            onClickType = {
                isPersonal = it
                isOpen = false
            }
        )
        Icon(
            modifier = Modifier
                .constrainAs(inviteBtn) {
                    top.linkTo(parent.top, 14.dp)
                    end.linkTo(parent.end, 14.dp)
                }
                .size(32.dp)
                .clickable { navigateToInviteScreen() },
            painter = painterResource(id = R.drawable.ic_baseline_group_add),
            contentDescription = "GroupAdd"
        )

        Column(
            modifier = Modifier
                .constrainAs(chatRoom) {
                    top.linkTo(changeType.bottom, 14.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(bottomBar.top)

                    height = Dimension.fillToConstraints
                    width = Dimension.fillToConstraints
                }
        ) {

            Column {
                if (isPersonal) {
                    val list = listOf(
                        ChatRoom(
                            image = R.drawable.person1,
                            title = "냠",
                            previewMessage = "언제쯤 도착함???",
                            chatCount = 1
                        ),
                        ChatRoom(
                            image = R.drawable.person2,
                            title = "현",
                            previewMessage = "아 내말이",
                            chatCount = 0
                        ),
                        ChatRoom(
                            image = R.drawable.person3,
                            title = "고도리",
                            previewMessage = "언제쯤 도착함???",
                            chatCount = 0
                        ),
                        ChatRoom(
                            image = R.drawable.person4,
                            title = "도금",
                            previewMessage = "ㅋㅋㅋㅋㅋㅋ",
                            chatCount = 0
                        )
                    )
                    PersonalChatList(
                        chats = list,
                        itemClick = {
                            navigateToPersonalChatScreen()
                        }
                    )
                } else {
                    val list = listOf(
                        ChatRoom(
                            image = R.drawable.image,
                            title = "JSND",
                            chatCount = 3
                        ),
                        ChatRoom(
                            image = R.drawable.group,
                            title = "그룹2",
                            chatCount = 1
                        ),
                        ChatRoom(
                            image = R.drawable.group,
                            title = "그룹3",
                            chatCount = 1
                        ),
                        ChatRoom(
                            image = R.drawable.group,
                            title = "그룹4",
                            chatCount = 0
                        )
                    )
                    GroupChatList(
                        chats = list,
                        itemClick = {
                            navigateToGroupChatScreen()
                        }
                    )
                }
            }
        }
        BottomBar(
            modifier = Modifier.constrainAs(bottomBar) {
                bottom.linkTo(parent.bottom)
            }
        )
    }
}

@Composable
fun ChangeType(
    modifier: Modifier = Modifier,
    isOpen: Boolean,
    isPersonal: Boolean,
    onClick: () -> Unit,
    onClickType: (Boolean) -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                color = Color(0xffd9d9d9),
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(top = 8.dp, bottom = 8.dp, start = 12.dp, end = 12.dp),
        horizontalAlignment = Alignment.End
    ) {
        Row(
            modifier = if (isOpen) {
                Modifier.clickable {
                    onClickType(isPersonal)
                }
            } else {
                Modifier
            },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .size(24.dp)
                    .rotate(if (isOpen) 180f else 0f),
                painter = painterResource(id = R.drawable.ic_arrow_down),
                contentDescription = "More"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = if (isPersonal) "개인" else "그룹")
        }
        if (isOpen) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.clickable {
                    onClickType(!isPersonal)
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(32.dp))
                Text(text = if (!isPersonal) "개인" else "그룹")
            }
        }
    }
}

@Composable
fun GroupChatList(
    modifier: Modifier = Modifier,
    chats: List<ChatRoom>,
    itemClick: () -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(12.dp)
    ) {
        items(chats.size) { index ->
            GroupChatItem(
                chatGroup = chats[index],
                onClick = itemClick
            )
        }
    }
}

@Composable
fun GroupChatItem(
    modifier: Modifier = Modifier,
    chatGroup: ChatRoom,
    onClick: () -> Unit
) {
    val screenWidth = (LocalConfiguration.current.screenWidthDp / 2) - 52

    Column(
        modifier = modifier
            .width(screenWidth.dp)
            .clickable {
                onClick()
            }
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier) {
            Image(
                modifier = Modifier
                    .size(screenWidth.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .border(
                        width = 3.dp,
                        color = Color(0xffe2e2e2),
                        shape = RoundedCornerShape(24.dp)
                    ),
                painter = painterResource(id = chatGroup.image),
                contentDescription = "이미지",
                contentScale = ContentScale.Crop
            )
            ChatCount(
                modifier = Modifier.align(Alignment.TopEnd),
                chatCount = chatGroup.chatCount
            )
        }
        Text(text = chatGroup.title)
    }
}

@Composable
fun PersonalChatList(
    modifier: Modifier = Modifier,
    chats: List<ChatRoom>,
    itemClick: () -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {
        items(chats.size) { index ->
            PersonalChatItem(
                chatRoom = chats[index], onClick = itemClick
            )
            if (index < chats.size - 1)
                HorizontalDivider()
        }
    }
}

@Composable
fun PersonalChatItem(
    modifier: Modifier = Modifier,
    chatRoom: ChatRoom,
    onClick: () -> Unit
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
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
            painter = painterResource(id = chatRoom.image),
            contentDescription = "ProfileImage"
        )
        Text(
            modifier = Modifier.constrainAs(person) {
                top.linkTo(parent.top)
                start.linkTo(image.end, 16.dp)
            },
            text = chatRoom.title,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        )
        Text(
            modifier = Modifier.constrainAs(message) {
                top.linkTo(person.bottom)
                start.linkTo(image.end, 16.dp)
            },
            text = chatRoom.previewMessage,
        )
        ChatCount(
            modifier = Modifier.constrainAs(count) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            },
            chatCount = chatRoom.chatCount
        )
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
            Icon(
                modifier = Modifier
                    .size(32.dp)
                    .fillMaxWidth(0.25f)
                    .alpha(0.4f),
                painter = painterResource(id = R.drawable.ic_upload),
                contentDescription = "BottomBarIcon"
            )
        }
        Box(
            modifier = Modifier.weight(0.25f),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier
                    .size(32.dp)
                    .fillMaxWidth(0.25f)
                    .alpha(0.4f),
                painter = painterResource(id = R.drawable.ic_inventory),
                contentDescription = "BottomBarIcon"
            )
        }
        Box(
            modifier = Modifier.weight(0.25f),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier
                    .size(32.dp)
                    .fillMaxWidth(0.25f),
                painter = painterResource(id = R.drawable.ic_chat),
                contentDescription = "BottomBarIcon"
            )
        }
        Box(
            modifier = Modifier.weight(0.25f),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier
                    .size(32.dp)
                    .fillMaxWidth(0.25f)
                    .alpha(0.4f),
                painter = painterResource(id = R.drawable.ic_smile),
                contentDescription = "BottomBarIcon"
            )
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
            navigateToInviteScreen = {},
            navigateToGroupChatScreen = {},
            navigateToPersonalChatScreen = {}
        )
    }
}

