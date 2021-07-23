package com.example.excursionstest.ui.util

import android.net.Uri
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

fun ImageView.loadImage(uri: Uri){

    Glide.with(this)
        .load(uri)
        .into(this)
}

fun ImageView.loadImage(resourceId: Int){

    Glide.with(this)
        .load(resourceId)
        .into(this)
}

fun TextView.setTime(durationMS: Long) {

    val durationS = durationMS / 1000
    val hours = durationS.toInt() / 3600
    val minutes = (durationS.toInt() - hours * 3600) / 60
    val seconds = durationS.toInt() - hours * 3600 - minutes * 60

    text = if (hours == 0) {
        String.format("%2d:%02d", minutes, seconds)
    } else {
        String.format("%d:%02d:%02d", hours, minutes, seconds)
    }
}