package com.example.latihanp12_firebase.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.latihanp12_firebase.admin.Item
import com.example.latihanp12_firebase.databinding.ItemMakananBinding

class FirestoreAdapter(private val items: MutableList<Item>) : RecyclerView.Adapter<FirestoreAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemMakananBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class ItemViewHolder(private val binding: ItemMakananBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            binding.txtNama.text = item.nama
            binding.txtKategori.text = item.kategori
            binding.txtKalori.text = item.jmlKalori.toString()
        }
    }
}

