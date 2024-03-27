package com.example.crud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crud.databinding.ActivityLihatBinding
import com.example.crud.factory.AccountModelFactory
import com.example.crud.firebase.Firestore
import com.example.crud.helper.AccountPreferences
import com.example.crud.helper.data.DataItem
import com.example.crud.helper.dataStoreAccount
import com.example.crud.model.AccountViewModel
import com.google.firebase.auth.FirebaseAuth

class LihatActivity : AppCompatActivity() {
//    lateinit var auth: FirebaseAuth
    lateinit var binding: ActivityLihatBinding
//    lateinit var dataModel: DataModel
    private lateinit var firestore: Firestore
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var listDataItem: ArrayList<DataItem>
    lateinit var dataAdapter: DataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLihatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        firestore = Firestore.instance
        listDataItem = ArrayList<DataItem>()

        val pref = AccountPreferences.getInstance(application.dataStoreAccount)
        accountViewModel = ViewModelProvider(this, AccountModelFactory(pref)).get(AccountViewModel::class.java)

        binding.LIImg1.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }

        accountViewModel.getUidData().observe(this){
            firestore.getDocument(it)
                .collection("order")
                .get()
                .addOnSuccessListener { result ->
                    listDataItem.clear()
                    for (document in result){
                        val orderData = document.data
                        val orderId = document.id

                        listDataItem.add(DataItem(
                            id = orderId,
                            tanggal = orderData["tanggal"].toString(),
                            namatoko = orderData["namatoko"].toString(),
                            namasales = orderData["namasales"].toString(),
                            kategori = orderData["kategori"].toString(),
                            namabarang = orderData["namabarang"].toString(),
                            qty = orderData["qty"].toString().toInt(),
                        ))
                    }

                    showRecycleList()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()
                    Log.e("Error get data LihatActivity", e.message.toString())
                }
        }

//        val dataList = mutableListOf<DataModel>()

//        val adapter = DataAdapter(dataList)
//        binding.recyclerView.adapter = adapter
//        binding.recyclerView.layoutManager = LinearLayoutManager(this)

//        val tanggal = intent.getStringExtra("tanggal") ?: ""
//        val namatoko = intent.getStringExtra("namatoko") ?: ""
//        val namasales = intent.getStringExtra("namasales") ?: ""
//        val kategori = intent.getStringExtra("kategori") ?: ""
//        val namabarang = intent.getStringExtra("namabarang") ?: ""
//        val qty = intent.getStringExtra("qty") ?: ""

//        dataList.add(DataModel(tanggal, namatoko, namasales, kategori, namabarang, qty))

//        adapter.notifyDataSetChanged()
    }

    private fun showRecycleList(){
        val recycler = binding.recyclerView
        recycler.layoutManager = LinearLayoutManager(this)
        dataAdapter = DataAdapter(listDataItem)
        recycler.adapter = dataAdapter
        dataAdapter.setOnClickCallback(object : DataAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DataItem) {
                Toast.makeText(this@LihatActivity, "anda menekan ${data.namatoko}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}