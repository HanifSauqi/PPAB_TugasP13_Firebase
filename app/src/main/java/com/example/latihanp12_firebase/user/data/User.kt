package com.example.latihanp12_firebase.user.data

data class User(
    val uid: String,
    val email: String,
    val role: String = "user" // Default role is user
) {
    constructor() : this("", "", "user") // Empty constructor for Firestore
}