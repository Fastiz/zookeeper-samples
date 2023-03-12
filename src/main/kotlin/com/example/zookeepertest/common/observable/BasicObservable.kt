package com.example.zookeepertest.common.observable

import java.util.*

class BasicObservable<T> : Observable<T> {
    private val observers = mutableMapOf<UUID, Observer<T>>()

    override fun register(observer: Observer<T>): UnregisterCallback {
        val id = UUID.randomUUID()
        observers[id] = observer
        return {
            observers.remove(id)
        }
    }

    fun getObserversIterator(): Iterator<Observer<T>> {
        return observers.map { it.value }.iterator()
    }
}
