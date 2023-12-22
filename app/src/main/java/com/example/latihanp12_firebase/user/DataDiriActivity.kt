package com.example.latihanp12_firebase.user

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.latihanp12_firebase.MainActivity
import com.example.latihanp12_firebase.databinding.ActivityDataDiriBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DataDiriActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDataDiriBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var uid: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataDiriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        uid = FirebaseAuth.getInstance().currentUser!!.uid

        binding.btnSimpan.setOnClickListener { onSimpanClick() }
    }

    private fun onSimpanClick() {
        val nama = binding.txtNama.text.toString()
        val tinggiBadan = binding.txtTinggiBadan.text.toString().toInt()
        val beratBadan = binding.txtBeratBadan.text.toString().toInt()
        val targetKalori = binding.txtTargetKalori.text.toString().toInt()

        val dataToUpdate = mutableMapOf(
            "nama" to nama,
            "tinggi_badan" to tinggiBadan,
            "berat_badan" to beratBadan,
            "target_kalori" to targetKalori
        )

        db.collection("users").document(uid).update(dataToUpdate as Map<String, Any>)
            .addOnSuccessListener {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to save data", Toast.LENGTH_SHORT).show()
            }
    }
}
