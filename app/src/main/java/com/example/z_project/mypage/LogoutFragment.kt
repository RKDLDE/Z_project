package com.example.z_project.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.z_project.R
import com.example.z_project.databinding.FragmentBottomsheetBinding
import com.example.z_project.databinding.FragmentLoginBinding
import com.example.z_project.databinding.FragmentMypageBinding
import com.google.android.material.dialog.MaterialDialogs
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.example.z_project.databinding.FragmentLogoutBinding

class LogoutFragment(
    context: Context,
) : Dialog(context) { // 뷰를 띄워야하므로 Dialog 클래스는 context를 인자로 받는다.

    private lateinit var binding: FragmentLogoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 만들어놓은 dialog_profile.xml 뷰를 띄운다.
        binding = FragmentLogoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() = with(binding) {
        // 뒤로가기 버튼, 빈 화면 터치를 통해 dialog가 사라지지 않도록
        setCancelable(false)

        // background를 투명하게 만듦
        // (중요) Dialog는 내부적으로 뒤에 흰 사각형 배경이 존재하므로, 배경을 투명하게 만들지 않으면
        // corner radius의 적용이 보이지 않는다.
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // OK Button 클릭에 대한 Callback 처리. 이 부분은 상황에 따라 자유롭게!
        noButton.setOnClickListener {
            dismiss()
        }
    }
}
//class LogoutFragment {
//    private lateinit var itemClickListener: ItemClickListener
//    private lateinit var binding: DialogCustomBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = DialogCustomBinding.inflate(LayoutInflater.from(context))
//        setContentView(binding.root)
//
//        // 사이즈를 조절하고 싶을 때 사용 (use it when you want to resize dialog)
//        // resize(this, 0.8f, 0.4f)
//
//        // 배경을 투명하게 (Make the background transparent)
//        // 다이얼로그를 둥글게 표현하기 위해 필요 (Required to round corner)
//        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//
//        // 다이얼로그 바깥쪽 클릭시 종료되도록 함 (Cancel the dialog when you touch outside)
//        setCanceledOnTouchOutside(true)
//
//        // 취소 가능 유무
//        setCancelable(true)
//
//        binding.tvCancel.setOnClickListener {
//            dismiss() // 다이얼로그 닫기 (Close the dialog)
//        }
//
//        binding.tvCall.setOnClickListener {
//            // interface를 이용해 다이얼로그를 호출한 곳에 값을 전달함
//            // Use interface to pass the value to the activty or fragment
//            itemClickListener.onClick("031-467-0000")
//            dismiss()
//        }
//    }
//
//    // 사이즈를 조절하고 싶을 때 사용 (use it when you want to resize dialog)
//    private fun resize(dialog: Dialog, width: Float, height: Float){
//        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
//
//        if (Build.VERSION.SDK_INT < 30) {
//            val size = Point()
//            windowManager.defaultDisplay.getSize(size)
//
//            val x = (size.x * width).toInt()
//            val y = (size.y * height).toInt()
//            dialog.window?.setLayout(x, y)
//        } else {
//            val rect = windowManager.currentWindowMetrics.bounds
//
//            val x = (rect.width() * width).toInt()
//            val y = (rect.height() * height).toInt()
//            dialog.window?.setLayout(x, y)
//        }
//    }
//
//    interface ItemClickListener {
//        fun onClick(message: String)
//    }
//
//    fun setItemClickListener(itemClickListener: ItemClickListener) {
//        this.itemClickListener = itemClickListener
//    }
//}