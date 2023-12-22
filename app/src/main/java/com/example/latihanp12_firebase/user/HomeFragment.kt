package com.example.latihanp12_firebase.user

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.latihanp12_firebase.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.latihanp12_firebase.NotifReceiver
import com.example.latihanp12_firebase.R


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val channelId = "TEST_NOTIF"
    private val notifId = 90

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        fetchTargetKalori()

        binding.btnNotif.setOnClickListener {
            val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_MUTABLE
            } else {
                0
            }

            val intent = Intent(requireContext(), NotifReceiver::class.java)
                .putExtra("Message", "Baca Selengkapnya")

            val pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, flag)

            val builder = NotificationCompat.Builder(requireContext(), channelId)
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle("Sisa Kalori Hari ini")
                .setContentText("Hello Word")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(0, "BacaNotif", pendingIntent)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notifChannel = NotificationChannel(channelId, "Notifku", NotificationManager.IMPORTANCE_DEFAULT)
                notificationManager.createNotificationChannel(notifChannel)
            }

            notificationManager.notify(notifId, builder.build())
        }

    }

    private fun fetchTargetKalori() {
        lifecycleScope.launch {
            val currentUserUid = auth.currentUser?.uid ?: ""

            db.collection("users")
                .whereEqualTo("uid", currentUserUid)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.isEmpty) {
                        binding.txtTargetKalori.text = "Target kalori belum diatur"
                    } else {
                        val targetKalori = documents.first().get("target_kalori").toString().toInt()
                        binding.txtTargetKalori.text = targetKalori.toString() + " Kkal"
                        val namaUser = documents.first().get("nama")
                        binding.txtNamaUser.text = namaUser.toString()

                        fetchKaloriHarian(targetKalori)
                    }
                }
                .addOnFailureListener { e ->
                    binding.txtTargetKalori.text = "Error fetching target kalori: ${e.message}"
                }
        }
    }

    private fun fetchKaloriHarian(targetKalori: Int) {
        lifecycleScope.launch {
            val currentUserUid = auth.currentUser?.uid ?: ""
            val today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yy"))

            db.collection("Kalori_harian")
                .whereEqualTo("uid", currentUserUid)
                .whereEqualTo("tanggal", today.toString())
                .get()
                .addOnSuccessListener { documents ->
                    var totalKalori = 0
                    for (document in documents) {
                        totalKalori += document.get("kalori").toString().toInt()
                    }
                    val sisaKalori = targetKalori - totalKalori
                    binding.txtSisaKalori.text = sisaKalori.toString() + " Kkal"

                    // Update progress chart and txt_persen
                    val progress = (100 - (sisaKalori * 100) / targetKalori).toInt()
                    binding.progressChart.progress = progress
                    binding.txtPersen.text = "$progress%"

                    showSisaKaloriNotification(sisaKalori) // Trigger notification
                }
                .addOnFailureListener { e ->
                    binding.txtSisaKalori.text = "Error fetching kalori harian: ${e.message}"
                }
        }
    }

    private fun showSisaKaloriNotification(sisaKalori: Int) {
        val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(requireContext(), channelId)
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle("Sisa Kalori Hari Ini")
            .setContentText("Sisa kalori Anda: $sisaKalori Kkal")
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        // Optionally, add a pending intent for notification action

        notificationManager.notify(notifId, builder.build())
    }

    private fun showNotification() {
        val channelId = "my_channel"
        val notificationId = 1

        val notificationManager =
            requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "My Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(requireContext(), this::class.java)
        val pendingIntent = PendingIntent.getActivity(
            requireContext(),
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(requireContext(), channelId)
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentTitle("Notification Title")
            .setContentText("Notification Text")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(notificationId, builder.build())
    }
}
