package com.akshay.playground.cats.view.common

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar


/**
 * Base Fragment class for Cats app
 */
abstract class BaseFragment : Fragment() {

    private val mOnBackPressedCallback: OnBackPressedCallback by lazy {
        initOnBackPressedCallback()
    }

    private fun initOnBackPressedCallback(): OnBackPressedCallback {
        return object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onFragmentBackPressed()
            }
        }
    }

    /**
     * This callback will be triggered on back press
     * provided we enable the mOnBackPressedCallback.enabled
     * Consumer can override this to handle back press scenarios
     */
    open fun onFragmentBackPressed() {
        mOnBackPressedCallback.isEnabled = false
    }

    protected open fun hideSupportActionBar() = false

    private fun setActionbarVisibility(isHidden: Boolean) {
        if (isHidden) {
            getAppCompatActivity()?.supportActionBar?.hide()
        } else {
            getAppCompatActivity()?.supportActionBar?.show()
        }
    }

    private fun getAppCompatActivity(): AppCompatActivity? {
        return if (activity is AppCompatActivity) {
            (activity as AppCompatActivity)
        } else null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            onNavigateHomeIconClick()
            handleNavigationHomeClick()
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentConfigurations()
    }

    protected open fun handleBackKey() = false

    protected open fun handleNavigationHomeClick() = false


    /**
     * This can be Overridden in child class to give its own functionality
     */
    open fun onNavigateHomeIconClick() {}

    private fun setFragmentConfigurations() {
        setActionbarVisibility(hideSupportActionBar())
        if (handleBackKey()) {
            activity?.onBackPressedDispatcher?.addCallback(this, mOnBackPressedCallback)
        }
        if (handleNavigationHomeClick()) {
            setHasOptionsMenu(true)
        }
    }

    protected fun setTitle(title: String, subtitle: String? = null) {
        getAppCompatActivity()?.supportActionBar?.title = title
        getAppCompatActivity()?.supportActionBar?.subtitle = subtitle
    }

    protected fun showSnackBar(text: String, snackBarLength: Int = Snackbar.LENGTH_LONG) {
        view?.let {
            Snackbar.make(
                it,
                text,
                snackBarLength
            ).show()
        }
    }
}