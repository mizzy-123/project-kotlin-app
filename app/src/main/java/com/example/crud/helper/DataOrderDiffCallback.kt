package com.example.crud.helper

import androidx.recyclerview.widget.DiffUtil
import com.example.crud.helper.data.DataItem

class DataOrderDiffCallback(
    private val oldList: ArrayList<DataItem>,
    private val newList: ArrayList<DataItem>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val new = newList[newItemPosition]
        val old = oldList[oldItemPosition]
        return old.namatoko == new.namatoko
    }
}