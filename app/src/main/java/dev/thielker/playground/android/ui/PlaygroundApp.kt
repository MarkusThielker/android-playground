@file:Suppress("functionName")

package dev.thielker.playground.android.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Group
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.thielker.playground.android.R
import dev.thielker.playground.android.ui.lobby.ConnectionScreen
import dev.thielker.playground.android.ui.lobby.ConnectionViewModel
import dev.thielker.playground.android.ui.lobby.LobbyScreen
import dev.thielker.playground.android.util.Destination
import dev.thielker.playground.android.util.InsetsScaffold
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@Composable
fun PlaygroundApp() {

    val coroutineScope = rememberCoroutineScope()

    var topBarTitle by remember { mutableStateOf("") }

    // navigation controller
    val navController = rememberNavController()
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination

    // navigation drawer
    val drawerState = rememberDrawerState(DrawerValue.Closed)

    val destinations = listOf(

        Destination(
            route = "lobby",
            icon = Icons.Rounded.Group,
            name = stringResource(id = R.string.nav_destination_lobby)
        ),
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                navController = navController,
                destinations = destinations,
                currentDestination = currentDestination,
                closeDrawer = { coroutineScope.launch { drawerState.close() } },
            )
        },
    ) {

        InsetsScaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                coroutineScope.launch { drawerState.open() }
                            },
                            content = {
                                Icon(
                                    imageVector = Icons.Rounded.Menu,
                                    contentDescription = stringResource(
                                        id = R.string.cd_menu_nav_button
                                    ),
                                )
                            },
                        )
                    },
                    title = { Text(text = topBarTitle) },
                )
            }
        ) {

            NavHost(
                navController = navController,
                startDestination = destinations[0].route,
            ) {

                DashboardNavGraph(
                    destinations = destinations,
                    updateTitle = { topBarTitle = it },
                )
            }
        }
    }
}

@ExperimentalMaterial3Api
private fun NavGraphBuilder.DashboardNavGraph(
    destinations: List<Destination>,
    updateTitle: (String) -> Unit,
) {

    composable(route = destinations[0].route) {

        val viewModel: ConnectionViewModel = viewModel()

        if (viewModel.isHosting) {

            updateTitle(viewModel.mServiceName)

            LobbyScreen(viewModel)

        } else {

            updateTitle(destinations[0].name)

            ConnectionScreen(viewModel)
        }
    }
}

@ExperimentalMaterial3Api
@Composable
private fun DrawerContent(
    navController: NavController,
    destinations: List<Destination>,
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
    closeDrawer: () -> Unit = { },
) {

    val insets = WindowInsets.statusBars.union(WindowInsets(top = 10.dp))

    Column(
        modifier = modifier
            .padding(12.dp)
            .windowInsetsPadding(insets),
    ) {

        destinations.forEach { destination ->

            NavigationDrawerItem(
                icon = {
                    Icon(
                        imageVector = destination.icon,
                        contentDescription = destination.name,
                    )
                },
                label = { Text(text = destination.name) },
                selected = currentDestination?.route == destination.route,
                onClick = {
                    navController.navigate(route = destination.route)
                    closeDrawer()
                },
            )
        }
    }
}
