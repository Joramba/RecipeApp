package com.example.recipeapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapp.databinding.PopularItemsBinding
import com.example.recipeapp.pojo.MealByCategory

class MostPopularAdapter():RecyclerView.Adapter<MostPopularAdapter.PopularMealViewHolder>() {
    lateinit var onItemClick: ((MealByCategory) -> Unit)
    var longItemClick: ((MealByCategory) -> Unit)? = null
    private var mealsList = ArrayList<MealByCategory>()

    fun setMeals(mealsList: ArrayList<MealByCategory>){
        this.mealsList = mealsList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealViewHolder {
        return PopularMealViewHolder(PopularItemsBinding.inflate(LayoutInflater.from(parent.context),parent, false))
    }

    override fun onBindViewHolder(holder: PopularMealViewHolder, position: Int) {
       Glide.with(holder.itemView)
           .load(mealsList[position].strMealThumb)
           .into(holder.binding.imgPopularMealItem)

        holder.itemView.setOnClickListener{
            onItemClick.invoke(mealsList[position])
        }

        holder.itemView.setOnLongClickListener{
            longItemClick?.invoke(mealsList[position])
            true
        }
    }

    override fun getItemCount(): Int {
        return mealsList.size
    }

    class PopularMealViewHolder(val binding: PopularItemsBinding): RecyclerView.ViewHolder(binding.root)

}
