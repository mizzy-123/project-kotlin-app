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
import android.widget.EditText
import android.widget.Spinner

class InputActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var binding: ActivityInputBinding

    private lateinit var tanggalInput: EditText
    private lateinit var namatokoInput: EditText
    private lateinit var namasalesInput: Spinner
    private lateinit var kategoriInput: Spinner
    private lateinit var namabarangInput: EditText
    private lateinit var QtyInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        val tanggalInput = binding.tvDate
        val namatokoInput = binding.edtNmTk
        val namasalesInput = binding.spNmSls
        val kategoriInput = binding.spKtBrg
        val namabarangInput = binding.edtNmBrg
        val QtyInput = binding.edtQtyBrg

        binding.tvDate.setOnClickListener { showDatePickerDialog() }

        binding.btnSimpan.setOnClickListener { showOrderDialog(this@InputActivity) }

        binding.btnBatal.setOnClickListener {
            startActivity(Intent(this, MenuActivity::class.java))
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
            val tanggal = tanggalInput.text.toString()
            val namatoko = namatokoInput.text.toString()
            val namasales = namasalesInput.selectedItem.toString()
            val kategori = kategoriInput.selectedItem.toString()
            val namabarang = namabarangInput.text.toString()
            val qty = QtyInput.text.toString().toInt()

            if (tanggal.isEmpty() || namatoko.isEmpty() || namasales.isEmpty() || kategori.isEmpty() ||
                namabarang.isEmpty() || qty <= 0
            ) {
                Toast.makeText(context, "Semua field harus diisi", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(context, LihatActivity::class.java)
                intent.putExtra("tanggal", tanggal)
                intent.putExtra("namatoko", namatoko)
                intent.putExtra("namasales", namasales)
                intent.putExtra("kategori", kategori)
                intent.putExtra("namabarang", namabarang)
                intent.putExtra("qty", qty)

                startActivity(intent)
            }
        }
        builder.setNegativeButton("BATAL") { _, _ ->
            // Handle cancel button click
        }
        builder.show()
    }
}