package com.sprite.spritephotobooth

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.sprite.spritephotobooth.Base.BaseActivity
import com.sprite.spritephotobooth.data.model.SpriteFeature
import com.sprite.spritephotobooth.databinding.ActivityMainBinding
import com.sprite.spritephotobooth.ui.SelectGenderActivity
import com.sprite.spritephotobooth.ui.SpriteFeaturesAdapter

class MainActivity : BaseActivity() {
    private var binding: ActivityMainBinding? = null
    private val mSpriteFeaturesAdapter by lazy{
        SpriteFeaturesAdapter(ArrayList())
    }
    companion object{
        const val TAG = "MainActivityTAG"
    }
    private val myLinearLayoutManager = object : LinearLayoutManager(this) {
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

        binding = ActivityMainBinding.inflate(layoutInflater)
        binding?.apply {
            setContentView(this.root)
            lifecycleOwner = this@MainActivity
            executePendingBindings()
        }
        init()
    }

    override fun initArguments() {

    }

    override fun initViews() {
        binding?.spriteFeatureRc?.apply {
            layoutManager = myLinearLayoutManager
            adapter =  mSpriteFeaturesAdapter
        }
        mSpriteFeaturesAdapter.updateData(getFeatureList())
    }

    override fun setupListener() {

        binding?.continueButton?.setOnClickListener {
            SelectGenderActivity.start(this@MainActivity)
        }
    }

    override fun loadData() {
    }

    private fun getFeatureList(): List<SpriteFeature> {
        // Create a list of SpriteFeature objects
        return listOf(
            SpriteFeature(id = 1, featureName = "Select Gender"),
//            SpriteFeature(id = 2, featureName = "Select Scene"),
            SpriteFeature(id = 2, featureName = "Strike a Pose"),
            SpriteFeature(id = 3, featureName = "Select the AI Avatar"),
            SpriteFeature(id = 4, featureName = "Download or Share on WhatsApp!"),
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}