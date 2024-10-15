import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.z_project.R
import com.example.z_project.databinding.FragmentFriendBinding
import com.example.z_project.mypage.CustomAdapter
import com.example.z_project.mypage.DeleteFragment
import com.example.z_project.mypage.FriendData
import com.example.z_project.mypage.FriendPlusFragment
import com.google.firebase.firestore.FirebaseFirestore
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.talk.model.Friend
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link


class FriendFragment : Fragment() {
    lateinit var binding: FragmentFriendBinding
    private lateinit var customAdapter: CustomAdapter
    private val friendList: MutableList<FriendData> = ArrayList() // FriendData 객체를 저장할 리스트

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFriendBinding.inflate(inflater, container, false)

//        val testDataSet: MutableList<String> = ArrayList()
//        for (i in 0..9) {
//            testDataSet.add("친구$i")
//        }
//
//        val recyclerView: RecyclerView = binding.rcvFriendList
//        recyclerView.layoutManager = LinearLayoutManager(context)
//
//        val customAdapter = CustomAdapter(testDataSet, requireContext())
//        recyclerView.adapter = customAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 설정
        val recyclerView: RecyclerView = binding.rcvFriendList
        recyclerView.layoutManager = LinearLayoutManager(context)

        customAdapter = CustomAdapter(friendList, requireContext())
        recyclerView.adapter = customAdapter

        // 친구 목록 가져오기
        loadFriendList()

        // 뒤로가기 버튼
        val backButton = view.findViewById<ImageButton>(R.id.ib_back)
        // 이미지 버튼 클릭 이벤트 처리
        backButton.setOnClickListener {
            // 이전 Fragment로 이동
            requireActivity().supportFragmentManager.popBackStack()
        }

        // ll_kakao 클릭 시 카카오톡 링크 보내기
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

    private fun loadFriendList() {
        val sharedPreferences =
            requireContext().getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
        val allEntries = sharedPreferences.all // 모든 SharedPreferences 엔트리 가져오기

        // 모든 친구 코드를 가져와서 Firebase에서 이름과 프로필 이미지를 가져옵니다.
        allEntries.keys.forEach { friendCode ->
            FirebaseFirestore.getInstance().collection("users").document(friendCode)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val friendName = document.getString("name") ?: "이름 없음"
                        val friendProfileImage =
                            document.getString("profileImageUrl") ?: "" // 프로필 이미지 URL

                        // FriendData 객체 생성 후 리스트에 추가
                        friendList.add(FriendData(friendCode, friendName, friendProfileImage))

                        // 로그로 친구 목록 출력
                        Log.d("FriendList", "친구 코드: ${friendCode}, 이름: ${friendName}")

                        customAdapter.notifyDataSetChanged() // 데이터 변경 알리기
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("FriendList", "친구 데이터 로드 실패: ${exception.message}")
                }
        }
    }
    private fun sendKakaoTalkInvite() {
        // SharedPreferences에서 고유 코드 가져오기
        val sharedPreferences =
            requireContext().getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE)
        val uniqueCode = sharedPreferences.getString("UNIQUE_CODE", null)
        Log.d("Friendkakao", "uniqueCode: ${uniqueCode}")

        if (uniqueCode != null) {
            // Firebase에서 사용자 이름과 프로필 이미지 URL 가져오기
            FirebaseFirestore.getInstance().collection("users").document(uniqueCode)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val userName = document.getString("name") ?: "이름 없음"

                        // FeedTemplate을 사용하여 초대 링크 생성
                        val feedTemplate = FeedTemplate(
                            content = Content(
                                title = "${userName}님이 보낸 초대코드를 확인하세요!",
                                description = "초대코드: ${uniqueCode}",
                                imageUrl = "",  // 프로필 이미지 URL 추가
                                link = Link(  // 빈 링크 설정
                                    webUrl = "",
                                    mobileWebUrl = ""
                                )
                            )
                        )
                        // 카카오톡 링크 전송
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
                                // 초대 링크가 성공적으로 전송됨
                                startActivity(linkResult.intent)
                                Log.d("Friendkakao", "초대 링크 전송 성공")
//                                copyToClipboard(uniqueCode)
                            }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("Friendkakao", "Firebase에서 데이터 가져오기 실패", exception)
                }
        }
    }

    // 고유 코드를 클립보드에 저장하는 함수
//    private fun copyToClipboard(uniqueCode: String) {
//        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//        val clip = ClipData.newPlainText("Unique Code", uniqueCode)
//        clipboard.setPrimaryClip(clip)
//        Toast.makeText(requireContext(), "친구코드가 클립보드에 저장되었습니다.", Toast.LENGTH_SHORT).show()
//    }
}