package com.example.latihanp12_firebase.admin

data class Item(
    val nama: String,
    val kategori: String,
    val jmlKalori: Int){
    // Add a no-argument constructor
    constructor() : this("", "", 0) // Provide default values or initialize as needed
}
