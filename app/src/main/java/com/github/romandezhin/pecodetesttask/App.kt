package com.github.romandezhin.pecodetesttask

import android.app.Application

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        DI.init(this)
    }
}