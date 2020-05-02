package com.isaac.recipeapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import java.io.File
import java.util.*

class RecipeDetailViewModel : ViewModel() {

    private val recipeRepository = RecipeRepository.get()
    private val recipeIdLiveData = MutableLiveData<UUID>()

    var recipeLiveData: LiveData<Recipe?> =
        Transformations.switchMap(recipeIdLiveData){ recipeId->
            recipeRepository.getRecipe(recipeId)
        }

    fun loadRecipe(recipeId: UUID){
        recipeIdLiveData.value = recipeId
    }

    fun saveRecipe(recipe :Recipe){
        recipeRepository.updateRecipe(recipe)
    }

    fun getPhotoFile(recipe :Recipe) : File {
        return recipeRepository.getPhotoFile(recipe)
    }

}