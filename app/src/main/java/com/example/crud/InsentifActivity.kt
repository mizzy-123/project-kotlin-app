package com.example.crud

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.crud.databinding.ActivityInsentifBinding
import com.example.crud.factory.AccountModelFactory
import com.example.crud.firebase.Firestore
import com.example.crud.helper.AccountPreferences
import com.example.crud.helper.data.DataItem
import com.example.crud.helper.dataStoreAccount
import com.example.crud.model.AccountViewModel
import com.google.firebase.auth.FirebaseAuth

class InsentifActivity : AppCompatActivity() {

    private lateinit var firestore: Firestore
    lateinit var binding: ActivityInsentifBinding
    private lateinit var accountViewModel: AccountViewModel

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsentifBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        firestore = Firestore.instance
        val pref = AccountPreferences.getInstance(application.dataStoreAccount)
        accountViewModel = ViewModelProvider(this, AccountModelFactory(pref)).get(AccountViewModel::class.java)

        binding.InImg1.setOnClickListener {
            finish()
        }

        accountViewModel.getUidData().observe(this) { uid ->
            firestore.getDocument(uid)
                .collection("order")
                .get()
                .addOnSuccessListener { result ->
                    val kategoryArray = resources.getStringArray(R.array.Kategori)
                    val qtyList = mutableMapOf<String, Int>()

                    for (x in kategoryArray) {
                        qtyList[x] = 0 // Inisialisasi jumlah qty untuk setiap kategori
                    }

                    for (document in result) {
                        val orderData = document.data
                        val kategori = orderData["kategori"]?.toString() ?: "" // Handling null value

                        // Pastikan kategori yang ditemukan ada dalam array kategoryArray
                        if (kategori in kategoryArray) {
                            val qty = orderData["qty"]?.toString()?.toInt() ?: 0 // Handling null value

                            // Tambahkan jumlah qty ke dalam Map
                            qtyList[kategori] = qtyList[kategori]!! + qty
                        }
                    }

                    // Lakukan sesuatu dengan qtyList setelah data selesai diproses
                    binding.also {
                        val aki = (qtyList["Aki"] ?: 0) * 5000
                        val aerosol = (qtyList["Aerosol"] ?: 0) * 2500
                        val brakeshoe = (qtyList["Brakeshoe"] ?: 0) * 2500
                        val shock = (qtyList["Shock Absorber"] ?: 0) * 4000
                        val wiper = (qtyList["Wiper Blade"] ?: 0) * 1000

                        it.KatTxt6.text = "Rp.$aki"
                        it.KatTxt7.text = "Rp.$aerosol"
                        it.KatTxt8.text = "Rp.$brakeshoe"
                        it.KatTxt9.text = "Rp.$shock"
                        it.KatTxt10.text = "Rp.$wiper"
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()
                    Log.e("Error get data LihatActivity", e.message.toString())
                }
        }
    }
}