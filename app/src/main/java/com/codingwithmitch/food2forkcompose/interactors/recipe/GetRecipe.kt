package com.codingwithmitch.food2forkcompose.interactors.recipe

import android.util.Log
import com.codingwithmitch.food2fork.network.RecipeService
import com.codingwithmitch.food2forkcompose.cache.RecipeDao
import com.codingwithmitch.food2forkcompose.cache.model.RecipeEntityMapper
import com.codingwithmitch.food2forkcompose.domain.data.DataState
import com.codingwithmitch.food2forkcompose.domain.model.Recipe
import com.codingwithmitch.food2forkcompose.network.model.RecipeDtoMapper
import com.codingwithmitch.food2forkcompose.util.TAG
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetRecipe(
    private val recipeDao: RecipeDao,
    private val recipeService: RecipeService,
    private val entityMapper: RecipeEntityMapper,
    private val recipeDtoMapper: RecipeDtoMapper,
) {
    fun execute(
        recipeId: Int, token: String
    ): Flow<DataState<Recipe>> = flow {
        
        try {
            emit(DataState.loading<Recipe>())

            // TODO: can be removed later
            delay(1000)

            // Tricky logic
            var recipe = getRecipeFromCache(recipeId)
            if (recipe != null) {
                emit(DataState.success(recipe))
            } else {
                // This else should not happen, but... it might.
                val networkRecipe = getRecipeFromNetwork(token, recipeId)

                recipeDao.insertRecipe(
                    entityMapper.mapFromDomainModel(networkRecipe)
                )

                recipe = getRecipeFromCache(recipeId)
                if (recipe != null) {
                    emit(DataState.success(recipe))
                } else {
                    // we should not get here at all, but...
                    throw Exception("Unable to get the recipe from the cache.")
                }
            }

        } catch (e: Exception) {
            Log.e(TAG, "execute: ${e.message}")

            emit(DataState.error<Recipe>(e.message?: "Unknow error"))
        }
        
    }

    private suspend fun getRecipeFromCache(recipeId: Int): Recipe? {
        return recipeDao.getRecipeById(recipeId)?.let { recipeEntity ->
            entityMapper.mapToDomainModel(recipeEntity)
        }
    }

    private suspend fun getRecipeFromNetwork(token: String, recipeId: Int): Recipe {
        return recipeDtoMapper.mapToDomainModel(recipeService.get(token, recipeId))
    }
}
