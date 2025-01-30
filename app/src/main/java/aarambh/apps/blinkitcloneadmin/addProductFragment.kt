package aarambh.apps.blinkitcloneadmin


import aarambh.apps.blinkitcloneadmin.adapter.AdapterSelectedImage
import aarambh.apps.blinkitcloneadmin.databinding.FragmentAddProductBinding
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


class addProductFragment : Fragment() {

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

        // Inflate the layout for this fragment
       return binding.root
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