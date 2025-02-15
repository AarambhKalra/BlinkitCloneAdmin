package aarambh.apps.blinkitcloneadmin.adapter

import aarambh.apps.blinkitcloneadmin.FilteringProducts
import aarambh.apps.blinkitcloneadmin.databinding.ItemViewProductBinding
import aarambh.apps.blinkitcloneadmin.models.Product
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.models.SlideModel

class AdapterProduct(val onEditButtonClicked: (Product) -> Unit) : RecyclerView.Adapter<AdapterProduct.ProductViewHolder>(),Filterable{
    class ProductViewHolder (val binding: ItemViewProductBinding): RecyclerView.ViewHolder(binding.root){
    }

    val diffUtil = object : DiffUtil.ItemCallback<Product>(){
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.productRandomId == newItem.productRandomId
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this,diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(ItemViewProductBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    override fun getItemCount(): Int {
        return differ.currentList.size

    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        val product = differ.currentList[position]


        holder.binding.apply {

            val imageList = ArrayList<SlideModel>()

            val productImage = product.productImageUris

            if (productImage != null) {
                for (i in 0 until productImage.size) {
                    imageList.add(SlideModel(product.productImageUris?.get(i).toString()))
                }
            }
            ivImageSlider.setImageList(imageList)
            tvProductTitle.text = product.productTitle
            val quantity = product.productQuantity.toString() + " " + product.productUnit
            tvProductQuantity.text = quantity

            tvProductPrice.text = "₹" + product.productPrice.toString()

        }

        holder.itemView.setOnClickListener{
            onEditButtonClicked(product)

        }

        // If this is the last item, reset the filter
        if (position == differ.currentList.size - 1) {
            filter = null
        }

    }

    var originalList = ArrayList<Product>()


    private var filter: FilteringProducts? = null

    override fun getFilter(): FilteringProducts {
        if(filter == null){
            return FilteringProducts(this,originalList)
        }
        return filter as FilteringProducts

    }

}