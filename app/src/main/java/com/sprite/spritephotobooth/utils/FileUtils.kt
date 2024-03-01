package com.sprite.spritephotobooth.utils

import android.content.Context
import com.sprite.spritephotobooth.R
import java.io.File



// get output directory
fun Context.getOutputDirectory(): File {
    val mediaDir = externalMediaDirs.firstOrNull()?.let {
        File(it, resources.getString(R.string.app_name)).apply {
            mkdir() } }
    return if(mediaDir != null && mediaDir.exists())
        mediaDir else filesDir
}

// Define a function to delete a file from the files directory
fun Context.deleteFileFromDirectory(fileName: String): Boolean {
    val file = File(getOutputDirectory(), fileName)
    return file.delete()
}