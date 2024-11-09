package com.dicoding.picodiploma.loginwithanimation.view.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.data.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.response.Story
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.RetrofitClient
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityDetailStoryBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.util.Log

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tampilkan ProgressBar
        binding.progressBar.visibility = View.VISIBLE

        // Ambil objek ListStoryItem dari intent
        val storyItem = intent.getParcelableExtra<ListStoryItem>("storyItem")

        // Set transition name untuk elemen yang di-animasi
        binding.storyImage.transitionName = "transition_story_image_${storyItem?.id}"

        if (storyItem != null) {
            // Jika storyItem ada, ambil ID dan token dari intent
            val storyId = storyItem.id
            val token = intent.getStringExtra("token")

            if (storyId != null && token != null) {
                fetchStoryDetail(storyId, token) // Panggil API untuk mendapatkan detail story
            } else {
                Toast.makeText(this, "ID story atau token tidak valid", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            }
        } else {
            // Jika storyItem tidak ada, ambil ID dari intent dan panggil API
            val storyId = intent.getStringExtra("storyId")
            val token = intent.getStringExtra("token")

            if (storyId != null && token != null) {
                fetchStoryDetail(storyId, token) // Panggil API untuk mendapatkan detail story
            } else {
                Toast.makeText(this, "ID story atau token tidak valid", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun displayStoryDetails(storyItem: Story) {
        // Sembunyikan ProgressBar setelah data dimuat
        binding.progressBar.visibility = View.GONE

        // Tampilkan data di UI
        binding.storyName.text = storyItem.name ?: "Nama tidak tersedia"
        binding.storyDescription.text = storyItem.description ?: "Deskripsi tidak tersedia"
        // Gunakan Glide untuk memuat gambar
        Glide.with(this)
            .load(storyItem.photoUrl)
            .into(binding.storyImage)
    }

    private fun fetchStoryDetail(storyId: String, token: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val apiService = RetrofitClient.getApiService()
                val response = apiService.getStoryDetail(storyId, "Bearer $token")

                // Log respons untuk debugging
                Log.d("DetailStoryActivity", "Response: $response")

                // Pindah ke Main Thread untuk update UI
                withContext(Dispatchers.Main) {
                    if (response.error == false) {
                        val storyDetail = response.story
                        if (storyDetail != null) {
                            displayStoryDetails(storyDetail)
                        } else {
                            Toast.makeText(this@DetailStoryActivity, "Detail story tidak ditemukan", Toast.LENGTH_SHORT).show()
                            binding.progressBar.visibility = View.GONE
                        }
                    } else {
                        Log.e("DetailStoryActivity", "Error message: ${response.message}")
                        Toast.makeText(this@DetailStoryActivity, "Gagal memuat detail story: ${response.message}", Toast.LENGTH_SHORT).show()
                        binding.progressBar.visibility = View.GONE
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@DetailStoryActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }
}