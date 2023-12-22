package com.example.latihanp12_firebase.user.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(itemUser: ItemUser)

    @Query("SELECT * FROM item_user WHERE uid = :uid")
    fun getAllItems(uid: String): LiveData<List<ItemUser>>

    @get:Query("SELECT * from item_user ORDER by uid ASC")
    val allDataHarian: LiveData<List<ItemUser>>

    @Update
    fun update(itemUser: ItemUser)

    @Delete
    fun delete(itemUser: ItemUser)
}