package com.example.zookeepertest.common.observable

object ObserverBuilder {
    fun <T> buildObserverFromLambda(nextLambda: (value: T) -> Unit): Observer<T> {
        return object : Observer<T> {
            override fun next(value: T) {
                nextLambda.invoke(value)
            }
        }
    }
}