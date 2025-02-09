package aarambh.apps.blinkitcloneadmin.fragments

import aarambh.apps.blinkitcloneadmin.Constants
import aarambh.apps.blinkitcloneadmin.R
import aarambh.apps.blinkitcloneadmin.Utils
import aarambh.apps.blinkitcloneadmin.adapter.AdapterProduct
import aarambh.apps.blinkitcloneadmin.adapter.CategoriesAdapter
import aarambh.apps.blinkitcloneadmin.databinding.EditProductLayoutBinding
import aarambh.apps.blinkitcloneadmin.databinding.FragmentHomeBinding
import aarambh.apps.blinkitcloneadmin.models.Categories
import aarambh.apps.blinkitcloneadmin.models.Product
import aarambh.apps.blinkitcloneadmin.viewmodels.AdminViewModel
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class homeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapterProduct: AdapterProduct
    val viewModel: AdminViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        setStatusBarColor()

        setCategories()

        searchProducts()

        getAllTheProducts("All")
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun searchProducts() {
        binding.searchEt.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString().trim()
                adapterProduct.filter.filter(query)
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
    }

    private fun onCategoryClicked(categories: Categories) {
        getAllTheProducts(categories.category)

    }

    private fun getAllTheProducts(category: String) {
        binding.shimmerViewContainer.visibility = View.VISIBLE
        lifecycleScope.launch {
            viewModel.fetchALlTheProducts(category).collect {
                if (it.isEmpty()) {
                    binding.rvProducts.visibility = View.GONE
                    binding.tvText.visibility = View.VISIBLE
                } else {
                    binding.rvProducts.visibility = View.VISIBLE
                    binding.tvText.visibility = View.GONE
                }
                adapterProduct = AdapterProduct(::onEditButtonClicked)
                binding.rvProducts.adapter = adapterProduct
                adapterProduct.differ.submitList(it)
                adapterProduct.originalList  = it as ArrayList<Product>
                binding.shimmerViewContainer.visibility = View.GONE
            }
        }
    }

    private fun setCategories() {
        val categoryList = ArrayList<Categories>()
        for (i in 0 until Constants.allProductsCategoryIcon.size) {
            categoryList.add(
                Categories(
                    Constants.allProductsCategory[i],
                    Constants.allProductsCategoryIcon[i]
                )
            )
        }

        binding.rvCategories.adapter = CategoriesAdapter(categoryList, ::onCategoryClicked)
    }

    private fun onEditButtonClicked(product: Product) {
        val editProduct = EditProductLayoutBinding.inflate(LayoutInflater.from(requireContext()))
        editProduct.apply {
            etProductTitle.setText(product.productTitle)
            etProductQuantity.setText(product.productQuantity.toString())
            etProductUnit.setText(product.productUnit)
            etProductPrice.setText(product.productPrice.toString())
            etProductStock.setText(product.productStock.toString())
            etProductCategory.setText(product.productCategory)
            etProductType.setText(product.productType)
        }

        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(editProduct.root)
            .create()
        alertDialog.show()

        editProduct.btnEdit.setOnClickListener {
            editProduct.etProductTitle.isEnabled = true
            editProduct.etProductQuantity.isEnabled = true
            editProduct.etProductUnit.isEnabled = true
            editProduct.etProductPrice.isEnabled = true
            editProduct.etProductStock.isEnabled = true
            editProduct.etProductCategory.isEnabled = true
            editProduct.etProductType.isEnabled = true
        }
        setAutoCompleteTextView(editProduct)

        editProduct.btnsaveProduct.setOnClickListener{
            lifecycleScope.launch {
                product.apply {
                    productTitle = editProduct.etProductTitle.text.toString()
                    productQuantity = editProduct.etProductQuantity.text.toString().toInt()
                    productUnit = editProduct.etProductUnit.text.toString()
                    productPrice = editProduct.etProductPrice.text.toString().toInt()
                    productStock = editProduct.etProductStock.text.toString().toInt()
                    productCategory = editProduct.etProductCategory.text.toString()
                    productType = editProduct.etProductType.text.toString()
                    viewModel.saveProduct(product)
                }
            }
            alertDialog.dismiss()
            Utils.showToast(requireContext(),"Products saved Successfully")
        }

    }


    fun setAutoCompleteTextView(editProduct : EditProductLayoutBinding){
            val units = ArrayAdapter(requireContext(), R.layout.show_list, Constants.allUnitsOfProducts)
            val category =
                ArrayAdapter(requireContext(), R.layout.show_list, Constants.allProductsCategory)
            val productType =
                ArrayAdapter(requireContext(), R.layout.show_list, Constants.allProductType)

            editProduct.apply {
                etProductUnit.setAdapter(units)
                etProductCategory.setAdapter(category)
                etProductType.setAdapter(productType)
            }
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