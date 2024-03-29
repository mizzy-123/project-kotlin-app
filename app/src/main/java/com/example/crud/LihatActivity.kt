package com.example.crud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
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
    lateinit var binding: ActivityLihatBinding
    private lateinit var firestore: Firestore
    private lateinit var accountViewModel: AccountViewModel
    private lateinit var listDataItem: ArrayList<DataItem>
    private lateinit var listDataItemOriginal: ArrayList<DataItem>
    lateinit var dataAdapter: DataAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLihatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        firestore = Firestore.instance
        listDataItem = ArrayList<DataItem>()
        listDataItemOriginal = ArrayList<DataItem>()

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
                        val dataItem = DataItem(
                            id = orderId,
                            tanggal = orderData["tanggal"].toString(),
                            namatoko = orderData["namatoko"].toString(),
                            namasales = orderData["namasales"].toString(),
                            kategori = orderData["kategori"].toString(),
                            namabarang = orderData["namabarang"].toString(),
                            qty = orderData["qty"].toString().toInt(),
                        )

                        listDataItem.add(dataItem)
                        listDataItemOriginal.add(dataItem)
                    }

                    showRecycleList()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, e.message.toString(), Toast.LENGTH_SHORT).show()
                    Log.e("Error get data LihatActivity", e.message.toString())
                }
        }

        binding.svSearch.clearFocus()
        binding.svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    try {
                        filter(newText)
                    } catch (e: Exception){
                        Toast.makeText(this@LihatActivity, "Ops.. something wrong", Toast.LENGTH_SHORT).show()
                        Log.e("search", e.message.toString())
                    }
                }
                return true
            }
        })
    }

    private fun showRecycleList(){
        val recycler = binding.recyclerView
        recycler.layoutManager = LinearLayoutManager(this)
        dataAdapter = DataAdapter(listDataItem)
        recycler.adapter = dataAdapter
        dataAdapter.setOnClickCallback(object : DataAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DataItem) {
                val intent = Intent(this@LihatActivity, DetailActivity::class.java)
                intent.also {
                    it.putExtra("id", data.id)
                    it.putExtra("namatoko", data.namatoko)
                    it.putExtra("qty", data.qty)
                    it.putExtra("kategori", data.kategori)
                    it.putExtra("namabarang", data.namabarang)
                    it.putExtra("namasales", data.namasales)
                    it.putExtra("tanggal", data.tanggal)
                }

                startActivity(intent)
            }
        })
    }

    private fun filter(newText: String?) {
        val listM: ArrayList<DataItem> = ArrayList()
        for (item in listDataItemOriginal) {
            if (item.namatoko.lowercase().contains(newText?.lowercase() ?: "") ||
                item.namabarang.lowercase().contains(newText?.lowercase() ?: "")
            ) {
                listM.add(item)
            }
        }
        if (listM.isEmpty()) {
//            Toast.makeText(this@LihatActivity, "Not found", Toast.LENGTH_SHORT).show()
        } else {
            dataAdapter.setListDataSearch(listM)
        }
    }
}