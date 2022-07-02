package com.example.stores.editModule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stores.common.entities.StoreEntity
import com.example.stores.editModule.model.EditStoreInteractor

class EditViewModel : ViewModel(){

    private val storeSelected = MutableLiveData<StoreEntity>()
    private val showFab = MutableLiveData<Boolean>()
    private val result = MutableLiveData<Any>()

    private val interactor : EditStoreInteractor = EditStoreInteractor()

    fun setStoreSelected(storeEntity: StoreEntity){
        storeSelected.value = storeEntity
    }

    fun getStoreSelected(): LiveData<StoreEntity> = storeSelected

    fun setShowFab(isVisible : Boolean){
        showFab.value = isVisible
    }

    fun getShowFab(): LiveData<Boolean> = showFab


    fun setResult(value : Any){
        result.value = value
    }

    fun getResult(): Any = result

    fun saveStore(storeEntity: StoreEntity){
        interactor.saveStore(storeEntity) { newId ->
            result.value = newId
        }
    }

    fun updateStore(storeEntity: StoreEntity){
        interactor.updateStore(storeEntity) { storeUpdated ->
            result.value = storeUpdated
        }
    }
}