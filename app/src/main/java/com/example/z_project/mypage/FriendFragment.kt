import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.z_project.R
import com.example.z_project.databinding.FragmentFriendBinding
import com.example.z_project.mypage.CustomAdapter
import com.example.z_project.mypage.FriendData
import com.example.z_project.mypage.FriendPlusFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link

class FriendFragment : Fragment() {
    private lateinit var binding: FragmentFriendBinding
    private lateinit var customAdapter: CustomAdapter
    private val friendList: MutableList<FriendData> = ArrayList()
    private var friendListListener: ListenerRegistration? = null  // Firestore 리스너

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFriendBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = binding.rcvFriendList
        recyclerView.layoutManager = LinearLayoutManager(context)

        // 친구 목록 실시간 업데이트 리스너 등록
        observeFriendList()

        customAdapter = CustomAdapter(friendList, requireContext())
        recyclerView.adapter = customAdapter

        val backButton = view.findViewById<ImageButton>(R.id.ib_back)
        backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        val llKakao = view.findViewById<FrameLayout>(R.id.ll_kakao)
        llKakao.setOnClickListener {
            sendKakaoTalkInvite()
        }

        val plusFriend = view.findViewById<ImageButton>(R.id.ib_friend_plus)
        plusFriend.setOnClickListener {
            showFriendPlusDialog()
        }
    }

    private fun showFriendPlusDialog() {
        FriendPlusFragment(requireContext()).show()
    }

    private fun observeFriendList() {
        val sharedPreferences =
            requireContext().getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
        val uniqueCode = sharedPreferences.getString("UNIQUE_CODE", null)

        if (uniqueCode != null) {
            Log.d("FriendList", "사용자 고유코드: ${uniqueCode}")

            // Firestore 실시간 리스너 설정
            friendListListener = FirebaseFirestore.getInstance().collection("friends")
                .document(uniqueCode)
                .collection("friendsList")
                .addSnapshotListener { querySnapshot, error ->
                    if (error != null) {
                        Log.e("FriendList", "친구 목록 실시간 조회 실패: ${error.message}")
                        return@addSnapshotListener
                    }

                    if (querySnapshot != null) {
                        friendList.clear() // 기존 리스트 초기화

                        for (document in querySnapshot.documents) {
                            val friendCode = document.getString("friendCode")
                            if (friendCode != null) {
                                FirebaseFirestore.getInstance().collection("users")
                                    .document(friendCode)
                                    .get()
                                    .addOnSuccessListener { userDocument ->
                                        if (userDocument.exists()) {
                                            val friendName = userDocument.getString("name") ?: "이름 없음"
                                            val friendProfileImage = userDocument.getString("profileImage") ?: ""

                                            friendList.add(FriendData(friendCode, friendName, friendProfileImage))

                                            customAdapter.notifyDataSetChanged()
                                        }
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.e("FriendList", "친구 데이터 로드 실패: ${exception.message}")
                                    }
                            } else {
                                Log.e("FriendList", "친구 코드가 null입니다.")
                            }
                        }
                    }
                }
        } else {
            Log.e("FriendList", "고유 코드가 존재하지 않습니다.")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 리스너 제거하여 메모리 누수 방지
        friendListListener?.remove()
    }

    private fun sendKakaoTalkInvite() {
        val sharedPreferences =
            requireContext().getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
        val uniqueCode = sharedPreferences.getString("UNIQUE_CODE", null)
        Log.d("Friendkakao", "uniqueCode: ${uniqueCode}")

        if (uniqueCode != null) {
            FirebaseFirestore.getInstance().collection("users").document(uniqueCode)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val userName = document.getString("name") ?: "이름 없음"

                        val feedTemplate = FeedTemplate(
                            content = Content(
                                title = "${userName}님이 보낸 초대코드를 확인하세요!",
                                description = "초대코드: ${uniqueCode}",
                                imageUrl = "",
                                link = Link(
                                    webUrl = "",
                                    mobileWebUrl = ""
                                )
                            )
                        )
                        ShareClient.instance.shareDefault(
                            requireContext(),
                            feedTemplate
                        ) { linkResult, error ->
                            if (error != null) {
                                Toast.makeText(
                                    requireContext(),
                                    "초대 링크 전송 실패: ${error.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                                Log.d("Friendkakao", "초대 링크 전송 실패")
                            } else if (linkResult != null) {
                                startActivity(linkResult.intent)
                                Log.d("Friendkakao", "초대 링크 전송 성공")
                            }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("Friendkakao", "Firebase에서 데이터 가져오기 실패", exception)
                }
        }
    }
}
