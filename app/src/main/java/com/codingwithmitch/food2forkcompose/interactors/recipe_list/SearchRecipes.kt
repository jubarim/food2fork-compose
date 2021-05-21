package com.codingwithmitch.food2forkcompose.interactors.recipe_list

import com.codingwithmitch.food2fork.network.RecipeService
import com.codingwithmitch.food2forkcompose.cache.RecipeDao
import com.codingwithmitch.food2forkcompose.cache.model.RecipeEntityMapper
import com.codingwithmitch.food2forkcompose.domain.data.DataState
import com.codingwithmitch.food2forkcompose.domain.model.Recipe
import com.codingwithmitch.food2forkcompose.network.model.RecipeDtoMapper
import com.codingwithmitch.food2forkcompose.util.RECIPE_PAGINATION_PAGE_SIZE
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchRecipes(
    private val recipeDao: RecipeDao,
    private val recipeService: RecipeService,
    private val entityMapper: RecipeEntityMapper,
    private val dtoMapper: RecipeDtoMapper,
) {
    fun execute(
        token: String,
        page: Int,
        query: String,
    ): Flow<DataState<List<Recipe>>> = flow {
        try {
            emit(DataState.loading<List<Recipe>>())

            // TODO: just to show pagination / progress bar because API is fast
            delay(1000)

            // TODO("Check if there is an internet connection")
            val recipes = getRecipesFromNetwork(token, page, query)

            // insert into the cache
            recipeDao.insertRecipes(entityMapper.toEntityList(recipes))

            // query the cache
            val cacheResult = if (query.isBlank()) {
                recipeDao.getAllRecipes(RECIPE_PAGINATION_PAGE_SIZE, page)
            } else {
                recipeDao.searchRecipes(
                    query = query,
                    page = page,
                    pageSize = RECIPE_PAGINATION_PAGE_SIZE,
                )
            }

            // emit a list of recipes from the cache
            val list = entityMapper.fromEntityList(cacheResult)

            emit(DataState.success(list))


        } catch (e: Exception) {
            emit(DataState.error<List<Recipe>>(e.message ?: "Unknown Error"))
        }
    }

    // This can throw an exception if there is no network connection
    private suspend fun getRecipesFromNetwork(
        token: String,
        page: Int,
        query: String
    ) = dtoMapper.toDomainList(
        recipeService.search(
            token = token,
            page = page,
            query = query
        ).recipes
    )
}