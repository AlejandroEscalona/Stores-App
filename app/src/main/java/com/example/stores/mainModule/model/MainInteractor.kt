package com.example.stores.mainModule.model

import com.example.stores.StoreApplication
import com.example.stores.common.entities.StoreEntity
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainInteractor {

    interface StoresCallback{
        fun getStoresCallback(stores: MutableList<StoreEntity>)
    }

    fun getStoresCallback(callback: StoresCallback){

    }

    fun getStores(callback: (MutableList<StoreEntity>) -> Unit){
        doAsync {
            val storesList = StoreApplication.database.storeDao().getAllStores()
            uiThread {
                callback(storesList)
            }
        }
    }
}