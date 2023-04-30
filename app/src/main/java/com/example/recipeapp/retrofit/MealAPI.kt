package com.example.recipeapp.retrofit

import com.example.recipeapp.pojo.CategotyList
import com.example.recipeapp.pojo.MealByCategory
import com.example.recipeapp.pojo.MealsByCategoryList
import com.example.recipeapp.pojo.MealList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealAPI {

    @GET("random.php")
    fun getRandomMeal(): Call<MealList>

    @GET("lookup.php")
    fun getMealDetails(@Query("i") id: String): Call<MealList>

    @GET("filter.php")
    fun getPopularItems(@Query("c") categoryName: String) : Call<MealsByCategoryList>

    @GET("categories.php")
    fun getCategories(): Call<CategotyList>

    @GET("filter.php")
    fun getMealsByCategory(@Query("c") categoryName: String) :Call<MealsByCategoryList>

    @GET("search.php")
    fun searchMeals(@Query("s") searchQuery: String) : Call<MealList>
}