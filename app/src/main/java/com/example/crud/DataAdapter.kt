package com.example.crud

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.crud.databinding.RecyclerItemBinding
import com.example.crud.helper.data.DataItem

class DataAdapter(private val dataList: ArrayList<DataItem>) : RecyclerView.Adapter<DataAdapter.DataViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    internal fun setOnClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: DataItem)
    }

    inner class DataViewHolder(var binding: RecyclerItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        val binding = RecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DataViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val dataOrder = dataList[position]
        holder.binding.also {
            it.recnamatoko.text = dataOrder.namatoko
            it.recbarang.text = dataOrder.namabarang
            it.rectanggal.text = dataOrder.tanggal

        }

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(dataOrder)
        }
    }
}