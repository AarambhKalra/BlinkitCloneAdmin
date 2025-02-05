package aarambh.apps.blinkitcloneadmin.fragments


import aarambh.apps.blinkitcloneadmin.Constants
import aarambh.apps.blinkitcloneadmin.R
import aarambh.apps.blinkitcloneadmin.Utils
import aarambh.apps.blinkitcloneadmin.activity.AdminMainActivity
import aarambh.apps.blinkitcloneadmin.adapter.AdapterSelectedImage
import aarambh.apps.blinkitcloneadmin.databinding.FragmentAddProductBinding
import aarambh.apps.blinkitcloneadmin.models.Product
import aarambh.apps.blinkitcloneadmin.viewmodels.AdminViewModel
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch


class addProductFragment : Fragment() {
    private val viewModel: AdminViewModel by viewModels()
    lateinit var binding: FragmentAddProductBinding
    private val imageUris: ArrayList<Uri> = arrayListOf()
    val selectedImage =
        registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { listOfUri ->
            val fiveImages = listOfUri.take(5)
            imageUris.clear()
            imageUris.addAll(fiveImages)

            binding.rvProductImages.adapter = AdapterSelectedImage(imageUris)
        }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddProductBinding.inflate(layoutInflater)
        setStatusBarColor()

        setAutoCompleteTextView()

        onImageSelectClick()

        onAddButtonClicked()

        // Inflate the layout for this fragment
        return binding.root
    }


    private fun onAddButtonClicked() {
        binding.btnAddProduct.setOnClickListener() {
            Utils.showDialog(requireContext(), "Uploading Images")
            val productTitle = binding.etProductTitle.text.toString()
            val productQuantity = binding.etProductQuantity.text.toString()
            val productUnit = binding.etProductUnit.text.toString()
            val productPrice = binding.etProductPrice.text.toString()
            val productStock = binding.etProductStock.text.toString()
            val productCategory = binding.etProductCategory.text.toString()
            val productType = binding.etProductType.text.toString()

            if (productTitle.isEmpty() || productQuantity.isEmpty() || productUnit.isEmpty() || productPrice.isEmpty() || productStock.isEmpty() || productCategory.isEmpty() || productType.isEmpty()) {
                Utils.hideDialog()
                Utils.showToast(requireContext(), "Please fill all the fields")
            } else if (imageUris.isEmpty()) {
                Utils.hideDialog()
                Utils.showToast(requireContext(), "Please upload some images")
            } else {
                val product = Product(
                    productTitle = productTitle,
                    productQuantity = productQuantity.toInt(),
                    productUnit = productUnit,
                    productPrice = productPrice.toInt(),
                    productStock = productStock.toInt(),
                    productCategory = productCategory,
                    productType = productType,
                    itemCount = 0,
                    adminUid = Utils.getCurrentUserId(),
                    productRandomId = Utils.getRandomId()
                )

                saveImage(product)

            }


        }
    }

    private fun saveProduct(product: Product){
        Utils.showDialog(requireContext(), "Saving Product")
        viewModel.saveProduct(product)
        lifecycleScope.launch {
            viewModel.isProductSaved.collect {
                if (it) {
                    Utils.hideDialog()
                    startActivity(Intent(requireContext(), AdminMainActivity::class.java))
                    Utils.showToast(requireContext(), "Your Product is Live")

                }
            }
        }

    }

    private fun getUrls(product:Product) {
        lifecycleScope.launch {
            viewModel.downloadedUrls.collect {urls->
                product.productImageUris = urls
                saveProduct(product)
            }
        }
    }

    private fun saveImage(product: Product) {
        viewModel.saveImageInDB(imageUris)
        lifecycleScope.launch{
            viewModel.imagesUploaded.collect{
                if(it){
                    Utils.hideDialog()
                    Utils.showToast(requireContext(), "Image Saved Successfully")
                }
                getUrls(product)
            }
        }
    }

    private fun onImageSelectClick() {
        binding.btnSelectImage.setOnClickListener {
            selectedImage.launch("image/*")

        }
    }

    private fun setAutoCompleteTextView() {
        val units = ArrayAdapter(requireContext(), R.layout.show_list, Constants.allUnitsOfProducts)
        val category =
            ArrayAdapter(requireContext(), R.layout.show_list, Constants.allProductsCategory)
        val productType =
            ArrayAdapter(requireContext(), R.layout.show_list, Constants.allProductType)

        binding.apply {
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