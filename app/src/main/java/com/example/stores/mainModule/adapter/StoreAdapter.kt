package com.example.stores.mainModule.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.stores.R
import com.example.stores.common.entities.StoreEntity
import com.example.stores.databinding.ItemStoreBinding

class StoreAdapter(private var stores: MutableList<StoreEntity>, private var listener: OnClickListener):
        RecyclerView.Adapter<StoreAdapter.ViewHolder>(){

    private lateinit var mContext: Context

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemStoreBinding.bind(view)

        fun setListener(storeEntity: StoreEntity){

            with(binding.root){
                setOnClickListener {
                    listener.onClick(storeEntity)
                }
                setOnLongClickListener {
                    listener.onDeleteStore(storeEntity)
                    true
                }
            }

            binding.cbFavorite.setOnClickListener {
                listener.onFavouriteStore((storeEntity))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        mContext = parent.context

        val view = LayoutInflater.from(mContext).inflate(R.layout.item_store,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(stores.size > 0){
            val store = stores.get(position)

            with(holder){
                setListener(store)
                binding.tvName.text = store.name
                binding.cbFavorite.isChecked = store.isFavorite
                Glide.with(mContext)
                    .load(store.photoUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()
                    .into(binding.imgPhoto)
            }
        }
    }

    override fun getItemCount(): Int = stores.size

    @SuppressLint("NotifyDataSetChanged")
    fun add(storeEntity: StoreEntity) {
        if(storeEntity.id != 0L){
            if(!stores.contains(storeEntity)){
                stores.add(storeEntity)
                notifyItemInserted(stores.size-1)
            }else{
                update(storeEntity)
                notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setStores(stores: List<StoreEntity>) {
        this.stores = stores as MutableList<StoreEntity>
        notifyDataSetChanged()
    }

    private fun update(storeEntity: StoreEntity) {
        val index = stores.indexOf(storeEntity)
        if(index != -1){
            stores.set(index, storeEntity)
            notifyItemChanged(index)
        }
    }
}