package com.vision.exercise.vision.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<T : ViewBinding, V : BaseViewModel> : AppCompatActivity() {

    abstract fun getLazyBinding(): Lazy<T>
    abstract fun getLazyViewModel(): Lazy<V>
    abstract fun setupInit()

    protected val binding by getLazyBinding()
    protected val viewModel by getLazyViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupInit()
    }

}
