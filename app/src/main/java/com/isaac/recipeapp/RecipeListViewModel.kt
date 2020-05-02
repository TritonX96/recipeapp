package com.isaac.recipeapp

import androidx.lifecycle.ViewModel

class RecipeListViewModel: ViewModel() {

    private val recipeRepository = RecipeRepository.get()
    val recipeListLiveData = recipeRepository.getRecipes()

    fun addRecipe(recipe : Recipe){
        recipeRepository.addRecipe(recipe)
    }
}