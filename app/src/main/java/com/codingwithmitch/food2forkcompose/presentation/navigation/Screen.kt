package com.codingwithmitch.food2forkcompose.presentation.navigation

sealed class Screen(
    route: String
) {
    object RecipeList : Screen("recipeList")

    object RecipeDetail : Screen("recipeDetail")
}