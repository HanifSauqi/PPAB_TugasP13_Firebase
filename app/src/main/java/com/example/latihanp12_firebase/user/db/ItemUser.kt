package com.example.latihanp12_firebase.user.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item_user")
data class  ItemUser(
    @PrimaryKey
    @ColumnInfo(name = "uid")
    var uid: String,

    @ColumnInfo(name = "nama")
    val nama: String,

    @ColumnInfo(name = "kategori")
    val kategori: String,

    @ColumnInfo(name = "kalori")
    val kalori: Int
)
