package com.example.stores

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.stores.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainActivity : AppCompatActivity(), OnClickListener, MainAux {

    private lateinit var mBinding: ActivityMainBinding

    private lateinit var mAdapter: StoreAdapter
    private lateinit var mGridLayout: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.fab.setOnClickListener {
            launchEditFragment()
        }

        setupRecyclerView()
    }

    private fun launchEditFragment(args : Bundle? = null) {
        val fragment = EditStoreFragment()
        if(args != null){
            fragment.arguments = args
        }

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.add(R.id.containerMain, fragment)
        fragmentTransaction.commit()
        fragmentTransaction.addToBackStack(null)
        hideFab()
    }

    private fun setupRecyclerView() {
        mAdapter = StoreAdapter(mutableListOf(),this)
        mGridLayout = GridLayoutManager(this,resources.getInteger(R.integer.main_column))
        getStores()

        mBinding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = mGridLayout
            adapter = mAdapter
        }
    }

    private fun getStores(){
        doAsync {
            val stores = StoreApplication.database.storeDao().getAllStores()
            uiThread {
                mAdapter.setStores(stores)
            }
        }
    }


    //    OnClickListener Interface
    override fun onClick(storeId: Long) {
        val args = Bundle()
        args.putLong(getString(R.string.id),storeId)

        launchEditFragment(args)
    }

    override fun onFavouriteStore(storeEntity: StoreEntity) {
        storeEntity.isFavorite = !storeEntity.isFavorite
        doAsync {
            StoreApplication.database.storeDao().updateStore(storeEntity)
            uiThread {
                updateStore(storeEntity)
            }
        }
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
                doAsync {
                    StoreApplication.database.storeDao().deleteStore(storeEntity)
                    uiThread {
                        mAdapter.delete(storeEntity)
                    }
                }
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

    //MainAux
    override fun hideFab(isVisible: Boolean) {
        if(isVisible) mBinding.fab.show() else mBinding.fab.hide()
    }

    override fun addStore(storeEntity: StoreEntity) {
        mAdapter.add(storeEntity)
    }

    override fun updateStore(storeEntity: StoreEntity) {
        mAdapter.update(storeEntity)
    }
}