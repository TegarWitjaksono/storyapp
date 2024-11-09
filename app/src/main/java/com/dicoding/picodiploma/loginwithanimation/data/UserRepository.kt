// UserRepository.kt
package com.dicoding.picodiploma.loginwithanimation.data

import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.response.StoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.ApiService // Import ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class UserRepository private constructor(
    private val userPreferences: UserPreference,
    private val apiService: ApiService // Gunakan ApiService yang diimpor
) {

    suspend fun saveSession(user: UserModel) {
        userPreferences.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreferences.getSession()
    }

    suspend fun logout() {
        userPreferences.logout()
    }

    suspend fun getUser(): UserModel {
        return userPreferences.getSession().first()
    }

    // Menambahkan metode untuk mengambil data cerita
    suspend fun getStories(token: String): StoryResponse {
        val response = apiService.getStories("Bearer $token") // Pastikan format token benar

        if (response.isSuccessful) {
            val storyResponse = response.body()
            if (storyResponse != null) {
                return storyResponse
            } else {
                throw Exception("Response body is null")
            }
        } else {
            throw Exception("Failed to load stories: ${response.message()}")
        }
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(
            userPreferences: UserPreference,
            apiService: ApiService // Tambahkan parameter untuk ApiService
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreferences, apiService).also { instance = it }
            }
    }
}
