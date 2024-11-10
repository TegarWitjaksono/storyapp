package com.dicoding.picodiploma.loginwithanimation.view.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.picodiploma.loginwithanimation.view.welcome.WelcomeActivity
import com.dicoding.picodiploma.loginwithanimation.R

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // Menampilkan splash screen selama 3 detik
        Handler().postDelayed({
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish() // Menutup SplashScreenActivity
        }, 3000) // 3000 ms = 3 detik
    }
}