package com.codingwithmitch.food2forkcompose.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.viewinterop.viewModel
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.codingwithmitch.food2forkcompose.presentation.navigation.Screen
import com.codingwithmitch.food2forkcompose.presentation.ui.recipe.RecipeDetailScreen
import com.codingwithmitch.food2forkcompose.presentation.ui.recipe.RecipeViewModel
import com.codingwithmitch.food2forkcompose.presentation.ui.recipe_list.RecipeListScreen
import com.codingwithmitch.food2forkcompose.presentation.ui.recipe_list.RecipeListViewModel
import com.codingwithmitch.food2forkcompose.presentation.util.ConnectivityManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@ExperimentalMaterialApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var connectivityManager: ConnectivityManager

    override fun onStart() {
        super.onStart()

        connectivityManager.registerConnectionObserver(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        connectivityManager.unregisterConnectionObserver(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController, startDestination = Screen.RecipeList.route
            ) {

                composable(route = Screen.RecipeList.route) { navBackStackEntry ->
                    val factory = HiltViewModelFactory(AmbientContext.current, navBackStackEntry)
                    val viewModel: RecipeListViewModel = viewModel(
                        "RecipeListViewModel", factory
                    )
                    RecipeListScreen(
                        isDarkTheme = (application as BaseApplication).isDark.value,
                        onToggleTheme = (application as BaseApplication)::toggleLightTheme,
                        onNavigateToRecipeDetailScreen = navController::navigate, // it passes the route here
                        viewModel = viewModel
                    )
                }

                composable(
                    route = Screen.RecipeDetail.route + "/{$RECIPE_ID}",
                    arguments = listOf(navArgument(RECIPE_ID) { type = NavType.IntType } )
                ) { navBackStackEntry ->
                    val factory = HiltViewModelFactory(AmbientContext.current, navBackStackEntry)
                    val viewModel: RecipeViewModel = viewModel(
                        "RecipeViewModel", factory
                    )
                    RecipeDetailScreen(
                        isDarkTheme = (application as BaseApplication).isDark.value,
                        recipeId = navBackStackEntry.arguments?.getInt(RECIPE_ID),
                        viewModel = viewModel
                    )
                }
            }
        }
    }

    companion object {
        // Used for navigation argument
        const val RECIPE_ID = "recipeId"
    }
}


