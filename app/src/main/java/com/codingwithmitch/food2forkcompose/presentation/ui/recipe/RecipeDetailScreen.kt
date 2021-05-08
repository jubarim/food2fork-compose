package com.codingwithmitch.food2forkcompose.presentation.ui.recipe

import android.util.Log
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.codingwithmitch.food2forkcompose.util.TAG
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Composable
fun RecipeDetailScreen(
    isDarkTheme: Boolean,
    recipeId: Int?,
    viewModel: RecipeViewModel
) {
    Log.d(TAG , "RecipeDetailScreen: $viewModel")

    Text("Recipe id: $recipeId ", style = MaterialTheme.typography.h2)

//    val loading = viewModel.loading.value
//
//    val recipe = viewModel.recipe.value
//
//    val scaffoldState = rememberScaffoldState()
//
//    AppTheme(
//        displayProgressBar = loading,
//        scaffoldState = scaffoldState,
//        darkTheme = application.isDark.value,
//    ){
//        Scaffold(
//            scaffoldState = scaffoldState,
//            snackbarHost = {
//                scaffoldState.snackbarHostState
//            }
//        ) {
//            Box (
//                modifier = Modifier.fillMaxSize()
//            ){
//                if (loading && recipe == null) LoadingRecipeShimmer(imageHeight = IMAGE_HEIGHT.dp)
//                else recipe?.let {
//                    if(it.id == 1) { // force an error to demo snackbar
//                        snackbarController.getScope().launch {
//                            snackbarController.showSnackbar(
//                                scaffoldState = scaffoldState,
//                                message = "An error occurred with this recipe",
//                                actionLabel = "Ok"
//                            )
//                        }
//                    }
//                    else{
//                        RecipeView(
//                            recipe = it,
//                        )
//                    }
//                }
//            }
//        }
//    }
}