package com.example.latihanp12_firebase.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.latihanp12_firebase.databinding.ItemMakananBinding

class ItemAdapter(val itemList: MutableList<Item>, private val itemClickListener: (Int) -> Unit) :
    RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemMakananBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]

        holder.binding.txtNama.text = item.nama
        holder.binding.txtKategori.text = item.kategori
        holder.binding.txtKalori.text = item.jmlKalori.toString()

        // Set click listener for each item
        holder.itemView.setOnClickListener {
            itemClickListener.invoke(position)
        }
    }

    override fun getItemCount(): Int = itemList.size

    class ItemViewHolder(val binding: ItemMakananBinding) : RecyclerView.ViewHolder(binding.root)

    fun updateItems(newItemList: List<Item>) {
        itemList.clear()
        itemList.addAll(newItemList)
        notifyDataSetChanged()
    }
}
