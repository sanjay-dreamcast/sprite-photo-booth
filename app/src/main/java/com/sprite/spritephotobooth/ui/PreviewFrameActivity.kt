package com.sprite.spritephotobooth.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.sprite.spritephotobooth.Base.BaseActivity
import com.sprite.spritephotobooth.databinding.ActivityPreviewFrameBinding
import com.sprite.spritephotobooth.utils.deleteFileFromDirectory
import com.sprite.spritephotobooth.utils.loadImage

class PreviewFrameActivity : BaseActivity() {
    private var binding: ActivityPreviewFrameBinding? = null
    companion object {
        const val SAVED_FILE_NAME = "savedFileName"
        const val SAVED_URI = "savedFileName"

        @JvmStatic
        fun start(context: Context, savedFileName: String, savedUri: Uri) {
            val starter = Intent(context, PreviewFrameActivity::class.java).apply {
                putExtra(SAVED_FILE_NAME,savedFileName)
                putExtra(SAVED_URI,savedUri)
            }
            context.startActivity(starter)
        }

    }
    private val savedFileName by lazy {
        intent.getStringExtra(SAVED_FILE_NAME)
    }
    private val savedUri by lazy {
        intent.getStringExtra(SAVED_FILE_NAME)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewFrameBinding.inflate(layoutInflater)
        binding?.apply {
            setContentView(this.root)
            lifecycleOwner = this@PreviewFrameActivity
            executePendingBindings()
        }
        init()
    }

    override fun initArguments() {
    }

    override fun initViews() {
        binding!!.btnRetake.setOnClickListener {
            savedFileName?.let { it1 -> deleteFileFromDirectory(it1) }
            finish()
        }
        binding!!.btnConfirm.setOnClickListener {  PreviewWhatappActivity.start(this@PreviewFrameActivity)}
    }

    override fun setupListener() {
    }

    override fun loadData() {
        binding?.imgSprite?.loadImage(savedUri)

    }

    override fun onDestroy() {
        savedFileName?.let { it1 -> deleteFileFromDirectory(it1) }
        super.onDestroy()
    }
}