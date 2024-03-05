package com.sprite.spritephotobooth.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import com.sprite.spritephotobooth.Base.BaseActivity
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
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
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

            val prefrence = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE)
            val intent = Intent(this@SelectGenderActivity, StrikePoseActivity::class.java)

            intent.putExtra("gender", "male")
            intent.putExtra("template", prefrence.getInt("template",0))
            startActivity(intent)

            val editor = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE).edit()
            editor.putInt("mtemplate", if(prefrence.getInt("mtemplate",0)==7) 0 else prefrence.getInt("mtemplate",0)+1)
            editor.apply()

        }

        binding?.femaleButton?.setOnClickListener {
            val prefrence = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE)
            val intent = Intent(this@SelectGenderActivity, StrikePoseActivity::class.java)
            intent.putExtra("gender", "female")
            intent.putExtra("template", prefrence.getInt("template",0))
            startActivity(intent)

            val editor = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE).edit()
            editor.putInt("ftemplate", if(prefrence.getInt("ftemplate",0)==6) 0 else prefrence.getInt("ftemplate",0)+1)
            editor.apply()


        }
    }

    override fun loadData() {
    }
}