package com.example.latihanp12_firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import com.example.latihanp12_firebase.loginregister.LoginFragment
import com.example.latihanp12_firebase.loginregister.RegisterFragment
import com.example.latihanp12_firebase.loginregister.TabLayoutAdapter

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        // Get references to the TabLayout and ViewPager
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val viewPager = findViewById<ViewPager>(R.id.viewPager)

        // Create an instance of the TabAdapter
        val adapter = TabLayoutAdapter(
            fragmentManager = supportFragmentManager,
            fragments = listOf(
                LoginFragment(),
                RegisterFragment()
            ),
            titles = listOf(
                "Login",
                "Register"
            )
        )

        // Set the adapter to the ViewPager
        viewPager.adapter = adapter

        // Connect the TabLayout to the ViewPager
        tabLayout.setupWithViewPager(viewPager)
    }
}