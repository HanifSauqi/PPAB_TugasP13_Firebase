package com.example.latihanp12_firebase

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast

class NotifReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val msg = intent?.getStringExtra("Message")
        if (msg != null){
            Log.d("notif_receiver","baca notif clicked")
            Toast.makeText(context,msg, Toast.LENGTH_SHORT).show()
        }
    }
}