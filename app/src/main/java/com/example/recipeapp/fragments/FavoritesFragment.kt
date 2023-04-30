package com.example.recipeapp.fragments

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapp.activites.MainActivity
import com.example.recipeapp.activites.MealActivity
import com.example.recipeapp.adapters.MealsAdapter
import com.example.recipeapp.databinding.FragmentFavoritesBinding
import com.example.recipeapp.istablet.Device
import com.example.recipeapp.istablet.getDeviceInfo
import com.example.recipeapp.viewModel.HomeViewModel

class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var viewModel : HomeViewModel
    private lateinit var favoritesAdapter: MealsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater)
        return binding.root
    }

    private fun onFavoritesItemClick() {
        favoritesAdapter.onItemClick = { meal ->
            val intent = Intent(activity, MealActivity::class.java)
            intent.putExtra(HomeFragment.MEAL_ID, meal.idMeal)
            intent.putExtra(HomeFragment.MEAL_NAME, meal.strMeal)
            intent.putExtra(HomeFragment.MEAL_THUMB, meal.strMealThumb)
            startActivity(intent)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()
        observeFavorites()

        onFavoritesItemClick()

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                viewModel.deleteMeal(favoritesAdapter.differ.currentList[position])

//                Snackbar.make(requireView(), "Meal deleted", Snackbar.LENGTH_LONG).setAction(
//                    "Undo",
//                    view.setOnClickListener{
//                        viewModel.insertMeal(favoritesAdapter.differ.currentList[position])
//                    }
//                ).show()
            }
        }

        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding.rvFavorites)
    }

    private fun prepareRecyclerView() {
        favoritesAdapter = MealsAdapter()
        val text = context?.let { getDeviceInfo(it, Device.DEVICE_TYPE) }

        if(text == "Tablet"){
            binding.rvFavorites.apply {
                layoutManager = GridLayoutManager(context, 4, GridLayoutManager.VERTICAL, false)
                adapter = favoritesAdapter
            }
        } else {
            binding.rvFavorites.apply {
                layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
                adapter = favoritesAdapter
            }
        }
    }

    private fun observeFavorites() {
        viewModel.observeFavoritesMealsLiveData().observe(requireActivity(), Observer { meals ->
            favoritesAdapter.differ.submitList(meals)
        })
    }
}
