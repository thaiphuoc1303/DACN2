package com.vision.exercise.vision.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<T : ViewBinding, V : BaseViewModel> : Fragment() {
    var isCreated = false

    abstract fun getLazyBinding(): Lazy<T>
    abstract fun getLazyViewModel(): Lazy<V>
    abstract fun setupInit()

    protected val binding by getLazyBinding()
    protected val viewModel by getLazyViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCreated = true
        setupInit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isCreated = false
    }
}
