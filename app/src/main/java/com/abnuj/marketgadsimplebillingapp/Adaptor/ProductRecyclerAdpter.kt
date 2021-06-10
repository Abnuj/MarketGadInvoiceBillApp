package com.abnuj.marketgadsimplebillingapp.Adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.abnuj.marketgadsimplebillingapp.R

class ProductRecyclerAdpter : ListAdapter<ProductModel, ProductRecyclerAdpter.viewholder>(ProductDiffUtis()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductRecyclerAdpter.viewholder {
        return viewholder(LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false))
    }

    override fun onBindViewHolder(holder: ProductRecyclerAdpter.viewholder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }


    class viewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvsi = itemView.findViewById<TextView>(R.id.rtvs)
        val tvprice = itemView.findViewById<TextView>(R.id.rtvprice)
        val imgitem = itemView.findViewById<ImageView>(R.id.rimgitem)

        fun bind(currentItem: ProductModel?) {
            tvsi.text = currentItem?.serielNumber.toString()
            tvprice.setText("${itemView.resources.getString(R.string.ruppes)} ${currentItem?.price.toString()}")
            imgitem.setImageBitmap(currentItem?.itemImage)
        }


    }


    class ProductDiffUtis : DiffUtil.ItemCallback<ProductModel>() {
        override fun areItemsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean {
            return oldItem.serielNumber == newItem.serielNumber
        }

        override fun areContentsTheSame(oldItem: ProductModel, newItem: ProductModel): Boolean {
            return oldItem == newItem
        }

    }

}