package com.github.romandezhin.pecodetesttask

import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private var nextValue = 1
    private val items = mutableListOf(FragmentState(nextValue++))
    val size: Int get() = items.size

    fun itemId(position: Int): Int = items[position].number

    fun lastPosition(): Int = items.lastIndex

    fun contains(itemId: Int): Boolean = items.any { it.number == itemId }

    fun addNew() = items.add(FragmentState(nextValue++))

    fun removeLast() {
        items.removeLast()
        nextValue--
    }

    fun createIdSnapshot(): List<Int> = (0 until size).map { position -> itemId(position) }

    fun addNotification(number: Int, id: Int) {
        items[number - 1].addNotification(id)
    }

    fun notificationIds(number: Int) = items[number].setOfIds()
}