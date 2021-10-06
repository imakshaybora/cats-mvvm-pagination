package com.akshay.playground.cats.view.common

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akshay.playground.cats.R
import com.akshay.playground.cats.common.GridSpaceDecoration
import com.akshay.playground.cats.databinding.FragmentBaseListBinding
import java.util.concurrent.CopyOnWriteArrayList

abstract class BaseListFragment<T> : BaseFragment(), BaseListInterface {

    protected val mList: CopyOnWriteArrayList<T> = CopyOnWriteArrayList()

    protected var mOnItemClick: ((T) -> Unit)? = null

    private var _binding: FragmentBaseListBinding? = null
    protected val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBaseListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.rvList?.setHasFixedSize(true)
        binding?.rvList?.layoutManager = getLayoutManager(context)
        binding?.rvList?.itemAnimator = null

        if (hasDividers()) {
            addDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
        if (hasGridDecoration()) {
            addDecoration(
                GridSpaceDecoration(resources.getDimensionPixelSize(R.dimen.space_xsmall))
            )
        }
    }

    private fun addDecoration(ItemDecoration: RecyclerView.ItemDecoration) {
        binding?.rvList?.addItemDecoration(ItemDecoration)
    }

    protected fun showError(errorMessage: String) {
        binding?.stubErrorStateLayout?.root?.visibility = View.VISIBLE
        binding?.stubErrorStateLayout?.tvErrorMsg?.text = errorMessage
        binding?.pbLoading?.visibility = View.GONE
        binding?.stubEmptyStateLayout?.root?.visibility = View.GONE
        binding?.rvList?.visibility = View.GONE
    }

    protected fun showEmptyState(emptyMessage: String) {
        binding?.stubEmptyStateLayout?.root?.visibility = View.VISIBLE
        binding?.stubEmptyStateLayout?.tvEmptyStateMessage?.text = emptyMessage
        binding?.pbLoading?.visibility = View.GONE
        binding?.stubErrorStateLayout?.root?.visibility = View.GONE
        binding?.rvList?.visibility = View.GONE
    }

    protected fun showLoadingState() {
        binding?.pbLoading?.visibility = View.VISIBLE
        binding?.stubEmptyStateLayout?.root?.visibility = View.GONE
        binding?.stubErrorStateLayout?.root?.visibility = View.GONE
        binding?.rvList?.visibility = View.GONE
    }

    fun setOnItemClickListener(onItemClick: (T) -> Unit) {
        this.mOnItemClick = onItemClick
    }

    override fun onDestroyView() {
        binding?.rvList?.recycledViewPool?.clear()
        binding?.rvList?.adapter = null
        this.mOnItemClick = null
        _binding = null
        super.onDestroyView()
    }
}

interface BaseListInterface {

    fun setHeader(): String? = null

    fun getLayoutManager(context: Context?): RecyclerView.LayoutManager =
        LinearLayoutManager(context)

    fun hasDividers(): Boolean = true

    fun hasGridDecoration(): Boolean = false
}


/*
*
* View -> viemodel -> Repo -> db
*
* */