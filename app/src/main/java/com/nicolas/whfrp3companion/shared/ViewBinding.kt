package com.nicolas.whfrp3companion.shared

import android.app.Activity
import android.support.annotation.IdRes
import android.support.v7.app.AppCompatActivity
import android.view.View

fun <T : View> Activity.bind(@IdRes idRes: Int): Lazy<T> {
    @Suppress("UNCHECKED_CAST")
    return unsafeLazy { findViewById<T>(idRes) }
}

fun <T : View> AppCompatActivity.bind(@IdRes idRes: Int): Lazy<T> {
    return unsafeLazy { findViewById<T>(idRes) }
}

fun <T : View> View.bind(@IdRes idRes: Int): Lazy<T> {
    @Suppress("UNCHECKED_CAST")
    return unsafeLazy { findViewById<T>(idRes) }
}

fun <T : View> Activity.getView(@IdRes idRes: Int): T {
    @Suppress("UNCHECKED_CAST")
    return findViewById(idRes)
}

fun <T : View> AppCompatActivity.getView(@IdRes idRes: Int): T {
    @Suppress("UNCHECKED_CAST")
    return findViewById(idRes)
}

fun <T : View> View.getView(@IdRes idRes: Int): T {
    @Suppress("UNCHECKED_CAST")
    return findViewById(idRes)
}

private fun <T> unsafeLazy(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)