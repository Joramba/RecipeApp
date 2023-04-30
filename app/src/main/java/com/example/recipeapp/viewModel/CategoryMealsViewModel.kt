package com.example.recipeapp.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipeapp.pojo.MealByCategory
import com.example.recipeapp.pojo.MealsByCategoryList
import com.example.recipeapp.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryMealsViewModel: ViewModel() {
    val mealsLiveData = MutableLiveData<List<MealByCategory>>()

    fun getMealsByCategory(categoryName: String){
        RetrofitInstance.api.getMealsByCategory(categoryName).enqueue(object:Callback<MealsByCategoryList>{
            override fun onResponse(
                call: Call<MealsByCategoryList>,
                response: Response<MealsByCategoryList>
            ) {
                response.body()?.let { mealslist ->
                    mealsLiveData.postValue(mealslist.meals)
                }
            }

            override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                Log.d("CategoryMealsViewModel", t.message.toString())
            }
        })
    }

    fun observeMealsLiveData(): LiveData<List<MealByCategory>>{
        return mealsLiveData
    }
}