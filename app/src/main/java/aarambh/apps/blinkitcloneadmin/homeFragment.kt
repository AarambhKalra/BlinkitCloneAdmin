package aarambh.apps.blinkitcloneadmin

import aarambh.apps.blinkitcloneadmin.adapter.AdapterProduct
import aarambh.apps.blinkitcloneadmin.adapter.CategoriesAdapter
import aarambh.apps.blinkitcloneadmin.databinding.FragmentHomeBinding
import aarambh.apps.blinkitcloneadmin.models.Categories
import aarambh.apps.blinkitcloneadmin.viewmodels.AdminViewModel
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class homeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    val viewModel: AdminViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        setStatusBarColor()

        setCategories()

        getAllTheProducts("All")
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun onCategoryClicked(categories: Categories) {
        getAllTheProducts(categories.category)

    }

    private fun getAllTheProducts(category: String) {
        lifecycleScope.launch{
            viewModel.fetchALlTheProducts(category).collect{
                if (it.isEmpty()){
                    binding.rvProducts.visibility = View.GONE
                    binding.tvText.visibility = View.VISIBLE
                }else{
                    binding.rvProducts.visibility = View.VISIBLE
                    binding.tvText.visibility = View.GONE
                }
                val adapterProduct = AdapterProduct()
                binding.rvProducts.adapter = adapterProduct
                adapterProduct.differ.submitList(it)
            }
        }
    }

    private fun setCategories() {
        val categoryList =  ArrayList<Categories>()
        for (i in 0 until Constants.allProductsCategoryIcon.size){
            categoryList.add(Categories(Constants.allProductsCategory[i],Constants.allProductsCategoryIcon[i]))
        }

        binding.rvCategories.adapter = CategoriesAdapter(categoryList,::onCategoryClicked)
    }

    private fun setStatusBarColor() {
        activity?.window?.apply {
            statusBarColor = Color.TRANSPARENT // Set the status bar color to transparent
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }
}