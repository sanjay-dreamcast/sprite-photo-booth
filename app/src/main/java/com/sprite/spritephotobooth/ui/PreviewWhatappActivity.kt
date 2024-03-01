package com.sprite.spritephotobooth.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.sprite.spritephotobooth.Base.BaseActivity
import com.sprite.spritephotobooth.MainActivity
import com.sprite.spritephotobooth.R


import com.sprite.spritephotobooth.databinding.AcivityWhatsappBinding


class PreviewWhatappActivity : BaseActivity() {
    companion object{
        @JvmStatic
        fun start(context: Context,) {
            val starter = Intent(context, PreviewWhatappActivity::class.java).apply {
            }
            context.startActivity(starter)
        }
    }
    private var binding: AcivityWhatsappBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.acivity_whatsapp)


        init()
    }

    override fun initArguments() {

    }

    override fun initViews() {
        binding!!.btnHome.setOnClickListener {
            val intent = Intent(this@PreviewWhatappActivity, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP and Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }


        binding!!.btnWhatsapp.setOnClickListener {
            val intent = Intent(this@PreviewWhatappActivity, SendWhatsappActivity::class.java)
            startActivity(intent)
        }

    }

    override fun setupListener() {
    }

    override fun loadData() {
    }

}