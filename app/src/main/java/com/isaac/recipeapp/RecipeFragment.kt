package com.isaac.recipeapp


import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import okhttp3.internal.Version
import java.io.File
import java.util.*

private const val TAG= "RecipeFragment"
private const val ARG_RECIPE_ID = "recipe_id"
private const val REQUEST_PHOTO = 2

class RecipeFragment: Fragment() {

    private lateinit var recipe: Recipe
    private lateinit var photoFile: File
    private lateinit var photoUri: Uri

    private lateinit var recipeName: EditText
    private lateinit var recipeDesc: EditText
    private lateinit var recipeIngredient: EditText
    private lateinit var recipeInstruction: EditText


    private lateinit var photoButton: Button
    private lateinit var photoView: ImageView
    private val recipeDetailViewModel: RecipeDetailViewModel by lazy {
        ViewModelProviders.of(this).get(RecipeDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipe = Recipe()
        val recipeId: UUID = arguments?.getSerializable(ARG_RECIPE_ID) as UUID
        recipeDetailViewModel.loadRecipe(recipeId)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_recipe, container, false)

        recipeName = view.findViewById(R.id.title_edittext) as EditText
        recipeDesc = view.findViewById(R.id.desc_editext) as EditText
        recipeIngredient = view.findViewById(R.id.ingredients_editext) as EditText
        recipeInstruction = view.findViewById(R.id.inst_editext) as EditText

        photoButton = view.findViewById(R.id.choose_image) as Button
        photoView = view.findViewById(R.id.recipe_image) as ImageView






        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recipeDetailViewModel.recipeLiveData.observe(
            viewLifecycleOwner,
            Observer { recipe ->
                recipe?.let{
                    this.recipe = recipe
                    photoFile = recipeDetailViewModel.getPhotoFile(recipe)
                    photoUri = FileProvider.getUriForFile(requireActivity(),
                        "com.isaac.recipeapp.fileprovider",photoFile)
                    updateUI()
                }
            }
        )

    }

    override fun onStart() {
        super.onStart()

        //Recipe Title
        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                recipe.recipeName = sequence.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        recipeName.addTextChangedListener(titleWatcher)


        //Recipe Desc
        val descWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                recipe.recipeDesc = sequence.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        recipeDesc.addTextChangedListener(descWatcher)

        //Recipe Ingredient
        val ingredientWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                recipe.recipeIngredient = sequence.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        recipeIngredient.addTextChangedListener(ingredientWatcher)

        //Recipe Instruction
        val instructionWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                recipe.recipeInstruction = sequence.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        recipeInstruction.addTextChangedListener(instructionWatcher)

        photoButton.apply{
            val packageManager: PackageManager = requireActivity().packageManager

            val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val resolvedActivity : ResolveInfo? =
                packageManager.resolveActivity(captureImage, PackageManager.MATCH_DEFAULT_ONLY)
            if(resolvedActivity == null){
                isEnabled = false
            }
            setOnClickListener{
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT,photoUri)

                val cameraActivities: List<ResolveInfo> =
                    packageManager.queryIntentActivities(captureImage,
                        PackageManager.MATCH_DEFAULT_ONLY)

                for (cameraActivity in cameraActivities){
                    requireActivity().grantUriPermission(
                        cameraActivity.activityInfo.packageName,
                        photoUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

                    startActivityForResult(captureImage, REQUEST_PHOTO)
                }

            }

        }



}



    override fun onStop(){
        super.onStop()
        recipeDetailViewModel.saveRecipe(recipe)
    }

    override fun onDetach(){
        super.onDetach()
        requireActivity().revokeUriPermission(photoUri,
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    }


    private fun updateUI(){
        recipeName.setText(recipe.recipeName)
        recipeDesc.setText(recipe.recipeDesc)
        recipeIngredient.setText(recipe.recipeIngredient)
        recipeInstruction.setText(recipe.recipeInstruction)
        updatePhotoView()
    }

    private fun updatePhotoView(){
        if(photoFile.exists()) {
            val bitmap = getScaledBitmap(photoFile.path, requireActivity())
            photoView.setImageBitmap(bitmap)
        }else{
            photoView.setImageDrawable(null)
        }
    }
    companion object{
        fun newInstance(recipeId: UUID): RecipeFragment{
            val args = Bundle().apply{
                putSerializable(ARG_RECIPE_ID, recipeId)
            }
            return RecipeFragment().apply{
                arguments = args
            }
        }
    }
}