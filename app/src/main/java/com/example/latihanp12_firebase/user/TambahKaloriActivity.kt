package com.example.latihanp12_firebase.user

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.latihanp12_firebase.admin.Item
import com.example.latihanp12_firebase.admin.ItemAdapter
import com.example.latihanp12_firebase.databinding.ActivityTambahKaloriBinding
import com.example.latihanp12_firebase.user.adapter.HistoryAdapter
import com.example.latihanp12_firebase.user.data.HistoryItem
import com.example.latihanp12_firebase.user.db.ItemDao
import com.example.latihanp12_firebase.user.db.ItemRoomDatabase
import com.example.latihanp12_firebase.user.db.ItemUser
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class TambahKaloriActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTambahKaloriBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var adapter: ItemAdapter
    private lateinit var adapterDao: RoomDatabaseAdapter

    private lateinit var auth: FirebaseAuth

    private lateinit var itemDao: ItemDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahKaloriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = ItemRoomDatabase.getDatabase(this)
        itemDao = db!!.itemDao()

        adapterDao = RoomDatabaseAdapter(emptyList())

        setupRecyclerView()
        fetchData()

        // Button listeners
        binding.btnTambahCustom.setOnClickListener {
            val intent = Intent(this, TambahCustomActivity::class.java)
            startActivity(intent)
        }

        setupRecyclerViewDao()
        fetchDataDao()

    }
    private fun setupRecyclerView() {
        adapter = ItemAdapter(mutableListOf()) { position ->
            val itemDiklik = adapter.itemList[position]

            // Handle item click as needed
            Toast.makeText(this, "Diklik: ${itemDiklik.nama}", Toast.LENGTH_SHORT).show()

            // Kirim data ke Firestore
            lifecycleScope.launch {
                val db = FirebaseFirestore.getInstance()
                val docRef = db.collection("Kalori_harian").document()

                // Get the current user's UID
                val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

                // Get the current date and time
                val formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yy"))


                // Create a HashMap to store the data
                val dataToSave = hashMapOf(
                    "nama" to itemDiklik.nama,
                    "kategori" to itemDiklik.kategori,
                    "kalori" to itemDiklik.jmlKalori,
                    "uid" to uid,
                    "tanggal" to formattedDate
                )

                docRef.set(dataToSave)
                    .addOnSuccessListener {
                        Log.d(TAG, "Item saved to Firestore: ${itemDiklik.nama}")
                        Toast.makeText(this@TambahKaloriActivity, "Item tersimpan!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error saving item to Firestore", e)
                        Toast.makeText(this@TambahKaloriActivity, "Gagal menyimpan item", Toast.LENGTH_SHORT).show()
                    }
            }
        }
        binding.rvFs.adapter = adapter
        binding.rvFs.layoutManager = LinearLayoutManager(this)
    }






    private fun setupRecyclerViewDao() {
        // Pass itemClickListener when creating RoomDatabaseAdapter
        adapterDao = RoomDatabaseAdapter(mutableListOf())

        // Set item click listener directly in the adapter
        adapterDao.setOnItemClickListener { position ->
            val itemDiklik = adapterDao.itemList[position]

            // Handle item click as needed
            Toast.makeText(this, "Diklik: ${itemDiklik.nama}", Toast.LENGTH_SHORT).show()

            // Kirim itemDiklik yang sudah ditambahkan uid dan tanggal ke Firestore
            lifecycleScope.launch {
                val db = FirebaseFirestore.getInstance()
                val docRef = db.collection("Kalori_harian").document()

                // Get the current user's UID
                val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

                // Get the current date and time
                val formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yy"))


                // Create a HashMap to store the data
                val dataToSave = hashMapOf(
                    "nama" to itemDiklik.nama,
                    "kategori" to itemDiklik.kategori,
                    "kalori" to itemDiklik.kalori,
                    "uid" to uid,
                    "tanggal" to formattedDate
                )

                docRef.set(dataToSave)
                    .addOnSuccessListener {
                        Log.d(TAG, "Item saved to Firestore: ${itemDiklik.nama}")
                        Toast.makeText(this@TambahKaloriActivity, "Item tersimpan!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Log.w(TAG, "Error saving item to Firestore", e)
                        Toast.makeText(this@TambahKaloriActivity, "Gagal menyimpan item", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        binding.rvRdb.adapter = adapterDao
        binding.rvRdb.layoutManager = LinearLayoutManager(this)
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
    private fun fetchDataDao() {
        itemDao.allDataHarian.observe(this@TambahKaloriActivity) { items ->
            val itemList = mutableListOf<ItemUser>()

            auth = Firebase.auth
            val currentUserUid = auth.currentUser?.uid.toString()

            for (document in items) {
                if (currentUserUid == document.uid) {
                    val item = ItemUser(
                        uid = document.uid,
                        nama = document.nama,
                        kategori = document.kategori,
                        kalori = document.kalori
                    )
                    itemList.add(item)
                }

            }
            Log.d("List Item", itemList.toString())
            adapterDao.updateItems(itemList)
        }
    }

}
