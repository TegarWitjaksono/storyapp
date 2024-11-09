package com.dicoding.picodiploma.loginwithanimation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.repository.AuthRepository
import com.dicoding.picodiploma.loginwithanimation.data.response.ErrorResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    fun register(name: String, email: String, password: String, onResult: (RegisterResponse) -> Unit) {
        viewModelScope.launch {
            try {
                val response = authRepository.register(name, email, password)
                onResult(response) // Panggil callback dengan response sukses
            } catch (e: HttpException) {
                // Mendapatkan pesan error
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message ?: "Terjadi kesalahan"
                onResult(RegisterResponse(error = true, message = errorMessage)) // Panggil callback dengan pesan error
            } catch (e: Exception) {
                onResult(RegisterResponse(error = true, message = e.message)) // Penanganan umum untuk error
            }
        }
    }

    fun login(email: String, password: String, onResult: (LoginResponse) -> Unit) {
        viewModelScope.launch {
            try {
                val response = authRepository.login(email, password)
                onResult(response) // Panggil callback dengan response sukses
            } catch (e: HttpException) {
                // Mendapatkan pesan error
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                val errorMessage = errorBody.message ?: "Terjadi kesalahan"
                onResult(LoginResponse(error = true, message = errorMessage)) // Panggil callback dengan pesan error
            } catch (e: Exception) {
                onResult(LoginResponse(error = true, message = e.message)) // Penanganan umum untuk error
            }
        }
    }

    // Tambahkan fungsi saveSession
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            authRepository.saveSession(user) // Pastikan AuthRepository memiliki fungsi ini
        }
    }
}
