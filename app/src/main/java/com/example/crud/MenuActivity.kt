package com.example.crud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.crud.databinding.ActivityMenuBinding
import com.google.firebase.auth.FirebaseAuth

class MenuActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth

    lateinit var binding: ActivityMenuBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.MImg1.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.btnInput.setOnClickListener {
            startActivity(Intent(this, InputActivity::class.java))
        }
        binding.btnLihat.setOnClickListener {
            startActivity(Intent(this, LihatActivity::class.java))
        }
        binding.btnInsentif.setOnClickListener {
            startActivity(Intent(this, InsentifActivity::class.java))
        }
        binding.btnInfo.setOnClickListener {
            startActivity(Intent(this, InfoActivity::class.java))
        }
        binding.btnLogout.setOnClickListener {
            startActivity(Intent(this, LogoutActivity::class.java))
        }
    }
}