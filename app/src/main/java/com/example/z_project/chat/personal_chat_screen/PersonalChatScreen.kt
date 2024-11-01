package com.example.z_project.chat.personal_chat_screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.node.CanFocusChecker.end
//import androidx.compose.ui.node.CanFocusChecker.start
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.z_project.R
import com.example.z_project.chat.model.Profile
import com.example.z_project.chat.model.Chat
import com.example.z_project.chat.model.getDefaultPersonalChats
import com.example.z_project.chat.ui.theme.ChatUITheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import coil.compose.rememberAsyncImagePainter
import java.util.Locale


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PersonalChatScreen(
    uiState: PersonalChatUiState,
    userId: String,
    onNavigateUp: () -> Unit,
    onClickMenu: (Boolean) -> Unit,
    onSwipeSelect: (Chat) -> Unit,
    onClickExit: () -> Unit,
    onClickSend: (String) -> Unit,
    onClickRemoveReply: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
        //.imePadding() // 전체 레이아웃에 imePadding 적용
        //.navigationBarsPadding().imePadding() // 키보드가 나타날 때 스크롤 가능하게 설정
        //.imeNestedScroll()    // 키보드에 맞춰 스크롤 가능하게 설정
    ) {// 배경색을 흰색으로 설정
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
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
                verticalAlignment = Alignment.CenterVertically
            ) {
                if(uiState.personalChat?.profile != null) {
                    Image(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape),
                        painter = rememberAsyncImagePainter(
                            model = uiState.personalChat.profile.profileImageRes
                        ),
                        contentDescription = "ProfileImage",
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = uiState.personalChat.profile.name ?: "냠",
                        fontSize = 18.sp
                    )
                }
            }
        }
        // 채팅 리스트
        ChatList(
            modifier = Modifier
                .constrainAs(chatList) {
                    top.linkTo(topBar.bottom)
                    bottom.linkTo(bottomBar.top)
                    height = Dimension.fillToConstraints
                }
                .padding(start = 12.dp, end = 12.dp),

            chats = uiState.chats ?: emptyList(),
            userId = userId,
            profile = uiState.personalChat?.profile,
            onSwipeSelect = onSwipeSelect
        )
        // 채팅 입력 부분
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
                    text = "차단하기",
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
                    .constrainAs(bottomBar) { bottom.linkTo(parent.bottom) }
                    .padding(start = 8.dp, end = 8.dp),

                replyChat = uiState.replyChat,
                onClickSend = onClickSend,
                //onClickSend = sendMessage,,
                userId = userId,
                profile = uiState.personalChat?.profile,
                onClickRemoveReply = onClickRemoveReply
            )
        }
    }
}


