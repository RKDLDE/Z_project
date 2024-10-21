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
import com.example.z_project.chat.model.GroupChat
import com.example.z_project.chat.model.PersonalChat
import com.example.z_project.chat.ui.theme.ChatUITheme

@Composable
fun MainScreen(
    uiState: MainUiState,
    onClickChatType: () -> Unit,
    onChangeChatType: (Boolean) -> Unit,
    navigateToInviteScreen: () -> Unit,
    navigateToGroupChatScreen: (GroupChat) -> Unit,
    navigateToPersonalChatScreen: (PersonalChat) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (topBar, chatRoom, bottomBar) = createRefs()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .constrainAs(topBar) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(start = 14.dp, end = 14.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(32.dp)
                        .clickable { navigateToInviteScreen() },
                    painter = painterResource(id = R.drawable.ic_baseline_group_add),
                    contentDescription = "GroupAdd"
                )
            }

            Column(
                modifier = Modifier
                    .constrainAs(chatRoom) {
                        top.linkTo(topBar.bottom, 14.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(bottomBar.top)

                        height = Dimension.fillToConstraints
                        width = Dimension.fillToConstraints
                    }
            ) {
                Column {
                    if (uiState.isPersonal) {
                        PersonalChatList(
                            chats = uiState.personalChats,
                            itemClick = { navigateToPersonalChatScreen(it) }
                        )
                    } else {
                        GroupChatList(
                            chats = uiState.groupChats,
                            itemClick = { navigateToGroupChatScreen(it) }
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
        ChangeType(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 14.dp),
            isOpen = uiState.isOpenMenu,
            isPersonal = uiState.isPersonal,
            onClick = { onClickChatType() },
            onClickType = { onChangeChatType(it) }
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
    modifier: Modifier = Modifier, //UI 요소의 외부 속성을 수정할 수 있는 Modifier입니다. 기본값은 Modifier
    chats: List<GroupChat>, //GroupChat 객체의 리스트로, 각 그룹 채팅의 데이터가 포함
    itemClick: (GroupChat) -> Unit // 각 그룹 채팅 항목이 클릭되었을 때 호출되는 람다 함수입니다. 클릭된 GroupChat 객체를 인자로 받습니다.
) {
    LazyVerticalGrid(//수직으로 나열된 그리드 형태의 목록을 표시하는 컴포넌트입니다. GridCells.Fixed(2)로 설정하여 두 개의 열로 구성
        modifier = modifier,
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(12.dp)
    ) {
        items(chats.size) { index ->
            GroupChatItem(
                chatGroup = chats[index],
                onClick = itemClick
            ) //제공된 chats 리스트의 크기만큼 반복하여 각 GroupChat 항목을 생성
        }
    }
}

@Composable
fun GroupChatItem(
    modifier: Modifier = Modifier, //UI 요소의 외부 속성을 수정할 수 있는 Modifier
    chatGroup: GroupChat, //현재 항목을 표시할 GroupChat 객체
    onClick: (GroupChat) -> Unit //항목이 클릭될 때 호출되는 람다 함수로, 클릭된 GroupChat 객체를 인자로 받습
) {
    val screenWidth = (LocalConfiguration.current.screenWidthDp / 2) - 52

    Column(
        modifier = modifier
            .width(screenWidth.dp)
            .clickable { onClick(chatGroup) }
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier) {
            Image( //그룹 채팅의 이미지를 표시
                modifier = Modifier
                    .size(screenWidth.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .border(
                        width = 3.dp,
                        color = Color(0xffe2e2e2),
                        shape = RoundedCornerShape(24.dp)
                    ),
                painter = painterResource(id = chatGroup.image), //chatGroup.image에서 이미지 리소스를 가져옵니다.
                contentDescription = "이미지", //접근성을 위해 이미지의 설명을 제공
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
            painter = painterResource(id = chatRoom.profile.profileImageRes),
            contentDescription = "ProfileImage"
        )
        Text(
            modifier = Modifier.constrainAs(person) {
                top.linkTo(parent.top)
                start.linkTo(image.end, 16.dp)
            },
            text = chatRoom.profile.name,
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

        }
        Box(
            modifier = Modifier.weight(0.25f),
            contentAlignment = Alignment.Center
        ) {
//            Icon(
//                modifier = Modifier
//                    .size(32.dp)
//                    .fillMaxWidth(0.25f)
//                    .alpha(0.4f),
//                painter = painterResource(id = R.drawable.ic_inventory),
//                contentDescription = "BottomBarIcon"
//            )
        }
        Box(
            modifier = Modifier.weight(0.25f),
            contentAlignment = Alignment.Center
        ){
//            Icon(
//                modifier = Modifier
//                    .size(32.dp)
//                    .fillMaxWidth(0.25f),
//                painter = painterResource(id = R.drawable.ic_chat),
//                contentDescription = "BottomBarIcon"
//            )
        }
        Box(
            modifier = Modifier.weight(0.25f),
            contentAlignment = Alignment.Center
        ) {
//            Icon(
//                modifier = Modifier
//                    .size(32.dp)
//                    .fillMaxWidth(0.25f)
//                    .alpha(0.4f),
//                painter = painterResource(id = R.drawable.ic_smile),
//                contentDescription = "BottomBarIcon"
//            )
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
            onClickChatType = {},
            onChangeChatType = {},
            navigateToInviteScreen = {},
            navigateToGroupChatScreen = {},
            navigateToPersonalChatScreen = {}
        )
    }
}
