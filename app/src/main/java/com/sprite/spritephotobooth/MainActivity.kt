package com.sprite.spritephotobooth

import android.os.Bundle
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
            SpriteFeature(id = 2, featureName = "Select Scene"),
            SpriteFeature(id = 4, featureName = "Strike a Pose"),
            SpriteFeature(id = 5, featureName = "Select the AI Avatar"),
            SpriteFeature(id = 6, featureName = "Download or Share on Email!"),
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}