//채팅창
@Composable
fun ChatList(
    modifier: Modifier = Modifier,
    chats: List<Chat>,
    userId: String?,
    profile: Profile?,
    onSwipeSelect: (Chat) -> Unit
) {
//    val listState = rememberLazyListState()
//    val imeState = rememberImeState()
//
//    LaunchedEffect(key1 = chats, imeState, onSwipeSelect) {
//        if (imeState) {
//            if (chats.isNotEmpty()) {
//                listState.scrollToItem(chats.size - 1)
//            }
//        }
//    }
//    LazyColumn(
//        modifier = modifier
//            .fillMaxSize(),
//        state = listState
//    ) {
//        item {
//            ChatDate()
//            Spacer(modifier = Modifier.height(20.dp))
//        }
//        items(chats) { chat -> // items 블록을 수정합니다.
//            if (chat.userId == userId) { // userId와 비교
//                MyChatContent(
//                    chat = chat,
//                    onSwipeSelect = onSwipeSelect
//                )
//            } else {
//                ChatContent(
//                    chat = chat,
//                    onSwipeSelect = onSwipeSelect
//                )
//            }
//            Spacer(modifier = Modifier.height(12.dp))
//        }
//    }

    LazyColumn(

        modifier = modifier
            .fillMaxSize()
            .background(Color.White) // 배경색을 흰색으로 설정
            .imePadding() // LazyColumn에 imePadding 추가

    ) {
        items(chats) { chat ->
            if (chat.userId == userId) { // userId와 비교
                MyChatContent(
                    chat = chat,
                    userId = userId ?: "",
                    profile = profile,
                    onSwipeSelect = onSwipeSelect
                )
            } else {
                ChatContent(
                    chat = chat,
                    userId = userId ?: "",
                    profile = profile,
                    onSwipeSelect = onSwipeSelect
                )
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
                text = SimpleDateFormat("yyyy년 MM월 dd일 E요일", Locale.KOREA)
                    .format(System.currentTimeMillis()),
                color = Color.White,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}


//상대방 말풍선  // UI부분 없애기
@Composable
fun ChatContent(
    modifier: Modifier = Modifier,
    userId: String,
    chat: Chat,
    profile: Profile?,
    onSwipeSelect: (Chat) -> Unit
) {
    val offsetX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    ConstraintLayout(
        modifier = modifier
            .draggable(
                state = rememberDraggableState { delta ->
                    val newValue = (offsetX.value + delta * 0.2f).coerceIn(null, 0f)
                    scope.launch {
                        offsetX.snapTo(newValue)
                    }
                },
                orientation = Orientation.Horizontal,
                onDragStopped = {
                    scope.launch {
                        offsetX.animateTo(
                            targetValue = 0f,
                            animationSpec = tween(durationMillis = 300)
                        )
                    }
                    if (offsetX.value <= -50f) {
                        onSwipeSelect(chat)
                    }
                }
            )
            .offset(x = offsetX.value.dp)
    ) {
        val (profileView, nameView, messageView, timeView) = createRefs()

        if (chat.imageRes != null) {
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = Color(0xfff2f2f2))
                        .clip(RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "피드 내용", color = Color.White)
                }
                Image(
                    modifier = Modifier
                        .padding(12.dp)
                        .size(36.dp)
                        .clip(CircleShape)
                        .align(Alignment.TopStart),
                    painter = painterResource(id = R.drawable.person3),
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .background(
                            color = Color(0xffd8d8d8),
                            shape = CircleShape
                        )
                        .align(Alignment.TopEnd),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        modifier = Modifier.padding(
                            start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp
                        ),
                        text = "7월 24일",
                        style = MaterialTheme.typography.labelMedium.copy(color = Color.White)
                    )
                }
            }
        } else {
            if (chat.time != 0L) {
                Image(
                    modifier = Modifier
                        .constrainAs(profileView) {
                            top.linkTo(nameView.bottom)
                            start.linkTo(parent.start)
                        }
                        .size(42.dp)
                        .clip(CircleShape),
                    painter = rememberAsyncImagePainter(model = profile?.profileImageRes),
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
                        shape = RoundedCornerShape(28.dp)
                    )
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                if (chat.replyChat != null) {
                    Column {
                        Text(
                            text = (
                                    if (chat.replyChat.userId != userId) {
                                        profile?.name
                                    } else {
                                        "나"
                                    } + "에게 답장"),
                            fontWeight = FontWeight.Bold
                        )
                        chat.replyChat.message?.let {
                            Text(
                                text = it,
                                maxLines = 1,
                                color = Color.Black.copy(alpha = 0.6f),
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = chat.message ?: "")
                    }
                } else {
                    Text(text = chat.message ?: "")
                }
            }
            Text(
                modifier = Modifier.constrainAs(timeView) {
                    start.linkTo(messageView.end, 4.dp)
                    bottom.linkTo(messageView.bottom)
                },
                text = SimpleDateFormat("HH:mm", Locale.KOREA).format(chat.time),
                style = MaterialTheme.typography.labelMedium.copy(color = Color.Gray)
            )
        }
    }
}


