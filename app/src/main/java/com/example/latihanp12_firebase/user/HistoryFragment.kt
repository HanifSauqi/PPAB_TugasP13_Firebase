package com.example.latihanp12_firebase.user

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.latihanp12_firebase.databinding.FragmentHistoryBinding
import com.example.latihanp12_firebase.user.adapter.HistoryAdapter
import com.example.latihanp12_firebase.user.data.HistoryItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var adapter: HistoryAdapter
    private val db = FirebaseFirestore.getInstance()
    private val currentUserUid = FirebaseAuth.getInstance().currentUser!!.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        loadHistoryData()

        binding.btnAddKalori.setOnClickListener {
            val intent = Intent(requireActivity(), TambahKaloriActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        adapter = HistoryAdapter(mutableListOf())
        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHistory.adapter = adapter
    }

    private fun loadHistoryData() {
        db.collection("Kalori_harian")
            .whereEqualTo("uid", currentUserUid)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val historyList = mutableListOf<HistoryItem>()
                    for (document in task.result!!) {
                        val historyItem = HistoryItem(
                            document.getString("nama"),
                            document.getString("kategori"),
                            document.getLong("kalori")!!.toInt(),
                            document.getString("tanggal")
                        )
                        historyList.add(historyItem)
                    }
                    adapter.updateHistoryItems(historyList)
                } else {
                    // Handle failure
                }
            }
    }
}




