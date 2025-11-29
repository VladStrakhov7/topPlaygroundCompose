package com.example.topplaygroundcompose.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.topplaygroundcompose.presentation.screen.MatchDetailScreen
import com.example.topplaygroundcompose.presentation.screen.MatchesScreen
import com.example.topplaygroundcompose.presentation.screen.SavedMatchesScreen

sealed class Screen(val route: String) {
    object Matches : Screen("matches")
    object SavedMatches : Screen("saved_matches")
    object MatchDetail : Screen("match_detail/{matchId}") {
        fun createRoute(matchId: Int) = "match_detail/$matchId"
    }
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Matches.route
    ) {
        composable(Screen.Matches.route) {
            MatchesScreen(
                onMatchClick = { matchId ->
                    navController.navigate(Screen.MatchDetail.createRoute(matchId))
                },
                onSavedMatchesClick = {
                    navController.navigate(Screen.SavedMatches.route)
                }
            )
        }
        composable(Screen.SavedMatches.route) {
            SavedMatchesScreen(
                onMatchClick = { matchId ->
                    navController.navigate(Screen.MatchDetail.createRoute(matchId))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = Screen.MatchDetail.route,
            arguments = listOf(navArgument("matchId") { type = NavType.IntType })
        ) { backStackEntry ->
            val matchId = backStackEntry.arguments?.getInt("matchId") ?: 0
            MatchDetailScreen(
                matchId = matchId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}

