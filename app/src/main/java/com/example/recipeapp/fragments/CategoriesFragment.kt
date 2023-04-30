package com.example.recipeapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.recipeapp.activites.CategoryMealsActivity
import com.example.recipeapp.activites.MainActivity
import com.example.recipeapp.adapters.CategoriesAdapter
import com.example.recipeapp.databinding.FragmentCategoriesBinding
import com.example.recipeapp.istablet.Device
import com.example.recipeapp.istablet.getDeviceInfo
import com.example.recipeapp.viewModel.HomeViewModel

class categoriesFragment : Fragment() {

    private lateinit var binding: FragmentCategoriesBinding
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoriesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareRecyclerView()

        observeCategories()

        onCategoryClick()
    }

    private fun onCategoryClick() {
        categoriesAdapter.onItemClick = {category ->
            val intent = Intent(activity, CategoryMealsActivity::class.java)
            intent.putExtra(HomeFragment.CATEGOTY_NAME, category.strCategory)
            startActivity(intent)
        }
    }

    private fun observeCategories() {
        viewModel.observeCategoriesLiveData().observe(viewLifecycleOwner) { categories ->
            categoriesAdapter.setCategoryList(categories)
        }
    }


    private fun prepareRecyclerView() {
        categoriesAdapter = CategoriesAdapter()

        val text = context?.let { getDeviceInfo(it, Device.DEVICE_TYPE) }

        if(text == "Tablet"){
            binding.rvCategories.apply {
                layoutManager = GridLayoutManager(context, 5,GridLayoutManager.VERTICAL,false)
                adapter = categoriesAdapter
            }
        } else {
            binding.rvCategories.apply {
                layoutManager = GridLayoutManager(context, 3,GridLayoutManager.VERTICAL,false)
                adapter = categoriesAdapter
            }
        }
    }
}