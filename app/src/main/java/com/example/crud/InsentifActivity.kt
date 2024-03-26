package com.example.crud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.crud.databinding.ActivityInsentifBinding
import com.google.firebase.auth.FirebaseAuth

class InsentifActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    lateinit var binding: ActivityInsentifBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsentifBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.InImg1.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }
    }
}