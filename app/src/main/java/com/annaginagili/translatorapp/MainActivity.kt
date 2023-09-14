package com.annaginagili.translatorapp

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment
import com.annaginagili.translatorapp.databinding.ActivityMainBinding
import com.annaginagili.translatorapp.databinding.FragmentHomeBinding
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var bottom: LinearLayout
    lateinit var chat: TextView
    lateinit var camera: TextView
    lateinit var home: ImageView
    lateinit var history: TextView
    lateinit var favorite: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottom = binding.bottom
        chat = binding.chat
        camera = binding.camera
        home = binding.home
        history = binding.history
        favorite = binding.favorite

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        val navController = navHostFragment.navController

        camera.setOnClickListener {
            navController.navigate(R.id.cameraFragment)
        }

        home.setOnClickListener {
            navController.navigate(R.id.homeFragment)
        }
    }
}