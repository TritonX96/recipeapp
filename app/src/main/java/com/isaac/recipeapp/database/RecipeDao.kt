package com.isaac.recipeapp.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.isaac.recipeapp.Recipe
import java.util.*

@Dao
interface RecipeDao {

    @Query("SELECT * FROM recipe")
    fun getRecipes(): LiveData<List<Recipe>>

    @Query("SELECT * FROM recipe WHERE id=(:id)")
    fun getRecipe(id: UUID): LiveData<Recipe?>

    @Update
    fun upateRecipe(recipe: Recipe)

    @Insert
    fun addRecipe (recipe: Recipe)


}