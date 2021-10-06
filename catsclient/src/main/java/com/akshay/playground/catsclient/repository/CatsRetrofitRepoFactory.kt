package com.akshay.playground.catsclient.repository

object CatsRetrofitRepoFactory {

    /**
     * This factory can be used to create different system Repo based on config flag which can be checked here
     */
    fun create(): CatsRepository {
        return CatsRetrofitRepository()
    }
}
