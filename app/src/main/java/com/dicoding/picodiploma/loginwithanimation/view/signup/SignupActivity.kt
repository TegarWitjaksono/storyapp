package com.dicoding.picodiploma.loginwithanimation.view.signup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.repository.AuthRepository
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.ApiConfig
import com.dicoding.picodiploma.loginwithanimation.viewmodel.AuthViewModel
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivitySignupBinding
import com.dicoding.picodiploma.loginwithanimation.view.login.dataStore
import com.dicoding.picodiploma.loginwithanimation.view.welcome.WelcomeActivity

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiService = ApiConfig.getApiService(token = toString())
        val userPreferences = UserPreference.getInstance(dataStore)
        val authRepository = AuthRepository(apiService, userPreferences)
        authViewModel = AuthViewModel(authRepository)

        setupAction()
    }

    private fun setupAction() {
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            // Validasi input
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showAlert("Error", "Semua field harus diisi!")
                return@setOnClickListener
            }

            // Tampilkan ProgressBar
            binding.progressBar.visibility = android.view.View.VISIBLE

            // Panggil fungsi register
            authViewModel.register(name, email, password) { response ->
                // Sembunyikan ProgressBar setelah proses selesai
                binding.progressBar.visibility = android.view.View.GONE

                if (response.error == true) {
                    // Tampilkan pesan error
                    showAlert("Error", response.message ?: "Terjadi kesalahan saat registrasi")
                } else {
                    // Proses registrasi berhasil
                    showAlert("Sukses", "Akun berhasil dibuat!") {
                        // Pindah ke WelcomeActivity
                        val intent = Intent(this@SignupActivity, WelcomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }

    private fun showAlert(title: String, message: String, onPositive: (() -> Unit)? = null) {
        AlertDialog.Builder(this).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("OK") { _, _ -> onPositive?.invoke() }
            create()
            show()
        }
    }
}