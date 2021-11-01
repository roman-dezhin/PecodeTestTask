package com.github.romandezhin.pecodetesttask.ui

import androidx.lifecycle.ViewModel
import com.github.romandezhin.pecodetesttask.DI
import com.github.romandezhin.pecodetesttask.model.FragmentState

class MainViewModel : ViewModel() {
    private var nextValue = DI.getRepository().fetchCount()
        set(value) {
            field = value
            saveState()
        }
    private val items = (1..nextValue++).map{ FragmentState(it) }.toMutableList()
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

    private fun saveState() {
        DI.getRepository().saveCount(nextValue - 1)
    }
}