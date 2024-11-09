package com.dicoding.picodiploma.loginwithanimation.view.maps

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityMapsBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.data.repository.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.ApiService
import com.dicoding.picodiploma.loginwithanimation.di.Injection
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.view.login.dataStore
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val viewModel: MapsViewModel by viewModels {
        ViewModelFactory(
            userRepository = Injection.provideRepository(this),
            storyRepository = StoryRepository(ApiService.create())
        )
    }

    private val locationPermissionRequestCode = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Menginisialisasi peta
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        checkLocationPermission() // Memeriksa izin lokasi

        // Pindahkan kamera ke Indonesia (Jakarta) saat peta siap
        val indonesiaLatLng = LatLng(-6.2088, 106.8456) // Koordinat Jakarta
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(indonesiaLatLng, 5f)) // Zoom level 5 untuk melihat area yang lebih luas

        // Mengambil token dari UserPreference dan menggunakan untuk memanggil getStories
        val userPreference = UserPreference.getInstance(dataStore)
        lifecycleScope.launch {
            userPreference.getSession().collectLatest { userModel ->
                val token = userModel.token
                val bearerToken = "Bearer $token" // Menambahkan "Bearer " sebelum token
                viewModel.fetchStoriesWithLocation(bearerToken) // Memanggil fungsi dengan token yang benar
                viewModel.storiesWithLocation.observe(this@MapsActivity) { stories ->
                    addMarkersToMap(stories) // Menambahkan marker ke peta
                }
            }
        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionRequestCode)
        } else {
            getMyLocation() // Izin sudah diberikan, dapatkan lokasi
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionRequestCode) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getMyLocation() // Izin diberikan, dapatkan lokasi
            } else {
                // Izin ditolak, tampilkan pesan
            }
        }
    }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true // Menampilkan lokasi pengguna di peta
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val userLatLng = LatLng(location.latitude, location.longitude)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15f)) // Pindahkan kamera ke lokasi pengguna
                }
            }
        }
    }

    private fun addMarkersToMap(stories: List<ListStoryItem>) {
        if (stories.isNotEmpty()) {
            stories.forEach { story ->
                val lat = story.lat ?: run {
                    Log.w("MapsActivity", "Latitude is null for story: ${story.name}")
                    return@forEach // Jika lat null, lewati
                }
                val lon = story.lon ?: run {
                    Log.w("MapsActivity", "Longitude is null for story: ${story.name}")
                    return@forEach // Jika lon null, lewati
                }
                val latLng = LatLng(lat, lon)
                Log.d("MapsActivity", "Adding marker for story: ${story.name} at ($lat, $lon)")
                mMap.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title(story.name)
                        .snippet(story.description)
                )
            }
            // Pindahkan kamera ke marker pertama setelah semua marker ditambahkan
            val firstStory = stories[0]
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(firstStory.lat ?: 0.0, firstStory.lon ?: 0.0), 10f))
        } else {
            Log.w("MapsActivity", "No stories available to display markers.")
        }
    }

    private fun setMapStyle() {
        // Implementasi untuk mengatur gaya peta
        // Misalnya, menggunakan JSON untuk mengatur gaya peta
    }
}