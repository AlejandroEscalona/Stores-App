package com.example.stores.mainModule

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.stores.*
import com.example.stores.common.entities.StoreEntity
import com.example.stores.databinding.ActivityMainBinding
import com.example.stores.editModule.EditStoreFragment
import com.example.stores.editModule.viewModel.EditViewModel
import com.example.stores.mainModule.adapter.OnClickListener
import com.example.stores.mainModule.adapter.StoreAdapter
import com.example.stores.mainModule.viewModel.MainViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity(), OnClickListener {

    private lateinit var mBinding: ActivityMainBinding

    private lateinit var mAdapter: StoreAdapter
    private lateinit var mGridLayout: GridLayoutManager

    //MVVM
    private lateinit var mMainViewModel: MainViewModel
    private lateinit var mEditStoreViewModel : EditViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.fab.setOnClickListener {
            launchEditFragment()
        }

        setupViewModel()

        setupRecyclerView()
    }

    private fun setupViewModel() {
        mMainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mMainViewModel.getStores().observe(this) { stores ->
            mAdapter.setStores(stores)

        }

        mMainViewModel.isShowProgress().observe(this) { isShowProgress ->
            mBinding.prorgessBar.visibility = if (isShowProgress) View.VISIBLE else View.GONE
        }

        mEditStoreViewModel = ViewModelProvider(this).get(EditViewModel::class.java)
        mEditStoreViewModel.getShowFab().observe(this) { isVisible ->
            if (isVisible) mBinding.fab.show() else mBinding.fab.hide()
        }

        mEditStoreViewModel.getStoreSelected().observe(this) { storeEntity ->
            mAdapter.add(storeEntity)
        }
    }

    private fun launchEditFragment(storeEntity: StoreEntity = StoreEntity()) {
        mEditStoreViewModel.setShowFab(false)
        mEditStoreViewModel.setStoreSelected(storeEntity)

        val fragment = EditStoreFragment()

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.add(R.id.containerMain, fragment)
        fragmentTransaction.commit()
        fragmentTransaction.addToBackStack(null)
    }

    private fun setupRecyclerView() {
        mAdapter = StoreAdapter(mutableListOf(),this)
        mGridLayout = GridLayoutManager(this,resources.getInteger(R.integer.main_column))

        mBinding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = mGridLayout
            adapter = mAdapter
        }
    }

    //    OnClickListener Interface
    override fun onClick(storeEntity: StoreEntity) {
        launchEditFragment(storeEntity)
    }

    override fun onFavouriteStore(storeEntity: StoreEntity) {
        mMainViewModel.updateStore(storeEntity)
    }

    override fun onDeleteStore(storeEntity: StoreEntity) {
       val items = resources.getStringArray(R.array.array_option_items)

        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialogTitle)
            .setItems(items) { dialogInterface, i ->
                when (i) {
                    0 -> confirmDelete(storeEntity)
                    1 -> dial(storeEntity.phone)
                    2 -> goWebsite(storeEntity.website)
                    else -> Toast.makeText(this, "Nothing", Toast.LENGTH_SHORT).show()
                }
            }.show()
    }

    private fun confirmDelete(storeEntity: StoreEntity){
        MaterialAlertDialogBuilder(this)
            .setTitle(resources.getString(R.string.delete_store))
            .setNegativeButton(resources.getString(R.string.decline), null)
            .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                mMainViewModel.deleteStore(storeEntity)
            }
            .show()
    }

    private fun dial(phone: String){
        val callIntent = Intent().apply {
            action = Intent.ACTION_DIAL
            data = Uri.parse("tel:$phone")
        }
        startIntent(callIntent)

    }

    private fun goWebsite(website : String){
        if(website.isEmpty()){
            Toast.makeText(this,"This url doesnt exits",Toast.LENGTH_SHORT).show()
        }else{
            val websiteIntent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = Uri.parse(website)
            }
            startIntent(websiteIntent)
        }
    }

    private fun startIntent(intent: Intent){
        if(intent.resolveActivity(packageManager) != null)
            startActivity(intent)
        else
            Toast.makeText(this,"Error in call",Toast.LENGTH_SHORT).show()
    }
}