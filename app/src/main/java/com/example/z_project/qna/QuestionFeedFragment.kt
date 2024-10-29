package com.example.z_project.qna

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.z_project.R
import com.example.z_project.databinding.FragmentMypageBinding
import com.example.z_project.databinding.FragmentQuestionfeedBinding

class QuestionFeedFragment : Fragment() {
    lateinit var binding: FragmentQuestionfeedBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuestionfeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val infoButton: ImageView = view.findViewById(R.id.question)
        infoButton.setOnClickListener {
            QnaInfoDialog().show(parentFragmentManager, "QnaInfoDialog")
        }
    }

}