package com.sprite.spritephotobooth.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sprite.spritephotobooth.Base.BaseActivity
import com.sprite.spritephotobooth.R
import com.sprite.spritephotobooth.databinding.ActivitySelectGenderBinding
import com.sprite.spritephotobooth.databinding.ActivityStrikePoseBinding

class StrikePoseActivity : BaseActivity() {
    private var binding: ActivityStrikePoseBinding? = null
    companion object{
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, StrikePoseActivity::class.java).apply {
            }
            context.startActivity(starter)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStrikePoseBinding.inflate(layoutInflater)
        binding?.apply {
            setContentView(this.root)
            lifecycleOwner = this@StrikePoseActivity
            executePendingBindings()
        }
        init()
    }

    override fun initArguments() {
    }

    override fun initViews() {
    }

    override fun setupListener() {
    }

    override fun loadData() {
    }
}