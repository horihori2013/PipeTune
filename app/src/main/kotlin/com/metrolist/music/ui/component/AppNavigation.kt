/**
 * Metrolist Project (C) 2026
 * Licensed under GPL-3.0 | See git history for contributors
 */

package com.metrolist.music.ui.component

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalViewConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.metrolist.music.ui.screens.Screens
import com.kyant.backdrop.Backdrop
import com.metrolist.music.ui.component.liquidglass.LiquidBottomTabs
import com.metrolist.music.ui.component.liquidglass.LiquidBottomTab
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Stable
private fun isRouteSelected(currentRoute: String?, screenRoute: String, navigationItems: List<Screens>): Boolean {
    if (currentRoute == null) return false
    if (currentRoute == screenRoute) return true
    if (navigationItems.any { it.route == screenRoute } &&
        currentRoute.startsWith("$screenRoute/")) return true

    // Fix: match the route template, not the resolved route
    if (screenRoute == "search_input" &&
        (currentRoute.startsWith("search/") || currentRoute == "search/{query}")) return true

    return false
}

@Composable
fun AppNavigationRail(
    navigationItems: List<Screens>,
    currentRoute: String?,
    onItemClick: (Screens, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    pureBlack: Boolean = false,
    onSearchLongClick: (() -> Unit)? = null,
    onHomeLongHold: (() -> Unit)? = null,
) {
    val containerColor = if (pureBlack) Color.Black else MaterialTheme.colorScheme.surfaceContainer
    val haptics = LocalHapticFeedback.current
    val viewConfiguration = LocalViewConfiguration.current

    NavigationRail(
        modifier = modifier,
        containerColor = containerColor
    ) {
        Spacer(modifier = Modifier.weight(1f))

        navigationItems.forEach { screen ->
            val isSelected = remember(currentRoute, screen.route) {
                isRouteSelected(currentRoute, screen.route, navigationItems)
            }
            val currentIsSelected by rememberUpdatedState(isSelected)
            val iconRes = remember(isSelected, screen) {
                if (isSelected) screen.iconIdActive else screen.iconIdInactive
            }

            val isSearchItem = screen == Screens.Search && onSearchLongClick != null
            val isHomeHoldItem = screen == Screens.Home && onHomeLongHold != null
            val interactionSource = remember { MutableInteractionSource() }

            if (isSearchItem || isHomeHoldItem) {
                LaunchedEffect(interactionSource) {
                    var isLongClick = false
                    interactionSource.interactions.collectLatest { interaction ->
                        when (interaction) {
                            is PressInteraction.Press -> {
                                isLongClick = false
                                delay(if (isHomeHoldItem) 15_000L else viewConfiguration.longPressTimeoutMillis)
                                isLongClick = true
                                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                if (isHomeHoldItem) onHomeLongHold.invoke() else onSearchLongClick?.invoke()
                            }
                            is PressInteraction.Release -> {
                                if (!isLongClick) {
                                    onItemClick(screen, currentIsSelected)
                                }
                            }
                            is PressInteraction.Cancel -> {
                                isLongClick = false
                            }
                        }
                    }
                }
            }

            NavigationRailItem(
                selected = isSelected,
                onClick = {
                    if (!isSearchItem && !isHomeHoldItem) {
                        onItemClick(screen, currentIsSelected)
                    }
                },
                interactionSource = interactionSource,
                icon = {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = stringResource(screen.titleId)
                    )
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun AppNavigationBar(
    navigationItems: List<Screens>,
    currentRoute: String?,
    onItemClick: (Screens, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    pureBlack: Boolean = false,
    slimNav: Boolean = false,
    onSearchLongClick: (() -> Unit)? = null,
    onHomeLongHold: (() -> Unit)? = null,
    backdrop: Backdrop? = null,
) {
    val haptics = LocalHapticFeedback.current
    val viewConfiguration = LocalViewConfiguration.current

    val currentIndex = remember(currentRoute, navigationItems) {
        navigationItems.indexOfFirst { screen ->
            isRouteSelected(currentRoute, screen.route, navigationItems)
        }.coerceAtLeast(0)
    }

    var selectedTabIndex by rememberSaveable { mutableIntStateOf(currentIndex) }

    LaunchedEffect(currentRoute, navigationItems) {
        val newIndex = navigationItems.indexOfFirst { screen ->
            isRouteSelected(currentRoute, screen.route, navigationItems)
        }.coerceAtLeast(0)
        if (newIndex != selectedTabIndex) {
            selectedTabIndex = newIndex
        }
    }

    if (backdrop != null) {
        LiquidBottomTabs(
            selectedTabIndex = { selectedTabIndex },
            onTabSelected = { index ->
                if (index in navigationItems.indices) {
                    val screen = navigationItems[index]
                    val isSelected = isRouteSelected(currentRoute, screen.route, navigationItems)
                    onItemClick(screen, isSelected)
                }
            },
            backdrop = backdrop,
            tabsCount = navigationItems.size,
            modifier = modifier
        ) {
            navigationItems.forEachIndexed { index, screen ->
                val isSelected = remember(currentRoute, screen.route) {
                    isRouteSelected(currentRoute, screen.route, navigationItems)
                }
                val iconRes = remember(isSelected, screen) {
                    if (isSelected) screen.iconIdActive else screen.iconIdInactive
                }

                val isSearchItem = screen == Screens.Search && onSearchLongClick != null
                val isHomeHoldItem = screen == Screens.Home && onHomeLongHold != null
                val interactionSource = remember { MutableInteractionSource() }
                val currentIsSelected by rememberUpdatedState(isSelected)

                if (isSearchItem || isHomeHoldItem) {
                    LaunchedEffect(interactionSource) {
                        var isLongClick = false
                        interactionSource.interactions.collectLatest { interaction ->
                            when (interaction) {
                                is PressInteraction.Press -> {
                                    isLongClick = false
                                    delay(if (isHomeHoldItem) 15_000L else viewConfiguration.longPressTimeoutMillis)
                                    isLongClick = true
                                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                    if (isHomeHoldItem) onHomeLongHold.invoke() else onSearchLongClick?.invoke()
                                }
                                is PressInteraction.Release -> {
                                    if (!isLongClick) {
                                        onItemClick(screen, currentIsSelected)
                                    }
                                }
                                is PressInteraction.Cancel -> {
                                    isLongClick = false
                                }
                            }
                        }
                    }
                }

                LiquidBottomTab(
                    onClick = {
                        if (!isSearchItem && !isHomeHoldItem) {
                            selectedTabIndex = index
                            val isCurrentlySelected = isRouteSelected(currentRoute, screen.route, navigationItems)
                            onItemClick(screen, isCurrentlySelected)
                        }
                    },
                    interactionSource = interactionSource
                ) {
                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = stringResource(screen.titleId)
                    )
                    if (!slimNav) {
                        Text(
                            text = stringResource(screen.titleId),
                            maxLines = 1,
                            overflow = TextOverflow.Clip
                        )
                    }
                }
            }
        }
    } else {
        // Fallback: standard Material NavigationBar when no backdrop is available
        NavigationBar(
            modifier = modifier,
            containerColor = if (pureBlack) Color.Black else MaterialTheme.colorScheme.surfaceContainer,
            contentColor = if (pureBlack) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
        ) {
            navigationItems.forEach { screen ->
                val isSelected = remember(currentRoute, screen.route) {
                    isRouteSelected(currentRoute, screen.route, navigationItems)
                }
                val currentIsSelected by rememberUpdatedState(isSelected)
                val iconRes = remember(isSelected, screen) {
                    if (isSelected) screen.iconIdActive else screen.iconIdInactive
                }

                val isSearchItem = screen == Screens.Search && onSearchLongClick != null
                val isHomeHoldItem = screen == Screens.Home && onHomeLongHold != null
                val interactionSource = remember { MutableInteractionSource() }

                if (isSearchItem || isHomeHoldItem) {
                    LaunchedEffect(interactionSource) {
                        var isLongClick = false
                        interactionSource.interactions.collectLatest { interaction ->
                            when (interaction) {
                                is PressInteraction.Press -> {
                                    isLongClick = false
                                    delay(if (isHomeHoldItem) 15_000L else viewConfiguration.longPressTimeoutMillis)
                                    isLongClick = true
                                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                    if (isHomeHoldItem) onHomeLongHold.invoke() else onSearchLongClick?.invoke()
                                }
                                is PressInteraction.Release -> {
                                    if (!isLongClick) {
                                        onItemClick(screen, currentIsSelected)
                                    }
                                }
                                is PressInteraction.Cancel -> {
                                    isLongClick = false
                                }
                            }
                        }
                    }
                }

                NavigationBarItem(
                    selected = isSelected,
                    onClick = {
                        if (!isSearchItem && !isHomeHoldItem) {
                            onItemClick(screen, currentIsSelected)
                        }
                    },
                    interactionSource = interactionSource,
                    icon = {
                        Icon(
                            painter = painterResource(id = iconRes),
                            contentDescription = stringResource(screen.titleId)
                        )
                    },
                    label = if (!slimNav) {
                        {
                            Text(
                                text = stringResource(screen.titleId),
                                maxLines = 1,
                                overflow = TextOverflow.Clip
                            )
                        }
                    } else null
                )
            }
        }
    }
}
