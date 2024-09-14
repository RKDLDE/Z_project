package com.example.z_project.chat.personal_chat_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.constraintlayout.compose.Dimension
import com.example.z_project.R
import com.example.z_project.chat.ui.theme.ChatUITheme

@Composable
fun PersonalChatScreen() {
    var isExit by remember { mutableStateOf(false) }

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
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
                modifier = Modifier.constrainAs(navigate) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                },
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
                    painter = painterResource(id = R.drawable.person1),
                    contentDescription = "ProfileImage",
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "냠",
                    fontSize = 18.sp
                )
            }
            Row(modifier = Modifier.constrainAs(icons) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            }) {
                Icon(
                    modifier = Modifier.clickable {
                        isExit = !isExit
                    },
                    painter = painterResource(id = R.drawable.ic_baseline_menu),
                    contentDescription = "Menu"
                )
            }
        }
        com.example.z_project.chat.group_chat_screen.ChatList(
            modifier = Modifier
                .constrainAs(chatList) {
                    top.linkTo(topBar.bottom)
                    bottom.linkTo(bottomBar.top)
                    height = Dimension.fillToConstraints
                }
                .padding(start = 12.dp, end = 12.dp)
        )
        if (isExit) {
            Box(
                modifier = Modifier
                    .constrainAs(bottomBar) {
                        bottom.linkTo(parent.bottom)
                    }
                    .fillMaxWidth()
                    .background(
                        color = Color(0xffd9d9d9),
                        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
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
            com.example.z_project.chat.group_chat_screen.InputChat(

                modifier = Modifier
                    .constrainAs(bottomBar) {
                        bottom.linkTo(parent.bottom, 12.dp)
                    }
                    .padding(start = 8.dp, end = 8.dp)
            )
        }
    }
}

@Composable
fun ChatList(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        com.example.z_project.chat.group_chat_screen.ChatDate()
        Spacer(modifier = Modifier.height(24.dp))
        ChatContent(
            profileImage = painterResource(id = R.drawable.person1),
            messageContent = "언제쯤 도착함???",
            time = "18:00"
        )
        Spacer(modifier = Modifier.height(12.dp))
        com.example.z_project.chat.group_chat_screen.MyChatContent(
            messageContent = "몰라?",
            time = "18:05"
        )
        Spacer(modifier = Modifier.height(12.dp))
        ChatContent(
            profileImage = painterResource(id = R.drawable.person1),
            messageContent = "",
            messageImage = painterResource(id = R.drawable.person1),
            time = ""
        )
        Spacer(modifier = Modifier.height(12.dp))
        ChatContent(
            profileImage = painterResource(id = R.drawable.person1),
            messageContent = "ㅋㅋㅋㅋㅋ 개웃겨",
            time = "18:10"
        )
        Spacer(modifier = Modifier.height(12.dp))
        ChatContent(
            profileImage = null,
            messageContent = "ㅋㅋㅋㅋㅋ 개웃겨",
            time = ""
        )
        Spacer(modifier = Modifier.height(12.dp))
        ChatContent(
            profileImage = null,
            messageContent = "ㅋㅋㅋㅋㅋ 개웃겨",
            time = ""
        )
        Spacer(modifier = Modifier.height(12.dp))
        ChatContent(
            profileImage = painterResource(id = R.drawable.person1),
            messageContent = "언제쯤 도착함???",
            time = "18:11"
        )
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
    profileImage: Painter?,
    messageContent: String,
    messageImage: Painter? = null,
    time: String
) {
    ConstraintLayout(modifier = modifier) {
        val (profileView, nameView, messageView, timeView) = createRefs()


        if (messageImage != null) {
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
            if (profileImage != null) {
                Image(
                    modifier = Modifier
                        .constrainAs(profileView) {
                            top.linkTo(nameView.bottom)
                            start.linkTo(parent.start)
                        }
                        .size(42.dp)
                        .clip(CircleShape),
                    painter = profileImage,
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
                Text(text = messageContent)
            }
            Text(
                modifier = Modifier.constrainAs(timeView) {
                    start.linkTo(messageView.end, 4.dp)
                    bottom.linkTo(messageView.bottom)
                },
                text = time,
                style = MaterialTheme.typography.labelMedium.copy(color = Color.Gray)
            )
        }
    }
}

@Composable
fun MyChatContent(
    modifier: Modifier = Modifier,
    messageContent: String,
    time: String
) {
    ConstraintLayout(modifier = modifier.fillMaxWidth()) {
        val (messageView, timeView) = createRefs()

        Box(
            modifier = Modifier
                .constrainAs(messageView) {
                    end.linkTo(parent.end)
                }
                .background(
                    color = Color(0xffc6c6c6),
                    shape = CircleShape
                )
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = messageContent)
        }
        Text(
            modifier = Modifier.constrainAs(timeView) {
                end.linkTo(messageView.start, 4.dp)
                bottom.linkTo(messageView.bottom)
            },
            text = time,
            style = MaterialTheme.typography.labelMedium.copy(color = Color.Gray)
        )
    }
}

@Composable
fun InputChat(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(42.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .weight(0.8f)
                .fillMaxHeight()
                .border(
                    width = 2.dp,
                    color = Color(0xffe2e2e2),
                    shape = CircleShape
                )
                .background(
                    color = Color(0xfff2f2f2)
                )
        )
        Spacer(modifier = Modifier.width(12.dp))
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(
                    color = Color(0xffbfbfbf),
                    shape = CircleShape
                ),
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

@Preview
@Composable
private fun PersonalChatScreenPreview() {
    ChatUITheme {
        PersonalChatScreen()
    }
}
