package com.example.latihanp12_firebase.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.latihanp12_firebase.R
import com.example.latihanp12_firebase.user.db.ItemUser

class RoomDatabaseAdapter( var itemList: List<ItemUser>) :
    RecyclerView.Adapter<RoomDatabaseAdapter.ItemUserViewHolder>() {

    // Click listener interface
    private var onItemClickListener: ((Int) -> Unit)? = null

    // Set the click listener
    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    // ViewHolder class
    class ItemUserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaTextView: TextView = itemView.findViewById(R.id.txt_nama)
        val kategoriTextView: TextView = itemView.findViewById(R.id.txt_kategori)
        val kaloriTextView: TextView = itemView.findViewById(R.id.txt_kalori)
    }

    // Create ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemUserViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_makanan, parent, false)

        // Set the click listener for each item
        return ItemUserViewHolder(itemView).apply {
            itemView.setOnClickListener {
                onItemClickListener?.invoke(adapterPosition)
            }
        }
    }

    // Bind data to ViewHolder
    override fun onBindViewHolder(holder: ItemUserViewHolder, position: Int) {
        val currentItem = itemList[position]

        holder.namaTextView.text = currentItem.nama
        holder.kategoriTextView.text = currentItem.kategori
        holder.kaloriTextView.text = currentItem.kalori.toString()
    }

    // Get the total number of items
    override fun getItemCount() = itemList.size

    // Update the list of items
    fun updateItems(newItemList: List<ItemUser>) {
        itemList = newItemList
        notifyDataSetChanged()
    }
}
