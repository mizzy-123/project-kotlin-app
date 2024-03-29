package com.example.crud

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.crud.databinding.ActivityInputBinding
import com.google.firebase.auth.FirebaseAuth
import android.app.DatePickerDialog
import java.util.Calendar
import android.app.TimePickerDialog
import android.util.Log
import android.widget.EditText
import android.widget.Spinner
import androidx.lifecycle.ViewModelProvider
import com.example.crud.factory.AccountModelFactory
import com.example.crud.firebase.Firestore
import com.example.crud.helper.AccountPreferences
import com.example.crud.helper.data.DataItem
import com.example.crud.helper.data.DataOrder
import com.example.crud.helper.dataStoreAccount
import com.example.crud.model.AccountViewModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions

class InputActivity : AppCompatActivity() {
    lateinit var binding: ActivityInputBinding

    private lateinit var firestore: Firestore
    private lateinit var accountViewModel: AccountViewModel
    var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        firestore = Firestore.instance

        val pref = AccountPreferences.getInstance(application.dataStoreAccount)
        accountViewModel = ViewModelProvider(this, AccountModelFactory(pref)).get(AccountViewModel::class.java)

        val bundle: Bundle? = intent.extras
        if (bundle !== null){
            val id = bundle.getString("id", "")
            this.id = id
            val namatoko = bundle.getString("namatoko", "")
            val qty = bundle.getInt("qty", 0)
            val kategori = bundle.getString("kategori", "")
            val namabarang = bundle.getString("namabarang", "")
            val namasales = bundle.getString("namasales", "")
            val tanggal = bundle.getString("tanggal", "")

            binding.also {
                it.tvDate.setText(tanggal)
                it.edtNmTk.setText(namatoko)

                val salesArray = resources.getStringArray(R.array.Sales)
                val indexOfSales = salesArray.indexOf(namasales)
                it.spNmSls.setSelection(indexOfSales)

                val kategoryArray = resources.getStringArray(R.array.Kategori)
                val indexOfKategory = kategoryArray.indexOf(kategori)
                it.spKtBrg.setSelection(indexOfKategory)

                it.edtNmBrg.setText(namabarang)
                it.edtQtyBrg.setText(qty.toString())
                it.ITxt1.text = "UPDATE ORDERAN"
            }
        }

        binding.tvDate.setOnClickListener { showDatePickerDialog() }

        binding.btnSimpan.setOnClickListener {
            if (id != ""){
                showUpdateDialog(this@InputActivity)
            } else {
                showOrderDialog(this@InputActivity)
            }
        }

        binding.btnBatal.setOnClickListener {
            finish()
        }

        binding.IImg1.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
        }
    }

    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                val date = "$dayOfMonth/${month + 1}/$year"
                binding.tvDate.text = date
            },
            Calendar.getInstance()[Calendar.YEAR],
            Calendar.getInstance()[Calendar.MONTH],
            Calendar.getInstance()[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.show()
    }

    private fun showOrderDialog(context: InputActivity) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Konfirmasi Orderan")
        builder.setMessage("Apakah Anda yakin ingin menyimpan data ini?")
        builder.setPositiveButton("YA") { _, _ ->
            val tanggal = binding.tvDate.text.toString()
            val namatoko = binding.edtNmTk.text.toString()
            val namasales = binding.spNmSls.selectedItem.toString()
            val kategori = binding.spKtBrg.selectedItem.toString()
            val namabarang = binding.edtNmBrg.text.toString()
            val qty = binding.edtQtyBrg.text.toString().toInt()

            if (tanggal.isEmpty() || namatoko.isEmpty() || namasales.isEmpty() || kategori.isEmpty() ||
                namabarang.isEmpty() || qty <= 0
            ) {
                Toast.makeText(context, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
            } else {

                try {
                    binding.btnSimpan.text = "Loading..."
                    binding.btnSimpan.isEnabled = false
                    accountViewModel.getUidData().observe(this){
                        firestore.getDocument(it).collection("order").add(DataOrder(
                            tanggal = tanggal,
                            namatoko = namatoko,
                            namasales = namasales,
                            kategori = kategori,
                            namabarang = namabarang,
                            qty = qty
                        )).addOnSuccessListener {
                            Toast.makeText(this, "Berhasil ditambah", Toast.LENGTH_SHORT).show()
                            binding.btnSimpan.text = "SIMPAN"
                            binding.btnSimpan.isEnabled = true

                        }.addOnFailureListener { e ->
                            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                            Log.e("failure_add_firestore", e.message.toString())
                            binding.btnSimpan.text = "SIMPAN"
                            binding.btnSimpan.isEnabled = true
                        }
                    }


                } catch (e: Exception){
                    Log.e("add_order", e.message.toString())
                    binding.btnSimpan.text = "SIMPAN"
                    binding.btnSimpan.isEnabled = true
                }

            }
        }
        builder.setNegativeButton("BATAL") { _, _ ->
            // Handle cancel button click
        }
        builder.show()
    }

    private fun showUpdateDialog(context: InputActivity) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Konfirmasi Orderan")
        builder.setMessage("Apakah Anda yakin ingin update data ini?")
        builder.setPositiveButton("YA") { _, _ ->
            val tanggal = binding.tvDate.text.toString()
            val namatoko = binding.edtNmTk.text.toString()
            val namasales = binding.spNmSls.selectedItem.toString()
            val kategori = binding.spKtBrg.selectedItem.toString()
            val namabarang = binding.edtNmBrg.text.toString()
            val qty = binding.edtQtyBrg.text.toString().toInt()

            if (tanggal.isEmpty() || namatoko.isEmpty() || namasales.isEmpty() || kategori.isEmpty() ||
                namabarang.isEmpty() || qty <= 0
            ) {
                Toast.makeText(context, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
            } else {

                try {

                    binding.btnSimpan.text = "Loading..."
                    binding.btnSimpan.isEnabled = false
                    accountViewModel.getUidData().observe(this){
                        firestore.getDocument(it).collection("order")
                            .document(id)
                            .set(DataOrder(
                                tanggal = tanggal,
                                namatoko = namatoko,
                                namasales = namasales,
                                kategori = kategori,
                                namabarang = namabarang,
                                qty = qty
                            ))
                            .addOnSuccessListener {
                                Toast.makeText(this, "Berhasil di update", Toast.LENGTH_SHORT).show()
                                binding.btnSimpan.text = "SIMPAN"
                                binding.btnSimpan.isEnabled = true
                            }
                            .addOnFailureListener { e ->
                                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                                Log.e("failure_add_firestore", e.message.toString())
                                binding.btnSimpan.text = "SIMPAN"
                                binding.btnSimpan.isEnabled = true
                            }
                    }


                } catch (e: Exception){
                    Log.e("add_order", e.message.toString())
                    binding.btnSimpan.text = "SIMPAN"
                    binding.btnSimpan.isEnabled = true
                }

            }
        }
        builder.setNegativeButton("BATAL") { _, _ ->

        }
        builder.show()
    }
}