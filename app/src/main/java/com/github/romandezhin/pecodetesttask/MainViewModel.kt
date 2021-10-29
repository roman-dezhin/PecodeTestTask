package com.github.romandezhin.pecodetesttask

import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private var nextValue = 1
    private val items = mutableListOf(FragmentState(nextValue++))
    val size: Int get() = items.size

    fun getItemById(id: Int): FragmentState = items.first { it.id == id }

    fun itemId(position: Int): Int = items[position].id

    fun lastPosition(): Int = items.lastIndex

    fun contains(itemId: Int): Boolean = items.any { it.id == itemId }

    fun addNew() = items.add(FragmentState(nextValue++))

    fun removeLast() {
        items.removeLast()
        nextValue--
    }

    fun createIdSnapshot(): List<Int> = (0 until size).map { position -> itemId(position) }
}