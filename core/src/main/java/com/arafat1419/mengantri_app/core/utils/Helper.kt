package com.arafat1419.mengantri_app.core.utils

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import com.arafat1419.mengantri_app.core.R
import com.avatarfirst.avatargenlib.AvatarGenerator

object Helper {
    fun setAvatarGenerator(context: Context, label: String): BitmapDrawable {
        return AvatarGenerator.AvatarBuilder(context)
            .setLabel(label)
            .setAvatarSize(80)
            .setTextSize(14)
            .setBackgroundColor(R.color.primary)
            .toCircle()
            .build()
    }
}