package com.example.z_project.chat.personal_chat_screen

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.z_project.chat.model.Chat
import com.example.z_project.chat.model.PersonalChat
import com.example.z_project.chat.model.Profile
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import javax.inject.Inject
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.random.Random

@HiltViewModel
class PersonalChatViewModel @Inject constructor (
    savedStateHandle: SavedStateHandle,
    application: Application
): ViewModel() {

    //firebase 테스트
    private val firestore = FirebaseFirestore.getInstance() //Cloud DB
    private val database = Firebase.database //Realtime DB
    private val messagesRef = database.getReference("messages")

    private val sharedPreferences =
        application.getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
    val userId: String? = sharedPreferences.getString("UNIQUE_CODE", null)
    var chatRoomId: String = ""

    // 메시지를 Realtime Firebase에 전송하는 함수
    fun sendMessage(message: String, message1: String) {
        val profile = _uiState.value.personalChat?.profile
        val messageId = messagesRef.push().key // 랜덤 메시지 ID 생성
        val timestamp = System.currentTimeMillis() // 현재 시간 생성

        // 채팅 메시지 데이터 생성
        val chat = Chat(
            chatRoomId = _uiState.value.personalChat?.id,
            messageId = messageId,
            userId = userId,
            message = message,
            time = timestamp,
            profile = Profile(
                profileImageRes = profile?.profileImageRes,
                name = profile?.name
            ),
            isOther = false
        )

        // Firebase Realtime Database에 메시지 저장
        messageId?.let {
            messagesRef.child(it).setValue(chat)
                .addOnSuccessListener {
                    Log.d("Firebase", "Message sent successfully")

//                    // Cloud Firestore에 채팅방 정보 저장
//                    saveChatRoomToFirestore(chatRoomId)

                    // 메시지 전송 후 UI 상태 업데이트 (채팅방 ID로 조회)
                    fetchMessages(chatRoomId)
                }
                .addOnFailureListener { e ->
                    Log.e("Firebase", "Failed to send message", e)
                }
        }
    }

    // Cloud Firestore에 채팅방 정보를 저장하는 함수f
    private fun saveChatRoomToFirestore(chatRoomId: String) {
        val db = FirebaseFirestore.getInstance()
        val chatListRef = db.collection("chat").document(userId ?: "")
            .collection("ChatList")

        chatListRef.document(chatRoomId).set(mapOf("chatRoomId" to chatRoomId))
            .addOnSuccessListener {
                Log.d("Firestore", "Chat room ID saved successfully to Firestore")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Failed to save chat room ID", e)
            }
    }


    // Firebase에서 메시지를 읽어오는 함수
    fun fetchMessages(chatRoomId: String) {
        // chatRooms/<chatRoomId>/ 메시지 경로 참조
        messagesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val chatList = mutableListOf<Chat>()
                for (snapshot in dataSnapshot.children) {
                    // 각 messageId 아래의 데이터 읽기
                    val chat = snapshot.getValue(Chat::class.java)
                    if(chat?.chatRoomId == _uiState.value.personalChat?.id) {
                        chat?.let { chatList.add(it) }
                    }
                }

                // UI 상태 업데이트 (가져온 메시지로)
                _uiState.update {
                    it.copy(chats = chatList)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 오류 처리 로직
                Log.e("Firebase", "Failed to read messages", databaseError.toException())
            }
        })
    }

    // UI State
    private val _uiState = MutableStateFlow(PersonalChatUiState())
    val uiState = _uiState.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PersonalChatUiState()
    )

    init {
        val personalChatJson = savedStateHandle.get<String>("personalChat")
        if (personalChatJson != null) {
//            val personalChat = Json.decodeFromString(PersonalChat.serializer(), personalChatJson)
            val decodedJson = URLDecoder.decode(personalChatJson, "UTF-8")
            val personalChat: PersonalChat =
                Json.decodeFromString(PersonalChat.serializer(), decodedJson)
            val chatList = mutableListOf<Chat>()

            messagesRef.get().addOnSuccessListener { doc ->
                for (snapshot in doc.children) {
                    // 각 messageId 아래의 데이터 읽기
                    val chat = snapshot.getValue(Chat::class.java)
                    if(chat?.chatRoomId == personalChat.id) {
                        chat?.let { chatList.add(it) }
                    }
                }

                _uiState.update {
                    it.copy(
                        personalChat = personalChat,
                        chats = chatList
                    )
                }
            }
        }
    }


    //랜덤 코드 생성 함수
    private fun generateRandomCode(length: Int = 12): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { chars[Random.nextInt(chars.length)] }
            .joinToString("")
    }


    fun openExitMenu(isExit: Boolean) {
        _uiState.update {
            it.copy(isExit = isExit)
        }
    }

    fun selectReplyChat(chat: Chat?) {
        _uiState.update {
            it.copy(replyChat = chat)
        }
    }
}