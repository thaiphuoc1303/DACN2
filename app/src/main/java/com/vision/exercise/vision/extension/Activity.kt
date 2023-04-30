package com.vision.exercise.vision.extension

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction

internal fun FragmentActivity.replaceFragment(
    @IdRes containerId: Int, fragment: Fragment,
    t: (transaction: FragmentTransaction) -> Unit = {},
    isAddBackStack: Boolean = false
) {
    if (supportFragmentManager.findFragmentByTag(fragment.javaClass.simpleName) == null) {
        val transaction = supportFragmentManager.beginTransaction()
        t.invoke(transaction)
        transaction.replace(containerId, fragment, fragment.javaClass.simpleName)
        if (isAddBackStack) {
            transaction.addToBackStack(fragment.javaClass.simpleName)
        }
        transaction.commitAllowingStateLoss()
    }
}

internal fun FragmentActivity.addFragment(
    @IdRes containerId: Int, fragment: Fragment,
    t: (transaction: FragmentTransaction) -> Unit = {}, backStackString: String? = null
) {
    if (supportFragmentManager.findFragmentByTag(fragment.javaClass.simpleName) == null) {
        val transaction = supportFragmentManager.beginTransaction()
        t.invoke(transaction)
        transaction.add(containerId, fragment, fragment.javaClass.simpleName)
        if (backStackString != null) {
            transaction.addToBackStack(backStackString)
        }
        transaction.commitAllowingStateLoss()
        supportFragmentManager.executePendingTransactions()
    }
}
