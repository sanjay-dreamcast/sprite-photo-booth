package com.sprite.spritephotobooth.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sprite.spritephotobooth.Base.BaseActivity
import com.sprite.spritephotobooth.R
import com.sprite.spritephotobooth.databinding.ActivityMainBinding
import com.sprite.spritephotobooth.databinding.ActivitySelectGenderBinding

class SelectGenderActivity : BaseActivity() {
    private var binding: ActivitySelectGenderBinding? = null
    companion object{
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, SelectGenderActivity::class.java).apply {
            }
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectGenderBinding.inflate(layoutInflater)
        binding?.apply {
            setContentView(this.root)
            lifecycleOwner = this@SelectGenderActivity
            executePendingBindings()
        }
        init()
    }

    override fun initArguments() {
    }

    override fun initViews() {
    }

    override fun setupListener() {
        binding?.maleButton?.setOnClickListener {
            SelectSceneActivity.start(this@SelectGenderActivity,"male")
        }

        binding?.femaleButton?.setOnClickListener {
            SelectSceneActivity.start(this@SelectGenderActivity,"female")
        }
    }

    override fun loadData() {
    }
}