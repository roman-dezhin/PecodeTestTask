package com.github.romandezhin.pecodetesttask

import android.app.Application
import com.github.romandezhin.pecodetesttask.data.Repository

object DI {
    private var repository: Repository? = null

    fun init(app: Application) {
        repository = Repository(app)
    }

    fun getRepository(): Repository = repository!!
}