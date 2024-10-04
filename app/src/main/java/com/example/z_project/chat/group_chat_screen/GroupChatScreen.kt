package com.example.z_project.chat.group_chat_screen

import android.view.ViewTreeObserver
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.z_project.R
import com.example.z_project.model.Chat
import com.example.z_project.getDefaultGroupChats
import com.example.z_project.chat.ui.theme.ChatUITheme

}@Composable
fun GroupChatScreen(
    uiState: GroupChatUiState,
    onNavigateUp: () -> Unit,
    onClickMenu: (Boolean) -> Unit,
    onClickExit: () -> Unit,
    onClickSend: (String) -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        val (topBar, chatList, bottomBar) = createRefs()
        ConstraintLayout(
            modifier = Modifier
                .constrainAs(topBar) { top.linkTo(parent.top) }
                .fillMaxWidth()
                .height(56.dp)
                .padding(start = 12.dp, end = 12.dp)
        ) {
            val (navigate, profile, icons) = createRefs()
            Icon(
                modifier = Modifier
                    .constrainAs(navigate) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    }
                    .clip(CircleShape)
                    .clickable { onNavigateUp() },
                painter = painterResource(id = R.drawable.ic_baseline_arrow_back_ios_new),
                contentDescription = "NavigateBack"
            )
            Row(
                modifier = Modifier.constrainAs(profile) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(navigate.end, 16.dp)
                    end.linkTo(icons.start)
                    width = Dimension.fillToConstraints
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape),
                    painter = painterResource(id = uiState.groupChat?.image ?: R.drawable.group),
                    contentDescription = "ProfileImage",
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = uiState.groupChat?.title ?: "",
                    fontSize = 18.sp
                )
            }
            Row(modifier = Modifier.constrainAs(icons) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_calendar_month),
                    contentDescription = "Calendar"
                )
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_user_talk),
                    contentDescription = "UserTalk"
                )
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    modifier = Modifier
                        .clip(CircleShape)
                        .clickable { onClickMenu(!uiState.isExit) },
                    painter = painterResource(id = R.drawable.ic_baseline_menu),
                    contentDescription = "Menu"
                )
            }
        }
        ChatList(
            modifier = Modifier
                .constrainAs(chatList) {
                    top.linkTo(topBar.bottom)
                    bottom.linkTo(bottomBar.top)
                    height = Dimension.fillToConstraints
                }
                .padding(start = 12.dp, end = 12.dp),
            chats = uiState.chats
        )
        if (uiState.isExit) {
            Box(
                modifier = Modifier
                    .constrainAs(bottomBar) {
                        bottom.linkTo(parent.bottom)
                    }
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .background(
                        color = Color(0xffd9d9d9),
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
                    .clickable { onClickExit() }
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "채팅방 나가기",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 20.sp,
                        color = Color.Red,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        } else {
            InputChat(
                modifier = Modifier
                    .constrainAs(bottomBar) { bottom.linkTo(parent.bottom, 12.dp) }
                    .padding(start = 8.dp, end = 8.dp),
                onClickSend = onClickSend
            )
        }
    }
}

