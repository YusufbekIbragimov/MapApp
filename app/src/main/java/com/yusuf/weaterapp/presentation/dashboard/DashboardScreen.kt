package com.yusuf.weaterapp.presentation.dashboard

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yusuf.weaterapp.presentation.dashboard.map.MapScreen
import com.yusuf.weaterapp.presentation.dashboard.profile.ProfileScreen
import com.yusuf.weaterapp.presentation.dashboard.saved.SavedScreen
import com.yusuf.weaterapp.presentation.navigation.Screen

@Composable
fun DashboardScreen() {
    val navController = rememberNavController()
    var isBottomVisible by rememberSaveable { mutableStateOf(true) }

    Scaffold(
        contentColor = Color.White,
        containerColor = Color.White,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            BottomBar(navController, isBottomVisible)
        },

    ) { innerPadding ->

        NavHost(
            navController,
            startDestination = Screen.Saved.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { fadeIn(tween(100)) },
            exitTransition = { fadeOut(tween(100)) }
        ) {
            composable(Screen.Saved.route) { SavedScreen() }
            composable(Screen.Map.route) { MapScreen(
                showBottomSheet = {
                    isBottomVisible = it
                    Log.d("DashboardScreen", "isBottomVisible: $it")
                }
            )
            }
            composable(Screen.Profile.route) { ProfileScreen() }
        }
    }
}

@Composable
private fun BottomBar(navController: NavHostController, isBottomVisible: Boolean) {
    AnimatedVisibility(visible = isBottomVisible, enter = fadeIn(tween(0)), exit = fadeOut(tween(0))) {
        Surface(
            shadowElevation = 16.dp,
            shape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp)
        ) {
            BottomAppBar(containerColor = Color.White, contentColor = Color.White) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                items.forEach { screen ->

                    NavigationBarItem(
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = MutableInteractionSource(),
                            onClick = {}
                        ),
                        icon = {
                            Image(
                                painter = painterResource(id = screen.iconId),
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(
                                    color = if (screen.route != navController.currentDestination?.route) Color.Gray
                                    else Color.Black
                                )
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.Black,
                            selectedTextColor = Color.Black,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color.White
                        ),
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            if (screen.route != navController.currentDestination?.route) {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

private val items = listOf(
    Screen.Saved,
    Screen.Map,
    Screen.Profile
)