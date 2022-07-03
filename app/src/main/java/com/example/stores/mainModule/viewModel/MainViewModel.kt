package com.example.stores.mainModule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stores.StoreApplication
import com.example.stores.common.entities.StoreEntity
import com.example.stores.common.utils.Constants
import com.example.stores.mainModule.model.MainInteractor
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainViewModel : ViewModel() {
    private var interactor : MainInteractor
    private var storeList: MutableList<StoreEntity>

    init {
        storeList = mutableListOf()
        interactor = MainInteractor()
    }

    private val showProgressBar: MutableLiveData<Boolean> = MutableLiveData()

    private val stores : MutableLiveData<MutableList<StoreEntity>> by lazy {
        MutableLiveData<MutableList<StoreEntity>>().also {
            loadStores()
        }
    }

    fun getStores(): LiveData<MutableList<StoreEntity>>{
        return stores
    }

    fun isShowProgress(): LiveData<Boolean> = showProgressBar

    private fun loadStores(){
        showProgressBar.value = Constants.SHOW
        interactor.getStores {
            stores.value = it
            storeList = it
            showProgressBar.value = Constants.HIDE
        }
    }

    fun deleteStore(storeEntity: StoreEntity){
        interactor.deleteStore(storeEntity) {
            val index = storeList.indexOf(storeEntity)
            if (index != -1) {
                storeList.removeAt(index)
                stores.value = storeList
            }
        }
    }

    fun updateStore(storeEntity: StoreEntity){
        storeEntity.isFavorite = !storeEntity.isFavorite

        interactor.updateStore(storeEntity) {
            val index = storeList.indexOf(storeEntity)
            if (index != -1) {
                storeList[index] = storeEntity
                stores.value = storeList
            }
        }
    }


}