package com.sprite.spritephotobooth.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.sprite.spritephotobooth.R


/**
 * an animation when a placeholder change to image from the internet.
 * */
val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()


@BindingAdapter("loadImage")
fun ImageView.loadImage(imageUrl: Any?){
    val requestOptions = RequestOptions()
    requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL)
    imageUrl?.let {
        Glide.with(this)
            .load(imageUrl).
            centerCrop().
            placeholder(R.drawable.ic_launcher_background)
            .apply (requestOptions)
            .into(this)
    }
}