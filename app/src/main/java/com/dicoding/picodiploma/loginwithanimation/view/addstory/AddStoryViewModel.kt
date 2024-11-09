package com.dicoding.picodiploma.loginwithanimation.view.addstory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.repository.StoryRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val repository: StoryRepository) : ViewModel() {

    fun uploadStory(
        description: RequestBody,
        photo: MultipartBody.Part,
        token: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = repository.uploadStory(description, photo, token)
                if (response.isSuccessful) {
                    onSuccess()
                } else {
                    onError("Upload failed: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                onError("An error occurred: ${e.message}")
            }
        }
    }
}