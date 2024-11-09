package com.dicoding.picodiploma.loginwithanimation.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.picodiploma.loginwithanimation.data.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.response.NewStoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.StoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class StoryRepository(private val apiService: ApiService) {

    suspend fun uploadStory(
        description: RequestBody,
        photo: MultipartBody.Part,
        token: String
    ): Response<NewStoryResponse> {
        return apiService.uploadStory(description, photo, token)
    }

    suspend fun getStoriesWithLocation(bearerToken: String): StoryResponse {
        return apiService.getStoriesWithLocation(bearerToken)
    }

    fun getStoriesStream(token: String): Flow<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { StoryPagingSource(apiService, token) }
        ).flow // Menggunakan .flow untuk mendapatkan Flow<PagingData<ListStoryItem>>
    }
}