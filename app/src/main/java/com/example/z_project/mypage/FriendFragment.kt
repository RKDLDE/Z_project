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
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link


class FriendFragment : Fragment() {
    lateinit var binding: FragmentFriendBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFriendBinding.inflate(inflater, container, false)

        val testDataSet: MutableList<String> = ArrayList()
        for (i in 0..9) {
            testDataSet.add("친구$i")
        }

        val recyclerView: RecyclerView = binding.rcvFriendList
        recyclerView.layoutManager = LinearLayoutManager(context)

        val customAdapter = CustomAdapter(testDataSet, requireContext())
        recyclerView.adapter = customAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
    }

    private fun sendKakaoTalkInvite() {
        // FeedTemplate을 사용하여 초대 링크 생성
        val feedTemplate = FeedTemplate(
            content = Content(
                title = "초대하기",
                description = "친구를 초대합니다!",
                imageUrl = "https://naver.com",  // 이미지 URL 추가
                link = Link(
                    webUrl = "https://naver.com",  // 초대 링크 추가
                    mobileWebUrl = "https://naver.com"
                )
            ),
            buttons = listOf(
                Button(
                    "친구추가 ",
                    Link(
                        mobileWebUrl = "https://naver.com",
                        //androidExecutionParams = "key1=value1"
                    )
                )
            )
        )

        // 카카오톡 링크 전송
        ShareClient.instance.shareDefault(requireContext(), feedTemplate) { linkResult, error ->
            if (error != null) {
                Toast.makeText(
                    requireContext(),
                    "초대 링크 전송 실패: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (linkResult != null) {
                // 초대 링크가 성공적으로 전송됨
                startActivity(linkResult.intent)
                Log.d("초대 링크", "성공")
            }
        }
    }
}
