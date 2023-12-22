package com.example.latihanp12_firebase.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.latihanp12_firebase.databinding.ActivityDashboardBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var adapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        fetchData()

        binding.btAddMenu.setOnClickListener {
            // Launch TambahMakananActivity
            startActivity(Intent(this, TambahItemActivity::class.java))
        }
    }

    private fun setupRecyclerView() {
        // Pass itemClickListener when creating ItemAdapter
        adapter = ItemAdapter(mutableListOf()) { position ->
            val itemDiklik = adapter.itemList[position]

            // Handle item click as needed
            Toast.makeText(this, "Diklik: ${itemDiklik.nama}", Toast.LENGTH_SHORT).show()
        }
        binding.rvDashboard.adapter = adapter
        binding.rvDashboard.layoutManager = LinearLayoutManager(this)
    }

    private fun fetchData() {
        // Query your "Item_admin" collection
        val query: Query = db.collection("Item_admin")

        query.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val itemList = mutableListOf<Item>()
                for (document in task.result!!) {
                    val item = Item(
                        document.getString("nama") ?: "",
                        document.getString("kategori") ?: "",
                        document.getLong("jml_kalori")?.toInt() ?: 0
                    )
                    itemList.add(item)
                }
                adapter.updateItems(itemList)
            } else {
                // Handle failure
            }
        }
    }
}
