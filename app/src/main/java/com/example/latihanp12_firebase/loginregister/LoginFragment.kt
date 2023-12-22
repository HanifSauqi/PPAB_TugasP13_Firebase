package com.example.latihanp12_firebase.loginregister

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.latihanp12_firebase.MainActivity
import com.example.latihanp12_firebase.R
import com.example.latihanp12_firebase.admin.DashboardActivity
import com.example.latihanp12_firebase.databinding.FragmentLoginBinding
import com.example.latihanp12_firebase.user.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val PREF_NAME = "MyPrefs"
    private val KEY_IS_LOGGED_IN = "isLoggedIn"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmailLogin.text.toString()
            val password = binding.edtPasswordLogin.text.toString()

            // Validasi email
            if (email.isEmpty()) {
                binding.edtEmailLogin.error = "Email Harus Diisi"
                binding.edtEmailLogin.requestFocus()
                return@setOnClickListener
            }

            // Validasi format email
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.edtPasswordLogin.error = "Email Tidak Valid"
                binding.edtPasswordLogin.requestFocus()
                return@setOnClickListener
            }

            // Validasi password
            if (password.isEmpty()) {
                binding.edtPasswordLogin.error = "Password Harus Diisi"
                binding.edtPasswordLogin.requestFocus()
                return@setOnClickListener
            }
            loginFirebase(email, password)
        }

        // Check if the user is already logged in
        val isLoggedIn = getLoginStatus()
        if (isLoggedIn) {
            navigateToHome()
        }
    }

    private fun loginFirebase(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Save the login status
                    saveLoginStatus(true)

                    val user = auth.currentUser

                    // Get user data from Firestore
                    db.collection("users").document(user!!.uid).get()
                        .addOnSuccessListener { documentSnapshot ->
                            // ... (existing code)

                            navigateToHome()
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                requireContext(),
                                "Gagal mengambil data pengguna",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Login gagal: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun navigateToHome() {
        val user = auth.currentUser
        if (user != null) {
            // Check the user role and navigate accordingly
            db.collection("users").document(user.uid).get()
                .addOnSuccessListener { documentSnapshot ->
                    val userData = documentSnapshot.toObject(User::class.java)
                    if (userData?.role == "admin") {
                        Toast.makeText(requireContext(), "Selamat datang admin", Toast.LENGTH_SHORT)
                            .show()
                        val intent = Intent(requireActivity(), DashboardActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Selamat datang ${user.email}",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(requireActivity(), MainActivity::class.java)
                        startActivity(intent)
                    }
                }
        }
    }

    private fun saveLoginStatus(isLoggedIn: Boolean) {
        val sharedPreferences =
            requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
        editor.apply()
    }

    private fun getLoginStatus(): Boolean {
        val sharedPreferences =
            requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
