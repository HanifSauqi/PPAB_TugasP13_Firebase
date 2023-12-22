package com.example.latihanp12_firebase

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.latihanp12_firebase.databinding.ActivityMainBinding
import com.example.latihanp12_firebase.user.HistoryFragment
import com.example.latihanp12_firebase.user.HomeFragment
import com.example.latihanp12_firebase.user.ProfileFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Menambahkan listener untuk BottomNavigationView
        binding.bottomNavigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_history -> {
                    loadFragment(HistoryFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_home -> {
                    loadFragment(HomeFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_profile -> {
                    loadFragment(ProfileFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                else -> return@setOnNavigationItemSelectedListener false
            }
        }

        // Menampilkan fragment default saat aplikasi pertama kali dijalankan
        loadFragment(HomeFragment())
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
