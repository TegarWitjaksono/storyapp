package com.dicoding.picodiploma.loginwithanimation.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.repository.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.data.response.ListStoryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel(
    private val userRepository: UserRepository,
    private val storyRepository: StoryRepository
) : ViewModel() {

    private val _stories = MutableLiveData<List<ListStoryItem?>>()
    val stories: LiveData<List<ListStoryItem?>> get() = _stories

    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

    fun fetchStories(token: String) {
        viewModelScope.launch {
            try {
                val response = userRepository.getStories(token)
                if (response.error == false) {
                    _stories.postValue(response.listStory ?: emptyList())
                } else {
                    _stories.postValue(emptyList())
                }
            } catch (e: Exception) {
                _stories.postValue(emptyList())
            }
        }
    }

    fun getPagedStories(token: String): Flow<PagingData<ListStoryItem>> {
        return storyRepository.getStoriesStream(token).cachedIn(viewModelScope)
    }
}