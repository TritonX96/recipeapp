package com.isaac.recipeapp

import android.app.Application


class RecipeIntentApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        RecipeRepository.initialize(this)
    }
}
