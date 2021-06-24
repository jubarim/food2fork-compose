package com.codingwithmitch.food2forkcompose.cache

import com.codingwithmitch.food2forkcompose.cache.model.RecipeEntity

class RecipeDaoFake(
    private val appDatabaseFake: AppDatabaseFake
): RecipeDao {
    override suspend fun insertRecipe(recipe: RecipeEntity): Long {
        // to test errors, just check for a specific id and throw an error
        appDatabaseFake.recipes.add(recipe)
        return 1
    }

    override suspend fun insertRecipes(recipes: List<RecipeEntity>): LongArray {
        appDatabaseFake.recipes.addAll(recipes)
        return longArrayOf(1)
    }

    override suspend fun getRecipeById(id: Int): RecipeEntity? {
        return appDatabaseFake.recipes.find { it.id == id }
    }

    override suspend fun deleteRecipes(ids: List<Int>): Int {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllRecipes() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteRecipe(primaryKey: Int): Int {
        TODO("Not yet implemented")
    }

    override suspend fun searchRecipes(
        query: String,
        page: Int,
        pageSize: Int
    ): List<RecipeEntity> {
        // keep it simple - return all
        return appDatabaseFake.recipes
    }

    override suspend fun getAllRecipes(page: Int, pageSize: Int): List<RecipeEntity> {
        return appDatabaseFake.recipes
    }

    override suspend fun restoreRecipes(
        query: String,
        page: Int,
        pageSize: Int
    ): List<RecipeEntity> {
        // keep it simple - return all
        return appDatabaseFake.recipes

    }

    override suspend fun restoreAllRecipes(page: Int, pageSize: Int): List<RecipeEntity> {
        // keep it simple - return all
        return appDatabaseFake.recipes
    }
}
