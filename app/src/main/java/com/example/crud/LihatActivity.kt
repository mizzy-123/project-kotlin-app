package com.example.crud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crud.databinding.ActivityLihatBinding
import com.google.firebase.auth.FirebaseAuth

class LihatActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var binding: ActivityLihatBinding
    lateinit var dataModel: DataModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLihatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.LIImg1.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }

        val dataList = mutableListOf<DataModel>()

        val adapter = DataAdapter(dataList)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        val tanggal = intent.getStringExtra("tanggal") ?: ""
        val namatoko = intent.getStringExtra("namatoko") ?: ""
        val namasales = intent.getStringExtra("namasales") ?: ""
        val kategori = intent.getStringExtra("kategori") ?: ""
        val namabarang = intent.getStringExtra("namabarang") ?: ""
        val qty = intent.getStringExtra("qty") ?: ""

        dataList.add(DataModel(tanggal, namatoko, namasales, kategori, namabarang, qty))

        adapter.notifyDataSetChanged()
    }
}