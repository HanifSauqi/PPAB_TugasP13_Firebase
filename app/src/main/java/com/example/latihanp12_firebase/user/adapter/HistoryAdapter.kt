package com.example.latihanp12_firebase.user.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.latihanp12_firebase.databinding.ItemHistoryBinding
import com.example.latihanp12_firebase.user.data.HistoryItem

class HistoryAdapter(val historyList: MutableList<HistoryItem>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding =
            ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val historyItem = historyList[position]

        holder.binding.txtNama.text = historyItem.namaItem
        holder.binding.txtKategori.text = historyItem.kategori
        holder.binding.txtKalori.text = historyItem.jmlKalori?.toString()
        holder.binding.txtTgl.text = historyItem.tanggal
    }

    override fun getItemCount(): Int = historyList.size

    inner class HistoryViewHolder(val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root)

    fun updateHistoryItems(newHistoryList: List<HistoryItem>) {
        historyList.clear()
        historyList.addAll(newHistoryList)
        notifyDataSetChanged()
    }
}