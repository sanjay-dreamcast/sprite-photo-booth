package com.sprite.spritephotobooth.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.sprite.spritephotobooth.Base.BaseActivity
import com.sprite.spritephotobooth.R
import com.sprite.spritephotobooth.adapters.SelectSceneAdapter
import com.sprite.spritephotobooth.data.model.SpriteFeature
import com.sprite.spritephotobooth.databinding.AcivitySelectSceneBinding
import com.sprite.spritephotobooth.databinding.ActivityMainBinding
import com.sprite.spritephotobooth.model.SceneItem
import com.sprite.spritephotobooth.ui.SpriteFeaturesAdapter

class SelectSceneActivity : BaseActivity() {
    private var binding: AcivitySelectSceneBinding? = null
   var selectSceneAdapter:SelectSceneAdapter?=null
    var sceneItem=ArrayList<SceneItem>()
    var intentTYPE=""

    companion object{
        @JvmStatic
        fun start(context: Context,intentType:String) {
            val starter = Intent(context, SelectSceneActivity::class.java).apply {putExtra("intentType",intentType)

            }
            context.startActivity(starter)

        }
    }

    private val myGridLayoutManager = object : GridLayoutManager(this,2) {
        override fun canScrollVertically(): Boolean {
            return false
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.acivity_select_scene)
        binding?.apply {
            setContentView(this.root)
            lifecycleOwner = this@SelectSceneActivity
            executePendingBindings()
        }

        binding!!.layout.tvTitle.text="Select a Scene"

        init()
    }

    override fun initArguments() {
       intentTYPE=intent.getStringExtra("intentTYPE").toString()

    }

    override fun initViews() {

        if (intentTYPE=="male")
        {
            sceneItem.add(SceneItem(resources.getDrawable(R.drawable.scene_1)))
            sceneItem.add(SceneItem(resources.getDrawable(R.drawable.scene_2)))
            sceneItem.add(SceneItem(resources.getDrawable(R.drawable.scene_3)))
            sceneItem.add(SceneItem(resources.getDrawable(R.drawable.scene_4)))
            sceneItem.add(SceneItem(resources.getDrawable(R.drawable.scene_5)))
            sceneItem.add(SceneItem(resources.getDrawable(R.drawable.scene_6)))
        }else{
            sceneItem.add(SceneItem(resources.getDrawable(R.drawable.scene_1)))
            sceneItem.add(SceneItem(resources.getDrawable(R.drawable.scene_2)))
            sceneItem.add(SceneItem(resources.getDrawable(R.drawable.scene_3)))
            sceneItem.add(SceneItem(resources.getDrawable(R.drawable.scene_4)))
            sceneItem.add(SceneItem(resources.getDrawable(R.drawable.scene_5)))
            sceneItem.add(SceneItem(resources.getDrawable(R.drawable.scene_6)))
        }

        selectSceneAdapter= SelectSceneAdapter(sceneItem,this,object :SelectSceneAdapter.ItemClick{
            override fun onItemClick(position: Int) {
                sceneItem[position].image
                StrikePoseActivity.start(this@SelectSceneActivity)

            }
        })
        binding?.recyScene?.apply {
            layoutManager = myGridLayoutManager

            adapter =  selectSceneAdapter
        }
    }

    override fun setupListener() {
    }

    override fun loadData() {
    }

}