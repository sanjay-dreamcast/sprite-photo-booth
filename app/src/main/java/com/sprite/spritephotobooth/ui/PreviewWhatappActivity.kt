package com.sprite.spritephotobooth.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import com.sprite.spritephotobooth.Base.BaseActivity
import com.sprite.spritephotobooth.MainActivity
import com.sprite.spritephotobooth.R
import android.view.WindowManager
import com.bumptech.glide.Glide

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
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding = DataBindingUtil.setContentView(this,R.layout.acivity_whatsapp)


        init()

        binding!!.layout.tvTitle.text="Scan the QR code, download the image"
        binding!!.layout.ivBack.setOnClickListener {
            finish()
        }
        Log.e("@@id",""+intent.getStringExtra("id"))
        Log.e("@@ai_img",""+intent.getStringExtra("ai_img"))

        Glide.with(this).load(intent.getStringExtra("ai_img").toString()).into(binding!!.recyScene)
        Glide.with(this).load(intent.getStringExtra("ai_qr").toString()).into(binding!!.imageView21)
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
            val intent1 = Intent(this@PreviewWhatappActivity, SendWhatsappActivity::class.java)
            intent1.putExtra("id",intent.getStringExtra("id").toString())
            intent1.putExtra("ai_img",intent.getStringExtra("ai_img").toString())
            startActivity(intent1)
        }

    }

    override fun setupListener() {
    }

    override fun loadData() {
    }

}