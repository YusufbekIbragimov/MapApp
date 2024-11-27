package com.yusuf.weaterapp.utils

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.IntegerRes
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourceProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getString(@StringRes stringId: Int): String = context.getString(stringId)

    fun getString(@StringRes stringId: Int, vararg formatParams: Any): String {
        return context.getString(stringId, *formatParams)
    }

    @ColorInt
    fun getColor(@ColorRes colorId: Int): Int = ContextCompat.getColor(context, colorId)

    fun getColorStateList(@ColorRes colorStateListId: Int): ColorStateList? {
        return ContextCompat.getColorStateList(context, colorStateListId)
    }

    fun getDrawable(@DrawableRes drawableId: Int): Drawable? = try {
        ContextCompat.getDrawable(context, drawableId)
    } catch (e: Resources.NotFoundException) {
        null
    }

    fun getDimension(@DimenRes dimensionId: Int): Float {
        return context.resources.getDimension(dimensionId)
    }

    fun getDimensionPixelSize(@DimenRes dimensionId: Int): Int {
        return context.resources.getDimensionPixelSize(dimensionId)
    }

    fun getInteger(@IntegerRes integerId: Int): Int = context.resources.getInteger(integerId)

    fun getQuantityString(@PluralsRes id: Int, quantity: Int, vararg formatParams: Any): String {
        return context.resources.getQuantityString(id, quantity, *formatParams)
    }
}