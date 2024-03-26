package com.example.crud

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DataAdapter(private val dataList: List<DataModel>) : RecyclerView.Adapter<DataAdapter.DataViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item, parent, false)
        return DataViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val currentData = dataList[position]
        holder.bind(currentData)
    }

    override fun getItemCount() = dataList.size

    inner class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tanggalTextView: TextView = itemView.findViewById(R.id.rectanggal)
        private val namatokoTextView: TextView = itemView.findViewById(R.id.recnamatoko)
        private val namabrgTextView: TextView = itemView.findViewById(R.id.recbarang)

        fun bind(data: DataModel) {
            tanggalTextView.text = data.tanggal
            namatokoTextView.text = data.namatoko
            namabrgTextView.text = data.namabrg
        }
    }
}