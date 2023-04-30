package com.example.recipeapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.recipeapp.R
import com.example.recipeapp.activites.CategoryMealsActivity
import com.example.recipeapp.activites.MainActivity
import com.example.recipeapp.activites.MealActivity
import com.example.recipeapp.adapters.CategoriesAdapter
import com.example.recipeapp.adapters.MostPopularAdapter
import com.example.recipeapp.databinding.FragmentHomeBinding
import com.example.recipeapp.fragments.bottomsheet.MealBottomSheetFragment
import com.example.recipeapp.istablet.Device
import com.example.recipeapp.istablet.getDeviceInfo
import com.example.recipeapp.pojo.MealByCategory
import com.example.recipeapp.pojo.Meal
import com.example.recipeapp.viewModel.HomeViewModel

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var randomMeal:Meal
    private lateinit var popularItemsAdapter: MostPopularAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    companion object{
        const val MEAL_ID = "com.example.recipeapp.fragments.idMeal"
        const val MEAL_NAME = "com.example.recipeapp.fragments.nameMeal"
        const val MEAL_THUMB = "com.example.recipeapp.fragments.nameThumb"
        const val CATEGOTY_NAME = "com.example.recipeapp.fragments.categoryName"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        popularItemsAdapter = MostPopularAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getRandomMeal()

        preparePopularItemRecycleView()

        observerRandomMeal()
        onRandomMealClick()

        viewModel.getPopularItems()
        observePopularItemsLiveData()

        onPopularItemClick()

        prepareCategoriesRecyclerView()

        viewModel.getCategoris()
        observeCategoriesLiveData()

        onCategoryClick()

        onPopularItemLongClick()

        onSearchIconClick()

    }

    private fun onSearchIconClick() {
        binding.imgSearch.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
        }
    }

    private fun onPopularItemLongClick() {
        popularItemsAdapter.longItemClick = { meal ->
            val mealBottomSheetFragment = MealBottomSheetFragment.newInstance(meal.idMeal)
            mealBottomSheetFragment.show(childFragmentManager, "Meal Info")
        }
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = {category ->
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(CATEGOTY_NAME, category.strCategory)
            startActivity(intent)
        }
    }

    private fun prepareCategoriesRecyclerView() {
        categoriesAdapter = CategoriesAdapter()
        val text = context?.let { getDeviceInfo(it, Device.DEVICE_TYPE) }

        if(text == "Tablet"){
            binding.recViewCategories.apply {
                layoutManager = GridLayoutManager(context, 4, GridLayoutManager.VERTICAL,false)
                adapter = categoriesAdapter
            }
        } else {
            binding.recViewCategories.apply {
                layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL,false)
                adapter = categoriesAdapter
            }
        }


    }

    private fun observeCategoriesLiveData() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner, Observer { categories ->
            categoriesAdapter.setCategoryList(categories)
        })
    }

    private fun onPopularItemClick() {
        popularItemsAdapter.onItemClick = {meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, meal.idMeal)
            intent.putExtra(MEAL_NAME, meal.strMeal)
            intent.putExtra(MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun preparePopularItemRecycleView() {
        binding.recViewMealsPopular.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularItemsAdapter
        }
    }

    private fun observePopularItemsLiveData() {
        viewModel.observePopularLiveData().observe(viewLifecycleOwner) { mealList ->
            popularItemsAdapter.setMeals(mealsList = mealList as ArrayList<MealByCategory>)
        }
    }

    private fun onRandomMealClick() {
        binding.randomMealCard.setOnClickListener{
            var intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(MEAL_ID, randomMeal.idMeal)
            intent.putExtra(MEAL_NAME, randomMeal.strMeal)
            intent.putExtra(MEAL_THUMB, randomMeal.strMealThumb)
            startActivity(intent)
        }
    }

    private fun observerRandomMeal() {
        viewModel.observeRandomMealLiveData().observe(viewLifecycleOwner
        ) { meal ->
            Glide.with(this@HomeFragment)
                .load(meal!!.strMealThumb)
                .into(binding.imgRandomMeal)

            this.randomMeal = meal
        }
    }
}