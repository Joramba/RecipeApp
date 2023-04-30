package com.example.recipeapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipeapp.db.MealDatabase
import com.example.recipeapp.pojo.*
import com.example.recipeapp.retrofit.RetrofitInstance
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(
    private  val mealDatabase: MealDatabase
): ViewModel() {
    private var randomMealLiveData = MutableLiveData<Meal>()
    private var popularItemsLiveData = MutableLiveData<List<MealByCategory>>()
    private var categoriesLiveData = MutableLiveData<List<Category>>()
    private var favoritesMealsLiveData = mealDatabase.mealDao().getAllMeals()
    private var bottomSheetMealLiveData = MutableLiveData<Meal>()
    private var searchedMealsLiveData = MutableLiveData<List<Meal>>()

    private var saveStateRandomMeal : Meal ?= null
    fun getRandomMeal(){
        saveStateRandomMeal?.let { randomMeal ->
            randomMealLiveData.postValue(randomMeal)
            return
        }


        RetrofitInstance.api.getRandomMeal().enqueue(object: Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if(response.body() != null){
                    val randomMeal: Meal = response.body()!!.meals[0]
                    randomMealLiveData.value = randomMeal
                    saveStateRandomMeal = randomMeal
                } else {
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }
        })
    }

    fun getPopularItems(){
        RetrofitInstance.api.getPopularItems("Seafood").enqueue(object: Callback<MealsByCategoryList>{
            override fun onResponse(call: Call<MealsByCategoryList>, response: Response<MealsByCategoryList>) {
                if(response.body() != null){
                    popularItemsLiveData.value = response.body()!!.meals
                }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.d("HomeFragment", t.message.toString())
            }
        })
    }

    fun getCategoris(){
        RetrofitInstance.api.getCategories().enqueue(object : Callback<CategotyList>{
            override fun onResponse(call: Call<CategotyList>, response: Response<CategotyList>) {
                response.body()?.let{categotyList ->
                    categoriesLiveData.postValue(categotyList.categories)
                }
            }

            override fun onFailure(call: Call<CategotyList>, t: Throwable) {
                Log.d("HomeViewModel", t.message.toString())
            }
        })
    }

    fun getMealById(id: String){
        RetrofitInstance.api.getMealDetails(id).enqueue(object : Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val meal = response.body()?.meals?.first()
                meal?.let { meal ->
                    bottomSheetMealLiveData.postValue(meal)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeViewModel", t.message.toString())
            }
        })
    }

    fun deleteMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().delete(meal)
        }
    }

    fun insertMeal(meal: Meal){
        viewModelScope.launch {
            mealDatabase.mealDao().upsert(meal)
        }
    }

    fun searchMeals(searchQuery: String) = RetrofitInstance.api.searchMeals(searchQuery).enqueue(
        object : Callback<MealList>{
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                val mealsList = response.body()?.meals
                mealsList?.let {
                    searchedMealsLiveData.postValue(it)
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("HomeViewModel", t.message.toString())
            }
        }
    )

    fun observeSearchedMealsLiveData(): LiveData<List<Meal>> = searchedMealsLiveData


    fun observeRandomMealLiveData(): LiveData<Meal>{
        return randomMealLiveData
    }

    fun observePopularLiveData():LiveData<List<MealByCategory>>{
        return popularItemsLiveData
    }

    fun observeCategoriesLiveData(): LiveData<List<Category>>{
        return categoriesLiveData
    }

    fun observeFavoritesMealsLiveData(): LiveData<List<Meal>>{
        return favoritesMealsLiveData
    }

    fun observeBottomSheetMeal(): LiveData<Meal> = bottomSheetMealLiveData
}