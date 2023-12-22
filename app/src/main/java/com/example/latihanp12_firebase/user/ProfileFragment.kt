package com.example.latihanp12_firebase.user

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.latihanp12_firebase.MainActivity2
import com.example.latihanp12_firebase.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var uid: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        uid = auth.currentUser!!.uid

        fetchUserData()

        binding.btnLogout.setOnClickListener {
            btnLogout()
        }

        binding.btnDelete.setOnClickListener {
            deleteAccount()
        }
    }

    private fun fetchUserData() {
        db.collection("users").document(uid)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val nama = document.getString("nama")
                    val email = document.getString("email")
                    val tinggiBadan = document.getLong("tinggi_badan")
                    val beratBadan = document.getLong("berat_badan")
                    val targetKalori = document.getLong("target_kalori")

                    binding.txtNama.text = nama
                    binding.txtEmail.text = email
                    binding.txtTinggiBadan.text = tinggiBadan.toString()
                    binding.txtBeratBadan.text = beratBadan.toString()
                    binding.txtTargetKalori.text = targetKalori.toString()
                } else {
                    // Handle the case where the user document doesn't exist
                }
            }
            .addOnFailureListener { exception ->
                // Handle the failure to retrieve data
            }
    }

    private fun btnLogout() {
        auth.signOut()
        val intent = Intent(requireContext(), MainActivity2::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun deleteAccount() {
        val user = auth.currentUser

        user?.delete()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        "Account deleted successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(requireContext(), MainActivity2::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Failed to delete account: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    companion object {
        const val REQ_CAM = 100
    }
}
