package com.github.romandezhin.pecodetesttask.data

import android.app.Application

class Repository(app: Application) {
    private val preferences = app.getSharedPreferences(PREF_FILE_NAME, Application.MODE_PRIVATE)

    fun fetchCount(): Int = preferences.getInt(KEY_COUNT, 1)

    fun saveCount(count: Int) {
        with(preferences.edit()) {
            putInt(KEY_COUNT, count)
            apply()
        }
    }

    companion object {
        private const val PREF_FILE_NAME = "pref"
        private const val KEY_COUNT = "count"
    }
}