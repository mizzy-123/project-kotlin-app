package com.example.crud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.widget.Toast
import com.example.crud.databinding.ActivityLoginBinding
import com.example.crud.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth

    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
            login()
        }

        binding.LImg1.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.txtRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun login() {
        val email = binding.edtEmail.text.toString()
        val password = binding.edtKatasandi.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            binding.edtEmail.error = "Email tidak boleh kosong"
            binding.edtKatasandi.error = "Kata sandi tidak boleh kosong"
            return
        } else {
            binding.edtEmail.error = null
            binding.edtKatasandi.error = null

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.edtEmail.error = "Email tidak valid"
                return
            }

            if (password.length < 6) {
                binding.edtKatasandi.error = "Kata sandi minimal 6 karakter"
                return
            }
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Selamat Datang $email", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()

                    }
                }

        }

    }
}