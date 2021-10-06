package com.akshay.playground.cats.view.breedlist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.akshay.playground.cats.R
import com.akshay.playground.cats.common.utils.loadImageFromUrl
import com.akshay.playground.cats.databinding.AdapterItemListBreedBinding
import com.akshay.playground.cats.view.common.BasePagedListAdapter
import com.akshay.playground.catsclient.dto.BreedDto

class BreedListAdapter(private val onClick: (item: BreedDto) -> Unit) :
    BasePagedListAdapter<BreedDto>(breedItemListCallback) {
    private val tag = BreedListAdapter::class.java.simpleName

    companion object {
        val breedItemListCallback = object : DiffUtil.ItemCallback<BreedDto>() {
            override fun areItemsTheSame(
                oldItem: BreedDto,
                newItem: BreedDto
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: BreedDto,
                newItem: BreedDto
            ): Boolean {
                return oldItem == newItem
            }
        }
    }


    override fun bindDataViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position < itemCount) {
            (holder as? BreedItemViewHolder)?.bind(getItem(position))
        } else {
            Log.d(tag, "position out of bounds")
        }
    }

    override fun getDataViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BreedItemViewHolder(
            AdapterItemListBreedBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    inner class BreedItemViewHolder(private val binding: AdapterItemListBreedBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(breedDto: BreedDto?) {
            binding.root.setOnClickListener {
                if (breedDto != null) onClick(breedDto)
            }

            if (breedDto != null) {
                binding.tvBreedName.text = breedDto.name
                binding.tvItemDescription.text = breedDto.description
                breedDto.image?.url.loadImageFromUrl(binding.root.context)
                    .into(binding.ivBreedImage)
            } else {
                binding.ivBreedImage.visibility = View.GONE
                binding.tvItemDescription.text =
                    itemView.context.getString(R.string.default_no_data_value)
            }
        }
    }
}