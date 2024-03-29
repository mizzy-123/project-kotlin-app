package com.example.crud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.crud.databinding.ActivityDetailBinding
import com.example.crud.factory.AccountModelFactory
import com.example.crud.firebase.Firestore
import com.example.crud.helper.AccountPreferences
import com.example.crud.helper.dataStoreAccount
import com.example.crud.model.AccountViewModel

class DetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailBinding
    private lateinit var firestore: Firestore
    private lateinit var accountViewModel: AccountViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        firestore = Firestore.instance
        val pref = AccountPreferences.getInstance(application.dataStoreAccount)
        accountViewModel = ViewModelProvider(this, AccountModelFactory(pref)).get(AccountViewModel::class.java)

        val bundle: Bundle? = intent.extras

        if (bundle != null){
            insertUpdateDelete(bundle)
        }
    }

    private fun insertUpdateDelete(bundle: Bundle){
        val id = bundle.getString("id", "")
        val namatoko = bundle.getString("namatoko", "")
        val qty = bundle.getInt("qty", 0)
        val kategori = bundle.getString("kategori", "")
        val namabarang = bundle.getString("namabarang", "")
        val namasales = bundle.getString("namasales", "")
        val tanggal = bundle.getString("tanggal", "")
        binding.also {
            it.KatTxt6.text = tanggal
            it.KatTxt7.text = namatoko
            it.KatTxt8.text = namasales
            it.KatTxt9.text = kategori
            it.KatTxt10.text = namabarang
            it.KatTxt12.text = qty.toString()

            accountViewModel.getUidData().observe(this){ uid ->
                it.btnDelete.setOnClickListener { btn ->
                    firestore.getDocument(uid)
                        .collection("order")
                        .document(id)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(this@DetailActivity, "Delete successfull", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@DetailActivity, LihatActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            startActivity(intent)
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this@DetailActivity, e.message.toString(), Toast.LENGTH_SHORT).show()
                            Log.e("delete_order", e.message.toString())
                        }
                }
            }


            it.btnUpdate.setOnClickListener { btn ->
                val intent = Intent(this@DetailActivity, InputActivity::class.java)
                intent.apply {
                    this.putExtra("id", id)
                    this.putExtra("namatoko", namatoko)
                    this.putExtra("qty", qty)
                    this.putExtra("kategori", kategori)
                    this.putExtra("namabarang", namabarang)
                    this.putExtra("namasales", namasales)
                    this.putExtra("tanggal", tanggal)
                }
                startActivity(intent)
            }
        }
    }
}