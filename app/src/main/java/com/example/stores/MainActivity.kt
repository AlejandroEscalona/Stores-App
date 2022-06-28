package com.example.stores

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.stores.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), OnClickListener {

    private lateinit var mBinding: ActivityMainBinding

    private lateinit var mAdapter: StoreAdapter
    private lateinit var mGridLayout: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.btnSave.setOnClickListener {
            val storeEntity = StoreEntity(name = mBinding.edName.text.toString().trim())
            mAdapter.add(storeEntity)
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        mAdapter = StoreAdapter(mutableListOf(),this)
        mGridLayout = GridLayoutManager(this,2)

        mBinding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = mGridLayout
            adapter = mAdapter
        }
    }


    //    OnClickListener Interface
    override fun onClick(storeEntity: StoreEntity) {
        TODO("Not yet implemented")
    }
}