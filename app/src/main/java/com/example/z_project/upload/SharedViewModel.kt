package com.example.z_project.upload

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {
    val imageUri: MutableLiveData<Uri> = MutableLiveData()
    //val inputText: LiveData<String> get() = _inputText
    val inputText = MutableLiveData<String>()
    //private val _inputText = MutableLiveData<String>()
    fun setInputText(text: String) {
        inputText.value = text
    }
}