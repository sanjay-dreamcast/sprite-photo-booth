package com.sprite.spritephotobooth.adapters


import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sprite.spritephotobooth.databinding.ItemSelectSceneBinding
import com.sprite.spritephotobooth.model.SceneItem

class SelectSceneAdapter (private var sceneItem: ArrayList<SceneItem>? = null ,var mContext: Context ,var itemClick: ItemClick): RecyclerView.Adapter<SelectSceneAdapter.SceneItemsVH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectSceneAdapter.SceneItemsVH {
        val binding = ItemSelectSceneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SceneItemsVH(binding)
    }
    override fun getItemCount(): Int {
        return sceneItem?.size ?: 0
    }

    override fun onBindViewHolder(holder: SelectSceneAdapter.SceneItemsVH, position: Int) {
        holder.loadData(sceneItem?.get(position))
        holder.ItemClickOn(sceneItem?.get(position))

    }
    inner  class SceneItemsVH(private val binding : ItemSelectSceneBinding): RecyclerView.ViewHolder(binding.root){
        fun loadData(sceneItem : SceneItem?){
            Glide.with(mContext).load(sceneItem!!.image).into(binding.ivImage)
        }

        fun ItemClickOn(sceneItem : SceneItem?){

            binding.ivImage.setOnClickListener {
                binding.ivImageClick.visibility=View.VISIBLE
                binding.ivTick.visibility=View.VISIBLE

                itemClick.onItemClick(position)
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.ivImageClick.visibility=View.GONE
                    binding.ivTick.visibility=View.GONE

                    //Do something after 100ms
                }, 500)

            }

        }

    }

    interface ItemClick{
       fun onItemClick(position: Int)
    }
}