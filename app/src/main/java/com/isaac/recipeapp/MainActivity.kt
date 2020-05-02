package com.isaac.recipeapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.isaac.recipeapp.RecipeListFragment.Companion.newInstance
import java.util.*

private const val TAG = "MainActivity "


class MainActivity : AppCompatActivity(),RecipeListFragment.Callbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
            val fragment = RecipeListFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }

    override fun onRecipeSelected(recipeId: UUID) {

     val fragment = RecipeFragment.newInstance(recipeId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container,fragment)
            .addToBackStack(null)
            .commit()
    }
}