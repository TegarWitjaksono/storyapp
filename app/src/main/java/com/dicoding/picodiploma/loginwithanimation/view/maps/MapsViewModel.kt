package com.dicoding.picodiploma.loginwithanimation.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.picodiploma.loginwithanimation.data.repository.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.data.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.response.StoryResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MapsViewModel(private val repository: StoryRepository) : ViewModel() {

    private val _storiesWithLocation = MutableLiveData<List<ListStoryItem>>()
    val storiesWithLocation: LiveData<List<ListStoryItem>> = _storiesWithLocation

    // Fetch stories with location
    fun fetchStoriesWithLocation(bearerToken: String) {
        viewModelScope.launch {
            try {
                val response: StoryResponse = repository.getStoriesWithLocation(bearerToken)
                val nonNullStories = response.listStory?.filterNotNull() ?: emptyList()
                _storiesWithLocation.value = nonNullStories
            } catch (e: Exception) {
                // Handle error by setting an empty list if there is an exception
                _storiesWithLocation.value = emptyList()
            }
        }
    }
}
