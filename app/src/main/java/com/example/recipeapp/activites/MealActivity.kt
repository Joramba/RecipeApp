package com.example.recipeapp.activites

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.databinding.ActivityMealBinding
import com.example.recipeapp.db.MealDatabase
import com.example.recipeapp.fragments.HomeFragment
import com.example.recipeapp.pojo.Meal
import com.example.recipeapp.viewModel.MealViewModel
import com.example.recipeapp.viewModel.MealViewModelFactory

class MealActivity : AppCompatActivity() {
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealThumb: String
    private lateinit var youtubeLink: String

    private lateinit var binding: ActivityMealBinding
    private lateinit var mealMvvm: MealViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mealDatabase = MealDatabase.getInstance(this)
        val viewModelFactory = MealViewModelFactory(mealDatabase)
//        mealMvvm = ViewModelProvider(this).get(MealViewModel::class.java)
        mealMvvm = ViewModelProvider(this,viewModelFactory)[MealViewModel::class.java]

        getMealInformationFromIntent()

        setInformationInViews()
        loadingCase()

        mealMvvm.getMealDatail(mealId)
        observeMealDataLiveData()

        onYoutubeImageClick()
        onShareButtonClick()

        onFavorityClick()
    }


    private fun onShareButtonClick(){
        binding.imgShare?.setOnClickListener {
            val message: String = "Meal Name: " + binding.collapsingToolBar.title as String + "\n"+ "Instruction: \n" + binding.tvInstractionText?.text as String
            val intent= Intent()
            intent.action=Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, message)
            intent.type="text/plain"
            startActivity(Intent.createChooser(intent,"Share To:"))
        }
    }
    private fun onFavorityClick() {
        binding.addToFavorite.setOnClickListener{
            mealToSave?.let {
                mealMvvm.insertMeal(it)
                Toast.makeText(this, "Meal Saved", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun onYoutubeImageClick() {
        binding.imgYoutube.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }

    private var mealToSave : Meal? = null
    private fun observeMealDataLiveData() {
        mealMvvm.observeMealDetailLiveData().observe(this,object: Observer<Meal>{
            override fun onChanged(t: Meal?) {
                onResponseCase()
                val meal = t
                mealToSave = meal

                binding.tvCategory.text = "Category: ${meal!!.strCategory}"
                binding.tvArea.text = "Area: ${meal!!.strArea}"
                binding.tvInstractionText?.text = meal.strInstructions
                youtubeLink = meal.strYoutube
            }
        })
    }

    private fun setInformationInViews() {
        Glide.with(applicationContext)
            .load(mealThumb)
            .into(binding.imgMealDetail)

        binding.collapsingToolBar.title = mealName
        binding.collapsingToolBar.setCollapsedTitleTextColor(resources.getColor(R.color.white))
        binding.collapsingToolBar.setExpandedTitleColor(resources.getColor(R.color.white))
    }

    private fun getMealInformationFromIntent(){
        val intent = intent
        mealId = intent.getStringExtra(HomeFragment.MEAL_ID)!!
        mealName = intent.getStringExtra(HomeFragment.MEAL_NAME)!!
        mealThumb = intent.getStringExtra(HomeFragment.MEAL_THUMB)!!
    }

    private fun loadingCase(){
        binding.progressBar.visibility = View.VISIBLE
        binding.addToFavorite.visibility = View.INVISIBLE
        binding.instraction.visibility = View.INVISIBLE
        binding.tvCategory.visibility = View.INVISIBLE
        binding.tvArea.visibility = View.INVISIBLE
        binding.imgYoutube.visibility = View.INVISIBLE
        binding.imgShare?.visibility = View.INVISIBLE
    }

    private fun onResponseCase(){
        binding.progressBar.visibility = View.INVISIBLE
        binding.addToFavorite.visibility = View.VISIBLE
        binding.instraction.visibility = View.VISIBLE
        binding.tvCategory.visibility = View.VISIBLE
        binding.tvArea.visibility = View.VISIBLE
        binding.imgYoutube.visibility = View.VISIBLE
        binding.imgShare?.visibility = View.VISIBLE
    }
}