@Composable
fun ChatList(
    modifier: Modifier = Modifier,
    chats: List<Chat>
) {
    val listState = rememberLazyListState()
    val imeState = rememberImeState()

    LaunchedEffect(key1 = chats, imeState) {
        if (imeState) {
            if (chats.isNotEmpty()) {
                listState.scrollToItem(chats.size - 1)
            }
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState
    ) {
        item {
            ChatDate()
            Spacer(modifier = Modifier.height(20.dp))
        }
        items(chats.size) { index ->
            val chat = chats[index]
            if (chat.isOther) {
                ChatContent(chat = chat)
            } else {
                MyChatContent(chat = chat)
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun ChatDate(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = Color(0xffd9d9d9),
                    shape = CircleShape
                )
                .padding(top = 4.dp, bottom = 4.dp, start = 36.dp, end = 36.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "2024년 7월 24일 수요일",
                color = Color.White,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
fun ChatContent(
    modifier: Modifier = Modifier,
    chat: Chat
) {
    ConstraintLayout(modifier = modifier) {
        val (profileView, nameView, messageView, timeView) = createRefs()

        if (chat.time.isNotEmpty()) {
            Image(
                modifier = Modifier
                    .constrainAs(profileView) {
                        top.linkTo(nameView.bottom)
                        start.linkTo(parent.start)
                    }
                    .size(42.dp)
                    .clip(CircleShape),
                painter = painterResource(id = chat.profile.profileImageRes),
                contentDescription = "ProfileImage",
                contentScale = ContentScale.Crop
            )
        } else {
            Box(modifier = Modifier
                .constrainAs(profileView) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                }
                .size(42.dp)
            )
        }
        Box(
            modifier = Modifier
                .constrainAs(messageView) {
                    top.linkTo(profileView.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(profileView.end, 8.dp)
                }
                .background(
                    color = Color(0xfff2f2f2),
                    shape = CircleShape
                )
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = chat.message)
        }
        if (chat.time.isNotEmpty()) {
            Text(
                modifier = Modifier.constrainAs(nameView) {
                    top.linkTo(parent.top)
                    start.linkTo(messageView.start, 10.dp)
                },
                text = chat.profile.name,
            )
        }
        Text(
            modifier = Modifier.constrainAs(timeView) {
                start.linkTo(messageView.end, 4.dp)
                bottom.linkTo(messageView.bottom)
            },
            text = chat.time,
            style = MaterialTheme.typography.labelMedium.copy(color = Color.Gray)
        )
    }
}

@Composable
fun MyChatContent(
    modifier: Modifier = Modifier,
    chat: Chat
) {
    ConstraintLayout(modifier = modifier.fillMaxWidth()) {
        val (messageView, timeView) = createRefs()
        val maxWidth = LocalConfiguration.current.screenWidthDp.dp

        Box(
            modifier = Modifier
                .constrainAs(messageView) {
                    end.linkTo(parent.end)
                }
                .background(
                    color = Color(0xffc6c6c6),
                    shape = CircleShape
                )
                .sizeIn(maxWidth = maxWidth.minus(58.dp))
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = chat.message)
        }
        Text(
            modifier = Modifier.constrainAs(timeView) {
                end.linkTo(messageView.start, 4.dp)
                bottom.linkTo(messageView.bottom)
            },
            text = chat.time,
            style = MaterialTheme.typography.labelMedium.copy(color = Color.Gray)
        )
    }
}

@Composable
fun InputChat(
    modifier: Modifier = Modifier,
    onClickSend: (String) -> Unit,
) {
    var text by remember { mutableStateOf("") }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .imePadding(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .weight(0.8f)
                .defaultMinSize(minHeight = 42.dp)
                .border(
                    width = 2.dp,
                    color = Color(0xffe2e2e2),
                    shape = CircleShape
                )
                .background(
                    color = Color(0xfff2f2f2)
                )
                .padding(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            BasicTextField(
                modifier = Modifier.fillMaxWidth(),
                value = text,
                onValueChange = { text = it }
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(
                    color = Color(0xffbfbfbf),
                    shape = CircleShape
                )
                .clickable {
                    onClickSend(text)
                    text = ""
                },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                modifier = Modifier.size(28.dp),
                painter = painterResource(id = R.drawable.ic_baseline_arrow_upward),
                contentDescription = "Send",
                tint = Color.White
            )
        }
    }
}

@Composable
fun rememberImeState(): Boolean {
    var imeState by remember {
        mutableStateOf(false)
    }

    val view = LocalView.current
    DisposableEffect(view) {
        val listener = ViewTreeObserver.OnGlobalLayoutListener {
            val isKeyboardOpen =
                ViewCompat.getRootWindowInsets(view)?.isVisible(WindowInsetsCompat.Type.ime())
                    ?: true
            imeState = isKeyboardOpen
        }

        view.viewTreeObserver.addOnGlobalLayoutListener(listener)
        onDispose { view.viewTreeObserver.removeOnGlobalLayoutListener(listener) }
    }
    return imeState
}

@Preview
@Composable
private fun GroupChatScreenPreview() {
    ChatUITheme {
        GroupChatScreen(
            uiState = GroupChatUiState(
                chats = getDefaultGroupChats()
            ),
            onNavigateUp = {},
            onClickMenu = {},
            onClickExit = {},
            onClickSend = {}
        )
    }
}