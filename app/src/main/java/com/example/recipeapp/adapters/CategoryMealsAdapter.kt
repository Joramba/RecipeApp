package com.example.recipeapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.databinding.MealItemBinding
import com.example.recipeapp.pojo.Meal
import com.example.recipeapp.pojo.MealByCategory

class CategoryMealsAdapter: RecyclerView.Adapter<CategoryMealsAdapter.CategoryMealsViewModel>() {
    lateinit var onItemClick: ((MealByCategory) -> Unit)
    private var mealsList = ArrayList<MealByCategory>()

    fun setMealsList(mealsList: List<MealByCategory>){
        this.mealsList = mealsList as ArrayList<MealByCategory>
        notifyDataSetChanged()
    }

    inner class CategoryMealsViewModel(val binding: MealItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryMealsViewModel {
        return CategoryMealsViewModel(
            MealItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryMealsViewModel, position: Int) {
        Glide.with(holder.itemView).load(mealsList[position].strMealThumb).into(holder.binding.imgMeal)
        holder.binding.tvMealName.text = mealsList[position].strMeal
        holder.itemView.setOnClickListener{
            onItemClick.invoke(mealsList[position])
        }
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }
}