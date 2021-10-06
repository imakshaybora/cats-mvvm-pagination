package com.akshay.playground.cats.view.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.akshay.playground.cats.databinding.AdapterFooterListCatsBinding

abstract class BasePagedListAdapter<T>(
    diffCallback: DiffUtil.ItemCallback<T>
) : PagedListAdapter<T, RecyclerView.ViewHolder>(diffCallback) {

    private var state = State.LOADING

    companion object {
        private const val DATA_VIEW_TYPE = 1
        private const val FOOTER_VIEW_TYPE = 2
    }

    final override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return if (viewType == DATA_VIEW_TYPE)
            getDataViewHolder(parent, viewType)
        else ListFooterViewHolder(
            AdapterFooterListCatsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    final override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DATA_VIEW_TYPE)
            bindDataViewHolder(holder, position)
        else
            (holder as BasePagedListAdapter<*>.ListFooterViewHolder).bind(state)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) DATA_VIEW_TYPE else FOOTER_VIEW_TYPE
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0 && (state == State.LOADING || state == State.ERROR)
    }

    fun setState(state: State) {
        this.state = state
        notifyItemChanged(super.getItemCount())
    }

    abstract fun bindDataViewHolder(holder: RecyclerView.ViewHolder, position: Int)

    abstract fun getDataViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder

    inner class ListFooterViewHolder(private val binding: AdapterFooterListCatsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(status: State?) {
            binding.progressBar.visibility =
                if (status == State.LOADING) View.VISIBLE else View.INVISIBLE
            binding.txtError.visibility =
                if (status == State.ERROR) View.VISIBLE else View.INVISIBLE
            binding.txtError.visibility =
                if (status == State.DONE) View.INVISIBLE else View.INVISIBLE
        }
    }

    enum class State {
        LOADING, ERROR, DONE
    }
}