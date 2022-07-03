package com.example.stores.mainModule.model

import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.stores.StoreApplication
import com.example.stores.common.entities.StoreEntity
import com.example.stores.common.utils.Constants
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainInteractor {


    fun getStores(callback: (MutableList<StoreEntity>) -> Unit){
        val url = Constants.STORES_URL + Constants.GET_ALL_PATH

        //Method, url, request, callback response, error response
        val jsonObject = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            val status = response.getInt(Constants.STATUS_PROPERTY)

            if(status == Constants.SUCCESS){
                val jsonObject = Gson().fromJson(
                    response.getJSONArray(Constants.STORES_PROPERTY).get(0).toString()
                    ,StoreEntity::class.java)

                //get stores from response
                val jsonList = response.getJSONArray(Constants.STORES_PROPERTY).toString()
                //Set type on TypeToken
                val mutableListType = object : TypeToken<MutableList<StoreEntity>>(){}.type
                //set type with date from response
                val storeList = Gson().fromJson<MutableList<StoreEntity>>(jsonList, mutableListType)

                callback(storeList)
            }
        },{
            it.printStackTrace()
        })

        StoreApplication.storeAPI.addToRequestQueue(jsonObject)
    }

    fun getStoresRoom(callback: (MutableList<StoreEntity>) -> Unit){
        doAsync {
            val storesList = StoreApplication.database.storeDao().getAllStores()
            uiThread {
                val json = Gson().toJson(storesList)
                callback(storesList)
            }
        }
    }

    fun deleteStore(storeEntity: StoreEntity, callback: (StoreEntity) -> Unit){
        doAsync {
            StoreApplication.database.storeDao().deleteStore(storeEntity)
            uiThread {
                callback(storeEntity)
            }
        }
    }

    fun updateStore(storeEntity: StoreEntity, callback: (StoreEntity) -> Unit){
        doAsync {
            StoreApplication.database.storeDao().updateStore(storeEntity)
            uiThread {
                callback(storeEntity)
            }
        }
    }


}