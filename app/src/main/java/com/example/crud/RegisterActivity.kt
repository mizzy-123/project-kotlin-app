package com.example.crud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.crud.databinding.ActivityRegisterBinding
import com.example.crud.factory.AccountModelFactory
import com.example.crud.helper.AccountPreferences
import com.example.crud.helper.dataStoreAccount
import com.example.crud.model.AccountViewModel
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var binding: ActivityRegisterBinding
    private lateinit var accountViewModel: AccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()

        val pref = AccountPreferences.getInstance(application.dataStoreAccount)
        accountViewModel = ViewModelProvider(this, AccountModelFactory(pref)).get(AccountViewModel::class.java)


        binding.RBtn1.setOnClickListener {
            register()
        }

        binding.RImg1.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.txtLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun register() {
        val email = binding.edtEmail.text.toString()
        val password = binding.edtKatasandi.text.toString()
        val confirmPassword = binding.edtKonfrm.text.toString()

        binding.RBtn1.text = "Loading..."
        binding.RBtn1.isEnabled = false
        if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            binding.edtEmail.error = "Email tidak boleh kosong"
            binding.edtKatasandi.error = "Kata sandi tidak boleh kosong"
            binding.edtKonfrm.error = "Konfirmasi kata sandi tidak boleh kosong"
            return
        } else {
            binding.edtEmail.error = null
            binding.edtKatasandi.error = null
            binding.edtKonfrm.error = null

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.edtEmail.error = "Email tidak valid"
                return
            }

            if (password.length < 6) {
                binding.edtKatasandi.error = "Kata sandi minimal 6 karakter"
                return
            }

            if (password != confirmPassword) {
                binding.edtKonfrm.error = "Kata sandi tidak sama"
                return

            }
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) {
                    if (it.isSuccessful) {
                        val user = it.result?.user
                        val uid = user?.uid

                        if (uid !== null){
                            accountViewModel.saveAccount(email, uid)
                            val intent = Intent(this@RegisterActivity, MenuActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }

                        Toast.makeText(this, "Registrasi berhasil", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                        binding.RBtn1.text = "REGISTER"
                        binding.RBtn1.isEnabled = true
                    }
                }
                .addOnFailureListener {
                    binding.RBtn1.text = "REGISTER"
                    binding.RBtn1.isEnabled = true
                }

        }

    }
}
