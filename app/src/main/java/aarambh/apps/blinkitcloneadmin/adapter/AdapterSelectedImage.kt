package aarambh.apps.blinkitcloneadmin.adapter

import aarambh.apps.blinkitcloneadmin.databinding.ItemViewImageSelectionBinding
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class AdapterSelectedImage(val imageUris: ArrayList<Uri>) : RecyclerView.Adapter<AdapterSelectedImage.SelectedImageViewHolder>() {
    class SelectedImageViewHolder(val binding: ItemViewImageSelectionBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedImageViewHolder {
        return SelectedImageViewHolder(ItemViewImageSelectionBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return imageUris.size
    }

    override fun onBindViewHolder(holder: SelectedImageViewHolder, position: Int) {
        val image = imageUris[position]
        holder.binding.apply {
            ivImage.setImageURI(image)
        }

        holder.binding.closeButton.setOnClickListener {
            if(position<imageUris.size) {
                imageUris.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, imageUris.size)
            }
        }
    }
}