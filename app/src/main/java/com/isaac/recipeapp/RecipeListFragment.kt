package com.isaac.recipeapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

private const val TAG = "RecipeListFragment"

class RecipeListFragment  : Fragment(){
    private var callbacks: Callbacks? = null
    private lateinit var recipeRecyclerView: RecyclerView
    private var adapter: RecipeAdapter? = RecipeAdapter(emptyList())

    private val recipeListViewModel: RecipeListViewModel by lazy {
        ViewModelProviders.of(this).get(RecipeListViewModel::class.java)
    }

    interface Callbacks{
        fun onRecipeSelected(recipeId: UUID)
    }

    override fun onAttach(context: Context){
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipe_list, container, false)

        recipeRecyclerView =
            view.findViewById(R.id.recipe_recycler_view) as RecyclerView
        recipeRecyclerView.layoutManager = LinearLayoutManager(context)
        recipeRecyclerView.adapter = adapter


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recipeListViewModel.recipeListLiveData.observe(
            viewLifecycleOwner,
            Observer { recipes ->
                recipes?.let {
                    Log.i(TAG, "Got recipes ${recipes.size}")
                    updateUI(recipes)
                }
            })
    }

    override fun onDetach(){
        super.onDetach()
        callbacks = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_recipe_list,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_recipe->{
                val recipe = Recipe()
                recipeListViewModel.addRecipe(recipe)
                callbacks?.onRecipeSelected(recipe.id)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun updateUI(recipes: List<Recipe>) {
        adapter?.let {
            it.recipes = recipes
        } ?: run {
            adapter = RecipeAdapter(recipes)
        }
        recipeRecyclerView.adapter = adapter
    }

    private inner class RecipeHolder(view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var recipe: Recipe

        private val titleTextView: TextView = itemView.findViewById(R.id.recipeName_Text)
        //private val imageView: ImageView = itemView.findViewById(R.id.imageRecipe_list)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(recipe: Recipe) {
            this.recipe = recipe
            titleTextView.text = this.recipe.recipeName


        }

        override fun onClick(v: View) {
            callbacks?.onRecipeSelected(recipe.id)
        }
    }

    private inner class RecipeAdapter(var recipes: List<Recipe>)
        : RecyclerView.Adapter<RecipeHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : RecipeHolder {
            val layoutInflater = LayoutInflater.from(context)
            val view = layoutInflater.inflate(R.layout.list_item_recipe, parent, false)
            return RecipeHolder(view)
        }

        override fun onBindViewHolder(holder: RecipeHolder, position: Int) {
            val recipe = recipes[position]
            holder.bind(recipe)
        }

        override fun getItemCount() = recipes.size
    }

    companion object {
        fun newInstance(): RecipeListFragment {
            return RecipeListFragment()
        }
    }
}