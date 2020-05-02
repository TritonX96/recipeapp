package com.isaac.recipeapp

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID
@Entity
data class Recipe(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    var recipeName : String = "",
    var recipeDesc: String = "",
    var recipeIngredient: String= "",
    var recipeInstruction: String = "")
{
    val photoFileName
        get() = "IMG_$id.jpg"
}