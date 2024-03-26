package com.example.crud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.crud.databinding.ActivityLogoutBinding
import com.google.firebase.auth.FirebaseAuth

class LogoutActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var binding: ActivityLogoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.LoImg1.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }
    }
}