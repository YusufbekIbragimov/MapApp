package com.yusuf.weaterapp.presentation.navigation

import androidx.navigation.NavGraphBuilder
import com.yusuf.weaterapp.presentation.dashboard.DashboardScreen

fun NavGraphBuilder.mainModuleGraph() {
    composable(Destination.DashboardScreen) { DashboardScreen() }
}