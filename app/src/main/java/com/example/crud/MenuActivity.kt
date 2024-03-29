package com.example.crud

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.example.crud.databinding.ActivityMenuBinding
import com.example.crud.factory.AccountModelFactory
import com.example.crud.helper.AccountPreferences
import com.example.crud.helper.dataStoreAccount
import com.example.crud.model.AccountViewModel
import com.google.firebase.auth.FirebaseAuth

class MenuActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth

    lateinit var binding: ActivityMenuBinding
    private lateinit var accountViewModel: AccountViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val pref = AccountPreferences.getInstance(application.dataStoreAccount)
        accountViewModel = ViewModelProvider(this, AccountModelFactory(pref)).get(AccountViewModel::class.java)

        binding.MImg1.setOnClickListener {
            finish()
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
//            startActivity(Intent(this, LogoutActivity::class.java))
            try {
                showAlertLogout()
            } catch (e: Exception) {
                Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()
                Log.e("logout", e.message.toString())
            }
        }
    }

    private fun showAlertLogout(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Are you sure wanna logout?")
            .setPositiveButton("Yes"){ dialog: DialogInterface, _:Int ->
                FirebaseAuth.getInstance().signOut()
                accountViewModel.deleteAccount()
//                finishAffinity()
                val intent = Intent(this@MenuActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                dialog.dismiss()
            }
            .setNegativeButton("No"){dialog: DialogInterface, _:Int ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .show()
    }
}