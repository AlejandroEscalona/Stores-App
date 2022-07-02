package com.example.stores.mainModule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stores.StoreApplication
import com.example.stores.common.entities.StoreEntity
import com.example.stores.mainModule.model.MainInteractor
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainViewModel : ViewModel() {
    private var interactor : MainInteractor

    init {
        interactor = MainInteractor()
    }

    private val stores : MutableLiveData<List<StoreEntity>> by lazy {
        MutableLiveData<List<StoreEntity>>().also {
            loadStores()
        }
    }

    fun getStores(): LiveData<List<StoreEntity>>{
        return stores
    }

    private fun loadStores(){
//        interactor.getStoresCallback(object : MainInteractor.StoresCallback{
//            override fun getStoresCallback(stores: MutableList<StoreEntity>){
//                this@MainViewModel.stores.value = stores
//            }
//        })
        interactor.getStores {
            stores.value = it
        }
    }
}