package com.example.latihanp12_firebase.loginregister

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.latihanp12_firebase.R
import com.example.latihanp12_firebase.databinding.FragmentRegisterBinding
import com.example.latihanp12_firebase.user.DataDiriActivity
import com.example.latihanp12_firebase.user.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.btnRegister.setOnClickListener {
            val email = binding.edtEmailRegister.text.toString()
            val password = binding.edtPasswordRegister.text.toString()

            validateInput(email, password)
        }
    }

    private fun validateInput(email: String, password: String) {
        if (email.isEmpty()) {
            binding.edtEmailRegister.error = "Email Harus Diisi"
            binding.edtEmailRegister.requestFocus()
            return
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edtEmailRegister.error = "Email Tidak Valid"
            binding.edtEmailRegister.requestFocus()
            return
        } else if (password.isEmpty()) {
            binding.edtPasswordRegister.error = "Password Harus Diisi"
            binding.edtPasswordRegister.requestFocus()
            return
        } else if (password.length < 6) {
            binding.edtPasswordRegister.error = "Password Minimal 6 Karakter"
            binding.edtPasswordRegister.requestFocus()
            return
        } else {
            registerFirebase(email, password)
        }
    }

    private fun registerFirebase(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Register successful
                    val user = auth.currentUser

                    // Save user data to Firestore
                    val userData = User(user!!.uid, email, "user")
                    db.collection("users").document(user.uid).set(userData)
                        .addOnSuccessListener {
                            Toast.makeText(
                                requireActivity(),
                                "Register Berhasil",
                                Toast.LENGTH_SHORT
                            ).show()
                            // Navigate to DataDiriActivity using Intent
                            val intent = Intent(requireActivity(), DataDiriActivity::class.java)
                            startActivity(intent)
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                requireActivity(),
                                "Gagal menyimpan data user",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                } else {
                    // Register failed
                    Toast.makeText(
                        requireActivity(),
                        "Register Gagal: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
