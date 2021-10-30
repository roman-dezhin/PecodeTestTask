package com.github.romandezhin.pecodetesttask

class FragmentState(val number: Int) {
    private val notifications = mutableSetOf<Int>()

    fun addNotification(id: Int) {
        notifications.add(id)
    }

    fun setOfIds(): Set<Int> = notifications.toSet()
}