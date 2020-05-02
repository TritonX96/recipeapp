package com.isaac.recipeapp

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.isaac.recipeapp.database.RecipeDatabase
import java.io.File
import java.lang.IllegalStateException
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "recipe-database"

class RecipeRepository private constructor(context: Context) {

    private val database : RecipeDatabase = Room.databaseBuilder(
        context.applicationContext,
        RecipeDatabase::class.java,
        DATABASE_NAME)
        .build()

    private val recipeDao = database.recipeDao()
    private val executor = Executors.newSingleThreadExecutor()
    private val filesDir = context.applicationContext.filesDir

    fun getRecipes(): LiveData<List<Recipe>> = recipeDao.getRecipes()

    fun getRecipe(id: UUID): LiveData<Recipe?> = recipeDao.getRecipe(id)

    fun updateRecipe(recipe:Recipe){
        executor.execute{
            recipeDao.upateRecipe(recipe)
        }
    }

    fun addRecipe(recipe: Recipe){
        executor.execute{
            recipeDao.addRecipe(recipe)
        }
    }

    fun getPhotoFile(recipe:Recipe) : File = File(filesDir, recipe.photoFileName)

    companion object {
        private var INSTANCE: RecipeRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = RecipeRepository(context)
            }
        }

        fun get(): RecipeRepository {
            return INSTANCE ?:
            throw IllegalStateException("RecipeRepository must be initialized")
        }
    }
}
