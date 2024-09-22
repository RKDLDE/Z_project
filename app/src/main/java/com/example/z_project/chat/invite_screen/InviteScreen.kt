package com.example.z_project.chat.invite_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.z_project.chat.ui.theme.ChatUITheme

@Composable
fun InviteScreen() {
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
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(id = R.drawable.ic_baseline_close),
                    contentDescription = "Close"
                )
            }
            InviteMemberList()
        }

        InviteButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp)
        )
    }
}

@Composable
fun InviteMemberList(
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.fillMaxWidth()) {
        item {
            InviteMember(
                image = painterResource(id = R.drawable.person1),
                name = "냠",
                isSelected = true
            )
            InviteMember(
                image = painterResource(id = R.drawable.person2),
                name = "현",
                isSelected = true
            )
            InviteMember(
                image = painterResource(id = R.drawable.person3),
                name = "고도리",
                isSelected = true
            )
            InviteMember(
                image = painterResource(id = R.drawable.person4),
                name = "도금",
                isSelected = false
            )
        }
    }
}

@Composable
fun InviteMember(
    modifier: Modifier = Modifier,
    image: Painter,
    name: String,
    isSelected: Boolean
) {
    ConstraintLayout(
        modifier = modifier
            .fillMaxWidth()
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
            painter = image,
            contentDescription = "ProfileImage"
        )
        Text(
            modifier = Modifier.constrainAs(person) {
                top.linkTo(parent.top)
                start.linkTo(imageLayout.end, 16.dp)
                bottom.linkTo(parent.bottom)
            },
            text = name,
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
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                color = Color(0xffd9d9d9),
                shape = RoundedCornerShape(16.dp)
            )
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
        InviteScreen()
    }
}