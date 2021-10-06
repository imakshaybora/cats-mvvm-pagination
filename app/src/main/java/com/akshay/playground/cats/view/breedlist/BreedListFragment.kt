package com.akshay.playground.cats.view.breedlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.akshay.playground.cats.R
import com.akshay.playground.cats.common.ScreenState
import com.akshay.playground.cats.common.utils.formatError
import com.akshay.playground.cats.view.common.BaseListFragment
import com.akshay.playground.cats.view.common.BasePagedListAdapter
import com.akshay.playground.cats.viewmodel.BreedListViewModel
import com.akshay.playground.cats.viewmodel.BreedListViewModelFactory
import com.akshay.playground.catsclient.dto.BreedDto
import com.akshay.playground.catsclient.repository.CatsRetrofitRepoFactory

class BreedListFragment : BaseListFragment<BreedDto>() {

    private val viewModel: BreedListViewModel by viewModels {
        BreedListViewModelFactory(CatsRetrofitRepoFactory.create())
    }
    private var breedListAdapter: BreedListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.state.observe(::getLifecycle, ::updateUi)
    }

    private fun updateUi(screenState: ScreenState<BreedListState>?) {
        when (screenState) {
            ScreenState.Loading -> {
                showLoadingState()
            }
            is ScreenState.Render -> handleScreenRenderState(screenState.renderState)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        breedListAdapter = BreedListAdapter {
            // on item click callback will come here
        }
        binding?.rvList?.adapter = breedListAdapter
        viewModel.dataList.observe(viewLifecycleOwner) { breedListAdapter?.submitList(it) }
        setTitle(getString(R.string.app_name), getString(R.string.breed_listing))
        viewModel.dispatchEvent(BreedListEvent.OnScreenLoaded)
    }

    private fun handleScreenRenderState(renderState: BreedListState) {
        binding?.pbLoading?.visibility = View.GONE
        when (renderState) {
            is BreedListState.ApiErrorState -> showError(
                context.formatError(renderState.errorWrapper)
            )

            is BreedListState.ApiErrorStateWhenDataAvailable -> showSnackBar(
                context.formatError(renderState.errorWrapper)
            )
            is BreedListState.EmptyData -> showEmptyState(getString(R.string.no_data_found))
            is BreedListState.EndOfList -> breedListAdapter?.setState(BasePagedListAdapter.State.DONE)
            is BreedListState.LoadingMore -> breedListAdapter?.setState(BasePagedListAdapter.State.LOADING)
            is BreedListState.UpdateData -> {
                if (binding?.rvList?.visibility != View.VISIBLE) {
                    binding?.rvList?.visibility = View.VISIBLE
                }
                breedListAdapter?.setState(BasePagedListAdapter.State.DONE)
            }
        }
    }
}