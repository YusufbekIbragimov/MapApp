package com.yusuf.weaterapp.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.yusuf.weaterapp.R

sealed class Screen(
    val route: String,
    @StringRes val resourceId: Int,
    @DrawableRes val iconId: Int
) {
    data object Saved : Screen("saved", R.string.my_addresses, R.drawable.ic_saved)
    data object Map : Screen("mapScreen", R.string.map, R.drawable.ic_location)
    data object Profile : Screen("profileScreen", R.string.profile, R.drawable.ic_profile)
}