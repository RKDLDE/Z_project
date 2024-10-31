package com.example.z_project.chat.main_screen

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.z_project.R
import com.example.z_project.chat.model.Chat

import com.example.z_project.chat.model.PersonalChat
import com.example.z_project.chat.model.Profile
import com.example.z_project.chatting2.GroupChat
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(application: Application) : ViewModel() {
    // 사용자 본인의 고유 코드 가져오기
    private val sharedPreferences =
        application.getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
    val userId: String? = sharedPreferences.getString("UNIQUE_CODE", null)

    //firebase 테스트
    private val firestore = FirebaseFirestore.getInstance() //Cloud DB
    private val database = Firebase.database //Realtime DB
    private val messagesRef = database.getReference("messages")

    // UI State
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainUiState()
    )

    init {

        messagesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val personalChats = _uiState.value.personalChats
                val chatList = mutableListOf<Chat>()

                for (snapshot in dataSnapshot.children) {
                    // 각 messageId 아래의 데이터 읽기
                    val chat = snapshot.getValue(Chat::class.java)
                    chat?.let { c -> chatList.add(c) }
                }

                val updateChats = personalChats.map {
                    val checkTime = sharedPreferences.getLong("CHECK_TIME_${it.id}", 0)
                    val filteredList = chatList.filter { c -> c.chatRoomId == it.id }

                    PersonalChat(
                        id = it.id,
                        previewMessage = if (filteredList.isNotEmpty()) {
                            filteredList.last().message
                        } else {
                            ""
                        },
                        chatCount = filteredList.filter { c ->
                            (c.userId != userId)
                                    && (c.time ?: 0) > checkTime
                        }.size,
                        profile = it.profile,
                        chats = filteredList,
                        timestamp = if (filteredList.isNotEmpty()) {
                            filteredList.last().time ?: 0L
                        } else {
                            0L
                        }
                    )
                }

                // UI 상태 업데이트 (가져온 메시지로)
                _uiState.update {
                    it.copy(personalChats = updateChats.sortedByDescending { it.timestamp })
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 오류 처리 로직
                Log.e("Firebase", "Failed to read messages", databaseError.toException())
            }
        })
    }

    // 친구 목록을 가져오는 함수
    fun fetchPersonalChats() = viewModelScope.launch {
        val personalChats = mutableListOf<PersonalChat>()
        val chats = messagesRef.get()
            .await()
            .children
            .map { doc ->
                doc.getValue(Chat::class.java)
            }

        // Firestore에서 친구 목록 가져오기
        userId?.let { uid ->
            val friendsRef = firestore.collection("friends").document(uid).collection("friendsList")
            val friendDocuments = friendsRef.get().await()

            for (friend in friendDocuments.documents) {
                val friendId = friend.id // 각 친구의 고유 코드
                val chatId = friend.get("chatId")?.toString() ?: ""
                val checkTime = sharedPreferences.getLong("CHECK_TIME_${chatId}", 0)

                val filteredList = chats.filter { it?.chatRoomId == chatId }
                val previewMessage = if (filteredList.isNotEmpty()) {
                    filteredList.last()?.message ?: ""
                } else {
                    ""
                }
                val timestamp = if (filteredList.isNotEmpty()) {
                    filteredList.last()?.time ?: 0L
                } else {
                    0L
                }

                // 친구의 세부 정보를 users 컬렉션에서 가져오기
                val userDoc = firestore.collection("users").document(friendId).get().await()

                if (userDoc.exists()) {
                    val friendName = userDoc.getString("name") ?: "Unknown"
                    val friendId = userDoc.getString("uniqueCode") ?: "Unknown"
                    val profileImage = userDoc.getString("profileImage")

                    //친구 목록 프사 로드하기
                    //if (!profileImage.isNullOrEmpty()) {
                    // loadProfileImage(profileImage)
                    //} else {
                    // R.drawable.profile // 기본 이미지
                    //}

                    // PersonalChat 객체 생성
                    val personalChat = PersonalChat(
                        id = chatId, // 고유한 ID 생성
                        previewMessage = previewMessage, // 기본 메시지
                        chatCount = filteredList.filter { c ->
                            (c?.userId != userId)
                                    && (c?.time ?: 0) > checkTime
                        }.size, // 기본 카운트
                        profile = Profile(
                            profileImageRes = profileImage, // 친구 프로필 이미지
                            name = friendName // 친구 이름
                        ),
                        timestamp = timestamp
                    )

                    personalChats.add(personalChat)
                }
            }
        }

        _uiState.update {
            it.copy(personalChats = personalChats.sortedByDescending { it.timestamp })
        }
    }

    private fun loadProfileImage(profileImage: String) {

    }
//
//    // 친구 목록을 가져오는 함수
//    fun fetchAndSetPersonalChats() {
//        viewModelScope.launch {
//            val personalChats = fetchPersonalChats() // 친구 목록 가져오기
//            _uiState.update { it.copy(personalChats = personalChats) } // UI 상태 업데이트
//        }
//    }

    fun updateCheckTime(chatId: String) {
        val editor = sharedPreferences.edit()
        editor.putLong("CHECK_TIME_${chatId}", System.currentTimeMillis()).apply()
    }

    fun openChatTypeMenu(isOpen: Boolean) {
        if (_uiState.value.isOpenMenu != isOpen) {
            _uiState.update { it.copy(isOpenMenu = isOpen) }
        }
    }

    fun changeChatTypeMenu(isPersonal: Boolean) {
        _uiState.update { it.copy(isPersonal = isPersonal) }
        openChatTypeMenu(false)
    }

    fun removeChat(id: Long, isRemoveGroupChat: Boolean) {
        _uiState.update {
            if (isRemoveGroupChat) {
                it.copy(
//                    groupChats = it.groupChats.filter { groupChat -> groupChat.id != id }
                )
            } else {
                it.copy(
                    personalChats = it.personalChats.filter { groupChat -> groupChat.id != id.toString() }
                )
            }
        }
    }

    fun addChat(members: List<Profile>) {
        if (members.size == 1) {
            val personalChats = _uiState.value.personalChats.toMutableList()
            personalChats.add(
                0,
                PersonalChat(
                    previewMessage = "",
                    chatCount = 0,
                    profile = members[0],
                    chats = emptyList()
                )
            )

            _uiState.update { it.copy(personalChats = personalChats) }
        } else {
//            val groupChats = _uiState.value.groupChats.toMutableList()
//            groupChats.add(
//                0,
//                GroupChat(
//                    image = R.drawable.baseline_groups_24,
//                    title = "그룹",
//                    chatCount = 0,
//                    member = members,
//                    chats = emptyList()
//                )
//            )

//            _uiState.update { it.copy(groupChats = groupChats) }
        }
    }

}