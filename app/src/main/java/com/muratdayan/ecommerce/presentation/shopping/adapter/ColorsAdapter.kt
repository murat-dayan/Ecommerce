package com.muratdayan.ecommerce.presentation.shopping.adapter

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.muratdayan.ecommerce.databinding.ColorRvItemBinding

class ColorsAdapter : RecyclerView.Adapter<ColorsAdapter.ColorItemHolder>() {

    private var selectedPosition = -1

    inner class ColorItemHolder(val binding: ColorRvItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(color:Int, position: Int){
            val imageDrawable = ColorDrawable(color)
            binding.imageCircleColor.setImageDrawable(imageDrawable)
            if (selectedPosition == position){
                binding.apply {
                    imageCircleShadowColor.visibility = View.VISIBLE
                    ivPickedIcon.visibility = View.VISIBLE
                }
            }else{
                binding.apply {
                    imageCircleShadowColor.visibility = View.INVISIBLE
                    ivPickedIcon.visibility = View.INVISIBLE
                }
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Int>(){
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
           return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorItemHolder {
        return ColorItemHolder(
            ColorRvItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ColorItemHolder, position: Int) {
        val color = differ.currentList[position]
        holder.bind(color,position)

        holder.itemView.setOnClickListener {
            if (selectedPosition >= 0){
                notifyItemChanged(selectedPosition)
            }
            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)
            onItemClick?.invoke(color)
        }
    }

    var onItemClick: ((Int) -> Unit)? = null

}