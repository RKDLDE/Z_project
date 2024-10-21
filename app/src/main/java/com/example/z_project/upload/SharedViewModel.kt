package com.example.z_project.upload

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel: ViewModel() {
    val imageUri: MutableLiveData<Uri> = MutableLiveData()
}