//사용자 본인의 말풍선
@Composable
fun MyChatContent(
    modifier: Modifier = Modifier,
    userId: String,
    profile: Profile?,
    chat: Chat,
    onSwipeSelect: (Chat) -> Unit
) {
    val scope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
            .draggable(
                state = rememberDraggableState { delta ->
                    val newValue = (offsetX.value + delta * 0.2f).coerceIn(null, 0f)
                    scope.launch {
                        offsetX.snapTo(newValue)
                    }
                },
                orientation = Orientation.Horizontal,
                onDragStopped = {
                    scope.launch {
                        offsetX.animateTo(
                            targetValue = 0f,
                            animationSpec = tween(durationMillis = 300)
                        )
                    }
                    if (offsetX.value <= -50f) {
                        onSwipeSelect(chat)
                    }
                }
            )
            .offset(x = offsetX.value.dp)
    ) {
        val (messageView, timeView, countView) = createRefs()
        val maxWidth = LocalConfiguration.current.screenWidthDp.dp


        Box(
            modifier = Modifier
                .constrainAs(messageView) {
                    end.linkTo(parent.end)
                }
                .background(
                    color = Color(0xffc6c6c6),
                    shape = RoundedCornerShape(28.dp)
                )
                .sizeIn(maxWidth = maxWidth.minus(58.dp))
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            if (chat.replyChat != null) {
                Column {
                    Text(
                        text = (
                                if (chat.replyChat.userId != userId) {
                                    profile?.name
                                } else {
                                    "나"
                                } + "에게 답장"),
                        fontWeight = FontWeight.Bold
                    )
                    chat.replyChat.message?.let {
                        Text(
                            text = it,
                            maxLines = 1,
                            color = Color.Black.copy(alpha = 0.6f),
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = chat.message!!)
                }
            } else {
                Text(text = chat.message!!)
            }

        }
        if(!chat.read) {
            Text(
                modifier = Modifier
                    .constrainAs(countView) {
                        end.linkTo(messageView.start, 4.dp)
                        bottom.linkTo(timeView.top, 2.dp)
                    },
                text = "읽지 않음",
                style = MaterialTheme.typography.labelMedium.copy(color = Color.Magenta)
            )
        }
        Text(
            modifier = Modifier
                .constrainAs(timeView) {
                    end.linkTo(messageView.start, 4.dp)
                    bottom.linkTo(messageView.bottom)
                },
            text = SimpleDateFormat("HH:mm", Locale.KOREA).format(chat.time),
            style = MaterialTheme.typography.labelMedium.copy(color = Color.Gray)
        )
    }
}


//텍스트 작성하는 공간
@Composable
fun InputChat(
    modifier: Modifier = Modifier,
    replyChat: Chat?, // ReplyChat으로 타입 수정
    userId: String,
    profile: Profile?,
    onClickSend: (String) -> Unit, // 여기수정함
    onClickRemoveReply: () -> Unit
) {
    var text by remember { mutableStateOf("") }
    Column(
        modifier = modifier
            .fillMaxWidth()
            //.imePadding() // 여기에 imePadding 추가
            .background(Color.White) // 배경색을 흰색으로 설정
    ) {
        if (replyChat != null) {
            // 답장 내용 표시
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
            ) {
                val (replyView, closeView) = createRefs()
                Column(
                    modifier = Modifier
                        .constrainAs(replyView) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(closeView.start, 8.dp)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.fillToConstraints
                        }
                        .widthIn(max = 250.dp) // replyChat 말풍선 최대 너비 제한
                        .padding(8.dp) // 패딩 추가로 보기 좋게 조정
                ) {
                    Text(
                        text = if (replyChat.userId != userId) {
                            profile?.name
                        } else {
                            "나"
                        } + "에게 답장",
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = replyChat.message ?: "",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.wrapContentSize() // 텍스트 길이에 맞춰 말풍선 조정
                    )
                }
                Icon(
                    modifier = Modifier
                        .constrainAs(closeView) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(parent.end)
                        }
                        .clickable { onClickRemoveReply() },
                    painter = painterResource(id = R.drawable.ic_baseline_close),
                    contentDescription = "close"
                )
            }
        }
        // 메시지 입력 UI
        Row(
            modifier = Modifier,
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
                        onClickSend(text) // 답장 정보 포함하여 메시지 전송
                        text = "" // 입력 필드 초기화
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
}


//    @Preview
@Composable
fun PersonalChatScreenPreview() {
    ChatUITheme {
        PersonalChatScreen(
            uiState = PersonalChatUiState(
                // 기본 대화 목록 데이터를 생성하고, 일부 데이터의 필드를 수정
                chats = getDefaultPersonalChats(
                    profile = Profile(
                        profileImageRes = R.drawable.person1.toString(),
                        name = "냠"
                    ),
                    lastMessage = "언제쯤 도착함???",
                ).mapIndexed { index, chat ->
                    if (index == 1) {
                        chat.copy(
                            replyChat = Chat(
                                profile = Profile(
                                    profileImageRes = R.drawable.person1.toString().toString(),
                                    name = "냠"
                                ),
                                message = "언제쯤 도착함???",
                                imageRes = null,
                                isOther = true,
                                time = System.currentTimeMillis()
//                            )
                            )
                        )
                    } else {
                        chat
                    }
                }
            ),
            userId = "",
            onNavigateUp = {},
            onClickMenu = {},
            onSwipeSelect = {},
            onClickExit = {},
            onClickSend = {},
            onClickRemoveReply = {}
        )
    }
}
