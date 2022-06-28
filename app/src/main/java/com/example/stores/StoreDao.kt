package com.example.stores

import androidx.room.*

@Dao
interface StoreDao {

    @Query("SELECT * FROM StoreEntity")
    fun getAllStores() : MutableList<StoreEntity>

    @Insert
    fun addStore(storeEntity: StoreEntity)

    @Update
    fun updateStore(storeEntity: StoreEntity)

    @Delete
    fun deleteStore(storeEntity: StoreEntity)
}