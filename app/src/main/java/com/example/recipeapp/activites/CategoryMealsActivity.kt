package com.example.recipeapp.activites

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.recipeapp.adapters.CategoryMealsAdapter
import com.example.recipeapp.databinding.ActivityCategoryMealsBinding
import com.example.recipeapp.fragments.HomeFragment
import com.example.recipeapp.istablet.Device
import com.example.recipeapp.istablet.getDeviceInfo
import com.example.recipeapp.viewModel.CategoryMealsViewModel

class CategoryMealsActivity : AppCompatActivity() {
    lateinit var binding: ActivityCategoryMealsBinding
    lateinit var categoryMealsViewModel : CategoryMealsViewModel
    lateinit var categotyMealsAdapter: CategoryMealsAdapter
    lateinit var text: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategoryMealsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareRecyclerView()

        categoryMealsViewModel = ViewModelProvider(this)[CategoryMealsViewModel::class.java]

        categoryMealsViewModel.getMealsByCategory(intent.getStringExtra(HomeFragment.CATEGOTY_NAME)!!)

        categoryMealsViewModel.observeMealsLiveData().observe(this, Observer { mealsList ->
            binding.tvCategotyCount.text = mealsList.size.toString()
            categotyMealsAdapter.setMealsList(mealsList)

        })

        onCategoryClick()
    }

//    override fun onConfigurationChanged(newConfig: Configuration) {
//        super.onConfigurationChanged(newConfig)
//        categotyMealsAdapter = CategoryMealsAdapter()
//        text = getDeviceInfo(applicationContext, Device.DEVICE_TYPE)
//
//        if(text == "Tablet"){
//            binding.rvMeals.apply {
//                layoutManager = GridLayoutManager(context, 4, GridLayoutManager.VERTICAL, false)
//            }
//        } else {
//            if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                binding.rvMeals.apply {
//                    layoutManager = GridLayoutManager(context, 4, GridLayoutManager.VERTICAL, false)
//                    adapter = categotyMealsAdapter
//
//            }
//            if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
//                binding.rvMeals.apply {
//                    layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
//                    adapter = categotyMealsAdapter
//            }
//        }
//    }


    private fun onCategoryClick() {
        categotyMealsAdapter.onItemClick = {meal ->
            val intent = Intent(this, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID, meal.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME, meal.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun prepareRecyclerView() {
        categotyMealsAdapter = CategoryMealsAdapter()

        text = getDeviceInfo(applicationContext, Device.DEVICE_TYPE)

        if(text == "Tablet"){
            binding.rvMeals.apply {
                layoutManager = GridLayoutManager(context, 4, GridLayoutManager.VERTICAL, false)
                adapter = categotyMealsAdapter
            }
        } else {
            binding.rvMeals.apply {
                layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
                adapter = categotyMealsAdapter
            }
        }

    }
}