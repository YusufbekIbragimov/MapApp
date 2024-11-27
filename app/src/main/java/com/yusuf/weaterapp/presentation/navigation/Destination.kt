package com.yusuf.weaterapp.presentation.navigation

sealed class Destination(protected val route: String, vararg params: String) {
    val fullRoute: String = if (params.isEmpty()) route else buildString {
        append(route)
        params.forEach { append("/{$it}") }
    }

    sealed class NoArgumentsDestination(route: String) : Destination(route) {
        operator fun invoke(): String = route
    }

    data object DashboardScreen : NoArgumentsDestination(DASHBOARD_SCREEN_ROUTE)

    companion object {
        private const val DASHBOARD_SCREEN_ROUTE = "dashboard_screen"
    }
}