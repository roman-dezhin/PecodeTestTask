package com.github.romandezhin.pecodetesttask

import android.os.Bundle
import androidx.fragment.app.Fragment

inline fun <reified T : Any?> Fragment.argument(key: String): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) {
        requireArguments().get(key) as T
    }
}

inline fun <T : Fragment> T.withArgs(argsBuilder: Bundle.() -> Unit): T {
    return this.apply {
        arguments = Bundle().apply(argsBuilder)
    }
}