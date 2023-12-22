package com.example.latihanp12_firebase.admin

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.latihanp12_firebase.databinding.ActivityTambahMakananBinding
import com.google.firebase.firestore.FirebaseFirestore

class TambahItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTambahMakananBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahMakananBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSpinner()

        binding.btnTambahItem.setOnClickListener {
            val nama = binding.txtNama.text.toString().trim()
            val kategori = binding.spinnerKategori.selectedItem.toString()
            val jmlKalori = Integer.parseInt(binding.txtJmlKalori.text.toString().trim())

            if (nama.isEmpty()) {
                Toast.makeText(this, "Nama makanan tidak boleh kosong", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val data = mutableMapOf(
                "nama" to nama,
                "kategori" to kategori,
                "jml_kalori" to jmlKalori
            )

            db.collection("Item_admin").add(data).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Makanan berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                    clearForm()
                } else {
                    Toast.makeText(this, "Gagal menambahkan makanan", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupSpinner() {
        // Populate spinner with categories (replace with your source)
        val kategoriList = listOf("Makanan", "Minuman", "Snack", "Buah", "Sayur", "Olahraga")
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            kategoriList
        )
        binding.spinnerKategori.adapter = adapter
    }

    private fun clearForm() {
        binding.txtNama.text = null
        binding.txtJmlKalori.text = null
        binding.spinnerKategori.setSelection(0)
    }
}