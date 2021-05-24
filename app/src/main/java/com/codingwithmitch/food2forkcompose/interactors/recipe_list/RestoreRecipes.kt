package com.codingwithmitch.food2forkcompose.interactors.recipe_list

import android.util.Log
import com.codingwithmitch.food2forkcompose.cache.RecipeDao
import com.codingwithmitch.food2forkcompose.cache.model.RecipeEntityMapper
import com.codingwithmitch.food2forkcompose.domain.data.DataState
import com.codingwithmitch.food2forkcompose.domain.model.Recipe
import com.codingwithmitch.food2forkcompose.util.RECIPE_PAGINATION_PAGE_SIZE
import com.codingwithmitch.food2forkcompose.util.TAG
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RestoreRecipes (
    private val recipeDao: RecipeDao,
    private val entityMapper: RecipeEntityMapper,
) {
    fun execute(
        page: Int,
        query: String,
    ): Flow<DataState<List<Recipe>>> = flow {
        try {
            emit(DataState.loading())

            delay(1000)

            val cacheResult = if (query.isBlank()) {
                recipeDao.restoreAllRecipes(
                    pageSize = RECIPE_PAGINATION_PAGE_SIZE,
                    page = page
                )
            } else {
                recipeDao.restoreRecipes(
                    query = query,
                    pageSize = RECIPE_PAGINATION_PAGE_SIZE,
                    page = page
                )
            }

            val list = entityMapper.fromEntityList(cacheResult)

            emit(DataState.success(list))

        } catch (e: Exception) {
            Log.e(TAG, "execute: ${e.message}" )
            emit(DataState.error<List<Recipe>>(e.message?: "Unknown error"))
        }
    }
}
