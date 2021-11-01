package com.github.romandezhin.pecodetesttask.ui.utils

import java.util.concurrent.atomic.AtomicInteger

object NotificationID {
    private val c: AtomicInteger = AtomicInteger(0)
    val ID: Int
        get() = c.incrementAndGet()
}