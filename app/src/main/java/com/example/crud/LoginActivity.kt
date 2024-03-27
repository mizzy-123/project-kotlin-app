package com.example.crud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.crud.databinding.ActivityLoginBinding
import com.example.crud.databinding.ActivityRegisterBinding
import com.example.crud.factory.AccountModelFactory
import com.example.crud.helper.AccountPreferences
import com.example.crud.helper.dataStoreAccount
import com.example.crud.model.AccountViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth

    lateinit var binding: ActivityLoginBinding
    private lateinit var accountViewModel: AccountViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        val pref = AccountPreferences.getInstance(application.dataStoreAccount)
        accountViewModel = ViewModelProvider(this, AccountModelFactory(pref)).get(AccountViewModel::class.java)


        binding.btnLogin.setOnClickListener {
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

        binding.btnLogin.text = "Loading..."
        binding.btnLogin.isEnabled = false

        if (email.isEmpty() || password.isEmpty()) {
            binding.edtEmail.error = "Email tidak boleh kosong"
            binding.edtKatasandi.error = "Kata sandi tidak boleh kosong"
//            return
        } else {
            binding.edtEmail.error = null
            binding.edtKatasandi.error = null

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.edtEmail.error = "Email tidak valid"
//                return
            }

            if (password.length < 6) {
                binding.edtKatasandi.error = "Kata sandi minimal 6 karakter"
//                return
            }
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) {
                    if (it.isSuccessful) {
//                        Toast.makeText(this, "Selamat Datang $email", Toast.LENGTH_SHORT).show()
                        val user = FirebaseAuth.getInstance().currentUser
                        val uid = user?.uid
                        if (uid != null) {
                            // Lakukan sesuatu dengan UID pengguna, misalnya, menyimpannya ke database atau menggunakan untuk tujuan tertentu
                            accountViewModel.saveAccount(email, uid)
                            val intent = Intent(this@LoginActivity, MenuActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            Toast.makeText(this@LoginActivity, "Login berhasil", Toast.LENGTH_SHORT).show()
                        } else {
                            // Pengguna tidak ditemukan, tangani kasus ini jika diperlukan
                            Toast.makeText(this, "data tidak ketemu", Toast.LENGTH_SHORT).show()
                            binding.btnLogin.text = "LOGIN"
                            binding.btnLogin.isEnabled = true
                        }
                    } else {
                        Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                        binding.btnLogin.text = "LOGIN"
                        binding.btnLogin.isEnabled = true

                    }
                }

        }

    }
}