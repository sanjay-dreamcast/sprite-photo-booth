package com.sprite.spritephotobooth.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
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
import android.view.WindowManager
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
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = DataBindingUtil.setContentView(this,R.layout.acivity_select_scene)
        binding?.apply {
            setContentView(this.root)
            lifecycleOwner = this@SelectSceneActivity
            executePendingBindings()
        }

        binding!!.layout.tvTitle.text="Select a Scene"
        binding!!.layout.ivBack.setOnClickListener {
            finish()
        }

        init()
    }

    override fun initArguments() {


    }

    override fun initViews() {
        intentTYPE=intent.getStringExtra("gender").toString()
        Log.e("@@intentTYPE",intentTYPE)
        if (intentTYPE=="male")
        {
            sceneItem.add(SceneItem(resources.getDrawable(R.drawable.scene_5)))
            sceneItem.add(SceneItem(resources.getDrawable(R.drawable.scene_3)))
            sceneItem.add(SceneItem(resources.getDrawable(R.drawable.scene_4)))
            sceneItem.add(SceneItem(resources.getDrawable(R.drawable.scene_6)))
            sceneItem.add(SceneItem(resources.getDrawable(R.drawable.scene_2)))
            sceneItem.add(SceneItem(resources.getDrawable(R.drawable.scene_1)))
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
                val intent1 = Intent(this@SelectSceneActivity, StrikePoseActivity::class.java)
                intent1.putExtra("gender", intent.getStringExtra("gender"))
                intent1.putExtra("template", position)
                startActivity(intent1)



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