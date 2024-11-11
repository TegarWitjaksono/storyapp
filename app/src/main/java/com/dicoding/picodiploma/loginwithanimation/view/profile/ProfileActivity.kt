package com.dicoding.picodiploma.loginwithanimation.view.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.view.welcome.WelcomeActivity
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import androidx.activity.viewModels
import com.dicoding.picodiploma.loginwithanimation.view.main.MainViewModel

class ProfileActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val logoutButton: Button = findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        viewModel.logout() // Panggil fungsi logout dari ViewModel
        startActivity(Intent(this, WelcomeActivity::class.java)) // Arahkan ke WelcomeActivity
        finish() // Tutup ProfileActivity
    }
}