package com.task.capstone_made

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.task.capstone_made.databinding.ActivitySplashscreenBinding

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private var _binding: ActivitySplashscreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashscreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val handler = Handler(mainLooper)
        handler.postDelayed({
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000L)
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

}