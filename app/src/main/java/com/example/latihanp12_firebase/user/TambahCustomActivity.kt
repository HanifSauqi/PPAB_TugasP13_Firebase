package com.example.latihanp12_firebase.user

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.latihanp12_firebase.databinding.ActivityTambahCustomBinding
import com.example.latihanp12_firebase.user.db.ItemDao
import com.example.latihanp12_firebase.user.db.ItemRoomDatabase
import com.example.latihanp12_firebase.user.db.ItemUser
import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class TambahCustomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTambahCustomBinding
    private lateinit var itemRoomDatabase: ItemRoomDatabase

    private lateinit var executorService: ExecutorService
    private lateinit var itemDao: ItemDao
    private lateinit var currentUserUid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahCustomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the current user's UID
        currentUserUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

        // Inisialisasi Room Database
        itemRoomDatabase = ItemRoomDatabase.getDatabase(this)!!

        // Isi data kategori spinner
        val kategoriArray = arrayOf("Makanan", "Minuman", "Snack", "Buah", "Sayur", "Olahraga")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, kategoriArray)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerKategori.adapter = adapter

        // Inisialisasi DAO
        itemDao = itemRoomDatabase.itemDao()

        // Inisialisasi ExecutorService
        executorService = Executors.newSingleThreadExecutor()

        binding.btnTambahItem.setOnClickListener {
            tambahItem()
        }
    }

    private fun tambahItem() {
        val nama = binding.txtNama.text.toString()
        val kategori = binding.spinnerKategori.selectedItem.toString()
        val kalori = binding.txtJmlKalori.text.toString().toInt()

        // Validasi input, misalnya cek apakah nama dan kategori tidak kosong
        if (nama.isNotEmpty() && kategori.isNotEmpty()) {
            // Tambahkan item ke Room Database
            val newItem = ItemUser(nama = nama, kategori = kategori, kalori = kalori, uid = currentUserUid)

            insert(newItem)

            runOnUiThread {
                // Tampilkan toast ketika data berhasil disimpan
                Toast.makeText(this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show()
            }

            Log.d("TambahCustomActivity", "Nama: $nama, Kategori: $kategori, Kalori: $kalori, UID: $currentUserUid")

            finish() // Tutup activity setelah menambahkan item
        } else {
            // Handle error jika input tidak valid
            // Misalnya, tampilkan pesan kesalahan atau lakukan aksi lain
        }
    }

    private fun insert(itemUser: ItemUser) {
        executorService.execute {
            itemDao.insert(itemUser)
        }
    }
}
