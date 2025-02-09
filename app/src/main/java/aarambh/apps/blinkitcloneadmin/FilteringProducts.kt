package aarambh.apps.blinkitcloneadmin

import aarambh.apps.blinkitcloneadmin.adapter.AdapterProduct
import aarambh.apps.blinkitcloneadmin.models.Product
import android.widget.Filter
import java.util.Locale

class FilteringProducts(
    val adapter : AdapterProduct,
    val filter : ArrayList<Product>
) : Filter(){
    override fun performFiltering(constraint: CharSequence?): FilterResults {
        val result = FilterResults()

        if (constraint.isNullOrEmpty()) {
            // When search is empty, return the full list
            result.values = filter
            result.count = filter.size
        } else {
            // When there's a search query
            val filteredList = ArrayList<Product>()
            val query = constraint.toString().trim().uppercase(Locale.getDefault())

            for (products in filter) {
                if (
                    (products.productTitle?.uppercase(Locale.getDefault()) ?: "").contains(query) ||
                    (products.productCategory?.uppercase(Locale.getDefault()) ?: "").contains(query) ||
                    (products.productPrice?.toString()?.uppercase(Locale.getDefault()) ?: "").contains(query) ||
                    (products.productType?.uppercase(Locale.getDefault()) ?: "").contains(query)
                ) {
                    filteredList.add(products)
                }
            }
            
            result.values = filteredList
            result.count = filteredList.size
        }

        return result
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
        adapter.differ.submitList(results?.values as ArrayList<Product>)


    }

}